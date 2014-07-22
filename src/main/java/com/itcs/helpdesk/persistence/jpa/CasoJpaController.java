/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itcs.helpdesk.persistence.jpa;

import com.itcs.helpdesk.persistence.entities.Archivo;
import com.itcs.helpdesk.persistence.entities.Attachment;
import com.itcs.helpdesk.persistence.entities.Canal;
import com.itcs.helpdesk.persistence.entities.Caso;
import com.itcs.helpdesk.persistence.entities.Categoria;
import com.itcs.helpdesk.persistence.entities.Componente;
import com.itcs.helpdesk.persistence.entities.Documento;
import com.itcs.helpdesk.persistence.entities.EmailCliente;
import com.itcs.helpdesk.persistence.entities.EstadoCaso;
import com.itcs.helpdesk.persistence.entities.Etiqueta;
import com.itcs.helpdesk.persistence.entities.FiltroVista;
import com.itcs.helpdesk.persistence.entities.Nota;
import com.itcs.helpdesk.persistence.entities.Prioridad;
import com.itcs.helpdesk.persistence.entities.Producto;
import com.itcs.helpdesk.persistence.entities.SubComponente;
import com.itcs.helpdesk.persistence.entities.SubEstadoCaso;
import com.itcs.helpdesk.persistence.entities.TipoAlerta;
import com.itcs.helpdesk.persistence.entities.Usuario;
import com.itcs.helpdesk.persistence.jpa.exceptions.IllegalOrphanException;
import com.itcs.helpdesk.persistence.jpa.exceptions.NonexistentEntityException;
import com.itcs.helpdesk.persistence.jpa.exceptions.PreexistingEntityException;
import com.itcs.helpdesk.persistence.jpa.exceptions.RollbackFailureException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.UserTransaction;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import org.eclipse.persistence.config.EntityManagerProperties;
import org.eclipse.persistence.config.HintValues;
import org.eclipse.persistence.config.QueryHints;

/**
 *
 * @author jonathan
 */
public class CasoJpaController extends AbstractJPAController implements Serializable {

    public CasoJpaController(UserTransaction utx, EntityManagerFactory emf, String schema) {
        super(utx, emf, schema);
    }
//    public CasoJpaController(UserTransaction utx, EntityManagerFactory emf) {
//        this.utx = utx;
//        this.emf = emf;
//    }

