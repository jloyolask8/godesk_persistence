--
-- PostgreSQL database dump
--

-- Dumped from database version 9.3.4
-- Dumped by pg_dump version 9.3.4
-- Started on 2014-07-21 20:40:04 CLT

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

--
-- TOC entry 6 (class 2615 OID 25691)
-- Name: tenant1; Type: SCHEMA; Schema: -; Owner: -
--

CREATE SCHEMA tenant1;


--
-- TOC entry 256 (class 3079 OID 11789)
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: -
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- TOC entry 2651 (class 0 OID 0)
-- Dependencies: 256
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: -
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- TOC entry 250 (class 1259 OID 49744)
-- Name: qrtz_blob_triggers; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE qrtz_blob_triggers (
    sched_name character varying(120) NOT NULL,
    trigger_name character varying(200) NOT NULL,
    trigger_group character varying(200) NOT NULL,
    blob_data bytea
);


--
-- TOC entry 251 (class 1259 OID 49757)
-- Name: qrtz_calendars; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE qrtz_calendars (
    sched_name character varying(120) NOT NULL,
    calendar_name character varying(200) NOT NULL,
    calendar bytea NOT NULL
);


--
-- TOC entry 248 (class 1259 OID 49718)
-- Name: qrtz_cron_triggers; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE qrtz_cron_triggers (
    sched_name character varying(120) NOT NULL,
    trigger_name character varying(200) NOT NULL,
    trigger_group character varying(200) NOT NULL,
    cron_expression character varying(120) NOT NULL,
    time_zone_id character varying(80)
);


--
-- TOC entry 253 (class 1259 OID 49770)
-- Name: qrtz_fired_triggers; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE qrtz_fired_triggers (
    sched_name character varying(120) NOT NULL,
    entry_id character varying(95) NOT NULL,
    trigger_name character varying(200) NOT NULL,
    trigger_group character varying(200) NOT NULL,
    instance_name character varying(200) NOT NULL,
    fired_time bigint NOT NULL,
    sched_time bigint NOT NULL,
    priority integer NOT NULL,
    state character varying(16) NOT NULL,
    job_name character varying(200),
    job_group character varying(200),
    is_nonconcurrent boolean,
    requests_recovery boolean
);


--
-- TOC entry 245 (class 1259 OID 49684)
-- Name: qrtz_job_details; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE qrtz_job_details (
    sched_name character varying(120) NOT NULL,
    job_name character varying(200) NOT NULL,
    job_group character varying(200) NOT NULL,
    description character varying(250),
    job_class_name character varying(250) NOT NULL,
    is_durable boolean NOT NULL,
    is_nonconcurrent boolean NOT NULL,
    is_update_data boolean NOT NULL,
    requests_recovery boolean NOT NULL,
    job_data bytea
);


--
-- TOC entry 255 (class 1259 OID 49783)
-- Name: qrtz_locks; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE qrtz_locks (
    sched_name character varying(120) NOT NULL,
    lock_name character varying(40) NOT NULL
);


--
-- TOC entry 252 (class 1259 OID 49765)
-- Name: qrtz_paused_trigger_grps; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE qrtz_paused_trigger_grps (
    sched_name character varying(120) NOT NULL,
    trigger_group character varying(200) NOT NULL
);


--
-- TOC entry 254 (class 1259 OID 49778)
-- Name: qrtz_scheduler_state; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE qrtz_scheduler_state (
    sched_name character varying(120) NOT NULL,
    instance_name character varying(200) NOT NULL,
    last_checkin_time bigint NOT NULL,
    checkin_interval bigint NOT NULL
);


--
-- TOC entry 247 (class 1259 OID 49705)
-- Name: qrtz_simple_triggers; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE qrtz_simple_triggers (
    sched_name character varying(120) NOT NULL,
    trigger_name character varying(200) NOT NULL,
    trigger_group character varying(200) NOT NULL,
    repeat_count bigint NOT NULL,
    repeat_interval bigint NOT NULL,
    times_triggered bigint NOT NULL
);


--
-- TOC entry 249 (class 1259 OID 49731)
-- Name: qrtz_simprop_triggers; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE qrtz_simprop_triggers (
    sched_name character varying(120) NOT NULL,
    trigger_name character varying(200) NOT NULL,
    trigger_group character varying(200) NOT NULL,
    str_prop_1 character varying(512),
    str_prop_2 character varying(512),
    str_prop_3 character varying(512),
    int_prop_1 integer,
    int_prop_2 integer,
    long_prop_1 bigint,
    long_prop_2 bigint,
    dec_prop_1 numeric(13,4),
    dec_prop_2 numeric(13,4),
    bool_prop_1 boolean,
    bool_prop_2 boolean
);


--
-- TOC entry 246 (class 1259 OID 49692)
-- Name: qrtz_triggers; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE qrtz_triggers (
    sched_name character varying(120) NOT NULL,
    trigger_name character varying(200) NOT NULL,
    trigger_group character varying(200) NOT NULL,
    job_name character varying(200) NOT NULL,
    job_group character varying(200) NOT NULL,
    description character varying(250),
    next_fire_time bigint,
    prev_fire_time bigint,
    priority integer,
    trigger_state character varying(16) NOT NULL,
    trigger_type character varying(8) NOT NULL,
    start_time bigint NOT NULL,
    end_time bigint,
    calendar_name character varying(200),
    misfire_instr smallint,
    job_data bytea
);


SET search_path = tenant1, pg_catalog;

--
-- TOC entry 171 (class 1259 OID 25692)
-- Name: accion; Type: TABLE; Schema: tenant1; Owner: -; Tablespace: 
--

CREATE TABLE accion (
    id_accion integer NOT NULL,
    parametros text,
    id_trigger character varying(40) NOT NULL,
    id_tipo_accion character varying(64) NOT NULL
);


--
-- TOC entry 172 (class 1259 OID 25698)
-- Name: accion_id_accion_seq; Type: SEQUENCE; Schema: tenant1; Owner: -
--

CREATE SEQUENCE accion_id_accion_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 2652 (class 0 OID 0)
-- Dependencies: 172
-- Name: accion_id_accion_seq; Type: SEQUENCE OWNED BY; Schema: tenant1; Owner: -
--

ALTER SEQUENCE accion_id_accion_seq OWNED BY accion.id_accion;


--
-- TOC entry 173 (class 1259 OID 25700)
-- Name: app_setting; Type: TABLE; Schema: tenant1; Owner: -; Tablespace: 
--

CREATE TABLE app_setting (
    setting_key character varying(64) NOT NULL,
    setting_value text,
    grupo character varying(40) NOT NULL,
    descripcion text,
    type character varying(64) NOT NULL,
    order_view integer NOT NULL,
    required boolean DEFAULT false NOT NULL,
    label character varying(64) DEFAULT 'Label'::character varying NOT NULL
);


--
-- TOC entry 174 (class 1259 OID 25708)
-- Name: archivo; Type: TABLE; Schema: tenant1; Owner: -; Tablespace: 
--

CREATE TABLE archivo (
    id_attachment integer NOT NULL,
    archivo bytea NOT NULL,
    content_type character varying(1000),
    format character varying(200),
    file_name character varying(2000)
);


--
-- TOC entry 175 (class 1259 OID 25714)
-- Name: area; Type: TABLE; Schema: tenant1; Owner: -; Tablespace: 
--

CREATE TABLE area (
    id_area character varying(40) NOT NULL,
    nombre character varying(40),
    descripcion text,
    editable boolean DEFAULT true NOT NULL,
    mail_smtp_host character varying(64),
    mail_smtp_port smallint,
    mail_smtp_user character varying(64),
    mail_smtp_password text,
    mail_smtp_from character varying(64),
    mail_smtp_fromname character varying(64),
    mail_smtp_auth boolean DEFAULT false,
    mail_smtp_socket_factory_port smallint,
    mail_smtp_connectiontimeout integer DEFAULT 0,
    mail_smtp_timeout integer DEFAULT 0,
    mail_server_type character varying(64),
    mail_transport_protocol character varying(64),
    mail_store_protocol character varying(64),
    mail_transport_tls boolean DEFAULT false,
    mail_use_jndi boolean DEFAULT false,
    mail_session_jndiname character varying(64) DEFAULT 'jdbc/'::character varying,
    mail_inbound_host character varying(64),
    mail_inbound_port smallint DEFAULT 0,
    mail_inbound_user character varying(64),
    mail_inbound_password text,
    mail_inbound_ssl_enabled boolean DEFAULT false,
    email_enabled boolean DEFAULT false,
    email_frecuencia integer DEFAULT 0,
    email_acusederecibo boolean DEFAULT false,
    subject_resp_automatica text,
    texto_resp_automatica text,
    texto_resp_caso text,
    mail_debug boolean DEFAULT false,
    dominio_exchange_salida character varying(64),
    dominio_exchange_inbound character varying(64),
    mail_server_type_salida character varying(64) DEFAULT 'smtp'::character varying,
    mail_smtp_ssl_enable boolean DEFAULT false,
    mail_smtp_encr_type character varying(64) DEFAULT 'none'::character varying NOT NULL
);


--
-- TOC entry 176 (class 1259 OID 25736)
-- Name: attachment; Type: TABLE; Schema: tenant1; Owner: -; Tablespace: 
--

CREATE TABLE attachment (
    id_attachment integer NOT NULL,
    id_caso bigint,
    nombre_archivo character varying(2000) NOT NULL,
    mime_type character varying(200),
    en_respuesta boolean,
    contentid character varying(200),
    file_size bigint
);


--
-- TOC entry 177 (class 1259 OID 25742)
-- Name: attachment_id_attachment_seq; Type: SEQUENCE; Schema: tenant1; Owner: -
--

CREATE SEQUENCE attachment_id_attachment_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 2653 (class 0 OID 0)
-- Dependencies: 177
-- Name: attachment_id_attachment_seq; Type: SEQUENCE OWNED BY; Schema: tenant1; Owner: -
--

ALTER SEQUENCE attachment_id_attachment_seq OWNED BY attachment.id_attachment;


--
-- TOC entry 178 (class 1259 OID 25744)
-- Name: audit_log; Type: TABLE; Schema: tenant1; Owner: -; Tablespace: 
--

CREATE TABLE audit_log (
    id_log integer NOT NULL,
    tabla character varying(100),
    campo character varying(100),
    old_value text,
    new_value text NOT NULL,
    fecha timestamp without time zone NOT NULL,
    id_user character varying(20) NOT NULL,
    id_caso bigint NOT NULL,
    owner character varying(20)
);


--
-- TOC entry 179 (class 1259 OID 25750)
-- Name: audit_log_id_log_seq; Type: SEQUENCE; Schema: tenant1; Owner: -
--

CREATE SEQUENCE audit_log_id_log_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 2654 (class 0 OID 0)
-- Dependencies: 179
-- Name: audit_log_id_log_seq; Type: SEQUENCE OWNED BY; Schema: tenant1; Owner: -
--

ALTER SEQUENCE audit_log_id_log_seq OWNED BY audit_log.id_log;


--
-- TOC entry 180 (class 1259 OID 25752)
-- Name: black_list_email; Type: TABLE; Schema: tenant1; Owner: -; Tablespace: 
--

CREATE TABLE black_list_email (
    email_address character varying(500) NOT NULL,
    description text
);


--
-- TOC entry 181 (class 1259 OID 25758)
-- Name: campo_comp_caso; Type: TABLE; Schema: tenant1; Owner: -; Tablespace: 
--

CREATE TABLE campo_comp_caso (
    id_campo character varying(40) NOT NULL,
    tipo character varying(255),
    nombre_tabla_valores character varying(255),
    nombre_col_valor character varying(255),
    descripcioncampo character varying(255),
    field_type_id character varying(40),
    label character varying(64) DEFAULT 'Undefined_Field_Label'::character varying NOT NULL,
    id_campo_full_path character varying(128) DEFAULT 'path.to.property'::character varying NOT NULL,
    tipo_campo_full_path character varying(256) DEFAULT 'java.lang.String'::character varying NOT NULL,
    base_class character varying(2000) DEFAULT 'Class'::character varying NOT NULL
);


--
-- TOC entry 182 (class 1259 OID 25768)
-- Name: canal; Type: TABLE; Schema: tenant1; Owner: -; Tablespace: 
--

CREATE TABLE canal (
    id_canal character varying(20) NOT NULL,
    nombre character varying(40),
    descripcion character varying(200)
);


--
-- TOC entry 183 (class 1259 OID 25771)
-- Name: caso; Type: TABLE; Schema: tenant1; Owner: -; Tablespace: 
--

CREATE TABLE caso (
    id_caso integer NOT NULL,
    tema character varying(1000) NOT NULL,
    descripcion text,
    tranfer_count integer,
    id_canal character varying(20),
    owner character varying(20),
    init_response_due timestamp without time zone,
    next_response_due timestamp without time zone,
    estado_escalacion character varying(40),
    es_preg_conocida boolean,
    fecha_creacion timestamp without time zone,
    fecha_modif timestamp without time zone,
    fecha_cierre timestamp without time zone,
    fecha_respuesta timestamp without time zone,
    respuesta text,
    id_prioridad character varying(40),
    id_caso_padre bigint,
    id_estado character varying(40),
    id_sub_estado character varying(40),
    es_prioritario boolean,
    id_categoria integer,
    estado_alerta integer,
    email_cliente character varying(80),
    id_producto character varying(64),
    id_componente character varying(64),
    id_sub_componente character varying(64),
    revisar_actualizacion boolean DEFAULT false,
    id_area character varying(40) DEFAULT 'DEFAULT_AREA'::character varying NOT NULL,
    tipo_caso character varying(64) NOT NULL,
    id_modelo character varying(200),
    descripcion_txt text,
    textsearchable_index_col tsvector
);


