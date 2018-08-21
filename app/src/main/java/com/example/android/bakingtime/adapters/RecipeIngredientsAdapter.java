package com.example.android.bakingtime.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bakingtime.R;
import com.example.android.bakingtime.model.Ingredients;

import java.util.ArrayList;
import java.util.List;

public class RecipeIngredientsAdapter extends RecyclerView.Adapter<RecipeIngredientsAdapter.ViewHolder> {
    private final Context mContext;
    private final ArrayList<Ingredients> mIngredientsList;

    public RecipeIngredientsAdapter(Context context) {
        this.mContext = context;
        this.mIngredientsList = new ArrayList<>();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final TextView ingredientName;
        final TextView ingredientMeasurement;
        final TextView ingredientQuantity;

        public ViewHolder(View view) {
            super(view);
            ingredientName = view.findViewById(R.id.ingredient_name);
            ingredientMeasurement = view.findViewById(R.id.ingredient_measurement);
            ingredientQuantity = view.findViewById(R.id.ingredient_quantity);
        }
    }

    @NonNull
    @Override
    public RecipeIngredientsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.ingredient_item, viewGroup, false);
        final RecipeIngredientsAdapter.ViewHolder mViewHolder = new RecipeIngredientsAdapter.ViewHolder(view);
        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Ingredients ingredients = mIngredientsList.get(position);
        viewHolder.ingredientName.setText(ingredients.getIngredient());
        viewHolder.ingredientMeasurement.setText(ingredients.getMeasure());
        String ingredientQuantityDouble = String.valueOf(ingredients.getQuantity());
        viewHolder.ingredientQuantity.setText(ingredientQuantityDouble);
    }

//    @Override
//    public void onBindViewHolder(@NonNull RecipeIngredientsAdapter.ViewHolder viewHolder, int position) {
//        Ingredients ingredients = mIngredientsList.get(position);
//        viewHolder.ingredientName.setText(ingredients.getIngredient());
//        viewHolder.ingredientMeasurement.setText(ingredients.getMeasure());
//        String ingredientQuantityDouble = String.valueOf(ingredients.getQuantity());
//        viewHolder.ingredientQuantity.setText(ingredientQuantityDouble);
//    }

    @Override
    public int getItemCount() {
        return mIngredientsList.size();
    }

    public void setIngredientsList(List<Ingredients> ingredients) {
        this.mIngredientsList.clear();
        this.mIngredientsList.addAll(ingredients);
        notifyDataSetChanged();
    }
}
