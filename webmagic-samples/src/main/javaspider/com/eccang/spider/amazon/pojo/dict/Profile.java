package com.eccang.spider.amazon.pojo.dict;

import com.eccang.spider.base.pojo.BasePojo;

/**
 * @author Jervis
 * @version V0.2
 * @Description: 项目全局配置表
 * @date 2017/3/22 14:34
 */
public class Profile extends BasePojo {

    public boolean debug;
    public int logSaveDay;

    @Override
    public String toString() {
        return "Profile{" +
                "debug=" + debug +
                ", logSaveDay=" + logSaveDay +
                '}';
    }
}
