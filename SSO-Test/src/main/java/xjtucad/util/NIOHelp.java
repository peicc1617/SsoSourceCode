package xjtucad.util;

import xjtucad.model.Data;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.List;

public class NIOHelp {
    boolean readLength = true;

    private final static ByteBuffer lengthByteBuffer = ByteBuffer.wrap(new byte[4]);
    private  ByteBuffer dataByteBuffer = null;
    public static void socketChannelWrite(List<Data> dataList, SocketChannel socketChannel) throws IOException {
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        //第一个数字代表数据的长度
        for (int i = 0; i < 4; i++) {
            baos.write(0);
        }
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(dataList);
        oos.close();
        final ByteBuffer wrap =ByteBuffer.wrap(baos.toByteArray());
        wrap.putInt(0,baos.size()-4);
        socketChannel.write(wrap);
    }

    public static List<Data> socketChannelRead(SelectionKey sKey) throws IOException, ClassNotFoundException {
        SocketChannel socketChannel = (SocketChannel) sKey.channel();
        List<Data> dataList = null;
        ByteBuffer dataByteBuffer = (ByteBuffer) sKey.attachment();
        if(dataByteBuffer==null) {
            socketChannel.read(lengthByteBuffer);
            if (lengthByteBuffer.remaining() == 0) {
                dataByteBuffer = ByteBuffer.allocate(lengthByteBuffer.getInt(0));
                lengthByteBuffer.clear();
                sKey.attach(dataByteBuffer);
            }
        }else {
            socketChannel.read(dataByteBuffer);
            if(dataByteBuffer.remaining()==0){
                ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(dataByteBuffer.array()));
                dataList = (List<Data>)ois.readObject();
                sKey.attach(null);
            }
        }
        return dataList;
    }
}