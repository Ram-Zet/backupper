package ru.ramzet.backupper.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.ramzet.backupper.model.ConsoleHelper;
import ru.ramzet.backupper.model.CopySettings;
import ru.ramzet.backupper.model.Entry;
import ru.ramzet.backupper.model.Status;
import ru.ramzet.backupper.service.CopyService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static ru.ramzet.backupper.constansts.Constants.*;

@Component
@AllArgsConstructor
public class CopyController {

    private final CopyService service;

    public Status makeCopyAll() {
        Status status = Status.OK;
        for (Entry entry: CopySettings.getSettings().getEntryList()){
            Status tempStatus = makeCopyByOneEntry(entry);
            if (tempStatus != Status.OK) status = tempStatus;
        }
        return status;
    }

    public Status makeCopyByOneEntry(Entry entry) {
        String date = LocalDateTime.now().format(FORMAT);
        Path destPath = Paths.get(entry.getDestination(), date);
        try {
            Files.createDirectories(destPath);
        } catch (IOException e) {
            return Status.ERROR_CREATING_DESTINATION_FOLDER;
        }
        File destFile = new File(destPath.toAbsolutePath().toString());
        Status status = Status.OK;
        for (String source : entry.getSources()) {
            File src = new File(source);
            if (!src.exists()) return Status.NO_SUCH_SOURCE;
            ConsoleHelper.writeMessage("Копирование " + source);
            Status tempStatus = src.isDirectory()?service.copyDir(src, destFile):service.copyFile(src, destFile);
            if (tempStatus != Status.OK) {
                ConsoleHelper.writeMessage("Ошибка при копировании " + source);
            }
        }
        service.cleanDestination(entry.getDestination(), entry.getCopies());
        return status;
    }

    public boolean removeEntry(String entryName) {
        boolean result = false;
        CopySettings settings = CopySettings.getSettings();
        for (int i = 0; i < settings.getEntryList().size(); i++) {
            if (settings.getEntryList().get(i).getName().equalsIgnoreCase(entryName)) {
                settings.getEntryList().remove(i);
                result = true;
            }
        }
        CopySettings.saveSettings();
        return result;
    }

    public void editEntry(Entry entry) {
        CopySettings.getSettings().getEntryList().stream()
                .filter(e -> e.getName().equalsIgnoreCase(entry.getName()))
                .forEach(e -> {
                    e.setCopies(entry.getCopies());
                    e.setDestination(entry.getDestination());
                    e.setSources(entry.getSources());
                });
        CopySettings.saveSettings();
    }

    public void addEntry(Entry entry) {
        CopySettings.getSettings().getEntryList().add(entry);
        CopySettings.saveSettings();
    }
}
