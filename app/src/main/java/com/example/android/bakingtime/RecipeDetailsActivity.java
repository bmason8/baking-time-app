package com.example.android.bakingtime;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.android.bakingtime.model.Recipe;
import com.example.android.bakingtime.model.Steps;

import java.util.ArrayList;
import java.util.List;

public class RecipeDetailsActivity extends AppCompatActivity implements RecipeStepsFragment.PositionUpdatedListener {

    List<Steps> mRecipeSteps;
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

        Recipe recipe = null;
        if (getIntent() != null && getIntent().getExtras() != null) {
            recipe = getIntent().getParcelableExtra("clickedRecipe");
            mRecipeSteps = recipe.getSteps();
        }

        Bundle bundle = new Bundle();
        bundle.putParcelable("recipe", recipe);
        bundle.putParcelableArrayList("recipeSteps", (ArrayList<? extends Parcelable>) mRecipeSteps);

        if (findViewById(R.id.tablet_container) != null) {
            mTwoPane = true;
            bundle.putBoolean("twoPane", mTwoPane);

            // since it's true, build both fragments
            if (savedInstanceState == null) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                // set up recipe steps fragment
                RecipeStepsFragment recipeStepsFragment = new RecipeStepsFragment();
                recipeStepsFragment.setArguments(bundle);
                fragmentManager.beginTransaction()
                        .add(R.id.master_recipe_steps_fragment, recipeStepsFragment)
                        .commit();

                // set up recipe details fragment
                StepInstructionsFragment stepInstructionsFragment = new StepInstructionsFragment();
                stepInstructionsFragment.setArguments(bundle);
                fragmentManager.beginTransaction()
                        .add(R.id.recipe_step_details_container, stepInstructionsFragment)
                        .commit();

            }
        } else {
            mTwoPane = false;
            bundle.putBoolean("twoPane", mTwoPane);

            RecipeStepsFragment recipeStepsFragment = new RecipeStepsFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            recipeStepsFragment.setArguments(bundle);
            fragmentManager.beginTransaction()
                    .add(R.id.fragment_container, recipeStepsFragment)
                    .commit();
        }

    }

    @Override
    public void updatePosition(int position) {
        Toast.makeText(getApplication(), "click from Activity Ran!", Toast.LENGTH_SHORT).show();
        StepInstructionsFragment stepInstructionsFragment = (StepInstructionsFragment)
                getSupportFragmentManager().findFragmentById(R.id.recipe_step_details_container);
        if (stepInstructionsFragment != null) {
            stepInstructionsFragment.positionUpdate(position);
        }
    }
}
