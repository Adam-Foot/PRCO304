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
import com.google.ar.core.Session;
import com.google.ar.core.TrackingState;
import com.google.ar.core.exceptions.UnavailableApkTooOldException;
import com.google.ar.core.exceptions.UnavailableArcoreNotInstalledException;
import com.google.ar.core.exceptions.UnavailableSdkTooOldException;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.ArSceneView;
import com.google.ar.sceneform.FrameTime;
import com.google.ar.sceneform.animation.ModelAnimator;
import com.google.ar.sceneform.rendering.AnimationData;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.Renderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

public class ArCamera extends AppCompatActivity {

    private static final String TAG = ArCamera.class.getSimpleName();
    Session mSession;
    private ArFragment arFragment;
    private ArSceneView arSceneView;
    private ModelRenderable modelRenderable;
    private boolean modelAdded = false;
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

    }

    private boolean setupAugmentedImageDatabase(Config config) {
        AugmentedImageDatabase augmentedImageDatabase;

        Bitmap augmentedImageBitmap = loadAugmentedImage();
        if (augmentedImageBitmap == null) {
            return false;
        }

        augmentedImageDatabase = new AugmentedImageDatabase(mSession);
        augmentedImageDatabase.addImage("statue", augmentedImageBitmap);

        config.setAugmentedImageDatabase(augmentedImageDatabase);
        config.setFocusMode(Config.FocusMode.AUTO);
        return true;
    }

    private Bitmap loadAugmentedImage() {
        try (InputStream is = getAssets().open("statue.jpg")) {
            return BitmapFactory.decodeStream(is);
        } catch (IOException e) {
            Log.e(TAG, "IO Exception!", e);
        }
        return null;
    }

    private void onUpdateFrame(FrameTime frameTime) {
        Frame frame = arFragment.getArSceneView().getArFrame();

        Collection<AugmentedImage> augmentedImages = frame.getUpdatedTrackables(AugmentedImage.class);

        for (AugmentedImage augmentedImage : augmentedImages) {
            if (augmentedImage.getTrackingState() == TrackingState.TRACKING) {
                if (augmentedImage.getName().contains("statue") && !modelAdded) {
                    renderObject(arFragment, augmentedImage.createAnchor(augmentedImage.getCenterPose()), R.raw.car);
                    modelAdded = true;
                }
            }
        }
    }

    private void renderObject(ArFragment fragment, Anchor anchor, int model) {
        ModelRenderable.builder()
                .setSource(this, model)
                .build()
                .thenAccept(renderable -> addNodeToScene(fragment, anchor, renderable))
                .exceptionally((throwable -> {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage(throwable.getMessage())
                            .setTitle("Error!");
                    AlertDialog dialog = builder.create();
                    dialog.show();
                    return null;
                }));

        // Animation Data for Andy OBJ. To be tested when Google update their Sceneform Plugin.
//        AnimationData danceData = modelRenderable.getAnimationData("andy_dance");
//        danceData.getName();
//        ModelAnimator andyAnimator = new ModelAnimator(danceData, modelRenderable);
//        andyAnimator.start();
//        andyAnimator.setRepeatCount(50);
    }

    private void addNodeToScene(ArFragment fragment, Anchor anchor, Renderable renderable){
        AnchorNode anchorNode = new AnchorNode(anchor);
        TransformableNode node = new TransformableNode(fragment.getTransformationSystem());
        node.setRenderable(renderable);
        node.setParent(anchorNode);
        fragment.getArSceneView().getScene().addChild(anchorNode);
        node.select();
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
