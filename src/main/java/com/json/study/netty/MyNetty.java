package com.json.study.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.CharsetUtil;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;

/**
 * netty实现 客户端
 * netty实现 服务端
 *
 *
 * nc -l
 * nc
 *
 */

public class MyNetty {

    public static void main(String[] args) {
        System.out.println(1);
    }

    public void clientMode() throws InterruptedException {
        NioEventLoopGroup thread = new NioEventLoopGroup();//一个多路复用器
        //客户端
        NioSocketChannel client = new NioSocketChannel();
        thread.register(client);  //epoll_ctl(5,ADD,3)  多路复用器注册   Channel.register(selector,……
        //响应式，先都定义好
        ChannelPipeline pipeline = client.pipeline();
        pipeline.addLast(new MyInHandler());//处理都注册到这里面

        ChannelFuture connect = client.connect(new InetSocketAddress("127.0.0.1", 9091));
        ChannelFuture sync = connect.sync();//是连接异步的，等连接完成再往下走

        ByteBuf buf = Unpooled.copiedBuffer("hello im connected".getBytes());//简写
        ChannelFuture send = client.writeAndFlush(buf);
        send.sync();

        sync.channel().closeFuture().sync();//等待结束
        System.out.println("client connect closed");
    }

}

class MyInHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {//注册到多路复用器上时触发
        System.out.println("client registered ");
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("@@@@@@@@@@@@@@@@@@@client active...");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {//可读时触发是读取
        ByteBuf buf = (ByteBuf) msg;
        //        CharSequence str = buf.readCharSequence(buf.readableBytes(), CharsetUtil.UTF_8);
        CharSequence str = buf.getCharSequence(0,buf.readableBytes(), CharsetUtil.UTF_8);
        System.out.println(str);
        ctx.writeAndFlush(buf);
    }
}
