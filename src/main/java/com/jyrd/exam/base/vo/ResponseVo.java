package com.jyrd.exam.base.vo;

/**
 * 统一响应ResponseVo
 */
public class ResponseVo {
    protected boolean succ;
    protected String msg;
    protected Object data;

    public ResponseVo() {
    }

    public ResponseVo(boolean succ, String msg,Object data) {
        this.succ = succ;
        this.msg = msg;
        this.data = data;
    }

    public ResponseVo(boolean succ, String msg) {
        this.succ = succ;
        this.msg = msg;
    }

    public boolean isSucc() {
        return succ;
    }

    public void setSucc(boolean succ) {
        this.succ = succ;
    }

    public void setData(Object data){
        this.data = data;
    }

    public Object getData(){
     return data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
