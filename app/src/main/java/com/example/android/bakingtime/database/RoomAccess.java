package com.example.android.bakingtime.database;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.AsyncTask;

import com.example.android.bakingtime.model.Recipe;

import java.util.List;

public class RoomAccess {

    public static void insertRecipes(final List<Recipe> mRecipeList, final Context context) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                RecipeDatabase mDatabase = Room.databaseBuilder(context, RecipeDatabase.class, "Recipe_db").build();
                mDatabase.recipeDao().insertAll(mRecipeList);
                return null;
            }
        }.execute();
    }
}
