package com.mitaoe.serviceexample;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextInputEditText urlInput;
    private Button downloadBtn;
    private Button viewDownloadsBtn;
    private RecyclerView recyclerView;
    private View historyCard;
    private View emptyStateLayout;
    private DownloadAdapter adapter;
    private List<DownloadItem> downloadList = new ArrayList<>();

    private final BroadcastReceiver onDownloadComplete = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            long downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            if (downloadId != -1) {
                refreshDownloads();
                openDownloadedFile(downloadId);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        
        urlInput = findViewById(R.id.urlInput);
        downloadBtn = findViewById(R.id.downloadBtn);
        viewDownloadsBtn = findViewById(R.id.viewDownloadsBtn);
        recyclerView = findViewById(R.id.downloadsRecyclerView);
        historyCard = findViewById(R.id.historyCard);
        emptyStateLayout = findViewById(R.id.emptyStateLayout);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new DownloadAdapter(downloadList, item -> {
            openFile(item.getUri(), item.getMimeType());
        });
        recyclerView.setAdapter(adapter);

        // Initial refresh to show current history
        refreshDownloads();

        ContextCompat.registerReceiver(this, onDownloadComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE), ContextCompat.RECEIVER_EXPORTED);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        downloadBtn.setOnClickListener(v -> {
            if (urlInput.getText() != null) {
                String url = urlInput.getText().toString().trim();
                if (url.isEmpty()) {
                    Toast.makeText(this, "Please enter a URL", Toast.LENGTH_SHORT).show();
                } else if (!URLUtil.isValidUrl(url)) {
                    Toast.makeText(this, "Please enter a valid URL", Toast.LENGTH_SHORT).show();
                } else {
                    startDownload(url);
                }
            }
        });

        viewDownloadsBtn.setOnClickListener(v -> {
            if (historyCard.getVisibility() == View.VISIBLE) {
                historyCard.setVisibility(View.GONE);
                viewDownloadsBtn.setText("EXPLORE");
            } else {
                refreshDownloads();
                historyCard.setVisibility(View.VISIBLE);
                viewDownloadsBtn.setText("CLOSE");
            }
        });
    }

    private void refreshDownloads() {
        downloadList.clear();
        DownloadManager manager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        if (manager != null) {
            DownloadManager.Query query = new DownloadManager.Query();
            query.setFilterByStatus(DownloadManager.STATUS_SUCCESSFUL);
            Cursor cursor = manager.query(query);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    int nameIndex = cursor.getColumnIndex(DownloadManager.COLUMN_TITLE);
                    int uriIndex = cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI);
                    int mimeIndex = cursor.getColumnIndex(DownloadManager.COLUMN_MEDIA_TYPE);
                    
                    String name = cursor.getString(nameIndex);
                    String uriString = cursor.getString(uriIndex);
                    String mime = cursor.getString(mimeIndex);
                    
                    if (uriString != null) {
                        downloadList.add(new DownloadItem(name, mime, Uri.parse(uriString)));
                    }
                }
                cursor.close();
            }
        }
        
        adapter.notifyDataSetChanged();
        if (downloadList.isEmpty()) {
            emptyStateLayout.setVisibility(View.VISIBLE);
            historyCard.setVisibility(View.GONE);
        } else {
            emptyStateLayout.setVisibility(View.GONE);
        }
    }

    private void openDownloadedFile(long downloadId) {
        DownloadManager manager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        if (manager != null) {
            Uri uri = manager.getUriForDownloadedFile(downloadId);
            String mimeType = manager.getMimeTypeForDownloadedFile(downloadId);
            openFile(uri, mimeType);
        }
    }

    private void openFile(Uri uri, String mimeType) {
        if (uri != null) {
            if (mimeType != null && (mimeType.startsWith("audio/") || mimeType.startsWith("video/"))) {
                Intent playerIntent = new Intent(this, PlayerActivity.class);
                playerIntent.putExtra("MEDIA_URI", uri.toString());
                startActivity(playerIntent);
            } else {
                Intent viewIntent = new Intent(Intent.ACTION_VIEW);
                viewIntent.setDataAndType(uri, mimeType);
                viewIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                viewIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                try {
                    startActivity(viewIntent);
                } catch (Exception e) {
                    Toast.makeText(this, "No app found to open this file", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(onDownloadComplete);
    }

    private void startDownload(String url) {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        // Use filename as title
        String fileName = URLUtil.guessFileName(url, null, null);
        request.setTitle(fileName);
        request.setDescription("Source: " + url);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);

        DownloadManager manager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        if (manager != null) {
            manager.enqueue(request);
            Toast.makeText(this, "Download started...", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Download Manager not available", Toast.LENGTH_SHORT).show();
        }
    }
}