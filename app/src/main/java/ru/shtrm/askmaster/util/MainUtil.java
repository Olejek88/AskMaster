package ru.shtrm.askmaster.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Build;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.WindowManager;

import java.io.File;
import java.lang.reflect.InvocationTargetException;

import ru.shtrm.askmaster.R;

public class MainUtil {

    public static int setToolBarColor(Context context, Activity myActivityReference) {
        if (PreferenceManager.getDefaultSharedPreferences(context).
                getBoolean("navigation_bar_tint", true)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                myActivityReference.getWindow().setNavigationBarColor(
                        ContextCompat.getColor(context, R.color.colorPrimaryDark));
            }
        }
        return 0;
    }

    public static Bitmap getBitmapByPath(String path, String filename) {
        File imageFull = new File(path + filename);
        Bitmap bmp = BitmapFactory.decodeFile(imageFull.getAbsolutePath());
        if (bmp != null) {
            return bmp;
        } else return null;
    }

    public static String getPicturesDirectory(Context context) {
        return Environment.getExternalStorageDirectory()
                + "/Android/data/"
                + context.getPackageName()
                + "/Files"
                + File.separator;
    }

}
