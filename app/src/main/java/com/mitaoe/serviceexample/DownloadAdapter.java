package com.mitaoe.serviceexample;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DownloadAdapter extends RecyclerView.Adapter<DownloadAdapter.ViewHolder> {

    private List<DownloadItem> downloads;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(DownloadItem item);
    }

    public DownloadAdapter(List<DownloadItem> downloads, OnItemClickListener listener) {
        this.downloads = downloads;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_download, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DownloadItem item = downloads.get(position);
        holder.nameText.setText(item.getName());
        holder.infoText.setText(item.getMimeType());
        
        // Basic icon logic based on type
        if (item.getMimeType() != null) {
            if (item.getMimeType().contains("audio")) {
                holder.icon.setImageResource(android.R.drawable.ic_lock_silent_mode_off);
            } else if (item.getMimeType().contains("video")) {
                holder.icon.setImageResource(android.R.drawable.ic_media_play);
            } else if (item.getMimeType().contains("pdf")) {
                holder.icon.setImageResource(android.R.drawable.ic_menu_edit);
            }
        }

        holder.itemView.setOnClickListener(v -> listener.onItemClick(item));
    }

    @Override
    public int getItemCount() {
        return downloads.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameText, infoText;
        ImageView icon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.itemName);
            infoText = itemView.findViewById(R.id.itemInfo);
            icon = itemView.findViewById(R.id.itemIcon);
        }
    }
}