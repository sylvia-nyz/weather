package cn.edu.lit.myweather;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.view.GravityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import java.io.IOException;
import cn.edu.lit.myweather.gson.weather.Future;
import cn.edu.lit.myweather.gson.weather.Result;
import cn.edu.lit.myweather.util.HttpUtil;
import cn.edu.lit.myweather.util.Utility;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WeatherActivity extends AppCompatActivity {

    public DrawerLayout drawerLayout;

    public SwipeRefreshLayout swipeRefresh;

    private ScrollView weatherLayout;

    private Button navButton;
    /*城市*/
    private TextView titleCity;
    /*最后的更新时间*/
    private TextView titleUpdateTime;
    /*当前温度*/
    private TextView degreeText;
    /*天气的类型*/
    private TextView weatherInfoText;
    /*今天温度的范围*/
    private TextView today_tempRange;
    /*数据的发布时间*/
    private TextView info_pubulishi_time;
    /*未来天气的布局*/
    private LinearLayout forecastLayout;
    /*空气质量*/
    private TextView aqiText;
    /*pm2.5含量*/
    private TextView pm25Text;
    /*舒适程度*/
    private TextView comfortText;
    /*洗车建议*/
    private TextView carWashText;

    private TextView sportText;
    /*每日一图*/
    private ImageView bingPicImg;

    private String cityname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("zmh", "WeatherActivity 创建" );
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_weather);
        // 初始化各控件
        bingPicImg = (ImageView) findViewById(R.id.bing_pic_img);
        weatherLayout = (ScrollView) findViewById(R.id.weather_layout);
        titleCity = (TextView) findViewById(R.id.title_city);
        titleUpdateTime = (TextView) findViewById(R.id.title_update_time);
        degreeText = (TextView) findViewById(R.id.degree_text2);//今天的温度
        weatherInfoText = (TextView) findViewById(R.id.weather_info_text2);//今天的天气类型
        today_tempRange = findViewById(R.id.temp_range);
        info_pubulishi_time = findViewById(R.id.info_pubulish_time);
        forecastLayout = (LinearLayout) findViewById(R.id.forecast_layout);
        aqiText = (TextView) findViewById(R.id.aqi_text);
        pm25Text = (TextView) findViewById(R.id.pm25_text);
        comfortText = (TextView) findViewById(R.id.comfort_text);
        carWashText = (TextView) findViewById(R.id.car_wash_text);
        sportText = (TextView) findViewById(R.id.sport_text);
        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navButton = (Button) findViewById(R.id.nav_button);
        //获取SP 中的数据
        // 第一步得到SP对象
        // 第二步使用SP对象的get方法获取存入的数据
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String weatherString = prefs.getString("weather", null);
        if (weatherString != null) {
            // 有缓存时直接解析天气数据
                Result result = Utility.handleWeatherResponse2(weatherString);
                 cityname = result.today.city;
                showWeatherInfo(result);
        } else {
            // 无缓存时去服务器查询天气
            cityname = getIntent().getStringExtra("cityname");
            weatherLayout.setVisibility(View.INVISIBLE);
            requestWeather(cityname);
        }
        //设置刷新监听
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestWeather(cityname);
                Toast.makeText(WeatherActivity.this,"更新成功！",Toast.LENGTH_SHORT).show();
            }
        });
        navButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        String bingPic =prefs.getString ("bing_pic",null);
        if(bingPic!=null){
            Glide.with(this).load(bingPic).into(bingPicImg);
        }else {
            loadBingPic();
        }

    }

    /**
     * 根据城市的名字查询天气
     */
    public void requestWeather(final String weatherId) {
        String weatherUrl = "http://v.juhe.cn/weather/index?cityname=" + weatherId + "&dtype=&format=&key=2dac353bce839f964bdb6fe5c57fd4cd";
        // 参数 CallBack（）回调方法  当OkHttp 发送网络请求成功后  会执行该方法
        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                final Result result = Utility.handleWeatherResponse2(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (result != null) {
                            //将服务器中查询的数据 存入sp中  下次进入应用先从sp中查询数据
                            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                            editor.putString("weather", responseText);
                            editor.apply();
                            cityname = result.today.city;
                            showWeatherInfo(result);
                        } else {
                            Toast.makeText(WeatherActivity.this, "获取天气信息失败", Toast.LENGTH_SHORT).show();
                        }
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this, "获取天气信息失败", Toast.LENGTH_SHORT).show();
                        swipeRefresh.setRefreshing(false);//获取失败者将刷新效果关掉
                    }
                });
            }
        });
        loadBingPic();
    }

    /**
     * 加载必应每日一图
     */
    private void loadBingPic() {
        String requestBingPic = "http://guolin.tech/api/bing_pic";
        HttpUtil.sendOkHttpRequest(requestBingPic, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String bingPic = response.body().string();
                //将数据存入sp中
                // 第一步先获得SP的Editor
                //  第二部 使用putString（key,value）方法 将数据添加到editor中
                // 第三步apply()提交添加的数据
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                editor.putString("bing_pic", bingPic);
                editor.apply();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(WeatherActivity.this).load(bingPic).into(bingPicImg);
                    }
                });
            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * 处理并展示Weather实体类中的数据。
     */
    private void showWeatherInfo(Result result) {
        String cityName = result.today.city;//城市的名字
        String updateTime = result.sk.time;//上次查询的时间
        String degree = result.sk.temp;//温度
        String weatherInfo = result.today.weather;//晴 多云  阴
        String tempRange = result.today.temperature;//今天的温度范围
        titleCity.setText(cityName);
        titleUpdateTime.setText(updateTime+"发布");
        /* SimpleDateFormat df = new SimpleDateFormat(" HH:mm:ss");
        titleUpdateTime.setText(df.format("上次刷新时间"+new Date()));*/
        degreeText.setText(degree+"\u2103");
        weatherInfoText.setText(weatherInfo);
        today_tempRange.setText(tempRange);//今日温度的范围
        info_pubulishi_time.setText("数据发布于："+result.sk.time);//数据的发布时间
        forecastLayout.removeAllViews();
        for (Future future : result.futureList) {
            View view = LayoutInflater.from(this).inflate(R.layout.forecast_item, forecastLayout, false);
            TextView dateText = (TextView) view.findViewById(R.id.date_text);
            TextView infoText = (TextView) view.findViewById(R.id.info_text);
            /*TextView maxText = (TextView) view.findViewById(R.id.max_text);*/
            TextView minText = (TextView) view.findViewById(R.id.min_text);
            dateText.setText(future.date);
            infoText.setText(future.weather);
            /*maxText.setText(future.temperature);*/
            minText.setText(future.temperature);
            forecastLayout.addView(view);
        }
        if (result != null) {
            aqiText.setText(result.sk.humidity);
            pm25Text.setText(result.sk.wind_strength);
        }
        String comfort = "紫外线强度：" +result.today.uv_index;
        String carWash = "洗车指数：" + result.today.wash_index;
        String sport = "穿衣建议：" + result.today.dressing_advice;
        comfortText.setText(comfort);
        carWashText.setText(carWash);
        sportText.setText(sport);
        //数据获取成功后  显示weatherLayout
        weatherLayout.setVisibility(View.VISIBLE);

    }

}
