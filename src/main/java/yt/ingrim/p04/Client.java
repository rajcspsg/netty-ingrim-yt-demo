package yt.ingrim.p04;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import yt.ingrim.p04.client.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

public class Client {

    private static final boolean EPOLL = Epoll.isAvailable();
    private static Client instance;

    public static final List<Class<? extends Packet>>OUT_PACKETS = Arrays.asList(PacketPing.class, PacketOutTime.class, PacketExit.class);
    public static final List<Class<? extends Packet>> IN_PACKETS = Arrays.asList(PacketPing.class, PacketInTime.class);
    public Client() throws IOException, InterruptedException {
        Client.instance = this;
        EventLoopGroup eventLoopGroup = EPOLL ? new EpollEventLoopGroup(): new NioEventLoopGroup();
        try {
            Channel channel = new Bootstrap()
                    .group(eventLoopGroup)
                    .channel(EPOLL? EpollSocketChannel.class : NioSocketChannel.class)
                    .handler(new ChannelInitializer<Channel>() {
                        @Override
                        protected void initChannel(Channel channel) throws Exception {
                            channel.pipeline()
                                    .addLast(new PacketDecoder())
                                    .addLast(new PacketEncoder())
                                    .addLast(new ClientHandler());
                        }
                    }).connect("127.0.0.1", 8000).sync().channel();

            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String line;
            while((line = reader.readLine()) != null) {
                if(line.isEmpty()) continue;
                Packet packet;
                if(line.startsWith("ping")) {
                    packet = new PacketPing(System.nanoTime());
                    channel.writeAndFlush(packet, channel.voidPromise());
                } else if (line.startsWith("time")) {
                    packet = new PacketOutTime();
                    channel.writeAndFlush(packet, channel.voidPromise());
                } else if (line.startsWith("exit")) {
                    channel.writeAndFlush(new PacketExit(), channel.voidPromise());
                    channel.closeFuture().syncUninterruptibly();
                    break;
                }
            }
        } finally {
            eventLoopGroup.shutdownGracefully();
        }
    }
    public static void main(String[] args) throws IOException, InterruptedException {
        new Client();
    }
}
