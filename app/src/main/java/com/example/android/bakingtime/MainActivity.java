package com.example.android.bakingtime;

import android.arch.persistence.room.Room;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.example.android.bakingtime.adapters.RecipeCardAdapter;
import com.example.android.bakingtime.database.RecipeDatabase;
import com.example.android.bakingtime.database.RoomAccess;
import com.example.android.bakingtime.fragments.RecipeCardsFragment;
import com.example.android.bakingtime.fragments.RecipeStepsFragment;
import com.example.android.bakingtime.model.Recipe;
import com.example.android.bakingtime.utilities.ApiInterface;
import com.example.android.bakingtime.utilities.Constants;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.android.bakingtime.utilities.ApiInterface.BAKING_RECIPE_JSON_URL;

public class MainActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;
    RecipeCardsFragment recipeCardsFragment;

    private List<Recipe> mRecipeList;
    private Recipe mRecipe;
    private RecyclerView mRecyclerView;
    private RecipeCardAdapter mAdapter;
    private int recipeId;
    private boolean setUpNewFragment;
    private boolean isFromWidget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentManager = getSupportFragmentManager();

        mRecipeList = new ArrayList<>();

        if (savedInstanceState == null) {
            setUpNewFragment = true;
            Log.d("MAIN", "saveInstanceState is null");

            // Check the database for Recipes. If not null then get them, if not then run fetchRecipes to download them.
            new GetAllRecipesFromDb().execute();

        } else {
            savedInstanceState.getSerializable("recipeList");
        }



        if (getIntent() != null && getIntent().getExtras() != null) {

            if (Objects.equals(getIntent().getStringExtra(Constants.RECIPE_INTENT_SOURCE), Constants.INTENT_FROM_WIDGET_CLICK)) {

                isFromWidget = true;
                clearBackStackTest();
//                fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

                recipeId = getIntent().getIntExtra(Constants.RECIPE_WIDGET_ID, 1);
                Log.d("IDrecievedByMA: ", String.valueOf(recipeId));
                new GetRecipeById().execute();
            }
        }
    }

    private void startRecipeStepsFragmentFromWidgetClick(Recipe recipe) {
        Log.d("MAIN", "startRecipeStepsFragmentFromWidgetClick ran");

        FragmentManager fragmentManager = getSupportFragmentManager();
        RecipeStepsFragment recipeStepsFragment = new RecipeStepsFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("recipe", recipe);
        recipeStepsFragment.setArguments(bundle);
        fragmentManager.beginTransaction().replace(R.id.frame_fragment_holder, recipeStepsFragment, "TAG")
                .commit();
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
//                    mAdapter.setRecipesList(result);
                    mRecipeList = result;
                    if (setUpNewFragment) {
                        setUpRecipeCardsFragment();
                    }

                    // Add all recipes to database
                    // http://androidkt.com/room-persistence-library/
                    RoomAccess.insertRecipes(mRecipeList, getApplicationContext());

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


    private void setUpRecipeCardsFragment() {
        clearBackStack();
        Toast.makeText(getApplicationContext(), "setUpRecipeCardsFragment", Toast.LENGTH_SHORT).show();
//        fragmentManager = getSupportFragmentManager();
        recipeCardsFragment = new RecipeCardsFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("recipeList", (Serializable) mRecipeList);
        recipeCardsFragment.setArguments(bundle);
        fragmentManager.beginTransaction().replace(R.id.frame_fragment_holder, recipeCardsFragment).commit();
    }

    private class GetRecipeById extends AsyncTask<Void, Void, Recipe> {

        @Override
        protected Recipe doInBackground(Void... voids) {
            RecipeDatabase mDatabase = Room.databaseBuilder(getApplicationContext(), RecipeDatabase.class, "Recipe_db").build();
            mRecipe = mDatabase.recipeDao().getRecipeById(recipeId);
            mDatabase.close();
            return mRecipe;
        }

        @Override
        protected void onPostExecute(Recipe recipe) {
            startRecipeStepsFragmentFromWidgetClick(recipe);
        }
    }

    private class GetAllRecipesFromDb extends AsyncTask<Void, Void, List<Recipe>> {

        @Override
        protected List<Recipe> doInBackground(Void... voids) {
            RecipeDatabase mDatabase = Room.databaseBuilder(getApplicationContext(), RecipeDatabase.class, "Recipe_db").build();
            mRecipeList = mDatabase.recipeDao().getAllRecipes();
            mDatabase.close();
            return mRecipeList;
        }

        @Override
        protected void onPostExecute(List<Recipe> recipeList) {
            if (recipeList.isEmpty()) {
                fetchRecipes();
            } else {
                mRecipeList = recipeList;
                if (!isFromWidget) {
                setUpRecipeCardsFragment();
                }
            }
        }
    }

    private void clearBackStack(){
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        fragmentManager.popBackStackImmediate();
    }

    private void clearBackStackTest() {
        FragmentManager manager = getSupportFragmentManager();
        if (manager.getBackStackEntryCount() > 0) {
            FragmentManager.BackStackEntry first = manager.getBackStackEntryAt(0);
            manager.popBackStack(first.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("recipeList", (Serializable) mRecipeList);
    }
}