    public void create(Caso caso) throws PreexistingEntityException, RollbackFailureException, Exception {
//        if (caso.getCasosRelacionadosList() == null) {
//            caso.setCasosRelacionadosList(new ArrayList<Caso>());
//        }
//        if (caso.getCasoList1() == null) {
//            caso.setCasoList1(new ArrayList<Caso>());
//        }
//        if (caso.getDocumentoList() == null) {
//            caso.setDocumentoList(new ArrayList<Documento>());
//        }
//        if (caso.getCasosHijosList() == null) {
//            caso.setCasosHijosList(new ArrayList<Caso>());
//        }
//        if (caso.getNotaList() == null) {
//            caso.setNotaList(new ArrayList<Nota>());
//        }

        List<Archivo> archivos = new ArrayList<Archivo>();
        if (caso.getAttachmentList() != null) {
            for (Attachment attachment : caso.getAttachmentList()) {
                if (attachment.getArchivo() != null) {
                    final Archivo archivo = attachment.getArchivo();
                    archivo.setContentType(attachment.getMimeType());
                    archivo.setFileName(attachment.getNombreArchivo());
                    archivos.add(archivo);
                }
            }
        }

        EntityManager em = null;
        try {
            utx.begin();
            em = createEntityManager();
            em.setProperty(EntityManagerProperties.MULTITENANT_PROPERTY_DEFAULT, getSchema());
            Usuario owner = caso.getOwner();
            if (owner != null) {
                owner = em.getReference(owner.getClass(), owner.getIdUsuario());
                caso.setOwner(owner);
            }
            TipoAlerta estadoAlerta = caso.getEstadoAlerta();
            if (estadoAlerta != null) {
                estadoAlerta = em.getReference(estadoAlerta.getClass(), estadoAlerta.getIdalerta());
                caso.setEstadoAlerta(estadoAlerta);
            }
            SubEstadoCaso idSubEstado = caso.getIdSubEstado();
            if (idSubEstado != null) {
                idSubEstado = em.getReference(idSubEstado.getClass(), idSubEstado.getIdSubEstado());
                caso.setIdSubEstado(idSubEstado);
            }
            SubComponente idSubComponente = caso.getIdSubComponente();
            if (idSubComponente != null) {
                idSubComponente = em.getReference(idSubComponente.getClass(), idSubComponente.getIdSubComponente());
                caso.setIdSubComponente(idSubComponente);
            }
            Producto idProducto = caso.getIdProducto();
            if (idProducto != null) {
                idProducto = em.getReference(idProducto.getClass(), idProducto.getIdProducto());
                caso.setIdProducto(idProducto);
            }
            Prioridad idPrioridad = caso.getIdPrioridad();
            if (idPrioridad != null) {
                idPrioridad = em.getReference(idPrioridad.getClass(), idPrioridad.getIdPrioridad());
                caso.setIdPrioridad(idPrioridad);
            }
            EstadoCaso idEstado = caso.getIdEstado();
            if (idEstado != null) {
                idEstado = em.getReference(idEstado.getClass(), idEstado.getIdEstado());
                caso.setIdEstado(idEstado);
            }
            EmailCliente emailCliente = caso.getEmailCliente();
            if (emailCliente != null) {
                emailCliente = em.getReference(emailCliente.getClass(), emailCliente.getEmailCliente());
                caso.setEmailCliente(emailCliente);
            }
            Componente idComponente = caso.getIdComponente();
            if (idComponente != null) {
                idComponente = em.getReference(idComponente.getClass(), idComponente.getIdComponente());
                caso.setIdComponente(idComponente);
            }
            Categoria idCategoria = caso.getIdCategoria();
            if (idCategoria != null) {
                idCategoria = em.getReference(idCategoria.getClass(), idCategoria.getIdCategoria());
                caso.setIdCategoria(idCategoria);
            }
            Caso idCasoPadre = caso.getIdCasoPadre();
            if (idCasoPadre != null) {
                idCasoPadre = em.getReference(idCasoPadre.getClass(), idCasoPadre.getIdCaso());
                caso.setIdCasoPadre(idCasoPadre);
            }
            Canal idCanal = caso.getIdCanal();
            if (idCanal != null) {
                idCanal = em.getReference(idCanal.getClass(), idCanal.getIdCanal());
                caso.setIdCanal(idCanal);
            }
            try {
                if (caso.getCasosRelacionadosList() != null) {
                    List<Caso> attachedCasoList = new ArrayList<Caso>();
                    for (Caso casoListCasoToAttach : caso.getCasosRelacionadosList()) {
                        casoListCasoToAttach = em.getReference(casoListCasoToAttach.getClass(), casoListCasoToAttach.getIdCaso());
                        attachedCasoList.add(casoListCasoToAttach);
                    }
                    caso.setCasosRelacionadosList(attachedCasoList);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

//            List<Caso> attachedCasoList1 = new ArrayList<Caso>();
//            for (Caso casoList1CasoToAttach : caso.getCasoList1()) {
//                casoList1CasoToAttach = em.getReference(casoList1CasoToAttach.getClass(), casoList1CasoToAttach.getIdCaso());
//                attachedCasoList1.add(casoList1CasoToAttach);
//            }
//            caso.setCasoList1(attachedCasoList1);
//            List<Documento> attachedDocumentoList = new ArrayList<Documento>();
//            for (Documento documentoListDocumentoToAttach : caso.getDocumentoList()) {
//                documentoListDocumentoToAttach = em.getReference(documentoListDocumentoToAttach.getClass(), documentoListDocumentoToAttach.getIdDocumento());
//                attachedDocumentoList.add(documentoListDocumentoToAttach);
//            }
//            caso.setDocumentoList(attachedDocumentoList);
            if (caso.getCasosHijosList() != null) {
                List<Caso> attachedCasosHijosList = new ArrayList<Caso>();
                for (Caso casoList2CasoToAttach : caso.getCasosHijosList()) {
                    casoList2CasoToAttach = em.getReference(casoList2CasoToAttach.getClass(), casoList2CasoToAttach.getIdCaso());
                    attachedCasosHijosList.add(casoList2CasoToAttach);
                }
                caso.setCasosHijosList(attachedCasosHijosList);
            }

//            List<Nota> attachedNotaList = new ArrayList<Nota>();
//            for (Nota notaListNotaToAttach : caso.getNotaList()) {
//                notaListNotaToAttach = em.getReference(notaListNotaToAttach.getClass(), notaListNotaToAttach.getIdNota());
//                attachedNotaList.add(notaListNotaToAttach);
//            }
//            caso.setNotaList(attachedNotaList);
            try {
                if (caso.getAttachmentList() != null) {
                    List<Attachment> attachedAttachmentList = new ArrayList<Attachment>();
                    for (Attachment attachmentListAttachmentToAttach : caso.getAttachmentList()) {

                        attachmentListAttachmentToAttach.setIdAttachment(null);
                        em.persist(attachmentListAttachmentToAttach);

                        attachedAttachmentList.add(attachmentListAttachmentToAttach);
                    }
                    caso.setAttachmentList(attachedAttachmentList);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                if (caso.getEtiquetaList() != null) {
                    List<Etiqueta> attachedEtiquetaList = new ArrayList<Etiqueta>();
                    for (Etiqueta etiquetaListEtiquetaToAttach : caso.getEtiquetaList()) {

                        if (em.find(etiquetaListEtiquetaToAttach.getClass(), etiquetaListEtiquetaToAttach.getTagId()) == null) {
                            em.persist(etiquetaListEtiquetaToAttach);
                        } else {
                            etiquetaListEtiquetaToAttach = em.getReference(etiquetaListEtiquetaToAttach.getClass(), etiquetaListEtiquetaToAttach.getTagId());
                        }

                        attachedEtiquetaList.add(etiquetaListEtiquetaToAttach);
                    }
                    caso.setEtiquetaList(attachedEtiquetaList);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            em.persist(caso);

            em.flush();

            if (!archivos.isEmpty()) {
                for (Attachment attachment : caso.getAttachmentList()) {
                    for (Archivo archivo : archivos) {

                        if (attachment.getNombreArchivo() != null && attachment.getNombreArchivo().equals(archivo.getFileName())) {

                            archivo.setIdAttachment(attachment.getIdAttachment());

                        }

                    }

                }
            }

            for (Archivo archivo : archivos) {
                em.persist(archivo);
            }
//            if (owner != null) {
//                owner.getCasoList().add(caso);
//                owner = em.merge(owner);
//            }
//            if (estadoAlerta != null) {
//                estadoAlerta.getCasoList().add(caso);
//                estadoAlerta = em.merge(estadoAlerta);
//            }
//            if (idSubEstado != null) {
//                idSubEstado.getCasoList().add(caso);
//                idSubEstado = em.merge(idSubEstado);
//            }
//            if (idSubComponente != null) {
//                idSubComponente.getCasoList().add(caso);
//                idSubComponente = em.merge(idSubComponente);
//            }
//            if (idProducto != null) {
//                idProducto.getCasoList().add(caso);
//                idProducto = em.merge(idProducto);
//            }
//            if (idPrioridad != null) {
//                idPrioridad.getCasoList().add(caso);
//                idPrioridad = em.merge(idPrioridad);
//            }
//            if (idEstado != null) {
//                idEstado.getCasoList().add(caso);
//                idEstado = em.merge(idEstado);
//            }
//            if (emailCliente != null) {
//                emailCliente.getCasoList().add(caso);
//                emailCliente = em.merge(emailCliente);
//            }
//            if (idComponente != null) {
//                idComponente.getCasoList().add(caso);
//                idComponente = em.merge(idComponente);
//            }
//            if (idCategoria != null) {
//                idCategoria.getCasoList().add(caso);
//                idCategoria = em.merge(idCategoria);
//            }
//            if (idCasoPadre != null) {
//                idCasoPadre.getCasosRelacionadosList().add(caso);
//                idCasoPadre = em.merge(idCasoPadre);
//            }
//            if (idCanal != null) {
//                idCanal.getCasoList().add(caso);
//                idCanal = em.merge(idCanal);
//            }
//
//            for (Caso casoListCaso : caso.getCasosRelacionadosList()) {
//                casoListCaso.getCasosRelacionadosList().add(caso);
//                casoListCaso = em.merge(casoListCaso);
//            }
////            for (Caso casoList1Caso : caso.getCasoList1()) {
////                casoList1Caso.getCasosRelacionadosList().add(caso);
////                casoList1Caso = em.merge(casoList1Caso);
////            }
//            for (Documento documentoListDocumento : caso.getDocumentoList()) {
//                documentoListDocumento.getCasoList().add(caso);
//                documentoListDocumento = em.merge(documentoListDocumento);
//            }
//
//            for (Caso casoList2Caso : caso.getCasosHijosList()) {
//                Caso oldIdCasoPadreOfCasosHijosListCaso = casoList2Caso.getIdCasoPadre();
//                casoList2Caso.setIdCasoPadre(caso);
//                casoList2Caso = em.merge(casoList2Caso);
//                if (oldIdCasoPadreOfCasosHijosListCaso != null) {
//                    oldIdCasoPadreOfCasosHijosListCaso.getCasosHijosList().remove(casoList2Caso);
//                    oldIdCasoPadreOfCasosHijosListCaso = em.merge(oldIdCasoPadreOfCasosHijosListCaso);
//                }
//            }

//            for (Nota notaListNota : caso.getNotaList()) {
//                Caso oldIdCasoOfNotaListNota = notaListNota.getIdCaso();
//                notaListNota.setIdCaso(caso);
//                notaListNota = em.merge(notaListNota);
//                if (oldIdCasoOfNotaListNota != null) {
//                    oldIdCasoOfNotaListNota.getNotaList().remove(notaListNota);
//                    oldIdCasoOfNotaListNota = em.merge(oldIdCasoOfNotaListNota);
//                }
//            }
//            for (Attachment attachmentListAttachment : caso.getAttachmentList()) {
//                Caso oldIdCasoOfAttachmentListAttachment = attachmentListAttachment.getIdCaso();
//                attachmentListAttachment.setIdCaso(caso);
//                attachmentListAttachment = em.merge(attachmentListAttachment);
//                if (oldIdCasoOfAttachmentListAttachment != null) {
//                    oldIdCasoOfAttachmentListAttachment.getAttachmentList().remove(attachmentListAttachment);
//                    oldIdCasoOfAttachmentListAttachment = em.merge(oldIdCasoOfAttachmentListAttachment);
//                }
//            }
            if (caso.getEtiquetaList() != null) {
                for (Etiqueta etiquetaListEtiqueta : caso.getEtiquetaList()) {
                    if (etiquetaListEtiqueta.getCasoList() == null) {
                        etiquetaListEtiqueta.setCasoList(new ArrayList<Caso>());
                    }
                    etiquetaListEtiqueta.getCasoList().add(caso);
                    etiquetaListEtiqueta = em.merge(etiquetaListEtiqueta);
                }
            }

            em.flush();

            utx.commit();
        } catch (Exception ex) {
            if (ex instanceof ConstraintViolationException) {
                printOutContraintViolation((ConstraintViolationException) ex);
            }

            if (ex.getCause() instanceof ConstraintViolationException) {
                printOutContraintViolation((ConstraintViolationException) (ex.getCause()));
            }
            ex.printStackTrace();
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (caso.getIdCaso() != null) {
                if (findCaso(caso.getIdCaso()) != null) {
                    throw new PreexistingEntityException("Caso " + caso + " already exists.", ex);
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

//    public void edit(Caso caso) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
//        EntityManager em = null;
//        try {
//            utx.begin();
//            em = getEntityManager(); em.setProperty(EntityManagerProperties.MULTITENANT_PROPERTY_DEFAULT, schema);
//            Caso persistentCaso = em.find(Caso.class, caso.getIdCaso());
//            Usuario ownerOld = persistentCaso.getOwner();
//            Usuario ownerNew = caso.getOwner();
//            TipoAlerta estadoAlertaOld = persistentCaso.getEstadoAlerta();
//            TipoAlerta estadoAlertaNew = caso.getEstadoAlerta();
//            SubEstadoCaso idSubEstadoOld = persistentCaso.getIdSubEstado();
//            SubEstadoCaso idSubEstadoNew = caso.getIdSubEstado();
//            SubComponente idSubComponenteOld = persistentCaso.getIdSubComponente();
//            SubComponente idSubComponenteNew = caso.getIdSubComponente();
//            Producto idProductoOld = persistentCaso.getIdProducto();
//            Producto idProductoNew = caso.getIdProducto();
//            Prioridad idPrioridadOld = persistentCaso.getIdPrioridad();
//            Prioridad idPrioridadNew = caso.getIdPrioridad();
//            EstadoCaso idEstadoOld = persistentCaso.getIdEstado();
//            EstadoCaso idEstadoNew = caso.getIdEstado();
//            EmailCliente emailClienteOld = persistentCaso.getEmailCliente();
//            EmailCliente emailClienteNew = caso.getEmailCliente();
//            Componente idComponenteOld = persistentCaso.getIdComponente();
//            Componente idComponenteNew = caso.getIdComponente();
//            Categoria idCategoriaOld = persistentCaso.getIdCategoria();
//            Categoria idCategoriaNew = caso.getIdCategoria();
//            Caso idCasoPadreOld = persistentCaso.getIdCasoPadre();
//            Caso idCasoPadreNew = caso.getIdCasoPadre();
//            Canal idCanalOld = persistentCaso.getIdCanal();
//            Canal idCanalNew = caso.getIdCanal();
////            List<Caso> casoListOld = persistentCaso.getCasosRelacionadosList();
////            List<Caso> casoListNew = caso.getCasosRelacionadosList();
////            List<Caso> casoList1Old = persistentCaso.getCasoList1();
////            List<Caso> casoList1New = caso.getCasoList1();
////            List<Documento> documentoListOld = persistentCaso.getDocumentoList();
////            List<Documento> documentoListNew = caso.getDocumentoList();
////            List<Caso> casoList2Old = persistentCaso.getCasosHijosList();
////            List<Caso> casoList2New = caso.getCasosHijosList();
////            List<Nota> notaListOld = persistentCaso.getNotaList();
////            List<Nota> notaListNew = caso.getNotaList();
////            List<Attachment> attachmentListOld = persistentCaso.getAttachmentList();
////            List<Attachment> attachmentListNew = caso.getAttachmentList();
//            List<String> illegalOrphanMessages = null;
////            for (Nota notaListOldNota : notaListOld) {
////                if (!notaListNew.contains(notaListOldNota)) {
////                    if (illegalOrphanMessages == null) {
////                        illegalOrphanMessages = new ArrayList<String>();
////                    }
////                    illegalOrphanMessages.add("You must retain Nota " + notaListOldNota + " since its idCaso field is not nullable.");
////                }
////            }
//            if (illegalOrphanMessages != null) {
//                throw new IllegalOrphanException(illegalOrphanMessages);
//            }
//            if (ownerNew != null) {
//                ownerNew = em.getReference(ownerNew.getClass(), ownerNew.getIdUsuario());
//                caso.setOwner(ownerNew);
//            }
//            if (estadoAlertaNew != null) {
//                estadoAlertaNew = em.getReference(estadoAlertaNew.getClass(), estadoAlertaNew.getIdalerta());
//                caso.setEstadoAlerta(estadoAlertaNew);
//            }
//            if (idSubEstadoNew != null) {
//                idSubEstadoNew = em.getReference(idSubEstadoNew.getClass(), idSubEstadoNew.getIdSubEstado());
//                caso.setIdSubEstado(idSubEstadoNew);
//            }
//            if (idSubComponenteNew != null) {
//                idSubComponenteNew = em.getReference(idSubComponenteNew.getClass(), idSubComponenteNew.getIdSubComponente());
//                caso.setIdSubComponente(idSubComponenteNew);
//            }
//            if (idProductoNew != null) {
//                idProductoNew = em.getReference(idProductoNew.getClass(), idProductoNew.getIdProducto());
//                caso.setIdProducto(idProductoNew);
//            }
//            if (idPrioridadNew != null) {
//                idPrioridadNew = em.getReference(idPrioridadNew.getClass(), idPrioridadNew.getIdPrioridad());
//                caso.setIdPrioridad(idPrioridadNew);
//            }
//            if (idEstadoNew != null) {
//                idEstadoNew = em.getReference(idEstadoNew.getClass(), idEstadoNew.getIdEstado());
//                caso.setIdEstado(idEstadoNew);
//            }
//            if (emailClienteNew != null) {
//                emailClienteNew = em.getReference(emailClienteNew.getClass(), emailClienteNew.getEmailCliente());
//                caso.setEmailCliente(emailClienteNew);
//            }
//            if (idComponenteNew != null) {
//                idComponenteNew = em.getReference(idComponenteNew.getClass(), idComponenteNew.getIdComponente());
//                caso.setIdComponente(idComponenteNew);
//            }
//            if (idCategoriaNew != null) {
//                idCategoriaNew = em.getReference(idCategoriaNew.getClass(), idCategoriaNew.getIdCategoria());
//                caso.setIdCategoria(idCategoriaNew);
//            }
//            if (idCasoPadreNew != null) {
//                idCasoPadreNew = em.getReference(idCasoPadreNew.getClass(), idCasoPadreNew.getIdCaso());
//                caso.setIdCasoPadre(idCasoPadreNew);
//            }
//            if (idCanalNew != null) {
//                idCanalNew = em.getReference(idCanalNew.getClass(), idCanalNew.getIdCanal());
//                caso.setIdCanal(idCanalNew);
//            }
////            List<Caso> attachedCasoListNew = new ArrayList<Caso>();
////            for (Caso casoListNewCasoToAttach : casoListNew) {
////                casoListNewCasoToAttach = em.getReference(casoListNewCasoToAttach.getClass(), casoListNewCasoToAttach.getIdCaso());
////                attachedCasoListNew.add(casoListNewCasoToAttach);
////            }
////            casoListNew = attachedCasoListNew;
////            caso.setCasosRelacionadosList(casoListNew);
////            List<Caso> attachedCasoList1New = new ArrayList<Caso>();
////            for (Caso casoList1NewCasoToAttach : casoList1New) {
////                casoList1NewCasoToAttach = em.getReference(casoList1NewCasoToAttach.getClass(), casoList1NewCasoToAttach.getIdCaso());
////                attachedCasoList1New.add(casoList1NewCasoToAttach);
////            }
////            casoList1New = attachedCasoList1New;
////            caso.setCasoList1(casoList1New);
////            List<Documento> attachedDocumentoListNew = new ArrayList<Documento>();
////            for (Documento documentoListNewDocumentoToAttach : documentoListNew) {
////                documentoListNewDocumentoToAttach = em.getReference(documentoListNewDocumentoToAttach.getClass(), documentoListNewDocumentoToAttach.getIdDocumento());
////                attachedDocumentoListNew.add(documentoListNewDocumentoToAttach);
////            }
////            documentoListNew = attachedDocumentoListNew;
////            caso.setDocumentoList(documentoListNew);
////            List<Caso> attachedCasosHijosListNew = new ArrayList<Caso>();
////            for (Caso casoList2NewCasoToAttach : casoList2New) {
////                casoList2NewCasoToAttach = em.getReference(casoList2NewCasoToAttach.getClass(), casoList2NewCasoToAttach.getIdCaso());
////                attachedCasosHijosListNew.add(casoList2NewCasoToAttach);
////            }
////            casoList2New = attachedCasosHijosListNew;
////            caso.setCasosHijosList(casoList2New);
//
////            List<Nota> attachedNotaListNew = new ArrayList<Nota>();
////            for (Nota notaListNewNotaToAttach : notaListNew) {
////                notaListNewNotaToAttach = em.getReference(notaListNewNotaToAttach.getClass(), notaListNewNotaToAttach.getIdNota());
////                attachedNotaListNew.add(notaListNewNotaToAttach);
////            }
////            notaListNew = attachedNotaListNew;
////            caso.setNotaList(notaListNew);
////            List<Attachment> attachedAttachmentListNew = new ArrayList<Attachment>();
////            for (Attachment attachmentListNewAttachmentToAttach : attachmentListNew) {
////                attachmentListNewAttachmentToAttach = em.getReference(attachmentListNewAttachmentToAttach.getClass(), attachmentListNewAttachmentToAttach.getIdAttachment());
////                attachedAttachmentListNew.add(attachmentListNewAttachmentToAttach);
////            }
////            attachmentListNew = attachedAttachmentListNew;
////            caso.setAttachmentList(attachmentListNew);
//            caso = em.merge(caso);
//            if (ownerOld != null && !ownerOld.equals(ownerNew)) {
//                ownerOld.getCasoList().remove(caso);
//                ownerOld = em.merge(ownerOld);
//            }
//            if (ownerNew != null && !ownerNew.equals(ownerOld)) {
//                ownerNew.getCasoList().add(caso);
//                ownerNew = em.merge(ownerNew);
//            }
//            if (estadoAlertaOld != null && !estadoAlertaOld.equals(estadoAlertaNew)) {
//                estadoAlertaOld.getCasoList().remove(caso);
//                estadoAlertaOld = em.merge(estadoAlertaOld);
//            }
//            if (estadoAlertaNew != null && !estadoAlertaNew.equals(estadoAlertaOld)) {
//                estadoAlertaNew.getCasoList().add(caso);
//                estadoAlertaNew = em.merge(estadoAlertaNew);
//            }
//            if (idSubEstadoOld != null && !idSubEstadoOld.equals(idSubEstadoNew)) {
//                idSubEstadoOld.getCasoList().remove(caso);
//                idSubEstadoOld = em.merge(idSubEstadoOld);
//            }
//            if (idSubEstadoNew != null && !idSubEstadoNew.equals(idSubEstadoOld)) {
//                idSubEstadoNew.getCasoList().add(caso);
//                idSubEstadoNew = em.merge(idSubEstadoNew);
//            }
//            if (idSubComponenteOld != null && !idSubComponenteOld.equals(idSubComponenteNew)) {
//                idSubComponenteOld.getCasoList().remove(caso);
//                idSubComponenteOld = em.merge(idSubComponenteOld);
//            }
//            if (idSubComponenteNew != null && !idSubComponenteNew.equals(idSubComponenteOld)) {
//                idSubComponenteNew.getCasoList().add(caso);
//                idSubComponenteNew = em.merge(idSubComponenteNew);
//            }
//            if (idProductoOld != null && !idProductoOld.equals(idProductoNew)) {
//                idProductoOld.getCasoList().remove(caso);
//                idProductoOld = em.merge(idProductoOld);
//            }
//            if (idProductoNew != null && !idProductoNew.equals(idProductoOld)) {
//                idProductoNew.getCasoList().add(caso);
//                idProductoNew = em.merge(idProductoNew);
//            }
//            if (idPrioridadOld != null && !idPrioridadOld.equals(idPrioridadNew)) {
//                idPrioridadOld.getCasoList().remove(caso);
//                idPrioridadOld = em.merge(idPrioridadOld);
//            }
//            if (idPrioridadNew != null && !idPrioridadNew.equals(idPrioridadOld)) {
//                idPrioridadNew.getCasoList().add(caso);
//                idPrioridadNew = em.merge(idPrioridadNew);
//            }
//            if (idEstadoOld != null && !idEstadoOld.equals(idEstadoNew)) {
//                idEstadoOld.getCasoList().remove(caso);
//                idEstadoOld = em.merge(idEstadoOld);
//            }
//            if (idEstadoNew != null && !idEstadoNew.equals(idEstadoOld)) {
//                idEstadoNew.getCasoList().add(caso);
//                idEstadoNew = em.merge(idEstadoNew);
//            }
//            if (emailClienteOld != null && !emailClienteOld.equals(emailClienteNew)) {
//                emailClienteOld.getCasoList().remove(caso);
//                emailClienteOld = em.merge(emailClienteOld);
//            }
//            if (emailClienteNew != null && !emailClienteNew.equals(emailClienteOld)) {
//                emailClienteNew.getCasoList().add(caso);
//                emailClienteNew = em.merge(emailClienteNew);
//            }
//            if (idComponenteOld != null && !idComponenteOld.equals(idComponenteNew)) {
//                idComponenteOld.getCasoList().remove(caso);
//                idComponenteOld = em.merge(idComponenteOld);
//            }
//            if (idComponenteNew != null && !idComponenteNew.equals(idComponenteOld)) {
//                idComponenteNew.getCasoList().add(caso);
//                idComponenteNew = em.merge(idComponenteNew);
//            }
//            if (idCategoriaOld != null && !idCategoriaOld.equals(idCategoriaNew)) {
//                idCategoriaOld.getCasoList().remove(caso);
//                idCategoriaOld = em.merge(idCategoriaOld);
//            }
//            if (idCategoriaNew != null && !idCategoriaNew.equals(idCategoriaOld)) {
//                idCategoriaNew.getCasoList().add(caso);
//                idCategoriaNew = em.merge(idCategoriaNew);
//            }
//            if (idCasoPadreOld != null && !idCasoPadreOld.equals(idCasoPadreNew)) {
//                idCasoPadreOld.getCasosRelacionadosList().remove(caso);
//                idCasoPadreOld = em.merge(idCasoPadreOld);
//            }
//            if (idCasoPadreNew != null && !idCasoPadreNew.equals(idCasoPadreOld)) {
//                idCasoPadreNew.getCasosRelacionadosList().add(caso);
//                idCasoPadreNew = em.merge(idCasoPadreNew);
//            }
//            if (idCanalOld != null && !idCanalOld.equals(idCanalNew)) {
//                idCanalOld.getCasoList().remove(caso);
//                idCanalOld = em.merge(idCanalOld);
//            }
//            if (idCanalNew != null && !idCanalNew.equals(idCanalOld)) {
//                idCanalNew.getCasoList().add(caso);
//                idCanalNew = em.merge(idCanalNew);
//            }
////            for (Caso casoListOldCaso : casoListOld) {
////                if (!casoListNew.contains(casoListOldCaso)) {
////                    casoListOldCaso.getCasosRelacionadosList().remove(caso);
////                    casoListOldCaso = em.merge(casoListOldCaso);
////                }
////            }
////            for (Caso casoListNewCaso : casoListNew) {
////                if (!casoListOld.contains(casoListNewCaso)) {
////                    casoListNewCaso.getCasosRelacionadosList().add(caso);
////                    casoListNewCaso = em.merge(casoListNewCaso);
////                }
////            }
////            for (Caso casoList1OldCaso : casoList1Old) {
////                if (!casoList1New.contains(casoList1OldCaso)) {
////                    casoList1OldCaso.getCasosRelacionadosList().remove(caso);
////                    casoList1OldCaso = em.merge(casoList1OldCaso);
////                }
////            }
////            for (Caso casoList1NewCaso : casoList1New) {
////                if (!casoList1Old.contains(casoList1NewCaso)) {
////                    casoList1NewCaso.getCasosRelacionadosList().add(caso);
////                    casoList1NewCaso = em.merge(casoList1NewCaso);
////                }
////            }
////            for (Documento documentoListOldDocumento : documentoListOld) {
////                if (!documentoListNew.contains(documentoListOldDocumento)) {
////                    documentoListOldDocumento.getCasoList().remove(caso);
////                    documentoListOldDocumento = em.merge(documentoListOldDocumento);
////                }
////            }
////            for (Documento documentoListNewDocumento : documentoListNew) {
////                if (!documentoListOld.contains(documentoListNewDocumento)) {
////                    documentoListNewDocumento.getCasoList().add(caso);
////                    documentoListNewDocumento = em.merge(documentoListNewDocumento);
////                }
////            }
////            for (Caso casoList2OldCaso : casoList2Old) {
////                if (!casoList2New.contains(casoList2OldCaso)) {
////                    casoList2OldCaso.setIdCasoPadre(null);
////                    casoList2OldCaso = em.merge(casoList2OldCaso);
////                }
////            }
////            for (Caso casoList2NewCaso : casoList2New) {
////                if (!casoList2Old.contains(casoList2NewCaso)) {
////                    Caso oldIdCasoPadreOfCasosHijosListNewCaso = casoList2NewCaso.getIdCasoPadre();
////                    casoList2NewCaso.setIdCasoPadre(caso);
////                    casoList2NewCaso = em.merge(casoList2NewCaso);
////                    if (oldIdCasoPadreOfCasosHijosListNewCaso != null && !oldIdCasoPadreOfCasosHijosListNewCaso.equals(caso)) {
////                        oldIdCasoPadreOfCasosHijosListNewCaso.getCasosHijosList().remove(casoList2NewCaso);
////                        oldIdCasoPadreOfCasosHijosListNewCaso = em.merge(oldIdCasoPadreOfCasosHijosListNewCaso);
////                    }
////                }
////            }
//
//
////            for (Nota notaListNewNota : notaListNew) {
////                if (!notaListOld.contains(notaListNewNota)) {
////                    Caso oldIdCasoOfNotaListNewNota = notaListNewNota.getIdCaso();
////                    notaListNewNota.setIdCaso(caso);
////                    notaListNewNota = em.merge(notaListNewNota);
////                    if (oldIdCasoOfNotaListNewNota != null && !oldIdCasoOfNotaListNewNota.equals(caso)) {
////                        oldIdCasoOfNotaListNewNota.getNotaList().remove(notaListNewNota);
////                        oldIdCasoOfNotaListNewNota = em.merge(oldIdCasoOfNotaListNewNota);
////                    }
////                }
////            }
//            
////            for (Attachment attachmentListOldAttachment : attachmentListOld) {
////                if (!attachmentListNew.contains(attachmentListOldAttachment)) {
////                    attachmentListOldAttachment.setIdCaso(null);
////                    attachmentListOldAttachment = em.merge(attachmentListOldAttachment);
////                }
////            }
////            for (Attachment attachmentListNewAttachment : attachmentListNew) {
////                if (!attachmentListOld.contains(attachmentListNewAttachment)) {
////                    Caso oldIdCasoOfAttachmentListNewAttachment = attachmentListNewAttachment.getIdCaso();
////                    attachmentListNewAttachment.setIdCaso(caso);
////                    attachmentListNewAttachment = em.merge(attachmentListNewAttachment);
////                    if (oldIdCasoOfAttachmentListNewAttachment != null && !oldIdCasoOfAttachmentListNewAttachment.equals(caso)) {
////                        oldIdCasoOfAttachmentListNewAttachment.getAttachmentList().remove(attachmentListNewAttachment);
////                        oldIdCasoOfAttachmentListNewAttachment = em.merge(oldIdCasoOfAttachmentListNewAttachment);
////                    }
////                }
////            }
//            utx.commit();
//        } catch (Exception ex) {
//            if (ex instanceof ConstraintViolationException) {
//                printOutContraintViolation((ConstraintViolationException) ex);
//            }
//
//            if (ex.getCause() instanceof ConstraintViolationException) {
//                printOutContraintViolation((ConstraintViolationException) (ex.getCause()));
//            }
//            try {
//                utx.rollback();
//            } catch (Exception re) {
//                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
//            }
//            String msg = ex.getLocalizedMessage();
//            if (msg == null || msg.length() == 0) {
//                Long id = caso.getIdCaso();
//                if (findCaso(id) == null) {
//                    throw new NonexistentEntityException("The caso with id " + id + " no longer exists.");
//                }
//            }
//            throw ex;
//        } finally {
//            if (em != null) {
//                em.close();
//            }
//        }
//    }
    public void destroy(Long id) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = createEntityManager();
            em.setProperty(EntityManagerProperties.MULTITENANT_PROPERTY_DEFAULT, getSchema());
            Caso caso;
            try {
                caso = em.getReference(Caso.class, id);
                caso.getIdCaso();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The caso with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Nota> notaListOrphanCheck = caso.getNotaList();
            for (Nota notaListOrphanCheckNota : notaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Caso (" + caso + ") cannot be destroyed since the Nota " + notaListOrphanCheckNota + " in its notaList field has a non-nullable idCaso field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Usuario owner = caso.getOwner();
            if (owner != null) {
                owner.getCasoList().remove(caso);
                owner = em.merge(owner);
            }
            TipoAlerta estadoAlerta = caso.getEstadoAlerta();
            if (estadoAlerta != null) {
                estadoAlerta.getCasoList().remove(caso);
                estadoAlerta = em.merge(estadoAlerta);
            }
            SubEstadoCaso idSubEstado = caso.getIdSubEstado();
            if (idSubEstado != null) {
                idSubEstado.getCasoList().remove(caso);
                idSubEstado = em.merge(idSubEstado);
            }
            SubComponente idSubComponente = caso.getIdSubComponente();
            if (idSubComponente != null) {
                idSubComponente.getCasoList().remove(caso);
                idSubComponente = em.merge(idSubComponente);
            }
            Producto idProducto = caso.getIdProducto();
            if (idProducto != null) {
                idProducto.getCasoList().remove(caso);
                idProducto = em.merge(idProducto);
            }
            Prioridad idPrioridad = caso.getIdPrioridad();
            if (idPrioridad != null) {
                idPrioridad.getCasoList().remove(caso);
                idPrioridad = em.merge(idPrioridad);
            }
            EstadoCaso idEstado = caso.getIdEstado();
            if (idEstado != null) {
                idEstado.getCasoList().remove(caso);
                idEstado = em.merge(idEstado);
            }
            EmailCliente emailCliente = caso.getEmailCliente();
            if (emailCliente != null) {
                emailCliente.getCasoList().remove(caso);
                emailCliente = em.merge(emailCliente);
            }
            Componente idComponente = caso.getIdComponente();
            if (idComponente != null) {
                idComponente.getCasoList().remove(caso);
                idComponente = em.merge(idComponente);
            }
            Categoria idCategoria = caso.getIdCategoria();
            if (idCategoria != null) {
                idCategoria.getCasoList().remove(caso);
                idCategoria = em.merge(idCategoria);
            }
            Caso idCasoPadre = caso.getIdCasoPadre();
            if (idCasoPadre != null) {
                idCasoPadre.getCasosRelacionadosList().remove(caso);
                idCasoPadre = em.merge(idCasoPadre);
            }
            Canal idCanal = caso.getIdCanal();
            if (idCanal != null) {
                idCanal.getCasoList().remove(caso);
                idCanal = em.merge(idCanal);
            }
            List<Caso> casoList = caso.getCasosRelacionadosList();
            for (Caso casoListCaso : casoList) {
                casoListCaso.getCasosRelacionadosList().remove(caso);
                casoListCaso = em.merge(casoListCaso);
            }
//            List<Caso> casoList1 = caso.getCasoList1();
//            for (Caso casoList1Caso : casoList1) {
//                casoList1Caso.getCasosRelacionadosList().remove(caso);
//                casoList1Caso = em.merge(casoList1Caso);
//            }
//            List<Documento> documentoList = caso.getDocumentoList();
//            for (Documento documentoListDocumento : documentoList) {
//                documentoListDocumento.getCasoList().remove(caso);
//                documentoListDocumento = em.merge(documentoListDocumento);
//            }
            List<Caso> casoList2 = caso.getCasosHijosList();
            for (Caso casoList2Caso : casoList2) {
                casoList2Caso.setIdCasoPadre(null);
                casoList2Caso = em.merge(casoList2Caso);
            }

            List<Attachment> attachmentList = caso.getAttachmentList();
            for (Attachment attachmentListAttachment : attachmentList) {
                attachmentListAttachment.setIdCaso(null);
                attachmentListAttachment = em.merge(attachmentListAttachment);
            }
            em.remove(caso);
            utx.commit();
        } catch (Exception ex) {
            if (ex instanceof ConstraintViolationException) {
                printOutContraintViolation((ConstraintViolationException) ex);
            }

            if (ex.getCause() instanceof ConstraintViolationException) {
                printOutContraintViolation((ConstraintViolationException) (ex.getCause()));
            }
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Caso> findCasoEntities() {
        return findCasoEntities(true, -1, -1);
    }

    public List<Caso> findCasoEntities(int maxResults, int firstResult) {
        return findCasoEntities(false, maxResults, firstResult);
    }

    private List<Caso> findCasoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = createEntityManager();
        em.setProperty(EntityManagerProperties.MULTITENANT_PROPERTY_DEFAULT, getSchema());
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Caso.class));
            Query q = em.createQuery(cq);
            q.setHint(QueryHints.REFRESH, HintValues.TRUE);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Caso findCaso(Long id) {
        EntityManager em = createEntityManager();
        em.setProperty(EntityManagerProperties.MULTITENANT_PROPERTY_DEFAULT, getSchema());
        try {
            return em.find(Caso.class, id);
        } finally {
            em.close();
        }
    }

    public int getCasoCount() {
        EntityManager em = createEntityManager();
        em.setProperty(EntityManagerProperties.MULTITENANT_PROPERTY_DEFAULT, getSchema());
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Caso> rt = cq.from(Caso.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    private void printOutContraintViolation(ConstraintViolationException ex) {
        Set<ConstraintViolation<?>> set = (ex).getConstraintViolations();
        for (ConstraintViolation<?> constraintViolation : set) {
            System.out.println("leafBean class: " + constraintViolation.getLeafBean().getClass());
            Iterator<Path.Node> iter = constraintViolation.getPropertyPath().iterator();
            System.out.println("constraintViolation.getPropertyPath(): ");
            while (iter.hasNext()) {
                System.out.print(iter.next().getName() + "/");
            }
            System.out.println("anotacion: " + constraintViolation.getConstraintDescriptor().getAnnotation().toString() + " value:" + constraintViolation.getInvalidValue());
        }
    }

    @Override
    protected Predicate createSpecialPredicate(FiltroVista filtro) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected boolean isThereSpecialFiltering(FiltroVista filtro) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
