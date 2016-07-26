package com.szxyyd.xyhl.modle;

/**
 * Created by jq on 2016/6/22.
 */
public class DetailFile {
    private int id;
    private String files;
    private String title;
    private String atrec;
    private String type;

    public DetailFile() {
        super();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAtrec() {
        return atrec;
    }

    public void setAtrec(String atrec) {
        this.atrec = atrec;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFiles() {
        return files;
    }

    public void setFiles(String files) {
        this.files = files;
    }
}