--
-- TOC entry 184 (class 1259 OID 25779)
-- Name: caso_custom_field; Type: TABLE; Schema: tenant1; Owner: -; Tablespace: 
--

CREATE TABLE caso_custom_field (
    id_caso integer NOT NULL,
    field_key character varying(40) NOT NULL,
    entity character(40) NOT NULL,
    valor text,
    valor2 text
);


--
-- TOC entry 185 (class 1259 OID 25785)
-- Name: caso_documento; Type: TABLE; Schema: tenant1; Owner: -; Tablespace: 
--

CREATE TABLE caso_documento (
    id_caso bigint NOT NULL,
    id_documento integer NOT NULL
);


--
-- TOC entry 186 (class 1259 OID 25788)
-- Name: caso_id_caso_seq; Type: SEQUENCE; Schema: tenant1; Owner: -
--

CREATE SEQUENCE caso_id_caso_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 2655 (class 0 OID 0)
-- Dependencies: 186
-- Name: caso_id_caso_seq; Type: SEQUENCE OWNED BY; Schema: tenant1; Owner: -
--

ALTER SEQUENCE caso_id_caso_seq OWNED BY caso.id_caso;


--
-- TOC entry 187 (class 1259 OID 25790)
-- Name: caso_relacionado; Type: TABLE; Schema: tenant1; Owner: -; Tablespace: 
--

CREATE TABLE caso_relacionado (
    id_caso_1 bigint NOT NULL,
    id_caso_2 bigint NOT NULL
);


--
-- TOC entry 188 (class 1259 OID 25793)
-- Name: categoria; Type: TABLE; Schema: tenant1; Owner: -; Tablespace: 
--

CREATE TABLE categoria (
    id_categoria integer NOT NULL,
    nombre character varying(40),
    id_categoria_padre integer,
    id_area character varying(40) NOT NULL,
    orden integer,
    editable boolean DEFAULT true NOT NULL
);


--
-- TOC entry 189 (class 1259 OID 25797)
-- Name: categoria_grupo; Type: TABLE; Schema: tenant1; Owner: -; Tablespace: 
--

CREATE TABLE categoria_grupo (
    id_categoria integer NOT NULL,
    id_grupo character varying(40) NOT NULL
);


--
-- TOC entry 190 (class 1259 OID 25800)
-- Name: categoria_sub_estado_caso; Type: TABLE; Schema: tenant1; Owner: -; Tablespace: 
--

CREATE TABLE categoria_sub_estado_caso (
    id_categoria integer NOT NULL,
    id_sub_estado character varying(40) NOT NULL
);


--
-- TOC entry 191 (class 1259 OID 25803)
-- Name: cliente; Type: TABLE; Schema: tenant1; Owner: -; Tablespace: 
--

CREATE TABLE cliente (
    id_cliente integer NOT NULL,
    rut character varying(40),
    nombres character varying(80),
    apellidos character varying(80),
    fono1 character varying(40),
    fono2 character varying(40),
    dir_particular character varying(400),
    dir_comercial character varying(400),
    theme character varying(40),
    sexo character varying(40) DEFAULT 'Desconocido'::character varying,
    CONSTRAINT check_sexo CHECK (((sexo)::text = ANY (ARRAY[('Hombre'::character varying)::text, ('Mujer'::character varying)::text, ('Desconocido'::character varying)::text])))
);


--
-- TOC entry 192 (class 1259 OID 25811)
-- Name: cliente_id_cliente_seq; Type: SEQUENCE; Schema: tenant1; Owner: -
--

CREATE SEQUENCE cliente_id_cliente_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 2656 (class 0 OID 0)
-- Dependencies: 192
-- Name: cliente_id_cliente_seq; Type: SEQUENCE OWNED BY; Schema: tenant1; Owner: -
--

ALTER SEQUENCE cliente_id_cliente_seq OWNED BY cliente.id_cliente;


--
-- TOC entry 193 (class 1259 OID 25813)
-- Name: clipping; Type: TABLE; Schema: tenant1; Owner: -; Tablespace: 
--

CREATE TABLE clipping (
    id_clipping integer NOT NULL,
    nombre text NOT NULL,
    texto text NOT NULL,
    id_clipping_padre integer,
    id_area character varying(40),
    id_grupo character varying(40),
    visible_to_all boolean DEFAULT true NOT NULL,
    orden integer,
    creada_por character varying(40),
    folder boolean
);


--
-- TOC entry 194 (class 1259 OID 25820)
-- Name: clipping_id_clipping_seq; Type: SEQUENCE; Schema: tenant1; Owner: -
--

CREATE SEQUENCE clipping_id_clipping_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 2657 (class 0 OID 0)
-- Dependencies: 194
-- Name: clipping_id_clipping_seq; Type: SEQUENCE OWNED BY; Schema: tenant1; Owner: -
--

ALTER SEQUENCE clipping_id_clipping_seq OWNED BY clipping.id_clipping;


--
-- TOC entry 195 (class 1259 OID 25822)
-- Name: componente; Type: TABLE; Schema: tenant1; Owner: -; Tablespace: 
--

CREATE TABLE componente (
    id_componente character varying(64) NOT NULL,
    nombre character varying(200),
    descripcion text,
    id_producto character varying(64)
);


--
-- TOC entry 196 (class 1259 OID 25828)
-- Name: condicion; Type: TABLE; Schema: tenant1; Owner: -; Tablespace: 
--

CREATE TABLE condicion (
    id_condicion integer NOT NULL,
    valor text,
    id_campo character varying(255),
    id_comparador character varying(2),
    id_trigger character varying(40) NOT NULL,
    valor2 text
);


--
-- TOC entry 197 (class 1259 OID 25834)
-- Name: condicion_id_condicion_seq; Type: SEQUENCE; Schema: tenant1; Owner: -
--

CREATE SEQUENCE condicion_id_condicion_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 2658 (class 0 OID 0)
-- Dependencies: 197
-- Name: condicion_id_condicion_seq; Type: SEQUENCE OWNED BY; Schema: tenant1; Owner: -
--

ALTER SEQUENCE condicion_id_condicion_seq OWNED BY condicion.id_condicion;


--
-- TOC entry 198 (class 1259 OID 25836)
-- Name: custom_field; Type: TABLE; Schema: tenant1; Owner: -; Tablespace: 
--

CREATE TABLE custom_field (
    field_key character varying(40) NOT NULL,
    entity character(40) NOT NULL,
    label character varying(40) NOT NULL,
    required boolean DEFAULT false NOT NULL,
    visible_to_customers boolean DEFAULT false NOT NULL,
    field_type_id character varying(40) NOT NULL,
    fecha_creacion timestamp without time zone,
    fecha_modif timestamp without time zone,
    list_options text
);


--
-- TOC entry 199 (class 1259 OID 25844)
-- Name: custom_field_tipo_accion; Type: TABLE; Schema: tenant1; Owner: -; Tablespace: 
--

CREATE TABLE custom_field_tipo_accion (
    field_key character varying(40) NOT NULL,
    entity character(40) NOT NULL,
    id_tipo_accion character varying(64) NOT NULL
);


--
-- TOC entry 200 (class 1259 OID 25847)
-- Name: custom_field_tipo_caso; Type: TABLE; Schema: tenant1; Owner: -; Tablespace: 
--

CREATE TABLE custom_field_tipo_caso (
    field_key character varying(40) NOT NULL,
    entity character(40) NOT NULL,
    id_tipo_caso character varying(64) NOT NULL
);


--
-- TOC entry 201 (class 1259 OID 25850)
-- Name: documento; Type: TABLE; Schema: tenant1; Owner: -; Tablespace: 
--

CREATE TABLE documento (
    id_documento integer NOT NULL,
    problema text NOT NULL,
    causa text NOT NULL,
    solucion text,
    created_by character varying(20) NOT NULL,
    created_date date NOT NULL,
    updated_date date,
    visible boolean DEFAULT false
);


--
-- TOC entry 202 (class 1259 OID 25857)
-- Name: documento_id_documento_seq; Type: SEQUENCE; Schema: tenant1; Owner: -
--

CREATE SEQUENCE documento_id_documento_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 2659 (class 0 OID 0)
-- Dependencies: 202
-- Name: documento_id_documento_seq; Type: SEQUENCE OWNED BY; Schema: tenant1; Owner: -
--

ALTER SEQUENCE documento_id_documento_seq OWNED BY documento.id_documento;


--
-- TOC entry 203 (class 1259 OID 25859)
-- Name: email_cliente; Type: TABLE; Schema: tenant1; Owner: -; Tablespace: 
--

CREATE TABLE email_cliente (
    email_cliente character varying(80) NOT NULL,
    id_cliente integer,
    tipo_email character varying(40)
);


--
-- TOC entry 204 (class 1259 OID 25862)
-- Name: estado_caso; Type: TABLE; Schema: tenant1; Owner: -; Tablespace: 
--

CREATE TABLE estado_caso (
    id_estado character varying(40) NOT NULL,
    nombre character varying(60) NOT NULL,
    descripcion character varying(200)
);


--
-- TOC entry 205 (class 1259 OID 25865)
-- Name: etiqueta; Type: TABLE; Schema: tenant1; Owner: -; Tablespace: 
--

CREATE TABLE etiqueta (
    tag_id character varying(64) NOT NULL,
    descripcion text,
    id_usuario_created_by character varying(20),
    owner character varying(20),
    color character varying(6)
);


--
-- TOC entry 206 (class 1259 OID 25871)
-- Name: etiqueta_caso; Type: TABLE; Schema: tenant1; Owner: -; Tablespace: 
--

CREATE TABLE etiqueta_caso (
    tag_id character varying(64) NOT NULL,
    id_caso integer NOT NULL
);


--
-- TOC entry 207 (class 1259 OID 25874)
-- Name: field_type; Type: TABLE; Schema: tenant1; Owner: -; Tablespace: 
--

CREATE TABLE field_type (
    field_type_id character varying(40) NOT NULL,
    name character varying(40) NOT NULL,
    description text,
    java_type character varying(40),
    is_custom_field boolean DEFAULT false
);


--
-- TOC entry 208 (class 1259 OID 25881)
-- Name: filtro_vista; Type: TABLE; Schema: tenant1; Owner: -; Tablespace: 
--

CREATE TABLE filtro_vista (
    id_filtro integer NOT NULL,
    valor text,
    id_vista integer,
    id_campo character varying(255),
    id_comparador character varying(2),
    valor2 text,
    visible_to_agents boolean DEFAULT true NOT NULL
);


--
-- TOC entry 209 (class 1259 OID 25888)
-- Name: filtros_vista_id_filtro_seq; Type: SEQUENCE; Schema: tenant1; Owner: -
--

CREATE SEQUENCE filtros_vista_id_filtro_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 2660 (class 0 OID 0)
-- Dependencies: 209
-- Name: filtros_vista_id_filtro_seq; Type: SEQUENCE OWNED BY; Schema: tenant1; Owner: -
--

ALTER SEQUENCE filtros_vista_id_filtro_seq OWNED BY filtro_vista.id_filtro;


--
-- TOC entry 210 (class 1259 OID 25890)
-- Name: funcion; Type: TABLE; Schema: tenant1; Owner: -; Tablespace: 
--

CREATE TABLE funcion (
    id_funcion integer NOT NULL,
    nombre character varying(40),
    descripcion text,
    outcome character varying(40)
);


--
-- TOC entry 211 (class 1259 OID 25896)
-- Name: grupo; Type: TABLE; Schema: tenant1; Owner: -; Tablespace: 
--

CREATE TABLE grupo (
    id_grupo character varying(40) NOT NULL,
    nombre character varying(40),
    descripcion text,
    id_area character varying(40) NOT NULL,
    editable boolean DEFAULT true NOT NULL
);


--
-- TOC entry 212 (class 1259 OID 25903)
-- Name: grupo_producto; Type: TABLE; Schema: tenant1; Owner: -; Tablespace: 
--

CREATE TABLE grupo_producto (
    id_producto character varying(64) NOT NULL,
    id_grupo character varying(40) NOT NULL
);


--
-- TOC entry 213 (class 1259 OID 25906)
-- Name: modelo_producto; Type: TABLE; Schema: tenant1; Owner: -; Tablespace: 
--

CREATE TABLE modelo_producto (
    id_modelo character varying(200) NOT NULL,
    nombre character varying(300) NOT NULL,
    descripcion text,
    id_producto character varying(64) NOT NULL,
    id_componente character varying(64)
);


--
-- TOC entry 214 (class 1259 OID 25912)
-- Name: nota; Type: TABLE; Schema: tenant1; Owner: -; Tablespace: 
--

CREATE TABLE nota (
    id_nota integer NOT NULL,
    id_caso bigint,
    creada_por character varying(20),
    texto text,
    fecha_creacion timestamp without time zone,
    fecha_modificacion date,
    visible boolean DEFAULT true,
    id_tipo_nota integer,
    labor_time numeric(8,0),
    enviado_por character varying(200),
    id_canal character varying(20)
);


--
-- TOC entry 215 (class 1259 OID 25919)
-- Name: nota_id_nota_seq; Type: SEQUENCE; Schema: tenant1; Owner: -
--

CREATE SEQUENCE nota_id_nota_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 2661 (class 0 OID 0)
-- Dependencies: 215
-- Name: nota_id_nota_seq; Type: SEQUENCE OWNED BY; Schema: tenant1; Owner: -
--

ALTER SEQUENCE nota_id_nota_seq OWNED BY nota.id_nota;


--
-- TOC entry 216 (class 1259 OID 25921)
-- Name: prioridad; Type: TABLE; Schema: tenant1; Owner: -; Tablespace: 
--

