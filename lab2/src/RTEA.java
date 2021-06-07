import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class RTEA {

    public int[] key;
    private final int w;

    public RTEA(String key) throws Exception {
        byte[] key_bytes = key.getBytes(StandardCharsets.ISO_8859_1);
        int length_key_bytes = key_bytes.length;
        if(length_key_bytes % 4 != 0)
            throw new Exception("Wrong key");

        this.key = convertByteToInt(key_bytes, length_key_bytes/4);
        this.w = this.key.length;
    }

    public String encrypt(String plainText) throws Exception {
        byte[] plainText_bytes = plainText.getBytes(StandardCharsets.ISO_8859_1);
        int length_plainText_bytes = plainText_bytes.length;
        if(length_plainText_bytes % 2 != 0)
            throw new Exception("Wrong word for encrypt");

        int[] ab = convertByteToInt(plainText_bytes, length_plainText_bytes/4);

        for(int i = 0; i < ab.length-1; i+=2) {
            for(int r = 0; r < w*4+32; r++) {
                ab[i + 1]+=ab[i]+((ab[i]<<6)^(ab[i]>>8))+ (key[r%w]+r);
                r++;
                ab[i]+=ab[i + 1]+((ab[i + 1]<<6)^(ab[i + 1]>>8))+ (key[r%w]+r);
            }
        }

        byte[] conInt = convertIntToByte(ab, length_plainText_bytes);
        return new String(conInt, StandardCharsets.ISO_8859_1);
    }

    public String decrypt(String cipherText) throws Exception {
        byte[] cipherText_bytes = cipherText.getBytes(StandardCharsets.ISO_8859_1);
        int length_cipherText_bytes = cipherText_bytes.length;
        if(length_cipherText_bytes % 2 != 0)
            throw new Exception("Wrong word for decrypt");

        int[] ab = convertByteToInt(cipherText_bytes, length_cipherText_bytes/4);

        for(int i = 0; i < ab.length-1; i+=2) {
            for(int r = w*4 + 31; r >= 0; r--) {
                ab[i]-= ab[i + 1]+(( ab[i + 1]<<6)^( ab[i + 1]>>8))+ (key[r%w]+r);
                r--;
                ab[i + 1]-=ab[i]+((ab[i]<<6)^(ab[i]>>8))+ (key[r%w]+r);
            }
        }

        byte[] conInt = convertIntToByte(ab, length_cipherText_bytes);
        return new String(conInt, StandardCharsets.ISO_8859_1);

    }
    private static int[] convertByteToInt(byte[] array, int length) {
        int[] byte_to_int = new int[length];

        for (int i = 0; i < byte_to_int.length; i++) {
            byte_to_int[i] = ((array[i*4] & 0xff)) |
                    ((array[i*4 + 1] & 0xff) << 8) |
                    ((array[i*4 + 2] & 0xff) << 16) |
                    ((array[i*4 + 3] & 0xff) << 24);
        }
        return byte_to_int;
    }
    private static byte[] convertIntToByte(int[] integerArray, int length) {
        byte[] int_to_byte = new byte[length];

        for (int i = 0; i < length; i++) {
            int_to_byte[i] = (byte) ((integerArray[i / 4] >>> (i % 4) * 8) & 0xff);
        }
        return int_to_byte;
    }
}
