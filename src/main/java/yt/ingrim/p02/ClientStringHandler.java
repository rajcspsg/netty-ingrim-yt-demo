package yt.ingrim.p02;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ClientStringHandler extends SimpleChannelInboundHandler<String> {

    private Channel channel;


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
      this.channel =  ctx.channel();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
      System.out.println("Server send : " + msg);
    }
}
