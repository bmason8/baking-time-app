package com.example.android.bakingtime;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.android.bakingtime.adapters.RecipeStepsAdapter;
import com.example.android.bakingtime.model.Recipe;
import com.example.android.bakingtime.model.Steps;

import java.util.ArrayList;
import java.util.List;

public class RecipeStepsFragment extends Fragment implements RecipeStepsAdapter.ClickHandler {

//    @BindView(R.id.recipe_step_container)
//    ConstraintLayout mRecipeStepContainer;
//    @BindView(R.id.step_description)
//    TextView mStepDescription;
//    @BindView(R.id.step_number)
//    TextView mStepNumber;

    private RecyclerView mRecyclerView;
    private RecipeStepsAdapter mAdapter;
    private int mPosition;

    StepInstructionsFragment stepInstructionsFragment;

    Recipe mRecipe;
    List<Steps> mRecipeSteps;
    boolean mTwoPane = false;

//    public RecipeStepsFragment() {
//    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRecipe = new Recipe();

        if (savedInstanceState != null) {
            mRecipeSteps = savedInstanceState.getParcelableArrayList("recipeSteps");
            mPosition = savedInstanceState.getInt("position");
        } else {

            if (getArguments() != null) {
                Bundle extras = getArguments();
//            mRecipe = (Recipe) extras.getSerializable("recipe");
                mRecipe = extras.getParcelable("recipe");
                mRecipeSteps = mRecipe.getSteps();
            } else {
                Toast.makeText(getContext(), "Arguments null", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recipe_steps, container, false);
//        ButterKnife.bind(this, rootView);

        // set up landscape mode if correct view is loaded
        if (rootView.findViewById(R.id.tablet_container) != null) {
            mTwoPane = true;
            // set up recyclerView for recipe steps
            mRecyclerView = rootView.findViewById(R.id.recipe_steps_recyclerView);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            mRecyclerView.setHasFixedSize(true);
            mAdapter = new RecipeStepsAdapter(getContext());
            mRecyclerView.setAdapter(mAdapter);
            mAdapter.AdapterClickListener(this);
            mAdapter.setRecipesSteps(mRecipeSteps);

            // set up recipe step details fragment
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("recipeSteps", (ArrayList<? extends Parcelable>) mRecipeSteps);
            stepInstructionsFragment = new StepInstructionsFragment();
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            stepInstructionsFragment.setArguments(bundle);
            fragmentTransaction.replace(R.id.recipe_step_details_container, stepInstructionsFragment);
//            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } else {
            mTwoPane = false;
            mRecyclerView = rootView.findViewById(R.id.recipe_steps_recyclerView);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            mRecyclerView.setHasFixedSize(true);
            mAdapter = new RecipeStepsAdapter(getContext());
            mRecyclerView.setAdapter(mAdapter);
            mAdapter.AdapterClickListener(this);
            mAdapter.setRecipesSteps(mRecipeSteps);
        }

        return rootView;
    }

    @Override
    public void onItemClick(int position) {

        // Pass mRecipeSteps and position so I can move through it based on button clicks in StepInstructionsFragment
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("recipeSteps", (ArrayList<? extends Parcelable>) mRecipeSteps);
        bundle.putInt("position", position);
        bundle.putBoolean("twoPane", mTwoPane);
        mPosition = position;

        // Update the look of the selected step


        if (mTwoPane) {
            // maybe don't recreate the fragment each time...try just passing the new position since it already has the array of steps
            stepInstructionsFragment.positionUpdateFromInterface(position);

//            StepInstructionsFragment stepInstructionsFragment = new StepInstructionsFragment();
//            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
//            stepInstructionsFragment.setArguments(bundle);
//            fragmentTransaction.replace(R.id.recipe_step_details_container, stepInstructionsFragment);
//            fragmentTransaction.commit();

        } else {
            // Build another fragment and perform a FragmentTransaction
            StepInstructionsFragment stepInstructionsFragment = new StepInstructionsFragment();
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            stepInstructionsFragment.setArguments(bundle);
            fragmentTransaction.replace(R.id.frame_fragment_holder, stepInstructionsFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("recipeSteps", (ArrayList<? extends Parcelable>) mRecipeSteps);
        outState.putInt("position", mPosition);
    }

    //    private void updateLookOfSelectedStep() {
//        mStepDescription.setTextColor(Color.WHITE);
//        mStepNumber.setTextColor(Color.WHITE);
//        mRecipeStepContainer.setBackgroundResource(R.drawable.round_corners_selected);
//
//    }
}
