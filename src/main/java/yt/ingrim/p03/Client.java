package yt.ingrim.p03;

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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Client {

    private static final boolean EPOLL = Epoll.isAvailable();

    public Client() throws IOException, InterruptedException {
        EventLoopGroup eventLoopGroup = EPOLL ? new EpollEventLoopGroup(): new NioEventLoopGroup();
        try {
            Channel channel = new Bootstrap()
                    .group(eventLoopGroup)
                    .channel(EPOLL? EpollSocketChannel.class : NioSocketChannel.class)
                    .handler(new ChannelInitializer<Channel>() {
                        @Override
                        protected void initChannel(Channel channel) throws Exception {
                            channel.pipeline().addLast(new ClientHandler());
                        }
                    }).connect("127.0.0.1", 8000).sync().channel();

            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String line;
            while((line = reader.readLine()) != null) {
                if(line.isEmpty()) continue;
                ByteBuf byteBuf = Unpooled.buffer();
                if(line.startsWith("ping")) {
                    byteBuf.writeInt(0);
                    byteBuf.writeLong(System.nanoTime());
                } else if (line.startsWith("time")) {
                    byteBuf.writeInt(1);
                } else if (line.startsWith("exit")) {
                    byteBuf.writeInt(2);
                    channel.writeAndFlush(byteBuf, channel.voidPromise());
                    channel.closeFuture().syncUninterruptibly();
                    break;
                }
                channel.writeAndFlush(byteBuf, channel.voidPromise());
            }
        } finally {

        }
    }
    public static void main(String[] args) throws IOException, InterruptedException {
        new Client();
    }
}
