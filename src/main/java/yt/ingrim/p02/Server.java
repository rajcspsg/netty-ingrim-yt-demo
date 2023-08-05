package yt.ingrim.p02;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.nio.charset.StandardCharsets;

public class Server {

  public static final boolean EPOLL = Epoll.isAvailable();

  public Server() throws Exception {

    EventLoopGroup eventLoopGroup = EPOLL? new EpollEventLoopGroup(): new NioEventLoopGroup();
    try {
      new ServerBootstrap()
            .group(eventLoopGroup)
            .channel(EPOLL? EpollServerSocketChannel.class : NioServerSocketChannel.class)
            .childHandler(new ChannelInitializer<Channel>() {

                @Override
                protected void initChannel(Channel channel) throws Exception {
                    channel.pipeline()
                            .addLast(new StringDecoder(StandardCharsets.UTF_8))
                            .addLast(new StringEncoder(StandardCharsets.UTF_8))
                            .addLast(new ServerStringHandler());
                }
            }).bind(8000)
              .sync()
              .channel()
              .closeFuture()
              .syncUninterruptibly();
    } finally {
        eventLoopGroup.shutdownGracefully();
    }
  }

  public static void main(String[] args) throws Exception {
      new Server();
  }
}
