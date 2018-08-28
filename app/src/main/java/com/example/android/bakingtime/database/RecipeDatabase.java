package com.example.android.bakingtime.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

import com.example.android.bakingtime.DataConverter;
import com.example.android.bakingtime.model.Recipe;

// https://codelabs.developers.google.com/codelabs/android-room-with-a-view/#6
@Database(entities = {Recipe.class}, version = 1, exportSchema = false)
@TypeConverters({DataConverter.class})
public abstract class RecipeDatabase extends RoomDatabase {

    public abstract RecipeDao recipeDao();
}
