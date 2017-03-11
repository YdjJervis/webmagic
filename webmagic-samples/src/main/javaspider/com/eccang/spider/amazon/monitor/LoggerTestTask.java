package com.eccang.spider.amazon.monitor;

import com.eccang.spider.base.monitor.ScheduledTask;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

/**
 * @author Jervis
 * @version V0.1
 * @Description:
 * @date 2016/10/21 9:37
 */
@Service
public class LoggerTestTask implements ScheduledTask {

    private Logger mLogger = Logger.getLogger(getClass());

    @Override
    public void execute() {
        mLogger.debug("debug level ...");
        mLogger.info("info level ...");
        mLogger.warn("warn level ...");
        mLogger.error("error level ...");
        mLogger.fatal("fatal level ...");
    }
}