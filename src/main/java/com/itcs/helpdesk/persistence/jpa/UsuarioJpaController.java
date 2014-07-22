/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itcs.helpdesk.persistence.jpa;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.itcs.helpdesk.persistence.entities.Usuario;
import com.itcs.helpdesk.persistence.entities.Grupo;
import com.itcs.helpdesk.persistence.entities.Rol;
import java.util.ArrayList;
import java.util.List;
import com.itcs.helpdesk.persistence.entities.Caso;
import com.itcs.helpdesk.persistence.entities.Documento;
import com.itcs.helpdesk.persistence.entities.Nota;
import com.itcs.helpdesk.persistence.jpa.exceptions.IllegalOrphanException;
import com.itcs.helpdesk.persistence.jpa.exceptions.NonexistentEntityException;
import com.itcs.helpdesk.persistence.jpa.exceptions.PreexistingEntityException;
import com.itcs.helpdesk.persistence.jpa.exceptions.RollbackFailureException;
import com.itcs.helpdesk.persistence.jpa.service.JPAServiceFacade;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.eclipse.persistence.config.EntityManagerProperties;

/**
 *
 * @author jonathan
 */
public class UsuarioJpaController extends AbstractJPAController implements Serializable {

    public UsuarioJpaController(UserTransaction utx, EntityManagerFactory emf, String schema) {
        super(utx, emf, schema);
    }

//public class UsuarioJpaController implements Serializable {
//
//    public UsuarioJpaController(UserTransaction utx, EntityManagerFactory emf) {
//        this.utx = utx;
//        this.emf = emf;
//    }
//    private UserTransaction utx = null;
//    private EntityManagerFactory emf = null;
//
//    public EntityManager getEntityManager() {
//        return emf.createEntityManager();
//    }
    public void create(Usuario usuario) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (usuario.getRolList() == null) {
            usuario.setRolList(new ArrayList<Rol>());
        }
        if (usuario.getCasoList() == null) {
            usuario.setCasoList(new ArrayList<Caso>());
        }
        if (usuario.getDocumentoList() == null) {
            usuario.setDocumentoList(new ArrayList<Documento>());
        }

        if (usuario.getUsuarioList() == null) {
            usuario.setUsuarioList(new ArrayList<Usuario>());
        }
        if (usuario.getNotaList() == null) {
            usuario.setNotaList(new ArrayList<Nota>());
        }

        if (usuario.getGrupoList() == null) {
            usuario.setGrupoList(new ArrayList<Grupo>());
        }

