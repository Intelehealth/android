package org.intelehealth.app.utilities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;


import com.google.firebase.crashlytics.FirebaseCrashlytics;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.intelehealth.app.app.AppConstants;

public class Base64Utils {
    private static String TAG = Base64Utils.class.getSimpleName();

    public String encoded(String USERNAME, String PASSWORD) {
        String encoded = null;
        encoded = Base64.encodeToString((USERNAME + ":" + PASSWORD).getBytes(StandardCharsets.UTF_8), Base64.NO_WRAP);
        return encoded;
    }

    public String getBase64FromFileWithConversion(String path) {
        Bitmap bmp = null;
        ByteArrayOutputStream baos = null;
        byte[] baat = null;
        String encodeString = "";
        try {
            bmp = BitmapFactory.decodeFile(path);
            baos = new ByteArrayOutputStream();
            if (bmp != null) {
                bmp.compress(Bitmap.CompressFormat.JPEG, AppConstants.IMAGE_JPG_QUALITY, baos);
                baat = baos.toByteArray();
                encodeString = Base64.encodeToString(baat, Base64.DEFAULT);
            }
        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().recordException(e);
        }

        return encodeString;
    }

    public static String convertAudioFileToBase64(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            Log.e(TAG, "File does not exist at the specified path: " + filePath);
            return null;
        }

        try {
            // Read the file into a byte array
            FileInputStream fis = new FileInputStream(file);
            byte[] fileBytes = new byte[(int) file.length()];
            fis.read(fileBytes);
            fis.close();

            // Encode the byte array to Base64
            return Base64.encodeToString(fileBytes, Base64.DEFAULT);
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
