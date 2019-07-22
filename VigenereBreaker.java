import java.util.*;
import edu.duke.*;
import java.io.*;

public class VigenereBreaker {
    public String sliceString(String message, int whichSlice, int totalSlices) {
        //REPLACE WITH YOUR CODE
        StringBuilder sb = new StringBuilder();
        for(int k=whichSlice;k<message.length();k+=totalSlices){
            char c = message.charAt(k);
            sb.append(c);
        }
        return sb.toString();
    }

    public int[] tryKeyLength(String encrypted, int klength, char mostCommon) {
        int[] key = new int[klength];
        //WRITE YOUR CODE HERE
        CaesarCracker cc = new CaesarCracker();
        String[] slices = new String[klength];
        for(int k=0;k<klength;k++){
            slices[k] = sliceString(encrypted,k,klength);
            key[k] = cc.getKey(slices[k]);
        }
        return key;
    }

    public void breakVigenere () {
        //WRITE YOUR CODE HERE
        HashMap<String,HashSet<String>> dictionary = new HashMap<String,HashSet<String>>();
        FileResource fr = new FileResource("secretmessage4.txt");
        String message = fr.asString();
        DirectoryResource dr = new DirectoryResource();
        for(File f : dr.selectedFiles()){
            FileResource fr2 = new FileResource(f.getName());
            String name = f.getName();
            dictionary.put(name,readDictionary(fr2));
            System.out.println("Done "+name);
        }
        breakForAllLangs(message,dictionary);
    }
    
    public HashSet<String> readDictionary(FileResource fr){
        HashSet<String> hs = new HashSet<String>();
        for(String s : fr.lines()){
            s = s.toLowerCase();
            hs.add(s);
        }
        return hs;
    }
    
    public int countWords(String message,HashSet<String> dic){
        int counts = 0;
        for(String s : message.split("\\W+")){
            String sLower = s.toLowerCase();
            if(dic.contains(sLower)){
                counts++;
            }
        }
        return counts;
    }
    
    public String breakForLanguage(String encrypted,HashSet<String> dic){
        int[] wordMatch = new int[101];
        int idx = 0;
        wordMatch[0] = 0;
        String message = "";
        char c = mostCommonCharIn(dic);
        for(int k=1;k<101;k++){
            int[] keys = tryKeyLength(encrypted,k,c);
            VigenereCipher vc = new VigenereCipher(keys);
            String decrypted = vc.decrypt(encrypted);
            wordMatch[k] = countWords(decrypted,dic);
            if(wordMatch[k]>wordMatch[0]){
                idx = k;
                wordMatch[0] = wordMatch[k];
                message = decrypted;
            }
        }
        
        return message;
    }
    
    public char mostCommonCharIn(HashSet<String> dictionary){
        int[] counts = new int[26];
        int max = 0;
        int cMax = 0;
        String alphabet = "abcdefghijklmnopqrstuvwxyz";
        for(String s : dictionary){
            for(int k=0;k<s.length();k++){
                char c = s.charAt(k);
                int idx = alphabet.indexOf(c);
                if(idx!=-1){
                    counts[idx]++;
                }
            }
        }
        for(int k=0;k<counts.length;k++){
            if(counts[k]>cMax){
                max = k;
                cMax = counts[k];
            }
        }
        return alphabet.charAt(max);
    }
    
    public void breakForAllLangs(String encrypted,HashMap<String,HashSet<String>> languages){
        int max = 0;
        String language = "";
        String dec = "";
        for(String w : languages.keySet()){
            String decrypted = breakForLanguage(encrypted,languages.get(w));
            int commonWords = countWords(decrypted,languages.get(w));
            System.out.println(w+" "+commonWords);
            if(commonWords>max){
                max = commonWords;
                language = w;
                dec = decrypted;
            }
        }
        System.out.println(language);
        System.out.println(dec);
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
}
