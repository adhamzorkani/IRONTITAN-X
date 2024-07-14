package com.example.irontitan_x;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder> {

    private List<VideoItem> videoItems;
    private OnPlayButtonClickListener playButtonClickListener;
    private OnCheckBoxClickListener checkBoxClickListener;

    public VideoAdapter(List<VideoItem> videoItems, OnPlayButtonClickListener playButtonClickListener, OnCheckBoxClickListener checkBoxClickListener) {
        this.videoItems = videoItems;
        this.playButtonClickListener = playButtonClickListener;
        this.checkBoxClickListener = checkBoxClickListener;
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
        holder.videoTitle.setText(item.getTitle());
        Glide.with(holder.itemView.getContext()).load(item.getThumbnailUrl()).into(holder.videoThumbnail);

        holder.playButton.setOnClickListener(v -> playButtonClickListener.onPlayButtonClicked(item.getVideoUrl()));

        holder.selectCheckBox.setChecked(item.isSelected());
        holder.selectCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            item.setSelected(isChecked);
            checkBoxClickListener.onCheckBoxClicked(item, isChecked);
        });
    }

    @Override
    public int getItemCount() {
        return videoItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView videoThumbnail;
        ImageButton playButton;
        CheckBox selectCheckBox;
        TextView videoTitle;

        public ViewHolder(View itemView) {
            super(itemView);
            videoThumbnail = itemView.findViewById(R.id.videoThumbnail);
            playButton = itemView.findViewById(R.id.playButton);
            selectCheckBox = itemView.findViewById(R.id.selectCheckBox);
            videoTitle = itemView.findViewById(R.id.videoTitle);
        }
    }

    public interface OnPlayButtonClickListener {
        void onPlayButtonClicked(String url);
    }

    public interface OnCheckBoxClickListener {
        void onCheckBoxClicked(VideoItem videoItem, boolean isChecked);
    }
}
