package org.intelehealth.apprtc.data;

public class Constants {
    //TODO: this base url will be same as user input server url, but port no is fixed here.
    // if server will change this port we have change this port no
    public static final String BASE_URL = Manager.getInstance().getBaseUrl() + ":3004";
    //public static final String BASE_URL = "https://testing.intelehealth.org:3004";

    public static final String ICE_SERVER_1_URL = "stun:stun.l.google.com:19302";
    public static final String ICE_SERVER_2_URL = "stun:stun1.l.google.com:19302";

    public static final String ICE_SERVER_3_URL = "turn:demo.intelehealth.org:3478";
    public static final String ICE_SERVER_3_USER = "ihuser";
    public static final String ICE_SERVER_3_PASSWORD = "keepitsecrect";

    public static final String ICE_SERVER_4_URL = "turn:testing.intelehealth.org:3478";
    public static final String ICE_SERVER_4_USER = "ihuser";
    public static final String ICE_SERVER_4_PASSWORD = "keepitsecrect";

    public static final String SEND_MESSAGE_URL = BASE_URL + "/api/messages/sendMessage";
    //@GET('https://testing.intelehealth.org:3004/messages/${fromUser}/${toUser}/${patientId}')
    public static final String GET_ALL_MESSAGE_URL = BASE_URL + "/api/messages/";
    public static final String SET_READ_STATUS_OF_MESSAGE_URL = BASE_URL + "/api/messages/read/"; //  https://uiux.intelehealth.org:3004/api/messages/read/881
    public static final String SAVE_FCM_TOKEN_URL = BASE_URL + "/api/mindmap/user_settings";

    public static final int LEFT_ITEM_DOCT = 1;
    public static final int RIGHT_ITEM_HW = 2;

    /*AWS S3 Bucket*/
    public static final String S3_ACCESS_ID = "AKIAQRYG2ZFM3FPZWEDN";
    public static final String S3_SECRET_KEY = "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";
    public static final String S3_BUCKET_NAME = "ih-addl-docs";
    public static final String S3_BUCKET_PREFIX_URL = "https://"+S3_BUCKET_NAME+".s3.ap-south-1.amazonaws.com/";

    public static final String IMAGE_CAPTURE_DONE_INTENT_ACTION = "org.intelehealth.app.IMAGE_CAPTURE_DONE_INTENT_ACTION";
    public static final String IMAGE_CAPTURE_REQUEST_INTENT_ACTION = "org.intelehealth.app.IMAGE_CAPTURE_REQUEST_INTENT_ACTION";


}
