package com.example.administrador.gestionusuarios;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.annotation.Size;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

public class CameraActivity extends AppCompatActivity {
    TextureView textureview;
    CameraDevice camDevice;
    String CamId;
    android.util.Size imgDim;
    CaptureRequest.Builder captureRequestBuilder;
    CameraCaptureSession CamSession;
    Handler backHandler;
    FloatingActionButton btn_tomarf;
    public static final int CAMERA_REQUEST_CODE = 1999;
    HandlerThread backThread;
    File galleryFolder;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);

        textureview = (TextureView) findViewById(R.id.texture_view);
        textureview.setSurfaceTextureListener(surfaceTextureListener);
        btn_tomarf=(FloatingActionButton) findViewById(R.id.fab_take_photo);

        startBackgroundThread();

        btn_tomarf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FileOutputStream outputPhoto = null;
                try {
                    outputPhoto = new FileOutputStream(createImageFile(galleryFolder));
                    textureview.getBitmap()
                            .compress(Bitmap.CompressFormat.PNG, 100, outputPhoto);

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (outputPhoto != null) {
                            outputPhoto.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }

    TextureView.SurfaceTextureListener surfaceTextureListener = new TextureView.SurfaceTextureListener (){

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i1) {
            try {
                openCamera();
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }


        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
            return false;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surface) {

        }
    };

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void openCamera() throws CameraAccessException {
        CameraManager cm = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        CamId = cm.getCameraIdList()[0];
        CameraCharacteristics cc = cm.getCameraCharacteristics(CamId);
        StreamConfigurationMap map = cc.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
        imgDim = map.getOutputSizes(SurfaceTexture.class)[0];

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        cm.openCamera(CamId, stateCallback, null);

    }

    CameraDevice.StateCallback stateCallback = new CameraDevice.StateCallback(){

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onOpened(@NonNull CameraDevice camera) {
            camDevice = camera;
            try {
                startPreview();
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onDisconnected(@NonNull CameraDevice camera) {
            camDevice.close();
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onError(@NonNull CameraDevice camera, int error) {
            camDevice.close();
            camDevice = null;
        }
    };
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void startPreview() throws CameraAccessException {
        SurfaceTexture texture = textureview.getSurfaceTexture();
       texture.setDefaultBufferSize(imgDim.getWidth(),imgDim.getHeight());


        Surface surface = new Surface(texture);

        captureRequestBuilder = camDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);

        captureRequestBuilder.addTarget(surface);

        camDevice.createCaptureSession(Arrays.asList(surface),new CameraCaptureSession.StateCallback(){

            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onConfigured(@NonNull  CameraCaptureSession session) {
                if(camDevice==null){return;}
                CamSession = session;
                try {
                    updatePreview();
                } catch (CameraAccessException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onConfigureFailed(CameraCaptureSession session) {

            }
        },null);


    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private  void updatePreview() throws CameraAccessException {
        if(camDevice==null){return;}
        captureRequestBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
        CamSession.setRepeatingRequest(captureRequestBuilder.build(),null,backHandler);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onResume() {
        super.onResume();
        startBackgroundThread();
        if(textureview.isAvailable()){try{openCamera();}catch(CameraAccessException e){e.printStackTrace();}
        }
        else{
            textureview.setSurfaceTextureListener(surfaceTextureListener);
        }
    }

    private void startBackgroundThread() {
        backThread = new HandlerThread("camera Background");
        backThread.start();
        backHandler = new Handler(backThread.getLooper());
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    protected void onPause() {
        super.onPause();
        try {
            stopBackgroundThread();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void stopBackgroundThread() throws InterruptedException {
        backThread.quitSafely();
        backThread.join();
        backHandler = null;
        backThread = null;

    }
    private void createImageGallery() {
        File storageDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        galleryFolder = new File(storageDirectory, getResources().getString(R.string.app_name));
        if (!galleryFolder.exists()) {
            boolean wasCreated = galleryFolder.mkdirs();
            if (!wasCreated) {
                //Log.e("CapturedImages", "Failed to create directory");
            }
        }
    }

    private File createImageFile(File galleryFolder) throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "image_" + timeStamp + "_";
        return File.createTempFile(imageFileName, ".jpg", Environment.getExternalStorageDirectory());
    }

}
