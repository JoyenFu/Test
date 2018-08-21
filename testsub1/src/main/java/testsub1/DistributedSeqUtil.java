package testsub1;

import java.lang.management.ManagementFactory;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;


/**
 * streamlet 分布式序列生成器<br>
 * streamlet的结构如下(每部分用-分开):<br>
 * 44位时间截(毫秒级)，注意，44位时间截不是存储当前时间的时间截，而是存储时间截的差值（当前时间截 - 开始时间截)得到的值，这里的的开始时间截，一般是我们的id生成器开始使用的时间。
 *   44位的时间截，可以使用557年，年T = (1L << 41) / (1000L * 60 * 60 * 24 * 365) = 557 （生成11位16进制）<br>
 * 工作机器ID （根据IP生成8位16进制序列）<br>
 * 进程ID （根据系统进程pid生成4位16进制）<br>
 * 12位序列，毫秒内的计数，12位的计数顺序号支持每个节点每毫秒(同一机器，同一系统，同一时间截)产生4096个ID序号 （生成3位16进制）<br>
 * 
 * 此序列整体上按照时间自增排序，并且整个分布式系统内不会产生ID碰撞(由机器ID和进程ID作区分)。序列共26（11+8+4+3）位。
 * 
 * @author fuwenjun
 * @since 1.0.0
 * 
 */


public class DistributedSeqUtil {

	private static volatile DistributedSeqUtil singleton;
	
    /** 开始时间截 (2015-01-01) */
    private final long twepoch = 1420041600000L;

    /** 工作机器ID (根据IP生成8位16进制序列) */
    private String mechineId;

    /** 进程ID (根据系统进程pid生成4位16进制) */
    private String processId;
    
    /** 序列位数 */
    private final long sequenceBits = 12L;

    /** 生成序列的掩码，这里为4095 (0b111111111111=0xfff=4095) */
    private final long sequenceMask = -1L ^ (-1L << sequenceBits);

    /** 毫秒内序列(0~4095) */
    private long sequence = 0L;

    /** 上次生成ID的时间截 */
    private long lastTimestamp = -1L;

  
    /**
     * 构造函数
     */
    private DistributedSeqUtil() {
        
    }
    
