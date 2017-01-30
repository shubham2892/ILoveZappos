package com.intern.zappos.Network;

import com.intern.zappos.POJOs.ProductSearch;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by jonty on 27/01/17.
 */

public interface RestAPI {
    @GET("Search")
    Call<ProductSearch> groupList(@Query("term") String searchTerm, @Query("key") String key);
}