package ihainan.me.androiduidesign.activities;

import android.content.Context;
import android.content.Intent;
import android.hardware.camera2.CameraManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.Result;

import ihainan.me.androiduidesign.R;
import ihainan.me.androiduidesign.utils.CommonUtils;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class QRCodeScannerActivity extends AppCompatActivity {
    public final static String TAG = QRCodeScannerActivity.class.getSimpleName();

    // UI Elements
    private ZXingScannerView mQRCodeScanner;
    private ImageView mBackButton, mFlashLightOnOrOffBtn, mCameraBtn;

    private boolean mRearCamera = true; // true 表示后置，false 表示前置
    private boolean mFlashLightOn = false;  // true 表示打开，false 表示关闭

    private CameraManager mCameraManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode_scanner);

        // Camera Manager 服务
        mCameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);

        // UI Elements
        mQRCodeScanner = (ZXingScannerView) findViewById(R.id.qr_code_scanner);
        mBackButton = (ImageView) findViewById(R.id.back_btn);
        mFlashLightOnOrOffBtn = (ImageView) findViewById(R.id.camera_flash_light);
        mCameraBtn = (ImageView) findViewById(R.id.camera_type);

        // 设置 Toolbar 按钮事件
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mCameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mQRCodeScanner.stopCamera();
                mRearCamera = !mRearCamera;
                mCameraBtn.setImageDrawable(mRearCamera ?
                        ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_camera_front_white_24dp)
                        : ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_camera_rear_white_24dp));
                mQRCodeScanner.startCamera(Integer.parseInt(mRearCamera ? CommonUtils.getRearFacingCameraId(mCameraManager) : CommonUtils.getFrontFacingCameraId(mCameraManager)));
            }
        });

        mFlashLightOnOrOffBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFlashLightOn = !mFlashLightOn;
                mFlashLightOnOrOffBtn.setImageDrawable(mFlashLightOn ?
                        ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_flash_off_white_24dp)
                        : ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_flash_on_white_24dp));
                mQRCodeScanner.setFlash(mFlashLightOn);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        mQRCodeScanner.setResultHandler(new ZXingScannerView.ResultHandler() {
            @Override
            public void handleResult(Result result) {
                Intent intent = new Intent(QRCodeScannerActivity.this, ItemDetailActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra(ItemDetailActivity.ITEM_ID_, Integer.parseInt(result.getText()));
                startActivity(intent);
                finish();
            }
        });
        mQRCodeScanner.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        mQRCodeScanner.stopCamera();           // Stop camera on pause
    }
}
