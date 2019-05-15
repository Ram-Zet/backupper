package ru.ramzet.backupper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import ru.ramzet.backupper.view.CopyView;

@SpringBootApplication
public class BackupperApplication {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(BackupperApplication.class, args);
        CopyView copyView = context.getBean(CopyView.class);
        copyView.start();
    }

}
