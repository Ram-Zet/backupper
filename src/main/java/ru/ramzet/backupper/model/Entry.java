package ru.ramzet.backupper.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Entry {
    private List<String> sources = new ArrayList<>();

    private String name;

    private String destination;

    private int copies;
}
