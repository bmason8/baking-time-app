package com.example.android.bakingtime.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RecipesList {

    @SerializedName("recipes")
    private List<Recipe> recipes;

    public List<Recipe> getRecipesList() {
        return recipes;
    }

    public void setRecipes(List<Recipe> recipeList) {
        this.recipes = recipeList;
    }
}
