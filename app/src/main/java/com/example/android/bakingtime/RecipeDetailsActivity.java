package com.example.android.bakingtime;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.example.android.bakingtime.model.Recipe;
import com.example.android.bakingtime.model.Steps;

import java.util.List;

public class RecipeDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

        Recipe recipe = null;
        List<Steps> mSteps;
        
        if (getIntent() != null && getIntent().getExtras() != null) {
            recipe = getIntent().getParcelableExtra("clickedRecipe");
        }
        
//        mSteps = recipe.getSteps();

        Bundle bundle = new Bundle();
        bundle.putParcelable("recipe", recipe);

        RecipeStepsFragment recipeStepsFragment = new RecipeStepsFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        recipeStepsFragment.setArguments(bundle);
        fragmentManager.beginTransaction()
                .add(R.id.recipe_steps_container, recipeStepsFragment)
                .commit();
    }
}
