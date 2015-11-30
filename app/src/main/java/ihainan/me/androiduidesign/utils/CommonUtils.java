package ihainan.me.androiduidesign.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.util.Log;
import android.util.TypedValue;

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
}