CREATE TABLE prioridad (
    id_prioridad character varying(40) NOT NULL,
    nombre character varying(40),
    descripcion text,
    sla_horas integer
);


--
-- TOC entry 217 (class 1259 OID 25927)
-- Name: producto; Type: TABLE; Schema: tenant1; Owner: -; Tablespace: 
--

CREATE TABLE producto (
    id_producto character varying(64) NOT NULL,
    nombre character varying(200),
    descripcion text
);


--
-- TOC entry 218 (class 1259 OID 25933)
-- Name: producto_contratado; Type: TABLE; Schema: tenant1; Owner: -; Tablespace: 
--

CREATE TABLE producto_contratado (
    id_cliente integer NOT NULL,
    id_producto character varying(64) NOT NULL,
    id_componente character varying(64) NOT NULL,
    id_sub_componente character varying(64) NOT NULL,
    vendedor character varying(64),
    fecha_compra date,
    observaciones text
);


--
-- TOC entry 219 (class 1259 OID 25939)
-- Name: regla_trigger; Type: TABLE; Schema: tenant1; Owner: -; Tablespace: 
--

CREATE TABLE regla_trigger (
    id_trigger character varying(40) NOT NULL,
    id_area character varying(40),
    desccripcion text,
    regla_activa boolean,
    evento character varying(40) DEFAULT 'CREATE'::character varying NOT NULL,
    orden integer DEFAULT 0 NOT NULL
);


--
-- TOC entry 220 (class 1259 OID 25947)
-- Name: regla_trigger_area; Type: TABLE; Schema: tenant1; Owner: -; Tablespace: 
--

CREATE TABLE regla_trigger_area (
    id_trigger character varying(40) NOT NULL,
    id_area character varying(40) NOT NULL
);


--
-- TOC entry 242 (class 1259 OID 47290)
-- Name: resource; Type: TABLE; Schema: tenant1; Owner: -; Tablespace: 
--

CREATE TABLE resource (
    id_resource integer NOT NULL,
    nombre character varying(200),
    descripcion text,
    tipo character varying(100)
);


--
-- TOC entry 241 (class 1259 OID 47288)
-- Name: resource_id_resource_seq; Type: SEQUENCE; Schema: tenant1; Owner: -
--

CREATE SEQUENCE resource_id_resource_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 2662 (class 0 OID 0)
-- Dependencies: 241
-- Name: resource_id_resource_seq; Type: SEQUENCE OWNED BY; Schema: tenant1; Owner: -
--

ALTER SEQUENCE resource_id_resource_seq OWNED BY resource.id_resource;


--
-- TOC entry 221 (class 1259 OID 25950)
-- Name: rol; Type: TABLE; Schema: tenant1; Owner: -; Tablespace: 
--

CREATE TABLE rol (
    id_rol character varying(40) NOT NULL,
    nombre character varying(40) NOT NULL,
    descripcion character varying(40),
    editable boolean DEFAULT true NOT NULL
);


--
-- TOC entry 222 (class 1259 OID 25954)
-- Name: rol_funcion; Type: TABLE; Schema: tenant1; Owner: -; Tablespace: 
--

CREATE TABLE rol_funcion (
    id_rol character varying(40) NOT NULL,
    id_funcion integer NOT NULL
);


--
-- TOC entry 223 (class 1259 OID 25957)
-- Name: schedule_event; Type: TABLE; Schema: tenant1; Owner: -; Tablespace: 
--

CREATE TABLE schedule_event (
    event_id integer NOT NULL,
    title character(64) NOT NULL,
    start_date timestamp without time zone NOT NULL,
    end_date timestamp without time zone,
    all_day boolean,
    id_caso integer,
    id_usuario character varying(20),
    fecha_creacion timestamp without time zone NOT NULL,
    notified_ok boolean,
    parametros_accion text,
    id_tipo_accion character varying(64),
    color_style_class character varying(40),
    email_invitados_csv text,
    public_event boolean DEFAULT true,
    lugar character varying(2000),
    descripcion text,
    execute_action boolean,
    quartz_job_id character varying(200)
);


--
-- TOC entry 224 (class 1259 OID 25963)
-- Name: schedule_event_event_id_seq; Type: SEQUENCE; Schema: tenant1; Owner: -
--

CREATE SEQUENCE schedule_event_event_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 2663 (class 0 OID 0)
-- Dependencies: 224
-- Name: schedule_event_event_id_seq; Type: SEQUENCE OWNED BY; Schema: tenant1; Owner: -
--

ALTER SEQUENCE schedule_event_event_id_seq OWNED BY schedule_event.event_id;


--
-- TOC entry 240 (class 1259 OID 47270)
-- Name: schedule_event_reminder; Type: TABLE; Schema: tenant1; Owner: -; Tablespace: 
--

CREATE TABLE schedule_event_reminder (
    id_reminder integer NOT NULL,
    reminder_type character varying(100),
    quantity_time integer NOT NULL,
    unit_of_time_in_minutes bigint,
    event_id bigint,
    quartz_job_id character varying(200),
    notified_ok boolean
);


--
-- TOC entry 239 (class 1259 OID 47268)
-- Name: schedule_event_reminder_id_reminder_seq; Type: SEQUENCE; Schema: tenant1; Owner: -
--

CREATE SEQUENCE schedule_event_reminder_id_reminder_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 2664 (class 0 OID 0)
-- Dependencies: 239
-- Name: schedule_event_reminder_id_reminder_seq; Type: SEQUENCE OWNED BY; Schema: tenant1; Owner: -
--

ALTER SEQUENCE schedule_event_reminder_id_reminder_seq OWNED BY schedule_event_reminder.id_reminder;


--
-- TOC entry 243 (class 1259 OID 47299)
-- Name: schedule_event_resource; Type: TABLE; Schema: tenant1; Owner: -; Tablespace: 
--

CREATE TABLE schedule_event_resource (
    event_id integer NOT NULL,
    id_resource integer NOT NULL
);


--
-- TOC entry 244 (class 1259 OID 47314)
-- Name: schedule_event_usuario; Type: TABLE; Schema: tenant1; Owner: -; Tablespace: 
--

CREATE TABLE schedule_event_usuario (
    id_usuario character varying(20) NOT NULL,
    event_id integer NOT NULL
);


--
-- TOC entry 225 (class 1259 OID 25965)
-- Name: sesiones; Type: TABLE; Schema: tenant1; Owner: -; Tablespace: 
--

CREATE TABLE sesiones (
    id_sesion integer NOT NULL,
    usado boolean NOT NULL,
    rut_usuario character varying(14) NOT NULL,
    fecha_ingreso date
);


--
-- TOC entry 226 (class 1259 OID 25968)
-- Name: sesiones_id_sesion_seq; Type: SEQUENCE; Schema: tenant1; Owner: -
--

CREATE SEQUENCE sesiones_id_sesion_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 2665 (class 0 OID 0)
-- Dependencies: 226
-- Name: sesiones_id_sesion_seq; Type: SEQUENCE OWNED BY; Schema: tenant1; Owner: -
--

ALTER SEQUENCE sesiones_id_sesion_seq OWNED BY sesiones.id_sesion;


--
-- TOC entry 227 (class 1259 OID 25970)
-- Name: sub_componente; Type: TABLE; Schema: tenant1; Owner: -; Tablespace: 
--

CREATE TABLE sub_componente (
    id_sub_componente character varying(64) NOT NULL,
    nombre character varying(200),
    descripcion text,
    id_componente character varying(64) NOT NULL,
    id_modelo character varying(200),
    id_producto character varying(64)
);


--
-- TOC entry 228 (class 1259 OID 25976)
-- Name: sub_estado_caso; Type: TABLE; Schema: tenant1; Owner: -; Tablespace: 
--

CREATE TABLE sub_estado_caso (
    id_sub_estado character varying(40) NOT NULL,
    nombre character varying(40) NOT NULL,
    descripcion text,
    id_estado character varying(40) NOT NULL,
    editable boolean DEFAULT true NOT NULL,
    font_color character varying(6) DEFAULT 'FFFFFF'::character varying NOT NULL,
    background_color character varying(6) DEFAULT '243971'::character varying NOT NULL,
    id_tipo_caso character varying(64),
    first boolean DEFAULT false NOT NULL
);


--
-- TOC entry 229 (class 1259 OID 25986)
-- Name: tipo_accion; Type: TABLE; Schema: tenant1; Owner: -; Tablespace: 
--

CREATE TABLE tipo_accion (
    id_tipo_accion character varying(64) NOT NULL,
    nombre character varying(64),
    descripcion text,
    implementation_class_name character varying(1000)
);


--
-- TOC entry 230 (class 1259 OID 25992)
-- Name: tipo_alerta; Type: TABLE; Schema: tenant1; Owner: -; Tablespace: 
--

CREATE TABLE tipo_alerta (
    idalerta integer NOT NULL,
    nombre text NOT NULL,
    descripcion text NOT NULL,
    style_class character varying(40)
);


--
-- TOC entry 231 (class 1259 OID 25998)
-- Name: tipo_caso; Type: TABLE; Schema: tenant1; Owner: -; Tablespace: 
--

CREATE TABLE tipo_caso (
    id_tipo_caso character varying(64) NOT NULL,
    nombre character varying(64) NOT NULL,
    descripcion text
);


--
-- TOC entry 232 (class 1259 OID 26004)
-- Name: tipo_comparacion; Type: TABLE; Schema: tenant1; Owner: -; Tablespace: 
--

CREATE TABLE tipo_comparacion (
    id_comparador character varying(2) NOT NULL,
    simbolo character varying(2),
    descripcion character varying(200),
    nombre character varying(64)
);


--
-- TOC entry 233 (class 1259 OID 26007)
-- Name: tipo_nota; Type: TABLE; Schema: tenant1; Owner: -; Tablespace: 
--

CREATE TABLE tipo_nota (
    id_tipo_nota integer NOT NULL,
    nombre character varying(100) NOT NULL,
    descripcion character varying(200)
);


--
-- TOC entry 234 (class 1259 OID 26010)
-- Name: user_rol; Type: TABLE; Schema: tenant1; Owner: -; Tablespace: 
--

CREATE TABLE user_rol (
    id_usuario character varying(20) NOT NULL,
    id_rol character varying(40) NOT NULL
);


--
-- TOC entry 235 (class 1259 OID 26013)
-- Name: usuario; Type: TABLE; Schema: tenant1; Owner: -; Tablespace: 
--

CREATE TABLE usuario (
    id_usuario character varying(20) NOT NULL,
    nombres character varying(40),
    apellidos character varying(40),
    email character varying(40),
    supervisor character varying(20),
    tel_fijo character varying(40),
    tel_movil character varying(40),
    activo boolean NOT NULL,
    pass character varying(32) NOT NULL,
    rut character varying(14) NOT NULL,
    editable boolean DEFAULT true NOT NULL,
    theme character(40),
    id_grupo character varying(40),
    page_layout_state text,
    notify_when_ticket_assigned boolean DEFAULT true,
    notify_when_new_ticket_in_group boolean DEFAULT true,
    notify_when_ticket_alert boolean DEFAULT true,
    notify_when_ticket_is_updated boolean DEFAULT true,
    email_notifications_enabled boolean DEFAULT true,
    template_theme character varying(40),
    main_menu_right boolean
);


--
-- TOC entry 236 (class 1259 OID 26020)
-- Name: usuario_grupo; Type: TABLE; Schema: tenant1; Owner: -; Tablespace: 
--

CREATE TABLE usuario_grupo (
    id_usuario character varying(20) NOT NULL,
    id_grupo character varying(40) NOT NULL
);


--
-- TOC entry 237 (class 1259 OID 26023)
-- Name: vista; Type: TABLE; Schema: tenant1; Owner: -; Tablespace: 
--

CREATE TABLE vista (
    id_vista integer NOT NULL,
    nombre character varying(40) NOT NULL,
    descripcion character varying(40),
    id_usuario_creada_por character varying(20) NOT NULL,
    id_area character varying(40),
    id_grupo character varying(40),
    visible_to_all boolean DEFAULT false NOT NULL,
    base_entity_type character varying(1000) DEFAULT 'class'::character varying NOT NULL
);


--
-- TOC entry 238 (class 1259 OID 26031)
-- Name: vista_casos_id_vista_seq; Type: SEQUENCE; Schema: tenant1; Owner: -
--

CREATE SEQUENCE vista_casos_id_vista_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 2666 (class 0 OID 0)
-- Dependencies: 238
-- Name: vista_casos_id_vista_seq; Type: SEQUENCE OWNED BY; Schema: tenant1; Owner: -
--

ALTER SEQUENCE vista_casos_id_vista_seq OWNED BY vista.id_vista;


--
-- TOC entry 2210 (class 2604 OID 26033)
-- Name: id_accion; Type: DEFAULT; Schema: tenant1; Owner: -
--

ALTER TABLE ONLY accion ALTER COLUMN id_accion SET DEFAULT nextval('accion_id_accion_seq'::regclass);


--
-- TOC entry 2229 (class 2604 OID 26034)
-- Name: id_attachment; Type: DEFAULT; Schema: tenant1; Owner: -
--

ALTER TABLE ONLY attachment ALTER COLUMN id_attachment SET DEFAULT nextval('attachment_id_attachment_seq'::regclass);


--
-- TOC entry 2230 (class 2604 OID 26035)
-- Name: id_log; Type: DEFAULT; Schema: tenant1; Owner: -
--

ALTER TABLE ONLY audit_log ALTER COLUMN id_log SET DEFAULT nextval('audit_log_id_log_seq'::regclass);


--
-- TOC entry 2237 (class 2604 OID 26036)
-- Name: id_caso; Type: DEFAULT; Schema: tenant1; Owner: -
--

