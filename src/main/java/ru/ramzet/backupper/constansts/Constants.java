package ru.ramzet.backupper.constansts;

import java.io.File;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Constants {
    public static final String SETTINGS_FILE = "settings.bus";
    public static final String PS = File.pathSeparator;
    public static final DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern("MM.dd.yyyy_HH.mm.ss");

    public static List<String> MAIN_MENU;
    public static List<String> SETTINGS_MENU;

    static {
        MAIN_MENU = new ArrayList<>();
        MAIN_MENU.add("1. Копировать все");
        MAIN_MENU.add("2. Копировать выборочно");
        MAIN_MENU.add("3. Настройки копирования");
        MAIN_MENU.add("4. Просмотреть копии");
        MAIN_MENU.add("5. Выход");

        SETTINGS_MENU = new ArrayList<>();
        SETTINGS_MENU.add("1. Изменить одну ячейку параметров");
        SETTINGS_MENU.add("2. Удалить одну ячейку параметров");
        SETTINGS_MENU.add("3. Добавить одну ячейку параметров");
        SETTINGS_MENU.add("4. Выйти в предыдущее меню");
    }

    private Constants(){}
}
