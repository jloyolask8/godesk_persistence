package com.itcs.helpdesk.persistence.entityenums;

import com.itcs.helpdesk.persistence.entities.TipoAlerta;

/**
 *
 * @author jorge
 */
public enum EnumTipoAlerta
{
    TIPO_ALERTA_PENDIENTE(new TipoAlerta(1, "Caso Pendiente", "Se encuentra dentro del tiempo esperado", "success")),
    TIPO_ALERTA_POR_VENCER(new TipoAlerta(2, "Caso Por Vencer", "Se encuentra finalizando el tiempo esperado", "warning")),
    TIPO_ALERTA_VENCIDO(new TipoAlerta(3, "Caso Vencido", "Se encuentra finalizado el tiempo esperado", "danger"));

    private TipoAlerta tipoAlerta;

    EnumTipoAlerta(TipoAlerta tipoAlerta)
    {
        this.tipoAlerta = tipoAlerta;
    }

    public TipoAlerta getTipoAlerta()
    {
        return tipoAlerta;
    }
}