ALTER TABLE ONLY caso ALTER COLUMN id_caso SET DEFAULT nextval('caso_id_caso_seq'::regclass);


--
-- TOC entry 2240 (class 2604 OID 26037)
-- Name: id_cliente; Type: DEFAULT; Schema: tenant1; Owner: -
--

ALTER TABLE ONLY cliente ALTER COLUMN id_cliente SET DEFAULT nextval('cliente_id_cliente_seq'::regclass);


--
-- TOC entry 2243 (class 2604 OID 26038)
-- Name: id_clipping; Type: DEFAULT; Schema: tenant1; Owner: -
--

ALTER TABLE ONLY clipping ALTER COLUMN id_clipping SET DEFAULT nextval('clipping_id_clipping_seq'::regclass);


--
-- TOC entry 2244 (class 2604 OID 26039)
-- Name: id_condicion; Type: DEFAULT; Schema: tenant1; Owner: -
--

ALTER TABLE ONLY condicion ALTER COLUMN id_condicion SET DEFAULT nextval('condicion_id_condicion_seq'::regclass);


--
-- TOC entry 2248 (class 2604 OID 26040)
-- Name: id_documento; Type: DEFAULT; Schema: tenant1; Owner: -
--

ALTER TABLE ONLY documento ALTER COLUMN id_documento SET DEFAULT nextval('documento_id_documento_seq'::regclass);


--
-- TOC entry 2251 (class 2604 OID 26041)
-- Name: id_filtro; Type: DEFAULT; Schema: tenant1; Owner: -
--

ALTER TABLE ONLY filtro_vista ALTER COLUMN id_filtro SET DEFAULT nextval('filtros_vista_id_filtro_seq'::regclass);


--
-- TOC entry 2254 (class 2604 OID 26042)
-- Name: id_nota; Type: DEFAULT; Schema: tenant1; Owner: -
--

ALTER TABLE ONLY nota ALTER COLUMN id_nota SET DEFAULT nextval('nota_id_nota_seq'::regclass);


--
-- TOC entry 2275 (class 2604 OID 47293)
-- Name: id_resource; Type: DEFAULT; Schema: tenant1; Owner: -
--

ALTER TABLE ONLY resource ALTER COLUMN id_resource SET DEFAULT nextval('resource_id_resource_seq'::regclass);


--
-- TOC entry 2258 (class 2604 OID 26043)
-- Name: event_id; Type: DEFAULT; Schema: tenant1; Owner: -
--

ALTER TABLE ONLY schedule_event ALTER COLUMN event_id SET DEFAULT nextval('schedule_event_event_id_seq'::regclass);


--
-- TOC entry 2274 (class 2604 OID 47273)
-- Name: id_reminder; Type: DEFAULT; Schema: tenant1; Owner: -
--

ALTER TABLE ONLY schedule_event_reminder ALTER COLUMN id_reminder SET DEFAULT nextval('schedule_event_reminder_id_reminder_seq'::regclass);


--
-- TOC entry 2260 (class 2604 OID 26044)
-- Name: id_sesion; Type: DEFAULT; Schema: tenant1; Owner: -
--

ALTER TABLE ONLY sesiones ALTER COLUMN id_sesion SET DEFAULT nextval('sesiones_id_sesion_seq'::regclass);


--
-- TOC entry 2273 (class 2604 OID 26045)
-- Name: id_vista; Type: DEFAULT; Schema: tenant1; Owner: -
--

ALTER TABLE ONLY vista ALTER COLUMN id_vista SET DEFAULT nextval('vista_casos_id_vista_seq'::regclass);


SET search_path = public, pg_catalog;

--
-- TOC entry 2425 (class 2606 OID 49751)
-- Name: qrtz_blob_triggers_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY qrtz_blob_triggers
    ADD CONSTRAINT qrtz_blob_triggers_pkey PRIMARY KEY (sched_name, trigger_name, trigger_group);


--
-- TOC entry 2427 (class 2606 OID 49764)
-- Name: qrtz_calendars_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY qrtz_calendars
    ADD CONSTRAINT qrtz_calendars_pkey PRIMARY KEY (sched_name, calendar_name);


--
-- TOC entry 2421 (class 2606 OID 49725)
-- Name: qrtz_cron_triggers_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY qrtz_cron_triggers
    ADD CONSTRAINT qrtz_cron_triggers_pkey PRIMARY KEY (sched_name, trigger_name, trigger_group);


--
-- TOC entry 2437 (class 2606 OID 49777)
-- Name: qrtz_fired_triggers_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY qrtz_fired_triggers
    ADD CONSTRAINT qrtz_fired_triggers_pkey PRIMARY KEY (sched_name, entry_id);


--
-- TOC entry 2403 (class 2606 OID 49691)
-- Name: qrtz_job_details_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY qrtz_job_details
    ADD CONSTRAINT qrtz_job_details_pkey PRIMARY KEY (sched_name, job_name, job_group);


--
-- TOC entry 2441 (class 2606 OID 49787)
-- Name: qrtz_locks_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY qrtz_locks
    ADD CONSTRAINT qrtz_locks_pkey PRIMARY KEY (sched_name, lock_name);


--
-- TOC entry 2429 (class 2606 OID 49769)
-- Name: qrtz_paused_trigger_grps_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY qrtz_paused_trigger_grps
    ADD CONSTRAINT qrtz_paused_trigger_grps_pkey PRIMARY KEY (sched_name, trigger_group);


--
-- TOC entry 2439 (class 2606 OID 49782)
-- Name: qrtz_scheduler_state_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY qrtz_scheduler_state
    ADD CONSTRAINT qrtz_scheduler_state_pkey PRIMARY KEY (sched_name, instance_name);


--
-- TOC entry 2419 (class 2606 OID 49712)
-- Name: qrtz_simple_triggers_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY qrtz_simple_triggers
    ADD CONSTRAINT qrtz_simple_triggers_pkey PRIMARY KEY (sched_name, trigger_name, trigger_group);


--
-- TOC entry 2423 (class 2606 OID 49738)
-- Name: qrtz_simprop_triggers_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY qrtz_simprop_triggers
    ADD CONSTRAINT qrtz_simprop_triggers_pkey PRIMARY KEY (sched_name, trigger_name, trigger_group);


--
-- TOC entry 2417 (class 2606 OID 49699)
-- Name: qrtz_triggers_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY qrtz_triggers
    ADD CONSTRAINT qrtz_triggers_pkey PRIMARY KEY (sched_name, trigger_name, trigger_group);


SET search_path = tenant1, pg_catalog;

--
-- TOC entry 2344 (class 2606 OID 26047)
-- Name: grupo_producto_pkey; Type: CONSTRAINT; Schema: tenant1; Owner: -; Tablespace: 
--

ALTER TABLE ONLY grupo_producto
    ADD CONSTRAINT grupo_producto_pkey PRIMARY KEY (id_producto, id_grupo);


--
-- TOC entry 2352 (class 2606 OID 26049)
-- Name: nombre_unico; Type: CONSTRAINT; Schema: tenant1; Owner: -; Tablespace: 
--

ALTER TABLE ONLY producto
    ADD CONSTRAINT nombre_unico UNIQUE (nombre);


--
-- TOC entry 2289 (class 2606 OID 26051)
-- Name: pk; Type: CONSTRAINT; Schema: tenant1; Owner: -; Tablespace: 
--

ALTER TABLE ONLY black_list_email
    ADD CONSTRAINT pk PRIMARY KEY (email_address);


--
-- TOC entry 2374 (class 2606 OID 26053)
-- Name: pk__tipo_accion__1b0907ce; Type: CONSTRAINT; Schema: tenant1; Owner: -; Tablespace: 
--

ALTER TABLE ONLY tipo_accion
    ADD CONSTRAINT pk__tipo_accion__1b0907ce PRIMARY KEY (id_tipo_accion);


--
-- TOC entry 2277 (class 2606 OID 26055)
-- Name: pk_accion; Type: CONSTRAINT; Schema: tenant1; Owner: -; Tablespace: 
--

ALTER TABLE ONLY accion
    ADD CONSTRAINT pk_accion PRIMARY KEY (id_accion);


--
-- TOC entry 2279 (class 2606 OID 26057)
-- Name: pk_app_setting; Type: CONSTRAINT; Schema: tenant1; Owner: -; Tablespace: 
--

ALTER TABLE ONLY app_setting
    ADD CONSTRAINT pk_app_setting PRIMARY KEY (setting_key);


--
-- TOC entry 2281 (class 2606 OID 26059)
-- Name: pk_archivo; Type: CONSTRAINT; Schema: tenant1; Owner: -; Tablespace: 
--

ALTER TABLE ONLY archivo
    ADD CONSTRAINT pk_archivo PRIMARY KEY (id_attachment);


--
-- TOC entry 2283 (class 2606 OID 26061)
-- Name: pk_area; Type: CONSTRAINT; Schema: tenant1; Owner: -; Tablespace: 
--

ALTER TABLE ONLY area
    ADD CONSTRAINT pk_area PRIMARY KEY (id_area);


--
-- TOC entry 2285 (class 2606 OID 26063)
-- Name: pk_attachment; Type: CONSTRAINT; Schema: tenant1; Owner: -; Tablespace: 
--

ALTER TABLE ONLY attachment
    ADD CONSTRAINT pk_attachment PRIMARY KEY (id_attachment);


--
-- TOC entry 2287 (class 2606 OID 26065)
-- Name: pk_audit_log; Type: CONSTRAINT; Schema: tenant1; Owner: -; Tablespace: 
--

ALTER TABLE ONLY audit_log
    ADD CONSTRAINT pk_audit_log PRIMARY KEY (id_log);


--
-- TOC entry 2291 (class 2606 OID 26067)
-- Name: pk_campo_comp_caso; Type: CONSTRAINT; Schema: tenant1; Owner: -; Tablespace: 
--

ALTER TABLE ONLY campo_comp_caso
    ADD CONSTRAINT pk_campo_comp_caso PRIMARY KEY (base_class, id_campo);


--
-- TOC entry 2293 (class 2606 OID 26069)
-- Name: pk_canal; Type: CONSTRAINT; Schema: tenant1; Owner: -; Tablespace: 
--

ALTER TABLE ONLY canal
    ADD CONSTRAINT pk_canal PRIMARY KEY (id_canal);


--
-- TOC entry 2295 (class 2606 OID 26071)
-- Name: pk_caso; Type: CONSTRAINT; Schema: tenant1; Owner: -; Tablespace: 
--

ALTER TABLE ONLY caso
    ADD CONSTRAINT pk_caso PRIMARY KEY (id_caso);


--
-- TOC entry 2302 (class 2606 OID 26073)
-- Name: pk_caso_caso; Type: CONSTRAINT; Schema: tenant1; Owner: -; Tablespace: 
--

ALTER TABLE ONLY caso_relacionado
    ADD CONSTRAINT pk_caso_caso PRIMARY KEY (id_caso_1, id_caso_2);


--
-- TOC entry 2298 (class 2606 OID 26075)
-- Name: pk_caso_custom_field; Type: CONSTRAINT; Schema: tenant1; Owner: -; Tablespace: 
--

ALTER TABLE ONLY caso_custom_field
    ADD CONSTRAINT pk_caso_custom_field PRIMARY KEY (id_caso, field_key, entity);


--
-- TOC entry 2300 (class 2606 OID 26077)
-- Name: pk_caso_documento; Type: CONSTRAINT; Schema: tenant1; Owner: -; Tablespace: 
--

ALTER TABLE ONLY caso_documento
    ADD CONSTRAINT pk_caso_documento PRIMARY KEY (id_caso, id_documento);


--
-- TOC entry 2304 (class 2606 OID 26079)
-- Name: pk_categoria; Type: CONSTRAINT; Schema: tenant1; Owner: -; Tablespace: 
--

ALTER TABLE ONLY categoria
    ADD CONSTRAINT pk_categoria PRIMARY KEY (id_categoria);


--
-- TOC entry 2306 (class 2606 OID 26081)
-- Name: pk_categoria_grupo; Type: CONSTRAINT; Schema: tenant1; Owner: -; Tablespace: 
--

ALTER TABLE ONLY categoria_grupo
    ADD CONSTRAINT pk_categoria_grupo PRIMARY KEY (id_categoria, id_grupo);


--
-- TOC entry 2308 (class 2606 OID 26083)
-- Name: pk_categoria_sub_estado_caso; Type: CONSTRAINT; Schema: tenant1; Owner: -; Tablespace: 
--

ALTER TABLE ONLY categoria_sub_estado_caso
    ADD CONSTRAINT pk_categoria_sub_estado_caso PRIMARY KEY (id_categoria, id_sub_estado);


--
-- TOC entry 2310 (class 2606 OID 26085)
-- Name: pk_cliente; Type: CONSTRAINT; Schema: tenant1; Owner: -; Tablespace: 
--

ALTER TABLE ONLY cliente
    ADD CONSTRAINT pk_cliente PRIMARY KEY (id_cliente);


--
-- TOC entry 2314 (class 2606 OID 26087)
-- Name: pk_clipping; Type: CONSTRAINT; Schema: tenant1; Owner: -; Tablespace: 
--

ALTER TABLE ONLY clipping
    ADD CONSTRAINT pk_clipping PRIMARY KEY (id_clipping);


--
-- TOC entry 2316 (class 2606 OID 26089)
-- Name: pk_componente; Type: CONSTRAINT; Schema: tenant1; Owner: -; Tablespace: 
--

ALTER TABLE ONLY componente
    ADD CONSTRAINT pk_componente PRIMARY KEY (id_componente);


--
-- TOC entry 2318 (class 2606 OID 26091)
-- Name: pk_condicion; Type: CONSTRAINT; Schema: tenant1; Owner: -; Tablespace: 
--

