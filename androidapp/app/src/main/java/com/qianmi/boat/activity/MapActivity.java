package com.qianmi.boat.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.qianmi.boat.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by su on 2015/8/21.
 */
public class MapActivity extends AppCompatActivity {

    @Bind(R.id.btn_loc)
    RelativeLayout btnLoc;
    @Bind(R.id.btn_roate)
    RelativeLayout btnRoate;

    private String Tag = "MapActivity";
    private LatLng lastLatLng;

    private BDLocation curLocation;

    LocationClient mLocClient;
    public MyLocationListenner myListener = new MyLocationListenner();
    private MyLocationConfiguration.LocationMode mCurrentMode;
    BitmapDescriptor mCurrentMarker;
    private LatLng tempLatLng;

    MapView mMapView;
    BaiduMap mBaiduMap;
    boolean isFirstLoc = true;// 是否首次定位
    boolean isFirstCanvas = true;

    private int count = 15;
    private int currCount = 0;
    int ax = 1;
    int ay = 3;
    int bx = 1;
    int by = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        ButterKnife.bind(this);

        mCurrentMode = MyLocationConfiguration.LocationMode.FOLLOWING;

        initMapAndLoc();
        addListener();
    }
    public void addCustomElementsDemo(LatLng latLng1, LatLng latLng2) {
        List<LatLng> points = new ArrayList<LatLng>();
        points.add(latLng1);
        points.add(latLng2);
        OverlayOptions ooPolyline = new PolylineOptions().width(10)
                .color(0xAAFF0000).points(points);
        mBaiduMap.addOverlay(ooPolyline);

    }

    public void addCustomEleemntsDemo(List<LatLng> latLngList) {
        List<LatLng> points = new ArrayList<LatLng>();
        points.addAll(latLngList);
        OverlayOptions ooPolyline = new PolylineOptions().width(10)
                .color(0xAAFF0000).points(points);
        mBaiduMap.addOverlay(ooPolyline);
    }

    public void addCustomEleemntsDemo(LatLng latLng) {
        if (lastLatLng == null) {
            lastLatLng = latLng;
            return;
        } else {
            addCustomElementsDemo(lastLatLng, latLng);
            lastLatLng = latLng;
        }
    }


    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null || mMapView == null)
                return;
            curLocation = location;
           /* MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                            // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);*/
            if (isFirstCanvas) {
                if (location.getLatitude() > 10 && location.getLongitude() > 10) {
//                    isFirstCanvas = false;
                    // addCustomElementsDemo((location.getLatitude()), (location.getLongitude()));
                    if(tempLatLng == null) {
                        tempLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                    }else{
                        currCount ++;
                        if(currCount % 45 == 0) {
                            currCount = 0;
                        }else if (currCount % 30 == 0) {
                            ax = -ax;
                        }else if (currCount % 15 == 0) {
                            bx = - bx;
                        }
                        tempLatLng = new LatLng(tempLatLng.latitude + (ax + new Random().nextInt(ay)) / 10000f, tempLatLng.longitude + (bx + new Random().nextInt(by)) / 10000f);
                    }
                    addCustomEleemntsDemo(tempLatLng);
                    Log.v(Tag, "location = " + location.getLatitude() + "  唯独" + location.getLongitude());
                }
            }
            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
                mBaiduMap.animateMapStatus(u);
            }
        }

        public void onReceivePoi(BDLocation poiLocation) {
        }
    }


    private void initMapAndLoc() {
        // 地图初始化
        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        // 定位初始化
        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);// 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        mLocClient.setLocOption(option);
        mLocClient.start();

        mBaiduMap
                .setMyLocationConfigeration(new MyLocationConfiguration(
                        mCurrentMode, true, mCurrentMarker));

    }

    private void addListener() {
       // btnLoc.
        btnLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (curLocation == null) {
                    return;
                }
                MyLocationData locData = new MyLocationData.Builder()
                        .accuracy(curLocation.getRadius())
                                // 此处设置开发者获取到的方向信息，顺时针0-360
                        .direction(100).latitude(curLocation.getLatitude())
                        .longitude(curLocation.getLongitude()).build();
                mBaiduMap.setMyLocationData(locData);
            }
        });
        btnRoate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MapStatus ms = new MapStatus.Builder(mBaiduMap.getMapStatus()).rotate(0).build();
                MapStatusUpdate u = MapStatusUpdateFactory.newMapStatus(ms);
                mBaiduMap.animateMapStatus(u);
            }
        });
    }


    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mMapView.onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        // 退出时销毁定位
        mLocClient.stop();
        // 关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        mMapView = null;
        super.onDestroy();
    }

}
