package yt.ingrim.p03;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class ClientHandler extends SimpleChannelInboundHandler<ByteBuf> {

    SimpleDateFormat FORMAT = new SimpleDateFormat("dd.MM.yyyy HH.mm.ss");

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        switch (msg.readInt()) {
            case 0:
                System.out.println("Your ping is " + TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - msg.readLong()));
                break;
            case 1:
                System.out.println("Time Offset " + FORMAT.format(new Date(msg.readLong() - System.currentTimeMillis())));
        }
    }
}
