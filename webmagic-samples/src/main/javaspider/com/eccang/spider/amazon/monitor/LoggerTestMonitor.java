package com.eccang.spider.amazon.monitor;

import com.eccang.spider.amazon.R;
import com.eccang.spider.base.monitor.ScheduledTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @author Jervis
 * @version V0.1
 * @Description:
 * @date 2016/10/21 9:37
 */
@Service
public class LoggerTestMonitor implements ScheduledTask {

    private static final Logger mLogger = LoggerFactory.getLogger(LoggerTestMonitor.class);
    private static final Logger mLoggerBusiness = LoggerFactory.getLogger(R.BusinessLog.AS);

    @Override
    public void execute() {
        print();
    }

    private void print() {
        mLogger.debug("debug level ...");
        mLogger.info("info level ...");
        mLogger.warn("warn level ...");
        mLogger.error("error level ...");

        mLoggerBusiness.debug("business debug...");
        mLoggerBusiness.info("business info...");
        mLoggerBusiness.warn("business warn...");
    }
}