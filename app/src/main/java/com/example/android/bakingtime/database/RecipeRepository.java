//package com.example.android.bakingtime.database;
//
//import android.app.Application;
//import android.arch.lifecycle.LiveData;
//import android.os.AsyncTask;
//
//import com.example.android.bakingtime.model.Recipe;
//
//import java.util.List;
//
//public class RecipeRepository {
//
//    private RecipeDao mRecipeDao;
//    private LiveData<Recipe> mRecipe;
//    private LiveData<List<Recipe>> mRecipeList;
//
//    RecipeRepository(Application application) {
//        RecipeDatabase db = RecipeDatabase.getDatabase(application);
//        mRecipeDao = db.recipeDao();
//        mRecipeList = mRecipeDao.getAllRecipes();
//    }
//
//    public LiveData<Recipe> getRecipeById(int recipeId) {
//        return mRecipeDao.getRecipeById(recipeId);
//    }
//
//    public LiveData<List<Recipe>> getRecipeList() {
//        return mRecipeList;
//    }
//
//    public void insert(List<Recipe> recipeList) {
//        new insertAsyncTask(mRecipeDao).execute(recipeList);
//    }
//
//    public static class insertAsyncTask extends AsyncTask<List<Recipe>, Void, Void> {
//
//        private RecipeDao mAsyncTaskDao;
//
//        insertAsyncTask(RecipeDao dao) {
//            mAsyncTaskDao = dao;
//        }
//
//        @Override
//        protected Void doInBackground(final List<Recipe>... lists) {
//            mAsyncTaskDao.insertAll(lists[0]);
//            return null;
//        }
//    }
//
//
//}
