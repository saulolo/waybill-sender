package com.vidanova.shipping.service;

import com.vidanova.shipping.model.ShippingData;
import com.vidanova.shipping.util.PriceFormatter;

import java.util.regex.Pattern;

/**
 * Servicio de validación con estrategia <b>Fail-Fast</b>.
 *
 * <p>Cada método {@code validate*} lanza una {@link ValidationException}
 * al primer error encontrado, indicando el nombre del campo afectado
 * para que el controlador pueda hacer foco en él.
 */
public final class ValidationService {

    /** Solo letras (incluyendo tildes, ñ, diéresis) y espacios. */
    private static final Pattern NOMBRE_PATTERN =
            Pattern.compile("^[a-zA-ZÀ-ÿ\\s]+$");

    /** Solo dígitos numéricos, al menos uno. */
    private static final Pattern GUIA_PATTERN =
            Pattern.compile("^[0-9]+$");

    private ValidationService() {}

    /**
     * Valida todos los campos del formulario en orden.
     * Lanza {@link ValidationException} al primer fallo.
     *
     * @param data datos del formulario (con precio ya en bruto, solo dígitos)
     * @throws ValidationException si algún campo no supera la validación
     */
    public static void validate(ShippingData data) throws ValidationException {

        // ── 1. Campos vacíos ──────────────────────────────────────────────
        if (isBlank(data.nombre())) {
            throw new ValidationException(
                    "nombre",
                    "Por favor completa todos los campos.\n\nEl campo «Nombre» no puede estar vacío."
            );
        }
        if (isBlank(data.producto())) {
            throw new ValidationException(
                    "producto",
                    "Por favor completa todos los campos.\n\nEl campo «Producto» no puede estar vacío."
            );
        }
        if (isBlank(data.numeroGuia())) {
            throw new ValidationException(
                    "numeroGuia",
                    "Por favor completa todos los campos.\n\nEl campo «Número de Guía» no puede estar vacío."
            );
        }
        if (isBlank(data.precio())) {
            throw new ValidationException(
                    "precio",
                    "Por favor completa todos los campos.\n\nEl campo «Precio» no puede estar vacío."
            );
        }
        if (isBlank(data.direccion())) {
            throw new ValidationException(
                    "direccion",
                    "Por favor completa todos los campos.\n\nEl campo «Dirección» no puede estar vacío."
            );
        }

        // ── 2. Formato Nombre ─────────────────────────────────────────────
        if (!NOMBRE_PATTERN.matcher(data.nombre().trim()).matches()) {
            throw new ValidationException(
                    "nombre",
                    "El campo «Nombre» solo puede contener letras y espacios.\n\n" +
                    "Valor ingresado: \"" + data.nombre() + "\""
            );
        }

        // ── 3. Formato Número de Guía ─────────────────────────────────────
        if (!GUIA_PATTERN.matcher(data.numeroGuia().trim()).matches()) {
            throw new ValidationException(
                    "numeroGuia",
                    "El campo «Número de Guía» solo puede contener dígitos numéricos.\n\n" +
                    "Valor ingresado: \"" + data.numeroGuia() + "\""
            );
        }

        // ── 4. Precio numérico válido ─────────────────────────────────────
        try {
            long precio = Long.parseLong(PriceFormatter.strip(data.precio()));
            if (precio <= 0) throw new NumberFormatException("precio negativo o cero");
        } catch (NumberFormatException e) {
            throw new ValidationException(
                    "precio",
                    "El campo «Precio» debe ser un número positivo.\n\n" +
                    "Valor ingresado: \"" + data.precio() + "\""
            );
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Helpers
    // ─────────────────────────────────────────────────────────────────────────

    private static boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Excepción interna tipada
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Excepción que identifica el campo que falló y el mensaje descriptivo
     * para mostrar en el {@link javafx.scene.control.Alert} de error.
     */
    public static class ValidationException extends Exception {

        /** Identificador del campo fallido (coincide con el fx:id del TextField). */
        private final String fieldId;

        public ValidationException(String fieldId, String message) {
            super(message);
            this.fieldId = fieldId;
        }

        /** @return el fx:id del campo erróneo para hacer {@code requestFocus()} */
        public String getFieldId() {
            return fieldId;
        }
    }
}
