package ihainan.me.androiduidesign.utils;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.util.TypedValue;

import java.io.ByteArrayOutputStream;

import ihainan.me.androiduidesign.R;

/**
 * 通用工具
 */
public class CommonUtils {
    public final static String TAG = CommonUtils.class.getSimpleName();

    /**
     * 获取前置摄像头的 ID
     *
     * @param cManager Camera Manager 服务
     * @return 前置摄像头 ID，获取失败返回 Null
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static String getFrontFacingCameraId(CameraManager cManager) {
        try {
            for (final String cameraId : cManager.getCameraIdList()) {
                CameraCharacteristics characteristics = cManager.getCameraCharacteristics(cameraId);
                int cOrientation = characteristics.get(CameraCharacteristics.LENS_FACING);
                if (cOrientation == CameraCharacteristics.LENS_FACING_FRONT) return cameraId;
            }
        } catch (CameraAccessException e) {
            Log.e(TAG, "获取后置摄像头失败");
            return null;
        }
        return null;
    }

    /**
     * 获取后置摄像头的 ID
     *
     * @param cManager Camera Manager 服务
     * @return 后置摄像头 ID，获取失败返回 Null
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static String getRearFacingCameraId(CameraManager cManager) {
        try {
            for (final String cameraId : cManager.getCameraIdList()) {
                CameraCharacteristics characteristics = cManager.getCameraCharacteristics(cameraId);
                int cOrientation = characteristics.get(CameraCharacteristics.LENS_FACING);
                if (cOrientation == CameraCharacteristics.LENS_FACING_BACK) return cameraId;
            }
        } catch (CameraAccessException e) {
            Log.e(TAG, "获取后置摄像头失败");
            return null;
        }
        return null;
    }

    /**
     * dp 转换成 Pixel
     *
     * @param context 程序上下文
     * @param dps     dp 为单位的长度值
     * @return pixel 为单位的长度值
     */
    public static int dpToPixel(Context context, float dps) {
        Resources r = context.getResources();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 14, r.getDisplayMetrics());
        return (int) px;
    }

    /**
     * 根据 Resource ID 获取对应的颜色
     *
     * @param context    上下文
     * @param resourceID Resource ID
     * @return 颜色整数值
     */
    public static int getColorFromResourceID(Context context, int resourceID) {
        return ContextCompat.getColor(context, resourceID);
    }

    /**
     * 根据资源的 URI 获取 Drawable 实例
     *
     * @param context
     * @param uri     资源 URI
     * @return Drawable 实例
     */
    public static Drawable getDrawableByResourceName(Context context, String uri) {
        int imageResource = context.getResources().getIdentifier(uri, null, context.getPackageName());
        Drawable res = ContextCompat.getDrawable(context, imageResource);
        return res;
    }


    /**
     * 获取 Toolbar 的 Height
     *
     * @param context 上下文
     * @return Toolbar Height
     */
    public static float getToolBarHeight(Context context) {
        float actionBarHeight = 0.0f;
        TypedValue tv = new TypedValue();
        if (context.getTheme().resolveAttribute(R.attr.actionBarSize, tv, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, context.getResources().getDisplayMetrics());
        }
        return actionBarHeight;
    }

    /**
     * 位图转换为 Byte 数组
     *
     * @param bitmap 需要转换的位图
     * @return Byte 数组
     */
    public static byte[] getBytesFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        return stream.toByteArray();
    }
}