    /**
     * 构造函数
     * @param mechineId 工作机器ID (根据IP生成8位16进制序列)
     * @param processId 进程ID (根据系统进程pid生成4位16进制)
     */
    private DistributedSeqUtil(String mechineId, String processId) {
    	if(mechineId == null || "".equals(mechineId)){
    		throw new IllegalArgumentException(String.format("mechine Id can't be empty!!!"));
    	}
    	if(processId == null || "".equals(processId)){
    		throw new IllegalArgumentException(String.format("process Id can't be empty!!!"));
    	}
    	
        this.mechineId = mechineId;
        this.processId = processId;
    }

    
    /**
     * 获得下一个ID (该方法是线程安全的)
     * @return streamletID
     */
    public synchronized String nextId() {
        long timestamp = timeGen();

        //如果当前时间小于上一次ID生成的时间戳，说明系统时钟回退过这个时候应当抛出异常
        if (timestamp < lastTimestamp) {
            throw new RuntimeException(
                    String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds", lastTimestamp - timestamp));
        }

        //如果是同一时间生成的，则进行毫秒内序列
        if (lastTimestamp == timestamp) {
            sequence = (sequence + 1) & sequenceMask;
            //毫秒内序列溢出
            if (sequence == 0) {
                //阻塞到下一个毫秒,获得新的时间戳
                timestamp = tilNextMillis(lastTimestamp);
            }
        }
        //时间戳改变，毫秒内序列重置
        else {
            sequence = 0L;
        }

        //上次生成ID的时间截
        lastTimestamp = timestamp;

        String hexTimestamp = String.format("%011x", (timestamp - this.twepoch));
        String hexSeq = String.format("%03d", sequence);
        
        //log.info("时间戳: "+ hexTimestamp + ", 机器: " + this.mechineId + ", 进程: " + this.processId + ", 序列: " + hexSeq);
        
        //拼接生成的序列
        StringBuilder streamletID = new StringBuilder();
        streamletID.append(hexTimestamp);
        streamletID.append(this.mechineId);
        streamletID.append(this.processId);
        streamletID.append(hexSeq);
        
        return streamletID.toString();
    }

    /**
     * 阻塞到下一个毫秒，直到获得新的时间戳
     * @param lastTimestamp 上次生成ID的时间截
     * @return 当前时间戳
     */
    protected long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    /**
     * 返回以毫秒为单位的当前时间
     * @return 当前时间(毫秒)
     */
    protected long timeGen() {
        return System.currentTimeMillis();
    }

    /**
     * 获取单实例
     * @return
     * @throws SocketException
     * @throws UnknownHostException
     * @throws Exception
     */
    public static DistributedSeqUtil getInstance() throws SocketException, UnknownHostException, Exception {
    	
        if (singleton == null) {
            synchronized (DistributedSeqUtil.class) {
                if (singleton == null) {
                	
                	StringBuilder  hexIp = new StringBuilder();
                    String hexPid = "";
            		
            		String ip = getIp();// 获得本机ip
            		for (String i : ip.split("\\.")) {
            	    	String str = String.format("%02x",Integer.parseInt(i));
            	    	hexIp.append(str);
            		}
            		
            		String name = ManagementFactory.getRuntimeMXBean().getName();//获取当前进程
            		hexPid = String.format("%04x",Integer.parseInt(name.split("@")[0]));
            		
            		//log.info("当前主机ip: "+ ip + "当前进程@主机名: " + name);
            		
                    singleton = new DistributedSeqUtil(hexIp.toString(), hexPid);
                }
            }
        }
        return singleton;
    }
    
    /**
     * 多IP处理，可以得到最终 ip
     *
     * 127.xxx.xxx.xxx 属于” loopback” 地址,即只能你自己的本机可见,就是本机地址,比较常见的有127.0.0.1;
     * 192.168.xxx.xxx 属于private 私有地址(site local address),属于本地组织内部访问,只能在本地局域网可见。同样10.xxx.xxx.xxx、从172.16.xxx.xxx 到 172.31.xxx.xxx都是私有地址,也是属于组织内部访问;
     * 169.254.xxx.xxx 属于连接本地地址(link local IP),在单独网段可用
     * 从224.xxx.xxx.xxx 到 239.xxx.xxx.xxx 属于组播地址
     * 比较特殊的255.255.255.255 属于广播地址
     * 除此之外的地址就是点对点的可用的公开IPv4地址
     *
     * @return
     * @throws SocketException
     * @throws UnknownHostException
     */  
    private static String getIp() throws SocketException, UnknownHostException {
        String ip = "";
        Enumeration<NetworkInterface> netInterfaces = NetworkInterface.getNetworkInterfaces();
        while (netInterfaces .hasMoreElements()) {
            NetworkInterface networkInterface = (NetworkInterface) netInterfaces .nextElement();
            Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
            while (addresses .hasMoreElements()) {
                InetAddress inetAddress = (InetAddress) addresses.nextElement();
                if(inetAddress instanceof Inet4Address){
                    if(!inetAddress .isLoopbackAddress()){//排除loopback类型地址。
                        if(inetAddress .isSiteLocalAddress()){
                            ip = inetAddress.getHostAddress();//如果找到site-local地址,优先取它。
                            return ip ;
                        } else{
                            ip = inetAddress.getHostAddress();//site-local类型的地址未被发现,先记录候选地址。
                        }
                    }
                }
               
            }
        }
       
        if("".equals(ip)){
            // 如果没有发现 non- loopback地址.只能用最次选的方案
            InetAddress jdkSuppliedAddress = InetAddress.getLocalHost();
            if (jdkSuppliedAddress == null) {
                throw new UnknownHostException("The JDK InetAddress.getLocalHost() method unexpectedly returned null.");
            }
            ip = jdkSuppliedAddress.getHostAddress();
        }
        return ip ;
    }
}
