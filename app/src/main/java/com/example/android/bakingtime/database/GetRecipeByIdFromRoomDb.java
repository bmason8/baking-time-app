package com.example.android.bakingtime.database;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.AsyncTask;

import com.example.android.bakingtime.model.Recipe;

public class GetRecipeByIdFromRoomDb extends AsyncTask<Void, Void, Recipe> {

    Context mContext;
    private int recipeId;
    private Recipe mRecipe;

    public GetRecipeByIdFromRoomDb(Context context, int recipeId, Recipe recipe) {
        mContext = context;
        this.recipeId = recipeId;
        mRecipe = recipe;
    }

    @Override
    protected Recipe doInBackground(Void... voids) {
        RecipeDatabase mDatabase = Room.databaseBuilder(mContext, RecipeDatabase.class, "Recipe_db").build();
        mRecipe = mDatabase.recipeDao().getRecipeById(recipeId);
        return mRecipe;
    }

    @Override
    protected void onPostExecute(Recipe recipe) {
        mRecipe = recipe;
    }
}
