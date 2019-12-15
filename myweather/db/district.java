package cn.edu.lit.myweather.db;

import org.litepal.crud.DataSupport;

public class district extends DataSupport {
    private int id;

    private String  districtId;

    private String city;

    private String  province;

    private String district;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String  getDistrictId() {
        return districtId;
    }

    public void setDistrictId(String districtId) {
        this.districtId = districtId;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }
}
