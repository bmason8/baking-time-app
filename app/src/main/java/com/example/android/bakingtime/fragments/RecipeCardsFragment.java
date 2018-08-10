package com.example.android.bakingtime.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.android.bakingtime.R;
import com.example.android.bakingtime.adapters.RecipeCardAdapter;
import com.example.android.bakingtime.model.Recipe;

import java.util.ArrayList;
import java.util.List;

public class RecipeCardsFragment extends Fragment implements RecipeCardAdapter.ClickHandler {

    private List<Recipe> mRecipeList;
    private Recipe mRecipe;
    private RecyclerView mRecyclerView;
    private RecipeCardAdapter mAdapter;
    private int recipeId;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recipe_cards, container, false);

        Toast.makeText(getContext(), "RecipeCardsFragment Created!", Toast.LENGTH_SHORT).show();

        mRecipeList = new ArrayList<>();

        if (getArguments() != null) {
            Toast.makeText(getContext(), "received arguments", Toast.LENGTH_SHORT).show();
            Bundle extras = getArguments();
            mRecipeList = (ArrayList<Recipe>) extras.getSerializable("recipe");
        }


        mRecyclerView = rootView.findViewById(R.id.recipe_cards_recyclerView);

        if (rootView.findViewById(R.id.activity_main_tablet_layout) != null) {
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
        // TODO: build a StepsFragment and pass the recipe along through a bundle/arguments

    }
}
