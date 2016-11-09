package com.heneng.heater.models;

import java.io.Serializable;
import java.util.List;

/**
 * Created by huangqland on 16/1/2.
 */
public class ResultReportFault extends BaseModel  implements Serializable{


    /**
     * Data : [{"ID":"63","Eid":"E2014110110013000001","EName":"xiaomm","ErrorCode":"19","AgentName":"aaa","AgentPhone":"1112","ErrorDescription":"bdbbdbdh","VisitUserName":"","VisitUserPhone":"","HandleDescription":"","CreateTime":"2016/1/23 17:16:37","EndTime":"","MaintainContent":"","MaintainCost":"","Evaluation":"","AddEvaluation":"","Step":"1","EvaluationTips":"您的故障我们已经修复完毕，请对我们的服务进行评价。"}]
     * Logined : 1
     */

    private String Logined;
    /**
     * ID : 63
     * Eid : E2014110110013000001
     * EName : xiaomm
     * ErrorCode : 19
     * AgentName : aaa
     * AgentPhone : 1112
     * ErrorDescription : bdbbdbdh
     * VisitUserName :
     * VisitUserPhone :
     * HandleDescription :
     * CreateTime : 2016/1/23 17:16:37
     * EndTime :
     * MaintainContent :
     * MaintainCost :
     * Evaluation :
     * AddEvaluation :
     * Step : 1
     * EvaluationTips : 您的故障我们已经修复完毕，请对我们的服务进行评价。
     */

    private List<DataEntity> Data;

    public void setLogined(String Logined) {
        this.Logined = Logined;
    }

    public void setData(List<DataEntity> Data) {
        this.Data = Data;
    }

    public String getLogined() {
        return Logined;
    }

    public List<DataEntity> getData() {
        return Data;
    }

    public static class DataEntity implements  Serializable{
        private String ID;
        private String Eid;
        private String EName;
        private String ErrorCode;
        private String AgentName;
        private String AgentPhone;
        private String ErrorDescription;
        private String VisitUserName;
        private String VisitUserPhone;
        private String HandleDescription;
        private String CreateTime;
        private String EndTime;
        private String MaintainContent;
        private String MaintainCost;
        private String Evaluation;
        private String AddEvaluation;
        private String Step;
        private String EvaluationTips;

        public void setID(String ID) {
            this.ID = ID;
        }

        public void setEid(String Eid) {
            this.Eid = Eid;
        }

        public void setEName(String EName) {
            this.EName = EName;
        }

        public void setErrorCode(String ErrorCode) {
            this.ErrorCode = ErrorCode;
        }

        public void setAgentName(String AgentName) {
            this.AgentName = AgentName;
        }

        public void setAgentPhone(String AgentPhone) {
            this.AgentPhone = AgentPhone;
        }

        public void setErrorDescription(String ErrorDescription) {
            this.ErrorDescription = ErrorDescription;
        }

        public void setVisitUserName(String VisitUserName) {
            this.VisitUserName = VisitUserName;
        }

        public void setVisitUserPhone(String VisitUserPhone) {
            this.VisitUserPhone = VisitUserPhone;
        }

        public void setHandleDescription(String HandleDescription) {
            this.HandleDescription = HandleDescription;
        }

        public void setCreateTime(String CreateTime) {
            this.CreateTime = CreateTime;
        }

        public void setEndTime(String EndTime) {
            this.EndTime = EndTime;
        }

        public void setMaintainContent(String MaintainContent) {
            this.MaintainContent = MaintainContent;
        }

        public void setMaintainCost(String MaintainCost) {
            this.MaintainCost = MaintainCost;
        }

        public void setEvaluation(String Evaluation) {
            this.Evaluation = Evaluation;
        }

        public void setAddEvaluation(String AddEvaluation) {
            this.AddEvaluation = AddEvaluation;
        }

        public void setStep(String Step) {
            this.Step = Step;
        }

        public void setEvaluationTips(String EvaluationTips) {
            this.EvaluationTips = EvaluationTips;
        }

        public String getID() {
            return ID;
        }

        public String getEid() {
            return Eid;
        }

        public String getEName() {
            return EName;
        }

        public String getErrorCode() {
            return ErrorCode;
        }

        public String getAgentName() {
            return AgentName;
        }

        public String getAgentPhone() {
            return AgentPhone;
        }

        public String getErrorDescription() {
            return ErrorDescription;
        }

        public String getVisitUserName() {
            return VisitUserName;
        }

        public String getVisitUserPhone() {
            return VisitUserPhone;
        }

        public String getHandleDescription() {
            return HandleDescription;
        }

        public String getCreateTime() {
            return CreateTime;
        }

        public String getEndTime() {
            return EndTime;
        }

        public String getMaintainContent() {
            return MaintainContent;
        }

        public String getMaintainCost() {
            return MaintainCost;
        }

        public String getEvaluation() {
            return Evaluation;
        }

        public String getAddEvaluation() {
            return AddEvaluation;
        }

        public String getStep() {
            return Step;
        }

        public String getEvaluationTips() {
            return EvaluationTips;
        }
    }
}
