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

    @Transactional
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

    @Transactional
    public <T> T save(T entity) {
        entityManager.persist(entity);
        entityManager.flush();
        return entity;
    }

    @Transactional
    public <T> T merge(T entity) {
        entityManager.merge(entity);
        entityManager.flush();
        return entity;
    }
}
