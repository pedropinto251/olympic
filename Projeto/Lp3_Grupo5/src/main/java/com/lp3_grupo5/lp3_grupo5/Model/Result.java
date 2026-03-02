package com.lp3_grupo5.lp3_grupo5.Model;
public class Result {
    private String sportName;
    private String result;
    private String medal;
    private String athleteName;

    public Result(String sportName, String result, String medal,String athleteName) {
        this.sportName = sportName;
        this.result = result;
        this.medal = medal;
        this.athleteName = athleteName;
    }

    public String getSportName() {
        return sportName;
    }

    public void setSportName(String sportName) {
        this.sportName = sportName;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMedal() {
        return medal;
    }


    public String getAthleteName() {
        return athleteName;
    }

    public void setAthleteName(String athleteName) {
        this.athleteName = athleteName;
    }
    public void setMedal(String medal) {
        this.medal = medal;
    }
    @Override
    public String toString() {
        return "Result{" +
                "sportName='" + sportName + '\'' +
                ", value='" + result + '\'' +
                ", medal='" + medal + '\'' +
                '}';
    }
}