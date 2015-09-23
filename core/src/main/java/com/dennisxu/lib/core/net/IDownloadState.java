package com.dennisxu.lib.core.net;
/**
 * @Description 下载进度回调
 * @author xuyang5
 * @date 2014-1-26 上午11:24:19
 */
public interface IDownloadState {

    public void onStart(Object arg);
    
    public void onProgressChanged(final int percent);
    
    public void onComplete(Object arg);
    
    public void onFail(Object arg);
}
