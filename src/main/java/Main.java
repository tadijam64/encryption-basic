import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Main extends Application {

    private final Logger log = LoggerFactory.getLogger(Main.class);

    private Stage primaryStage;
    private static TabPane root;

    public static void main(String[] args) {

        BasicConfigurator.configure();
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/pocetna.fxml"));
            root = fxmlLoader.load();

            Scene scene = new Scene(root, 1024, 768);
            primaryStage.setScene(scene);
            primaryStage.setTitle("OS2 projekt");
            primaryStage.setMinWidth(1010);
            primaryStage.setMinHeight(750);

            primaryStage.setMaxWidth(1010);
            primaryStage.setMaxHeight(750);

            primaryStage.show();
        } catch (Exception e) {
            log.error("Neuspješno prikazivanje Maina: ", e);
        }
    }
}
