package com.ashwinrao.packup.view.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.view.TextureView;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.AspectRatio;
import androidx.camera.core.CameraX;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureConfig;
import androidx.camera.core.Preview;
import androidx.camera.core.PreviewConfig;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.ashwinrao.packup.R;
import com.ashwinrao.packup.databinding.ActivityCameraBinding;
import com.ashwinrao.packup.util.HideShowNotch;
import com.ashwinrao.packup.util.SettingsUtil;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

public class CameraActivity extends AppCompatActivity {

    private TextureView textureView;
    private CardView shutterButton;
    private CoordinatorLayout snackbarContainer;
    private ArrayList<String> paths = new ArrayList<>();

    private static final int CAMERA_PERMISSION_REQUEST_CODE = 5;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        HideShowNotch.applyThemeIfAvailable(this);
        super.onCreate(savedInstanceState);
        final ActivityCameraBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_camera);
        textureView = binding.preview;
        snackbarContainer = binding.snackbarContainer;
        shutterButton = binding.shutter.findViewById(R.id.button);
        checkPermissionsBeforeBindingTextureView();
        fadeCameraInstructions(binding.cameraInstructions);
    }

    private void fadeCameraInstructions(@NonNull final TextView instructions) {
        final AlphaAnimation anim = new AlphaAnimation(1.0f, 0.0f);
        anim.setDuration(500);
        anim.setFillAfter(true);
        anim.setStartOffset(10000);
        instructions.startAnimation(anim);
    }

    private void checkPermissionsBeforeBindingTextureView() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            // Permission has already been granted, no need to ask
            textureView.post(this::startCamera);
        } else {
            // Permission has not been granted, ask for permission
            requestPermissions(new String[]{Manifest.permission.CAMERA},
                    CAMERA_PERMISSION_REQUEST_CODE);
        }
    }

    private void finishUpActivity() {
        if (paths.size() > 0) {
            final Intent intent = new Intent();
            intent.putStringArrayListExtra("paths", paths);
            setResult(Activity.RESULT_OK, intent);
        }
        finish();
    }

    private void startCamera() {

        final DisplayMetrics metrics = new DisplayMetrics();
        textureView.getDisplay().getRealMetrics(metrics);
//        final Size screenSize = new Size(metrics.widthPixels, metrics.heightPixels);
        final AspectRatio screenAspectRatio = AspectRatio.RATIO_4_3;

        final PreviewConfig previewConfig = new PreviewConfig.Builder()
                .setLensFacing(CameraX.LensFacing.BACK)
                .setTargetAspectRatio(screenAspectRatio)
//                .setTargetResolution(screenSize)
                .build();

        final Preview preview = new Preview(previewConfig);

        preview.setOnPreviewOutputUpdateListener(output -> {
            final ViewGroup parent = (ViewGroup) textureView.getParent();
            parent.removeView(textureView);
            parent.addView(textureView, 0);

            textureView.setSurfaceTexture(output.getSurfaceTexture());
        });

        final ImageCaptureConfig imageCaptureConfig = new ImageCaptureConfig.Builder()
                .setTargetAspectRatio(screenAspectRatio)
                .setCaptureMode(ImageCapture.CaptureMode.MIN_LATENCY)
//                .setTargetResolution(screenSize)
                .build();

        final ImageCapture imageCapture = new ImageCapture(imageCaptureConfig);
        shutterButton.setOnClickListener(v -> {

            final File file = new File(getExternalMediaDirs()[0], System.currentTimeMillis() + ".jpg");
            imageCapture.takePicture(file, command -> {

                paths.add(file.getAbsolutePath());

                // Provide vibration feedback
                if (SettingsUtil.getShutterVibrationSetting(this)) {
                    if (android.os.Build.VERSION.SDK_INT >= 26) {
                        ((Vibrator) Objects.requireNonNull(getSystemService(VIBRATOR_SERVICE))).vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE));
                    } else {
                        ((Vibrator) Objects.requireNonNull(getSystemService(VIBRATOR_SERVICE))).vibrate(50);
                    }
                }

                // Notify user of saved image
                final Snackbar snackbar = Snackbar.make(snackbarContainer, "Saved", Snackbar.LENGTH_SHORT)
                        .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_FADE)
                        .setBackgroundTint(ContextCompat.getColor(CameraActivity.this,
                                R.color.success_green))
                        .setTextColor(ContextCompat.getColor(CameraActivity.this, R.color.success_green_text));
                snackbar.getView().getLayoutParams().width = ViewGroup.LayoutParams.WRAP_CONTENT;
                snackbar.show();

            }, new ImageCapture.OnImageSavedListener() {
                @Override
                public void onImageSaved(@NonNull File file) {
                }

                @Override
                public void onError(@NonNull ImageCapture.ImageCaptureError imageCaptureError, @NonNull String message, @Nullable Throwable cause) {
                    // Notify user of failed save
                    final Snackbar snackbar = Snackbar.make(snackbarContainer, "Error", Snackbar.LENGTH_SHORT)
                            .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_FADE)
                            .setBackgroundTint(ContextCompat.getColor(CameraActivity.this,
                                    R.color.error_red))
                            .setTextColor(ContextCompat.getColor(CameraActivity.this, R.color.error_red_text));
                    snackbar.getView().getLayoutParams().width = ViewGroup.LayoutParams.WRAP_CONTENT;
                    snackbar.show();
                }
            });
        });

        CameraX.bindToLifecycle(this, preview, imageCapture);
    }

    private static int getCameraPermissionRequestCode() {
        return CAMERA_PERMISSION_REQUEST_CODE;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == getCameraPermissionRequestCode()) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission was granted
                textureView.post(this::startCamera);
            } else {
                // Permission was denied
                // Notify the user the camera functionality will not be available
                Toast.makeText(this, "You will not be able to take photos unless you grant permission to use the camera.", Toast.LENGTH_SHORT).show();
                finish();
            }

        }
    }

    @Override
    public void onBackPressed() {
        finishUpActivity();
        super.onBackPressed();
    }
}
