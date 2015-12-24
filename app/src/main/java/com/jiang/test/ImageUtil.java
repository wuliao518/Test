package com.jiang.test;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by damin on 2015/8/20.
 */
public class ImageUtil {



    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 将图片旋转
     *
     * @param angle
     * @param bitmap
     * @return
     */
    public static Bitmap rotaingImageView(int angle, Bitmap bitmap) {
        //旋转图片 动作
        Matrix matrix = new Matrix();
        ;
        matrix.postRotate(angle);
        // 创建新的图片
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return resizedBitmap;
    }






    //将file对象压缩、旋转后存放
    public static File saveImage(File file) {
        if (file.exists()) {
            try {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
                int scale = 1;
                float bitWidth = options.outWidth;
                float bitHeight = options.outHeight;
                float width = 720;
                float height = 600;
                float scaleX = bitWidth / width;
                float scaleY = bitHeight / height;
                scale = (int) Math.ceil(Math.max(scaleX, scaleY));
                try {
                    if (scale > 1) {
                        options.inJustDecodeBounds = false;
                        options.inSampleSize = scale;
                    } else {
                        options.inJustDecodeBounds = false;
                        options.inSampleSize = 1;
                    }
                    bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
                } catch (OutOfMemoryError e) {
                    options.inJustDecodeBounds = false;
                    options.inSampleSize = scale + 1;
                    try {
                        bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
                    } catch (OutOfMemoryError e1) {
                        options.inJustDecodeBounds = false;
                        options.inSampleSize = scale + 1;
                        bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
                    }
                }
                int degree = readPictureDegree(file.getAbsolutePath());
                bitmap = rotaingImageView(degree, bitmap);
                OutputStream out = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                out.close();
                return file;
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    /**
     * 将data中的数据存储到file中
     *
     * @param context
     * @param data
     * @param file
     * @return
     */
    public static File photoImage(Context context, Intent data, File file) {
        try {
            if (data == null) {
                return null;
            }
            if (file.exists()) {
                file.delete();
                file.createNewFile();
            }
            Uri uri = data.getData();
            ContentResolver cr = context.getContentResolver();
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = false;
            Bitmap bitmap = BitmapFactory.decodeStream(
                    cr.openInputStream(uri), null, options);
            int scale = 1;
            float bitWidth = options.outWidth;
            float bitHeight = options.outHeight;
            float width = 720;
            float height = 600;
            float scaleX = bitWidth / width;
            float scaleY = bitHeight / height;
            scale = (int) Math.ceil(Math.max(scaleX, scaleY));
            try {
                if (scale > 1) {
                    options.inJustDecodeBounds = false;
                    options.inSampleSize = scale;
                } else {
                    options.inJustDecodeBounds = false;
                    options.inSampleSize = 1;
                }
                bitmap = BitmapFactory.decodeStream(
                        cr.openInputStream(uri), null, options);
            } catch (OutOfMemoryError e) {
                try {
                    options.inJustDecodeBounds = false;
                    options.inSampleSize = scale + 1;
                } catch (OutOfMemoryError e1) {
                    options.inJustDecodeBounds = false;
                    options.inSampleSize = scale + 1;
                }
                try {
                    bitmap = BitmapFactory.decodeStream(
                            cr.openInputStream(uri), null, options);
                } catch (OutOfMemoryError e1) {
                    try {
                        options.inJustDecodeBounds = false;
                        options.inSampleSize = scale + 1;
                    } catch (OutOfMemoryError e2) {
                        options.inJustDecodeBounds = false;
                        options.inSampleSize = scale + 1;
                    }
                    bitmap = BitmapFactory.decodeStream(
                            cr.openInputStream(uri), null, options);
                }

            }
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.close();
            return file;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static String compressImage(String path, String newPath) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        int inSampleSize = 1;
        int maxSize = 3000;

        if (options.outWidth > maxSize || options.outHeight > maxSize) {
            int widthScale = (int) Math.ceil(options.outWidth * 1.0 / maxSize);
            int heightScale = (int) Math.ceil(options.outHeight * 1.0 / maxSize);
            inSampleSize = Math.max(widthScale, heightScale);
        }
        options.inJustDecodeBounds = false;
        options.inSampleSize = inSampleSize;
        Bitmap bitmap = BitmapFactory.decodeFile(path, options);
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        int newW = w;
        int newH = h;
        if (w > maxSize || h > maxSize) {
            if (w > h) {
                newW = maxSize;
                newH = (int) (newW * h * 1.0 / w);
            } else {
                newH = maxSize;
                newW = (int) (newH * w * 1.0 / h);
            }
        }
        Bitmap newBitmap = Bitmap.createScaledBitmap(bitmap, newW, newH, false);
        //recycle(bitmap);

        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(newPath);
            newBitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        recycle(newBitmap);
        recycle(bitmap);
        return newPath;
    }

    public static void recycle(Bitmap bitmap) {
        // 先判断是否已经回收
        if (bitmap != null && !bitmap.isRecycled()) {
            // 回收并且置为null
            bitmap.recycle();
        }
        System.gc();
    }

    public static File saveBitmap(Context context, Bitmap bitmap) {
        File file = new File(Environment.getExternalStorageDirectory().toString() + "/" + context.getPackageName() + "/file");
        if (!file.exists()) {
            file.mkdirs();
        }
        file = new File(file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".png");
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
            return file;
        } catch (FileNotFoundException e) {
            return null;
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                return null;
            }
        }
    }

}
