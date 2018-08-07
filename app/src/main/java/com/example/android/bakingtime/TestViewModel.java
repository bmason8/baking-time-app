package com.example.android.bakingtime;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.example.android.bakingtime.model.Recipe;
import com.example.android.bakingtime.model.Steps;

import java.util.List;

public class TestViewModel extends ViewModel {

    private LiveData<List<Steps>> testRecipeSteps;
    private MutableLiveData<Recipe> testRecipe;

    public MutableLiveData<Recipe> mRecipeMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<List<Steps>> mRecipeStepsMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<String> mTestString = new MutableLiveData<>();

    public MutableLiveData<List<Steps>> getRecipeStepsMutableLiveData() {
//        Log.d("MutableLiveData", String.valueOf(mRecipeStepsMutableLiveData));
        return mRecipeStepsMutableLiveData;
    }

    public LiveData<List<Steps>> getTestRecipeSteps() {
        return testRecipeSteps;
    }

    public void setTestRecipeSteps(LiveData<List<Steps>> testRecipeSteps) {
        this.testRecipeSteps = testRecipeSteps;
    }


    public MutableLiveData<Recipe> getTestRecipe() {
        return testRecipe;
    }

    public void setTestRecipe(MutableLiveData<Recipe> testRecipe) {
        this.testRecipe = testRecipe;
    }
}
