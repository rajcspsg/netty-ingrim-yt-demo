package yt.ingrim.p01;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import java.nio.charset.StandardCharsets;

public class Server {
    public static class MySimpleChannelInboundHandler extends SimpleChannelInboundHandler<Object> {
        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            super.channelActive(ctx);
            System.out.println("channel Connected --> " + ctx.channel());
        }

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {

        }
    }

    public static class NetworkHandler extends SimpleChannelInboundHandler<String> {

        private Channel channel;
        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            super.channelActive(ctx);
            this.channel = ctx.channel();
        }

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
            if(msg.equalsIgnoreCase("exit")) {
                channel.close();
            } else if(msg.equalsIgnoreCase("test"))
                channel.writeAndFlush("echo");
            System.out.println(msg);
        }

        @Override
        public void channelInactive(ChannelHandlerContext ctx) throws Exception {
            super.channelInactive(ctx);
            System.out.println("channel inactive!!!");
        }
    }
    public static final boolean EPOLL = Epoll.isAvailable();
    Class channelClazz = EPOLL? EpollServerSocketChannel.class : NioServerSocketChannel.class;

    public Server() throws Exception {
        EventLoopGroup eventLoopGroup = EPOLL? new EpollEventLoopGroup(): new NioEventLoopGroup();
        try {
            new ServerBootstrap()
                    .group(eventLoopGroup)
                    .channel(channelClazz)
                    .childHandler(new ChannelInitializer<Channel>() {
                        @Override
                        protected void initChannel(Channel ch) throws Exception {
                            ChannelPipeline cpl = ch.pipeline();
                            //cpl.addLast("default_channel_handler", new MySimpleChannelInboundHandler());
                            cpl.addLast(new StringDecoder(StandardCharsets.UTF_8))
                            .addLast(new StringEncoder(StandardCharsets.UTF_8))
                            .addLast(new NetworkHandler());
                        }
                    }).bind(8000).sync().channel().closeFuture().syncUninterruptibly();
        } finally {
            eventLoopGroup.shutdownGracefully().await();
        }
    }
    public static void main(String[] args) throws Exception {
        new Server();
    }
}
