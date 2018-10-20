package testsub1;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Repx {

    public static void main(String[] args) {
        System.out.println("008618966662222".matches("(\\+?0?0?86-?)?1\\d{10}"));
        Pattern patTel = Pattern.compile("(\\+?0?0?86-?)?1\\d{10}");
        Matcher matcher = patTel.matcher("罗贤治008618966662222雁塔区");
        if(matcher.find()){
            System.out.println(matcher.group(0));
        }
        
        
        System.out.println("======");
        System.out.println("029-88889999".matches("((0\\d{2})?-?\\d{8}(-?\\d{1,4})?)|((0\\d{3})?-?\\d{7,8}(-?\\d{1,4})?)"));
        Pattern patTel2 = Pattern.compile("((0\\d{2})?-?\\d{8}(-?\\d{1,4})?)|((0\\d{3})?-?\\d{7,8}(-?\\d{1,4})?)");
        Matcher matcher2 = patTel2.matcher("029-88889999-0310");
        if(matcher2.find()){
            System.out.println(matcher2.group(0));
        }
    }
}
