package yt.ingrim.p04.server;

import io.netty.buffer.ByteBuf;
import java.io.IOException;

public class PacketOutTime implements Packet {
    private long time;

    public PacketOutTime() {}

    public PacketOutTime(long time) {
        this.time = time;
    }

    @Override
    public void read(ByteBuf buf) throws IOException {
        this.time =  buf.readLong();
    }

    @Override
    public void write(ByteBuf buf) throws IOException {
        buf.writeLong(this.time);
    }

    public long getTime() {
        return time;
    }
}
