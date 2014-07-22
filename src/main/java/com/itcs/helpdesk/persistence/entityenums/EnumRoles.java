/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itcs.helpdesk.persistence.entityenums;

import com.itcs.helpdesk.persistence.entities.Rol;

/**
 *
 * @author jorge
 */
public enum EnumRoles 
{
    ADMINISTRADOR(new Rol("ADMINISTRADOR", "Administrador Sistema", null, EnumFunciones.getAll(), EnumUsuariosBase.getAll(), false));
    
    private Rol rol;
    
    EnumRoles(Rol rol)
    {
        this.rol = rol;
    }
    
    public Rol getRol()
    {
        return this.rol;
    }
}
