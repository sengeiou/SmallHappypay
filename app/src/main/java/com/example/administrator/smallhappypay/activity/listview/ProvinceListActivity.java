package com.example.administrator.smallhappypay.activity.listview;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.example.administrator.smallhappypay.App;
import com.example.administrator.smallhappypay.R;
import com.example.administrator.smallhappypay.adapter.ListviewProvinceAdapter;
import com.example.administrator.smallhappypay.constant.Constant;
import com.example.administrator.smallhappypay.net.Api;
import com.example.administrator.smallhappypay.tool.SPUtils;
import com.example.administrator.smallhappypay.util.CityBean;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ProvinceListActivity extends Activity {


    @BindView(R.id.provincelist_lv_list)
    ListView provincelistLvList;
    private CityBean cityBean = null;
    private ListviewProvinceAdapter ListviewProvinceadapter = null;

    private String judgeprovince;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_province_list);
        ButterKnife.bind(this);
        getCityBean();
        judgeprovince= SPUtils.getString(getApplication(),"judgeprovince");
        provincelistLvList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.putExtra(Constant.IN_City_ID, cityBean.getList().get(position).getCityId());
                intent.putExtra(Constant.IN_City_Name, cityBean.getList().get(position).getCityName());
                if ("1".equals(judgeprovince)){
                    ProvinceListActivity.this.setResult(Constant.ProvinceRESULT_OK, intent);
                }else if ("2".equals(judgeprovince)){
                    ProvinceListActivity.this.setResult(Constant.NewProvinceRESULT_OK, intent);
                }
                ProvinceListActivity.this.finish();
            }
        });
    }

    private void initView() {
        ListviewProvinceadapter = new ListviewProvinceAdapter(this, cityBean.getList());
        provincelistLvList.setAdapter(ListviewProvinceadapter);
    }

    private void getCityBean() {
        Api.gethttpService().getPrivinceData("0").enqueue(new Callback<CityBean>() {
            @Override
            public void onResponse(Call<CityBean> call, Response<CityBean> response) {
                if ("00".equals(response.body().getCode())&&response.body().getList()!=null){
                    cityBean=response.body();
                    initView();
                }else{
                    App.getInstance().showToast(response.body().getFailMessage());
                }
            }
            @Override
            public void onFailure(Call<CityBean> call, Throwable t) {
                App.getInstance().showToast("服务器维护中");
            }
        });
    }
}
