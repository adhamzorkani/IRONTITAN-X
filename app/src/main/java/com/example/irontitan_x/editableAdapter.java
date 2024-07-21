package com.example.irontitan_x;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.irontitan_x.editableItem;

import java.util.List;

public class editableAdapter extends RecyclerView.Adapter<editableAdapter.ViewHolder> {

    private List<editableItem> userProfiles;

    public editableAdapter(List<editableItem> userProfiles) {
        this.userProfiles = userProfiles;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_editable, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        editableItem userProfile = userProfiles.get(position);
        holder.title.setText(userProfile.getTitle());
        holder.editText.setText(userProfile.getValue());

        holder.editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                userProfile.setValue(s.toString());
            }
        });
    }

    @Override
    public int getItemCount() {
        return userProfiles.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        EditText editText;

        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            editText = itemView.findViewById(R.id.editText);
        }
    }
}
