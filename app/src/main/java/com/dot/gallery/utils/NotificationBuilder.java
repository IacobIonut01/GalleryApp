package com.dot.gallery.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.media.MediaScannerConnection;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import com.dot.gallery.R;

import java.io.File;
import java.net.URLConnection;

import androidx.core.app.NotificationCompat;
import androidx.fragment.app.FragmentActivity;

public class NotificationBuilder {

    private NotificationManager mNotifyManager;
    private NotificationCompat.Builder mBuilder;
    int id = 45621;


    public void initDeleteAlbumNotification(FragmentActivity afctivity, String album_name, String album_path) {
        mNotifyManager = afctivity.getSystemService(NotificationManager.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("0", "Media Manager", importance);
            channel.setDescription("Delete media files");
            mNotifyManager = afctivity.getSystemService(NotificationManager.class);
            mNotifyManager.createNotificationChannel(channel);
        }
        mBuilder = new NotificationCompat.Builder(afctivity, "0");
        mBuilder.setContentTitle(String.format("Deleting album '%s'", album_name))
                .setSmallIcon(R.drawable.ic_delete)
                .setOngoing(true);
        new Thread(
                () -> {
                    File dir = new File(album_path);
                    int counter = 0;
                    int max = 0;
                    String[] myFiles;
                    myFiles = dir.list();
                    for (String myFile1 : myFiles) {
                        File myFile = new File(dir, myFile1);
                        if (myFile.isFile() && (isImageFile(myFile.getPath()) || isVideoFile(myFile.getPath())))
                            max++;
                    }
                    mNotifyManager.notify(id, mBuilder.build());
                    for (String myFile1 : myFiles) {
                        File myFile = new File(dir, myFile1);
                        if (myFile.isFile() && (isImageFile(myFile.getPath()) || isVideoFile(myFile.getPath()))) {
                            if (MediaFileFunctions.deleteViaContentProvider(afctivity, myFile.getPath())) {
                                MediaScannerConnection.scanFile(afctivity, new String[]{Environment.getExternalStorageDirectory().toString()}, null, (path, uri) -> {
                                });
                                counter++;
                                mBuilder.setProgress(max, counter, false)
                                        .setContentText(String.format("%s/%s", counter, max));
                                mNotifyManager.notify(id, mBuilder.build());
                                if (counter == max) {
                                    mBuilder.setProgress(0, 0, false)
                                            .setContentTitle("Successfully deleted")
                                            .setSmallIcon(R.drawable.ic_check_black)
                                            .setContentText("Done")
                                            .setOngoing(false);
                                    mNotifyManager.notify(id, mBuilder.build());
                                }
                            } else {
                                Log.d("Deleter", myFile.getPath() + " not deleted");
                            }
                        }
                    }
                }
        ).start();
    }

    private boolean isImageFile(String path) {
        String mimeType = URLConnection.guessContentTypeFromName(path);
        return mimeType != null && mimeType.startsWith("image");
    }

    private boolean isVideoFile(String path) {
        String mimeType = URLConnection.guessContentTypeFromName(path);
        return mimeType != null && mimeType.startsWith("video");
    }

}
