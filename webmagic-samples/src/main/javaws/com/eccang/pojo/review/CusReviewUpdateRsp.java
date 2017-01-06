package com.eccang.pojo.review;

import com.eccang.pojo.BaseRspParam;

/**
 * @author Hardy
 * @version V0.2
 * @Description:
 * @date 2016/12/12 11:51
 */
public class CusReviewUpdateRsp extends BaseRspParam {

    public Effect data = new Effect();

    public class Effect {
        public int changed;
        public int noChange;
        public int usableNum; /*可用数*/
        public int hasUsedNum; /*已用数*/
    }
}