package testsub1;

import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 分布式序列生成器<br>
 * 结构如下(每部分用-分开):<br>
 * 44位时间截(毫秒级)，注意，44位时间截不是存储当前时间的时间截，而是存储时间截的差值（当前时间截 - 开始时间截)得到的值，这里的的开始时间截，一般是我们的id生成器开始使用的时间。
 *   44位的时间截，可以使用557年，年T = (1L << 41) / (1000L * 60 * 60 * 24 * 365) = 557 （生成11位16进制）<br>
 * 工作机器ID （根据IP生成8位16进制序列）<br>
 * 进程ID （根据系统进程pid生成4位16进制）<br>
 * 12位序列，毫秒内的计数，12位的计数顺序号支持每个节点每毫秒(同一机器，同一系统，同一时间截)产生4096个ID序号 （生成3位16进制）<br>
 * 
 * 此序列整体上按照时间自增排序，并且整个分布式系统内不会产生ID碰撞(由机器ID和进程ID作区分)
 */
public class IdWorker {

    // ==============================Fields===========================================
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

    //==============================Constructors=====================================
    /**
     * 构造函数
     * @param mechineId 工作机器ID (根据IP生成8位16进制序列)
     * @param processId 进程ID (根据系统进程pid生成4位16进制)
     */
    public IdWorker(String mechineId, String processId) {
        this.mechineId = mechineId;
        this.processId = processId;
    }

    // ==============================Methods==========================================
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

        //移位并通过或运算拼到一起组成64位的ID
        System.out.println("时间戳: "+ String.format("%011x", (timestamp - twepoch)) + ", 机器: " + this.mechineId + ", 进程: " + this.processId + ", 序列: " + String.format("%03d", sequence));
        return String.format("%011x", (timestamp - twepoch)) + this.mechineId + this.processId + String.format("%03d", sequence);
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

    //==============================Test=============================================
    /** 测试 */
    public static void main(String[] args) {
    	long btime = System.currentTimeMillis();
    	StringBuilder  hexIp = new StringBuilder();
    	
    	InetAddress addr =  null;
		try {
			addr = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//新建一个InetAddress类
    	
    	String ip = addr.getHostAddress();// 获得本机ip
    	for (String i : ip.split("\\.")) {
        	String str = String.format("%02x",Integer.parseInt(i));
        	hexIp.append(str);
		}
    	
    	String name = ManagementFactory.getRuntimeMXBean().getName();
    	String hexPid = String.format("%04x",Integer.parseInt(name.split("@")[0]));
    	
        IdWorker idWorker = new IdWorker(hexIp.toString(), hexPid);
        
        for (int i = 0; i < 1000; i++) {
            String id = idWorker.nextId();
            System.out.println(id);
        }
        long etime = System.currentTimeMillis();
        System.out.println(etime-btime);
        System.out.println((1L << 44) / (1000L * 60 * 60 * 24 * 365));
    }
}