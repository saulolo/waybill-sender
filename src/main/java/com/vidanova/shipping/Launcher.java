package com.vidanova.shipping;

/**
 * Clase de arranque separada de {@link MainApp}.
 *
 * <p>Esta clase existe como solución al error de JavaFX cuando se ejecuta
 * directamente desde el classpath de IntelliJ sin configuración de módulos.
 * Al no extender {@code javafx.application.Application}, el JVM puede
 * invocar este {@code main} sin conflicto con el launcher de JavaFX.
 *
 * <p><b>Configura IntelliJ para ejecutar esta clase</b>, no {@code MainApp}.
 */
public class Launcher {

    public static void main(String[] args) {
        MainApp.main(args);
    }
}
