package cn.edu.lit.myweather.gson.weather;
//未来几天天气


public class Future {

    //温度范围
    public String temperature;

    /// 天气类型  晴  多云
    public String weather ;
    //天气的id
    public Weather_id weather_id;

    /// 东南风3-5级
    public String wind;

    /// 星期三
    public String week ;
    //当天的日期时间   yy mm dd hh mm ss
    public String date ;

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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Future{" +
                "wind='" + wind + '\'' +
                ", week='" + week + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
