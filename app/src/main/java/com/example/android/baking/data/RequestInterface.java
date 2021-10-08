package com.example.android.baking.data;

import com.example.android.baking.data.parcelableclasses.Recipe;
import com.example.android.baking.utils.AppUtils;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;

//For the purpose of retrofit
public interface RequestInterface {
    @GET(AppUtils.JSON_LOC)
    Call<List<Recipe>> getJSON();
}
