package com.mitaoe.serviceexample;

import android.net.Uri;

public class DownloadItem {
    private String name;
    private String mimeType;
    private Uri uri;

    public DownloadItem(String name, String mimeType, Uri uri) {
        this.name = name;
        this.mimeType = mimeType;
        this.uri = uri;
    }

    public String getName() { return name; }
    public String getMimeType() { return mimeType; }
    public Uri getUri() { return uri; }
}