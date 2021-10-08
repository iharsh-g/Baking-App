package com.example.android.baking.data;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.baking.R;
import com.example.android.baking.data.parcelableclasses.Ingredient;
import com.example.android.baking.databinding.IngredientRowBinding;
import com.example.android.baking.utils.AppUtils;

import java.util.List;

public class IngredientsAdapter extends
        RecyclerView.Adapter<IngredientsAdapter.ViewHolder> {
    private List<Ingredient> mIngredients;

    public IngredientsAdapter(List<Ingredient> ingredients){
        mIngredients = ingredients;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        IngredientRowBinding binding = DataBindingUtil.
                                        inflate(inflater, R.layout.ingredient_row, parent, false);
        return new IngredientsAdapter.ViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Ingredient ingredient = mIngredients.get(position);

        holder.setTitle(ingredient.getIngredient());
        holder.setDose(ingredient.getDoseStr());
    }

    @Override
    public int getItemCount() {
        return mIngredients.size();
    }

    public Ingredient getIngredientAtIndex(int index){
        return mIngredients.get(index);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        IngredientRowBinding binding;
        private Context context;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            binding = DataBindingUtil.bind(itemView);
            context = itemView.getContext();
        }

        void setTitle(String title){
            binding.tvIngredientName.setText(AppUtils.capitalizeFirstLetter(title));
        }

        void setDose(String dose){
            binding.tvIngredientDose.setText(dose);
        }
    }
}
