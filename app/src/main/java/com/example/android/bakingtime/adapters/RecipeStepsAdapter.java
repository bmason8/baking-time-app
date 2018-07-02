package com.example.android.bakingtime.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bakingtime.R;
import com.example.android.bakingtime.adapters.RecipeStepsAdapter.ViewHolder;
import com.example.android.bakingtime.model.Steps;

import java.util.ArrayList;
import java.util.List;

public class RecipeStepsAdapter extends RecyclerView.Adapter<ViewHolder> {
    private final Context mContext;
    private final ArrayList<Steps> mRecipeSteps;
    private RecipeStepsAdapter.ClickHandler mClickListener;
    private int testPosition;

    public interface ClickHandler {
        void onItemClick(int position);
    }

    public void AdapterClickListener(RecipeStepsAdapter.ClickHandler clickHandler) {
        mClickListener = clickHandler;
    }

    public RecipeStepsAdapter(Context context) {
        this.mContext = context;
        this.mRecipeSteps = new ArrayList<>();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements OnClickListener {
        final TextView recipeStepTextView;
        final TextView recipeNameTextView;
        final ConstraintLayout mContainerLayout;

        public ViewHolder(View view) {
            super(view);
            recipeStepTextView = view.findViewById(R.id.step_number);
            recipeNameTextView = view.findViewById(R.id.step_description);
            mContainerLayout = view.findViewById(R.id.recipe_step_container);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            mClickListener.onItemClick(position);
        }
    }


    @NonNull
    @Override
    public RecipeStepsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recipe_step_layout, viewGroup, false);
        final RecipeStepsAdapter.ViewHolder mViewHolder = new RecipeStepsAdapter.ViewHolder(view);
        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeStepsAdapter.ViewHolder viewHolder, int position) {
        Steps steps = mRecipeSteps.get(position);
        String recipeStep = String.valueOf(steps.getId() + 1);
        viewHolder.recipeStepTextView.setText(recipeStep);
        viewHolder.recipeNameTextView.setText(steps.getShortDescription());

//        // update style
//        viewHolder.mContainerLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                testPosition = position;
//            }
//        });
//        if (testPosition == position) {
//            viewHolder.recipeStepTextView.setTextColor(Color.WHITE);
//            viewHolder.recipeNameTextView.setTextColor(Color.WHITE);
//            viewHolder.mContainerLayout.setBackgroundResource(R.drawable.round_corners_selected);
//        }



    }

    @Override
    public int getItemCount() {
        return mRecipeSteps.size();
    }

    public void setRecipesSteps(List<Steps> steps) {
        this.mRecipeSteps.clear();
        this.mRecipeSteps.addAll(steps);
        notifyDataSetChanged();
    }
}
