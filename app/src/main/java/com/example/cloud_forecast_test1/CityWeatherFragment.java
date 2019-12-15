package com.example.cloud_forecast_test1;


import android.app.AlertDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.cloud_forecast_test1.base.BaseFragment;
import com.example.cloud_forecast_test1.bean.WeatherBean;


import com.example.cloud_forecast_test1.db.DBManager;
import com.google.gson.Gson;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class CityWeatherFragment extends BaseFragment implements View.OnClickListener {
    TextView tempTv,cityTv,conditionTv,windTv,tempRangeTv,clothTv,dateTv,clothIndexTv,carIndexTv,coldIndexTv,sportIndexTv,raysIndexTv;
    ImageView dayIv;
    LinearLayout futureLayout;
    String url1 = "https://free-api.heweather.net/s6/weather/?location=";
    String url2 = "&key=70a7e71c3dd446c7adc0b6954e4a81c4";
    private List<WeatherBean.HeWeather6Bean.LifestyleBean> indexList;
    String city;
    public CityWeatherFragment() {
        // Required empty public constructor
    }
    Gson gson = new Gson();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_city_weather, container, false);
        initView(view);
        //可以通过activity传值取得当前fragment加载的那个地方的天气情况
        Bundle bundle = getArguments();
        city = bundle.getString("city");
//        String city_1 = "auto_ip";
        String url = url1+city+url2;
//      调用父类basefragment获取数据方法
        loadData(url);
        return view;
    }
    private  void initView(View view){
        //初始化控件
        tempTv = view.findViewById(R.id.frag_temprature);
        cityTv = view.findViewById(R.id.frag_tv_city);
        conditionTv = view.findViewById(R.id.frag_condition);
        windTv = view.findViewById(R.id.frag_wind);
        tempRangeTv = view.findViewById(R.id.frag_tv_t1temp);
        dateTv = view.findViewById(R.id.frag_date);
        clothIndexTv = view.findViewById(R.id.frag_dress);
        carIndexTv = view.findViewById(R.id.frag_washcar);
        coldIndexTv = view.findViewById(R.id.frag_colding);
        sportIndexTv = view.findViewById(R.id.frag_sports);
        raysIndexTv = view.findViewById(R.id.frag_rays);
        dayIv = view.findViewById(R.id.frag_today);
        futureLayout = view.findViewById(R.id.frag_center_layout);
//点击事件的监听
        clothIndexTv.setOnClickListener(this);
        carIndexTv.setOnClickListener(this);
        coldIndexTv.setOnClickListener(this);
        sportIndexTv.setOnClickListener(this);
        raysIndexTv.setOnClickListener(this);
//        miniIv = view.findViewById(R.id.item_center_iv);

    }


    public void onSuccess(String result) {
        //解析并展示数据
        parseShowData(result);

//        minipictrueChange(result);
        //更新数据
        int i = DBManager.updateInfoByCity(city,result);
        if (i<=0){
            //更新数据库失败，说明无此城市，可以增加这个城市的记录
            DBManager.addCityInfo(city,result);
        }
    }

    @Override
    public void onError(Throwable ex, boolean isOnCallback) {
        //数据库查找上一次信息显示在Fragment中
        String s = DBManager.queryInfoByCity(city);
        if (!TextUtils.isEmpty(s)){
            parseShowData(s);
        }
    }

    private  void  parseShowData(String result){
        //使用了json解析的数据

        WeatherBean weatherBean = gson.fromJson(result, WeatherBean.class);
        WeatherBean.HeWeather6Bean resultBean = weatherBean.getHeWeather6().get(0);

        //天气大图片的加载
        if (resultBean.getNow().getCond_txt().equals("晴")){
            dayIv.setImageResource(R.mipmap.a100);
        }
        else if (resultBean.getNow().getCond_txt().equals("阴")){
            dayIv.setImageResource(R.mipmap.a104);
        }else if (resultBean.getNow().getCond_txt().equals("多云")){
            dayIv.setImageResource(R.mipmap.a101);
        }else if (resultBean.getNow().getCond_txt().equals("雨")){
            dayIv.setImageResource(R.mipmap.a399);
        }
        else if (resultBean.getNow().getCond_txt().equals("雪")){
            dayIv.setImageResource(R.mipmap.a499);
        }
        else{
            dayIv.setImageResource(R.mipmap.a999);
        }

        //获取指数天气信息列表
        indexList = resultBean.getLifestyle();
        dateTv.setText(resultBean.getDaily_forecast().get(0).getDate());
        cityTv.setText(resultBean.getBasic().getParent_city());
        //惊天的天气情况的获取
        WeatherBean.HeWeather6Bean.DailyForecastBean todayDateBean = resultBean.getDaily_forecast().get(0);
        WeatherBean.HeWeather6Bean.NowBean now_bean = resultBean.getNow();
        windTv.setText(todayDateBean.getWind_dir()+todayDateBean.getWind_sc()+"级");
        tempRangeTv.setText("体感温度："+now_bean.getFl()+"℃");
        conditionTv.setText(resultBean.getNow().getCond_txt());
        tempTv.setText(todayDateBean.getTmp_min()+"℃"+"~"+todayDateBean.getTmp_max()+"℃");
        //获取未来三天的天气情况，加载到layout当中
        List<WeatherBean.HeWeather6Bean.DailyForecastBean> futureList = resultBean.getDaily_forecast();
        futureList.remove(0);
        for (int i=0;i<futureList.size();i++){
            View itemView = LayoutInflater.from(getActivity()).inflate(R.layout.item_main_center,null);
            itemView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
            futureLayout.addView(itemView);
            TextView idateTv = itemView.findViewById(R.id.item_center_tv_date);
            TextView iconTv = itemView.findViewById(R.id.item_center_tv_con);
            TextView itemtemprangTv = itemView.findViewById(R.id.item_center_tv_temp);
            ImageView iTv = itemView.findViewById(R.id.item_center_iv);
            WeatherBean.HeWeather6Bean.DailyForecastBean dataBean = futureList.get(i);
            idateTv.setText(dataBean.getDate());
            iconTv.setText(dataBean.getCond_txt_d());
            itemtemprangTv.setText(dataBean.getTmp_min()+"~"+dataBean.getTmp_max()+"℃");
//            minipictrueChange(dataBean);
            if (dataBean.getCond_txt_d().equals("晴")){
                iTv.setImageResource(R.mipmap.a100);
            }
            else if (dataBean.getCond_txt_d().equals("阴")){
                iTv.setImageResource(R.mipmap.a104);
            }else if (dataBean.getCond_txt_d().equals("多云")){
                iTv.setImageResource(R.mipmap.a101);
            }else if (dataBean.getCond_txt_d().equals("雨")){
                iTv.setImageResource(R.mipmap.a399);
            }
            else if (dataBean.getCond_txt_d().equals("雪")){
                iTv.setImageResource(R.mipmap.a499);
            }
            else{
                iTv.setImageResource(R.mipmap.a999);
            }
        }
    }

