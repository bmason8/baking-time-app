package com.example.android.bakingtime.utilities;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.example.android.bakingtime.database.RecipeDatabase;
import com.example.android.bakingtime.model.Recipe;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.android.bakingtime.utilities.ApiInterface.BAKING_RECIPE_JSON_URL;

public class NetworkUtils {
    private static List<Recipe> mRecipeList;

    public static Uri convertStringToUri(String mediaString) {
        return Uri.parse(mediaString);
    }


    public static List<Recipe> fetchRecipes(final Context context) {

        mRecipeList = new ArrayList<>();

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
                    mRecipeList = result;

                    // Add all recipes to database
                    RecipeDatabase recipeDatabase = Room.databaseBuilder(context, RecipeDatabase.class, "Recipe_db").allowMainThreadQueries().build();
                    recipeDatabase.recipeDao().insertAll(mRecipeList);

                } else {
                    Log.d("failed", "failed to retrieve recipe list");
                    Toast.makeText(context, "Failed to fetch recipes", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG).show();
                Log.d("fail: ", t.getMessage());
            }
        });


        return mRecipeList;
    }


}
