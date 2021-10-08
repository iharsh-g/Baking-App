package com.example.android.baking.fragments;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.android.baking.R;
import com.example.android.baking.activities.RecipeInfoActivity;
import com.example.android.baking.data.IngredientsAdapter;
import com.example.android.baking.data.StepsAdapter;
import com.example.android.baking.data.parcelableclasses.Recipe;
import com.example.android.baking.databinding.FragmentRecipeInfoBinding;

import java.util.Objects;

public class RecipeInfoFragment extends Fragment {

    private FragmentRecipeInfoBinding binding;
    private Recipe recipe;
    private StepsAdapter stepsAdapter;

    private final String CURRENT_TAB = "current_tab";
    private final String CURRENT_STEP = "current_step";

    OnStepClickListener mCallback;

    public interface OnStepClickListener{
        void onStepSelected(int position);
    }

    public RecipeInfoFragment(){}

    @SuppressLint("NotifyDataSetChanged")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_recipe_info, container, false);
        final View rootView = binding.getRoot();

        if(recipe == null){
            recipe =((RecipeInfoActivity) requireContext()).getRecipe();
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        binding.rvIngredients.setLayoutManager(linearLayoutManager);
        binding.rvIngredients.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        binding.rvSteps.setLayoutManager(layoutManager);
        binding.rvSteps.setHasFixedSize(true);

        binding.tabHost.setup();
        addTab("Ingredients", R.id.tab1);
        addTab("Steps", R.id.tab2);

        initViews();

        if(savedInstanceState != null){
            binding.tabHost.setCurrentTab(savedInstanceState.getInt(CURRENT_TAB));

            stepsAdapter.selectedPosition = savedInstanceState.getInt(CURRENT_STEP);
            stepsAdapter.notifyDataSetChanged();
        }

        return rootView;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try{
            mCallback = (OnStepClickListener) context;
        }
        catch (ClassCastException e){
            throw new ClassCastException(context.toString() +
                    " must implement OnStepClickListener");
        }
    }

    private void initViews(){
        IngredientsAdapter ingredientsAdapter = new IngredientsAdapter(recipe.getIngredients());
        binding.rvIngredients.setAdapter(ingredientsAdapter);

        stepsAdapter = new StepsAdapter(recipe.getSteps(), (int clickedItemIndex) -> {
            mCallback.onStepSelected(clickedItemIndex);
        });
        binding.rvSteps.setAdapter(stepsAdapter);
    }

    private void addTab(String tabName, int contentId) {
        TabHost.TabSpec spec = binding.tabHost.newTabSpec(tabName);
        spec.setContent(contentId);
        spec.setIndicator(tabName);
        binding.tabHost.addTab(spec);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(CURRENT_TAB, binding.tabHost.getCurrentTab());
        outState.putInt(CURRENT_STEP, stepsAdapter.selectedPosition);
    }
}
