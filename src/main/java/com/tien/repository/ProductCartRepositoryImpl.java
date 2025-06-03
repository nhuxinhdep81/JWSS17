package com.tien.repository;

import com.tien.model.ProductCart;
import jakarta.persistence.NoResultException;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProductCartRepositoryImpl implements ProductCartRepository {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public ProductCart saveOrUpdate(ProductCart productCart) {
        sessionFactory.getCurrentSession().saveOrUpdate(productCart);
        return productCart;
    }

    @Override
    public ProductCart findByCustomerIdAndProductId(int customerId, int productId) {
        try {
            String hql = "FROM ProductCart pc JOIN FETCH pc.product WHERE pc.customerId = :customerId AND pc.product.id = :productId";
            return sessionFactory.getCurrentSession()
                    .createQuery(hql, ProductCart.class)
                    .setParameter("customerId", customerId)
                    .setParameter("productId", productId)
                    .uniqueResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public List<ProductCart> findByCustomerId(int customerId) {
        String hql = "FROM ProductCart pc JOIN FETCH pc.product WHERE pc.customerId = :customerId";
        return sessionFactory.getCurrentSession()
                .createQuery(hql, ProductCart.class)
                .setParameter("customerId", customerId)
                .list();
    }

    @Override
    public ProductCart findByIdAndCustomerId(int cartId, int customerId) {
        try {
            String hql = "FROM ProductCart pc JOIN FETCH pc.product WHERE pc.id = :cartId AND pc.customerId = :customerId";
            return sessionFactory.getCurrentSession()
                    .createQuery(hql, ProductCart.class)
                    .setParameter("cartId", cartId)
                    .setParameter("customerId", customerId)
                    .uniqueResult();
        } catch (NoResultException e) {
            return null;
        }
    }


    @Override
    public void delete(ProductCart productCart) {
        sessionFactory.getCurrentSession().delete(productCart);
    }

    @Override
    public void deleteAllByCustomerId(int customerId) {
        String hql = "DELETE FROM ProductCart pc WHERE pc.customerId = :customerId";
        sessionFactory.getCurrentSession().createQuery(hql)
                .setParameter("customerId", customerId)
                .executeUpdate();
    }
}