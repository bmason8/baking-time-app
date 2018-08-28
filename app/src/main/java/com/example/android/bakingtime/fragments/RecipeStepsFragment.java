package com.example.android.bakingtime.fragments;

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

import com.example.android.bakingtime.R;
import com.example.android.bakingtime.adapters.RecipeIngredientsAdapter;
import com.example.android.bakingtime.adapters.RecipeStepsAdapter;
import com.example.android.bakingtime.model.Ingredients;
import com.example.android.bakingtime.model.Recipe;
import com.example.android.bakingtime.model.Steps;

import java.util.ArrayList;
import java.util.List;

public class RecipeStepsFragment extends Fragment implements RecipeStepsAdapter.ClickHandler {

    private RecyclerView mRecyclerView;
    private RecipeStepsAdapter mAdapter;
    private int mPosition;

    private RecyclerView mIngredientListRecyclerView;
    private RecipeIngredientsAdapter mIngredientsAdapter;
    List<Ingredients> mIngredients;

    StepInstructionsFragment stepInstructionsFragment;

    Recipe mRecipe;
    List<Steps> mRecipeSteps;
    boolean mTwoPane = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRecipe = new Recipe();
        mIngredients = new ArrayList<>();

        if (savedInstanceState != null) {
            mRecipeSteps = savedInstanceState.getParcelableArrayList("recipeSteps");
            mPosition = savedInstanceState.getInt("position");
            mIngredients = savedInstanceState.getParcelableArrayList("ingredientList");
        } else {

            if (getArguments() != null) {
                Bundle extras = getArguments();
//            mRecipe = (Recipe) extras.getSerializable("recipe");
                mRecipe = extras.getParcelable("recipe");
                mRecipeSteps = mRecipe.getSteps();
                mIngredients = mRecipe.getIngredients();
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recipe_steps, container, false);

        // set up landscape mode if correct view is loaded
        if (rootView.findViewById(R.id.tablet_container) != null) {
            mTwoPane = true;

            // set up ingredient list recyclerView
            mIngredientListRecyclerView = rootView.findViewById(R.id.recipe_ingredients_recyclerView);
            RecyclerView.LayoutManager ingredientsLayoutManager = new LinearLayoutManager(getContext());
            mIngredientListRecyclerView.setLayoutManager(ingredientsLayoutManager);
            mIngredientsAdapter = new RecipeIngredientsAdapter(getContext());
            mIngredientListRecyclerView.setAdapter(mIngredientsAdapter);
            mIngredientsAdapter.setIngredientsList(mIngredients);

            // set up recyclerView for recipe steps
            mRecyclerView = rootView.findViewById(R.id.recipe_steps_recyclerView);
            RecyclerView.LayoutManager stepsLayoutManager = new LinearLayoutManager(getContext());
            mRecyclerView.setLayoutManager(stepsLayoutManager);
            mAdapter = new RecipeStepsAdapter(getContext());
            mRecyclerView.setAdapter(mAdapter);
            mAdapter.AdapterClickListener(this);
            mAdapter.setRecipesSteps(mRecipeSteps);

            // set up recipe step details fragment
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("recipeSteps", (ArrayList<? extends Parcelable>) mRecipeSteps);
            bundle.putBoolean("splitView", true);
            stepInstructionsFragment = new StepInstructionsFragment();
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            stepInstructionsFragment.setArguments(bundle);
            fragmentTransaction.replace(R.id.recipe_step_details_container, stepInstructionsFragment);
            fragmentTransaction.commit();
        } else {
            mTwoPane = false;
            mRecyclerView = rootView.findViewById(R.id.recipe_steps_recyclerView);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            mAdapter = new RecipeStepsAdapter(getContext());
            mRecyclerView.setAdapter(mAdapter);
            mAdapter.AdapterClickListener(this);
            mAdapter.setRecipesSteps(mRecipeSteps);

            // set up ingredient list recyclerView
            mIngredientListRecyclerView = rootView.findViewById(R.id.recipe_ingredients_recyclerView);
            mIngredientListRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            mIngredientsAdapter = new RecipeIngredientsAdapter(getContext());
            mIngredientListRecyclerView.setAdapter(mIngredientsAdapter);
            mIngredientsAdapter.setIngredientsList(mIngredients);
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

        if (mTwoPane) {
            // maybe don't recreate the fragment each time...try just passing the new position since it already has the array of steps
            stepInstructionsFragment.updatePosition(position);

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
        outState.putParcelableArrayList("ingredientList", (ArrayList<? extends Parcelable>) mIngredients);
    }


    private void setUpIngredientListRecyclerView() {
        // set up ingredient list recyclerView
        mIngredientListRecyclerView = getActivity().findViewById(R.id.recipe_ingredients_recyclerView);
        mIngredientListRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//            mIngredientListRecyclerView.setHasFixedSize(false);
        mIngredientsAdapter = new RecipeIngredientsAdapter(getContext());
        mIngredientListRecyclerView.setAdapter(mIngredientsAdapter);
        mIngredientsAdapter.setIngredientsList(mIngredients);
    }

}
