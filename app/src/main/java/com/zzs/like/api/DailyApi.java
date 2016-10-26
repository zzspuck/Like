package com.zzs.like.api;

import com.zzs.like.data.daily.DailyTimeLine;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;


/**
 * Daily请求接口
 *
 * @author zzs
 * @date 2016.09.22
 * @note -
 * -------------------------------------------------------------------------------------------------
 * @modified -
 * @date -
 * @note -
 */

public interface DailyApi {

    @GET("homes/index/{num}.json")
    Observable<DailyTimeLine> getDailyTimeLine(@Path("num") String num);

    @GET("options/index/{id}/{num}.json")
    Observable<DailyTimeLine> getDailyFeedDetail(@Path("id") String id,@Path("num") String num);

}
