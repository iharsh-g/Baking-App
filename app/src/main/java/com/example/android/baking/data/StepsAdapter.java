package com.example.android.baking.data;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.baking.R;
import com.example.android.baking.data.parcelableclasses.Step;
import com.example.android.baking.databinding.StepRowBinding;

import java.util.List;

public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.ViewHolder> {

    public interface StepItemClickListener {
        void onStepItemClick(int clickedItemIndex);
    }

    final private StepItemClickListener mOnClickListener;
    private List<Step> mSteps;
    public int selectedPosition = -1;

    public StepsAdapter (List<Step> steps, StepItemClickListener clickListener){
        mOnClickListener = clickListener;
        mSteps = steps;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        StepRowBinding stepRowBinding = DataBindingUtil
                                            .inflate(inflater, R.layout.step_row, parent, false);
        return new StepsAdapter.ViewHolder(stepRowBinding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Step step = mSteps.get(position);

        String stepNum = "";
        if(position > 0)
            stepNum = (position) + ". ";

        holder.setName(stepNum + step.getShortDescription());

        holder.binding.idStepRow.setSelected(position == selectedPosition);
    }

    @Override
    public int getItemCount() {
        return mSteps.size();
    }

    public Step getStepAtIndex(int index) {
        return mSteps.get(index);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        StepRowBinding binding;
        private Context context;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setTag(3);
            binding = DataBindingUtil.bind(itemView);
            context = itemView.getContext();

            itemView.setOnClickListener(this);
        }

        void setName(String name){
            binding.tvStepName.setText(name);
        }

        @SuppressLint("NotifyDataSetChanged")
        @Override
        public void onClick(View v) {
            selectedPosition = getAdapterPosition();
            mOnClickListener.onStepItemClick(selectedPosition);
            notifyDataSetChanged();
        }
    }
}
