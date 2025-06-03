package com.tien.repository;

import com.tien.model.Order;
import jakarta.persistence.NoResultException;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OrderRepositoryImpl implements OrderRepository {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public Order save(Order order) {
        sessionFactory.getCurrentSession().persist(order);
        return order;
    }

    @Override
    public void update(Order order) {
        sessionFactory.getCurrentSession().merge(order);
    }

    @Override
    public Order findById(int orderId) {
        return sessionFactory.getCurrentSession().get(Order.class, orderId);
    }


    @Override
    public Order findByIdAndCustomerId(int orderId, int customerId) {
        try {
            return sessionFactory.getCurrentSession()
                    .createQuery("FROM Order WHERE id = :orderId AND customerId = :customerId", Order.class)
                    .setParameter("orderId", orderId)
                    .setParameter("customerId", customerId)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public List<Order> findByCustomerId(int customerId, int page, int size) {
        return sessionFactory.getCurrentSession()
                .createQuery("FROM Order WHERE customerId = :customerId ORDER BY id DESC", Order.class) // Thêm ORDER BY
                .setParameter("customerId", customerId)
                .setFirstResult((page - 1) * size)
                .setMaxResults(size)
                .list();
    }

    @Override
    public long countByCustomerId(int customerId) {
        return sessionFactory.getCurrentSession()
                .createQuery("SELECT COUNT(o.id) FROM Order o WHERE o.customerId = :customerId", Long.class)
                .setParameter("customerId", customerId)
                .uniqueResult();
    }
}