ALTER TABLE ONLY condicion
    ADD CONSTRAINT pk_condicion PRIMARY KEY (id_condicion);


--
-- TOC entry 2320 (class 2606 OID 26093)
-- Name: pk_custom_field; Type: CONSTRAINT; Schema: tenant1; Owner: -; Tablespace: 
--

ALTER TABLE ONLY custom_field
    ADD CONSTRAINT pk_custom_field PRIMARY KEY (field_key, entity);


--
-- TOC entry 2322 (class 2606 OID 26095)
-- Name: pk_custom_field_tipo_accion; Type: CONSTRAINT; Schema: tenant1; Owner: -; Tablespace: 
--

ALTER TABLE ONLY custom_field_tipo_accion
    ADD CONSTRAINT pk_custom_field_tipo_accion PRIMARY KEY (field_key, entity, id_tipo_accion);


--
-- TOC entry 2324 (class 2606 OID 26097)
-- Name: pk_custom_field_tipo_caso; Type: CONSTRAINT; Schema: tenant1; Owner: -; Tablespace: 
--

ALTER TABLE ONLY custom_field_tipo_caso
    ADD CONSTRAINT pk_custom_field_tipo_caso PRIMARY KEY (field_key, entity, id_tipo_caso);


--
-- TOC entry 2328 (class 2606 OID 26099)
-- Name: pk_email_cliente; Type: CONSTRAINT; Schema: tenant1; Owner: -; Tablespace: 
--

ALTER TABLE ONLY email_cliente
    ADD CONSTRAINT pk_email_cliente PRIMARY KEY (email_cliente);


--
-- TOC entry 2330 (class 2606 OID 26101)
-- Name: pk_estado_caso; Type: CONSTRAINT; Schema: tenant1; Owner: -; Tablespace: 
--

ALTER TABLE ONLY estado_caso
    ADD CONSTRAINT pk_estado_caso PRIMARY KEY (id_estado);


--
-- TOC entry 2332 (class 2606 OID 26103)
-- Name: pk_etiqueta; Type: CONSTRAINT; Schema: tenant1; Owner: -; Tablespace: 
--

ALTER TABLE ONLY etiqueta
    ADD CONSTRAINT pk_etiqueta PRIMARY KEY (tag_id);


--
-- TOC entry 2334 (class 2606 OID 26105)
-- Name: pk_etiqueta_caso; Type: CONSTRAINT; Schema: tenant1; Owner: -; Tablespace: 
--

ALTER TABLE ONLY etiqueta_caso
    ADD CONSTRAINT pk_etiqueta_caso PRIMARY KEY (tag_id, id_caso);


--
-- TOC entry 2326 (class 2606 OID 26107)
-- Name: pk_faq; Type: CONSTRAINT; Schema: tenant1; Owner: -; Tablespace: 
--

ALTER TABLE ONLY documento
    ADD CONSTRAINT pk_faq PRIMARY KEY (id_documento);


--
-- TOC entry 2336 (class 2606 OID 26109)
-- Name: pk_field_type; Type: CONSTRAINT; Schema: tenant1; Owner: -; Tablespace: 
--

ALTER TABLE ONLY field_type
    ADD CONSTRAINT pk_field_type PRIMARY KEY (field_type_id);


--
-- TOC entry 2338 (class 2606 OID 26111)
-- Name: pk_filtros_vista; Type: CONSTRAINT; Schema: tenant1; Owner: -; Tablespace: 
--

ALTER TABLE ONLY filtro_vista
    ADD CONSTRAINT pk_filtros_vista PRIMARY KEY (id_filtro);


--
-- TOC entry 2340 (class 2606 OID 26113)
-- Name: pk_funcion; Type: CONSTRAINT; Schema: tenant1; Owner: -; Tablespace: 
--

ALTER TABLE ONLY funcion
    ADD CONSTRAINT pk_funcion PRIMARY KEY (id_funcion);


--
-- TOC entry 2342 (class 2606 OID 26115)
-- Name: pk_grupo; Type: CONSTRAINT; Schema: tenant1; Owner: -; Tablespace: 
--

ALTER TABLE ONLY grupo
    ADD CONSTRAINT pk_grupo PRIMARY KEY (id_grupo);


--
-- TOC entry 2346 (class 2606 OID 26117)
-- Name: pk_modelo_producto; Type: CONSTRAINT; Schema: tenant1; Owner: -; Tablespace: 
--

ALTER TABLE ONLY modelo_producto
    ADD CONSTRAINT pk_modelo_producto PRIMARY KEY (id_modelo, id_producto);


--
-- TOC entry 2348 (class 2606 OID 26119)
-- Name: pk_nota; Type: CONSTRAINT; Schema: tenant1; Owner: -; Tablespace: 
--

ALTER TABLE ONLY nota
    ADD CONSTRAINT pk_nota PRIMARY KEY (id_nota);


--
-- TOC entry 2350 (class 2606 OID 26121)
-- Name: pk_prioridad; Type: CONSTRAINT; Schema: tenant1; Owner: -; Tablespace: 
--

ALTER TABLE ONLY prioridad
    ADD CONSTRAINT pk_prioridad PRIMARY KEY (id_prioridad);


--
-- TOC entry 2354 (class 2606 OID 26123)
-- Name: pk_producto; Type: CONSTRAINT; Schema: tenant1; Owner: -; Tablespace: 
--

ALTER TABLE ONLY producto
    ADD CONSTRAINT pk_producto PRIMARY KEY (id_producto);


--
-- TOC entry 2356 (class 2606 OID 26125)
-- Name: pk_producto_contratado; Type: CONSTRAINT; Schema: tenant1; Owner: -; Tablespace: 
--

ALTER TABLE ONLY producto_contratado
    ADD CONSTRAINT pk_producto_contratado PRIMARY KEY (id_cliente, id_producto, id_componente, id_sub_componente);


--
-- TOC entry 2358 (class 2606 OID 26127)
-- Name: pk_regla; Type: CONSTRAINT; Schema: tenant1; Owner: -; Tablespace: 
--

ALTER TABLE ONLY regla_trigger
    ADD CONSTRAINT pk_regla PRIMARY KEY (id_trigger);


--
-- TOC entry 2360 (class 2606 OID 26129)
-- Name: pk_regla_trigger_area; Type: CONSTRAINT; Schema: tenant1; Owner: -; Tablespace: 
--

ALTER TABLE ONLY regla_trigger_area
    ADD CONSTRAINT pk_regla_trigger_area PRIMARY KEY (id_trigger, id_area);


--
-- TOC entry 2395 (class 2606 OID 47298)
-- Name: pk_resource; Type: CONSTRAINT; Schema: tenant1; Owner: -; Tablespace: 
--

ALTER TABLE ONLY resource
    ADD CONSTRAINT pk_resource PRIMARY KEY (id_resource);


--
-- TOC entry 2362 (class 2606 OID 26131)
-- Name: pk_rol; Type: CONSTRAINT; Schema: tenant1; Owner: -; Tablespace: 
--

ALTER TABLE ONLY rol
    ADD CONSTRAINT pk_rol PRIMARY KEY (id_rol);


--
-- TOC entry 2364 (class 2606 OID 26133)
-- Name: pk_rol_funcion; Type: CONSTRAINT; Schema: tenant1; Owner: -; Tablespace: 
--

ALTER TABLE ONLY rol_funcion
    ADD CONSTRAINT pk_rol_funcion PRIMARY KEY (id_rol, id_funcion);


--
-- TOC entry 2366 (class 2606 OID 26135)
-- Name: pk_schedule_event; Type: CONSTRAINT; Schema: tenant1; Owner: -; Tablespace: 
--

ALTER TABLE ONLY schedule_event
    ADD CONSTRAINT pk_schedule_event PRIMARY KEY (event_id);


--
-- TOC entry 2393 (class 2606 OID 47275)
-- Name: pk_schedule_event_reminder; Type: CONSTRAINT; Schema: tenant1; Owner: -; Tablespace: 
--

ALTER TABLE ONLY schedule_event_reminder
    ADD CONSTRAINT pk_schedule_event_reminder PRIMARY KEY (id_reminder);


--
-- TOC entry 2397 (class 2606 OID 47303)
-- Name: pk_schedule_event_resource; Type: CONSTRAINT; Schema: tenant1; Owner: -; Tablespace: 
--

ALTER TABLE ONLY schedule_event_resource
    ADD CONSTRAINT pk_schedule_event_resource PRIMARY KEY (event_id, id_resource);


--
-- TOC entry 2399 (class 2606 OID 47318)
-- Name: pk_schedule_event_usuario; Type: CONSTRAINT; Schema: tenant1; Owner: -; Tablespace: 
--

ALTER TABLE ONLY schedule_event_usuario
    ADD CONSTRAINT pk_schedule_event_usuario PRIMARY KEY (id_usuario, event_id);


--
-- TOC entry 2368 (class 2606 OID 26137)
-- Name: pk_sesiones; Type: CONSTRAINT; Schema: tenant1; Owner: -; Tablespace: 
--

ALTER TABLE ONLY sesiones
    ADD CONSTRAINT pk_sesiones PRIMARY KEY (id_sesion);


--
-- TOC entry 2370 (class 2606 OID 26139)
-- Name: pk_sub_componente; Type: CONSTRAINT; Schema: tenant1; Owner: -; Tablespace: 
--

ALTER TABLE ONLY sub_componente
    ADD CONSTRAINT pk_sub_componente PRIMARY KEY (id_sub_componente);


--
-- TOC entry 2372 (class 2606 OID 26141)
-- Name: pk_sub_estado_caso; Type: CONSTRAINT; Schema: tenant1; Owner: -; Tablespace: 
--

ALTER TABLE ONLY sub_estado_caso
    ADD CONSTRAINT pk_sub_estado_caso PRIMARY KEY (id_sub_estado);


--
-- TOC entry 2376 (class 2606 OID 26143)
-- Name: pk_tipo_alerta; Type: CONSTRAINT; Schema: tenant1; Owner: -; Tablespace: 
--

ALTER TABLE ONLY tipo_alerta
    ADD CONSTRAINT pk_tipo_alerta PRIMARY KEY (idalerta);


--
-- TOC entry 2378 (class 2606 OID 26145)
-- Name: pk_tipo_caso; Type: CONSTRAINT; Schema: tenant1; Owner: -; Tablespace: 
--

ALTER TABLE ONLY tipo_caso
    ADD CONSTRAINT pk_tipo_caso PRIMARY KEY (id_tipo_caso);


--
-- TOC entry 2380 (class 2606 OID 26147)
-- Name: pk_tipo_comparacion; Type: CONSTRAINT; Schema: tenant1; Owner: -; Tablespace: 
--

ALTER TABLE ONLY tipo_comparacion
    ADD CONSTRAINT pk_tipo_comparacion PRIMARY KEY (id_comparador);


--
-- TOC entry 2382 (class 2606 OID 26149)
-- Name: pk_tipo_nota; Type: CONSTRAINT; Schema: tenant1; Owner: -; Tablespace: 
--

ALTER TABLE ONLY tipo_nota
    ADD CONSTRAINT pk_tipo_nota PRIMARY KEY (id_tipo_nota);


--
-- TOC entry 2384 (class 2606 OID 26151)
-- Name: pk_user_rol; Type: CONSTRAINT; Schema: tenant1; Owner: -; Tablespace: 
--

ALTER TABLE ONLY user_rol
    ADD CONSTRAINT pk_user_rol PRIMARY KEY (id_usuario, id_rol);


--
-- TOC entry 2386 (class 2606 OID 26153)
-- Name: pk_usuario; Type: CONSTRAINT; Schema: tenant1; Owner: -; Tablespace: 
--

ALTER TABLE ONLY usuario
    ADD CONSTRAINT pk_usuario PRIMARY KEY (id_usuario);


--
-- TOC entry 2388 (class 2606 OID 26155)
-- Name: pk_usuario_grupo; Type: CONSTRAINT; Schema: tenant1; Owner: -; Tablespace: 
--

ALTER TABLE ONLY usuario_grupo
    ADD CONSTRAINT pk_usuario_grupo PRIMARY KEY (id_usuario, id_grupo);


--
-- TOC entry 2390 (class 2606 OID 26157)
-- Name: pk_vista_casos; Type: CONSTRAINT; Schema: tenant1; Owner: -; Tablespace: 
--

ALTER TABLE ONLY vista
    ADD CONSTRAINT pk_vista_casos PRIMARY KEY (id_vista);


--
-- TOC entry 2312 (class 2606 OID 26159)
-- Name: rut_unique; Type: CONSTRAINT; Schema: tenant1; Owner: -; Tablespace: 
--

ALTER TABLE ONLY cliente
    ADD CONSTRAINT rut_unique UNIQUE (rut);


SET search_path = public, pg_catalog;

--
-- TOC entry 2430 (class 1259 OID 49803)
-- Name: idx_qrtz_ft_inst_job_req_rcvry; Type: INDEX; Schema: public; Owner: -; Tablespace: 
--

CREATE INDEX idx_qrtz_ft_inst_job_req_rcvry ON qrtz_fired_triggers USING btree (sched_name, instance_name, requests_recovery);


--
-- TOC entry 2431 (class 1259 OID 49804)
-- Name: idx_qrtz_ft_j_g; Type: INDEX; Schema: public; Owner: -; Tablespace: 
--

CREATE INDEX idx_qrtz_ft_j_g ON qrtz_fired_triggers USING btree (sched_name, job_name, job_group);


--
-- TOC entry 2432 (class 1259 OID 49805)
-- Name: idx_qrtz_ft_jg; Type: INDEX; Schema: public; Owner: -; Tablespace: 
--

