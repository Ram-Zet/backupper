package ru.ramzet.backupper.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Data;
import org.apache.commons.io.FileUtils;

import static ru.ramzet.backupper.constansts.Constants.*;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

@Data
public class CopySettings {
    private static CopySettings copySettings = null;
    private static Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private List<Entry> entryList = new ArrayList<>();


    private CopySettings(){}

    public  static CopySettings getSettings(){
        if (copySettings != null) return copySettings;

        copySettings = new CopySettings();

        File settingsFile = new File(SETTINGS_FILE);
        if (!settingsFile.exists()) {
            saveSettings();
            return copySettings;
        }

        try {
            String gSonString = FileUtils.readFileToString(settingsFile, Charset.defaultCharset());
            copySettings = gson.fromJson(gSonString,CopySettings.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return copySettings;
    }

    public static CopySettings saveSettings(){
        if (copySettings == null)
            copySettings = getSettings();
        File settingsFile = new File(SETTINGS_FILE);
        String gSonString = gson.toJson(copySettings);
        try {
            FileUtils.writeStringToFile(settingsFile, gSonString, Charset.defaultCharset());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return copySettings;
    }
}
