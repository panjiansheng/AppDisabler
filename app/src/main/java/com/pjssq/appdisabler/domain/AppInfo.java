package com.pjssq.appdisabler.domain;

import android.graphics.drawable.Drawable;
import android.util.Log;

/**
 * Created by Pan on 2016-05-25.
 */
public class AppInfo {
    public String appName="";
    public String packageName="";
    public String versionName="";
    public int versionCode=0;
    public Drawable appIcon=null;
    public boolean enabled=true;
    public void print()
    {
        Log.v("app", "Name:" + appName + " Package:" + packageName);
        Log.v("app","Name:"+appName+" versionName:"+versionName);
        Log.v("app","Name:"+appName+" versionCode:"+versionCode);
    }

}
