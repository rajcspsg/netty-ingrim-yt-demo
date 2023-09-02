package yt.ingrim.p04.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import yt.ingrim.p04.Server;

public class PacketEncoder extends MessageToByteEncoder<Packet> {
    @Override
    protected void encode(ChannelHandlerContext ctx, Packet packet, ByteBuf out) throws Exception {
        int id = Server.OUT_PACKETS.indexOf(packet.getClass());

        if(id == -1) {
            throw new NullPointerException("Couldn't find id of the packet " +  packet.getClass().getSimpleName());
        }
        out.writeInt(id);
        packet.write(out);
    }
}