//    public void minipictrueChange(WeatherBean.HeWeather6Bean.DailyForecastBean dataBean){
//        if (dataBean.getCond_txt_d().equals("晴")){
//            miniIv.setImageResource(R.mipmap.a100);
//        }
//        else if (dataBean.getCond_txt_d().equals("阴")){
//            miniIv.setImageResource(R.mipmap.a104);
//        }else if (dataBean.getCond_txt_d().equals("多云")){
//            miniIv.setImageResource(R.mipmap.a101);
//        }else if (dataBean.getCond_txt_d().equals("雨")){
//            miniIv.setImageResource(R.mipmap.a399);
//        }
//        else if (dataBean.getCond_txt_d().equals("雪")){
//            miniIv.setImageResource(R.mipmap.a499);
//        }
//        else{
//            miniIv.setImageResource(R.mipmap.a999);
//        }
//    }
//
//        WeatherBean weatherBean = gson.fromJson(result, WeatherBean.class);
//        WeatherBean.HeWeather6Bean resultBean = weatherBean.getHeWeather6().get(0);
//        for (int i =1;i<3;i++) {
//            switch (resultBean.getDaily_forecast().get(i).getCond_txt_d()) {
//                case "晴":
//                    miniIv.setImageResource(R.mipmap.sunny_1);
////                    break;
//                case "阴":
//                    miniIv.setImageResource(R.mipmap.littlecloud);
////                    break;
//                case "多云":
//                    miniIv.setImageResource(R.mipmap.cloudy_1);
////                    break;
//                case "小雨":
//                    miniIv.setImageResource(R.mipmap.littlerain);
////                    break;
//                case "雨":
//                    miniIv.setImageResource(R.mipmap.littlerain);
////                    break;
//                case "中雨":
//                    miniIv.setImageResource(R.mipmap.middlerain);
////                    break;
//                case "大雨":
//                    miniIv.setImageResource(R.mipmap.heavyrain);
////                    break;
//                case "暴雨":
//                    miniIv.setImageResource(R.mipmap.strongrain);
////                    break;
//                case "雪":
//                    miniIv.setImageResource(R.mipmap.snow);
////                    break;
//                case "小雪":
//                    miniIv.setImageResource(R.mipmap.snow);
////                    break;
//                case "中雪":
//                    miniIv.setImageResource(R.mipmap.snow);
////                    break;
//                case "暴雪":
//                    miniIv.setImageResource(R.mipmap.snow);
////                    break;
//                case "大雪":
//                    miniIv.setImageResource(R.mipmap.snow);
////                    break;
//                default:
//                    miniIv.setImageResource(R.mipmap.unkown);
//                    break;
//            }
//        }
//    }

    @Override
    public void onClick(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        switch (view.getId()){
            case R.id.frag_dress:
                builder.setTitle("穿衣指数");
                WeatherBean.HeWeather6Bean.LifestyleBean indexBean = indexList.get(0);
                String msg = indexBean.getBrf()+"\n"+indexBean.getTxt();
                builder.setMessage(msg);
                builder.setPositiveButton("确定",null);
                break;
            case R.id.frag_washcar:
                builder.setTitle("洗车指数");
                indexBean = indexList.get(6);
                msg = indexBean.getBrf()+"\n"+indexBean.getTxt();
                builder.setMessage(msg);
                builder.setPositiveButton("确定",null);
                break;
            case R.id.frag_colding:
                builder.setTitle("感冒指数");
                indexBean = indexList.get(2);
                msg = indexBean.getBrf()+"\n"+indexBean.getTxt();
                builder.setMessage(msg);
                builder.setPositiveButton("确定",null);
                break;
            case R.id.frag_sports:
                builder.setTitle("运动指数");
                indexBean = indexList.get(3);
                msg = indexBean.getBrf()+"\n"+indexBean.getTxt();
                builder.setMessage(msg);
                builder.setPositiveButton("确定",null);
                break;
            case R.id.frag_rays:
                builder.setTitle("紫外线指数");
                indexBean = indexList.get(5);
                msg = indexBean.getBrf()+"\n"+indexBean.getTxt();
                builder.setMessage(msg);
                builder.setPositiveButton("确定",null);
                break;
        }
        builder.create().show();
    }
}
