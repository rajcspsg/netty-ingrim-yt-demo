package yt.ingrim.p04;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import yt.ingrim.p04.server.*;

import java.util.Arrays;
import java.util.List;

public class Server {

    private static final boolean EPOLL = Epoll.isAvailable();
    public static final List<Class<? extends Packet>> OUT_PACKETS = Arrays.asList(PacketPing.class, PacketOutTime.class);
    public static final List<Class<? extends Packet>> IN_PACKETS = Arrays.asList(PacketPing.class, PacketInTime.class, PacketExit.class);
    public Server() throws InterruptedException {
        EventLoopGroup eventLoopGroup = EPOLL? new EpollEventLoopGroup() : new NioEventLoopGroup();

        try {
            new ServerBootstrap()
                    .group(eventLoopGroup)
                    .channel(EPOLL? EpollServerSocketChannel.class : NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<Channel>() {
                        @Override
                        protected void initChannel(Channel channel) throws Exception {
                            channel.pipeline()
                                    .addLast(new PacketEncoder())
                                    .addLast(new PacketDecoder())
                                    .addLast(new ServerHandler());
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
