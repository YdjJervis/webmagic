package us.codecraft.webmagic.netsense.base;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Timer;
import java.util.TimerTask;

public class AdslManager {

    /**
     * 执行CMD命令,并返回String字符串
     */
    public static String executeCmd(String strCmd) throws Exception {

        StringBuilder sbCmd = new StringBuilder();
        String ret = null;
        try {
            Process p = Runtime.getRuntime().exec("/sbin/" + strCmd);
            BufferedReader br = new BufferedReader(new InputStreamReader(p
                    .getInputStream()));
            String line;
            while ((line = br.readLine()) != null) {
                sbCmd.append(line + "\n");
            }
            ret = sbCmd.toString();
        } catch (Exception e) {
            e.toString();
        }
        return ret;
    }

    /**
     * 连接ADSL
     */
    public static boolean connAdsl(String oldConninfo) throws Exception {
        String adslCmd = "ifup ppp0";

        for (int i = 0; i < 3; i++) {
            String result = executeCmd(adslCmd);

            boolean status = isChangeIp(oldConninfo);
            if (status) {
                return true;
            } else {
                //oldConninfo = getStatusInfo();
                continue;//断开成功
            }
        }
        return false;//拨号失败
    }


    /**
     * 断开ADSL
     */
    public static boolean discAdsl() throws Exception {
        String cutAdsl0 = "ifdown ppp";

        for (int i = 0; i < 5; i++) {
            String cutAdsl = cutAdsl0;
            cutAdsl += i;
            String result = executeCmd(cutAdsl);
            boolean status = getStatus();
            if (status) {
                continue;
            } else
                return true;//断开成功
        }

        return false;  //断开失败
    }

    /**
     * 获取ADSL链接状态
     */
    public static boolean getStatus() throws Exception {
        String cutAdsl = "pppoe-status";
        String result = executeCmd(cutAdsl);
        if (result == null)
            return false;
        int beg = result.indexOf("inet");
        int end = result.indexOf("global ppp0");


        if (beg == -1) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 获取ADSL链接状态
     */
    public static boolean isChangeIp(String conninfo) throws Exception {
        String now = getStatusInfo();
        System.out.println("oldinfo:" + conninfo + " nowinfo:" + now);
        if (now != null) {
            if (now.equalsIgnoreCase(conninfo)) {
                return false;
            } else
                return true;
        }

        return false;
    }
    //获取状态信息

    /**
     * 重新连接ADSL
     */
    public static boolean reConnAdsl() throws Exception {
        boolean ret = false;
        String oldConnInfo = null;

        //获取老的链接信息
        oldConnInfo = getStatusInfo();

        //断开连接
        discAdsl();
        {
            if (connAdsl(oldConnInfo)) {
                ret = true;
            }
        }

        //重拨
        return ret;

    }

    /**
     * 获取ADSL链接状态
     */
    public static String getStatusInfo() {
        String cutAdsl = "pppoe-status";
        String result = "";
        try {
            result = executeCmd(cutAdsl);
            int beg = result.indexOf("inet");
            int end = result.indexOf("global ppp0");
            if (beg < end && beg > 0) {
                return result.substring(beg, end);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * @param args args[0] = 分钟数
     */
    public static void main(String[] args) throws Exception {

        int delay = 1000 * 60 * 60;
        if (args.length == 1) {
            try {
                delay = Integer.valueOf(args[0]) * 60 * 1000;
            } catch (Exception e) {
                e.toString();
            }
        }


        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    AdslManager.reConnAdsl();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 0, delay);

    }
}  
