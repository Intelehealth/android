package org.intelehealth.app.utilities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

/**
 * Created by - Prajwal W. on 29/11/24.
 * Email: prajwalwaingankar@gmail.com
 * Mobile: +917304154312
 **/

public class PermissionHelper {

    // Constants
    private static final String TAG = "PermissionHelper";
    private static final int REQUEST_CODE_PERMISSIONS = 2001;

    // Permissions needed
    private static final String[] REQUIRED_PERMISSIONS = {
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO
    };

    private final Activity activity;

    // Constructor
    public PermissionHelper(Activity activity) {
        this.activity = activity;
    }

    // Check if all required permissions are granted
    public boolean hasAllPermissions() {
        for (String permission : REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    // Request missing permissions
    public void requestPermissions() {
        if (shouldShowRationale()) {
            Toast.makeText(activity, "Permissions are required for camera and microphone functionality.", Toast.LENGTH_LONG).show();
        }
        ActivityCompat.requestPermissions(activity, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);
    }

    // Check if any rationale should be shown to the user
    private boolean shouldShowRationale() {
        for (String permission : REQUIRED_PERMISSIONS) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                return true;
            }
        }
        return false;
    }

    // Handle permission results
    public void handlePermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            boolean allGranted = true;

            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, permissions[i] + " granted");
                } else {
                    Log.d(TAG, permissions[i] + " denied");
                    allGranted = false;
                }
            }

            if (allGranted) {
                Toast.makeText(activity, "All permissions granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(activity, "Some permissions denied", Toast.LENGTH_LONG).show();
                redirectToSettingsIfDenied();
            }
        }
    }

    // Redirect to app settings if permissions are permanently denied
    private void redirectToSettingsIfDenied() {
        if (!shouldShowRationale()) {
            Toast.makeText(activity, "Enable permissions from app settings.", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
            intent.setData(uri);
            activity.startActivity(intent);
        }
    }
}

