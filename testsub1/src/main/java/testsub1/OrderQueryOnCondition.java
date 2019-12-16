package testsub1;

import com.alibaba.fastjson.JSON;
import org.apache.commons.codec.binary.Base64;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhanghaitao on 2018/9/27
 *
 * @Description:
 */
public class OrderQueryOnCondition {
    public static void main(String[] args) {

        Map<String, String> headers = new HashMap<>();
//        headers.put("Content-Type", "application/json");
        Map<String, String> params = new HashMap<>();
//        String orderChannelCode = "OTP_CQZYJ_STD";
        String orderChannelCode = "OTP_ORD_STD";
//        String orderChannelCode = "BCND_TEST";
//        String orderChannelSecretKey = "cmfde62e";
//        String orderChannelSecretKey = "qr83uupv4rsw4899";
//        String orderChannelSecretKey = "079b22e6a59d4ff2";
        String orderChannelSecretKey = "eq3o89r7kr0ot92k";//生产秘钥 海外
        String content = JSON.toJSONString(createOrderQueryRequest());
        System.out.println(content);
        String dataSign = doSign(content , "utf-8", orderChannelSecretKey);
//        System.out.println(dataSign+"---");
        System.out.println(content);
//        String dataSign = "cbVEvmt3auRwa5Z+TJDbaQ==";
        String line = "";
        String result = "";
        URL url = null;
        PrintWriter out = null;

        //String urlstr = "http://58.40.21.182:8081/order-query/v1/queryOrderDetail";
//        String urlstr = "http://10.1.184.137:8095/order-query/v1/queryOrderDetail";
//        String urlstr = "http://172.18.201.26:8095/order-query/v1/queryOrderDetail";
//        String urlstr = "http://58.40.21.182:8081/order-query/v1/queryOrder";
//        String urlstr = "http://10.1.5.101:8081/order-query/v1/queryOrder";
//        String urlstr = "http://114.80.61.45:8082/order-query/v1/queryOrder";
//         String urlstr = "http://114.80.61.45:8082/order-query/v1/queryOrderDetail";
//        String urlstr = "http://10.129.221.182:8095/order-query/v1/queryOrder";
        String urlstr = "http://114.80.61.45:8082/order-query/v1/queryOrder";
//
        try {
            String target = "logisticProviderId="+ URLEncoder.encode(orderChannelCode, "UTF-8")+"&logisticsInterface="+ URLEncoder.encode(content, "UTF-8")+ "&dataDigest=" +URLEncoder.encode(dataSign, "UTF-8");
            System.out.println(URLEncoder.encode(content, "UTF-8"));
            System.out.println("______");
            System.out.println(target);
            url = new URL(urlstr);
            URLConnection conn = url.openConnection();
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            out = new PrintWriter(conn.getOutputStream());
            out.print(target.toString());
            out.flush();
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            while((line = in.readLine()) != null) {
                result += line;
            }
            in.close();
            System.out.println(result);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String doSign(String content, String charset, String keys) {
        String sign = "";
        try {
            content = content + keys;
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(content.getBytes(charset));
            sign = new String(Base64.encodeBase64(md.digest()), charset);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return sign;
    }

    public static OrderQueryRequest createOrderQueryRequest() {
        OrderQueryRequest orderQueryRequest = new OrderQueryRequest();
        //物流号
        orderQueryRequest.setOrderLogisticsCode("OP813481859776512");
        //运单号
//        orderQueryRequest.setWaybillNo("DB0001176158");
//        orderQueryRequest.setContactNumber("18521572340");
        //是否查询增值服务,如果有增值服务,且要查询就传此参数
        orderQueryRequest.setIncrement(true);
        return orderQueryRequest;
    }
}
