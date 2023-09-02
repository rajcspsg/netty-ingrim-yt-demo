package org.demo.h11client;

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
import io.netty.handler.codec.http.*;
import io.netty.util.NetUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Client {

    private static final boolean EPOLL = Epoll.isAvailable();

    public Client() throws InterruptedException {
        EventLoopGroup eventLoopGroup = EPOLL ? new EpollEventLoopGroup(): new NioEventLoopGroup();
        try {
            Channel channel = new Bootstrap()
                    .group(eventLoopGroup)
                    .channel(EPOLL? EpollSocketChannel.class : NioSocketChannel.class)
                    .handler(new ChannelInitializer<Channel>() {
                        @Override
                        protected void initChannel(Channel channel) {
                            channel.pipeline().addLast(new HttpRequestEncoder()).addLast(new HttpResponseDecoder());
                        }
                    }).connect(NetUtil.LOCALHOST6, 3000).sync().channel();

            DefaultHttpRequest req = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, "");
            channel.writeAndFlush(req, channel.voidPromise());
            Thread.sleep(1000);

        } finally {

        }
    }
    public static void main(String[] args) throws IOException, InterruptedException {
        new Client();
    }
}
