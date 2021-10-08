package com.example.android.baking.activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.android.baking.R;
import com.example.android.baking.data.MainDataAdapter;
import com.example.android.baking.data.RequestInterface;
import com.example.android.baking.data.parcelableclasses.Recipe;
import com.example.android.baking.databinding.ActivityMainBinding;
import com.example.android.baking.utils.AppUtils;
import com.facebook.drawee.backends.pipeline.Fresco;

import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    private Context context;
    private ActivityMainBinding binding;
    private MainDataAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        setTitle(getString(R.string.app_name));

        AppUtils.setIdleResourceTo(false);

        context = this;
        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        binding.rvMaster.setLayoutManager(layoutManager);

        // allow swipe to refresh
        binding.mainLayoutSwipe.setOnRefreshListener(this);

        loadJSON();
    }

    private void loadJSON() {
        binding.mainLayoutSwipe.setRefreshing(true);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppUtils.BASE_URL)
                .client(new OkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RequestInterface request = retrofit.create(RequestInterface.class);
        Call<List<Recipe>> call = request.getJSON();

        Log.d("JSON CALL", "RETROFIT CALL");
        call.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(@NonNull Call<List<Recipe>> call, @NonNull Response<List<Recipe>> response) {
                binding.noNetworkLayout.setVisibility(View.GONE);
                binding.rvMaster.setVisibility(View.VISIBLE);

                Log.i("JSON RESPONSE", "SUCCESS");
                List<Recipe> jsonResponse = response.body();

                adapter = new MainDataAdapter(jsonResponse, clickedItemIndex -> {
                    Recipe recipe = adapter.getRecipeAtIndex(clickedItemIndex);
                    Intent intent = new Intent(context, RecipeInfoActivity.class);
                    intent.putExtra(AppUtils.EXTRAS_RECIPE, recipe);
                    startActivityForResult(intent, 1);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                });
                binding.rvMaster.setAdapter(adapter);
                binding.mainLayoutSwipe.setRefreshing(false);

                AppUtils.setIdleResourceTo(true);
            }

            @Override
            public void onFailure(@NonNull Call<List<Recipe>> call, @NonNull Throwable t) {
                Log.e("JSON RESPONSE", "FAIL: " + t.getMessage());
                binding.mainLayoutSwipe.setRefreshing(false);

                // if we have a network error, prompt a dialog asking to retry or exit
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage(R.string.main_no_network)
                        .setNegativeButton(R.string.main_no_network_try_again, (dialog, id) -> loadJSON())
                        .setPositiveButton(R.string.main_no_network_close, (dialog, id) -> finish());
                builder.create().show();

                AppUtils.setIdleResourceTo(false);
            }
        });
    }

    @Override
    public void onRefresh() {
        loadJSON();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!AppUtils.isNetworkAvailable(this)){
            binding.noNetworkLayout.setVisibility(View.VISIBLE);
            binding.rvMaster.setVisibility(View.GONE);
            binding.retryButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loadJSON();
                }
            });
        }
    }
}