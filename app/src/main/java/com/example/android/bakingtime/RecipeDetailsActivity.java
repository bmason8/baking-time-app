package com.example.android.bakingtime;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.example.android.bakingtime.model.Recipe;
import com.example.android.bakingtime.model.Steps;

import java.util.ArrayList;
import java.util.List;

public class RecipeDetailsActivity extends AppCompatActivity implements RecipeStepsFragment.PositionUpdatedListener {

    List<Steps> mRecipeSteps;
    Recipe mRecipe;
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

//        mRecipe = null;
        if (getIntent() != null && getIntent().getExtras() != null) {

            mRecipe = getIntent().getParcelableExtra("clickedRecipe");
            mRecipeSteps = mRecipe.getSteps();
            Toast.makeText(getApplicationContext(), "SHOULD HAVE RUN", Toast.LENGTH_LONG).show();
            Log.d("RAN:", "else if with SHOULD HAVE RUN");


//            if (Objects.equals(getIntent().getStringExtra(Constants.RECIPE_INTENT_SOURCE), Constants.INTENT_FROM_WIDGET_CLICK)) {
//                int recipeId = getIntent().getIntExtra(Constants.RECIPE_WIDGET_ID, 1);
//                RecipeDatabase mRecipeDatabase = Room.databaseBuilder(getApplicationContext(), RecipeDatabase.class, "Recipe_db").allowMainThreadQueries().build();
//                mRecipe = mRecipeDatabase.recipeDao().getRecipeById(recipeId);
//
//                Toast.makeText(getApplicationContext(), "SHOULDN'T HAVE RUN", Toast.LENGTH_LONG).show();
//
//                // SHOULD TRY TO GET RECIPE FROM DATABASE FIRST
////                List<Recipe> mRecipeList = NetworkUtils.fetchRecipes(getApplicationContext());
////                recipe = mRecipeList.get(recipeId);
////                mRecipeSteps = recipe.getSteps();
//
//            } else if (Objects.equals(getIntent().getStringExtra(Constants.RECIPE_INTENT_SOURCE), Constants.INTENT_FROM_MAIN_ACTIVITY_CLICK)) {
//
//                mRecipe = getIntent().getParcelableExtra("clickedRecipe");
//                mRecipeSteps = mRecipe.getSteps();
//                Toast.makeText(getApplicationContext(), "SHOULD HAVE RUN", Toast.LENGTH_LONG).show();
//                Log.d("RAN:", "else if with SHOULD HAVE RUN");
//            }


            Bundle bundle = new Bundle();
            bundle.putParcelable("recipe", mRecipe);
            bundle.putParcelableArrayList("recipeSteps", (ArrayList<? extends Parcelable>) mRecipeSteps);

            if (findViewById(R.id.tablet_container) != null) {
                mTwoPane = true;
                bundle.putBoolean("twoPane", mTwoPane);
                Toast.makeText(getApplicationContext(), "Tablet Container NOT NULL", Toast.LENGTH_LONG).show();

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
                    Toast.makeText(getApplicationContext(), "Two Pane Layout", Toast.LENGTH_SHORT).show();

                }
            } else {
                    mTwoPane = false;
                    bundle.putBoolean("twoPane", mTwoPane);

                    FragmentManager fragmentManager = getSupportFragmentManager();
                    RecipeStepsFragment recipeStepsFragment = new RecipeStepsFragment();
                    recipeStepsFragment.setArguments(bundle);
                    fragmentManager.beginTransaction()
                            .add(R.id.fragment_container, recipeStepsFragment)
                            .commit();
                    Toast.makeText(getApplicationContext(), "Single Pane Layout", Toast.LENGTH_LONG).show();
            }

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