package com.nepalese.virgolib.widget.camera2;

/**
 * Created by Administrator on 2022/3/17.
 * Usage:
 */

public interface Camera2Listener {

    void onCameraOpened(String cameraId, boolean isMirror);

    void onPreview(byte[] data);

    void onCameraClosed();

    void onCameraError(String msg);
}
