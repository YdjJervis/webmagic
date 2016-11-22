package com.eccang.pojo;

/**
 * @author Jervis
 * @version V0.20
 * @Description:
 * @date 2016/11/22 9:41
 */
public class AsinPriorityRsp extends BaseRspParam {

    public Effect data = new Effect();

    public class Effect {
        public int changed;
        public int noChange;
    }
}