package yt.ingrim.p04.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import yt.ingrim.p04.Server;

import java.util.List;

public class PacketDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf buf, List<Object> out) throws Exception {
        int id = buf.readInt();
        Class<? extends Packet> packetClass = Server.IN_PACKETS.get(id);
        if(packetClass == null) {
            throw new NullPointerException("Couldn't find packet by id " + id);
        }
        Packet packet = packetClass.newInstance();
        packet.read(buf);
        out.add(packet);
    }
}
