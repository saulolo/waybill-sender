package com.vidanova.shipping.model;

/**
 * POJO inmutable que representa los datos capturados en el formulario
 * para generar una confirmación de guía de envío.
 *
 * @param nombre       Nombre del destinatario (solo letras y espacios)
 * @param producto     Descripción del producto pedido
 * @param numeroGuia   Número de guía de la transportadora (solo dígitos)
 * @param precio       Precio COD en formato limpio (ej. "79900") — se formatea en la vista
 * @param direccion    Dirección de entrega del destinatario
 */
public record ShippingData(
        String nombre,
        String producto,
        String numeroGuia,
        String precio,
        String direccion
) {}
