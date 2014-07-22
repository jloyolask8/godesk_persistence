package com.itcs.helpdesk.persistence.entityenums;

import com.itcs.helpdesk.persistence.entities.TipoCaso;

/**
 *
 * @author jorge
 */
public enum EnumTipoCaso {

//    PREGUNTA(new TipoCaso("Pregunta", "Pregunta", "Caso donde el cliente solo hace una pregunta.")),
//    PROBLEMA(new TipoCaso("Problema", "Problema", "Caso donde el cliente tiene un problema con el producto o servicio entregado.")),
//    INCIDENTE(new TipoCaso("Incidente", "Incidente", "Caso donde el cliente tiene un incidente que impide el correcto funcionamiento del producto o servicio entregado.")),
//    TAREA(new TipoCaso("Tarea", "Tarea", "Caso que representa una tarea a realizar por el agente.")),
    INTERNO(new TipoCaso("interno", "Interno", "Caso Interno.")),
    COTIZACION(new TipoCaso("cotizacion", "Cotización", "Caso donde el cliente solicita una Cotización de productos.")),
    PREVENTA(new TipoCaso("preventa", "Pre Venta", "Caso donde el cliente solicita una Cotización de productos y se inicia el proceso de venta.")),
    POSTVENTA(new TipoCaso("postventa", "Soporte/Post Venta", "Caso donde el cliente solicita servicio de postventa.")),
    CONTACTO(new TipoCaso("contacto", "Contacto", "Caso donde el cliente solicita un contacto."));
//    OTRO(new TipoCaso("Otro", "Otro", "Otro."));
    private TipoCaso tipoCaso;

    EnumTipoCaso(TipoCaso tipoCaso) {
        this.tipoCaso = tipoCaso;
    }

    public TipoCaso getTipoCaso() {
        return tipoCaso;
    }
}
