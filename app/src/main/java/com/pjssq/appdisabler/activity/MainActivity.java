package com.pjssq.appdisabler.activity;

import android.content.pm.PackageInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Switch;
import android.widget.TextView;

import com.pjssq.appdisabler.domain.AppInfo;
import com.pjssq.appdisabler.R;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends ActionBarActivity {

    List<String> appInstalledList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void init(){
        ListView appLv= (ListView) findViewById(R.id.appList);
       // appInstalledList=new ArrayList();
        ArrayList<AppInfo> appList = new ArrayList<AppInfo>(); //用来存储获取的应用信息数据　　　　　
         List<PackageInfo> packages = getPackageManager().getInstalledPackages(0);

        for(int i=0;i<packages.size();i++) {
            PackageInfo packageInfo = packages.get(i);
            AppInfo tmpInfo = new AppInfo();
            tmpInfo.appName = packageInfo.applicationInfo.loadLabel(getPackageManager()).toString();
            tmpInfo.packageName = packageInfo.packageName;
            tmpInfo.versionName = packageInfo.versionName;
            tmpInfo.versionCode = packageInfo.versionCode;
            tmpInfo.enabled=packageInfo.applicationInfo.enabled;
            tmpInfo.appIcon = packageInfo.applicationInfo.loadIcon(getPackageManager());
            appList.add(tmpInfo);

        }//好啦 这下手机上安装的应用数据都存在appList里了。
       int a=1;
        List<HashMap<String,Object>> data=new ArrayList<HashMap<String, Object>>();
        for(AppInfo appInfo:appList){
            HashMap<String,Object> map=new HashMap<String, Object>();
            map.put("icon",appInfo.appIcon);
            map.put("appName",appInfo.appName);
            map.put("appPackageName",appInfo.packageName);
            map.put("enabled",appInfo.enabled);
            data.add(map);
        }
        SimpleAdapter simpleAdapter=new SimpleAdapter(this,data,R.layout.app_item,new String[]{"icon","appName","appPackageName","enabled"},new int[]{R.id.icon,R.id.appName,R.id.appPackageName,R.id.disableSwitch});
        appLv.setAdapter(simpleAdapter);
        simpleAdapter.setViewBinder(new SimpleAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(final View view, Object data, String textRepresentation) {
                if(view instanceof ImageView && data instanceof Drawable){
                    ImageView iv = (ImageView)view;
                    iv.setImageDrawable((Drawable)data);
                    return true;
                }else if (view.getId()==R.id.disableSwitch){

                    Switch st=(Switch)view;
                    st.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (isChecked) {
                                ((TextView)(((LinearLayout) (view.getParent())).findViewById(R.id.appPackageName))).getText().toString();
                                    enableApp(((TextView)(((LinearLayout) (view.getParent())).findViewById(R.id.appPackageName))).getText().toString());
                            } else {

                                disableApp(((TextView)(((LinearLayout) (view.getParent())).findViewById(R.id.appPackageName))).getText().toString());
                            }
                        }
                    });
                    boolean enabled=(Boolean)data;
                    st.setChecked(enabled);
                    return  true;
                }
                else{
                    return false;
                }
            }
        });
    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.button:
                init();
                break;
        }
    }
    public void enableApp(String appPackageName){

        try {
            Process localProcess=Runtime.getRuntime().exec("su");
            String cmd="pm enable "+appPackageName+"\n";
            DataOutputStream dataOutputStream=new DataOutputStream(localProcess.getOutputStream());
            dataOutputStream.writeBytes(cmd);
            dataOutputStream.flush();
            dataOutputStream.writeBytes("exit\n");

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void disableApp(String appPackageName){
        try {
            Process localProcess=Runtime.getRuntime().exec("su");
            String cmd="pm disable "+appPackageName+"\n";
            DataOutputStream dataOutputStream=new DataOutputStream(localProcess.getOutputStream());
            dataOutputStream.writeBytes(cmd);
            dataOutputStream.flush();
            dataOutputStream.writeBytes("exit\n");

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}



//        try {
//            Process localProcess=Runtime.getRuntime().exec("su");
//            String cmd="pm list packages\n";
//            DataOutputStream dataOutputStream=new DataOutputStream(localProcess.getOutputStream());
//            dataOutputStream.writeBytes(cmd);
//            dataOutputStream.flush();
//            dataOutputStream.writeBytes("exit\n");
//            InputStream inputStream = localProcess
//                    .getInputStream();
//            InputStreamReader inputStreamReader = new InputStreamReader(
//                    inputStream);
//            BufferedReader bufferedReader = new BufferedReader(
//                    inputStreamReader);
//            String line = "";
//            StringBuilder stringBuilder = new StringBuilder(
//                    line);
//            while ((line = bufferedReader.readLine()) != null) {
//                stringBuilder.append(line);
//                stringBuilder.append("|");
//
//            }
//            String  a= stringBuilder.toString();
//            String b=a.replace("package:", "");
//            String[] appArray=stringBuilder.toString().replace("package:", "").split("\\|");
//            appInstalledList= Arrays.asList(appArray);
//            String c=appInstalledList.get(2);
//           int aa=1;
//        } catch (IOException e) {
//            e.printStackTrace();
//        }