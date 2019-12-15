package com.example.cloud_forecast_test1.city_manager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.cloud_forecast_test1.MainActivity;
import com.example.cloud_forecast_test1.R;
import com.example.cloud_forecast_test1.base.BaseActivity;
import com.example.cloud_forecast_test1.bean.WeatherBean;
import com.google.gson.Gson;

public class SearchCityActivity extends BaseActivity implements View.OnClickListener {
    EditText searchEt;
    ImageView submitIv;
    GridView searchGv;
    String[] hotCities = {"北京","上海","广州","深圳","珠海","桂林","佛山","南京","南宁","苏州","厦门","武汉","成都","武汉","拉萨",
            "杭州","青岛","天津","太原","西安"};
    private ArrayAdapter<String> adapter;
    String url1 = "https://free-api.heweather.net/s6/weather/?location=";
    String url2 = "&key=70a7e71c3dd446c7adc0b6954e4a81c4";
    String city;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_city);
        searchEt = findViewById(R.id.search_et);
        submitIv = findViewById(R.id.search_iv_submit);
        searchGv = findViewById(R.id.search_gv);
        submitIv.setOnClickListener(this);

        //设置适配器
        adapter = new ArrayAdapter<>(this, R.layout.item_hotcity, hotCities);
        searchGv.setAdapter(adapter);
        setListener();
    }

    //设置GridView的监听事件
    private void setListener() {
        searchGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                city = hotCities[i];
                String url = url1+city+url2;
                loadData(url);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case  R.id.search_iv_submit:
                city = searchEt.getText().toString();
                if (!TextUtils.isEmpty(city)){
                    //判断是否能找到这个城市
                    String url = url1+city+url2;
                    loadData(url);
                }else{
                    Toast.makeText(this,"输入内容不能为空！",Toast.LENGTH_SHORT).show();
                }
        }
    }

    public void onSuccess(String result){
        WeatherBean weatherBean = new Gson().fromJson(result, WeatherBean.class);
        if (weatherBean.getHeWeather6().get(0).getStatus().equals("ok")){
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("city",city);
            startActivity(intent);

        }else {
            Toast.makeText(this,weatherBean.getHeWeather6().get(0).getStatus(),Toast.LENGTH_SHORT).show();
        }

    }


}
