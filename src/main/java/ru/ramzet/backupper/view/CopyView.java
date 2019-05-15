package ru.ramzet.backupper.view;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.ramzet.backupper.controller.CopyController;
import ru.ramzet.backupper.model.CopySettings;
import ru.ramzet.backupper.model.Entry;
import ru.ramzet.backupper.model.Status;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import static ru.ramzet.backupper.constansts.Constants.*;

@Component
@AllArgsConstructor
public class CopyView {

    private final CopyController copyController;

    public void start() {
        mainMenu();
    }

    private void mainMenu() {
        boolean exit = false;
        while (!exit) {
            ConsoleHelper.writeMessage("Введите номер меню:");
            ConsoleHelper.writeMenu(MAIN_MENU);
            int input = ConsoleHelper.readMenuItem(MAIN_MENU);
            switch (input) {
                case 1:
                    ConsoleHelper.writeMessage("Старт копирования. Процесс может занять от 1 до 20 минут");
                    Status status = copyController.makeCopyAll();
                    if (status != Status.OK) ConsoleHelper.writeMessage("Произошла ошибка во врем копирования");
                    else ConsoleHelper.writeMessage("Конец копирования");
                    break;
                case 2:
                    String entryName = ConsoleHelper.inputEntry(CopySettings.getSettings().getEntryList());
                    for (Entry entry : CopySettings.getSettings().getEntryList()) {
                        if (entry.getName().equalsIgnoreCase(entryName)) {
                            Status status1 = copyController.makeCopyByOneEntry(entry);
                            if (status1 != Status.OK)
                                ConsoleHelper.writeMessage("Произошла ошибка во врем копирования");
                            else ConsoleHelper.writeMessage("Конец копирования");
                        }
                    }
                    break;
                case 3:
                    makeSettings();
                    break;
                case 4:
                    viewCopies();
                    break;
                case 5:
                    exit = true;
                    break;
            }
        }
        System.exit(0);
    }

    private void viewCopies() {
        CopySettings settings = CopySettings.getSettings();
        settings.getEntryList().forEach(e -> {
            System.out.println("====================================");
            System.out.println(e.getName());
            File destination = new File(e.getDestination());
            for (String s : destination.list()) {
                System.out.println(s);
            }
            System.out.println("====================================");
        });
    }

    private void makeSettings() {
        CopySettings settings = CopySettings.getSettings();
        boolean mainMenu = false;
        while (!mainMenu) {
            ConsoleHelper.writeSettingsEntries(settings.getEntryList());
            ConsoleHelper.writeMenu(SETTINGS_MENU);
            int input = ConsoleHelper.readMenuItem(SETTINGS_MENU);
            switch (input) {
                case 1:
                    //Editing
                    String entryName = ConsoleHelper.inputEntry(settings.getEntryList());
                    if (entryName == null) break;
                    Entry entry = settings.getEntryList().stream()
                            .filter(e -> e.getName().equalsIgnoreCase(entryName))
                            .findAny().orElse(null);
                    if (entry == null) {
                        ConsoleHelper.writeMessage("Такая запись отсутствует");
                        break;
                    }
                    ConsoleHelper.writeMessage("Вы выбрали запись с именем " + entryName);
                    entry = fillEntry(entry);
                    if (entry == null) break;
                    copyController.editEntry(entry);
                    break;
                case 2:
                    //Removing
                    String entryName1 = ConsoleHelper.inputEntry(settings.getEntryList());
                    if (entryName1 != null) {
                        if (copyController.removeEntry(entryName1)) {
                            ConsoleHelper.writeMessage("Запись с именем " + entryName1 + " успешно удалена");
                        }
                    }
                    break;
                case 3:
                    //Добавить ячейку
                    addEntry();
                    break;
                case 4:
                    mainMenu = true;
                    break;
            }
        }
    }

    private void addEntry() {
        Entry entry = new Entry();
        ConsoleHelper.writeMessage("Введите название задания");
        entry.setName(ConsoleHelper.input());
        entry = fillEntry(entry);
        if (entry == null) return;

        copyController.addEntry(entry);
    }

    private Entry fillEntry(Entry entry) {
        List<String> sources = new ArrayList<>();
        while (true) {
            ConsoleHelper.writeMessage("Введите путь файла/папки, которую нужно скопировать");
            ConsoleHelper.writeMessage("или нажмите Enter для завершения списка");
            String input = ConsoleHelper.input();
            if (input == null || input.isEmpty() || input.equalsIgnoreCase("")) break;
            try {
                input = new String(URLDecoder.decode(input, "UTF-8").getBytes("UTF-8"), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                System.out.println("Ошибка декодирования имени файла");
            }
            File file = new File(input);
            if (file.exists()) {
                ConsoleHelper.writeMessage("Путь добавлен");
                sources.add(input);
            } else
                ConsoleHelper.writeMessage("Неверный путь. Повторите");
        }
        if (sources == null || sources.isEmpty()) {
            ConsoleHelper.writeMessage("Не добавлено ни одного источника");
            return null;
        }
        entry.setSources(sources);

        while (true) {
            ConsoleHelper.writeMessage("Введите путь конечной точки для копирования");
            ConsoleHelper.writeMessage("или нажмите Enter для отмены");
            String input = ConsoleHelper.input();
            if (input == null || input.isEmpty() || input.equalsIgnoreCase("")) {
                break;
            }
            try {
                input = new String(URLDecoder.decode(input, "UTF-8").getBytes("UTF-8"), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                System.out.println("Ошибка декодирования имени файла");
            }
            File file = new File(input);
            if (file.exists()) {
                entry.setDestination(input);
                break;
            } else
                ConsoleHelper.writeMessage("Неверный путь, попробуйте еще раз");
        }
        if (entry.getDestination() == null || entry.getDestination().isEmpty()) {
            ConsoleHelper.writeMessage("Не введен путь конечной точки");
            return null;
        }

        while (true) {
            ConsoleHelper.writeMessage("Введите количество снапшотов (1 - 5");
            try {
                int input = Integer.parseInt(ConsoleHelper.input());
                entry.setCopies(input);
                break;
            } catch (NumberFormatException e) {
                ConsoleHelper.writeMessage("Неверный ввод");
            }
        }
        return entry;
    }
}
