package com.vidanova.shipping;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

/**
 * Punto de entrada de la aplicación VidanovaStore — Generador de Guías de Envío.
 * Extiende {@link Application} de JavaFX y carga el layout principal desde FXML.
 */
public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(
                Objects.requireNonNull(
                        getClass().getResource("main-view.fxml"),
                        "No se encontró main-view.fxml en el classpath"
                )
        );

        Scene scene = new Scene(loader.load(), 960, 660);

        // Aplicar hoja de estilos CSS
        scene.getStylesheets().add(
                Objects.requireNonNull(
                        getClass().getResource("styles.css"),
                        "No se encontró styles.css en el classpath"
                ).toExternalForm()
        );

        primaryStage.setTitle("🍃 Waybill Sender — VidanovaStore");
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(820);
        primaryStage.setMinHeight(580);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
