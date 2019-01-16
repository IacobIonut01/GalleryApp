package com.dot.gallery.utils;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;

public class MediaFileFunctions {

    public static boolean deleteViaContentProvider(Context context, String fullname) {
        Uri uri = getFileUri(context, fullname);

        if (uri == null) {
            return false;
        }

        try {
            ContentResolver resolver = context.getContentResolver();

            ContentValues contentValues = new ContentValues();
            int media_type = 1;
            contentValues.put("media_type", media_type);
            resolver.update(uri, contentValues, null, null);

            return resolver.delete(uri, null, null) > 0;
        } catch (Throwable e) {
            return false;
        }
    }

    private static Uri getFileUri(Context context, String fullname) {
        Uri uri = null;
        Cursor cursor = null;
        ContentResolver contentResolver = null;

        try {
            contentResolver = context.getContentResolver();
            if (contentResolver == null)
                return null;

            uri = MediaStore.Files.getContentUri("external");
            String[] projection = new String[2];
            projection[0] = "_id";
            projection[1] = "_data";
            String selection = "_data = ? ";    // this avoids SQL injection
            String[] selectionParams = new String[1];
            selectionParams[0] = fullname;
            String sortOrder = "_id";
            cursor = contentResolver.query(uri, projection, selection, selectionParams, sortOrder);

            if (cursor != null) {
                try {
                    if (cursor.getCount() > 0) // file present!
                    {
                        cursor.moveToFirst();
                        int dataColumn = cursor.getColumnIndex("_data");
                        String s = cursor.getString(dataColumn);
                        if (!s.equals(fullname))
                            return null;
                        int idColumn = cursor.getColumnIndex("_id");
                        long id = cursor.getLong(idColumn);
                        uri = MediaStore.Files.getContentUri("external", id);
                    } else // file isn't in the media database!
                    {
                        ContentValues contentValues = new ContentValues();
                        contentValues.put("_data", fullname);
                        uri = MediaStore.Files.getContentUri("external");
                        uri = contentResolver.insert(uri, contentValues);
                    }
                } catch (Throwable e) {
                    uri = null;
                } finally {
                    cursor.close();
                }
            }
        } catch (Throwable e) {
            uri = null;
        }
        return uri;
    }
}