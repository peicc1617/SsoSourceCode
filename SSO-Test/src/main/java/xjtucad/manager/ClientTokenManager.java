package xjtucad.manager;

import xjtucad.model.Data;
import xjtucad.model.SSOConf;
import xjtucad.util.NIOHelp;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.*;

public class ClientTokenManager  extends StandTokenManger{

    SSOConf clientConf = null;
    private SocketChannel clientChannel =null;
    private  Selector sel = null;

    public ClientTokenManager() {

    }

    public ClientTokenManager(SSOConf clientConf) throws IOException {
        this.clientConf = clientConf;
       new Thread(new Sync()).start();
    }
    @Override
    public Map<String, String> getUserInfo(String token) {
        Map<String,String> userInfo = super.getUserInfo(token);
        sendMessage(new Data(Data.OP_UPDATE,userInfo,token));
        return super.getUserInfo(token);
    }

    @Override
    public boolean containToken(String token) {
        if(super.containToken(token)){
            Map<String,String> userInfo = super.getUserInfo(token);
            sendMessage(new Data(Data.OP_UPDATE,userInfo,token));
            return true;
        }
        return false;
    }

    @Override
    public void removeUserInfo(String token) {
        Map<String,String> userInfo = super.getUserInfo(token);
        sendMessage(new Data(Data.OP_DELETE,userInfo,token));
        super.removeUserInfo(token);
    }

    protected void recMessage(List<Data> dataList){
        for (Data data : dataList) {
            if(data.getMethod()== Data.OP_UPDATE){
                tokenMap.put(data.getToken(),data.getUserInfo());
            }else if(data.getMethod()== Data.OP_DELETE){
                tokenMap.remove(data.getToken());
            }
        }
    }

    protected void sendMessage(Data data){
        try {
            clientChannel.register(sel,SelectionKey.OP_WRITE,data);
            sel.wakeup();
        } catch (ClosedChannelException e) {
            e.printStackTrace();
        }
    }


    class Sync implements Runnable{

        @Override
        public void run() {
            SocketChannel channel = null;
            try {
//            新建NIO通道对象
                channel = SocketChannel.open();
//            设置通道为非阻塞
                channel.configureBlocking(false);
//            设置端口号
                channel.connect(new InetSocketAddress(clientConf.getRemoteHost(),clientConf.getPort()));
//           新建选择器
                sel = Selector.open();
                System.out.println("中央认证服务器的地址为:"+clientConf.getRemoteHost());
//将NIO通道选绑定到择器,当然绑定后分配的主键为skey
                channel.register(sel, SelectionKey.OP_CONNECT);
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
                    for (Iterator<SelectionKey> it = keySet.iterator(); it.hasNext();) {
                        SelectionKey sKey = it.next();
                        it.remove();
                        try {
                            if (sKey.isConnectable()) {
                                SocketChannel socketChannel = (SocketChannel) sKey.channel();
                                if(socketChannel.isConnectionPending()){
                                    socketChannel.finishConnect();
                                    clientChannel=socketChannel;
                                }
                                System.out.println("成功链接服务器:"+socketChannel.socket().getInetAddress() + ":"
                                        + socketChannel.socket().getPort());
                                socketChannel.register(sel, SelectionKey.OP_READ);
                            } else if (sKey.isWritable()) {
                                SocketChannel socketChannel = (SocketChannel) sKey.channel();
                                Data data= (Data) sKey.attachment();
                                NIOHelp.socketChannelWrite(Collections.singletonList(data),socketChannel);
                                sKey.attach(null);
                                sKey.interestOps(SelectionKey.OP_READ);
                            } else if (sKey.isReadable()) {
                                List<Data> list = NIOHelp.socketChannelRead(sKey);
                                if(list!=null){
                                    System.out.println("服务器发来的数据:" + list.toString());
                                    recMessage(list);
                                }
                            }
                        } catch (Exception e) {
                            sKey.cancel();
                            e.printStackTrace();
                        }
                    }

                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (channel != null) {
                    try {
                        channel.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
