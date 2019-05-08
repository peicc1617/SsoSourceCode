package xjtucad.manager;

import xjtucad.model.Data;
import xjtucad.model.SSOConf;
import xjtucad.util.NIOHelp;

import java.io.IOException;
import java.net.BindException;
import java.net.InetSocketAddress;
import java.nio.channels.*;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;

public class ServerTokenManager extends ClientTokenManager{
    SSOConf serverConf = null;
    static int changNum = 0;
    Selector sel = null;
    private List<SocketChannel> channels= new ArrayList<>();
    private SelectionKey serverKey;

    ArrayBlockingQueue<Data> dataQueue= new ArrayBlockingQueue<>(1000);

    public ServerTokenManager( SSOConf conf) {
        this.serverConf = conf;
        //开启NIO SOCKET线程
        new Thread(new Sync(dataQueue)).start();
        //开启消息监听线程
        new Thread(new SyncListen(dataQueue)).start();
    }

    @Override
    public Map<String, String> getUserInfo(String token) {
        return super.getUserInfo(token);
    }

    @Override
    public void storeUserInfo(Map<String, String> userInfo, String token) {
        tokenMap.put(token,userInfo);
        sendMessage(new Data(Data.OP_UPDATE,userInfo,token));
        super.storeUserInfo(userInfo, token);
    }

    @Override
    public boolean containToken(String token) {
        return super.containToken(token);
    }

    @Override
    protected void sendMessage(Data data) {
        dataQueue.add(data);
    }

    @Override
    public void removeUserInfo(String token) {
        sendMessage(new Data(Data.OP_DELETE,null,token));
        super.removeUserInfo(token);
    }

    class SyncListen implements Runnable{
        private ArrayBlockingQueue<Data> dataQueue;

        public SyncListen(ArrayBlockingQueue<Data> dataQueue) {
            this.dataQueue = dataQueue;
        }

        @Override
        public void run() {
            while (true){
                Data data = null;
                try {
                    data = dataQueue.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                for (SocketChannel channel : channels) {
                    try {
                        channel.register(sel,SelectionKey.OP_WRITE,data);
                    } catch (ClosedChannelException e) {
                        e.printStackTrace();
                        channels.remove(channel);
                    }
                }
                sel.wakeup();
            }
        }
    }
    class Sync implements Runnable {
        private ArrayBlockingQueue<Data> dataQueue;
        public Sync(ArrayBlockingQueue<Data> dataQueue) {
            this.dataQueue = dataQueue;
        }


        @Override
        public void run() {
            ServerSocketChannel ssc = null;
            try {
//            新建NIO通道对象
                ssc = ServerSocketChannel.open();
//            设置通道为非阻塞
                ssc.configureBlocking(false);
//            设置端口号
                ssc.bind(new InetSocketAddress(serverConf.getPort()));
//           新建选择器
                if (sel == null) sel = Selector.open();
                System.out.println("开启统一认证服务，端口号为:" + serverConf.getPort());
//将NIO通道选绑定到择器,当然绑定后分配的主键为skey
                serverKey = ssc.register(sel, SelectionKey.OP_ACCEPT);
                while (true) {
                    Set<SelectionKey> keySet = null;
                    try {
//                    获取通道内是否有选择器的关心事件
                        sel.select();
//                    获取通道内关心事件的集合
                        keySet = sel.selectedKeys();
                    } catch (Exception e) {
                        e.printStackTrace();
                        break;
                    }
                    for (Iterator<SelectionKey> it = keySet.iterator(); it.hasNext(); ) {
                        SelectionKey sKey = it.next();
                        it.remove();
                        try {
                            if (sKey.isAcceptable()) {
                                ServerSocketChannel serChannel = (ServerSocketChannel) sKey.channel();
                                SocketChannel clientChannel = serChannel.accept();
                                System.out.println("接收到客户端连接，来自：" + clientChannel.socket().getInetAddress() + ":"
                                        + clientChannel.socket().getPort());
                                channels.add(clientChannel);
                                clientChannel.configureBlocking(false);
                                List<Data> dataList = new ArrayList<>();
                                List<String> tokenList= new ArrayList<>(tokenMap.keySet());
                                for (String token : tokenList) {
                                    dataList.add(new Data(Data.OP_UPDATE, tokenMap.get(token), token));
                                }
                                NIOHelp.socketChannelWrite(dataList, clientChannel);
                                clientChannel.register(sel, SelectionKey.OP_READ);
                            } else if (sKey.isValid() && sKey.isWritable()) {
                                SocketChannel socketChannel = (SocketChannel) sKey.channel();
                                Data data = (Data) sKey.attachment();
                                NIOHelp.socketChannelWrite(Collections.singletonList(data), socketChannel);
                                sKey.attach(null);
                                sKey.interestOps(SelectionKey.OP_READ);
                            } else if (sKey.isValid() && sKey.isReadable()) {
                                List<Data> list = NIOHelp.socketChannelRead(sKey);
                                if (list != null) {
                                    System.out.println("客户端发来的数据:" + list.toString());
                                    recMessage(list);
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            SocketChannel socketChannel = (SocketChannel) sKey.channel();
                            sKey.cancel();
                            channels.remove(socketChannel);
                            socketChannel.close();
                            System.out.println("客户端断开了链接:" + socketChannel.socket().getInetAddress() + ":"
                                    + socketChannel.socket().getPort());
                        }
                    }

                }
            }catch (BindException e){
                System.out.println("端口被占用，请确认端口使用情况，端口号：" + serverConf.getPort());
                System.out.println("统一登录服务器启动失败，可以重启Tomcat尝试再次启动");
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                System.out.println("退出统一登录");
                if (ssc != null) {
                    try {
                        ssc.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

}
