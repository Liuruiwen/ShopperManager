package com.rw.shoppermanager.ui

import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.alibaba.android.arouter.launcher.ARouter
import com.rw.basemvp.adapter.TabAdapter
import com.rw.shoppermanager.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val homeFragment=ARouter.getInstance().build("/home/homePageFragment").navigation()
        val pcFragment=ARouter.getInstance().build("/pc/PersonalCenterFragment").navigation()
        val list= arrayListOf<Fragment>()
        if (homeFragment is Fragment){
            list.add(homeFragment)
        }

        if (pcFragment is Fragment){
            list.add(pcFragment)
        }
        val adapter=TabAdapter(supportFragmentManager,list)
        viewpager.adapter=adapter
        viewpager.addOnPageChangeListener(object :ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                setCurrent(position)
            }

        })
        tv_main.setOnClickListener {
            viewpager.currentItem=0
            setCurrent(0)
        }
        tv_pc.setOnClickListener {
            viewpager.currentItem=1
            setCurrent(1)
        }


//        tv_login.setOnClickListener {
//            startActivity(Intent(this@MainActivity,LoginActivity::class.java))
//        }
    }

    private fun setCurrent(position:Int){
        when(position){
            0->{
                setTextState(1,tv_main)
                setTextState(0,tv_pc)
            }
            1->{
                setTextState(0,tv_main)
                setTextState(1,tv_pc)
            }
        }
    }


    private fun setTextState(type :Int,tv:TextView){
        val color=if (type==1) R.color.colorPrimary else R.color.textGray
        tv.setTextColor(ContextCompat.getColor(this,color))
        tv.typeface=if (type==1) Typeface.DEFAULT_BOLD else Typeface.DEFAULT
    }
}