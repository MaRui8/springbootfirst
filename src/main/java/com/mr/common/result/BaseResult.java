package com.mr.common.result;

public class BaseResult {

    /**
     * 0成功
     * 其他失败
     */
    private Integer code;
    /**
     * 结果描述
     */
    private String msg;

    public BaseResult() {
        code = 0;
    }

    public BaseResult(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
