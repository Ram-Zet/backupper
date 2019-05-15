package ru.ramzet.backupper.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class ConsoleHelper {
    private ConsoleHelper() {
    }

    private static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    public static void writeMessage(String message) {
        System.out.println(message);
    }

    public static void writeMenu(List<String> menuItems) {
        System.out.println("Выберите действие: ");
        menuItems.forEach(System.out::println);
    }

    public static int readMenuItem(List<String> menuItems) {
        while (true) {
            System.out.println("Введите номер и нажмите Enter:");
            try {
                int input = Integer.parseInt(reader.readLine());
                if (input <= menuItems.size() && input > 0) return input;
                    else System.out.println("Неверный ввод");
            } catch (Exception e) {
                System.out.println("Неверный ввод");
            } finally {
                System.out.println();
            }
        }
    }

    public static void writeSettingsEntries(List<Entry> entries) {
        entries.forEach(e -> {
            System.out.println("===========================================================");
            System.out.println("Имя записи: " + e.getName());
            System.out.println("Источники копирования:      ");
            e.getSources().forEach(m -> System.out.println(m));
            System.out.println("Папка назначения");
            System.out.println(e.getDestination());
            System.out.println("Количество резервных копий: " + e.getCopies());
            System.out.println();
        });
        System.out.println("===========================================================");
    }

    public static String inputEntry(List<Entry> entries) {
        while (true) {
            try {
                System.out.println("Введите имя записи для удаления\\изменения или нажмите Enter для отмены действия");
                String input = reader.readLine();
                if (input == null || input.isEmpty()) return null;
                for (Entry entry:entries)
                    if (entry.getName().equalsIgnoreCase(input)) return input;
                    else System.out.println("Неверный ввод");
            } catch (IOException e) {
                System.out.println("Неверный ввод");
            } finally {
                System.out.println();
            }
        }
    }

    public static String input(){
        while (true){
            try {
                return reader.readLine();
            } catch (IOException e) {
                System.out.println("Ошибка ввода");
            }
        }
    }
}
