package com.hassan.masla7ty.cameradeal;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

/**
 * Created by Hassan on 5/2/2015.
 */

    public class PhotoEncryptDecrypt {
        public static byte[] getBytes(Bitmap bitmap) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);


            return stream.toByteArray();

        }

        // convert from byte array to bitmap
        public static Bitmap getPhoto(byte[] image) {
            return BitmapFactory.decodeByteArray(image, 0, image.length);
        }
    public static String getString(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 0, stream);
        String imageEncoded = Base64.encodeToString(stream.toByteArray(), Base64.DEFAULT);


        return imageEncoded;

    }


    }

