package com.example.android.bakingtime.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.example.android.bakingtime.model.Recipe;

import java.util.List;

@Dao
public interface RecipeDao {

    @Query("SELECT * FROM recipes WHERE :id = id")
    Recipe getRecipeById(int id);

    @Query("SELECT * FROM recipes")
    List<Recipe> getAllRecipes();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Recipe> recipes);

}
