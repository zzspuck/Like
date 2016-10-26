package com.zzs.like.data.daily;

import java.io.Serializable;

/**
 * HeadLine
 *
 * @author zzs
 * @date 2016.09.26
 */
public class HeadLine implements Serializable {

    private String description;
    private String title;

    public String getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return "HeadLine{" +
                "description='" + description + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
