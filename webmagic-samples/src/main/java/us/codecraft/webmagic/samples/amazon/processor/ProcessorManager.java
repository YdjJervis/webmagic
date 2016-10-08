package us.codecraft.webmagic.samples.amazon.processor;

import org.springframework.stereotype.Service;
import us.codecraft.webmagic.samples.base.monitor.ScheduledTask;

/**
 * Processor管理器，把所有任务在这里统一管理
 */
@Service
public class ProcessorManager implements ScheduledTask {

    @Override
    public void execute() {

    }
}
