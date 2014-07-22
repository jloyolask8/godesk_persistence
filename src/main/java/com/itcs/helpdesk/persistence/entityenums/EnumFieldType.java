/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itcs.helpdesk.persistence.entityenums;

import com.itcs.helpdesk.persistence.entities.FieldType;

/**
 *
 * @author jonathan
 */
public enum EnumFieldType {

    NUMBER(new FieldType("NUMBER", "Numero", true)), //Long, Integer, etc
    TEXT(new FieldType("TEXT", "Campo de texto", true)), //String
    TEXTAREA(new FieldType("TEXTAREA", "Area de texto", true)), //String   
    SELECTONE(new FieldType("SELECTONE", "Lista selección simple", true)), //String
    SELECTMANY(new FieldType("SELECTMANY", "Lista selección múltiple", true)), //Entity or String
    CHECKBOX(new FieldType("CHECKBOX", "CheckBox (verdadero o falso)", true)), //Boolean    
    CALENDAR(new FieldType("CALENDAR", "Fecha", true)), //Date or String Date
    RADIO(new FieldType("RADIO", "Radio", false)), //Entity or String
    SELECTONE_ENTITY(new FieldType("SELECTONE_ENTITY", "Lista selección simple (Entidad)", false)), //Entity
    SELECT_MANY_ENTITIES(new FieldType("SELECT_MANY_ENTITIES", "Lista de seleccion múltiple (Entidad)", false)), //several Entities
    SELECTONE_PLACE_HOLDER(new FieldType("SELECTONE_PLACE_HOLDER", "Lista selección simple", false)), //Entity
    COMMA_SEPARATED_VALUELIST(new FieldType("COMMA_SEPARATED_VALUELIST", "Varios valores (separados por coma)", false)); //Entity or String
    //CALENDAR_PERIOD(new FieldType("CALENDAR_PERIOD", "Periodo de Fechas")),; //Date or String Date
    private FieldType fieldType;

    private EnumFieldType(FieldType fieldType) {
        this.fieldType = fieldType;
    }

    /**
     * @return the fieldType
     */
    public FieldType getFieldType() {
        return fieldType;
    }

    /**
     * @param fieldType the fieldType to set
     */
    public void setFieldType(FieldType fieldType) {
        this.fieldType = fieldType;
    }

    @Override
    public String toString() {
        return this.name();
    }
}
