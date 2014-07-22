package com.itcs.helpdesk.persistence.entityenums;

import com.itcs.helpdesk.persistence.entities.Canal;

/**
 *
 * @author jorge
 */
public enum EnumCanal
{
    INTERNO(new Canal("INTERNO", "INTERNO", "Caso creado internamente o colaborativo creado por otro usuario")),
//    PAGINA(new Canal("WEB", "PAGINA WEB", "Caso recibido desde pagina web")),
    PORTAL(new Canal("PORTAL", "PORTAL WEB", "Caso recibido desde el portal web")),
    WEBSERVICE(new Canal("WEBSERVICE", "WEB SERVICE", "Caso recibido desde un cliente del Web Service.")),
    CHAT(new Canal("CHAT", "CHAT", "Caso recibido desde CHAT en pagina web")),
    PHONE(new Canal("PHONE", "PHONE", "Caso recibido telefonicamente.")),
    CORREO(new Canal("CORREO", "E-MAIL", "Caso recibido por e-mail"));
//    OTRO(new Canal("OTRO", "OTRO CANAL", "Caso recibido por otro canal"));

    private Canal canal;

    EnumCanal(Canal canal)
    {
        this.canal = canal;
    }

    public Canal getCanal()
    {
        return canal;
    }
}
