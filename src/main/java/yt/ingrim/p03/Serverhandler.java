package yt.ingrim.p03;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class Serverhandler extends SimpleChannelInboundHandler<ByteBuf> {
    private Channel channel;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
       this.channel = ctx.channel();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        ByteBuf callback;
        switch (msg.readInt()) {
            case 0 -> {
                callback = Unpooled.buffer();
                callback.writeInt(0);
                callback.writeLong(msg.readLong());
                this.channel.writeAndFlush(callback, this.channel.voidPromise());
            }
            case 1 ->  {
                System.out.println("Time");
                callback = Unpooled.buffer();
                callback.writeInt(1);
                callback.writeLong(System.currentTimeMillis() + (short) (Math.random() * Short.MAX_VALUE));
                this.channel.writeAndFlush(callback, this.channel.voidPromise());
                break;
            }
            case 2 -> this.channel.close();
            default -> {}
        }
    }
}
