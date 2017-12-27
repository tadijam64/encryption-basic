package controller;


import algorithm.Aes;
import algorithm.Rsa;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.codehaus.plexus.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.Constants;
import utils.MessageDialogUtils;
import utils.MyFileUtils;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

public class PocetnaController extends MainController {

    private  final Logger log = LoggerFactory.getLogger(PocetnaController.class);

    @FXML
    private TextArea txtOrginalni;

    @FXML
    private TextArea txtOrginalni1;

    @FXML
    private TextArea txtOrginalni11;

    @FXML
    private TextArea txtKriptirani;

    @FXML
    private TextField txtTajniKljuc;

    @FXML
    private TextField txtPrivatniKljuc;

    @FXML
    private TextField txtPrivatniKljuc1;

    @FXML
    private TextField txtJavniKljuc;

    @FXML
    private TextField txtJavniKljuc1;

    @FXML
    private TextArea txtSazetak1;

    @FXML
    private TextArea txtDigitalniPotpis;

    public void btnStvoriTajniClicked() {
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance("AES");
            keyGen.init(128);
            SecretKey secretKey = keyGen.generateKey();

            String hexString = Hex.encodeHexString(secretKey.getEncoded());

            spremiDatoteku(Constants.TAJNI_KLJUC_PATH, hexString);
            txtTajniKljuc.setText(hexString);

        } catch (NoSuchAlgorithmException e) {
            log.error("Neuspjesno generiranje tajnog kljuca: ", e);
        }
    }

    public void btnUcitajClicked() {
        txtOrginalni.setText(ucitajDatoteku(Constants.ORIGINALNI_TEKST_PATH));
        txtOrginalni1.setText(ucitajDatoteku(Constants.ORIGINALNI_TEKST_PATH));
        txtOrginalni11.setText(ucitajDatoteku(Constants.ORIGINALNI_TEKST_PATH));
    }

    public void btnSpremiClicked() {
        spremiDatoteku(Constants.ORIGINALNI_TEKST_PATH, txtOrginalni.getText());
    }

    public void btnUcitajTajniClicked() {
        txtTajniKljuc.setText(ucitajDatoteku(Constants.TAJNI_KLJUC_PATH));
    }

    public void btnSpremiTajniClicked() {
        spremiDatoteku(Constants.TAJNI_KLJUC_PATH, txtTajniKljuc.getText());
    }

    public void btnUcitajKriptiraniClicked() {
        txtKriptirani.setText(ucitajDatoteku(Constants.KRIPTIRANI_TEKST_PATH));
    }

    public void btnKriptirajClicked() {
        String tajniKljuc = txtTajniKljuc.getText();
        if (!StringUtils.isEmpty(tajniKljuc)) {
            String originalniText = txtOrginalni.getText();

            if(!StringUtils.isEmpty(originalniText)) {
                try{
                    txtKriptirani.setText(Aes.encrypt(originalniText, tajniKljuc, false));
                    spremiDatoteku(Constants.KRIPTIRANI_TEKST_PATH, txtKriptirani.getText());
                    spremiDatoteku(Constants.ORIGINALNI_TEKST_PATH, txtOrginalni.getText());
                } catch(Exception e) {
                    log.error("Neispravan tajni kljuc: ", e);
                    showMessageForTajniKljucError(e);
                }
            } else {
                MessageDialogUtils.showMessage(Alert.AlertType.ERROR,
                        "Greška",
                        null,
                        "Originalni tekst je prazan");
            }
        } else {
            showMessageForTajniKljucMissing();
        }
    }

    public void btnDekriptirajClicked() {
        String tajniKljuc = txtTajniKljuc.getText();
        if(!StringUtils.isEmpty(tajniKljuc)){
            String kriptiraniTekst = txtKriptirani.getText();

            if(!StringUtils.isEmpty(kriptiraniTekst)){
                try{
                    txtOrginalni.setText(Aes.decrypt(kriptiraniTekst, tajniKljuc, false));
                    spremiDatoteku(Constants.ORIGINALNI_TEKST_PATH, txtOrginalni.getText());
                } catch (Exception e) {
                    showMessageForTajniKljucError(e);
                }
            } else {
                MessageDialogUtils.showMessage(Alert.AlertType.ERROR,
                        "Greska,",
                        null,
                        "Kriptirani tekst ne smije biti prazan");
            }
        } else {
            showMessageForTajniKljucMissing();
        }
    }

    public void btnKriptirajRSAClicked() {
        String originalniText = txtOrginalni.getText();
        String kljuc = txtJavniKljuc.getText();

        if (!StringUtils.isEmpty(txtPrivatniKljuc.getText()) && !StringUtils.isEmpty(txtJavniKljuc.getText())) {
            if (!StringUtils.isEmpty(originalniText)) {
                try {
                    txtKriptirani.setText(Rsa.encrypt(originalniText, kljuc, true));
                    spremiDatoteku(Constants.KRIPTIRANI_TEKST_PATH, txtKriptirani.getText());
                } catch (Exception e) {
                    log.error("Doslo je do greske prilikom RSA kriptiranja");
                    MessageDialogUtils.showMessage(Alert.AlertType.ERROR,
                            "Greska",
                            "Došlo je do greške prilikom kriptiranja",
                            "Greška: " + e.getMessage());
                }
            } else {
                MessageDialogUtils.showMessage(Alert.AlertType.ERROR,
                        "Greška",
                        null,
                        "Ova funkcija nije moguća bez javnog i privatnog ključa");
            }
        }
    }

    public void btnDekriptirajRSAClicked() {
        if (!StringUtils.isEmpty(txtPrivatniKljuc.getText()) && !StringUtils.isEmpty(txtJavniKljuc.getText())) {

         try {
             String kljuc = txtPrivatniKljuc.getText();
             txtOrginalni.setText(Rsa.decrypt(txtKriptirani.getText(), kljuc, false));
                spremiDatoteku(Constants.ORIGINALNI_TEKST_PATH, txtOrginalni.getText());
            } catch (Exception e) {
                log.error("Doslo je do greske prilikom RSA dekriptiranja");
                MessageDialogUtils.showMessage(Alert.AlertType.ERROR,
                        "Greska",
                        "Došlo je do greške prilikom dekriptiranja",
                        "Greška: " + e.getMessage());
            }
        } else {
            MessageDialogUtils.showMessage(Alert.AlertType.ERROR,
                    "Greška",
                    null,
                    "Moraju postojati javni i privatni kljucevi");
        }
    }

    public void btnSpremiPrivatniClicked() throws IOException {
        spremiDatoteku(Constants.PRIVATNI_KLJUC_PATH, txtPrivatniKljuc.getText());
    }

    public void btnSpremiPrivatniClicked1() throws IOException {
        spremiDatoteku(Constants.PRIVATNI_KLJUC_PATH, txtPrivatniKljuc1.getText());
    }

    public void btnStvoriPrivatniJavniClicked() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            KeyPair keyPair = keyPairGenerator.genKeyPair();

            String hexPrivateKey = Hex.encodeHexString(keyPair.getPrivate().getEncoded());
            String hexPublicKey = Hex.encodeHexString(keyPair.getPublic().getEncoded());

            spremiDatoteku(Constants.PRIVATNI_KLJUC_PATH, hexPrivateKey);
            spremiDatoteku(Constants.JAVNI_KLJUC_PATH, hexPublicKey);

            txtPrivatniKljuc.setText(hexPrivateKey);
            txtJavniKljuc.setText(hexPublicKey);

        } catch (NoSuchAlgorithmException e) {
            log.error("Neuspjesno geniranje privatnog/javnog kljuca: ", e);
            MessageDialogUtils.showMessage(Alert.AlertType.ERROR,
                    "Greška",
                    "Dogodila se greška prilikom generianja ključeva",
                    "Greška: " + e.getMessage());
        }
    }

    public void btnSpremiJavniClicked() {
        spremiDatoteku(Constants.JAVNI_KLJUC_PATH, txtJavniKljuc.getText());
    }

    public void btnSpremiJavniClicked1() {
        spremiDatoteku(Constants.JAVNI_KLJUC_PATH, txtJavniKljuc1.getText());
    }

    public void btnUcitajPrivatniJavniClicked() {
        try {
            txtPrivatniKljuc.setText(MyFileUtils.openTextFile(Constants.PRIVATNI_KLJUC_PATH));
            txtJavniKljuc.setText(MyFileUtils.openTextFile(Constants.JAVNI_KLJUC_PATH));
        } catch (IOException e) {
            log.error("Neuspjesno citanje iz privatni/javni: {}", e.getMessage());
        }
    }

    public void btnUcitajPrivatniJavniClicked1() {
        try {
            txtPrivatniKljuc1.setText(MyFileUtils.openTextFile(Constants.PRIVATNI_KLJUC_PATH));
            txtJavniKljuc1.setText(MyFileUtils.openTextFile(Constants.JAVNI_KLJUC_PATH));
        } catch (IOException e) {
            log.error("Neuspjesno citanje iz privatni/javni: {}", e.getMessage());
        }
    }

    private void showMessageForTajniKljucMissing() {
        MessageDialogUtils.showMessage(Alert.AlertType.ERROR,
                "Greška",
                null,
                "Za korištenje ove funkcionalnosti tajni ključ mora postojati. Generirajte tajni ključ!");
    }

    private void showMessageForTajniKljucError(Throwable e) {
        MessageDialogUtils.showMessage(Alert.AlertType.ERROR,
                "Greška",
                "Dogodila se greška prilikom ove akcije.",
                "Greška: " + e.getMessage());
    }

    public void btnGenerirajSazetakClicked() {
        txtSazetak1.setText(DigestUtils.sha512Hex(txtOrginalni1.getText()));
    }

    public void btnProvjeriSazetakClicked() {
        String trenutniSazetak = txtSazetak1.getText();
        String noviSazetak = DigestUtils.sha512Hex(txtOrginalni1.getText());

        if (trenutniSazetak.equals(noviSazetak)) {
            MessageDialogUtils.showMessage(Alert.AlertType.INFORMATION, "Informacija", null, "Dva sažetka su jednaka");
        } else {
            MessageDialogUtils.showMessage(Alert.AlertType.WARNING, "Informacija", null, "Dva sažetka nisu jednaka");
        }
    }

    public void btnGenerirajDigitalniPotpisClicked() {
        if (!StringUtils.isEmpty(txtJavniKljuc1.getText()) && !StringUtils.isEmpty(txtPrivatniKljuc1.getText())) {

            String sha512Hex = DigestUtils.sha512Hex(txtOrginalni11.getText());
            String privatniKljuc = txtPrivatniKljuc1.getText();

            try {
                txtDigitalniPotpis.setText(Rsa.encrypt(sha512Hex, privatniKljuc, false));
            } catch (Exception e) {
                log.error("Neupjesno kreiranje digitalnog potpisa");
                MessageDialogUtils.showMessage(Alert.AlertType.ERROR,
                        "Greška",
                        "Došlo je do greške prilikom kreiranja digitalnog potpisa",
                        "Greška: " + e.getMessage());
            }
        } else {
            MessageDialogUtils.showMessage(Alert.AlertType.ERROR,
                    "Greška",
                    null,
                    "Ova funkcija nije moguća bez javnog i privatnog ključa");
        }
    }


    public void btnProvjeriDigitalniPotpisClicked() {
        if(!StringUtils.isEmpty(txtJavniKljuc1.getText()) && !StringUtils.isEmpty(txtPrivatniKljuc1.getText())) {

            String sha512Hex = DigestUtils.sha512Hex(txtOrginalni11.getText());
            String JavniKljuc = txtJavniKljuc1.getText();

            try {
                String digitalniPotpisMessage = Rsa.decrypt(txtDigitalniPotpis.getText(), JavniKljuc, true);

                if(sha512Hex.equals(digitalniPotpisMessage)) {
                    MessageDialogUtils.showMessage(Alert.AlertType.INFORMATION,
                            "Informacija",
                            null,
                            "Digitalni potpis je valjan.");
                } else {
                    MessageDialogUtils.showMessage(Alert.AlertType.WARNING,
                            "Informacija",
                            null,
                            "Digitalni potpis nije valjan.");
                }
            } catch (Exception e) {
                log.error("Neuspjesna provjera digitalnog potpisa");
                MessageDialogUtils.showMessage(Alert.AlertType.ERROR,
                        "Greška",
                        "Došlo je do greške prilikom provjere digitalnog potpisa",
                        "Greška" + e.getMessage());
                }
            } else {
                MessageDialogUtils.showMessage(Alert.AlertType.ERROR,
                        "Greška",
                        null,
                        "Ova funkcija nije moguća bez javnog i privatnog ključa");
        }
    }
}