CREATE INDEX idx_qrtz_ft_jg ON qrtz_fired_triggers USING btree (sched_name, job_group);


--
-- TOC entry 2433 (class 1259 OID 49806)
-- Name: idx_qrtz_ft_t_g; Type: INDEX; Schema: public; Owner: -; Tablespace: 
--

CREATE INDEX idx_qrtz_ft_t_g ON qrtz_fired_triggers USING btree (sched_name, trigger_name, trigger_group);


--
-- TOC entry 2434 (class 1259 OID 49807)
-- Name: idx_qrtz_ft_tg; Type: INDEX; Schema: public; Owner: -; Tablespace: 
--

CREATE INDEX idx_qrtz_ft_tg ON qrtz_fired_triggers USING btree (sched_name, trigger_group);


--
-- TOC entry 2435 (class 1259 OID 49802)
-- Name: idx_qrtz_ft_trig_inst_name; Type: INDEX; Schema: public; Owner: -; Tablespace: 
--

CREATE INDEX idx_qrtz_ft_trig_inst_name ON qrtz_fired_triggers USING btree (sched_name, instance_name);


--
-- TOC entry 2400 (class 1259 OID 49789)
-- Name: idx_qrtz_j_grp; Type: INDEX; Schema: public; Owner: -; Tablespace: 
--

CREATE INDEX idx_qrtz_j_grp ON qrtz_job_details USING btree (sched_name, job_group);


--
-- TOC entry 2401 (class 1259 OID 49788)
-- Name: idx_qrtz_j_req_recovery; Type: INDEX; Schema: public; Owner: -; Tablespace: 
--

CREATE INDEX idx_qrtz_j_req_recovery ON qrtz_job_details USING btree (sched_name, requests_recovery);


--
-- TOC entry 2404 (class 1259 OID 49792)
-- Name: idx_qrtz_t_c; Type: INDEX; Schema: public; Owner: -; Tablespace: 
--

CREATE INDEX idx_qrtz_t_c ON qrtz_triggers USING btree (sched_name, calendar_name);


--
-- TOC entry 2405 (class 1259 OID 49793)
-- Name: idx_qrtz_t_g; Type: INDEX; Schema: public; Owner: -; Tablespace: 
--

CREATE INDEX idx_qrtz_t_g ON qrtz_triggers USING btree (sched_name, trigger_group);


--
-- TOC entry 2406 (class 1259 OID 49790)
-- Name: idx_qrtz_t_j; Type: INDEX; Schema: public; Owner: -; Tablespace: 
--

CREATE INDEX idx_qrtz_t_j ON qrtz_triggers USING btree (sched_name, job_name, job_group);


--
-- TOC entry 2407 (class 1259 OID 49791)
-- Name: idx_qrtz_t_jg; Type: INDEX; Schema: public; Owner: -; Tablespace: 
--

CREATE INDEX idx_qrtz_t_jg ON qrtz_triggers USING btree (sched_name, job_group);


--
-- TOC entry 2408 (class 1259 OID 49796)
-- Name: idx_qrtz_t_n_g_state; Type: INDEX; Schema: public; Owner: -; Tablespace: 
--

CREATE INDEX idx_qrtz_t_n_g_state ON qrtz_triggers USING btree (sched_name, trigger_group, trigger_state);


--
-- TOC entry 2409 (class 1259 OID 49795)
-- Name: idx_qrtz_t_n_state; Type: INDEX; Schema: public; Owner: -; Tablespace: 
--

CREATE INDEX idx_qrtz_t_n_state ON qrtz_triggers USING btree (sched_name, trigger_name, trigger_group, trigger_state);


--
-- TOC entry 2410 (class 1259 OID 49797)
-- Name: idx_qrtz_t_next_fire_time; Type: INDEX; Schema: public; Owner: -; Tablespace: 
--

CREATE INDEX idx_qrtz_t_next_fire_time ON qrtz_triggers USING btree (sched_name, next_fire_time);


--
-- TOC entry 2411 (class 1259 OID 49799)
-- Name: idx_qrtz_t_nft_misfire; Type: INDEX; Schema: public; Owner: -; Tablespace: 
--

CREATE INDEX idx_qrtz_t_nft_misfire ON qrtz_triggers USING btree (sched_name, misfire_instr, next_fire_time);


--
-- TOC entry 2412 (class 1259 OID 49798)
-- Name: idx_qrtz_t_nft_st; Type: INDEX; Schema: public; Owner: -; Tablespace: 
--

CREATE INDEX idx_qrtz_t_nft_st ON qrtz_triggers USING btree (sched_name, trigger_state, next_fire_time);


--
-- TOC entry 2413 (class 1259 OID 49800)
-- Name: idx_qrtz_t_nft_st_misfire; Type: INDEX; Schema: public; Owner: -; Tablespace: 
--

CREATE INDEX idx_qrtz_t_nft_st_misfire ON qrtz_triggers USING btree (sched_name, misfire_instr, next_fire_time, trigger_state);


--
-- TOC entry 2414 (class 1259 OID 49801)
-- Name: idx_qrtz_t_nft_st_misfire_grp; Type: INDEX; Schema: public; Owner: -; Tablespace: 
--

CREATE INDEX idx_qrtz_t_nft_st_misfire_grp ON qrtz_triggers USING btree (sched_name, misfire_instr, next_fire_time, trigger_group, trigger_state);


--
-- TOC entry 2415 (class 1259 OID 49794)
-- Name: idx_qrtz_t_state; Type: INDEX; Schema: public; Owner: -; Tablespace: 
--

CREATE INDEX idx_qrtz_t_state ON qrtz_triggers USING btree (sched_name, trigger_state);


SET search_path = tenant1, pg_catalog;

--
-- TOC entry 2391 (class 1259 OID 47281)
-- Name: fki_event; Type: INDEX; Schema: tenant1; Owner: -; Tablespace: 
--

CREATE INDEX fki_event ON schedule_event_reminder USING btree (event_id);


--
-- TOC entry 2296 (class 1259 OID 26160)
-- Name: textsearch_idx; Type: INDEX; Schema: tenant1; Owner: -; Tablespace: 
--

CREATE INDEX textsearch_idx ON caso USING gist (textsearchable_index_col);


--
-- TOC entry 2538 (class 2620 OID 26161)
-- Name: tsvector_update; Type: TRIGGER; Schema: tenant1; Owner: -
--

CREATE TRIGGER tsvector_update BEFORE INSERT OR UPDATE ON caso FOR EACH ROW EXECUTE PROCEDURE tsvector_update_trigger('textsearchable_index_col', 'pg_catalog.english', 'tema', 'descripcion');


SET search_path = public, pg_catalog;

--
-- TOC entry 2537 (class 2606 OID 49752)
-- Name: qrtz_blob_triggers_sched_name_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY qrtz_blob_triggers
    ADD CONSTRAINT qrtz_blob_triggers_sched_name_fkey FOREIGN KEY (sched_name, trigger_name, trigger_group) REFERENCES qrtz_triggers(sched_name, trigger_name, trigger_group);


--
-- TOC entry 2535 (class 2606 OID 49726)
-- Name: qrtz_cron_triggers_sched_name_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY qrtz_cron_triggers
    ADD CONSTRAINT qrtz_cron_triggers_sched_name_fkey FOREIGN KEY (sched_name, trigger_name, trigger_group) REFERENCES qrtz_triggers(sched_name, trigger_name, trigger_group);


--
-- TOC entry 2534 (class 2606 OID 49713)
-- Name: qrtz_simple_triggers_sched_name_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY qrtz_simple_triggers
    ADD CONSTRAINT qrtz_simple_triggers_sched_name_fkey FOREIGN KEY (sched_name, trigger_name, trigger_group) REFERENCES qrtz_triggers(sched_name, trigger_name, trigger_group);


--
-- TOC entry 2536 (class 2606 OID 49739)
-- Name: qrtz_simprop_triggers_sched_name_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY qrtz_simprop_triggers
    ADD CONSTRAINT qrtz_simprop_triggers_sched_name_fkey FOREIGN KEY (sched_name, trigger_name, trigger_group) REFERENCES qrtz_triggers(sched_name, trigger_name, trigger_group);


--
-- TOC entry 2533 (class 2606 OID 49700)
-- Name: qrtz_triggers_sched_name_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY qrtz_triggers
    ADD CONSTRAINT qrtz_triggers_sched_name_fkey FOREIGN KEY (sched_name, job_name, job_group) REFERENCES qrtz_job_details(sched_name, job_name, job_group);


SET search_path = tenant1, pg_catalog;

--
-- TOC entry 2467 (class 2606 OID 26162)
-- Name: area_categoria; Type: FK CONSTRAINT; Schema: tenant1; Owner: -
--

ALTER TABLE ONLY categoria
    ADD CONSTRAINT area_categoria FOREIGN KEY (id_area) REFERENCES area(id_area);


--
-- TOC entry 2477 (class 2606 OID 28795)
-- Name: area_clipping; Type: FK CONSTRAINT; Schema: tenant1; Owner: -
--

ALTER TABLE ONLY clipping
    ADD CONSTRAINT area_clipping FOREIGN KEY (id_area) REFERENCES area(id_area) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 2507 (class 2606 OID 26172)
-- Name: area_regla_trigger_area; Type: FK CONSTRAINT; Schema: tenant1; Owner: -
--

ALTER TABLE ONLY regla_trigger_area
    ADD CONSTRAINT area_regla_trigger_area FOREIGN KEY (id_area) REFERENCES area(id_area);


--
-- TOC entry 2525 (class 2606 OID 26177)
-- Name: area_vista; Type: FK CONSTRAINT; Schema: tenant1; Owner: -
--

ALTER TABLE ONLY vista
    ADD CONSTRAINT area_vista FOREIGN KEY (id_area) REFERENCES area(id_area);


--
-- TOC entry 2461 (class 2606 OID 26182)
-- Name: caso_caso_documento; Type: FK CONSTRAINT; Schema: tenant1; Owner: -
--

ALTER TABLE ONLY caso_documento
    ADD CONSTRAINT caso_caso_documento FOREIGN KEY (id_caso) REFERENCES caso(id_caso);


--
-- TOC entry 2491 (class 2606 OID 26187)
-- Name: caso_etiqueta_caso; Type: FK CONSTRAINT; Schema: tenant1; Owner: -
--

ALTER TABLE ONLY etiqueta_caso
    ADD CONSTRAINT caso_etiqueta_caso FOREIGN KEY (id_caso) REFERENCES caso(id_caso);


--
-- TOC entry 2511 (class 2606 OID 26192)
-- Name: caso_schedule_event; Type: FK CONSTRAINT; Schema: tenant1; Owner: -
--

ALTER TABLE ONLY schedule_event
    ADD CONSTRAINT caso_schedule_event FOREIGN KEY (id_caso) REFERENCES caso(id_caso) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 2488 (class 2606 OID 26197)
-- Name: cliente_email_cliente; Type: FK CONSTRAINT; Schema: tenant1; Owner: -
--

ALTER TABLE ONLY email_cliente
    ADD CONSTRAINT cliente_email_cliente FOREIGN KEY (id_cliente) REFERENCES cliente(id_cliente);


--
-- TOC entry 2503 (class 2606 OID 26202)
-- Name: cliente_producto_contratado; Type: FK CONSTRAINT; Schema: tenant1; Owner: -
--

ALTER TABLE ONLY producto_contratado
    ADD CONSTRAINT cliente_producto_contratado FOREIGN KEY (id_cliente) REFERENCES cliente(id_cliente);


--
-- TOC entry 2474 (class 2606 OID 26207)
-- Name: clipping_clipping; Type: FK CONSTRAINT; Schema: tenant1; Owner: -
--

ALTER TABLE ONLY clipping
    ADD CONSTRAINT clipping_clipping FOREIGN KEY (id_clipping_padre) REFERENCES clipping(id_clipping);


--
-- TOC entry 2498 (class 2606 OID 26212)
-- Name: componente_modelo_producto; Type: FK CONSTRAINT; Schema: tenant1; Owner: -
--

ALTER TABLE ONLY modelo_producto
    ADD CONSTRAINT componente_modelo_producto FOREIGN KEY (id_componente) REFERENCES componente(id_componente);


--
-- TOC entry 2504 (class 2606 OID 26217)
-- Name: componente_producto_contratado; Type: FK CONSTRAINT; Schema: tenant1; Owner: -
--

ALTER TABLE ONLY producto_contratado
    ADD CONSTRAINT componente_producto_contratado FOREIGN KEY (id_componente) REFERENCES componente(id_componente);


--
-- TOC entry 2460 (class 2606 OID 26222)
-- Name: custom_field_caso_custom_field; Type: FK CONSTRAINT; Schema: tenant1; Owner: -
--

ALTER TABLE ONLY caso_custom_field
    ADD CONSTRAINT custom_field_caso_custom_field FOREIGN KEY (id_caso) REFERENCES caso(id_caso) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 2484 (class 2606 OID 26227)
-- Name: custom_field_custom_field_tipo_caso; Type: FK CONSTRAINT; Schema: tenant1; Owner: -
--

ALTER TABLE ONLY custom_field_tipo_caso
    ADD CONSTRAINT custom_field_custom_field_tipo_caso FOREIGN KEY (field_key, entity) REFERENCES custom_field(field_key, entity);


--
-- TOC entry 2462 (class 2606 OID 26232)
-- Name: documento_caso_documento; Type: FK CONSTRAINT; Schema: tenant1; Owner: -
--

ALTER TABLE ONLY caso_documento
    ADD CONSTRAINT documento_caso_documento FOREIGN KEY (id_documento) REFERENCES documento(id_documento);


--
-- TOC entry 2516 (class 2606 OID 26237)
-- Name: estado_caso_sub_estado_caso; Type: FK CONSTRAINT; Schema: tenant1; Owner: -
--

