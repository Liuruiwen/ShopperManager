package com.rw.map.ui

import android.content.res.Resources
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.lifecycle.Observer
import com.alibaba.android.arouter.facade.annotation.Route
import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode
import com.amap.api.location.AMapLocationListener
import com.amap.api.maps.*
import com.amap.api.maps.model.*
import com.rw.basemvp.widget.TitleView
import com.rw.map.BaseMapActivity
import com.rw.map.HttpApi
import com.rw.map.R
import com.rw.map.bean.ClockStateReq
import com.rw.map.bean.ClockStateResultBean
import com.rw.map.bean.ClockWorkReq
import com.rw.map.model.MapModel
import com.rw.map.presenter.MapPresenter
import kotlinx.android.synthetic.main.map_activity_employees_clock.*
import java.text.SimpleDateFormat
import java.util.*


@Route(path = "/map/ClockActivity")
class EmployeesClockActivity : BaseMapActivity<MapPresenter>(), AMapLocationListener {
    private var mapView: MapView? = null
    private var aMap: AMap? = null
    private var mUiSettings: UiSettings? = null
    private var currentLatLng:LatLng?=null
    private var shopperLatLng:LatLng?=null
    private var currentAddress:String?=null
    private var clockState:String="1"
    private var clockTime:String?=null

    //声明mlocationClient对象
    var mlocationClient: AMapLocationClient? = null

    //声明mLocationOption对象
    var mLocationOption: AMapLocationClientOption? = null
    override fun setLayout(): Int {
        return R.layout.map_activity_employees_clock
    }

    override fun initData(savedInstanceState: Bundle?, titleView: TitleView) {
        titleView.setTitle("员工打卡")
        mapView = findViewById(R.id.map)
        initMap(savedInstanceState)
    }

    override fun getPresenter(): MapPresenter {
        return MapPresenter()
    }

    private fun initMap(savedInstanceState: Bundle?){
        mapView?.onCreate(savedInstanceState) // 此方法必须重写
        if (aMap == null) {
            aMap = mapView?.map
            mUiSettings = aMap!!.uiSettings
        }


//        val  myLocationStyle = MyLocationStyle() //初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
//
//        myLocationStyle.interval(9000000) //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
//
//        aMap?.mapType = AMap.MAP_TYPE_NORMAL
//        aMap?.myLocationStyle = myLocationStyle //设置定位蓝点的Style
//
//        aMap?.uiSettings?.isZoomControlsEnabled = true //隐藏放大缩小按钮
//
//        myLocationStyle.showMyLocation(true) //设置是否显示定位小蓝点，用于满足只想使用定位，不想使用定位小蓝点的场景，设置false以后图面上不再有定位蓝点的概念，但是会持续回调位置信息。
//
//        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW_NO_CENTER) //连续定位、蓝点不会移动到地图中心点，并且蓝点会跟随设备移动。
//
//        aMap?.isMyLocationEnabled = true // 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。

        aMap?.minZoomLevel = 18.0f

        tv_clock_work.text="打卡"
        tv_go_clock.text="上班卡"

        tv_clock_after_work.text="打卡"
        tv_after_clock.text="下班卡"
        updateShapeColor(tv_clock_work,"#EEEEEE","#666666")
        updateShapeColor(tv_clock_after_work,"#EEEEEE","#666666")

        requestResult()
        initLocation()
        click()
    }

    private fun requestResult(){

        getViewModel()?.baseBean?.observe(this, Observer {
            when (it?.requestType) {
                HttpApi.HTTP_CLOCK_STATE -> {
                   if (it is ClockStateResultBean){
                       if (shopperLatLng!=null&& currentLatLng!=null){
                           val distance = AMapUtils.calculateLineDistance(shopperLatLng,currentLatLng)
                           if (!it.data?.clockTime.isNullOrEmpty()){
                               tv_clock_work.text="已打卡"
                               tv_go_clock.text="上班卡:${it.data?.clockTime}"
                               updateShapeColor(tv_clock_work,"#EEEEEE","#666666")
                           }else{
                               if (distance<=100){
                                   updateShapeColor(tv_clock_work,"#33a3dc","#FFFFFF")
                               }else{
                                   updateShapeColor(tv_clock_work,"#EEEEEE","#666666")
                               }
                           }

                           if (!it.data?.afterWorkTime.isNullOrEmpty()){
                               tv_clock_after_work.text="已打卡"
                               tv_after_clock.text="下班卡:${it.data?.afterWorkTime}"
                               updateShapeColor(tv_clock_after_work,"#EEEEEE","#666666")
                           }else{
                               if (distance<=100){
                                   updateShapeColor(tv_clock_after_work,"#33a3dc","#FFFFFF")
                               }else{
                                   updateShapeColor(tv_clock_after_work,"#EEEEEE","#666666")
                               }
                           }



                       }

                   }
                }
                HttpApi.HTTP_CLOCK_WORK->{
                    if (clockState=="1"){
                        tv_clock_work.text="已打卡"
                        tv_go_clock.text="上班卡:${clockTime}"
                        updateShapeColor(tv_clock_work,"#EEEEEE","#666666")
                    }else{
                        tv_clock_after_work.text="已打卡"
                        tv_after_clock.text="下班卡:${clockTime}"
                        updateShapeColor(tv_clock_after_work,"#EEEEEE","#666666")
                    }
                }
                else -> showToast("系统异常")
            }
        })
    }

