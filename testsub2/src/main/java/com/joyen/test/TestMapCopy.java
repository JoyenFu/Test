package com.joyen.test;

import java.util.HashMap;

public class TestMapCopy {

    public static void main(String[] args) {
        HashMap<String,Object> hm = new HashMap();
        HashMap<String,Object> hmCopy = new HashMap();
        Integer it = new Integer(123);
        hm.put("123", it);
        System.out.println(hm.get("123"));
        hmCopy.putAll(hm);
        //hmCopy.remove("123");
        //hmCopy.put("123", "1234");
        System.out.println(hm.get("123"));
        System.out.println(hmCopy.get("123"));
    }

}
