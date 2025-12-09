package com.ues.edu.controler.persistencia;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Gaby Laínez
 */
public class JpaControler<T> implements Serializable {

    private final EntityManagerFactory emf;
    private final Class<T> entityClass;

    public JpaControler(Class<T> entityClass) {
        this.emf = Persistence.createEntityManagerFactory("EclipseLink_JPA");
        this.entityClass = entityClass;
    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public String create(T entity) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(entity);
            em.getTransaction().commit();
            return "exito";
        } finally {
            em.close();
        }
    }

    public String edit(T entity) throws Exception {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(entity);
            em.getTransaction().commit();
            return "exito";
        } catch (Exception ex) {
            throw new Exception("Error al editar la entidad", ex);
        } finally {
            em.close();
        }
    }

    public String destroy(Object id) throws Exception {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            T entity = em.getReference(entityClass, id);
            em.remove(entity);
            em.getTransaction().commit();
            return "exito";
        } catch (Exception ex) {
            throw new Exception("Entidad no encontrada", ex);
        } finally {
            em.close();
        }
    }

    public T find(Object id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(entityClass, id);
        } finally {
            em.close();
        }
    }

    public List<T> findAll() {
        return findEntities(true, -1, -1);
    }

    public List<T> findEntities(int maxResults, int firstResult) {
        return findEntities(false, maxResults, firstResult);
    }

    private List<T> findEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery<T> cq = em.getCriteriaBuilder().createQuery(entityClass);
            cq.select(cq.from(entityClass));
            Query query = em.createQuery(cq);
            if (!all) {
                query.setMaxResults(maxResults);
                query.setFirstResult(firstResult);
            }
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public int getEntityCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery<Long> cq = em.getCriteriaBuilder().createQuery(Long.class);
            Root<T> rt = cq.from(entityClass);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query query = em.createQuery(cq);
            return ((Long) query.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
    

