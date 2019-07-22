import edu.duke.*;

public class TestCaesarCipher{
    
    public void testCaesarCipher(){
        FileResource fr = new FileResource("titus-small.txt");
        String message = fr.asString();
        int[] keys = {17,14,12,4};
        VigenereCipher vc = new VigenereCipher(keys);
        String encrypted = vc.encrypt(message);
        System.out.println(encrypted);
        String x = vc.toString();
        System.out.println(x);
    }
    
}
