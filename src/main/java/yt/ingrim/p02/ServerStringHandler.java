package yt.ingrim.p02;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ServerStringHandler extends SimpleChannelInboundHandler<String> {

    private Channel channel;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
      this.channel = ctx.channel();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        if(msg.equals("exit")) {
          channel.close();
        } else if("test".equalsIgnoreCase(msg)) {
          channel.writeAndFlush("echo");
        }
        System.out.println("get message: " + msg);
    }
}


