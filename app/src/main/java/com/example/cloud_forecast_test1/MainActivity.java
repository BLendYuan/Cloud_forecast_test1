package com.example.cloud_forecast_test1;



import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.cloud_forecast_test1.city_manager.CityManagerActivity;
import com.example.cloud_forecast_test1.db.DBManager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView addCityIv,moreIv;
    LinearLayout pointLayout;
    ViewPager mainVp;
    //Viewpager的数据源
    List<Fragment>fragmentList;
    //表示选中的城市的集合《要显示的》
    List<String>cityList;
    //表示ViewPAger的页数指示器显示的内容
    List<ImageView>imgList;
    private CityFragmentPagerAdapter adapter;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addCityIv = findViewById(R.id.main_iv_add);
        moreIv = findViewById(R.id.main_iv_more);
        pointLayout = findViewById(R.id.main_layout_point);
        mainVp = findViewById(R.id.main_vp);
        addCityIv.setOnClickListener(this);
        moreIv.setOnClickListener(this);


        fragmentList = new ArrayList<>();
        cityList = DBManager.queryAllCityName();//获取数据库包含的城市信息列表
        imgList = new ArrayList<>();
        //初始化城市列表,默认第一个为auto_ip
        if (cityList.size()==0){
            cityList.add("北京");
//            cityList.add("北京");
//            cityList.add("上海");
//            cityList.add("广州");
        }

        //因为可能搜索界面跳转到此界面会传值，所以此处获取一下
        try {
            Intent intent = getIntent();
            String city = intent.getStringExtra("city");
            if (!cityList.contains(city)&&!TextUtils.isEmpty(city)) {
                cityList.add(city);
            }
        }catch (Exception e){
            Log.i("blybly","此处出错！！！！！！！！！");
        }
        
        //初始化Viewpager页面的方法
        initPager();
        adapter =  new CityFragmentPagerAdapter(getSupportFragmentManager(),fragmentList);
        mainVp.setAdapter(adapter);
        //创建小圆点指示器
        iniPoint();
        //设置最后一个城市的信息
        mainVp.setCurrentItem(fragmentList.size()-1);
        //设置Viewpage页面监听(滑动Viewpager时，下面的小点也跟着移动。)
        setPagerListener();

    }

    private void setPagerListener() {
        mainVp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //页面选择的位置
                for (int i = 0; i<imgList.size();i++){
                    imgList.get(i).setImageResource(R.mipmap.a1);
                }
                imgList.get(position).setImageResource(R.mipmap.a2);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void iniPoint() {
        //创建小圆点ViewPager页面指示器的函数
        for (int i = 0;i<fragmentList.size();i++){
            ImageView pIv = new ImageView(this);
            pIv.setImageResource(R.mipmap.a1);
            pIv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT));
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) pIv.getLayoutParams();
            lp.setMargins(0,0,20,0);
            imgList.add(pIv);
            pointLayout.addView(pIv);

        }
        imgList.get(imgList.size()-1).setImageResource(R.mipmap.a2);
    }

    private void initPager() {
        //创建Fragment对象，添加到Viewpager数据源当中
        for (int i = 0; i<cityList.size(); i++){
            CityWeatherFragment cwFrag = new  CityWeatherFragment();
            Bundle bundle = new Bundle();
            bundle.putString("city",cityList.get(i));
            cwFrag.setArguments(bundle);
            fragmentList.add(cwFrag);
        }
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent();

        switch (view.getId()){
            case R.id.main_iv_add:
                intent.setClass(this, CityManagerActivity.class);
                break;
            case R.id.main_iv_more:
                Toast.makeText(this,"功能尚未完善，待续",Toast.LENGTH_SHORT).show();
                break;

        }
        startActivity(intent);
    }
}
