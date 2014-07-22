package com.itcs.helpdesk.persistence.entityenums;

import com.itcs.helpdesk.persistence.entities.EstadoCaso;

/**
 *
 * @author jorge
 */
public enum EnumEstadoCaso
{
    ABIERTO(new EstadoCaso("abierto", "Abierto","Caso Abierto")),
    CERRADO(new EstadoCaso("cerrado", "Cerrado","Caso Cerrado"));

    private EstadoCaso estado;

    EnumEstadoCaso(EstadoCaso estado)
    {
        this.estado = estado;
    }

    public EstadoCaso getEstado()
    {
        return estado;
    }
    
     public static EnumEstadoCaso findEnumEstadoCaso(String nombreCampo) {
        for (EnumEstadoCaso x : EnumEstadoCaso.values()) {
            if (x.getEstado().getIdEstado().equalsIgnoreCase(nombreCampo)) {
                return x;
            }
        }
        return null;
    }
}
