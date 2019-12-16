package testsub1;

import java.util.ArrayList;
import java.util.List;

public class ArrayTest {

    public static void main(String[] args) {

        List<String> list = new ArrayList<String>();
        list.add("1");
        //list.add("2");
        String[] a = new String[2]; 
        String[] aa = list.toArray(a);
        for (String string : aa) {
            System.out.println(string);
        }
        
    }

}
