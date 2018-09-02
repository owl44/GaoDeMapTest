package com.map.gaodemaptest;

import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

public class MarkerActivity extends AppCompatActivity implements AMap.OnMarkerClickListener {

    List<Marker> markers = new ArrayList<>();
    List<LatLng> latLngs = new ArrayList<>();
    AMap aMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marker2);
        MapView mapView = findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        aMap = mapView.getMap();
        aMap.setOnMarkerClickListener(this);
        //绘制marker
        Marker marker = aMap.addMarker(new MarkerOptions()
                .position(new LatLng(39.986919,116.353369))
                .icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                        .decodeResource(getResources(),R.drawable.start)))
                .draggable(true));
        markers.add(marker);
        latLngs.add(new LatLng(39.986919,116.353369));
        Marker marker1 = aMap.addMarker(new MarkerOptions()
                .position(new LatLng(39.386919,116.953369))
                .icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                        .decodeResource(getResources(),R.drawable.gps_point)))
                .draggable(true));
        markers.add(marker1);
        latLngs.add(new LatLng(39.386919,116.953369));
        Marker marker2 = aMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_car))
                .position(new LatLng(40.186919,116.353369))
                .draggable(true));
        markers.add(marker2);
        latLngs.add(new LatLng(40.186919,116.353369));
        // 绘制曲线
        aMap.addPolyline((new PolylineOptions())
                .add(new LatLng(39.986919,116.353369), new LatLng(39.986919,116.353369))
                .geodesic(true).color(Color.RED));

        zoomToSpanWithCenter();
     /*   //多个Marker标记自动缩放全部显示在屏幕中
        LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();//存放所有点的经纬度
        for(int i=0;i<markers.size();i++){
            boundsBuilder.include(markers.get(i).getPosition());//把所有点都include进去（LatLng类型）
        }
        aMap.animateCamera(CameraUpdateFactory.newLatLngBounds(boundsBuilder.build(), 55));//第二个参数为四周留空宽度*/
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        for(int i=0;i<markers.size();i++){
            if(marker.getId().equals(markers.get(i).getId())){
                Toast.makeText(this, "点击了第"+i+"个marker", Toast.LENGTH_SHORT).show();
            }
        }

        return false;
    }


    /**
     * 缩放移动地图，保证所有自定义marker在可视范围中，且地图中心点不变。
     */
    public void zoomToSpanWithCenter() {
        if (latLngs != null && latLngs.size() > 0) {
            if (aMap == null)
                return;
            markers.get(0).setVisible(true);
            markers.get(0).showInfoWindow();
            LatLngBounds bounds = getLatLngBounds(latLngs.get(0), latLngs);
            aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50));
        }
    }

    //根据中心点和自定义内容获取缩放bounds
    private LatLngBounds getLatLngBounds(LatLng centerpoint, List<LatLng> pointList) {
        LatLngBounds.Builder b = LatLngBounds.builder();
        if (centerpoint != null){
            for (int i = 0; i < pointList.size(); i++) {
                LatLng p = pointList.get(i);
                LatLng p1 = new LatLng((centerpoint.latitude * 2) - p.latitude, (centerpoint.longitude * 2) - p.longitude);
                b.include(p);
                b.include(p1);
            }
        }
        return b.build();
    }

    /**
     *  缩放移动地图，保证所有自定义marker在可视范围中。
     */
    public void zoomToSpan() {
        if (latLngs != null && latLngs.size() > 0) {
            if (aMap == null)
                return;
            markers.get(0).setVisible(false);
            LatLngBounds bounds = getLatLngBounds(latLngs);
            aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50));
        }
    }
    /**
     * 根据自定义内容获取缩放bounds
     */
    private LatLngBounds getLatLngBounds( List<LatLng> pointList) {
        LatLngBounds.Builder b = LatLngBounds.builder();
        for (int i = 0; i < pointList.size(); i++) {
            LatLng p = pointList.get(i);
            b.include(p);
        }
        return b.build();
    }
}
