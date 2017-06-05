package ir.university.toosi.tms.util;

import ir.university.toosi.tms.model.entity.zone.PDP;
import ir.university.toosi.tms.model.service.zone.PDPServiceImpl;
import ir.university.toosi.wtms.web.util.CalendarUtil;

import java.io.File;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Farzad on 4/20/2014.
 */
public class FileUploaderUtil implements Runnable {
    private String pdpDirectory;
    private String gateDirectory;
    private String pdpIP;
    private PDP pdp;
    private PDPServiceImpl pdpService;

    public FileUploaderUtil(PDP pdp, PDPServiceImpl pdpService) {
        this.pdpDirectory = Configuration.getProperty("pdp.dir") + pdp.getId() + File.separator;
        this.gateDirectory = Configuration.getProperty("gate.dir") + pdp.getGateway().getId() + File.separator;
        this.pdpService = pdpService;
        this.pdpIP = pdp.getIp();
        this.pdp = pdp;
    }

    @Override
    public void run() {
        boolean flag = true;
        pdp.setUpdateDate(CalendarUtil.getDate(new Date(), new Locale("fa")));
        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get(pdpDirectory), "{*.dat,*.sch,*.png,*.txt,*.bmp,*.fpt}")) {
            for (Path path : directoryStream) {
                System.out.println(path.toAbsolutePath().toString());
                TFTPUtility.put(pdpIP, path.toAbsolutePath().toString(), path.toAbsolutePath().getFileName().toString());
            }

        } catch (Exception e) {
            flag = false;
            e.printStackTrace();
        }
        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get(gateDirectory), "{*.dat,*.sch,*.png,*.txt,*.bmp}")) {
            for (Path path : directoryStream) {
                System.out.println(path.toAbsolutePath().toString());
                TFTPUtility.put(pdpIP, path.toAbsolutePath().toString(), path.toAbsolutePath().getFileName().toString());
            }
        } catch (Exception e) {
            flag = false;
            e.printStackTrace();
        }
        pdp.setSuccess(flag);

        pdpService.editPDP(pdp);


    }
}
