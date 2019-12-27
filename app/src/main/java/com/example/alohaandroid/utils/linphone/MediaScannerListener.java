package com.example.alohaandroid.utils.linphone;

import android.net.Uri;

public interface MediaScannerListener {
    void onMediaScanned(String path, Uri uri);
}
