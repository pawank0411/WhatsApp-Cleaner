package com.example.pawan.whatsAppcleaner;

import android.os.Environment;

public class DataHolder {
    public static final String IMAGE = "image";
    public static final String DOCUMENT = "document";
    public static final String VIDEO = "video";
    public static final String AUDIO = "audio";
    public static final String GIF = "gif";
    public static final String WALLPAPER = "Wallpaper";
    public static final String VOICE = "Voice";
/*/WhatsApp/Media/WhatsApp Images**/
    public static final String imagesReceivedPath = Environment.getExternalStorageDirectory().toString() + "/WhatsApp/Media/WhatsApp Images";
    public static final String imagesSentPath = Environment.getExternalStorageDirectory().toString() + "/WhatsApp/Media/WhatsApp Images/Sent";

    public static final String documentsReceivedPath = Environment.getExternalStorageDirectory().toString() + "/WhatsApp/Media/WhatsApp Documents";
    public static final String documentsSentPath = Environment.getExternalStorageDirectory().toString() + "/WhatsApp/Media/WhatsApp Documents/Sent";

    public static final String videosReceivedPath = Environment.getExternalStorageDirectory().toString() + "/WhatsApp/Media/WhatsApp Video";
    public static final String videosSentPath = Environment.getExternalStorageDirectory().toString() + "/WhatsApp/Media/WhatsApp Video/Sent";

    public static final String audiosReceivedPath = Environment.getExternalStorageDirectory().toString() + "/WhatsApp/Media/WhatsApp Audio";
    public static final String audiosSentPath = Environment.getExternalStorageDirectory().toString() + "/WhatsApp/Media/WhatsApp Audio/Sent";

    public static final String gifReceivedPath = Environment.getExternalStorageDirectory().toString() + "/WhatsApp/Media/WhatsApp Animated Gifs";
    public static final String gifSentPath = Environment.getExternalStorageDirectory().toString() + "/WhatsApp/Media/WhatsApp Animated Gifs/Sent";

    public static final String wallperReceivedPath = Environment.getExternalStorageDirectory().toString() + "/WhatsApp/Media/WhatsApp WallPaper";
    public static final String wallperSentPath = Environment.getExternalStorageDirectory().toString() + "/WhatsApp/Media/WhatsApp WallPaper";

    public static final String voiceRecievedPath = Environment.getExternalStorageDirectory().toString() + "/WhatsApp/Media/WhatsApp Voice Notes";
    public static final String voiceSentPath = Environment.getExternalStorageDirectory().toString() + "/WhatsApp/Media/WhatsApp Voice Notes";
}
