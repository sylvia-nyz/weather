package cn.edu.lit.myweather.gson.exp;

import java.util.List;

import cn.edu.lit.myweather.db.ExpressConpany;
//存储所有的快递公司名称及对应编号
public class ExpResult {
    public List<ExpressConpany> expressConpanyList;

    public List<ExpressConpany> getExpressConpanyList() {
        return expressConpanyList;
    }

    public void setExpressConpanyList(List<ExpressConpany> expressConpanyList) {
        this.expressConpanyList = expressConpanyList;
    }
}
