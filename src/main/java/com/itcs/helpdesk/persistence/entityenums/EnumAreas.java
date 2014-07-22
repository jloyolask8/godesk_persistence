/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itcs.helpdesk.persistence.entityenums;

import com.itcs.helpdesk.persistence.entities.Area;

/**
 *
 * @author jorge
 */
public enum EnumAreas {
    DEFAULT_AREA(new Area("DEFAULT_AREA", "default area", "Area por defecto", false));
    
    private Area area;
    
    EnumAreas(Area area)
    {
        this.area = area;
    }
    
    public Area getArea()
    {
        return this.area;
    }
}
