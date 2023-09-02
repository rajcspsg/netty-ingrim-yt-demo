package yt.ingrim.p04.server;

import io.netty.buffer.ByteBuf;
import java.io.IOException;

public class PacketPing implements Packet {
    private long time;

    public PacketPing() {}

    public PacketPing(long time) { this.time = time; }

    @Override
    public void read(ByteBuf buf) throws IOException {
        buf.readLong();
    }

    @Override
    public void write(ByteBuf buf) throws IOException {
        buf.writeLong(this.time);
    }

    public long getTime() {
        return time;
    }
}
