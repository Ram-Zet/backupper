package ru.ramzet.backupper.model;

public enum Status {
    OK,
    NO_SUCH_SOURCE_FILE,
    NO_SUCH_SOURCE_PATH,
    NO_SUCH_DESTINATION_PATH,
    ERROR_IN_COPY_TIME,
    ERROR_CREATING_DESTINATION_FOLDER,
    NO_SUCH_SOURCE
}
