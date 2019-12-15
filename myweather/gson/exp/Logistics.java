package cn.edu.lit.myweather.gson.exp;

import java.util.List;

//物流信息
public class Logistics {
    public String company;
    public String companyid;
    public String no;
    public String status;
    public String status_detail;
    public List<ExpinfoList> expinfoLists;
    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getCompanyid() {
        return companyid;
    }

    public void setCompanyid(String companyid) {
        this.companyid = companyid;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus_detail() {
        return status_detail;
    }

    public void setStatus_detail(String status_detail) {
        this.status_detail = status_detail;
    }

    public List<ExpinfoList> getExpinfoLists() {
        return expinfoLists;
    }

    public void setExpinfoLists(List<ExpinfoList> expinfoLists) {
        this.expinfoLists = expinfoLists;
    }
}
