/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itcs.helpdesk.persistence.jpa.custom;

import com.itcs.helpdesk.persistence.jpa.EmailClienteJpaController;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author jonathan
 */
public class EmailClienteJpaCustomController extends EmailClienteJpaController {

    public EmailClienteJpaCustomController(UserTransaction utx, EntityManagerFactory emf, String schema) {
        super(utx, emf, schema);
    }

//    public EmailClienteJpaCustomController(UserTransaction utx, EntityManagerFactory emf) {
//        super(utx, emf);
//    }
   

    

    
}
