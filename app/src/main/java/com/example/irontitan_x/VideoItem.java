package com.example.irontitan_x;

public class VideoItem {
    private int thumbnailResId;
    private String title;

    public VideoItem(int thumbnailResId, String title) {
        this.thumbnailResId = thumbnailResId;
        this.title = title;
    }

    public int getThumbnailResId() {
        return thumbnailResId;
    }

    public String getTitle() {
        return title;
    }
}
