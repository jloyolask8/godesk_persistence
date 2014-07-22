package com.itcs.helpdesk.persistence.entityenums;

import com.itcs.helpdesk.persistence.entities.TipoNota;

/**
 *
 * @author jorge
 */
public enum EnumTipoNota {

    NOTA(new TipoNota(1, "Comentario"),"fa-comments"),
    RESPUESTA_A_CLIENTE(new TipoNota(2, "Respuesta"),"fa-envelope"),
    RESPUESTA_DE_CLIENTE(new TipoNota(3, "Comentario del Solicitante"), "fa-comments-o"),
    BORRADOR_RESPUESTA_A_CLIENTE(new TipoNota(4, "Borrador"),"fa-edit"),
    RESPUESTA_SERVIDOR(new TipoNota(5, "Respuesta del servidor"),"fa-cloud-upload"),
    LLAMADA_A_CLIENTE(new TipoNota(6, "LLamada"),"fa-phone"),
    TRANSFERENCIA_CASO(new TipoNota(7, "Transferencia"),"fa-exchange"),
    NOTIFICACION_UPDATE_CASO(new TipoNota(8, "Notificación por email"),"fa-bell");
    private TipoNota tipoNota;
    private String style;

    EnumTipoNota(TipoNota tipoNota, String style) {
        this.tipoNota = tipoNota;
        this.style = style;
    }

    public TipoNota getTipoNota() {
        return tipoNota;
    }
    
    public String getStyle()
    {
        return style;
    }
}
