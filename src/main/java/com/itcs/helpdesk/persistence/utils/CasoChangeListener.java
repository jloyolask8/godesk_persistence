/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itcs.helpdesk.persistence.utils;

import com.itcs.helpdesk.persistence.entities.AuditLog;
import com.itcs.helpdesk.persistence.entities.Caso;
import java.util.List;

/**
 *
 * @author jorge
 */
public interface CasoChangeListener
{
    void casoChanged(Caso caso, List<AuditLog> changeList);
    void casoCreated(Caso caso);
    void notifyAllWatchersOnline(Caso caso, String whoMadeTheChange, String message);
}
