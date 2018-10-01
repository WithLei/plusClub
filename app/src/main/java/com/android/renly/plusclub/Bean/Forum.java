package com.android.renly.plusclub.Bean;

/**
 * HomeFragment论坛模块Bean类
 */
public class Forum {
    private String title;
    private int img;
    // 模块位置
    private int header;

    public Forum(String title, int img, int header) {
        this.title = title;
        this.img = img;
        this.header = header;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public int getHeader() {
        return header;
    }

    public void setHeader(int header) {
        this.header = header;
    }
}
