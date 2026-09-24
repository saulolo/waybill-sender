package com.vidanova.shipping.controller;

import com.vidanova.shipping.model.ShippingData;
import com.vidanova.shipping.service.TemplateService;
import com.vidanova.shipping.service.ValidationService;
import com.vidanova.shipping.service.ValidationService.ValidationException;
import com.vidanova.shipping.util.PriceFormatter;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;

import java.util.Map;

/**
 * Controlador JavaFX para la vista principal {@code main-view.fxml}.
 *
 * <p>Responsabilidades:
 * <ul>
 *   <li>Captura y formateo en tiempo real del campo Precio.</li>
 *   <li>Validación Fail-Fast mediante {@link ValidationService}.</li>
 *   <li>Generación de la plantilla Markdown con {@link TemplateService}.</li>
 *   <li>Copia del resultado al portapapeles del sistema.</li>
 * </ul>
 */
public class MainController {

    // ─── Campos del formulario ────────────────────────────────────────────────
    @FXML private TextField nombreField;
    @FXML private TextField productoField;
    @FXML private TextField numeroGuiaField;
    @FXML private TextField precioField;
    @FXML private TextField direccionField;

    // ─── Área de previsualización ─────────────────────────────────────────────
    @FXML private TextArea previewArea;

    // ─── Botones ──────────────────────────────────────────────────────────────
    @FXML private Button generarBtn;
    @FXML private Button limpiarBtn;
    @FXML private Button copiarBtn;

    // ─── Label de estado de copia ─────────────────────────────────────────────
    @FXML private Label copyStatusLabel;

    /**
     * Flag para evitar recursión en el listener del campo Precio.
     * Cuando el listener modifica el texto programáticamente, este flag
     * evita que el cambio dispare otra iteración.
     */
    private boolean updatingPrecio = false;

    /**
     * Mapa de fx:id → TextField para hacer foco dinámico al fallar validación.
     * Se llena en {@link #initialize()} tras que FXML inyecte los campos.
     */
    private Map<String, TextField> fieldMap;

    // ─────────────────────────────────────────────────────────────────────────
    // Inicialización
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Llamado automáticamente por JavaFX tras cargar el FXML.
     * Registra el listener de formateo en tiempo real del campo Precio.
     */
    @FXML
    public void initialize() {
        // Mapa para resolver campo por su fx:id
        fieldMap = Map.of(
                "nombre",     nombreField,
                "producto",   productoField,
                "numeroGuia", numeroGuiaField,
                "precio",     precioField,
                "direccion",  direccionField
        );

        // Listener: formatea el precio mientras el usuario escribe
        precioField.textProperty().addListener((obs, oldValue, newValue) -> {
            if (updatingPrecio) return;
            updatingPrecio = true;
            try {
                // Extraer solo dígitos del valor actual
                String digits = newValue.replaceAll("[^0-9]", "");
                String formatted = digits.isEmpty() ? "" : PriceFormatter.format(digits);
                precioField.setText(formatted);
                // Mover cursor al final
                precioField.positionCaret(formatted.length());
            } finally {
                updatingPrecio = false;
            }
        });

        // Deshabilitar botón Copiar hasta que haya contenido generado
        copiarBtn.setDisable(true);
        copyStatusLabel.setVisible(false);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Manejadores de eventos
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Acción del botón "Generar Confirmación".
     * Valida los campos y, si son correctos, genera y muestra la plantilla.
     */
    @FXML
    private void handleGenerar() {
        copyStatusLabel.setVisible(false);

        // Precio en bruto (solo dígitos) para validación y modelo
        String precioRaw = PriceFormatter.strip(precioField.getText());

        ShippingData data = new ShippingData(
                nombreField.getText(),
                productoField.getText(),
                numeroGuiaField.getText(),
                precioRaw,
                direccionField.getText()
        );

        try {
            ValidationService.validate(data);
            String template = TemplateService.buildTemplate(data);
            previewArea.setText(template);
            copiarBtn.setDisable(false);

        } catch (ValidationException ex) {
            // Mostrar Alert tipo ERROR con detalle del campo
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error de validación");
            alert.setHeaderText("Campo inválido: " + humanFieldName(ex.getFieldId()));
            alert.setContentText(ex.getMessage());
            alert.getDialogPane().setMinHeight(javafx.scene.layout.Region.USE_PREF_SIZE);
            alert.showAndWait();

            // Hacer foco en el campo erróneo
            TextField target = fieldMap.get(ex.getFieldId());
            if (target != null) {
                target.requestFocus();
                target.selectAll();
            }
        }
    }

    /**
     * Acción del botón "Limpiar".
     * Resetea todos los campos de entrada y el área de previsualización.
     */
    @FXML
    private void handleLimpiar() {
        nombreField.clear();
        productoField.clear();
        numeroGuiaField.clear();
        precioField.clear();
        direccionField.clear();
        previewArea.clear();
        copiarBtn.setDisable(true);
        copyStatusLabel.setVisible(false);
        nombreField.requestFocus();
    }

    /**
     * Acción del botón "Copiar al Portapapeles".
     * Copia el contenido del {@code previewArea} al portapapeles del sistema.
     */
    @FXML
    private void handleCopiar() {
        String content = previewArea.getText();
        if (content == null || content.isBlank()) return;

        ClipboardContent clipboardContent = new ClipboardContent();
        clipboardContent.putString(content);
        Clipboard.getSystemClipboard().setContent(clipboardContent);

        // Feedback visual al usuario
        copyStatusLabel.setText("✅ ¡Copiado al portapapeles!");
        copyStatusLabel.setVisible(true);

        // Ocultar el label después de 2.5 segundos
        javafx.animation.PauseTransition pause =
                new javafx.animation.PauseTransition(javafx.util.Duration.seconds(2.5));
        pause.setOnFinished(e -> copyStatusLabel.setVisible(false));
        pause.play();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Helpers privados
    // ─────────────────────────────────────────────────────────────────────────

    /** Convierte el fx:id de un campo a su nombre legible para el Alert. */
    private String humanFieldName(String fieldId) {
        return switch (fieldId) {
            case "nombre"     -> "Nombre";
            case "producto"   -> "Producto";
            case "numeroGuia" -> "Número de Guía";
            case "precio"     -> "Precio";
            case "direccion"  -> "Dirección";
            default           -> fieldId;
        };
    }
}
