package com.dennisxu.lib.core.net;

import com.dennisxu.lib.core.entity.Entity;
import com.dennisxu.lib.core.entity.KeepAttr;

/**
 * Response 请求返回
 * 
 * @author xuwei19
 * @date 2014年11月26日 下午12:22:50
 */
public class Response<T>  implements KeepAttr{

    private int mErrno;
    private String mMsg;
    private Entity<T> mData;

    private String mResultStr;
    private Throwable mError;

    public int getErrno() {
        return mErrno;
    }

    public void setErrno(int errno) {
        this.mErrno = errno;
    }

    public String getMsg() {
        return mMsg;
    }

    public void setMsg(String msg) {
        this.mMsg = msg;
    }

    public Entity<T> getData() {
        return mData;
    }

    public void setData(Entity<T> data) {
        this.mData = data;
    }

    public String getResultStr() {
        return mResultStr;
    }

    public void setResultStr(String resultStr) {
        this.mResultStr = resultStr;
    }

    public Throwable getError() {
        return mError;
    }

    public void setError(Throwable error) {
        this.mError = error;
    }
}
