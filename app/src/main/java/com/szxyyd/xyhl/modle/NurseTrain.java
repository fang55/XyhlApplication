package com.szxyyd.xyhl.modle;

/**
 * Created by jq on 2016/6/29.
 */
public class NurseTrain {
    private int id;
    private String title;
    private String atrec2;
    private String atrec1;
    private String score;
    private String nurseid;

    public NurseTrain() {
        super();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAtrec2() {
        return atrec2;
    }

    public void setAtrec2(String atrec2) {
        this.atrec2 = atrec2;
    }

    public String getAtrec1() {
        return atrec1;
    }

    public void setAtrec1(String atrec1) {
        this.atrec1 = atrec1;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getNurseid() {
        return nurseid;
    }

    public void setNurseid(String nurseid) {
        this.nurseid = nurseid;
    }
}
