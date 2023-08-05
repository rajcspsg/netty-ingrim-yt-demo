package yt.ingrim.p01;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

public class Client {

    public static final boolean EPOLL = Epoll.isAvailable();

    Class channelClazz = EPOLL? EpollServerSocketChannel.class : NioSocketChannel.class;

    public Client() throws Exception {
        EventLoopGroup eventLoopGroup = EPOLL? new EpollEventLoopGroup(): new NioEventLoopGroup();
        try {
            new Bootstrap()
                    .group(eventLoopGroup)
                    .channel(channelClazz)
                    .remoteAddress(new InetSocketAddress("127.0.0.1", 8000))
                    .handler(new ChannelInitializer<Channel>() {
                        @Override
                        protected void initChannel(Channel ch) throws Exception {

                        }
                    }).connect().sync().channel().closeFuture().syncUninterruptibly();
        } finally {
            eventLoopGroup.shutdownGracefully().sync();
        }
    }
    public static void main(String []args) throws Exception {
        System.out.println(EPOLL);
        new Client();
    }
}
