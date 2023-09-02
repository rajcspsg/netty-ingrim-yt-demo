package yt.ingrim.p04;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import yt.ingrim.p04.client.Packet;
import yt.ingrim.p04.client.PacketInTime;
import yt.ingrim.p04.client.PacketPing;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class ClientHandler extends SimpleChannelInboundHandler<Packet> {

    SimpleDateFormat FORMAT = new SimpleDateFormat("dd.MM.yyyy HH.mm.ss");

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Packet packet) throws Exception {
        if(packet instanceof PacketPing) {
            System.out.println("Your ping is :" + TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - ((PacketPing) packet).getTime()) + " ms");
        } else if (packet instanceof PacketInTime) {
            System.out.println("Time Offset: " + FORMAT.format(new Date(((PacketInTime) packet).getTime() - System.currentTimeMillis())));
        }
    }
}
