package testsub1.updateorder;

import com.alibaba.fastjson.JSON;
import com.google.inject.internal.util.Lists;

import org.apache.commons.codec.binary.Base64;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class UpdateOrderTest {
    public static void main(String[] args) {

        /**
         * 将下面的providerId 和secretkey 换成我们提供的值
         */
        String providerId = "OTP_ORDER_UPDATE";
        String secretKey = "123456";
        String content = JSON.toJSONString(createOrder());
        System.out.println("###content:"+content);
        String dataSign = doSign(content, "utf-8", secretKey);
        String line = "";
        String result = "";
        URL url = null;
        PrintWriter out = null;
        // 测外
        //String urlstr = "http://10.129.221.183:8081/order/v1/chinaPost/createOrderAndPrint";
//       String urlstr = "http://114.80.61.45:8082/order/v1/updateOrder";
//        String urlstr = "http://localhost:9082/order/v1/updateOrder";
        //String urlstr = "http://192.168.204.127:9082/order/v1/chinaPost/createOrderAndPrint";
        // 测内
		String urlstr = "http://10.129.221.182:9082/order/v1/updateOrder";
        try {
            String target = "logisticProviderId=" + URLEncoder.encode(providerId, "UTF-8") + "&logisticsInterface="
                    + URLEncoder.encode(content, "UTF-8") + "&dataDigest=" + URLEncoder.encode(dataSign, "UTF-8");
            System.out.println(target);
            System.err.println("=============");
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
            while ((line = in.readLine()) != null) {
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

    public static ChinaPostOrderDTO createOrder() {
        ChinaPostOrderDTO order = new ChinaPostOrderDTO();
        // 测试
//        order.setCustomerCode("K21004148");
//        order.setCustomerSecretKey("XFEDDSddf5ff");

        /**
          * 将下面的orderChannelCode 换成我们提供的providerId值
         */
        order.setOrderChannelCode("OTP_WX");
        order.setOrderLogisticsCode("yto_test070111");
        order.setCustOrderCreateTime("2018-11-13 11:10:46");
        order.setWeight(1.0);
        order.setGoodsTotalFee(10020.0);
        order.setGoodsType("G1");
        order.setGoodsName("手机");
        order.setRecipientProvCode("");
        order.setRecipientProvName("上海");
        order.setRecipientCityCode("");
        order.setRecipientCityName("上海市");
        order.setRecipientAreaCode("");
        order.setRecipientAreaName("青浦区");
        order.setRecipientTownCode("徐泾镇");
        order.setRecipientAddress("盈港东路  2165弄24号#%! ");
        order.setRecipientTownName("");
        order.setRecipientMobile("1377777777");
        order.setRecipientPhone("0379-65630357");
        order.setRecipientName("董兴3210");

        order.setRemark("测试5555555");
        order.setSenderProvCode("310000");
        order.setSenderProvName("上海");
        order.setSenderCityCode("310100");
        order.setSenderCityName("上海市");
        order.setSenderAreaCode("310118");
        order.setSenderAreaName("青浦区");
        order.setSenderTownCode("310118107");
        order.setSenderTownName("华新镇");
        order.setSenderAddress("华 徐公 路3029 弄28号@!*# ");
        order.setSenderMobile("15800777777");
        order.setSenderPhone("021-69773588");
        order.setSenderName("陈猜333");
        order.setDeviceAddress("懋隆%&网点 三间房东 路一号懋隆*文化产业园@ B座西侧一层~~~");
        order.setDeviceProvince("上海市");
        order.setDeviceCity("上海");
        order.setDeviceRegion("123");
        order.setDeviceCityCode("123");
        order.setSenderRealName("李四");
        order.setSenderIdCard("340901198811186630");
        order.setActualFee(1.00);
        order.setPayableFee(5.00);
        order.setThirdCouponsFee(1.00);
        order.setDeviceLatitude(1.00);
        order.setDeviceLongitude(2.00);
        order.setDeviceRegionCode("111");
        order.setDeviceFullAddress("123");
        order.setDeviceBoxType("middle");

        order.setDeviceOpenCode("12345");
        //order.setPayObject("POB001");

        List<IncrementService> incrementServiceList = Lists.newArrayList();
        IncrementService inc = new IncrementService();
        inc.setIncrementCode("I001");
        inc.setIncrementName("保价");
        inc.setIncrementAmt(33d);
        incrementServiceList.add(inc);
        IncrementService inc2 = new IncrementService();
//        inc2.setIncrementCode("I002");
//        inc2.setIncrementName("代收货款");
//        inc2.setIncrementAmt(44d);
//        incrementServiceList.add(inc2);
        order.setIncrementService(incrementServiceList);
        return order;
    }
}
