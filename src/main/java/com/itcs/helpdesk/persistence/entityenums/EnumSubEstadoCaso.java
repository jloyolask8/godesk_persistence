package com.itcs.helpdesk.persistence.entityenums;

import com.itcs.helpdesk.persistence.entities.SubEstadoCaso;

/**
 *
 * @author jorge
 */
public enum EnumSubEstadoCaso {

    INTERNO_NUEVO(new SubEstadoCaso(EnumTipoCaso.INTERNO.getTipoCaso(), "INTERNO_NUEVO", "Nuevo", EnumEstadoCaso.ABIERTO.getEstado(), "Estado inicial de un caso cuando ha sido creado.", false, "FFFFFF", "00aeed", true)),
    INTERNO_EN_PROCESO(new SubEstadoCaso(EnumTipoCaso.INTERNO.getTipoCaso(), "INTERNO_EN_PROCESO", "En Proceso", EnumEstadoCaso.ABIERTO.getEstado(), "El Agente esta trabajando en el caso. Ej: investigando o buscando una solución al caso.", false, "FFFFFF", "EB6420", false)),
    INTERNO_EN_CLIENTE(new SubEstadoCaso(EnumTipoCaso.INTERNO.getTipoCaso(), "INTERNO_EN_CLIENTE", "Esperando Cliente", EnumEstadoCaso.ABIERTO.getEstado(), "El Agente envía una posible solucion, o consulta o cualquier comentario que deja el caso en espera por respuesta del cliente.", false, "FFFFFF", "4BB14B", false)),
    INTERNO_REVISAR_ACTUALIZACION(new SubEstadoCaso(EnumTipoCaso.INTERNO.getTipoCaso(), "INTERNO_REVISAR_ACTUALIZACION", "Revisar Actualizacion", EnumEstadoCaso.ABIERTO.getEstado(), "El caso necesita que el Agente lo revise, ya que ha sido actualizado.", false, "000000", "FFC40D", false)),
    INTERNO_SOLUCIONADO(new SubEstadoCaso(EnumTipoCaso.INTERNO.getTipoCaso(), "INTERNO_SOLUCIONADO", "Solucionado", EnumEstadoCaso.CERRADO.getEstado(), "Caso solucionado. Respuesta enviada al cliente y cerrado.", false, "FFFFFF", "563D7C", false)),
    INTERNO_INVALIDO(new SubEstadoCaso(EnumTipoCaso.INTERNO.getTipoCaso(), "INTERNO_INVALIDO", "Caso no valido", EnumEstadoCaso.CERRADO.getEstado(), "Caso no es un caso valido.", false, "FFFFFF", "B94A48", false)),
    INTERNO_ABANDONADO(new SubEstadoCaso(EnumTipoCaso.INTERNO.getTipoCaso(), "INTERNO_ABANDONADO", "Abandonado", EnumEstadoCaso.CERRADO.getEstado(), "Cliente crea un caso pero nunca mas responde preguntas o comentarios del agente.", false, "FFFFFF", "444444", false)),
    //
    CONTACTO_NUEVO(new SubEstadoCaso(EnumTipoCaso.CONTACTO.getTipoCaso(), "nuevoCaso", "Nuevo", EnumEstadoCaso.ABIERTO.getEstado(), "Estado inicial de un caso cuando ha sido creado.", false, "FFFFFF", "00aeed", true)),
    CONTACTO_EN_PROCESO(new SubEstadoCaso(EnumTipoCaso.CONTACTO.getTipoCaso(), "enProceso", "En Proceso", EnumEstadoCaso.ABIERTO.getEstado(), "El Agente esta trabajando en el caso. Ej: investigando o buscando una solución al caso.", false, "FFFFFF", "EB6420", false)),
    CONTACTO_EN_CLIENTE(new SubEstadoCaso(EnumTipoCaso.CONTACTO.getTipoCaso(), "Esperando", "Esperando Cliente", EnumEstadoCaso.ABIERTO.getEstado(), "El Agente envía una posible solucion, o consulta o cualquier comentario que deja el caso en espera por respuesta del cliente.", false, "FFFFFF", "4BB14B", false)),
    CONTACTO_REVISAR_ACTUALIZACION(new SubEstadoCaso(EnumTipoCaso.CONTACTO.getTipoCaso(), "reviewUpdate", "Revisar Actualizacion", EnumEstadoCaso.ABIERTO.getEstado(), "El caso necesita que el Agente lo revise, ya que ha sido actualizado.", false, "000000", "FFC40D", false)),
    CONTACTO_SOLUCIONADO(new SubEstadoCaso(EnumTipoCaso.CONTACTO.getTipoCaso(), "solucionado", "Solucionado", EnumEstadoCaso.CERRADO.getEstado(), "Caso solucionado. Respuesta enviada al cliente y cerrado.", false, "FFFFFF", "563D7C", false)),
    CONTACTO_INVALIDO(new SubEstadoCaso(EnumTipoCaso.CONTACTO.getTipoCaso(), "invalido", "Caso no valido", EnumEstadoCaso.CERRADO.getEstado(), "Caso no es un caso valido.", false, "FFFFFF", "B94A48", false)),
    CONTACTO_ABANDONADO(new SubEstadoCaso(EnumTipoCaso.CONTACTO.getTipoCaso(), "Abandonado", "Abandonado", EnumEstadoCaso.CERRADO.getEstado(), "Cliente crea un caso pero nunca mas responde preguntas o comentarios del agente.", false, "FFFFFF", "444444", false)),
    //
    POSTVENTA_NUEVO(new SubEstadoCaso(EnumTipoCaso.POSTVENTA.getTipoCaso(), "POSTVENTA_NUEVO", "Nuevo", EnumEstadoCaso.ABIERTO.getEstado(), "Estado inicial de un caso cuando ha sido creado.", false, "FFFFFF", "00aeed", true)),
    POSTVENTA_EN_PROCESO(new SubEstadoCaso(EnumTipoCaso.POSTVENTA.getTipoCaso(), "POSTVENTA_EN_PROCESO", "En Proceso", EnumEstadoCaso.ABIERTO.getEstado(), "El Agente esta trabajando en el caso. Ej: investigando o buscando una solución al caso.", false, "FFFFFF", "EB6420", false)),
    POSTVENTA_EN_CLIENTE(new SubEstadoCaso(EnumTipoCaso.POSTVENTA.getTipoCaso(), "POSTVENTA_EN_CLIENTE", "Esperando Cliente", EnumEstadoCaso.ABIERTO.getEstado(), "El Agente envía una posible solucion, o consulta o cualquier comentario que deja el caso en espera por respuesta del cliente.", false, "FFFFFF", "4BB14B", false)),
    POSTVENTA_REVISAR_ACTUALIZACION(new SubEstadoCaso(EnumTipoCaso.POSTVENTA.getTipoCaso(), "POSTVENTA_REVISAR_ACTUALIZACION", "Revisar Actualizacion", EnumEstadoCaso.ABIERTO.getEstado(), "El caso necesita que el Agente lo revise, ya que ha sido actualizado.", false, "000000", "FFC40D", false)),
    POSTVENTA_SOLUCIONADO(new SubEstadoCaso(EnumTipoCaso.POSTVENTA.getTipoCaso(), "POSTVENTA_SOLUCIONADO", "Solucionado", EnumEstadoCaso.CERRADO.getEstado(), "Caso solucionado. Respuesta enviada al cliente y cerrado.", false, "FFFFFF", "563D7C", false)),
    POSTVENTA_INVALIDO(new SubEstadoCaso(EnumTipoCaso.POSTVENTA.getTipoCaso(), "POSTVENTA_INVALIDO", "Caso no valido", EnumEstadoCaso.CERRADO.getEstado(), "Caso no es un caso valido.", false, "FFFFFF", "B94A48", false)),
    POSTVENTA_ABANDONADO(new SubEstadoCaso(EnumTipoCaso.POSTVENTA.getTipoCaso(), "POSTVENTA_ABANDONADO", "Abandonado", EnumEstadoCaso.CERRADO.getEstado(), "Cliente crea un caso pero nunca mas responde preguntas o comentarios del agente.", false, "FFFFFF", "444444", false)),
    //
    COTIZACION_NUEVO(new SubEstadoCaso(EnumTipoCaso.COTIZACION.getTipoCaso(), "COTIZACION_NUEVO", "Nuevo", EnumEstadoCaso.ABIERTO.getEstado(), "Estado inicial de un caso cuando ha sido creado.", false, "FFFFFF", "00aeed", true)),
    COTIZACION_EN_PROCESO(new SubEstadoCaso(EnumTipoCaso.COTIZACION.getTipoCaso(), "COTIZACION_EN_PROCESO", "En Proceso", EnumEstadoCaso.ABIERTO.getEstado(), "El Agente esta trabajando en el caso. Ej: investigando o buscando una solución al caso.", false, "FFFFFF", "EB6420", false)),
    COTIZACION_EN_CLIENTE(new SubEstadoCaso(EnumTipoCaso.COTIZACION.getTipoCaso(), "COTIZACION_EN_CLIENTE", "Esperando Cliente", EnumEstadoCaso.ABIERTO.getEstado(), "El Agente envía una posible solucion, o consulta o cualquier comentario que deja el caso en espera por respuesta del cliente.", false, "FFFFFF", "4BB14B", false)),
    COTIZACION_REVISAR_ACTUALIZACION(new SubEstadoCaso(EnumTipoCaso.COTIZACION.getTipoCaso(), "COTIZACION_REVISAR_ACTUALIZACION", "Revisar Actualizacion", EnumEstadoCaso.ABIERTO.getEstado(), "El caso necesita que el Agente lo revise, ya que ha sido actualizado.", false, "000000", "FFC40D", false)),
    COTIZACION_SOLUCIONADO(new SubEstadoCaso(EnumTipoCaso.COTIZACION.getTipoCaso(), "COTIZACION_SOLUCIONADO", "Solucionado", EnumEstadoCaso.CERRADO.getEstado(), "Caso solucionado. Respuesta enviada al cliente y cerrado.", false, "FFFFFF", "563D7C", false)),
    COTIZACION_INVALIDO(new SubEstadoCaso(EnumTipoCaso.COTIZACION.getTipoCaso(), "COTIZACION_INVALIDO", "Caso no valido", EnumEstadoCaso.CERRADO.getEstado(), "Caso no es un caso valido.", false, "FFFFFF", "B94A48", false)),
    COTIZACION_ABANDONADO(new SubEstadoCaso(EnumTipoCaso.COTIZACION.getTipoCaso(), "COTIZACION_ABANDONADO", "Abandonado", EnumEstadoCaso.CERRADO.getEstado(), "Cliente crea un caso pero nunca mas responde preguntas o comentarios del agente.", false, "FFFFFF", "444444", false)),
    //
    PREVENTA_PENDIENTE(new SubEstadoCaso(EnumTipoCaso.PREVENTA.getTipoCaso(), "PREVENTA_PENDIENTE", "Pendiente de Contacto", EnumEstadoCaso.ABIERTO.getEstado(), "Estado inicial de la preventa, agente debe ponerse en contacto dentro de los plazos del SLA.", false, "FFFFFF", "00aeed", true)),
    PREVENTA_EVALUACION(new SubEstadoCaso(EnumTipoCaso.PREVENTA.getTipoCaso(), "PREVENTA_EVALUACION", "Evaluación", EnumEstadoCaso.ABIERTO.getEstado(), "El Agente esta trabajando en el caso. Ej: investigando o buscando una solución al caso.", false, "FFFFFF", "EB6420", false)),
    PREVENTA_EN_CLIENTE(new SubEstadoCaso(EnumTipoCaso.PREVENTA.getTipoCaso(), "PREVENTA_EN_CLIENTE", "Esperando Respuesta del Cliente", EnumEstadoCaso.ABIERTO.getEstado(), "El Agente envía una posible solucion, o consulta o cualquier comentario que deja el caso en espera por respuesta del cliente.", false, "FFFFFF", "4BB14B", false)),
    PREVENTA_REVISAR_ACTUALIZACION(new SubEstadoCaso(EnumTipoCaso.PREVENTA.getTipoCaso(), "PREVENTA_REVISAR_ACTUALIZACION", "Revisar Actualizacion", EnumEstadoCaso.ABIERTO.getEstado(), "El caso necesita que el Agente lo revise, ya que ha sido actualizado.", false, "000000", "FFC40D", false)),
    PREVENTA_PROXIMO_A_CIERRE(new SubEstadoCaso(EnumTipoCaso.PREVENTA.getTipoCaso(), "PREVENTA_PROXIMO_A_CIERRE", "Próximo a Cierre", EnumEstadoCaso.ABIERTO.getEstado(), "Venta Próxima a cierre.", false, "FFFFFF", "EB6420", false)),
    PREVENTA_COMPRO(new SubEstadoCaso(EnumTipoCaso.PREVENTA.getTipoCaso(), "PREVENTA_COMPRO", "Compró", EnumEstadoCaso.CERRADO.getEstado(), "Cliente compra exitosamente.", false, "FFFFFF", "563D7C", false)),
    PREVENTA_COMPRO_A_COMPETENCIA(new SubEstadoCaso(EnumTipoCaso.PREVENTA.getTipoCaso(), "PREVENTA_COMPRO_A_COMPETENCIA", "Compró en la competencia", EnumEstadoCaso.CERRADO.getEstado(), "Cliente desiste de comprar con nosotros y compra en la competencia", false, "FFFFFF", "B94A48", false)),
    PREVENTA_NOAPLICA(new SubEstadoCaso(EnumTipoCaso.PREVENTA.getTipoCaso(), "PREVENTA_NOAPLICA", "No cumple Requisitos de compra", EnumEstadoCaso.CERRADO.getEstado(), "Cliente no cumple los requisitos minimos de compra", false, "FFFFFF", "555555", false)),
    PREVENTA_ABANDONADO(new SubEstadoCaso(EnumTipoCaso.COTIZACION.getTipoCaso(), "PREVENTA_ABANDONADO", "Sin Respuesta del Cliente", EnumEstadoCaso.CERRADO.getEstado(), "Cliente crea un caso pero nunca mas responde preguntas o comentarios del agente.", false, "FFFFFF", "444444", false));
    private SubEstadoCaso subestado;

    EnumSubEstadoCaso(SubEstadoCaso subestado) {
        this.subestado = subestado;
    }

    public SubEstadoCaso getSubEstado() {
        return subestado;
    }
}
