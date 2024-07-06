package com.example.irontitan_x;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder> {

    private List<VideoItem> videoItems;

    public VideoAdapter(List<VideoItem> videoItems) {
        this.videoItems = videoItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_item_video, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        VideoItem item = videoItems.get(position);
        holder.videoThumbnail.setImageResource(item.getThumbnailResId());
        holder.videoTitle.setText(item.getTitle());
    }

    @Override
    public int getItemCount() {
        return videoItems.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView videoThumbnail;
        ImageButton playButton;
        CheckBox selectCheckBox;
        TextView videoTitle;

        ViewHolder(View itemView) {
            super(itemView);
            videoThumbnail = itemView.findViewById(R.id.videoThumbnail);
            playButton = itemView.findViewById(R.id.playButton);
            selectCheckBox = itemView.findViewById(R.id.selectCheckBox);
            videoTitle = itemView.findViewById(R.id.videoTitle);
        }
    }
}