ALTER TABLE ONLY sub_estado_caso
    ADD CONSTRAINT estado_caso_sub_estado_caso FOREIGN KEY (id_estado) REFERENCES estado_caso(id_estado);


--
-- TOC entry 2492 (class 2606 OID 26242)
-- Name: etiqueta_etiqueta_caso; Type: FK CONSTRAINT; Schema: tenant1; Owner: -
--

ALTER TABLE ONLY etiqueta_caso
    ADD CONSTRAINT etiqueta_etiqueta_caso FOREIGN KEY (tag_id) REFERENCES etiqueta(tag_id);


--
-- TOC entry 2445 (class 2606 OID 26247)
-- Name: field_type_campo_comp_caso; Type: FK CONSTRAINT; Schema: tenant1; Owner: -
--

ALTER TABLE ONLY campo_comp_caso
    ADD CONSTRAINT field_type_campo_comp_caso FOREIGN KEY (field_type_id) REFERENCES field_type(field_type_id);


--
-- TOC entry 2481 (class 2606 OID 26252)
-- Name: field_type_custom_field; Type: FK CONSTRAINT; Schema: tenant1; Owner: -
--

ALTER TABLE ONLY custom_field
    ADD CONSTRAINT field_type_custom_field FOREIGN KEY (field_type_id) REFERENCES field_type(field_type_id);


--
-- TOC entry 2442 (class 2606 OID 26257)
-- Name: fk_accion_id_trigger; Type: FK CONSTRAINT; Schema: tenant1; Owner: -
--

ALTER TABLE ONLY accion
    ADD CONSTRAINT fk_accion_id_trigger FOREIGN KEY (id_trigger) REFERENCES regla_trigger(id_trigger);


--
-- TOC entry 2444 (class 2606 OID 26262)
-- Name: fk_attachment_id_caso; Type: FK CONSTRAINT; Schema: tenant1; Owner: -
--

ALTER TABLE ONLY attachment
    ADD CONSTRAINT fk_attachment_id_caso FOREIGN KEY (id_caso) REFERENCES caso(id_caso) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 2465 (class 2606 OID 26267)
-- Name: fk_caso_caso_caso; Type: FK CONSTRAINT; Schema: tenant1; Owner: -
--

ALTER TABLE ONLY caso_relacionado
    ADD CONSTRAINT fk_caso_caso_caso FOREIGN KEY (id_caso_1) REFERENCES caso(id_caso);


--
-- TOC entry 2463 (class 2606 OID 26272)
-- Name: fk_caso_documento_id_caso; Type: FK CONSTRAINT; Schema: tenant1; Owner: -
--

ALTER TABLE ONLY caso_documento
    ADD CONSTRAINT fk_caso_documento_id_caso FOREIGN KEY (id_caso) REFERENCES caso(id_caso);


--
-- TOC entry 2464 (class 2606 OID 26277)
-- Name: fk_caso_documento_id_documento; Type: FK CONSTRAINT; Schema: tenant1; Owner: -
--

ALTER TABLE ONLY caso_documento
    ADD CONSTRAINT fk_caso_documento_id_documento FOREIGN KEY (id_documento) REFERENCES documento(id_documento);


--
-- TOC entry 2446 (class 2606 OID 26282)
-- Name: fk_caso_email_cliente; Type: FK CONSTRAINT; Schema: tenant1; Owner: -
--

ALTER TABLE ONLY caso
    ADD CONSTRAINT fk_caso_email_cliente FOREIGN KEY (email_cliente) REFERENCES email_cliente(email_cliente) ON UPDATE CASCADE;


--
-- TOC entry 2447 (class 2606 OID 26287)
-- Name: fk_caso_estado_alerta; Type: FK CONSTRAINT; Schema: tenant1; Owner: -
--

ALTER TABLE ONLY caso
    ADD CONSTRAINT fk_caso_estado_alerta FOREIGN KEY (estado_alerta) REFERENCES tipo_alerta(idalerta);


--
-- TOC entry 2448 (class 2606 OID 26292)
-- Name: fk_caso_id_canal; Type: FK CONSTRAINT; Schema: tenant1; Owner: -
--

ALTER TABLE ONLY caso
    ADD CONSTRAINT fk_caso_id_canal FOREIGN KEY (id_canal) REFERENCES canal(id_canal);


--
-- TOC entry 2449 (class 2606 OID 26297)
-- Name: fk_caso_id_caso_padre; Type: FK CONSTRAINT; Schema: tenant1; Owner: -
--

ALTER TABLE ONLY caso
    ADD CONSTRAINT fk_caso_id_caso_padre FOREIGN KEY (id_caso_padre) REFERENCES caso(id_caso);


--
-- TOC entry 2450 (class 2606 OID 26302)
-- Name: fk_caso_id_categoria; Type: FK CONSTRAINT; Schema: tenant1; Owner: -
--

ALTER TABLE ONLY caso
    ADD CONSTRAINT fk_caso_id_categoria FOREIGN KEY (id_categoria) REFERENCES categoria(id_categoria);


--
-- TOC entry 2451 (class 2606 OID 26307)
-- Name: fk_caso_id_componente; Type: FK CONSTRAINT; Schema: tenant1; Owner: -
--

ALTER TABLE ONLY caso
    ADD CONSTRAINT fk_caso_id_componente FOREIGN KEY (id_componente) REFERENCES componente(id_componente);


--
-- TOC entry 2452 (class 2606 OID 26312)
-- Name: fk_caso_id_estado; Type: FK CONSTRAINT; Schema: tenant1; Owner: -
--

ALTER TABLE ONLY caso
    ADD CONSTRAINT fk_caso_id_estado FOREIGN KEY (id_estado) REFERENCES estado_caso(id_estado);


--
-- TOC entry 2453 (class 2606 OID 26317)
-- Name: fk_caso_id_prioridad; Type: FK CONSTRAINT; Schema: tenant1; Owner: -
--

ALTER TABLE ONLY caso
    ADD CONSTRAINT fk_caso_id_prioridad FOREIGN KEY (id_prioridad) REFERENCES prioridad(id_prioridad);


--
-- TOC entry 2454 (class 2606 OID 26322)
-- Name: fk_caso_id_producto; Type: FK CONSTRAINT; Schema: tenant1; Owner: -
--

ALTER TABLE ONLY caso
    ADD CONSTRAINT fk_caso_id_producto FOREIGN KEY (id_producto) REFERENCES producto(id_producto);


--
-- TOC entry 2455 (class 2606 OID 26327)
-- Name: fk_caso_id_sub_componente; Type: FK CONSTRAINT; Schema: tenant1; Owner: -
--

ALTER TABLE ONLY caso
    ADD CONSTRAINT fk_caso_id_sub_componente FOREIGN KEY (id_sub_componente) REFERENCES sub_componente(id_sub_componente);


--
-- TOC entry 2456 (class 2606 OID 26332)
-- Name: fk_caso_id_sub_estado; Type: FK CONSTRAINT; Schema: tenant1; Owner: -
--

ALTER TABLE ONLY caso
    ADD CONSTRAINT fk_caso_id_sub_estado FOREIGN KEY (id_sub_estado) REFERENCES sub_estado_caso(id_sub_estado);


--
-- TOC entry 2457 (class 2606 OID 26337)
-- Name: fk_caso_owner; Type: FK CONSTRAINT; Schema: tenant1; Owner: -
--

ALTER TABLE ONLY caso
    ADD CONSTRAINT fk_caso_owner FOREIGN KEY (owner) REFERENCES usuario(id_usuario);


--
-- TOC entry 2466 (class 2606 OID 26342)
-- Name: fk_caso_relacionado_id_caso_1; Type: FK CONSTRAINT; Schema: tenant1; Owner: -
--

ALTER TABLE ONLY caso_relacionado
    ADD CONSTRAINT fk_caso_relacionado_id_caso_1 FOREIGN KEY (id_caso_1) REFERENCES caso(id_caso);


--
-- TOC entry 2470 (class 2606 OID 28840)
-- Name: fk_categoria_grupo_id_categoria; Type: FK CONSTRAINT; Schema: tenant1; Owner: -
--

ALTER TABLE ONLY categoria_grupo
    ADD CONSTRAINT fk_categoria_grupo_id_categoria FOREIGN KEY (id_categoria) REFERENCES categoria(id_categoria) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 2468 (class 2606 OID 26352)
-- Name: fk_categoria_id_area; Type: FK CONSTRAINT; Schema: tenant1; Owner: -
--

ALTER TABLE ONLY categoria
    ADD CONSTRAINT fk_categoria_id_area FOREIGN KEY (id_area) REFERENCES area(id_area);


--
-- TOC entry 2469 (class 2606 OID 26357)
-- Name: fk_categoria_id_categoria_padre; Type: FK CONSTRAINT; Schema: tenant1; Owner: -
--

ALTER TABLE ONLY categoria
    ADD CONSTRAINT fk_categoria_id_categoria_padre FOREIGN KEY (id_categoria_padre) REFERENCES categoria(id_categoria);


--
-- TOC entry 2472 (class 2606 OID 26362)
-- Name: fk_categoria_sub_estado_caso_id_categoria; Type: FK CONSTRAINT; Schema: tenant1; Owner: -
--

ALTER TABLE ONLY categoria_sub_estado_caso
    ADD CONSTRAINT fk_categoria_sub_estado_caso_id_categoria FOREIGN KEY (id_categoria) REFERENCES categoria(id_categoria);


--
-- TOC entry 2478 (class 2606 OID 26367)
-- Name: fk_componente_id_producto; Type: FK CONSTRAINT; Schema: tenant1; Owner: -
--

ALTER TABLE ONLY componente
    ADD CONSTRAINT fk_componente_id_producto FOREIGN KEY (id_producto) REFERENCES producto(id_producto);


--
-- TOC entry 2479 (class 2606 OID 26372)
-- Name: fk_condicion_id_trigger; Type: FK CONSTRAINT; Schema: tenant1; Owner: -
--

ALTER TABLE ONLY condicion
    ADD CONSTRAINT fk_condicion_id_trigger FOREIGN KEY (id_trigger) REFERENCES regla_trigger(id_trigger);


--
-- TOC entry 2482 (class 2606 OID 26377)
-- Name: fk_custom_field; Type: FK CONSTRAINT; Schema: tenant1; Owner: -
--

ALTER TABLE ONLY custom_field_tipo_accion
    ADD CONSTRAINT fk_custom_field FOREIGN KEY (field_key, entity) REFERENCES custom_field(field_key, entity);


--
-- TOC entry 2486 (class 2606 OID 26382)
-- Name: fk_documento_created_by; Type: FK CONSTRAINT; Schema: tenant1; Owner: -
--

ALTER TABLE ONLY documento
    ADD CONSTRAINT fk_documento_created_by FOREIGN KEY (created_by) REFERENCES usuario(id_usuario);


--
-- TOC entry 2489 (class 2606 OID 26387)
-- Name: fk_email_cliente_id_cliente; Type: FK CONSTRAINT; Schema: tenant1; Owner: -
--

ALTER TABLE ONLY email_cliente
    ADD CONSTRAINT fk_email_cliente_id_cliente FOREIGN KEY (id_cliente) REFERENCES cliente(id_cliente);


--
-- TOC entry 2528 (class 2606 OID 47282)
-- Name: fk_event; Type: FK CONSTRAINT; Schema: tenant1; Owner: -
--

