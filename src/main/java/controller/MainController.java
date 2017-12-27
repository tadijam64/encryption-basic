package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.Constants;
import utils.MessageDialogUtils;
import utils.MyFileUtils;
import java.io.IOException;

public class MainController {

    private final Logger log = LoggerFactory.getLogger(MainController.class);

    protected void spremiDatoteku(String path, String text) {
        try{
            MyFileUtils.saveTextFile(path, text);
            MessageDialogUtils.showMessage(Alert.AlertType.INFORMATION,
                    "Info",
                    "Uspješno",
                    "Uspješno spremljena datoteka");
        }catch (Exception e){
            log.error("Neuspješno spremanje datoteke: " + path + e.getMessage());
            MessageDialogUtils.showMessage(Alert.AlertType.ERROR,
                    "Greška",
                    "Neuspješno spremanje datoteke",
                    "Greška" + e.getMessage());
        }
    }

    protected String ucitajDatoteku(String path) {
            String tekst = "";
        try {
            tekst = MyFileUtils.openTextFile(path);
        } catch (IOException e) {
            log.error("Neuspješno učitavanje datoteke: " + path, e);
            MessageDialogUtils.showMessage(Alert.AlertType.ERROR,
                    "Greška",
                    "Neuspješno učitavanje datoteke" + path,
                    "Greška" + e.getMessage());
        }
        return tekst;
    }
}
