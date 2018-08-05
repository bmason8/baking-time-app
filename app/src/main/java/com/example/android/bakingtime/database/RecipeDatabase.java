package com.example.android.bakingtime.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

import com.example.android.bakingtime.DataConverter;
import com.example.android.bakingtime.model.Recipe;

@Database(entities = {Recipe.class}, version = 1, exportSchema = false)
@TypeConverters({DataConverter.class})
public abstract class RecipeDatabase extends RoomDatabase {

    public abstract RecipeDao recipeDao();

    private static RecipeDatabase INSTANCE;

    // https://codelabs.developers.google.com/codelabs/android-room-with-a-view/#6

//    static RecipeDatabase getDatabase(final Context context) {
//        if (INSTANCE == null) {
//            synchronized (RecipeDatabase.class) {
//                if (INSTANCE == null) {
//                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
//                            RecipeDatabase.class, "Recipe.db")
//                            .build();
//                }
//            }
//        }
//        return INSTANCE;
//    }
}
