package com.dennisxu.lib.core.common;

import android.os.MessageQueue.IdleHandler;

import com.dennisxu.lib.core.util.LogUtil;

import java.util.LinkedList;
import java.util.List;

/**
 * 内存检测、低内存警告handler
 *
 * @author: xuyang
 * @date: 2014-8-18 上午11:47:56
 */
public class LowMemoryMonitor implements IdleHandler {

    private static final float MIN_FREE_MEM = 0.2f;//最小安全内存值使用比，根据后台oom log查看，发生oom是内存占用比一般为80%--85%

    private static final float NORMAL_FREE_MEM = 0.3f;

    private List<LowMemoryListener> memoryListeners = new LinkedList<LowMemoryListener>();

    private boolean isLowMemState = false;

    public synchronized void regLowMemoryListener(LowMemoryListener listener) {
        memoryListeners.add(listener);
    }

    public synchronized void unregLowMemoryListener(LowMemoryListener listener) {
        memoryListeners.remove(listener);
    }

    public interface LowMemoryListener {
        public void onLowMemory();
    }

    public interface LowMemoryListener2 extends LowMemoryListener {
        public void onNormalMemory();
    }

    /* (non-Javadoc)
     * @see android.os.MessageQueue.IdleHandler#queueIdle()
     *
     * UI线程，不需要同步
     */
    @Override
    public boolean queueIdle() {
        LogUtil.d("LowMemoryMonitor", "LowMemoryMonitor called");
        float freeMem = Runtime.getRuntime().freeMemory() / (float) Runtime.getRuntime().totalMemory();
        LogUtil.d("LowMemoryMonitor", "free meme : " + freeMem);
        if (freeMem < MIN_FREE_MEM) {
            for (LowMemoryListener l : memoryListeners) {
                l.onLowMemory();
            }
            isLowMemState = true;
//			System.gc();
            LogUtil.d("LowMemoryMonitor", "release memory ,add free mem : " + Runtime.getRuntime().freeMemory());
        } else if (freeMem >= NORMAL_FREE_MEM && isLowMemState) {
            isLowMemState = false;
            for (LowMemoryListener l : memoryListeners) {
                if (l instanceof LowMemoryListener2) {
                    ((LowMemoryListener2) l).onNormalMemory();
                }
            }
        }
        return true;
    }

}
