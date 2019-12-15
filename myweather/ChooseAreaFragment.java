package cn.edu.lit.myweather;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import org.litepal.crud.DataSupport;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import cn.edu.lit.myweather.db.district;
import cn.edu.lit.myweather.db.ExpressConpany;
import cn.edu.lit.myweather.util.HttpUtil;
import cn.edu.lit.myweather.util.Utility;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ChooseAreaFragment extends Fragment {

    private static final String TAG = "ChooseAreaFragment";

    public static final int LEVEL_PROVINCE = 0;

    public static final int LEVEL_CITY = 1;

    public static final int LEVEL_COUNTY = 2;

    private ProgressDialog progressDialog;

    private TextView titleText;
    //返回按钮
    private Button backButton;

    private ListView listView;

    private ArrayAdapter<String> adapter;

    private List<String> dataList = new ArrayList<>();

    //地区列表

    private List<district> districtList;
    private List<ExpressConpany> expressConpanyList;

  private List<String> provinceList=new ArrayList();
  private List<String> cityList=new ArrayList();
  private List<String> countyList=new ArrayList();
  private List<String> expcompanyIdList = new ArrayList();

    //选中的省份
    private String  selectedProvince;

    //选中的城市
    private String  selectedCity;

    //当前选中的级别
    private int currentLevel;

    //每次创建、绘制该Fragment的View组件时回调该方法，Fragment将会显示该方法返回的View组件。
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {//初始化各个组件
        View view = inflater.inflate(R.layout.choose_area, container, false);
        titleText = (TextView) view.findViewById(R.id.title_text);
        backButton = (Button) view.findViewById(R.id.back_button);
        listView = (ListView) view.findViewById(R.id.list_view);
        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, dataList);//将布局和数据进行绑定
        listView.setAdapter(adapter);//为listView 绑定适配器
        return view;
    }



    //当Fragment所在的Activity被启动完成后回调该方法。
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //为listView设置点击事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (currentLevel == LEVEL_PROVINCE) {
                    selectedProvince = provinceList.get(position);
                    queryCities();
                } else if (currentLevel == LEVEL_CITY) {
                    selectedCity = cityList.get(position);
                    queryCounties();
                }else if (currentLevel == LEVEL_COUNTY){
                    String cityname = dataList.get(position);
                    if(getActivity() instanceof MainActivity) {
                        Intent intent = new Intent(getActivity(), WeatherActivity.class);
                        intent.putExtra("cityname", cityname);
                        startActivity(intent);
                        getActivity().finish();
                    }else if(getActivity() instanceof WeatherActivity){
                        WeatherActivity activity = (WeatherActivity) getActivity();
                        activity.drawerLayout.closeDrawers();
                        activity.swipeRefresh.setRefreshing(true);
                        activity.requestWeather(cityname);
                    }
                }
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentLevel == LEVEL_COUNTY) {
                    queryCities();
                } else if (currentLevel == LEVEL_CITY) {
                    queryProvinces();
                }
            }
        });

        //如果是第一次启动以上的逻辑都不执行
        queryProvinces();
    }


    /**
     * 查询全国所有的省，优先从数据库查询，如果没有查询到再去服务器上查询。
     */
    private void queryProvinces() {
        titleText.setText("中国");
        backButton.setVisibility(View.GONE);
        districtList = DataSupport.findAll(district.class);
        if (districtList.size() > 0) {
            dataList.clear();
            provinceList.clear();
            for (district dis : districtList) {
                provinceList.add(dis.getProvince());//查询出来的数据有很多重复的数据
            }
            dataList.addAll(removeDuplicate(provinceList));//removeDuplicate去重复后 将数据加入到dataList
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel = LEVEL_PROVINCE;
        } else {
            String address = "http://v.juhe.cn/weather/citys?key=2dac353bce839f964bdb6fe5c57fd4cd";
            queryFromServer(address, "province");
        }
    }

    /*
    * 查询快递公司
    * */
    private void queryExpCompany(){
        titleText.setText("请选择快递公司");
        backButton.setVisibility(View.GONE);
        expressConpanyList = DataSupport.findAll(ExpressConpany.class);
        if(expressConpanyList.size()>0){//数据库中有数据
            dataList.clear();
            for(ExpressConpany expc: expressConpanyList){
                expcompanyIdList.add(expc.getCompanyname());//获取到快递公司的名字
            }
            dataList.addAll(expcompanyIdList);
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
        }else{  //数据里中没有数据
            String address="http://v.juhe.cn/exp/com?key=4f4230575f8f0b66669fc56a2e0a803b";
            queryExpCompanyFromServer(address);
        }
    }



    /**
     * 查询选中省内所有的市，优先从数据库查询，如果没有查询到再去服务器上查询存入数据库然后在从数据库中查询。
     */
    private void queryCities() {
        titleText.setText(selectedProvince);
        backButton.setVisibility(View.VISIBLE);
        districtList = DataSupport.where("province = ?",selectedProvince).find(district.class);
        if (districtList.size() > 0) {
            dataList.clear();
            cityList.clear();
            for (district dis : districtList) {
                cityList.add(dis.getCity());//cityList里面有很多的重复数据
            }

            dataList.addAll(removeDuplicate(cityList));//去重复后加入到datalist
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel = LEVEL_CITY;
        } else {
            String address = "http://v.juhe.cn/weather/citys?key=2dac353bce839f964bdb6fe5c57fd4cd";
            queryFromServer(address, "city");
        }
    }



    /**
     * 查询选中市内所有的县，优先从数据库查询，如果没有查询到再去服务器上查询。
     */
    private void queryCounties() {
        titleText.setText(selectedCity);
        backButton.setVisibility(View.VISIBLE);
        districtList = DataSupport.where("city = ?",selectedCity).find(district.class);
        if (districtList.size() > 0) {
            dataList.clear();
            countyList.clear();
            for (district dis : districtList) {
                countyList.add(dis.getDistrict());
            }
            dataList.addAll(removeDuplicate(countyList));//去重复
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel = LEVEL_COUNTY;
        } else {
            String address = "http://v.juhe.cn/weather/citys?key=2dac353bce839f964bdb6fe5c57fd4cd";
            queryFromServer(address, "county");
        }
    }

    /*
    * 从服务器中查询所有的快递公司的名称及对应的id   然后将其存入数据库中  在调用queryExpCompany
    * */
    private void queryExpCompanyFromServer( String address) {
    HttpUtil.sendOkHttpRequest(address, new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getContext(),"加载失败！",Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            String responseText = response.body().string();
            boolean result = Utility.handleExpCompanyInfo(responseText);
            if (result){
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        queryExpCompany();
                    }
                });
            }
        }
    });
    }

    /**
     * 根据传入的地址和类型从服务器上查询省市县数据。
     */
    private void queryFromServer(String address, final String type) {
        showProgressDialog();
        Log.e("zmh", "queryFromServer: 即将发送请求" );
        HttpUtil.sendOkHttpRequest(address, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                    boolean result = false;
                if ("province".equals(type)) {
                    result = Utility.handleResponse(responseText);
                } else if ("city".equals(type)) {
                    result = Utility.handleResponse(responseText);
                } else if ("county".equals(type)) {
                    result = Utility.handleResponse(responseText);
                }
                if (result) {
                    getActivity().runOnUiThread(new Runnable() {   //getActivity(): 返回此fragment当前与之关联的Activity。
                        @Override
                        public void run() {
                            closeProgressDialog();
                            if ("province".equals(type)) {
                                queryProvinces();
                            } else if ("city".equals(type)) {
                                queryCities();
                            } else if ("county".equals(type)) {
                                queryCounties();
                            }
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                // 通过runOnUiThread()方法回到主线程处理逻辑
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("zmh", "请求失败！" );
                        closeProgressDialog();
                        Toast.makeText(getContext(), "加载失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }



    /**
     * 显示进度对话框
     */
    private void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("正在加载...");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }



    /**
     * 关闭进度对话框
     */
    private void closeProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }
    //去掉LIST中的重复数据
    public static List removeDuplicate(List list) {
        HashSet h = new HashSet(list);
        list.clear();
        list.addAll(h);
        return list;
    }




}
