package com.rw.map.ui

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.lifecycle.Observer
import com.alibaba.android.arouter.facade.annotation.Route
import com.amap.api.maps.AMap
import com.amap.api.maps.CameraUpdate
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.MapsInitializer
import com.amap.api.maps.model.*
import com.amap.api.services.core.LatLonPoint
import com.amap.api.services.geocoder.GeocodeResult
import com.amap.api.services.geocoder.GeocodeSearch
import com.amap.api.services.geocoder.RegeocodeQuery
import com.amap.api.services.geocoder.RegeocodeResult
import com.rw.basemvp.until.setVisible
import com.rw.basemvp.widget.TitleView
import com.rw.map.BaseMapActivity
import com.rw.map.HttpApi
import com.rw.map.R
import com.rw.map.bean.MapReq
import com.rw.map.presenter.MapPresenter
import kotlinx.android.synthetic.main.map_activity_add_address.*

@Route(path = "/map/AddAddressActivity")
class AddAddressActivity : BaseMapActivity<MapPresenter>(),GeocodeSearch.OnGeocodeSearchListener  {

    private var geocoderSearch: GeocodeSearch? = null
    private var mLatitude:String?=null//经度
    private var mLongitude:String?=null//纬度
    private var mapType:Int=1
    private var  aMap:AMap?=null
    private var addressId:Int=0
    override fun getPresenter(): MapPresenter {
        return MapPresenter()
    }

    override fun setLayout(): Int {
        return R.layout.map_activity_add_address
    }

    override fun initData(savedInstanceState: Bundle?, titleView: TitleView) {
        MapsInitializer.updatePrivacyShow(this,true,true)
        MapsInitializer.updatePrivacyAgree(this,true)
        titleView.setTitle("让我说点什么好")
        val  tvRight=titleView.getView<TextView>(R.id.tv_title_right)
        tvRight?.setVisible(true)
        tvRight?.text="提交修改"
        tvRight?.setOnClickListener {
                 if (!mLatitude.isNullOrEmpty()&&!mLongitude.isNullOrEmpty()){
                    mPresenter?.addOrEditAddress(MapReq(tv_address.text.toString().trim()
                             ,mLongitude?:"",mLatitude?:"",mapType,addressId
                    ))
                 }else{
                     showToast("请选择修改地址")
                 }
        }
        addressId=intent.getIntExtra("id",0)
        mLatitude=intent.getStringExtra("latitude")
        mLongitude=intent.getStringExtra("longitude")
        initMap(savedInstanceState)

        getViewModel()?.baseBean?.observe(this, Observer {
            when (it?.requestType) {
                HttpApi.HTTP_ADD_ADDRESS -> {
                    finish()
                }
                else -> showToast("系统异常")
            }
        })
    }


    override fun onPause() {
        super.onPause()
        map.onPause()
    }

    override fun onResume() {
        super.onResume()
        map.onResume()

    }

    override fun onDestroy() {
        super.onDestroy()
        map.onDestroy()

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        map.onSaveInstanceState(outState)
    }


    private fun initMap(savedInstanceState: Bundle?){
        map.onCreate(savedInstanceState)
        locationMap()
        search()
    }


    /**
     * 定位处理
     */
    override fun locationMap() {
        super.locationMap()
       aMap = map.map
        if (!mLatitude.isNullOrEmpty()&&!mLongitude.isNullOrEmpty()){
            mapType=2
            updateMapLocation(LatLng(mLatitude?.toDouble()?:0.0,mLongitude?.toDouble()?:0.0))
        }else{
            val myLocationStyle = MyLocationStyle() //初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。

            myLocationStyle.interval(9000000) //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。

            aMap?.mapType = AMap.MAP_TYPE_NORMAL
            aMap?.myLocationStyle = myLocationStyle //设置定位蓝点的Style
            aMap?.uiSettings?.isZoomControlsEnabled = false //隐藏放大缩小按钮

            myLocationStyle.showMyLocation(true) //设置是否显示定位小蓝点，用于满足只想使用定位，不想使用定位小蓝点的场景，设置false以后图面上不再有定位蓝点的概念，但是会持续回调位置信息。

            myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW_NO_CENTER) //连续定位、蓝点不会移动到地图中心点，并且蓝点会跟随设备移动。

            aMap?.isMyLocationEnabled = true // 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。

            aMap?.minZoomLevel = 16.0f
        }


        aMap?.setOnCameraChangeListener(object :AMap.OnCameraChangeListener{
            override fun onCameraChangeFinish(cameraPosition: CameraPosition?) {
                cameraPosition?.target?.let {
                    getAddress(it)
                }
                tv_address.visibility=View.VISIBLE

            }

            override fun onCameraChange(cameraPosition: CameraPosition?) {
                tv_address.visibility=View.GONE
            }

        })
    }

    /**
     * 搜索处理
     */
    private fun  search(){
        geocoderSearch =  GeocodeSearch(this)
        geocoderSearch?.setOnGeocodeSearchListener(this)
    }

    override fun onRegeocodeSearched(result: RegeocodeResult?, i: Int) {
        if (i == 1000) {
            if (result?.regeocodeAddress != null
                && result.regeocodeAddress.formatAddress != null) {
                tv_address.text = result.regeocodeAddress.formatAddress

            }
        }
    }


    override fun onGeocodeSearched(p0: GeocodeResult?, p1: Int) {

    }


    /**
     * 根据经纬度得到地址
     */
    fun getAddress(latLonPoint: LatLng) {
        mLatitude=latLonPoint.latitude.toString()
        mLongitude=latLonPoint.longitude.toString()
        // 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
        val query = RegeocodeQuery(
            LatLonPoint(latLonPoint.latitude, latLonPoint.longitude),
            500F,
            GeocodeSearch.AMAP
        )
        geocoderSearch?.getFromLocationAsyn(query) // 设置同步逆地理编码请求
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
            MarkerOptions().position(LatLng(28.80, 113.3154950))
                .icon(
                    BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_BLUE)))
    }

    /**
     * 根据动画按钮状态，调用函数animateCamera或moveCamera来改变可视区域
     */
    private fun changeCamera(update: CameraUpdate) {
        aMap?.moveCamera(update)
    }



}