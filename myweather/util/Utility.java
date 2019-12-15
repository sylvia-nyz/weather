package cn.edu.lit.myweather.util;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import cn.edu.lit.myweather.db.district;
import cn.edu.lit.myweather.gson.exp.ExpinfoList;
import cn.edu.lit.myweather.db.ExpressConpany;
import cn.edu.lit.myweather.gson.exp.Logistics;
import cn.edu.lit.myweather.gson.weather.Future;
import cn.edu.lit.myweather.gson.weather.Result;
import cn.edu.lit.myweather.gson.weather.Sk;
import cn.edu.lit.myweather.gson.weather.Today;
import cn.edu.lit.myweather.gson.weather.Weather_id;

public class Utility {

    /**
     * 解析和处理服务器返回的省级数据
     */
    public static boolean handleResponse(String response) {
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONObject obj = new JSONObject(response);
                String result = obj.getString("resultcode");
                if (result != null && result.equals("200")) {
                    String allDistrict = obj.getString("result");
                    JSONArray arr = new JSONArray(allDistrict);
                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject districtObj = arr.getJSONObject(i);
                        district dis = new district();
                        dis.setDistrictId(districtObj.getString("id"));
                        dis.setProvince(districtObj.getString("province"));
                        dis.setCity(districtObj.getString("city"));
                        dis.setDistrict(districtObj.getString("district"));
                        dis.save();
                    }
                    return true;
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
            return false;
    }


    /**
     * 将返回的JSON数据解析成Weather实体类
     */
    //该方法使用Gson将json数据转换为实体类  暂时没有用到

    public static Result handleWeatherResponse(String response) {
        try {

            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("result");
            String weatherContent = jsonArray.getJSONObject(0).toString();
            return new Gson().fromJson(weatherContent, Result.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    // 不会使用GSON  自己写的一个json的处理方法
    public static Result handleWeatherResponse2(String response){
        Today today2= new Today();
        Sk sk2= new Sk();
        Future future2= new Future();
        Weather_id weather_id2 = new Weather_id();
        Result result = new Result();
        if(response!=null){
            JSONObject obj  = null;
            try {
                obj = new JSONObject(response);
                response = obj.getString("resultcode");
                if(response!=null&&response.equals("200")){
                    response = obj.getString("result");
                    obj = new JSONObject(response);
                    //today 的数据
                   String  result_today = obj.getString("today");
                   JSONObject  obj_today = new JSONObject(result_today);
                   today2.setTemperature(obj_today.getString("temperature"));
                    today2.setWeather(obj_today.getString("weather"));
                    //info.setWeather_id(obj_today.getString("weather_id"));
                    today2.setWind(obj_today.getString("wind"));
                    today2.setWeek(obj_today.getString("week"));
                    today2.setCity(obj_today.getString("city"));
                    today2.setDate_y(obj_today.getString("date_y"));
                    today2.setDressing_index(obj_today.getString("dressing_index"));
                    today2.setDressing_advice(obj_today.getString("dressing_advice"));
                    today2.setUv_index(obj_today.getString("uv_index"));
                    today2.setComfort_index(obj_today.getString("comfort_index"));
                    today2.setWash_index(obj_today.getString("wash_index"));
                    today2.setTravel_index(obj_today.getString("travel_index"));
                    today2.setExercise_index(obj_today.getString("exercise_index"));
                    today2.setDrying_index(obj_today.getString("drying_index"));
                    result.setToday(today2);
                    //sk 的数据
                    String  result_SK = obj.getString("sk");
                    JSONObject  obj_sk = new JSONObject(result_SK);
                    sk2.setTemp(obj_sk.getString("temp"));
                    sk2.setWind_direction(obj_sk.getString("wind_direction"));
                    sk2.setWind_strength(obj_sk.getString("wind_strength"));
                    sk2.setHumidity(obj_sk.getString("humidity"));
                    sk2.setTime(obj_sk.getString("time"));
                    result.setSk(sk2);
                    // future 的数据
                    String result_future=obj.getString("future");
                    result.setFutureList(getFutureWeatherInfo( result_future));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
    //解析Future 数据s
    public static List<Future> getFutureWeatherInfo(String result_future){

        List<Future> futures = new ArrayList();
        Weather_id weather_id= new Weather_id();
        try {
            JSONObject object = new JSONObject(result_future);
            String [] result_day = new String [7];
            for (int i=0;i<7;i++){
                Future future = new Future();
                result_day[i] = object.getString(getNowData(i));
                JSONObject obj_day =new JSONObject(result_day[i]);
                future.temperature=(obj_day.getString("temperature"));
                future.weather=(obj_day.getString("weather"));
                future.wind=(obj_day.getString("wind"));
                future.week=(obj_day.getString("week"));
                future.date=(SwichDate.switcher(obj_day.getString("date")));
                String  result_day01_wid = obj_day.getString("weather_id");
                JSONObject object_day1_weather_id = new JSONObject(result_day01_wid);
                weather_id.setFa(object_day1_weather_id.getString("fa"));
                future.setWeather_id(weather_id);
                futures.add(future);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return futures;
    }

    //解析快递公司ID的json数据  并存入数据库中
    public  static boolean handleExpCompanyInfo(String response){

        if (!TextUtils.isEmpty(response)) {
            try {
                JSONObject obj = new JSONObject(response);
                String resultcode = obj.getString("resultcode");
                if (resultcode != null && resultcode.equals("200")) {
                    String result = obj.getString("result");
                    JSONArray arr = new JSONArray(result);
                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject expcObj = arr.getJSONObject(i);
                        ExpressConpany expc = new ExpressConpany();
                        expc.setCompanyname(expcObj.getString("com"));
                        expc.setNo(expcObj.getString("no"));
                        expc.save();
                    }
                    return true;
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    //解析服务器返回json物流信息
    public static Logistics handleLogisticResponse(String response){
        Logistics logistics = new Logistics();
        List<ExpinfoList> expinfoLists = new ArrayList();
        if (!TextUtils.isEmpty(response)){
            try {
                JSONObject obj  = new JSONObject(response);
                String resultCode = obj.getString("resultcode");
                if(resultCode!=null&&resultCode.equals("200")){
                    String result =obj.getString("result");
                    JSONObject resultobj = new JSONObject(result);
                    logistics.setCompany(resultobj.getString("company"));
                    logistics.setCompanyid(resultobj.getString("com"));
                    logistics.setNo(resultobj.getString("status"));
                    logistics.setStatus_detail(resultobj.getString("status_detail"));
                    String list= obj.getString("list");
                    JSONArray array = new JSONArray(list);
                    for (int i=0;i<array.length();i++){
                        ExpinfoList expinfoList = new ExpinfoList();
                        JSONObject listobj = array.getJSONObject(i);
                        expinfoList.setDatetime(listobj.getString("datetime"));
                        expinfoList.setRemark(listobj.getString("remark"));
                        expinfoList.setZone(listobj.getString("zone"));
                        expinfoLists.add(expinfoList);
                    }
                    logistics.setExpinfoLists(expinfoLists);
                    return logistics;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    // 日期格式转化方法
    public  static  String getNowData(int inmanyday){
        java.util.Calendar c=java.util.Calendar.getInstance();
        java.text.SimpleDateFormat f=new java.text.SimpleDateFormat("yyyyMMdd");
        int now =Integer.parseInt(f.format(c.getTime()));
        now +=inmanyday;
        return "day_"+String.valueOf(now);

    }

}
