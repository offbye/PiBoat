package com.qianmi.boat;

import android.app.Application;

import com.baidu.mapapi.SDKInitializer;

/**
 * Created by su on 2015/8/21.
 */
public class MainApplication extends Application {

  /*  public LocationClient mLocationClient;
    public MyLocationListener mMyLocationListener;*/
  @Override
  public void onCreate() {
    super.onCreate();
    // 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
    SDKInitializer.initialize(this);
  }
}
