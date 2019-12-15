package cn.edu.lit.myweather.gson.weather;

public class Today {


    //今日温度
    public  String temperature;
    //今天天气
    public String weather;
    //天气的唯一标识
    public Weather_id weather_id;

    /// 南风3-5级
    public String wind ;

    /// 星期四
    public String week;

    /// 洛阳
    public String city;

    ///  今日日期：2019年05月23日
    public String date_y;

    /// 穿衣指数：炎热
    public String dressing_index;

    /// 穿衣建议：天气炎热，建议着短衫、短裙、短裤、薄型T恤衫等清凉夏季服装。
    public String dressing_advice;

    ///  紫外线强度：很强
    public String uv_index;
    //舒适度指数 ：空
    public String comfort_index;

    /// 洗车指数：较适宜
    public String wash_index;

    /// 旅游指数：较适宜
    public String travel_index;

    /// 晨练指数：较适宜
    public String exercise_index;
    //干燥指数： 空
    public String drying_index ;

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public Weather_id getWeather_id() {
        return weather_id;
    }

    public void setWeather_id(Weather_id weather_id) {
        this.weather_id = weather_id;
    }

    public String getWind() {
        return wind;
    }

    public void setWind(String wind) {
        this.wind = wind;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDate_y() {
        return date_y;
    }

    public void setDate_y(String date_y) {
        this.date_y = date_y;
    }

    public String getDressing_index() {
        return dressing_index;
    }

    public void setDressing_index(String dressing_index) {
        this.dressing_index = dressing_index;
    }

    public String getDressing_advice() {
        return dressing_advice;
    }

    public void setDressing_advice(String dressing_advice) {
        this.dressing_advice = dressing_advice;
    }

    public String getUv_index() {
        return uv_index;
    }

    public void setUv_index(String uv_index) {
        this.uv_index = uv_index;
    }

    public String getComfort_index() {
        return comfort_index;
    }

    public void setComfort_index(String comfort_index) {
        this.comfort_index = comfort_index;
    }

    public String getWash_index() {
        return wash_index;
    }

    public void setWash_index(String wash_index) {
        this.wash_index = wash_index;
    }

    public String getTravel_index() {
        return travel_index;
    }

    public void setTravel_index(String travel_index) {
        this.travel_index = travel_index;
    }

    public String getExercise_index() {
        return exercise_index;
    }

    public void setExercise_index(String exercise_index) {
        this.exercise_index = exercise_index;
    }

    public String getDrying_index() {
        return drying_index;
    }

    public void setDrying_index(String drying_index) {
        this.drying_index = drying_index;
    }
}
