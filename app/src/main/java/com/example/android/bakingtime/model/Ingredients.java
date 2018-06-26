package com.example.android.bakingtime.model;
import android.os.Parcel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
/**
 * Awesome Pojo Generator
 * */
public class Ingredients implements android.os.Parcelable {

  @SerializedName("quantity")
  @Expose
  private double quantity;

  @SerializedName("measure")
  @Expose
  private String measure;

  @SerializedName("ingredient")
  @Expose
  private String ingredient;

  public void setQuantity(double quantity){
   this.quantity=quantity;
  }
  public double getQuantity(){
   return quantity;
  }
  public void setMeasure(String measure){
   this.measure=measure;
  }
  public String getMeasure(){
   return measure;
  }
  public void setIngredient(String ingredient){
   this.ingredient=ingredient;
  }
  public String getIngredient(){
   return ingredient;
  }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.quantity);
        dest.writeString(this.measure);
        dest.writeString(this.ingredient);
    }

    public Ingredients() {
    }

    protected Ingredients(Parcel in) {
        this.quantity = (double) in.readValue(double.class.getClassLoader());
        this.measure = in.readString();
        this.ingredient = in.readString();
    }

    public static final Creator<Ingredients> CREATOR = new Creator<Ingredients>() {
        @Override
        public Ingredients createFromParcel(Parcel source) {
            return new Ingredients(source);
        }

        @Override
        public Ingredients[] newArray(int size) {
            return new Ingredients[size];
        }
    };
}