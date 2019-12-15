package cn.edu.lit.myweather.db;

import org.litepal.crud.DataSupport;

//快递公司名称及对应的编号
public class ExpressConpany extends DataSupport {
    public int id;
   public  String companyname;
    public String no;

    public String getCompanyname() {
        return companyname;
    }

    public void setCompanyname(String companyname) {
        this.companyname = companyname;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }
}
