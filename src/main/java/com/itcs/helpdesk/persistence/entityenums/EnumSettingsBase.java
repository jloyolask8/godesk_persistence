/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itcs.helpdesk.persistence.entityenums;

import com.itcs.helpdesk.persistence.entities.AppSetting;

/**
 *
 * @author jorge
 */
public enum EnumSettingsBase {
    
    //TODO fix the fucking bug

    COMPANY_NAME(new AppSetting("COMPANY_NAME", "Nombre de su empresa", "", "app", "input", 1, "", true)),
    HELPDESK_TITLE(new AppSetting("HELPDESK_TITLE", "Titulo Helpdesk", "Itcs HelpDesk", "app", "input", 2, "", true)),
//    COMPANY_LOGO_ID_ATTACHMENT(new AppSetting("COMPANY_LOGO_ID_ATTACHMENT", "Logo de su empresa", "0", "app", "inputfile", 3, "Logo a mostrar en la pagina del cliente.", false)),
    SHOW_COMPANY_LOGO(new AppSetting("SHOW_COMPANY_LOGO", "Mostrar el logo", "true", "app", "booleanchoice", 4, "", true)),
    COMPANY_LOGO_SIZE(new AppSetting("COMPANY_LOGO_SIZE", "Tamaño logo", "100", "app", "input", 4, "", false)),
    DEFAULT_THEME(new AppSetting("DEFAULT_THEME", "Tema por defecto", "itcs-theme", "app", "theme", 5, "", false)),
    PORTAL_WEB_URL_CASO(new AppSetting("PORTAL_WEB_URL_CASO", "Link al caso (clientes)", "", "app", "input", 6, "", true)),
    COMPANY_HELPDESK_SITE_URL(new AppSetting("COMPANY_HELPDESK_SITE_URL", "Url Sitio Web helpdesk", "", "app", "input", 7, "", true)),
    PRODUCT_DESCRIPTION(new AppSetting("PRODUCT_DESCRIPTION", "Producto -> Nombre", "", "app", "input", 8, "", true)),
    PRODUCT_COMP_DESCRIPTION(new AppSetting("PRODUCT_COMP_DESCRIPTION", "Producto -> Nombre Componente", "", "app", "input", 9, "", true)),
    PRODUCT_SUBCOMP_DESCRIPTION(new AppSetting("PRODUCT_SUBCOMP_DESCRIPTION", "Producto -> Nombre Sub Componente", "", "app", "input", 10, "", true)),
    SALUDO_CLIENTE_HOMBRE(new AppSetting("SALUDO_CLIENTE_HOMBRE", "Saludo al cliente (sexo masculino)", "Estimado", "app", "input", 11, "", true)),
    SALUDO_CLIENTE_MUJER(new AppSetting("SALUDO_CLIENTE_MUJER", "Saludo al cliente (sexo femenino)", "Estimada", "app", "input", 12, "", true)),
    SALUDO_CLIENTE_UNKNOWN(new AppSetting("SALUDO_CLIENTE_UNKNOWN", "Saludo al cliente (sexo desconocido)", "Estimad@", "app", "input", 13, "", true)),
    SEND_NOTIFICATION_ON_TRANSFER(new AppSetting("SEND_NOTIFICATION_ON_TRANSFER", "Notificar cuando se asigne un caso", "true", "app", "booleanchoice", 14, "", true)),
    NOTIFICATION_SUBJECT_TEXT(new AppSetting("NOTIFICATION_SUBJECT_TEXT", "Asunto Nofiticación de su empresa", "Se le ha asignado el caso #[${NumeroCaso}].", "app", "input", 15, "", true)),
    NOTIFICATION_BODY_TEXT(new AppSetting("NOTIFICATION_BODY_TEXT", "Cuerpo mensaje Nofiticación",
    "Estimado ${NombreAgente}<br/>,"
    + "Le notificamos que le han asignado el caso N°#[${NumeroCaso}] para su pronta atención. Para poder revisar el caso acceda a la plataforma de godesk.cl.", "app", "inputhtml", 16, "", true)),
    
    NOTIFICATION_UPDATE_CLIENT_SUBJECT_TEXT(new AppSetting("NOTIFICATION_UPDATE_CLIENT_SUBJECT_TEXT", "Asunto Nofiticación al cliente", "su caso #[${NumeroCaso}] ha sido actualizado.", "app", "input", 17, "", true)),
    NOTIFICATION_UPDATE_CLIENT_BODY_TEXT(new AppSetting("NOTIFICATION_UPDATE_CLIENT_BODY_TEXT", "Cuerpo mensaje Nofiticación Cliente",
    "Estimado ${NombreCliente}<br/>,"
    + "Le notificamos que su caso N°#[${NumeroCaso}] ha sido actualizado. Para ver la actualización favor acceder a la plataforma godesk.", "app", "inputhtml", 18, "", true)),
    
    DEBUG_ENABLED(new AppSetting("DEBUG_ENABLED", "Debug", "false", "admin", "booleanchoice", 19, "Habilitar esta opcion para realizar un diagnostico de la ejecucion del sistema (Herramienta de diagnostico de problemas para Soporte).", true)),
    
    REAL_TIME_NOTIF_TO_AGENTS_ENABLED(new AppSetting("REAL_TIME_NOTIF_TO_AGENTS_ENABLED", "Notificaciones en tiempo real a agentes", "false", "app", "booleanchoice", 20, "Habilitar esta opcion si desea que los agentes reciban notificaciones cuando se asigne o modifique uno de sus casos.", true)),
    
    REAL_TIME_NOTIF_TO_CUSTOMER_ENABLED(new AppSetting("REAL_TIME_NOTIF_TO_CUSTOMER_ENABLED", "Notificaciones en tiempo real a los clientes", "false", "app", "booleanchoice", 21, "Habilitar esta opcion si desea que los clientes reciban notificaciones cuando se modifique uno de sus casos.", true)),
    SEND_GROUP_NOTIFICATION_ON_NEW_CASE(new AppSetting("SEND_GROUP_NOTIFICATION_ON_NEW_CASE", "Notificar a el/los Grupo(s)", "true", "app", "booleanchoice", 22, "Notificar a el/los Grupo(s) encargado(s) del producto cuando llegue un nuevo caso", true));
    
    private AppSetting appSetting;

    EnumSettingsBase(AppSetting appSetting) {
        this.appSetting = appSetting;
    }

    public AppSetting getAppSetting() {
        return this.appSetting;
    }

    @Override
    public String toString() {
        return appSetting.getSettingKey();
    }
}
