package com.example.prco.ui.landmarkselfie;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.prco.CameraPermissionHelper;
import com.example.prco.R;

public class LandmarkSelfie extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landmark_selfie);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!CameraPermissionHelper.hasCameraPermission(this)) {
            CameraPermissionHelper.requestCameraPermission(this);
            return;
        }
    }
}
