package com.eccang.spider.amazon.monitor;

import com.eccang.spider.amazon.pojo.dict.Profile;
import com.eccang.spider.amazon.service.dict.ProfileService;
import com.eccang.spider.base.monitor.ScheduledTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * @author Jervis
 * @version V0.1
 * @Description: 定时删除数据库配置的日志保存天数之前的日志文件
 * @date 2017/03/23
 */
@Service
public class LoggerDeleteMonitor implements ScheduledTask {

    private static final Logger mLogger = LoggerFactory.getLogger(LoggerDeleteMonitor.class);
    @Autowired
    private ProfileService mProfileService;

    @Override
    public void execute() {
        Profile profile = mProfileService.find();

        Date now = new Date();

        String logSavePath = System.getProperty("webapp.root") + File.separator + "WEB-INF" + File.separator + "log";

        File file = new File(logSavePath);
        File[] logs = file.listFiles();

        if (logs == null || logs.length == 0) {
            return;
        }

        for (File log : logs) {
            if (isLogWithDate(log.getName()) && dateDiff(log.getName(), now) > profile.logSaveDay) {
                log.delete();
                mLogger.info("删除日志文件：" + log.getAbsolutePath());
            }
        }
    }

    private boolean isLogWithDate(String fileName) {
        return Pattern.compile(".*[0-9]{4}.*").matcher(fileName).matches();
    }

    private int dateDiff(String fileName, Date now) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH");

        String dataStr = fileName.substring(fileName.indexOf("_") + 1, fileName.length() - 4);
        try {
            Date fileDate = sdf.parse(dataStr);
            return differentDays(fileDate, now);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return 0;
    }

    /**
     * date2比date1多的天数
     */
    private int differentDays(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        int day1 = cal1.get(Calendar.DAY_OF_YEAR);
        int day2 = cal2.get(Calendar.DAY_OF_YEAR);

        int year1 = cal1.get(Calendar.YEAR);
        int year2 = cal2.get(Calendar.YEAR);
        if (year1 != year2) {//同一年

            int timeDistance = 0;
            for (int i = year1; i < year2; i++) {

                if (i % 4 == 0 && i % 100 != 0 || i % 400 == 0) {//闰年
                    timeDistance += 366;
                } else {//不是闰年
                    timeDistance += 365;
                }

            }

            return timeDistance + (day2 - day1);
        } else { //不同年
            return day2 - day1;
        }
    }

}