package cn.edu.lit.myweather;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import java.util.List;

import cn.edu.lit.myweather.db.district;
import cn.edu.lit.myweather.db.ExpressConpany;
import cn.edu.lit.myweather.util.HttpUtil;
import cn.edu.lit.myweather.util.Utility;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ChooseExpcFragment extends Fragment {

    private static final String TAG = "ChooseExpcFragment";


    private ProgressDialog progressDialog;

    private TextView titleText;
    //返回按钮
    private Button backButton;

    private ListView listView;

    private ArrayAdapter<String> adapter;

    private List<String> dataList = new ArrayList<>();
    //快递公司列表
    private List<ExpressConpany> expressConpanyList;

    private List<String> expcompanyIdList = new ArrayList();

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
        //设置selectIntemListener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getContext(),"快递公司："+dataList.get(position),Toast.LENGTH_SHORT).show();
                Intent it = new Intent(getActivity(),ExpressionActivity.class);
                startActivity(it);
                getActivity().finish();
            }
        });
        queryExpCompany();
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
        }else{  //数据里中没有数据 则去服务器上查找
            String address="http://v.juhe.cn/exp/com?key=4f4230575f8f0b66669fc56a2e0a803b";
            queryExpCompanyFromServer(address);
        }
    }


    /*
    * 从服务器中查询所有的快递公司的名称及对应的id   然后将其存入数据库中  在调用queryExpCompany
    * */
    private void queryExpCompanyFromServer( String address) {
        showProgressDialog();
        HttpUtil.sendOkHttpRequest(address, new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    closeProgressDialog();
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
                        closeProgressDialog();
                        queryExpCompany();
                    }
                });
            }
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


}
