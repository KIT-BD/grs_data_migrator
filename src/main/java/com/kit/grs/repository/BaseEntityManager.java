package com.kit.grs.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.*;

@Slf4j
@Repository
public class BaseEntityManager {

    @PersistenceContext
    private EntityManager entityManager;

    public <T> T  findSingleByQuery(String nativeQuery,Map<String, Object> params) {
        Query query = entityManager.createNativeQuery(nativeQuery);
        if (params != null && params.size() >0) {
            params.forEach(query::setParameter);
        }
        try {
            return (T) query.getSingleResult();
        } catch (NoResultException ne) {
            return null;
        }
    }

    public <T> T  findSingleByQuery(String nativeQuery,Class<T> entity,Map<String, Object> params) {
        Query query = entityManager.createNativeQuery(nativeQuery, entity);
        if (params != null && params.size() >0) {
            params.forEach(query::setParameter);
        }
        query.setFirstResult(0).setMaxResults(1);
        try {
            List<T> list = query.getResultList();
            if (list.size() >0) {
                return list.get(0);
            }
            return null;
        } catch (Exception ne) {
            return null;
        }
    }

    @Transactional
    public <T> T save(T entity) {
        entityManager.persist(entity);
        entityManager.flush();
        return entity;
    }

    @Transactional
    public <T> T delete(T entity) {
        entityManager.remove(entity);
        entityManager.flush();
        return entity;
    }

    @Transactional
    public <T> T merge(T entity) {
        entityManager.merge(entity);
        entityManager.flush();
        return entity;
    }

    @Transactional
    public boolean deleteBySql(String sql) {
        Query query = entityManager.createNativeQuery(sql);
        query.executeUpdate();
        return true;
    }
}
