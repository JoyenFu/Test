package testsub1;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

public class TestJson {

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        //这是一个错误提交的版本需要回退
        String s = "200";
        
        JsonObject json = new JsonObject();
        json.addProperty("status", 200);
        json.addProperty("statusstr", s);
        json.addProperty("teststr", "abc");
        System.out.println(json.toString());
        
        //test null
        Gson gson2 = new GsonBuilder().disableHtmlEscaping().create();
        System.out.println(gson2.toJson(null));
        
        
        //test sublist
        List<String> l = new ArrayList<String>();
        l.add("1");
        l.add("2");
        l.add("3");
        l.add("4");
        System.out.println(l.subList(0, 2));
    }

}
