package com.eccang.intercepter;

import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Message;
import org.apache.cxf.message.MessageContentsList;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;

/**
 * @author Jervis
 * @version V0.2
 * @Description:
 * @date 2016/12/15 14:21
 */
public class ECRequestIntercepter extends AbstractPhaseInterceptor {

    public ECRequestIntercepter() {
        super(Phase.RECEIVE);
    }

    @Override
    public void handleMessage(Message message) throws Fault {
        MessageContentsList contentList = MessageContentsList.getContentsList(message);
        System.out.println(contentList);
    }
}