        EntityManager em = null;
        try {
            utx.begin();
            em = createEntityManager();
            em.setProperty(EntityManagerProperties.MULTITENANT_PROPERTY_DEFAULT, getSchema());
            Usuario supervisor = usuario.getSupervisor();
            if (supervisor != null) {
                supervisor = em.getReference(supervisor.getClass(), supervisor.getIdUsuario());
                usuario.setSupervisor(supervisor);
            }
            List<Grupo> attachedGrupoList = new ArrayList<Grupo>();
            for (Grupo grupoListGrupoToAttach : usuario.getGrupoList()) {
                grupoListGrupoToAttach = em.getReference(grupoListGrupoToAttach.getClass(), grupoListGrupoToAttach.getIdGrupo());
                attachedGrupoList.add(grupoListGrupoToAttach);
            }
            usuario.setGrupoList(attachedGrupoList);
            List<Rol> attachedRolList = new ArrayList<Rol>();
            for (Rol rolListRolToAttach : usuario.getRolList()) {
                rolListRolToAttach = em.getReference(rolListRolToAttach.getClass(), rolListRolToAttach.getIdRol());
                attachedRolList.add(rolListRolToAttach);
            }
            usuario.setRolList(attachedRolList);
            List<Caso> attachedCasoList = new ArrayList<Caso>();
            for (Caso casoListCasoToAttach : usuario.getCasoList()) {
                casoListCasoToAttach = em.getReference(casoListCasoToAttach.getClass(), casoListCasoToAttach.getIdCaso());
                attachedCasoList.add(casoListCasoToAttach);
            }
            usuario.setCasoList(attachedCasoList);
            List<Documento> attachedDocumentoList = new ArrayList<Documento>();
            for (Documento documentoListDocumentoToAttach : usuario.getDocumentoList()) {
                documentoListDocumentoToAttach = em.getReference(documentoListDocumentoToAttach.getClass(), documentoListDocumentoToAttach.getIdDocumento());
                attachedDocumentoList.add(documentoListDocumentoToAttach);
            }
            usuario.setDocumentoList(attachedDocumentoList);

            List<Usuario> attachedUsuarioList = new ArrayList<Usuario>();
            for (Usuario usuarioListUsuarioToAttach : usuario.getUsuarioList()) {
                usuarioListUsuarioToAttach = em.getReference(usuarioListUsuarioToAttach.getClass(), usuarioListUsuarioToAttach.getIdUsuario());
                attachedUsuarioList.add(usuarioListUsuarioToAttach);
            }
            usuario.setUsuarioList(attachedUsuarioList);
            List<Nota> attachedNotaList = new ArrayList<Nota>();
            for (Nota notaListNotaToAttach : usuario.getNotaList()) {
                notaListNotaToAttach = em.getReference(notaListNotaToAttach.getClass(), notaListNotaToAttach.getIdNota());
                attachedNotaList.add(notaListNotaToAttach);
            }
            usuario.setNotaList(attachedNotaList);
            em.persist(usuario);
            if (supervisor != null) {
                supervisor.getUsuarioList().add(usuario);
                supervisor = em.merge(supervisor);
            }
            for (Grupo grupoListGrupo : usuario.getGrupoList()) {
                grupoListGrupo.getUsuarioList().add(usuario);
                grupoListGrupo = em.merge(grupoListGrupo);
            }
            for (Rol rolListRol : usuario.getRolList()) {
                rolListRol.getUsuarioList().add(usuario);
                rolListRol = em.merge(rolListRol);
            }
            for (Caso casoListCaso : usuario.getCasoList()) {
                Usuario oldOwnerOfCasoListCaso = casoListCaso.getOwner();
                casoListCaso.setOwner(usuario);
                casoListCaso = em.merge(casoListCaso);
                if (oldOwnerOfCasoListCaso != null) {
                    oldOwnerOfCasoListCaso.getCasoList().remove(casoListCaso);
                    oldOwnerOfCasoListCaso = em.merge(oldOwnerOfCasoListCaso);
                }
            }
            for (Documento documentoListDocumento : usuario.getDocumentoList()) {
                Usuario oldCreatedByOfDocumentoListDocumento = documentoListDocumento.getCreatedBy();
                documentoListDocumento.setCreatedBy(usuario);
                documentoListDocumento = em.merge(documentoListDocumento);
                if (oldCreatedByOfDocumentoListDocumento != null) {
                    oldCreatedByOfDocumentoListDocumento.getDocumentoList().remove(documentoListDocumento);
                    oldCreatedByOfDocumentoListDocumento = em.merge(oldCreatedByOfDocumentoListDocumento);
                }
            }

            for (Usuario usuarioListUsuario : usuario.getUsuarioList()) {
                Usuario oldSupervisorOfUsuarioListUsuario = usuarioListUsuario.getSupervisor();
                usuarioListUsuario.setSupervisor(usuario);
                usuarioListUsuario = em.merge(usuarioListUsuario);
                if (oldSupervisorOfUsuarioListUsuario != null) {
                    oldSupervisorOfUsuarioListUsuario.getUsuarioList().remove(usuarioListUsuario);
                    oldSupervisorOfUsuarioListUsuario = em.merge(oldSupervisorOfUsuarioListUsuario);
                }
            }
            for (Nota notaListNota : usuario.getNotaList()) {
                Usuario oldCreadaPorOfNotaListNota = notaListNota.getCreadaPor();
                notaListNota.setCreadaPor(usuario);
                notaListNota = em.merge(notaListNota);
                if (oldCreadaPorOfNotaListNota != null) {
                    oldCreadaPorOfNotaListNota.getNotaList().remove(notaListNota);
                    oldCreadaPorOfNotaListNota = em.merge(oldCreadaPorOfNotaListNota);
                }

            }
            utx.commit();
        } catch (Exception ex) {
            try {                
                utx.rollback();
            } catch (Exception re) {
                //ignore.
                //throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findUsuario(usuario.getIdUsuario()) != null) {
                throw new PreexistingEntityException("Usuario " + usuario + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Usuario usuario) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = createEntityManager();
            em.setProperty(EntityManagerProperties.MULTITENANT_PROPERTY_DEFAULT, getSchema());
            Usuario persistentUsuario = em.find(Usuario.class, usuario.getIdUsuario());
            Usuario supervisorOld = persistentUsuario.getSupervisor();
            Usuario supervisorNew = usuario.getSupervisor();
            List<Grupo> grupoListOld = persistentUsuario.getGrupoList();
            List<Grupo> grupoListNew = usuario.getGrupoList();
            List<Rol> rolListOld = persistentUsuario.getRolList();
            List<Rol> rolListNew = usuario.getRolList();
            List<Caso> casoListOld = persistentUsuario.getCasoList();
            List<Caso> casoListNew = usuario.getCasoList();
            List<Documento> documentoListOld = persistentUsuario.getDocumentoList();
            List<Documento> documentoListNew = usuario.getDocumentoList();
            List<Usuario> usuarioListOld = persistentUsuario.getUsuarioList();
            List<Usuario> usuarioListNew = usuario.getUsuarioList();
            List<Nota> notaListOld = persistentUsuario.getNotaList();
            List<Nota> notaListNew = usuario.getNotaList();
            List<String> illegalOrphanMessages = null;
            for (Documento documentoListOldDocumento : documentoListOld) {
                if (!documentoListNew.contains(documentoListOldDocumento)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Documento " + documentoListOldDocumento + " since its createdBy field is not nullable.");
                }
            }
            for (Nota notaListOldNota : notaListOld) {
                if (!notaListNew.contains(notaListOldNota)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Nota " + notaListOldNota + " since its creadaPor field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (supervisorNew != null) {
                supervisorNew = em.getReference(supervisorNew.getClass(), supervisorNew.getIdUsuario());
                usuario.setSupervisor(supervisorNew);
            }

            List<Grupo> attachedGrupoListNew = new ArrayList<Grupo>();
            for (Grupo grupoListNewGrupoToAttach : grupoListNew) {
                grupoListNewGrupoToAttach = em.getReference(grupoListNewGrupoToAttach.getClass(), grupoListNewGrupoToAttach.getIdGrupo());
                attachedGrupoListNew.add(grupoListNewGrupoToAttach);
            }
            grupoListNew = attachedGrupoListNew;
            usuario.setGrupoList(grupoListNew);

            List<Rol> attachedRolListNew = new ArrayList<Rol>();
            for (Rol rolListNewRolToAttach : rolListNew) {
                rolListNewRolToAttach = em.getReference(rolListNewRolToAttach.getClass(), rolListNewRolToAttach.getIdRol());
                attachedRolListNew.add(rolListNewRolToAttach);
            }
            rolListNew = attachedRolListNew;
            usuario.setRolList(rolListNew);
            List<Caso> attachedCasoListNew = new ArrayList<Caso>();
            for (Caso casoListNewCasoToAttach : casoListNew) {
                casoListNewCasoToAttach = em.getReference(casoListNewCasoToAttach.getClass(), casoListNewCasoToAttach.getIdCaso());
                attachedCasoListNew.add(casoListNewCasoToAttach);
            }
            casoListNew = attachedCasoListNew;
            usuario.setCasoList(casoListNew);
            List<Documento> attachedDocumentoListNew = new ArrayList<Documento>();
            for (Documento documentoListNewDocumentoToAttach : documentoListNew) {
                documentoListNewDocumentoToAttach = em.getReference(documentoListNewDocumentoToAttach.getClass(), documentoListNewDocumentoToAttach.getIdDocumento());
                attachedDocumentoListNew.add(documentoListNewDocumentoToAttach);
            }
            documentoListNew = attachedDocumentoListNew;
            usuario.setDocumentoList(documentoListNew);

            List<Usuario> attachedUsuarioListNew = new ArrayList<Usuario>();
            for (Usuario usuarioListNewUsuarioToAttach : usuarioListNew) {
                usuarioListNewUsuarioToAttach = em.getReference(usuarioListNewUsuarioToAttach.getClass(), usuarioListNewUsuarioToAttach.getIdUsuario());
                attachedUsuarioListNew.add(usuarioListNewUsuarioToAttach);
            }
            usuarioListNew = attachedUsuarioListNew;
            usuario.setUsuarioList(usuarioListNew);
            List<Nota> attachedNotaListNew = new ArrayList<Nota>();
            for (Nota notaListNewNotaToAttach : notaListNew) {
                notaListNewNotaToAttach = em.getReference(notaListNewNotaToAttach.getClass(), notaListNewNotaToAttach.getIdNota());
                attachedNotaListNew.add(notaListNewNotaToAttach);
            }
            notaListNew = attachedNotaListNew;
            usuario.setNotaList(notaListNew);
            usuario = em.merge(usuario);
            if (supervisorOld != null && !supervisorOld.equals(supervisorNew)) {
                supervisorOld.getUsuarioList().remove(usuario);
                supervisorOld = em.merge(supervisorOld);
            }
            if (supervisorNew != null && !supervisorNew.equals(supervisorOld)) {
                supervisorNew.getUsuarioList().add(usuario);
                supervisorNew = em.merge(supervisorNew);
            }
            for (Grupo grupoListOldGrupo : grupoListOld) {
                if (!grupoListNew.contains(grupoListOldGrupo)) {
                    grupoListOldGrupo.getUsuarioList().remove(usuario);
                    grupoListOldGrupo = em.merge(grupoListOldGrupo);
                }
            }
            for (Grupo grupoListNewGrupo : grupoListNew) {
                if (!grupoListOld.contains(grupoListNewGrupo)) {
                    grupoListNewGrupo.getUsuarioList().add(usuario);
                    grupoListNewGrupo = em.merge(grupoListNewGrupo);
                }
            }
            for (Rol rolListOldRol : rolListOld) {
                if (!rolListNew.contains(rolListOldRol)) {
                    rolListOldRol.getUsuarioList().remove(usuario);
                    rolListOldRol = em.merge(rolListOldRol);
                }
            }
            for (Rol rolListNewRol : rolListNew) {
                if (!rolListOld.contains(rolListNewRol)) {
                    rolListNewRol.getUsuarioList().add(usuario);
                    rolListNewRol = em.merge(rolListNewRol);
                }
            }
            for (Caso casoListOldCaso : casoListOld) {
                if (!casoListNew.contains(casoListOldCaso)) {
                    casoListOldCaso.setOwner(null);
                    casoListOldCaso = em.merge(casoListOldCaso);
                }
            }
            for (Caso casoListNewCaso : casoListNew) {
                if (!casoListOld.contains(casoListNewCaso)) {
                    Usuario oldOwnerOfCasoListNewCaso = casoListNewCaso.getOwner();
                    casoListNewCaso.setOwner(usuario);
                    casoListNewCaso = em.merge(casoListNewCaso);
                    if (oldOwnerOfCasoListNewCaso != null && !oldOwnerOfCasoListNewCaso.equals(usuario)) {
                        oldOwnerOfCasoListNewCaso.getCasoList().remove(casoListNewCaso);
                        oldOwnerOfCasoListNewCaso = em.merge(oldOwnerOfCasoListNewCaso);
                    }
                }
            }
            for (Documento documentoListNewDocumento : documentoListNew) {
                if (!documentoListOld.contains(documentoListNewDocumento)) {
                    Usuario oldCreatedByOfDocumentoListNewDocumento = documentoListNewDocumento.getCreatedBy();
                    documentoListNewDocumento.setCreatedBy(usuario);
                    documentoListNewDocumento = em.merge(documentoListNewDocumento);
                    if (oldCreatedByOfDocumentoListNewDocumento != null && !oldCreatedByOfDocumentoListNewDocumento.equals(usuario)) {
                        oldCreatedByOfDocumentoListNewDocumento.getDocumentoList().remove(documentoListNewDocumento);
                        oldCreatedByOfDocumentoListNewDocumento = em.merge(oldCreatedByOfDocumentoListNewDocumento);
                    }
                }
            }

            for (Usuario usuarioListOldUsuario : usuarioListOld) {
                if (!usuarioListNew.contains(usuarioListOldUsuario)) {
                    usuarioListOldUsuario.setSupervisor(null);
                    usuarioListOldUsuario = em.merge(usuarioListOldUsuario);
                }
            }
            for (Usuario usuarioListNewUsuario : usuarioListNew) {
                if (!usuarioListOld.contains(usuarioListNewUsuario)) {
                    Usuario oldSupervisorOfUsuarioListNewUsuario = usuarioListNewUsuario.getSupervisor();
                    usuarioListNewUsuario.setSupervisor(usuario);
                    usuarioListNewUsuario = em.merge(usuarioListNewUsuario);
                    if (oldSupervisorOfUsuarioListNewUsuario != null && !oldSupervisorOfUsuarioListNewUsuario.equals(usuario)) {
                        oldSupervisorOfUsuarioListNewUsuario.getUsuarioList().remove(usuarioListNewUsuario);
                        oldSupervisorOfUsuarioListNewUsuario = em.merge(oldSupervisorOfUsuarioListNewUsuario);
                    }
                }
            }
            for (Nota notaListNewNota : notaListNew) {
                if (!notaListOld.contains(notaListNewNota)) {
                    Usuario oldCreadaPorOfNotaListNewNota = notaListNewNota.getCreadaPor();
                    notaListNewNota.setCreadaPor(usuario);
                    notaListNewNota = em.merge(notaListNewNota);
                    if (oldCreadaPorOfNotaListNewNota != null && !oldCreadaPorOfNotaListNewNota.equals(usuario)) {
                        oldCreadaPorOfNotaListNewNota.getNotaList().remove(notaListNewNota);
                        oldCreadaPorOfNotaListNewNota = em.merge(oldCreadaPorOfNotaListNewNota);
                    }
                }
            }
            utx.commit();
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "edit usuario failed!", ex);
            try {
                utx.rollback();
            } catch (Exception re) {
                //ignore
                //throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = usuario.getIdUsuario();
                if (findUsuario(id) == null) {
                    throw new NonexistentEntityException("The usuario with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = createEntityManager();
            em.setProperty(EntityManagerProperties.MULTITENANT_PROPERTY_DEFAULT, getSchema());
            Usuario usuario;
            try {
                usuario = em.getReference(Usuario.class, id);
                usuario.getIdUsuario();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The usuario with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Documento> documentoListOrphanCheck = usuario.getDocumentoList();
            for (Documento documentoListOrphanCheckDocumento : documentoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuario (" + usuario + ") cannot be destroyed since the Documento " + documentoListOrphanCheckDocumento + " in its documentoList field has a non-nullable createdBy field.");
            }
            List<Nota> notaListOrphanCheck = usuario.getNotaList();
            for (Nota notaListOrphanCheckNota : notaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuario (" + usuario + ") cannot be destroyed since the Nota " + notaListOrphanCheckNota + " in its notaList field has a non-nullable creadaPor field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Usuario supervisor = usuario.getSupervisor();
            if (supervisor != null) {
                supervisor.getUsuarioList().remove(usuario);
                supervisor = em.merge(supervisor);
            }
            List<Grupo> grupoList = usuario.getGrupoList();
            for (Grupo grupoListGrupo : grupoList) {
                grupoListGrupo.getUsuarioList().remove(usuario);
                grupoListGrupo = em.merge(grupoListGrupo);
            }
            List<Rol> rolList = usuario.getRolList();
            for (Rol rolListRol : rolList) {
                rolListRol.getUsuarioList().remove(usuario);
                rolListRol = em.merge(rolListRol);
            }
            List<Caso> casoList = usuario.getCasoList();
            for (Caso casoListCaso : casoList) {
                casoListCaso.setOwner(null);
                casoListCaso = em.merge(casoListCaso);
            }

            List<Usuario> usuarioList = usuario.getUsuarioList();
            for (Usuario usuarioListUsuario : usuarioList) {
                usuarioListUsuario.setSupervisor(null);
                usuarioListUsuario = em.merge(usuarioListUsuario);
            }
            em.remove(usuario);
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

    private void printOutContraintViolation(ConstraintViolationException ex) {
        Set<ConstraintViolation<?>> set = (ex).getConstraintViolations();
        for (ConstraintViolation<?> constraintViolation : set) {
            System.out.println("leafBean class: " + constraintViolation.getLeafBean().getClass());
            System.out.println("anotacion: " + constraintViolation.getConstraintDescriptor().getAnnotation().toString() + " value:" + constraintViolation.getInvalidValue());
        }
    }

    public List<Usuario> findUsuarioEntities() {
        return findUsuarioEntities(true, -1, -1);
    }

    public List<Usuario> findUsuarioEntities(int maxResults, int firstResult) {
        return findUsuarioEntities(false, maxResults, firstResult);
    }

    private List<Usuario> findUsuarioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = createEntityManager();
        em.setProperty(EntityManagerProperties.MULTITENANT_PROPERTY_DEFAULT, getSchema());
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Usuario.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Usuario findUsuario(String id) {
        EntityManager em = createEntityManager();
        em.setProperty(EntityManagerProperties.MULTITENANT_PROPERTY_DEFAULT, getSchema());
        try {
            return em.find(Usuario.class, id);
        } finally {
            em.close();
        }
    }

    public int getUsuarioCount() {
        EntityManager em = createEntityManager();
        em.setProperty(EntityManagerProperties.MULTITENANT_PROPERTY_DEFAULT, getSchema());
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Usuario> rt = cq.from(Usuario.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
}
