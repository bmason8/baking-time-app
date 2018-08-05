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

//    public static Recipe getRecipeById(final int recipeId, final Context context) {
//        new AsyncTask<Void, Void, Recipe>() {
//            @Override
//            protected Recipe doInBackground(Void... voids) {
//                return null;
//            }
//
////            protected Recipe doInBackground(Recipe... result) {
////                try {
////                    RecipeDatabase mDatabase = Room.databaseBuilder(context, RecipeDatabase.class, "Recipe_db").build();
////                    Recipe recipeResult = mDatabase.recipeDao().getRecipeById(recipeId);
////                } catch (Exception e) {
////                    e.printStackTrace();
////                }
////                return result;
////            }
//        }.execute();
//    }

//    public static Recipe getRecipeById(final int recipeId, final Context context) {
//        new AsyncTask<Void, Void, Recipe>() {
//            @Override
//            protected void doInBackground(Void... voids) {
//                RecipeDatabase mDatabase = Room.databaseBuilder(context, RecipeDatabase.class, "Recipe_db").build();
//                Recipe recipe = mDatabase.recipeDao().getRecipeById(recipeId);
//            }
//        };
//        return recipe;
//    }


//    public static class getRecipe extends AsyncTask<Void, Void, Recipe> {
//
//        private Context mContext;
//        private int recipeId;
//
//        public getRecipe(final int recipeId, final Context context) {
//            mContext = context;
//            this.recipeId = recipeId;
//
//        }
//
//        @Override
//        protected Recipe doInBackground(Void... voids) {
//            RecipeDatabase mDatabase = Room.databaseBuilder(mContext, RecipeDatabase.class, "Recipe_db").build();
//            Recipe recipeResult = mDatabase.recipeDao().getRecipeById(recipeId);
//            return recipeResult;
//        }
//
////        protected void onPostExecute(Recipe recipe) {
////            return;
////        }
//    }


}
