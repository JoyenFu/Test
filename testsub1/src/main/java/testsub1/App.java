package testsub1;

import java.io.File;
import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


/**
 * Hello world!
 *
 */
public class App 
{
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int T = sc.nextInt();
        for (int i = 1; i <= T; i++) {
            int n = sc.nextInt();
            int[] arr = new int[n];
            for (int j = 0; j < n; j++) {
                arr[j] = sc.nextInt();
            }
            int max = Integer.MIN_VALUE;
            int sum = 0;
            int left = 0;
            int right = 0;
            int temp = 0;
            for (int j = 0; j < n; j++) {
                sum += arr[j];
                if(max < sum) {
                    max = sum;
                    left = temp;
                    right = j;
                }
                if(sum < 0) {
                    sum = 0;
                    temp = j + 1;
                }
            }
            if(max < 0) {
                for (int j = 0; j < n; j++) {
                    if(max == arr[j]) {
                        left = j;
                        right = j;
                        break;
                    }
                }
            }
            System.out.println("Case " + i + ":");
            System.out.println(max + " " + (left + 1) + " " + (right + 1));
            if(i != T) {
                System.out.println("");
            }
        }
    }
    
    
}