    private fun click(){
        tv_clock_work.setOnClickListener { //打上班卡
            clockReq("1")
        }
        tv_clock_after_work.setOnClickListener { //打下班卡
            clockReq("2")
        }


    }

    private fun clockReq(state:String){
        clockState=state
        clockTime=getCurrentDate("yyyy-MM-dd HH:mm:ss")
        mPresenter?.clockWork(ClockWorkReq(clockTime?:"",
            currentAddress?:"",clockState))
    }

    /**
     * 获取当前日期
     *
     * @return
     */
    fun getCurrentDate(time: String?): String? {
        //"yyyy-MM-dd HH:mm:ss"
        val df = SimpleDateFormat(time)
        return df.format(Date())
    }

    private fun updateShapeColor(view: TextView,color:String,fontColor:String){

        view.isEnabled= color != "#EEEEEE"
         val drawable=view.background
        view.setTextColor(Color.parseColor(fontColor))
        if (drawable is GradientDrawable){
            drawable.setColor(Color.parseColor(color))
        }
    }

    /**
     * 方法必须重写
     */
    override fun onResume() {
        super.onResume()
        mapView!!.onResume()

    }


    /**
     * 方法必须重写
     */
    override fun onPause() {
        super.onPause()
        mapView!!.onPause()
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView!!.onSaveInstanceState(outState)
    }


    /**
     * 方法必须重写
     */
    override fun onDestroy() {
        super.onDestroy()
        mapView!!.onDestroy()
    }



    private fun initLocation(){
        mlocationClient = AMapLocationClient(this)
//初始化定位参数
//初始化定位参数
        mLocationOption = AMapLocationClientOption()
//设置定位监听
//设置定位监听
        mlocationClient?.setLocationListener(this)
//设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
//设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption?.locationMode = AMapLocationMode.Hight_Accuracy
//设置定位间隔,单位毫秒,默认为2000ms
//设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption?.interval = 2000
//设置定位参数
//设置定位参数
        mlocationClient?.setLocationOption(mLocationOption)
// 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
// 注意设置合适的定位时间的间隔（最小间隔支持为1000ms），并且在合适时间调用stopLocation()方法来取消定位请求
// 在定位结束后，在合适的生命周期调用onDestroy()方法
// 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
//启动定位
// 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
// 注意设置合适的定位时间的间隔（最小间隔支持为1000ms），并且在合适时间调用stopLocation()方法来取消定位请求
// 在定位结束后，在合适的生命周期调用onDestroy()方法
// 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
//启动定位
        mlocationClient?.startLocation()
    }

    override fun onLocationChanged(amapLocation:AMapLocation?) {
        if (amapLocation != null) {
            if (amapLocation.errorCode === 0) {
                mlocationClient?.stopLocation()
                //定位成功回调信息，设置相关消息
                amapLocation.locationType //获取当前定位结果来源，如网络定位结果，详见定位类型表
                amapLocation.latitude //获取纬度
                amapLocation.longitude //获取经度
                amapLocation.accuracy //获取精度信息
                val latlng=LatLng(amapLocation.latitude,amapLocation.longitude)
                updateMapLocation(latlng)
                drawableCircle()
                currentLatLng=latlng
                currentAddress="${amapLocation.latitude }_${amapLocation.longitude}"
                mPresenter?.getClockState(ClockStateReq(getCurrentDate("yyyy-MM-dd HH:mm:ss")?:""))
                val df =
                    SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                val date = Date(amapLocation.time)
                df.format(date) //定位时间
            } else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                Log.e(
                    "AmapError", ("location Error, ErrCode:"
                            + amapLocation.errorCode) + ", errInfo:"
                            + amapLocation.errorInfo
                )
            }
        }
    }

    /**
     * 更新地图位置
     */
    private fun updateMapLocation(latLonPoint: LatLng){
        changeCamera(
            CameraUpdateFactory.newCameraPosition(
                CameraPosition(
                    latLonPoint, 18.0F, 30F, 0F
                )
            ))
        aMap?.clear()
        aMap?.addMarker(
            MarkerOptions().position(latLonPoint)
                .icon(
                    BitmapDescriptorFactory.fromBitmap(BitmapFactory
                        .decodeResource(resources,R.drawable.map_location_marker))))
    }

    /**
     * 根据动画按钮状态，调用函数animateCamera或moveCamera来改变可视区域
     */
    private fun changeCamera(update: CameraUpdate) {
        aMap?.moveCamera(update)
    }


    /**
     * 打卡位置画圈圈
     */
    private fun drawableCircle(){
        val latitude:String?
        val longitude:String?
        val mapMessage= MapModel.get()?.address?.value
        if (mapMessage!=null){
            latitude=mapMessage.latitude
            longitude=mapMessage.longitude
        }else{
            latitude = intent.getStringExtra("latitude")
            longitude = intent.getStringExtra("longitude")
        }


        if (!latitude.isNullOrEmpty()&& !longitude.isNullOrEmpty()){
            shopperLatLng= LatLng(latitude.toDouble(),longitude.toDouble())
            aMap?.addCircle(
                CircleOptions().center(shopperLatLng).radius(100.0).fillColor(Color.argb(50, 0, 0, 255))
                    .strokeColor(Color.argb(1,0, 0, 255)).strokeWidth(18f)
            )
        }

    }
}