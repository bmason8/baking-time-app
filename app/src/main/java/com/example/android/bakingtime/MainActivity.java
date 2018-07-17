package com.example.android.bakingtime;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.example.android.bakingtime.adapters.RecipeCardAdapter;
import com.example.android.bakingtime.database.RecipeDao;
import com.example.android.bakingtime.database.RecipeDatabase;
import com.example.android.bakingtime.model.Recipe;
import com.example.android.bakingtime.utilities.ApiInterface;
import com.example.android.bakingtime.utilities.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.android.bakingtime.utilities.ApiInterface.BAKING_RECIPE_JSON_URL;

public class MainActivity extends AppCompatActivity implements RecipeCardAdapter.ClickHandler {

    RecipeDatabase mDatabase;
    RecipeDao mRecipeDao;

    private List<Recipe> mRecipeList;
    private RecyclerView mRecyclerView;
    private RecipeCardAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (getIntent() != null && getIntent().getExtras() != null) {

            if (Objects.equals(getIntent().getStringExtra(Constants.RECIPE_INTENT_SOURCE), Constants.INTENT_FROM_WIDGET_CLICK)) {

                int recipeId = getIntent().getIntExtra(Constants.RECIPE_WIDGET_ID, 1);

                RecipeDatabase mRecipeDatabase = Room.databaseBuilder(getApplicationContext(), RecipeDatabase.class, "Recipe_db").allowMainThreadQueries().build();
                Recipe recipe = mRecipeDatabase.recipeDao().getRecipeById(recipeId);

                startRecipeDetailsActivityFromWidgetClick(recipe);

                // SHOULD TRY TO GET RECIPE FROM DATABASE FIRST
//                List<Recipe> mRecipeList = NetworkUtils.fetchRecipes(getApplicationContext());
//                recipe = mRecipeList.get(recipeId);
//                mRecipeSteps = recipe.getSteps();
            }
            }

        mRecipeList = new ArrayList<>();
        mRecyclerView = findViewById(R.id.recipe_cards_recyclerView);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new RecipeCardAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.AdapterClickListener(MainActivity.this);
        mAdapter.setRecipesList(mRecipeList);

        fetchRecipes();
    }

    private void startRecipeDetailsActivityFromWidgetClick(Recipe recipe) {
        Intent intent = new Intent(this, RecipeDetailsActivity.class);
        intent.putExtra(Constants.RECIPE_INTENT_SOURCE, Constants.INTENT_FROM_MAIN_ACTIVITY_CLICK);
        intent.putExtra("clickedRecipe", recipe);
        startActivity(intent);

    }

    private void fetchRecipes() {

        // Retrofit set up
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BAKING_RECIPE_JSON_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        Call<List<Recipe>> call = apiInterface.getRecipes();

        call.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(@NonNull Call<List<Recipe>> call, @NonNull Response<List<Recipe>> response) {
                List<Recipe> result = response.body();
                if (result != null) {
                    mAdapter.setRecipesList(result);
                    mRecipeList = result;
                    Log.d("result: ", mRecipeList.toString());

                    // Add all recipes to database
                    mDatabase = Room.databaseBuilder(getApplicationContext(), RecipeDatabase.class, "Recipe_db").allowMainThreadQueries().build();
                    mDatabase.recipeDao().insertAll(mRecipeList);

                } else {
                    Log.d("failed", "failed to retrieve recipe list");
                    Toast.makeText(MainActivity.this, "Failed to fetch recipes", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                Log.d("fail: ", t.getMessage());
            }
        });
    }

    @Override
    public void onItemClick(int position) {
        Recipe recipe = mRecipeList.get(position);
        // build an Intent to pass recipe information to recipe detail activity
        Intent intent = new Intent(this, RecipeDetailsActivity.class);
        intent.putExtra(Constants.RECIPE_INTENT_SOURCE, Constants.INTENT_FROM_MAIN_ACTIVITY_CLICK);
        intent.putExtra("clickedRecipe", recipe);
        startActivity(intent);
    }
}
