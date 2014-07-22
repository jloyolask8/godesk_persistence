/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itcs.helpdesk.persistence.utils;

import com.itcs.helpdesk.persistence.entityenums.EnumFieldType;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author jonathan
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface FilterField {

    EnumFieldType fieldTypeId();

    String label();

    String fieldIdFull();

    Class fieldTypeFull();

    String listGenericTypeFieldId() default "";
}
