package com.szxyyd.xyhl.modle;

/**
 * Created by jq on 2016/6/27.
 */
public class Nurse {
    private int fvrid; //是否收藏
    private String workvideo; //视频

    public Nurse() {
        super();
    }

    public int getFvrid() {
        return fvrid;
    }
    public void setFvrid(int fvrid) {
        this.fvrid = fvrid;
    }
    public String getWorkvideo() {
        return workvideo;
    }

    public void setWorkvideo(String workvideo) {
        this.workvideo = workvideo;
    }
}
