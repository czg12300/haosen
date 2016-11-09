package com.heneng.heater.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.heneng.heater.R;
import com.heneng.heater.models.BaseModel;
import com.heneng.heater.models.ResultReportFault;
import com.heneng.heater.models.ResultReportFaultList;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Administrator on 2015/9/20.
 */
public class RepairProgressFragment extends BaseNetFragment {
    @InjectView(R.id.progress_repair_et_company_name)
    EditText et_company_name;
    @InjectView(R.id.progress_repair_et_company_phone)
    EditText et_company_phone;
    @InjectView(R.id.progress_repair_et_progress)
    EditText et_progress;
    @InjectView(R.id.progress_repair_et_human)
    EditText et_human_name;
    @InjectView(R.id.progress_repair_et_phone)
    EditText et_human_phone;
    @InjectView(R.id.progress_repair_et_desc)
    EditText et_desc;
    @InjectView(R.id.progress_repair_tv_info)
    TextView tv_info;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View _view = inflater.inflate(R.layout.a1_fragment_repair_progress, null);
        ButterKnife.inject(this, _view);
        return _view;
    }


    public void setResultReportFault(ResultReportFault.DataEntity entity) {
        if(entity == null){
            return;
        }



            if("1".equals(entity.getStep())||"2".equals(entity.getStep())){
                et_company_name.setText(entity.getAgentName());
                et_company_phone.setText(entity.getAgentPhone());
                et_desc.setText(entity.getMaintainContent());
                et_human_name.setText(entity.getVisitUserName());
               et_human_phone.setText(entity.getVisitUserPhone());
                if ("1".equals(entity.getStep())){
                    et_progress.setText("未处理");
                }else{
                    et_progress.setText("处理中");
                }
               tv_info.setText(entity.getHandleDescription());
            }




    }

    @Override
    public void callback(int code, BaseModel model) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
