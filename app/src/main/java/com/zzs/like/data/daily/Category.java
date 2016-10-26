package com.zzs.like.data.daily;

import java.io.Serializable;

/**
 * 类别
 *
 * @author zzs
 * @date 2016.09.26
 */
public class Category implements Serializable {

    private String image_lab;
    private String title;

    public String getImage_lab() {
        return image_lab;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return "Category{" +
                "image_lab='" + image_lab + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
