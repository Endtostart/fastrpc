package fastrpc.serialize;

public interface Encode{
    String encode(Object orign);

    byte[] encodeToByte(Object orign);
}
