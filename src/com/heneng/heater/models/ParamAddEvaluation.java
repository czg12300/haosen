package com.heneng.heater.models;

/**
 * Created by Administrator on 2015/10/27.
 */
public class ParamAddEvaluation extends BaseModel {


    /**
     * ID : 报障记录ID
     * Evaluation : 评价
     * AddEvaluation : 追加评价（文字描述）
     * EvaluationClientID : 评价客户的编号
     */

    private String ID;
    private String Evaluation;
    private String AddEvaluation;
    private String EvaluationClientID;

    public void setID(String ID) {
        this.ID = ID;
    }

    public void setEvaluation(String Evaluation) {
        this.Evaluation = Evaluation;
    }

    public void setAddEvaluation(String AddEvaluation) {
        this.AddEvaluation = AddEvaluation;
    }

    public void setEvaluationClientID(String EvaluationClientID) {
        this.EvaluationClientID = EvaluationClientID;
    }

    public String getID() {
        return ID;
    }

    public String getEvaluation() {
        return Evaluation;
    }

    public String getAddEvaluation() {
        return AddEvaluation;
    }

    public String getEvaluationClientID() {
        return EvaluationClientID;
    }
}
