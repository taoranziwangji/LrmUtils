package com.vdolrm.lrmutils.OpenSourceUtils.net.http.okhttp;

import java.util.List;

/**
 * Created by Administrator on 2016/3/21.
 */
public class TestOkHttpBean {

    /**
     * ecode : 0
     * edata : [{"ename":"个人信息","evalue":"PersonalInformation","newflg":"0","zdyflg":0},{"ename":"兴趣爱好","evalue":"Hobby","newflg":"1","zdyflg":0},{"ename":"生活和休闲","evalue":"LifeAndLeisure","newflg":"1","zdyflg":0},{"ename":"高科技及抽象","evalue":"HighTechAndAbstract","newflg":"1","zdyflg":0}]
     */

    private String ecode;
    /**
     * ename : 个人信息
     * evalue : PersonalInformation
     * newflg : 0
     * zdyflg : 0
     */

    private List<EdataEntity> edata;

    public void setEcode(String ecode) {
        this.ecode = ecode;
    }

    public void setEdata(List<EdataEntity> edata) {
        this.edata = edata;
    }

    public String getEcode() {
        return ecode;
    }

    public List<EdataEntity> getEdata() {
        return edata;
    }

    public static class EdataEntity {
        private String ename;
        private String evalue;
        private String newflg;
        private int zdyflg;

        public void setEname(String ename) {
            this.ename = ename;
        }

        public void setEvalue(String evalue) {
            this.evalue = evalue;
        }

        public void setNewflg(String newflg) {
            this.newflg = newflg;
        }

        public void setZdyflg(int zdyflg) {
            this.zdyflg = zdyflg;
        }

        public String getEname() {
            return ename;
        }

        public String getEvalue() {
            return evalue;
        }

        public String getNewflg() {
            return newflg;
        }

        public int getZdyflg() {
            return zdyflg;
        }
    }
}
