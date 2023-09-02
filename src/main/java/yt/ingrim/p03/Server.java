package yt.ingrim.p03;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class Server {

    private static final boolean EPOLL = Epoll.isAvailable();

    public Server() throws InterruptedException {
        EventLoopGroup eventLoopGroup = EPOLL? new EpollEventLoopGroup() : new NioEventLoopGroup();

        try {
            new ServerBootstrap()
                    .group(eventLoopGroup)
                    .channel(EPOLL? EpollServerSocketChannel.class : NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<Channel>() {
                        @Override
                        protected void initChannel(Channel channel) throws Exception {
                            channel.pipeline().addLast(new Serverhandler());
                        }
                    }).bind(8000).sync().channel().closeFuture().syncUninterruptibly();
        } finally {
            eventLoopGroup.shutdownGracefully();
        }
    }
    public static void main(String[] args) throws InterruptedException {
        new Server();
    }
}
