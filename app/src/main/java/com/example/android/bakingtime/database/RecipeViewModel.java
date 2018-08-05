//package com.example.android.bakingtime.database;
//
//import android.app.Application;
//import android.arch.lifecycle.AndroidViewModel;
//import android.arch.lifecycle.LiveData;
//import android.support.annotation.NonNull;
//
//import com.example.android.bakingtime.model.Recipe;
//
//import java.util.List;
//
//public class RecipeViewModel extends AndroidViewModel {
//
//    private RecipeRepository mRepository;
//
//    private LiveData<Recipe> mRecipe;
//
//    private LiveData<List<Recipe>> mRecipeList;
//
//    public RecipeViewModel(@NonNull Application application) {
//        super(application);
//        mRepository = new RecipeRepository(application);
////        mRecipe = mRepository.getRecipeById(int recipeId);
//        mRecipeList = mRepository.getRecipeList();
//    }
//
//    public LiveData<Recipe> getRecipe(int recipeId) {
//        return mRepository.getRecipeById(recipeId);
//    }
//
//    public LiveData<List<Recipe>> getRecipeList() {
//        return mRecipeList;
//    }
//
//    public void insert(List<Recipe> recipeList) {
//        mRepository.insert(recipeList);
//    }
//}
