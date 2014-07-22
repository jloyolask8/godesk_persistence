/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itcs.helpdesk.persistence.utils;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.eclipse.persistence.descriptors.ClassDescriptor;
import org.eclipse.persistence.sessions.SessionEvent;
import org.eclipse.persistence.sessions.SessionEventAdapter;
import org.eclipse.persistence.sessions.server.ClientSession;

/**
 *
 * @author jonathan
 */
public class SessionEventListener extends SessionEventAdapter {

    @Override
    public void preReleaseClientSession(SessionEvent event) {
        
        ClientSession session = (ClientSession) event.getSession();
        Map<Class, ClassDescriptor> descriptors = session.getDescriptors();

        for (Entry<Class, ClassDescriptor> entry : descriptors.entrySet()) {
            ClassDescriptor descriptor = entry.getValue();
            try {
                Field field = ClassDescriptor.class.getDeclaredField("referencingClasses");
                field.setAccessible(true);
                Set<?> set = (Set<?>) field.get(descriptor);
                set.clear();
            } catch (Exception e) {
                e.printStackTrace();
            }
            descriptor.getMappingsPostCalculateChanges().clear();
        }
    }
}
