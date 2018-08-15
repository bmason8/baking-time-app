package com.example.android.bakingtime.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.bakingtime.R;
import com.example.android.bakingtime.adapters.RecipeCardAdapter.ViewHolder;
import com.example.android.bakingtime.model.Recipe;
import com.example.android.bakingtime.model.Steps;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class RecipeCardAdapter extends RecyclerView.Adapter<ViewHolder> {
    private final Context mContext;
    private final ArrayList<Recipe> mRecipeList;
    private ClickHandler mClickListener;

    public interface ClickHandler {
        void onItemClick(int position);
    }

    public void AdapterClickListener(ClickHandler clickHandler) {
        mClickListener = clickHandler;
    }

    public RecipeCardAdapter(Context context) {
        this.mContext = context;
        this.mRecipeList = new ArrayList<>();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements OnClickListener {
        final ImageView recipeCardImageView;
        final TextView recipeServesTextView;
        final TextView recipeStepsTextView;
        final TextView recipeNameTextView;

        public ViewHolder(View view) {
            super(view);
            recipeCardImageView = view.findViewById(R.id.recipe_background_image);
            recipeServesTextView = view.findViewById(R.id.servings);
            recipeStepsTextView = view.findViewById(R.id.steps);
            recipeNameTextView = view.findViewById(R.id.recipe_name);
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
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recipe_card_layout, viewGroup, false);
        final ViewHolder mViewHolder = new ViewHolder(view);
        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Recipe recipe = mRecipeList.get(position);
        List<Steps> stepsArray = recipe.getSteps();
        String numberOfSteps = String.valueOf(stepsArray.size());
        String servings = String.valueOf(recipe.getServings());
        viewHolder.recipeServesTextView.setText(servings);
        viewHolder.recipeNameTextView.setText(recipe.getName());
        viewHolder.recipeStepsTextView.setText(numberOfSteps);

        Log.d("imagePath: ", recipe.getImage());
        String imagePath = recipe.getImage();
        if (imagePath.isEmpty()) {
            viewHolder.recipeCardImageView.setImageResource(R.drawable.ic_banana);
        } else {
            Picasso.get()
                    .load(recipe.getImage())
                    .placeholder(R.drawable.ic_banana)
                    .error(R.drawable.ic_banana)
                    .into(viewHolder.recipeCardImageView);
        }
    }

    @Override
    public int getItemCount() {
        return mRecipeList.size();
    }

    public void setRecipesList(List<Recipe> recipes) {
        this.mRecipeList.clear();
        this.mRecipeList.addAll(recipes);
        notifyDataSetChanged();
    }
}
