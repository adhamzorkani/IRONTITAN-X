package com.example.irontitan_x;

import android.os.Parcel;
import android.os.Parcelable;

public class VideoItem implements Parcelable {
    private String thumbnailUrl;
    private String title;
    private String videoUrl;
    private boolean isSelected;

    public VideoItem(String thumbnailUrl, String title, String videoUrl) {
        this.thumbnailUrl = thumbnailUrl;
        this.title = title;
        this.videoUrl = videoUrl;
        this.isSelected = false;
    }

    protected VideoItem(Parcel in) {
        thumbnailUrl = in.readString();
        title = in.readString();
        videoUrl = in.readString();
        isSelected = in.readByte() != 0;
    }

    public static final Creator<VideoItem> CREATOR = new Creator<VideoItem>() {
        @Override
        public VideoItem createFromParcel(Parcel in) {
            return new VideoItem(in);
        }

        @Override
        public VideoItem[] newArray(int size) {
            return new VideoItem[size];
        }
    };

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(thumbnailUrl);
        dest.writeString(title);
        dest.writeString(videoUrl);
        dest.writeByte((byte) (isSelected ? 1 : 0));
    }
}
