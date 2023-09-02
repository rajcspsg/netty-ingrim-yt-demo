package yt.ingrim.p05;

public class ByteBufExplain {
    public static void main(String[] args) {
        byte[] array = new byte[Integer.MAX_VALUE - 2];
        int x = 1782469555; // 01101010 00111110 01010011 10110011
        //printIntBinary(x);
        array[0] = (byte) (x >>> 24);
        array[1] = (byte) (x >>> 16);
        array[2] = (byte) (x >>> 8);
        array[3] = (byte) (x >>> 0);

        //printIntBinary(x >> 24); // 00000000 00000000 00000000 01101010
        int y = (array[0] & 0xFF ) << 24 | (array[1] & 0xFF) << 16 | (array[2] & 0xFF) << 8 | (array[3] & 0xFF) << 0 ;
        writeVarInt(array,4, x);

    }

    static void writeVarInt(byte[] array, int index, int value) {
        byte part;
        do {
            part = (byte) (value & 0x7F);
            value >>>= 7;
            if(value != 0)
                part |= 0x80;
            array[index++] = part;
        } while (value != 0);
    }

    static int readVarInt(byte[] array, int index) throws Exception {
        int out = 0, bytes = 0;
        byte part;
        do {
            part = array[index++];
            out |= (part & 0x7F) << (bytes++ *7);
            if(bytes > 2) {
                throw new Exception(String.format("VarInt is too Long (%d > 5", bytes));
            }
        } while ((part & 0x80) == 0x80);
        return out;
    }
    public static void printIntBinary(int number) {
        StringBuilder binary = new StringBuilder();
        for (int i = 31; i >= 0; i--) {
           binary.append((number & (1 << i)) != 0? "1" : "0");
           if(i %8 ==0 && i != 0)
               binary.append(" ");
        }
        System.out.println(binary);
    }
}
