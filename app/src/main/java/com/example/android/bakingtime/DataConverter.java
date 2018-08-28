package com.example.android.bakingtime;

import android.arch.persistence.room.TypeConverter;

import com.example.android.bakingtime.model.Ingredients;
import com.example.android.bakingtime.model.Steps;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.List;

// https://mobikul.com/add-typeconverters-room-database-android/
public class DataConverter implements Serializable {

    // TypeConverter for List<Ingredients>
    @TypeConverter
    public String fromListIngredients(List<Ingredients> ingredientsList) {
        if (ingredientsList == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<Ingredients>>() {
        }.getType();
        String json = gson.toJson(ingredientsList, type);
        return json;
    }

    @TypeConverter
    public List<Ingredients> toIngredientsList(String optionValuesString) {
        if (optionValuesString == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<Ingredients>>() {
        }.getType();
        List<Ingredients> ingredients = gson.fromJson(optionValuesString, type);
        return ingredients;
    }

    // TypeConverter for List<Steps>
    @TypeConverter
    public String fromListSteps(List<Steps> stepsList) {
        if (stepsList == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<Steps>>() {
        }.getType();
        String json = gson.toJson(stepsList, type);
        return json;
    }

    @TypeConverter // note this annotation
    public List<Steps> toStepsList(String optionValuesString) {
        if (optionValuesString == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<Steps>>() {
        }.getType();
        List<Steps> steps = gson.fromJson(optionValuesString, type);
        return steps;
    }
}
