package com.example.android.baking.data;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.baking.R;
import com.example.android.baking.data.parcelableclasses.Recipe;
import com.example.android.baking.databinding.MainCardRowBinding;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Set;

public class MainDataAdapter extends RecyclerView.Adapter<MainDataAdapter.ViewHolder> {

    public interface RecipeItemClickListener {
        void onRecipeItemClick(int clickedItemIndex);
    }

    final private RecipeItemClickListener mOnClickListener;
    private List<Recipe> mRecipes;

    public  MainDataAdapter(List<Recipe> recipes, RecipeItemClickListener clickListener) {
        mRecipes = recipes;
        mOnClickListener = clickListener;
    }


    @NonNull
    @Override
    public MainDataAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        MainCardRowBinding binding = DataBindingUtil
                                        .inflate(inflater, R.layout.main_card_row, parent, false);
        return new ViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull MainDataAdapter.ViewHolder holder, int position) {
        holder.binding.tvName.setText(mRecipes.get(position).getName());
        holder.setImage(mRecipes.get(position).getImage(), mRecipes.get(position).getId());
    }

    @Override
    public int getItemCount() {
        return mRecipes.size();
    }


    public Recipe getRecipeAtIndex(int index) {
        return mRecipes.get(index);
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        MainCardRowBinding binding;
        private Context context;

        ViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
            context = itemView.getContext();

            itemView.setOnClickListener(this);
        }

        // Set the holder's thumbnail
        void setImage(String image, int id) {
            if (image.equals("") && id == 1){
                binding.rowIvMainThumb.setImageResource(R.drawable.nutella_pie);
            }else if(image.equals("") && id == 2){
                binding.rowIvMainThumb.setImageResource(R.drawable.brownies);
            } else if(image.equals("") && id ==3){
                binding.rowIvMainThumb.setImageResource(R.drawable.yellow_cake);
            } else if (image.equals("") && id == 4){
                binding.rowIvMainThumb.setImageResource(R.drawable.cheesecake);
            }
            else{
                binding.rowIvMainThumb.setImageResource(R.drawable.recipe);
            }
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onRecipeItemClick(clickedPosition);

        }
    }
}
