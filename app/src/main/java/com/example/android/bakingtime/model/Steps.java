package com.example.android.bakingtime.model;
import android.arch.lifecycle.MutableLiveData;
import android.os.Parcel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
/**
 * Awesome Pojo Generator
 * */
public class Steps extends MutableLiveData implements android.os.Parcelable {

  @SerializedName("videoURL")
  @Expose
  private String videoURL;

  @SerializedName("description")
  @Expose
  private String description;

  @SerializedName("id")
  @Expose
  private Integer id;

  @SerializedName("shortDescription")
  @Expose
  private String shortDescription;

  @SerializedName("thumbnailURL")
  @Expose
  private String thumbnailURL;

  public void setVideoURL(String videoURL){
   this.videoURL=videoURL;
  }
  public String getVideoURL(){
   return videoURL;
  }
  public void setDescription(String description){
   this.description=description;
  }
  public String getDescription(){
   return description;
  }
  public void setId(Integer id){
   this.id=id;
  }
  public Integer getId(){
   return id;
  }
  public void setShortDescription(String shortDescription){
   this.shortDescription=shortDescription;
  }
  public String getShortDescription(){
   return shortDescription;
  }
  public void setThumbnailURL(String thumbnailURL){
   this.thumbnailURL=thumbnailURL;
  }
  public String getThumbnailURL(){
   return thumbnailURL;
  }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.videoURL);
        dest.writeString(this.description);
        dest.writeValue(this.id);
        dest.writeString(this.shortDescription);
        dest.writeString(this.thumbnailURL);
    }

    public Steps() {
    }

    protected Steps(Parcel in) {
        this.videoURL = in.readString();
        this.description = in.readString();
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.shortDescription = in.readString();
        this.thumbnailURL = in.readString();
    }

    public static final Creator<Steps> CREATOR = new Creator<Steps>() {
        @Override
        public Steps createFromParcel(Parcel source) {
            return new Steps(source);
        }

        @Override
        public Steps[] newArray(int size) {
            return new Steps[size];
        }
    };
}