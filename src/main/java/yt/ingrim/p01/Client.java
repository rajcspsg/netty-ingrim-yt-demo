package yt.ingrim.p01;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.internal.StringUtil;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class Client {

    public static final boolean EPOLL = Epoll.isAvailable();

    Class channelClazz = EPOLL? EpollSocketChannel.class : NioSocketChannel.class;

    public Client() throws Exception {
        EventLoopGroup eventLoopGroup = EPOLL? new EpollEventLoopGroup(): new NioEventLoopGroup();
        try {
            Channel channel = new Bootstrap()
                    .group(eventLoopGroup)
                    .channel(channelClazz)
                    .handler(new ChannelInitializer<Channel>() {
                        @Override
                        protected void initChannel(Channel ch) throws Exception {
                            ch.pipeline().addLast(new StringDecoder(StandardCharsets.UTF_8))
                                    .addLast(new StringEncoder(StandardCharsets.UTF_8));

                        }
                    }).connect("127.0.0.1", 8000).sync().channel(); //.closeFuture().syncUninterruptibly();

            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String line;

            while ((line = reader.readLine()) != null) {
                System.out.println(line);
                if(StringUtil.isNullOrEmpty(line)) continue;
                channel.writeAndFlush(line);
                if(line!= null && line.startsWith("exit")) {
                    System.out.println("Waiting for disconnect!!!");
                    channel.closeFuture().sync();
                    System.out.println("Disconnected");
                    break;
                }
            }
        } finally {
            eventLoopGroup.shutdownGracefully().sync();
        }
    }
    public static void main(String []args) throws Exception {
        new Client();
    }
}
