package com.example.prco.ui.landmarkselfie;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.prco.CameraPermissionHelper;
import com.example.prco.R;
import com.google.ar.core.ArCoreApk;
import com.google.ar.core.Session;
import com.google.ar.core.exceptions.UnavailableApkTooOldException;
import com.google.ar.core.exceptions.UnavailableArcoreNotInstalledException;
import com.google.ar.core.exceptions.UnavailableDeviceNotCompatibleException;
import com.google.ar.core.exceptions.UnavailableSdkTooOldException;
import com.google.ar.core.exceptions.UnavailableUserDeclinedInstallationException;
import com.google.ar.sceneform.rendering.ModelRenderable;

import static com.google.ar.core.ArCoreApk.InstallStatus.INSTALLED;
import static com.google.ar.core.ArCoreApk.InstallStatus.INSTALL_REQUESTED;


public class LandmarkSelfie extends AppCompatActivity {

    private boolean mUserRequestedInstall = true;
    private Session mSession;

    private ModelRenderable andyRenderable;
    private static final String TAG = "LandmarkSelfie";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landmark_selfie);


        ModelRenderable.builder()
                .setSource(this, R.raw.andy)
                .build()
                .thenAccept(renderable -> andyRenderable = renderable)
                .exceptionally(
                        throwable -> {
                            Log.e(TAG, "Unable to load Renderable.", throwable);
                            return null;
                        });


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] results) {
        if (!CameraPermissionHelper.hasCameraPermission(this)) {
            Toast.makeText(this, "Camera permission is needed to run this application", Toast.LENGTH_LONG)
                    .show();
            if (!CameraPermissionHelper.shouldShowRequestPermissionRationale(this)) {
                // Permission denied with checking "Do not ask again".
                CameraPermissionHelper.launchPermissionSettings(this);
            }
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!CameraPermissionHelper.hasCameraPermission(this)) {
            CameraPermissionHelper.requestCameraPermission(this);
            return;
        }


        // Make sure Google Play Services for AR is installed and up to date.
        try {
            if (mSession == null) {

                switch (ArCoreApk.getInstance().requestInstall(this, mUserRequestedInstall)) {
                    case INSTALLED:
                        // Success, create the AR session.
                        mSession = new Session(this);
                        break;
                    case INSTALL_REQUESTED:
                        // Ensures next invocation of requestInstall() will either return
                        // INSTALLED or throw an exception.
                        mUserRequestedInstall = false;
                }
            }
        } catch (UnavailableUserDeclinedInstallationException e) {
            // Display an appropriate message to the user and return gracefully.
            Toast.makeText(this, "To be able to use this feature you need to have the latest version of Google Play Services. Please go back and try again.", Toast.LENGTH_LONG)
                    .show();

        } catch (UnavailableArcoreNotInstalledException e) {
            Toast.makeText(this, "To be able to use this feature you must have AR Core installed. You can find it on the Google Play Store.", Toast.LENGTH_LONG)
                    .show();
        } catch (UnavailableApkTooOldException e) {
            Toast.makeText(this, "To be able to use this feature the app needs to be of a specific version. Please update the app and try again.", Toast.LENGTH_LONG)
                    .show();
        } catch (UnavailableSdkTooOldException e) {
            Toast.makeText(this, "To be able to use this feature your phone needs to be running a specific version or Android. Please check your phone for updates and try again.", Toast.LENGTH_LONG)
                    .show();
        } catch (UnavailableDeviceNotCompatibleException e) {
            Toast.makeText(this, "Your phone doesn't seem to support AR Core at this moment. Unfortunately you will not be able to use this feature.", Toast.LENGTH_LONG)
                    .show();
        }
    }
}

