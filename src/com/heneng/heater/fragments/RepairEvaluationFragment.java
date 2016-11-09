package com.heneng.heater.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.heneng.heater.R;
import com.heneng.heater.activitys.ShowResultActivity;
import com.heneng.heater.lastcoder.LoginActivity;
import com.heneng.heater.lastcoder.model.UserInfo;
import com.heneng.heater.models.BaseModel;
import com.heneng.heater.models.ParamAddEvaluation;
import com.heneng.heater.models.ResultDetail;
import com.heneng.heater.models.ResultReportFault;
import com.heneng.heater.models.ResultReportFaultList;
import com.heneng.heater.utils.CommonUtils;
import com.heneng.heater.utils.Constants;
import com.heneng.heater.utils.LogUtils;
import com.heneng.heater.utils.NetUtils;

import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2015/9/20.
 */
public class RepairEvaluationFragment extends BaseNetFragment {
    @InjectView(R.id.evaluation_repair_et_device)
    EditText et_device;
    @InjectView(R.id.evaluation_repair_et_start_time)
    EditText et_startTime;
    @InjectView(R.id.evaluation_repair_et_end_time)
    EditText et_endTime;
    @InjectView(R.id.evaluation_repair_et_human)
    EditText et_man;
    @InjectView(R.id.evaluation_repair_et_content)
    EditText et_content;
    @InjectView(R.id.evaluation_repair_et_fee)
    EditText et_fee;
    @InjectView(R.id.evaluation_repair_rb_service_evaluation)
    RatingBar rb_evaluation;
    @InjectView(R.id.evaluation_repair_et_add_evluation)
    EditText et_extra;
    @InjectView(R.id.evaluation_repair_tv_info)
    TextView tv_info;


    private String _repairID;


    private  ResultReportFault.DataEntity _entity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View _view = inflater.inflate(R.layout.a1_fragment_repair_evaluation, null);
        ButterKnife.inject(this, _view);
        rb_evaluation.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if (fromUser) {
                    switch ((int) rating) {
                        case 1:
                            Toast.makeText(mContext, "非常不满意", Toast.LENGTH_SHORT).show();
                            break;
                        case 2:
                            Toast.makeText(mContext, "有点不满", Toast.LENGTH_SHORT).show();
                            break;
                        case 3:
                            Toast.makeText(mContext, "一般", Toast.LENGTH_SHORT).show();
                            break;
                        case 4:
                            Toast.makeText(mContext, "基本满意", Toast.LENGTH_SHORT).show();
                            break;

                        case 5:
                            Toast.makeText(mContext, "非常满意", Toast.LENGTH_SHORT).show();
                            break;

                    }
                }
            }
        });
        return _view;
    }


    public void setReportFault(ResultReportFault.DataEntity entity) {
        _entity = entity;
        if (entity == null) {
            return;
        }

        et_device.setText(_app._chooseDevice.getEname());
        et_content.setText(entity.getMaintainContent());
        et_fee.setText(entity.getMaintainCost());
        et_endTime.setText(entity.getEndTime());
        et_man.setText(entity.getVisitUserName());
        et_startTime.setText(entity.getCreateTime());

        tv_info.setText(entity.getEvaluationTips());

        _repairID = entity.getID();


    }


    @Override
    public void callback(int code, BaseModel model) {
        if (code == Constants.CODE_ADDEVALUATION) {
            ResultDetail result = (ResultDetail) model;
            closeLoading();

            resetData();

            Bundle extra = new Bundle();
            extra.putString(Constants.KEY_EXTRA_RESULT, result.getResult());
            extra.putString(Constants.KEY_EXTRA_RESULT_TYPE, Constants.RESULT_TYPE_EVALUATION);
            extra.putString(Constants.KEY_EXTRA_INFO, result.getInfo());
            openActivityWithParams(ShowResultActivity.class, extra);

        }
    }


    public void resetData(){
        et_extra.setText("");
        rb_evaluation.setNumStars(5);
        rb_evaluation.setRating(5);
        _entity = null;
    }


    @OnClick(R.id.evaluation_repair_tv_evaluate)
    public void toEvaluate() {
        if (_repairID == null) {
            Toast.makeText(mContext, "未获取到客户ID,无法评价", Toast.LENGTH_SHORT).show();
            return;
        }
        String extra = et_extra.getText().toString();
        //一星到五星，从-2到2表示，所以减3
        float evaluation = rb_evaluation.getRating() - 3;
        ParamAddEvaluation param = new ParamAddEvaluation();
        param.setAddEvaluation(extra);
        param.setEvaluation(evaluation + "");
        param.setEvaluationClientID(UserInfo.ID + "");
        param.setID(_repairID);


        Map<String, Object> params = getParamsMap();
        params.put("uc", UserInfo.UserCode);
        params.put("jsonInfo", CommonUtils.toJson(param));


        NetUtils.executPost(mContext,Constants.CODE_ADDEVALUATION, "AddEvaluation", params, ResultDetail.class);
        showLoading();

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
