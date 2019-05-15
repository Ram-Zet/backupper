package ru.ramzet.backupper.service;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;
import ru.ramzet.backupper.model.Status;

import static ru.ramzet.backupper.constansts.Constants.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class CopyService {
    public Status copyDir(File sourcePath, File destinationPath) {
        if (!checkDirIsExist(sourcePath)) return Status.NO_SUCH_SOURCE_PATH;

        if (!checkDirIsExist(destinationPath)) return Status.NO_SUCH_DESTINATION_PATH;

        try {
            FileUtils.copyDirectoryToDirectory(sourcePath, destinationPath);
        } catch (IOException e) {
            return Status.ERROR_IN_COPY_TIME;
        }
        return Status.OK;
    }

    public Status copyFile(File sourcePath, File destinationPath) {
        if (!checkFileIsExist(sourcePath)) return Status.NO_SUCH_SOURCE_FILE;

        if (!checkDirIsExist(destinationPath)) return Status.NO_SUCH_DESTINATION_PATH;

        try {
            FileUtils.copyFileToDirectory(sourcePath, destinationPath);
        } catch (IOException e) {
            return Status.ERROR_IN_COPY_TIME;
        }
        return Status.OK;
    }

    public void cleanDestination(String destination, int numOfSnapshots) {
        File dest = new File(destination);
        String[] list = dest.list();
        List<LocalDateTime> dates = new ArrayList<>();
        for (String name : list) {
            try {
                LocalDateTime dateTime = LocalDateTime.parse(name, FORMAT);
                dates.add(dateTime);
            } catch (Exception e) {
            }
        }
        if (dates.size() > numOfSnapshots) {
            Collections.sort(dates);
            Path path = Paths.get(destination, dates.get(0).format(FORMAT));
            File file = new File(path.toAbsolutePath().toString());
            try {
                FileUtils.forceDelete(file);

            } catch (IOException e) {

            }
        }

    }

    private boolean checkDirIsExist(File dir) {
        return dir.exists() && dir.isDirectory();
    }

    private boolean checkFileIsExist(File file) {
        return file.exists() && file.isFile();
    }
}
