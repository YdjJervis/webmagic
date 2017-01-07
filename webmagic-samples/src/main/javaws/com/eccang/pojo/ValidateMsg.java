package com.eccang.pojo;

import com.eccang.R;

/**
 * @author Jervis
 * @version V0.2
 * @Description:
 * @date 2017/1/5 13:51
 */
public class ValidateMsg {

    public boolean isSuccess = true;
    public String msg = R.RequestMsg.PARAMETER_DATA_NULL_ERROR;

    public ValidateMsg(boolean isSuccess, String msg) {
        this.isSuccess = isSuccess;
        this.msg = msg;
    }
}
