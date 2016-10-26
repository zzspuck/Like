package com.zzs.like.data.daily;

import java.io.Serializable;

/**
 * DailyTimeLine
 *
 * @author zzs
 * @date 2016.09.26
 */
public class DailyTimeLine implements Serializable {

    private Meta meta;
    private Response response;

    public Meta getMeta() {
        return meta;
    }
    public Response getResponse() {
        return response;
    }

    @Override
    public String toString() {
        return "DailyTimeLine{" +
                "meta=" + meta +
                ", response=" + response +
                '}';
    }
}
