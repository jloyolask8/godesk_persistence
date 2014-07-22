/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itcs.helpdesk.persistence.entityenums;

import com.itcs.helpdesk.persistence.entities.Categoria;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jorge
 */
public enum EnumCategorias {
    CATEGORIA_EJEMPLO(new Categoria(1, "CATEGORIA_EJEMPLO", true, 1, EnumAreas.DEFAULT_AREA.getArea()), true);
    
    private Categoria categoria;
    private boolean persistente;
    
    EnumCategorias(Categoria categoria, boolean persistente)
    {
        this.categoria = categoria;
        this.persistente = persistente;
    }
    
    public Categoria getCategoria()
    {
        return this.categoria;
    }
    
    public static List<Categoria> getAll()
    {
        ArrayList<Categoria> lista = new ArrayList<Categoria>(values().length);
        for (EnumCategorias enumCat : values()) {
            if(enumCat.isPersistente()){
            lista.add(enumCat.getCategoria());
            }
        }
        return lista;
    }

    /**
     * @return the persistente
     */
    public boolean isPersistente() {
        return persistente;
    }
}
