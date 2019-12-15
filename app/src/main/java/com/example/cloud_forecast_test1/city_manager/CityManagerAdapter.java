package com.example.cloud_forecast_test1.city_manager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.cloud_forecast_test1.R;
import com.example.cloud_forecast_test1.bean.WeatherBean;
import com.example.cloud_forecast_test1.db.DatabaseBean;
import com.google.gson.Gson;

import java.util.List;

public class CityManagerAdapter extends BaseAdapter {
    Context context;
    List<DatabaseBean> mDatas;

    public CityManagerAdapter(Context context, List<DatabaseBean> mDatas) {
        this.context = context;
        this.mDatas = mDatas;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int i) {
        return mDatas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (view == null){
            view = LayoutInflater.from(context).inflate(R.layout.item_city_manager,null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }else {
            holder = (ViewHolder) view.getTag();
        }
        DatabaseBean bean = mDatas.get(i);
        holder.cityTv.setText(bean.getCity());
        WeatherBean weatherBean = new Gson().fromJson(bean.getContent(), WeatherBean.class);
        //获取今日的天气情况
        WeatherBean.HeWeather6Bean.DailyForecastBean databean = weatherBean.getHeWeather6().get(0).getDaily_forecast().get(0);
        WeatherBean.HeWeather6Bean.NowBean now_bean = weatherBean.getHeWeather6().get(0).getNow();
        holder.conTv.setText("天气:"+databean.getCond_txt_d()+"转"+databean.getCond_txt_n());
        holder.currentTempTv.setText("体感温度:"+now_bean.getFl());
        holder.windTv.setText(databean.getWind_dir());
        holder.tempRangeTv.setText(databean.getTmp_min()+"~"+databean.getTmp_max());
        return view;
    }

    class ViewHolder{
        TextView cityTv,conTv,currentTempTv,windTv,tempRangeTv;
        public ViewHolder(View itemView){
            cityTv = itemView.findViewById(R.id.item_city_tv_city);
            conTv = itemView.findViewById(R.id.item_city_tv_condition);
            currentTempTv = itemView.findViewById(R.id.item_city_tv_temp);
            windTv = itemView.findViewById(R.id.item_city_tv_wind);
            tempRangeTv = itemView.findViewById(R.id.item_city_tv_temprange);

        }
    }
}
