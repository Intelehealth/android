package org.intelehealth.app.utilities;

import android.util.Log;

import org.intelehealth.app.app.AppConstants;

/**
 * Created by Prajwal Maruti Waingankar on 04-07-2022, 18:23
 * Copyright (c) 2021 . All rights reserved.
 * Email: prajwalwaingankar@gmail.com
 * Github: prajwalmw
 */

public class TimeRecordUtils {

    public synchronized static void record(String describe, long timemills){
        Log.e("Fu", timemills + "\t" + describe);
    }

    public static String getCurrentTimeForFileName() {
        return DateAndTimeUtils.getCurrentDateTimeForFileName();
    }
}
