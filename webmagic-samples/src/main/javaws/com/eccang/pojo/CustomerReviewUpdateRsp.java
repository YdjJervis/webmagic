package com.eccang.pojo;

/**
 * @author Hardy
 * @version V0.2
 * @Description:
 * @date 2016/12/12 11:51
 */
public class CustomerReviewUpdateRsp extends BaseRspParam {

    public Effect data = new Effect();

    public class Effect {
        public int changed;
        public int noChange;
    }
}