package com.example.android.bakingtime;

import android.app.Activity;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
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

    PositionUpdatedListener mCallback;

//    @BindView(R.id.recipe_step_container)
//    ConstraintLayout mRecipeStepContainer;
//    @BindView(R.id.step_description)
//    TextView mStepDescription;
//    @BindView(R.id.step_number)
//    TextView mStepNumber;

    TestViewModel testViewModel;

    List<Steps> mRecipeSteps;
    MutableLiveData<List<Steps>> mTestSteps;
    boolean mTwoPane = false;

    public RecipeStepsFragment() {
    }

    public interface PositionUpdatedListener {
        void updatePosition(int position);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recipe_steps, container, false);
//        ButterKnife.bind(this, rootView);

        testViewModel = ViewModelProviders.of(this).get(TestViewModel.class);

        // Set up ViewModel and observe mRecipeStepsLiveData from it.
        // When it changes, set up the recyclerView stuff...which will update the adapter with the new mRecipeStepsLiveData

//        RecipeDetailsViewModel viewModel = ViewModelProviders.of(this).get(RecipeDetailsViewModel.class);
//        viewModel.getRecipeStepsLiveData().observe(this, new Observer<List<Steps>>() {
//            @Override
//            public void onChanged(@Nullable List<Steps> steps) {
//                mRecipeSteps = steps;
//
//            }
//        });


//        testViewModel.mRecipeStepsMutableLiveData.observe(this, new Observer<List<Steps>>() {
//            @Override
//            public void onChanged(@Nullable List<Steps> steps) {
//                if (steps != null) {
//                    mRecipeSteps = steps;
//                }
//            }
//        });

//        testViewModel.mRecipeMutableLiveData.observe(getActivity(), new Observer<Recipe>() {
//            @Override
//            public void onChanged(@Nullable Recipe recipe) {
//                if (recipe != null) {
//                    mRecipeSteps = recipe.getSteps();
//                }
//            }
//        });


//        mTestSteps = testViewModel.getRecipeStepsMutableLiveData();
//        mRecipeSteps = mTestSteps.getValue();
//        Log.d("mutableTest", String.valueOf(mTestSteps.getValue()));


//        testViewModel.getRecipeStepsMutableLiveData().observe(this, new Observer<List<Steps>>() {
//            @Override
//            public void onChanged(@Nullable List<Steps> steps) {
//                Log.d("mutableSteps", "onCHANGED RAN!");
//                Log.d("mutableSteps", "null if nothing more: " + String.valueOf(steps));
//                mRecipeSteps = steps;
//            }
//        });

        testViewModel.mTestString.observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                Toast.makeText(getContext(), s, Toast.LENGTH_LONG).show();
            }
        });

        if (mRecipeSteps == null) {
            Toast.makeText(getContext(), "mRecipeSteps was null! Used Bundle", Toast.LENGTH_LONG).show();
            Bundle bundle = getArguments();
            Recipe recipe = null;
            if (bundle != null) {
                recipe = bundle.getParcelable("recipe");
                mRecipeSteps = recipe.getSteps();
                mTwoPane = bundle.getBoolean("twoPane");
            }
        }

        RecyclerView recyclerView = rootView.findViewById(R.id.recipe_steps_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        RecipeStepsAdapter recipeStepsAdapter = new RecipeStepsAdapter(getContext());
        recyclerView.setAdapter(recipeStepsAdapter);
        recipeStepsAdapter.AdapterClickListener(this);
        recipeStepsAdapter.setRecipesSteps(mRecipeSteps);


        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity activity = (Activity) context;
        try {
            mCallback = (PositionUpdatedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement PositionUpdatedListener");
        }
    }



    @Override
    public void onItemClick(int position) {

        // Pass mRecipeSteps and position so I can move through it based on button clicks in StepInstructionsFragment
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("recipeSteps", (ArrayList<? extends Parcelable>) mRecipeSteps);
        bundle.putInt("position", position);

        // Update the look of the selected step


        if (mTwoPane) {
            // maybe don't recreate the fragment each time...try just passing the new position since it already has the array of steps

//            mCallback.updatePosition(position);

            StepInstructionsFragment stepInstructionsFragment = new StepInstructionsFragment();
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            stepInstructionsFragment.setArguments(bundle);
            fragmentTransaction.replace(R.id.recipe_step_details_container, stepInstructionsFragment);
            fragmentTransaction.commit();

        } else if (!mTwoPane) {
            Toast.makeText(getContext(), "Clicked! Single Pane", Toast.LENGTH_SHORT).show();

            // Build another fragment and perform a FragmentTransaction
            StepInstructionsFragment stepInstructionsFragment = new StepInstructionsFragment();
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            stepInstructionsFragment.setArguments(bundle);
            fragmentTransaction.replace(R.id.fragment_container, stepInstructionsFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("recipeSteps", (ArrayList<? extends Parcelable>) mRecipeSteps);
    }

    //    private void updateLookOfSelectedStep() {
//        mStepDescription.setTextColor(Color.WHITE);
//        mStepNumber.setTextColor(Color.WHITE);
//        mRecipeStepContainer.setBackgroundResource(R.drawable.round_corners_selected);
//
//    }
}
