package com.example.android.bakingtime;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.example.android.bakingtime.model.Recipe;
import com.example.android.bakingtime.model.Steps;

import java.util.List;

public class RecipeDetailsViewModel extends AndroidViewModel {

    private LiveData<Recipe> mRecipeLiveData;
    private LiveData<List<Steps>> mRecipeStepsLiveData;

    private MutableLiveData<List<Steps>> mTestingSteps;

    public RecipeDetailsViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<Recipe> getRecipeLiveData() {
        return mRecipeLiveData;
    }

    public LiveData<List<Steps>> getRecipeStepsLiveData() {
        return mRecipeStepsLiveData;
    }


    public MutableLiveData<List<Steps>> getTestingSteps() {
        return mTestingSteps;
    }

    public void setTestingSteps(MutableLiveData<List<Steps>> testingSteps) {
        mTestingSteps = testingSteps;
    }
}
