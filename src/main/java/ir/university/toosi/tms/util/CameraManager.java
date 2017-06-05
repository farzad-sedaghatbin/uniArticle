package ir.university.toosi.tms.util;

import ir.university.toosi.tms.model.entity.zone.Camera;
import ir.university.toosi.tms.model.service.PhotoServiceImpl;
import ir.university.toosi.tms.model.service.zone.CameraServiceImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by farzad on 8/20/2014.
 */
public class CameraManager implements Runnable {
    private PhotoServiceImpl photoService;
    private CameraServiceImpl cameraService;

    public CameraManager(PhotoServiceImpl photoService, CameraServiceImpl cameraService) {
        this.photoService = photoService;
        this.cameraService = cameraService;
    }

    @Override
    public void run() {
        while (true) {
            for (Camera camera : (List<Camera>) cameraService.getAllCamera()) {
                List<byte[]> photos = new ArrayList<>();
                for (int i = 0; i < camera.getFrames(); i++) {
                    photos.add(photoService.connect(camera.getIp()));
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Initializer.pics.put(camera.getId(), photos);
            }
        }
    }
}
