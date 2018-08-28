package com.example.android.bakingtime.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.bakingtime.R;
import com.example.android.bakingtime.adapters.RecipeCardAdapter;
import com.example.android.bakingtime.model.Recipe;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RecipeCardsFragment extends Fragment implements RecipeCardAdapter.ClickHandler {

    private List<Recipe> mRecipeList;
    private Recipe mRecipe;
    private RecyclerView mRecyclerView;
    private RecipeCardAdapter mAdapter;
    private boolean landscapeMode;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mRecipeList = (List<Recipe>) savedInstanceState.getSerializable("recipeList");
        } else {
            mRecipeList = new ArrayList<>();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recipe_cards, container, false);

        if (rootView.findViewById(R.id.tablet_container) != null) {
            landscapeMode = true;
        } else {
            landscapeMode = false;
        }


        if (getArguments() != null) {
            Bundle extras = getArguments();
            mRecipeList = (ArrayList<Recipe>) extras.getSerializable("recipeList");
        }

        mRecyclerView = rootView.findViewById(R.id.recipe_cards_recyclerView);

        if (rootView.findViewById(R.id.recipe_cards_tablet_layout) != null) {
            mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        } else {
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        }

        mRecyclerView.setHasFixedSize(true);
        mAdapter = new RecipeCardAdapter(getContext());
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.AdapterClickListener(RecipeCardsFragment.this);
        mAdapter.setRecipesList(mRecipeList);

        return rootView;
    }

    @Override
    public void onItemClick(int position) {
        Bundle bundle = new Bundle();
        // if in tablet/landscape mode then set up both fragments
            mRecipe = mRecipeList.get(position);
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            RecipeStepsFragment recipeStepsFragment = new RecipeStepsFragment();
            bundle.putParcelable("recipe", mRecipe);
            recipeStepsFragment.setArguments(bundle);
            fragmentManager.beginTransaction().replace(R.id.frame_fragment_holder, recipeStepsFragment).
                    addToBackStack(null).commit();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("recipeList", (Serializable) mRecipeList);
    }
}
