package com.dennisxu.sample.common.utils;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.dennisxu.sample.home.MainActivity;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class ApplicationUtil {
    
    private List<WeakReference<Activity>> mActivities = new ArrayList<WeakReference<Activity>>();
    
    private static ApplicationUtil sInstance;
    
    public static ApplicationUtil getInstance() {
        if (sInstance == null) {
            synchronized (ApplicationUtil.class) {
                sInstance = new ApplicationUtil();
            }
        }
        return sInstance;
    }
    
    private ApplicationUtil() {
        
    }

    public static boolean checkActivity(Activity activity) {
        if ((null == activity) || activity.isFinishing()
                || activity.isRestricted()) {
            return false;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            if (activity.isDestroyed()) {
                return false;
            }
        }

        return true;
    }

    public void addActivity(Activity activity) {
        mActivities.add(new WeakReference<Activity>(activity));
    }
    
    public void removeActivity(Activity activity) {
        Iterator<WeakReference<Activity>> iterator = mActivities.iterator();
        while (iterator.hasNext()) {
            WeakReference<Activity> ref = iterator.next();
            if (ref != null && ref.get() == activity) {
                iterator.remove();
                return;
            }
        }
    }
    
    public void removeAllActivities() {
        Iterator<WeakReference<Activity>> iterator = mActivities.iterator();
        while (iterator.hasNext()) {
            WeakReference<Activity> ref = iterator.next();
            iterator.remove();
            return;
        }
    }

    public void finishMainActivity() {
        Iterator<WeakReference<Activity>> iterator = mActivities.iterator();
        while (iterator.hasNext()) {
            WeakReference<Activity> ref = iterator.next();
            if (ref != null && ref.get() != null) {
                Activity activity = ref.get();
                if (activity instanceof MainActivity) {
                    activity.finish();
                    iterator.remove();
                    mActivities.remove(activity);
                    return;
                }
            }
        }
    }

    public void finishAllActivities() {
        Iterator<WeakReference<Activity>> iterator = mActivities.iterator();
        while (iterator.hasNext()) {
            WeakReference<Activity> ref = iterator.next();
            if (ref != null && ref.get() != null) {
                Activity activity = ref.get();
                activity.finish();
                iterator.remove();
            }
        }
        mActivities.clear();
    }
    
    public void exitAndRestartApplication(Context context) {
        finishAllActivities();
        Intent restartIntent = context.getPackageManager()
                .getLaunchIntentForPackage(context.getPackageName() );
        PendingIntent intent = PendingIntent.getActivity(
                context, 0, restartIntent, Intent.FLAG_ACTIVITY_CLEAR_TOP);
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        manager.set(AlarmManager.RTC, System.currentTimeMillis() + 1000, intent);
        System.exit(2);
    }

    public void exitAndRestartApplicationCurrent(Context context) {
        finishAllActivities();
        Intent restartIntent = context.getPackageManager()
                .getLaunchIntentForPackage(context.getPackageName() );
        PendingIntent intent = PendingIntent.getActivity(
                context, 0, restartIntent, Intent.FLAG_ACTIVITY_CLEAR_TOP);
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        manager.set(AlarmManager.RTC, System.currentTimeMillis() + 100, intent);
        System.exit(2);
    }
    
    public void exitApplication() {
        finishAllActivities();
        System.exit(0);
    }
}