ALTER TABLE ONLY schedule_event_reminder
    ADD CONSTRAINT fk_event FOREIGN KEY (event_id) REFERENCES schedule_event(event_id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 2495 (class 2606 OID 28805)
-- Name: fk_grupo_id_area; Type: FK CONSTRAINT; Schema: tenant1; Owner: -
--

ALTER TABLE ONLY grupo
    ADD CONSTRAINT fk_grupo_id_area FOREIGN KEY (id_area) REFERENCES area(id_area) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 2531 (class 2606 OID 47319)
-- Name: fk_id_usuario; Type: FK CONSTRAINT; Schema: tenant1; Owner: -
--

ALTER TABLE ONLY schedule_event_usuario
    ADD CONSTRAINT fk_id_usuario FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 2500 (class 2606 OID 26397)
-- Name: fk_nota_creada_por; Type: FK CONSTRAINT; Schema: tenant1; Owner: -
--

ALTER TABLE ONLY nota
    ADD CONSTRAINT fk_nota_creada_por FOREIGN KEY (creada_por) REFERENCES usuario(id_usuario);


--
-- TOC entry 2501 (class 2606 OID 26402)
-- Name: fk_nota_id_caso; Type: FK CONSTRAINT; Schema: tenant1; Owner: -
--

ALTER TABLE ONLY nota
    ADD CONSTRAINT fk_nota_id_caso FOREIGN KEY (id_caso) REFERENCES caso(id_caso) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 2502 (class 2606 OID 26407)
-- Name: fk_nota_id_tipo_nota; Type: FK CONSTRAINT; Schema: tenant1; Owner: -
--

ALTER TABLE ONLY nota
    ADD CONSTRAINT fk_nota_id_tipo_nota FOREIGN KEY (id_tipo_nota) REFERENCES tipo_nota(id_tipo_nota);


--
-- TOC entry 2530 (class 2606 OID 47309)
-- Name: fk_resource; Type: FK CONSTRAINT; Schema: tenant1; Owner: -
--

ALTER TABLE ONLY schedule_event_resource
    ADD CONSTRAINT fk_resource FOREIGN KEY (id_resource) REFERENCES resource(id_resource) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 2509 (class 2606 OID 26412)
-- Name: fk_rol_funcion_id_funcion; Type: FK CONSTRAINT; Schema: tenant1; Owner: -
--

ALTER TABLE ONLY rol_funcion
    ADD CONSTRAINT fk_rol_funcion_id_funcion FOREIGN KEY (id_funcion) REFERENCES funcion(id_funcion);


--
-- TOC entry 2529 (class 2606 OID 47304)
-- Name: fk_schedule_event; Type: FK CONSTRAINT; Schema: tenant1; Owner: -
--

ALTER TABLE ONLY schedule_event_resource
    ADD CONSTRAINT fk_schedule_event FOREIGN KEY (event_id) REFERENCES schedule_event(event_id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 2532 (class 2606 OID 47324)
-- Name: fk_schedule_event; Type: FK CONSTRAINT; Schema: tenant1; Owner: -
--

ALTER TABLE ONLY schedule_event_usuario
    ADD CONSTRAINT fk_schedule_event FOREIGN KEY (event_id) REFERENCES schedule_event(event_id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 2514 (class 2606 OID 26417)
-- Name: fk_sub_componente_id_componente; Type: FK CONSTRAINT; Schema: tenant1; Owner: -
--

ALTER TABLE ONLY sub_componente
    ADD CONSTRAINT fk_sub_componente_id_componente FOREIGN KEY (id_componente) REFERENCES componente(id_componente);


--
-- TOC entry 2517 (class 2606 OID 26422)
-- Name: fk_sub_estado_caso_id_estado; Type: FK CONSTRAINT; Schema: tenant1; Owner: -
--

ALTER TABLE ONLY sub_estado_caso
    ADD CONSTRAINT fk_sub_estado_caso_id_estado FOREIGN KEY (id_estado) REFERENCES estado_caso(id_estado);


--
-- TOC entry 2443 (class 2606 OID 26427)
-- Name: fk_tipo_accion; Type: FK CONSTRAINT; Schema: tenant1; Owner: -
--

ALTER TABLE ONLY accion
    ADD CONSTRAINT fk_tipo_accion FOREIGN KEY (id_tipo_accion) REFERENCES tipo_accion(id_tipo_accion);


--
-- TOC entry 2512 (class 2606 OID 26432)
-- Name: fk_tipo_accion_schedule_event; Type: FK CONSTRAINT; Schema: tenant1; Owner: -
--

ALTER TABLE ONLY schedule_event
    ADD CONSTRAINT fk_tipo_accion_schedule_event FOREIGN KEY (id_tipo_accion) REFERENCES tipo_accion(id_tipo_accion);


--
-- TOC entry 2518 (class 2606 OID 26437)
-- Name: fk_tipo_caso; Type: FK CONSTRAINT; Schema: tenant1; Owner: -
--

ALTER TABLE ONLY sub_estado_caso
    ADD CONSTRAINT fk_tipo_caso FOREIGN KEY (id_tipo_caso) REFERENCES tipo_caso(id_tipo_caso);


--
-- TOC entry 2524 (class 2606 OID 28835)
-- Name: fk_usuario_grupo_id_grupo; Type: FK CONSTRAINT; Schema: tenant1; Owner: -
--

ALTER TABLE ONLY usuario_grupo
    ADD CONSTRAINT fk_usuario_grupo_id_grupo FOREIGN KEY (id_grupo) REFERENCES grupo(id_grupo) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 2523 (class 2606 OID 28800)
-- Name: fk_usuario_grupo_id_usuario; Type: FK CONSTRAINT; Schema: tenant1; Owner: -
--

ALTER TABLE ONLY usuario_grupo
    ADD CONSTRAINT fk_usuario_grupo_id_usuario FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 2471 (class 2606 OID 28845)
-- Name: grupo_categoria_grupo; Type: FK CONSTRAINT; Schema: tenant1; Owner: -
--

ALTER TABLE ONLY categoria_grupo
    ADD CONSTRAINT grupo_categoria_grupo FOREIGN KEY (id_grupo) REFERENCES grupo(id_grupo) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 2475 (class 2606 OID 26457)
-- Name: grupo_clipping; Type: FK CONSTRAINT; Schema: tenant1; Owner: -
--

ALTER TABLE ONLY clipping
    ADD CONSTRAINT grupo_clipping FOREIGN KEY (id_grupo) REFERENCES grupo(id_grupo);


--
-- TOC entry 2496 (class 2606 OID 26462)
-- Name: grupo_fk; Type: FK CONSTRAINT; Schema: tenant1; Owner: -
--

ALTER TABLE ONLY grupo_producto
    ADD CONSTRAINT grupo_fk FOREIGN KEY (id_grupo) REFERENCES grupo(id_grupo);


--
-- TOC entry 2526 (class 2606 OID 26467)
-- Name: grupo_vista; Type: FK CONSTRAINT; Schema: tenant1; Owner: -
--

ALTER TABLE ONLY vista
    ADD CONSTRAINT grupo_vista FOREIGN KEY (id_grupo) REFERENCES grupo(id_grupo);


--
-- TOC entry 2521 (class 2606 OID 26472)
-- Name: id_grupo_fk; Type: FK CONSTRAINT; Schema: tenant1; Owner: -
--

ALTER TABLE ONLY usuario
    ADD CONSTRAINT id_grupo_fk FOREIGN KEY (id_grupo) REFERENCES grupo(id_grupo) ON UPDATE SET DEFAULT ON DELETE SET DEFAULT;


--
-- TOC entry 2458 (class 2606 OID 26477)
-- Name: id_modelo_fk; Type: FK CONSTRAINT; Schema: tenant1; Owner: -
--

ALTER TABLE ONLY caso
    ADD CONSTRAINT id_modelo_fk FOREIGN KEY (id_modelo, id_producto) REFERENCES modelo_producto(id_modelo, id_producto);


--
-- TOC entry 2490 (class 2606 OID 26482)
-- Name: id_usuario_fk; Type: FK CONSTRAINT; Schema: tenant1; Owner: -
--

ALTER TABLE ONLY etiqueta
    ADD CONSTRAINT id_usuario_fk FOREIGN KEY (id_usuario_created_by) REFERENCES usuario(id_usuario);


--
-- TOC entry 2515 (class 2606 OID 26487)
-- Name: modelo_producto_sub_componente; Type: FK CONSTRAINT; Schema: tenant1; Owner: -
--

ALTER TABLE ONLY sub_componente
    ADD CONSTRAINT modelo_producto_sub_componente FOREIGN KEY (id_modelo, id_producto) REFERENCES modelo_producto(id_modelo, id_producto);


--
-- TOC entry 2459 (class 2606 OID 26492)
-- Name: pk_caso_tipo_caso; Type: FK CONSTRAINT; Schema: tenant1; Owner: -
--

ALTER TABLE ONLY caso
    ADD CONSTRAINT pk_caso_tipo_caso FOREIGN KEY (tipo_caso) REFERENCES tipo_caso(id_tipo_caso);


--
-- TOC entry 2497 (class 2606 OID 26497)
-- Name: product_fk; Type: FK CONSTRAINT; Schema: tenant1; Owner: -
--

ALTER TABLE ONLY grupo_producto
    ADD CONSTRAINT product_fk FOREIGN KEY (id_producto) REFERENCES producto(id_producto);


--
-- TOC entry 2499 (class 2606 OID 26502)
-- Name: producto_modelo_producto; Type: FK CONSTRAINT; Schema: tenant1; Owner: -
--

ALTER TABLE ONLY modelo_producto
    ADD CONSTRAINT producto_modelo_producto FOREIGN KEY (id_producto) REFERENCES producto(id_producto);


--
-- TOC entry 2505 (class 2606 OID 26507)
-- Name: producto_producto_contratado; Type: FK CONSTRAINT; Schema: tenant1; Owner: -
--

ALTER TABLE ONLY producto_contratado
    ADD CONSTRAINT producto_producto_contratado FOREIGN KEY (id_producto) REFERENCES producto(id_producto);


--
-- TOC entry 2508 (class 2606 OID 26512)
-- Name: regla_trigger_regla_trigger_area; Type: FK CONSTRAINT; Schema: tenant1; Owner: -
--

ALTER TABLE ONLY regla_trigger_area
    ADD CONSTRAINT regla_trigger_regla_trigger_area FOREIGN KEY (id_trigger) REFERENCES regla_trigger(id_trigger);


--
-- TOC entry 2510 (class 2606 OID 26517)
-- Name: rol_rol_funcion; Type: FK CONSTRAINT; Schema: tenant1; Owner: -
--

ALTER TABLE ONLY rol_funcion
    ADD CONSTRAINT rol_rol_funcion FOREIGN KEY (id_rol) REFERENCES rol(id_rol);


--
-- TOC entry 2519 (class 2606 OID 26522)
-- Name: rol_user_rol; Type: FK CONSTRAINT; Schema: tenant1; Owner: -
--

ALTER TABLE ONLY user_rol
    ADD CONSTRAINT rol_user_rol FOREIGN KEY (id_rol) REFERENCES rol(id_rol);


--
-- TOC entry 2506 (class 2606 OID 26527)
-- Name: sub_componente_producto_contratado; Type: FK CONSTRAINT; Schema: tenant1; Owner: -
--

ALTER TABLE ONLY producto_contratado
    ADD CONSTRAINT sub_componente_producto_contratado FOREIGN KEY (id_sub_componente) REFERENCES sub_componente(id_sub_componente);


--
-- TOC entry 2473 (class 2606 OID 26532)
-- Name: sub_estado_caso_categoria_sub_estado_caso; Type: FK CONSTRAINT; Schema: tenant1; Owner: -
--

ALTER TABLE ONLY categoria_sub_estado_caso
    ADD CONSTRAINT sub_estado_caso_categoria_sub_estado_caso FOREIGN KEY (id_sub_estado) REFERENCES sub_estado_caso(id_sub_estado);


--
-- TOC entry 2483 (class 2606 OID 26537)
-- Name: tipo_accion_custom_field_tipo_accion; Type: FK CONSTRAINT; Schema: tenant1; Owner: -
--

ALTER TABLE ONLY custom_field_tipo_accion
    ADD CONSTRAINT tipo_accion_custom_field_tipo_accion FOREIGN KEY (id_tipo_accion) REFERENCES tipo_accion(id_tipo_accion);


--
-- TOC entry 2485 (class 2606 OID 26542)
-- Name: tipo_caso_custom_field_tipo_caso; Type: FK CONSTRAINT; Schema: tenant1; Owner: -
--

ALTER TABLE ONLY custom_field_tipo_caso
    ADD CONSTRAINT tipo_caso_custom_field_tipo_caso FOREIGN KEY (id_tipo_caso) REFERENCES tipo_caso(id_tipo_caso);


--
-- TOC entry 2480 (class 2606 OID 26547)
-- Name: tipo_comparacion_condicion; Type: FK CONSTRAINT; Schema: tenant1; Owner: -
--

ALTER TABLE ONLY condicion
    ADD CONSTRAINT tipo_comparacion_condicion FOREIGN KEY (id_comparador) REFERENCES tipo_comparacion(id_comparador);


--
-- TOC entry 2493 (class 2606 OID 26552)
-- Name: tipo_comparacion_filtros_vista; Type: FK CONSTRAINT; Schema: tenant1; Owner: -
--

ALTER TABLE ONLY filtro_vista
    ADD CONSTRAINT tipo_comparacion_filtros_vista FOREIGN KEY (id_comparador) REFERENCES tipo_comparacion(id_comparador);


--
-- TOC entry 2476 (class 2606 OID 26557)
-- Name: user; Type: FK CONSTRAINT; Schema: tenant1; Owner: -
--

ALTER TABLE ONLY clipping
    ADD CONSTRAINT "user" FOREIGN KEY (creada_por) REFERENCES usuario(id_usuario);


--
-- TOC entry 2487 (class 2606 OID 26562)
-- Name: usuario_faq; Type: FK CONSTRAINT; Schema: tenant1; Owner: -
--

ALTER TABLE ONLY documento
    ADD CONSTRAINT usuario_faq FOREIGN KEY (created_by) REFERENCES usuario(id_usuario);


--
-- TOC entry 2513 (class 2606 OID 26567)
-- Name: usuario_schedule_event; Type: FK CONSTRAINT; Schema: tenant1; Owner: -
--

ALTER TABLE ONLY schedule_event
    ADD CONSTRAINT usuario_schedule_event FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario);


--
-- TOC entry 2520 (class 2606 OID 26572)
-- Name: usuario_user_rol; Type: FK CONSTRAINT; Schema: tenant1; Owner: -
--

ALTER TABLE ONLY user_rol
    ADD CONSTRAINT usuario_user_rol FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario);


--
-- TOC entry 2522 (class 2606 OID 26577)
-- Name: usuario_usuario; Type: FK CONSTRAINT; Schema: tenant1; Owner: -
--

ALTER TABLE ONLY usuario
    ADD CONSTRAINT usuario_usuario FOREIGN KEY (supervisor) REFERENCES usuario(id_usuario);


--
-- TOC entry 2527 (class 2606 OID 26582)
-- Name: usuario_vista_casos; Type: FK CONSTRAINT; Schema: tenant1; Owner: -
--

ALTER TABLE ONLY vista
    ADD CONSTRAINT usuario_vista_casos FOREIGN KEY (id_usuario_creada_por) REFERENCES usuario(id_usuario);


--
-- TOC entry 2494 (class 2606 OID 26587)
-- Name: vista_casos_filtros_vista; Type: FK CONSTRAINT; Schema: tenant1; Owner: -
--

ALTER TABLE ONLY filtro_vista
    ADD CONSTRAINT vista_casos_filtros_vista FOREIGN KEY (id_vista) REFERENCES vista(id_vista) ON UPDATE CASCADE ON DELETE CASCADE;


-- Completed on 2014-07-21 20:40:04 CLT

--
-- PostgreSQL database dump complete
--

