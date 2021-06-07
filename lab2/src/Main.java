public class Main {
    public static void main(String[] args) throws Exception {
        String key = "tanyakurdelchukTANYA";
        RTEA rtea = new RTEA(key);
        String plaintext = "TWILIGHTTWILIGHT";
        String st = rtea.encrypt(plaintext);
        System.out.println(st);
        String st1 = rtea.decrypt(st);
        System.out.println(st1);
    }
}
