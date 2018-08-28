package com.example.android.bakingtime.model;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;
/**
 * Awesome Pojo Generator
 * */
@Entity(tableName = "recipes")
public class Recipe implements android.os.Parcelable {

  @SerializedName("image")
  @Expose
  private String image;

  @SerializedName("servings")
  @Expose
  private Integer servings;

  @SerializedName("name")
  @Expose
  private String name;

  @SerializedName("ingredients")
  @Expose
  private List<Ingredients> ingredients;

  @PrimaryKey
  @SerializedName("id")
  @Expose
  private Integer id;

  @SerializedName("steps")
  @Expose
  private List<Steps> steps;

  public void setImage(String image){
   this.image=image;
  }
  public String getImage(){
   return image;
  }
  public void setServings(Integer servings){
   this.servings=servings;
  }
  public Integer getServings(){
   return servings;
  }
  public void setName(String name){
   this.name=name;
  }
  public String getName(){
   return name;
  }
  public void setIngredients(List<Ingredients> ingredients){
   this.ingredients=ingredients;
  }
  public List<Ingredients> getIngredients(){
   return ingredients;
  }
  public void setId(Integer id){
   this.id=id;
  }
  public Integer getId(){
   return id;
  }
  public void setSteps(List<Steps> steps){
   this.steps=steps;
  }
  public List<Steps> getSteps(){
   return steps;
  }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.image);
        dest.writeValue(this.servings);
        dest.writeString(this.name);
        dest.writeTypedList(this.ingredients);
        dest.writeValue(this.id);
        dest.writeTypedList(this.steps);
    }

    public Recipe() {
    }

    protected Recipe(Parcel in) {
        this.image = in.readString();
        this.servings = (Integer) in.readValue(Integer.class.getClassLoader());
        this.name = in.readString();
        this.ingredients = in.createTypedArrayList(Ingredients.CREATOR);
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.steps = in.createTypedArrayList(Steps.CREATOR);
    }

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel source) {
            return new Recipe(source);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };
}