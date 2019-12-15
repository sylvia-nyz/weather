package cn.edu.lit.myweather.gson.weather;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Result {
    @SerializedName("future")
    public List<Future> futureList;
    public Sk sk;
    public Today today;

    public List<Future> getFutureList() {
        return futureList;
    }

    public void setFutureList(List<Future> futureList) {
        this.futureList = futureList;
    }

    public Sk getSk() {
        return sk;
    }

    public void setSk(Sk sk) {
        this.sk = sk;
    }

    public Today getToday() {
        return today;
    }

    public void setToday(Today today) {
        this.today = today;
    }
}
