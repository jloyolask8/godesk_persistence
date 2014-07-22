package com.itcs.helpdesk.persistence.entityenums;

import com.itcs.helpdesk.persistence.entities.Usuario;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jorge
 */
public enum EnumUsuariosBase
{
    SISTEMA(new Usuario("SISTEMA", true, "40617a8e1f34052a48696d30cd8e47e6", "1-9", "invalid@address.com", false, "SISTEMA",""),true);
//    SIN_PROPIETARIO(new Usuario("sinpropietario", "Sin", "Propietario"),false);

    private Usuario usuario;
    private boolean persistente;

    EnumUsuariosBase(Usuario usuario, boolean persistente)
    {
        this.usuario = usuario;
        this.persistente = persistente;
    }
    
    public boolean isPersistente()
    {
        return persistente;
    }

    public Usuario getUsuario()
    {
        return usuario;
    }
    
    public static List<Usuario> getAll()
    {
        ArrayList<Usuario> funciones = new ArrayList<Usuario>(values().length);
        for (EnumUsuariosBase enumUsers : values()) {            
            if (enumUsers.isPersistente()) {
                funciones.add(enumUsers.getUsuario());
            }
        }
        return funciones;
    }
}
