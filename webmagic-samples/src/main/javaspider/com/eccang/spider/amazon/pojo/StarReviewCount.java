package com.eccang.spider.amazon.pojo;

import com.eccang.spider.base.pojo.BasePojo;

/**
 * @author Jervis
 * @version V0.1
 * @Description:
 * @date 2016/10/29 17:47
 */
public class StarReviewCount extends BasePojo {

    public int star;
    public int count;

    @Override
    public String toString() {
        return "StarReviewCount{" +
                "star=" + star +
                ", count=" + count +
                '}';
    }
}