/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itcs.helpdesk.persistence.entityenums;

import com.itcs.helpdesk.persistence.entities.Prioridad;

/**
 *
 * @author jorge
 */
public enum EnumPrioridad {

//    INTERNO_BAJA(new Prioridad(EnumTipoCaso.INTERNO.getTipoCaso(), "INTERNO_BAJA", "SEVERIDAD BAJA", "Severidad Baja: No tengo apuro por la solucion a mi problema.", 72)),
//    INTERNO_MEDIA(new Prioridad(EnumTipoCaso.INTERNO.getTipoCaso(), "INTERNO_MEDIA", "SEVERIDAD MEDIA", "Severidad Media: Necesito una solucion pero puedo esperar en pro de la calidad del servicio.", 48)),
//    INTERNO_ALTA(new Prioridad(EnumTipoCaso.INTERNO.getTipoCaso(), "INTERNO_ALTA", "SEVERIDAD ALTA", "Severidad alta: El problema es importante pero no de vida o muerte.", 24)),
    //    
    BAJA(new Prioridad("BAJA", "BAJA", "Prioridad Baja: No tengo apuro por la solucion a mi problema.", 72)),
    MEDIA(new Prioridad("MEDIA", "MEDIA", "Prioridad Media: Necesito una solucion pero puedo esperar en pro de la calidad del servicio.", 48)),
    ALTA(new Prioridad("ALTA", "ALTA", "Prioridad alta: El problema es importante pero no de vida o muerte.", 24)),
    MAXIMA(new Prioridad("MAXIMA", "MAXIMA", "Prioridad Maxima: El problema es crítico.", 4));
    //
//    POSTVENTA_BAJA(new Prioridad(EnumTipoCaso.POSTVENTA.getTipoCaso(), "POSTVENTA_BAJA", "SEVERIDAD BAJA", "Severidad Baja: No tengo apuro por la solucion a mi problema.", 72)),
//    POSTVENTA_MEDIA(new Prioridad(EnumTipoCaso.POSTVENTA.getTipoCaso(), "POSTVENTA_MEDIA", "SEVERIDAD MEDIA", "Severidad Media: Necesito una solucion pero puedo esperar en pro de la calidad del servicio.", 48)),
//    POSTVENTA_ALTA(new Prioridad(EnumTipoCaso.POSTVENTA.getTipoCaso(), "POSTVENTA_ALTA", "SEVERIDAD ALTA", "Severidad alta: El problema es importante pero no de vida o muerte.", 24)),
//    //
//    COTIZACION_BAJA(new Prioridad(EnumTipoCaso.COTIZACION.getTipoCaso(), "COTIZACION_BAJA", "SEVERIDAD BAJA", "Severidad Baja: No tengo apuro por la solucion a mi problema.", 72)),
//    COTIZACION_MEDIA(new Prioridad(EnumTipoCaso.COTIZACION.getTipoCaso(), "COTIZACION_MEDIA", "SEVERIDAD MEDIA", "Severidad Media: Necesito una solucion pero puedo esperar en pro de la calidad del servicio.", 48)),
//    COTIZACION_ALTA(new Prioridad(EnumTipoCaso.COTIZACION.getTipoCaso(), "COTIZACION_ALTA", "SEVERIDAD ALTA", "Severidad alta: El problema es importante pero no de vida o muerte.", 24)),
//    //
//    PREVENTA_BAJA(new Prioridad(EnumTipoCaso.PREVENTA.getTipoCaso(), "PREVENTA_BAJA", "SEVERIDAD BAJA", "Severidad Baja: No tengo apuro por la solucion a mi problema.", 72)),
//    PREVENTA_MEDIA(new Prioridad(EnumTipoCaso.PREVENTA.getTipoCaso(), "PREVENTA_MEDIA", "SEVERIDAD MEDIA", "Severidad Media: Necesito una solucion pero puedo esperar en pro de la calidad del servicio.", 48)),
//    PREVENTA_ALTA(new Prioridad(EnumTipoCaso.PREVENTA.getTipoCaso(), "PREVENTA_ALTA", "SEVERIDAD ALTA", "Severidad alta: El problema es importante pero no de vida o muerte.", 24));
    private Prioridad prioridad;

    private EnumPrioridad(Prioridad prioridad) {
        this.prioridad = prioridad;
    }

    public Prioridad getPrioridad() {
        return prioridad;
    }
}
