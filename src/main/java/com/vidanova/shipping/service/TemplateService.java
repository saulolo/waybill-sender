package com.vidanova.shipping.service;

import com.vidanova.shipping.model.ShippingData;
import com.vidanova.shipping.util.PriceFormatter;

/**
 * Servicio responsable de construir la plantilla Markdown con emojis
 * para la confirmación de guía de envío de VidanovaStore.
 *
 * <p>Produce exactamente el formato acordado, interpolando los
 * datos validados del formulario.
 */
public final class TemplateService {

    private TemplateService() {}

    /**
     * Construye la confirmación de guía de envío en formato Markdown.
     *
     * @param data datos validados del formulario
     * @return cadena con la plantilla completa lista para copiar / enviar
     */
    public static String buildTemplate(ShippingData data) {
        String precioFormateado = PriceFormatter.format(
                PriceFormatter.strip(data.precio())
        );

        return """
                *CONFIRMACIÓN GUIA*
                ¡Hola, %s! 👋 Te saludamos nuevamente de 🍃VidanovaStore.

                Te confirmamos que tu pedido de la %s ya fue empacado y entregado a la transportadora. 📦✨

                🚚 *Número de Guía*: %s
                🔗 *Rastreo*: Puedes consultar el estado de tu paquete directamente en la página web de la transportadora con ese número de guía.

                El tiempo estimado de entrega es de *2 a 4 días hábiles*.
                💵 Recuerda tener listos los *%s* en efectivo para cuando el repartidor llegue a tu dirección (%s).

                ¡Cualquier duda que tengas durante el camino, nos puedes escribir por aquí! Que tengas un excelente día. 😊"""
                .formatted(
                        data.nombre().trim(),
                        data.producto().trim(),
                        data.numeroGuia().trim(),
                        precioFormateado,
                        data.direccion().trim()
                );
    }
}
