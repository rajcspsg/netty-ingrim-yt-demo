package yt.ingrim.p04.server;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

public interface Packet {
    void read(ByteBuf buf) throws IOException;
    void write(ByteBuf buf) throws IOException;
}
