package com.example.android.baking.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.android.baking.R;
import com.example.android.baking.data.parcelableclasses.Step;
import com.example.android.baking.fragments.RecipeInfoDetailFragment;
import com.example.android.baking.utils.AppUtils;

public class RecipeInfoDetailActivity extends AppCompatActivity {

    // Fragment key to restore onSaveInstanceState
    private static final String DETAIL_FRAGMENT_KEY = "detail_fragment";
    private Fragment detailFragment;
    RelativeLayout mLayout;
    Button mRetryButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_info_detail);
        mLayout = (RelativeLayout) findViewById(R.id.noNetwork_layout);
        mRetryButton = (Button) findViewById(R.id.retry_button);

        // persistence: Save the state of the detail fragment so video is preserved after device rotation
        if (savedInstanceState==null){
            detailFragment = new RecipeInfoDetailFragment();
        }
        else{
            detailFragment = getSupportFragmentManager().getFragment(savedInstanceState, DETAIL_FRAGMENT_KEY);
        }

        Bundle bundle = new Bundle();
        Step step = getIntent().getParcelableExtra(AppUtils.EXTRAS_STEP);
        String recipeName = getIntent().getStringExtra(AppUtils.EXTRAS_RECIPE_NAME);
        bundle.putParcelable(AppUtils.EXTRAS_STEP, step);
        detailFragment.setArguments(bundle);

        ActionBar ab = getSupportActionBar();
        if (step.getId()>0)
            ab.setTitle(getString(R.string.step) + (step.getId()));
        else
            ab.setTitle(getString(R.string.recipe_introduction));
        ab.setSubtitle(recipeName);

        setFragment();
    }

    private void setFragment(){
        if( AppUtils.isNetworkAvailable(this)){
            mLayout.setVisibility(View.GONE);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.frm_recipe_info_detail, detailFragment)
                    .commit();
        }
        else{
            mLayout.setVisibility(View.VISIBLE);
            mRetryButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setFragment();
                }
            });
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState, DETAIL_FRAGMENT_KEY, detailFragment);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_up);
    }
}
