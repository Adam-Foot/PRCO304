package com.example.prco.ui.arcamera;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.prco.R;
import com.google.ar.core.Anchor;
import com.google.ar.core.AugmentedImage;
import com.google.ar.core.AugmentedImageDatabase;
import com.google.ar.core.Config;
import com.google.ar.core.Frame;
import com.google.ar.core.Pose;
import com.google.ar.core.Session;
import com.google.ar.core.TrackingState;
import com.google.ar.core.exceptions.UnavailableApkTooOldException;
import com.google.ar.core.exceptions.UnavailableArcoreNotInstalledException;
import com.google.ar.core.exceptions.UnavailableSdkTooOldException;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.ArSceneView;
import com.google.ar.sceneform.FrameTime;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.math.Quaternion;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.AnimationData;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.Renderable;
import com.google.ar.sceneform.rendering.ViewRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;
import com.google.common.collect.Sets;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

public class ArCamera extends AppCompatActivity {

    private static final String TAG = ArCamera.class.getSimpleName();
    Session mSession;
    private ArFragment arFragment;
    private ArSceneView arSceneView;
    private ModelRenderable modelRenderable;
    private boolean model1Added = false;
    private boolean model2Added = false;
    private boolean model3Added = false;
    private boolean model4Added = false;
    private boolean sessionConfigured = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arcamera);
        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.ar_fragment);

        arFragment.getPlaneDiscoveryController().hide();
        arFragment.getPlaneDiscoveryController().setInstructionView(null);
        arFragment.getArSceneView().getScene().addOnUpdateListener(this::onUpdateFrame);

        arSceneView = arFragment.getArSceneView();
        arSceneView.getPlaneRenderer().setVisible(false);

    }

    private boolean setupAugmentedImageDatabase(Config config) {
        AugmentedImageDatabase augmentedImageDatabase = new AugmentedImageDatabase(mSession);
        try(InputStream inputStream = getAssets().open("myimages.imgdb")) {
            augmentedImageDatabase = AugmentedImageDatabase.deserialize(mSession, inputStream);
        } catch (IOException e) {
            Log.e(TAG, "IO exception loading augmented image database.", e);
        }

        config.setAugmentedImageDatabase(augmentedImageDatabase);
        config.setFocusMode(Config.FocusMode.AUTO);
        return true;
    }

    private void onUpdateFrame(FrameTime frameTime) {
        Frame frame = arFragment.getArSceneView().getArFrame();

        Collection<AugmentedImage> augmentedImages = frame.getUpdatedTrackables(AugmentedImage.class);

        for (AugmentedImage augmentedImage : augmentedImages) {
            if (augmentedImage.getTrackingState() == TrackingState.TRACKING && augmentedImage.getName().contains("logoGrey") && !model1Added) {
                    renderObject(arFragment, augmentedImage.createAnchor(augmentedImage.getCenterPose()), R.layout.layout_ar_grey);
                    model1Added = true;
            } else if (augmentedImage.getTrackingState() == TrackingState.TRACKING && augmentedImage.getName().contains("logoGreen") && !model2Added) {
                renderObject(arFragment, augmentedImage.createAnchor(augmentedImage.getCenterPose()), R.layout.layout_ar_green);
                model2Added = true;
            }
        }
    }

    private void renderObject(ArFragment fragment, Anchor anchor, int model) {
        ViewRenderable.builder().setView(this, model).build().thenAccept(renderable -> addNodeToScene(fragment, anchor, renderable));
    }

//    private void renderObject(ArFragment fragment, Anchor anchor, int model) {
//        ModelRenderable.builder()
//                .setSource(this, model)
//                .build()
//                .thenAccept(renderable -> addNodeToScene(fragment, anchor, renderable))
//                .exceptionally((throwable -> {
//                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
//                    builder.setMessage(throwable.getMessage())
//                            .setTitle("Error!");
//                    AlertDialog dialog = builder.create();
//                    dialog.show();
//                    return null;
//                }));
//    }

    private void addNodeToScene(ArFragment fragment, Anchor anchor, Renderable renderable){
        AnchorNode anchorNode = new AnchorNode(anchor);
        Node node = new Node();
        node.setRenderable(renderable);
        node.setLocalRotation(Quaternion.axisAngle(new Vector3(-1f, 0, 0), 90f));
        node.setLocalScale(new Vector3(0.25f, 0.25f, 0.25f));
        node.setParent(anchorNode);

        fragment.getArSceneView().getScene().addChild(anchorNode);

    }

    @Override
    public void onPause() {
        super.onPause();
        if (mSession != null) {

            arSceneView.pause();
            mSession.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mSession == null) {
            String message = null;
            Exception exception = null;
            try {
                mSession = new Session(this);
            } catch (UnavailableArcoreNotInstalledException
                    e) {
                message = "Please install ARCore";
                exception = e;
            } catch (UnavailableApkTooOldException e) {
                message = "Please update ARCore";
                exception = e;
            } catch (UnavailableSdkTooOldException e) {
                message = "Please update android";
                exception = e;
            } catch (Exception e) {
                message = "AR is not supported";
                exception = e;
            }

            if (message != null) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Exception creating session", exception);
                return;
            }
            sessionConfigured = true;

        }
        if (sessionConfigured) {
            configureSession();
            sessionConfigured = false;

            arSceneView.setupSession(mSession);
        }

    }

    private void configureSession() {
        Config config = new Config(mSession);
        if (!setupAugmentedImageDatabase(config)) {
            Toast.makeText(this, "Unable to setup augmented", Toast.LENGTH_SHORT).show();
        }
        config.setUpdateMode(Config.UpdateMode.LATEST_CAMERA_IMAGE);
        mSession.configure(config);
    }

}
