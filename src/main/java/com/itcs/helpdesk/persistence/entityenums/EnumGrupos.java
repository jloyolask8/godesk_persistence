/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itcs.helpdesk.persistence.entityenums;

import com.itcs.helpdesk.persistence.entities.Grupo;

/**
 *
 * @author jorge
 */
public enum EnumGrupos 
{
    GRUPO_SISTEMA(new Grupo("GRUPO SISTEMA", "GRUPO SISTEMA", "Reservado para el sistema", EnumAreas.DEFAULT_AREA.getArea(),false));
    
    private Grupo grupo;
    
    EnumGrupos(Grupo grupo)
    {
        this.grupo = grupo;
    }
    
    public Grupo getGrupo()
    {
        return this.grupo;
    }
}
