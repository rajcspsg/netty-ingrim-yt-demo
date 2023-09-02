package yt.ingrim.p04;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import yt.ingrim.p04.server.*;

public class ServerHandler extends SimpleChannelInboundHandler<Packet> {
    private Channel channel;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
       this.channel = ctx.channel();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Packet packet) throws Exception {
        if(packet instanceof PacketPing) {
            this.channel.writeAndFlush(packet, this.channel.voidPromise());
        } else if (packet instanceof PacketInTime) {
            this.channel.writeAndFlush(new PacketOutTime(System.currentTimeMillis() + (short) (Math.random() * Short.MAX_VALUE)));
        } else if (packet instanceof PacketExit) {
            this.channel.close();
        }
    }
}
