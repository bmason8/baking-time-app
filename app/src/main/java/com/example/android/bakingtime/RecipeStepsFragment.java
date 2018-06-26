package com.example.android.bakingtime;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.bakingtime.adapters.RecipeStepsAdapter;
import com.example.android.bakingtime.model.Recipe;
import com.example.android.bakingtime.model.Steps;

import java.util.List;

public class RecipeStepsFragment extends Fragment implements RecipeStepsAdapter.ClickHandler {

    List<Steps> mRecipeSteps;

    public RecipeStepsFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Bundle bundle = getArguments();
        Recipe recipe = bundle.getParcelable("recipe");
        mRecipeSteps = recipe.getSteps();

        View rootView = inflater.inflate(R.layout.fragment_recipe_steps, container, false);


//        RecyclerView recyclerView = rootView.findViewById(R.id.recipe_steps_recyclerView);
//        RecipeStepsAdapter recipeStepsAdapter = new RecipeStepsAdapter(getContext());
//        recyclerView.setAdapter(recipeStepsAdapter);
//        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
//        recyclerView.setLayoutManager(layoutManager);

        RecyclerView recyclerView = rootView.findViewById(R.id.recipe_steps_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        RecipeStepsAdapter recipeStepsAdapter = new RecipeStepsAdapter(getContext());
        recyclerView.setAdapter(recipeStepsAdapter);
//        recipeStepsAdapter.AdapterClickListener(RecipeDetailsActivity.this);
        recipeStepsAdapter.setRecipesSteps(mRecipeSteps);

        return rootView;
    }

    @Override
    public void onItemClick(int position) {
        Steps steps = mRecipeSteps.get(position);
        // I think...build another fragment and inflate the view


    }
}
