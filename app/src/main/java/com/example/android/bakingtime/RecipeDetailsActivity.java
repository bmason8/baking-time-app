package com.example.android.bakingtime;

import android.arch.lifecycle.ViewModelProviders;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
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

    List<Steps> mStepsTest;
    Recipe mRecipeTest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

        // In onCreate, this activity checks for an intent. If not null, gets the Recipe and then gets the Recipe Steps from it.
        // It then creates a new bundle with the Recipe and the Recipe Steps.
        // Then it checks for a tablet/landscape layout and creates one or both fragments depending on whether that view exists.
        // As a result, the fragment(s) are inflated and the view is updated

//        RecipeDetailsViewModel viewModel = ViewModelProviders.of(this).get(RecipeDetailsViewModel.class);
//        viewModel.getRecipeLiveData().observe(this, new Observer<Recipe>() {
//            @Override
//            public void onChanged(@Nullable Recipe recipe) {
//                mRecipe = recipe;
//                mRecipeSteps = mRecipe.getSteps();
//            }
//        });

        TestViewModel testViewModel = ViewModelProviders.of(this).get(TestViewModel.class);



//        mRecipe = null;
        if (getIntent() != null && getIntent().getExtras() != null) {

            mRecipe = getIntent().getParcelableExtra("clickedRecipe");
            mRecipeSteps = mRecipe.getSteps();
            mStepsTest = mRecipe.getSteps();
//            Log.d("mutableSteps:", "mRecipe.getSteps: " + String.valueOf(mRecipeSteps));


            testViewModel.mTestString.setValue("It worked!");
            testViewModel.mRecipeMutableLiveData.setValue(mRecipe);
//            testViewModel.mRecipeStepsMutableLiveData.setValue(mRecipeSteps);

            testViewModel.mRecipeStepsMutableLiveData.setValue(mStepsTest);

//            Toast.makeText(getApplicationContext(), "SHOULD HAVE RUN", Toast.LENGTH_LONG).show();
            Log.d("RAN:", "else if with SHOULD HAVE RUN");


            Bundle bundle = new Bundle();
            bundle.putParcelable("recipe", mRecipe);
            bundle.putParcelableArrayList("recipeSteps", (ArrayList<? extends Parcelable>) mRecipeSteps);

            // Landscape Mode
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
                // Portrait Mode
            } else {
                    mTwoPane = false;
                    bundle.putBoolean("twoPane", mTwoPane);

                    FragmentManager fragmentManager = getSupportFragmentManager();
                    List<Fragment> fragments = fragmentManager.getFragments();

                    if (fragments.isEmpty()) {
//                        Toast.makeText(getApplicationContext(), "isEmpty", Toast.LENGTH_LONG).show();
                        RecipeStepsFragment recipeStepsFragment = new RecipeStepsFragment();
                        recipeStepsFragment.setArguments(bundle);
                        fragmentManager.beginTransaction()
                                .add(R.id.fragment_container, recipeStepsFragment)
                                .commit();
                    }
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

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Toast.makeText(getApplicationContext(), "onConfigurationChanged!", Toast.LENGTH_SHORT).show();

    }
}
