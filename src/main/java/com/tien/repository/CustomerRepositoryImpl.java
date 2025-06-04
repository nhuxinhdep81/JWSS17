package com.tien.repository;

import com.tien.model.Customer;
import jakarta.persistence.NoResultException;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CustomerRepositoryImpl implements CustomerRepository {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public void save(Customer customer) {
        sessionFactory.getCurrentSession().persist(customer);
    }

    @Override
    public void update(Customer customer) {
        sessionFactory.getCurrentSession().merge(customer);
    }

    @Override
    public boolean existsByUsername(String username) {
        Query<Long> query = sessionFactory.getCurrentSession()
                .createQuery("SELECT COUNT(c.id) FROM Customer c WHERE c.username = :username", Long.class);
        query.setParameter("username", username);
        return query.uniqueResult() > 0;
    }

    @Override
    public boolean existsByEmail(String email) {
        Query<Long> query = sessionFactory.getCurrentSession()
                .createQuery("SELECT COUNT(c.id) FROM Customer c WHERE c.email = :email", Long.class);
        query.setParameter("email", email);
        return query.uniqueResult() > 0;
    }

    @Override
    public boolean existsByEmailAndIdNot(String email, int customerId) {
        Query<Long> query = sessionFactory.getCurrentSession()
                .createQuery("SELECT COUNT(c.id) FROM Customer c WHERE c.email = :email AND c.id != :customerId", Long.class);
        query.setParameter("email", email);
        query.setParameter("customerId", customerId);
        return query.uniqueResult() > 0;
    }

    @Override
    public Customer findByUsername(String username) {
        try {
            return sessionFactory.getCurrentSession()
                    .createQuery("FROM Customer WHERE username = :username", Customer.class)
                    .setParameter("username", username)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public Customer findById(int id) {
        return sessionFactory.getCurrentSession().get(Customer.class, id);
    }

    @Override
    public List<Customer> findAllPaginated(int page, int size) {
        return sessionFactory.getCurrentSession()
                .createQuery("FROM Customer where role = 'USER'", Customer.class)
                .setFirstResult((page - 1) * size)
                .setMaxResults(size)
                .list();
    }

    @Override
    public long countAll() {
        return sessionFactory.getCurrentSession()
                .createQuery("SELECT COUNT(c.id) FROM Customer c", Long.class)
                .uniqueResult();
    }

    @Override
    public long countByStatus(boolean status) {
        return sessionFactory.getCurrentSession()
                .createQuery("SELECT COUNT(c.id) FROM Customer c WHERE c.status = :status", Long.class)
                .setParameter("status", status)
                .uniqueResult();
    }

    @Override
    public List<Customer> findByUsernameContainingPaginated(String username, int page, int size) {
        return sessionFactory.getCurrentSession()
                .createQuery("FROM Customer WHERE role = 'USER' AND username LIKE CONCAT('%', :username, '%')", Customer.class)
                .setParameter("username", username)
                .setFirstResult((page - 1) * size)
                .setMaxResults(size)
                .list();
    }

    @Override
    public long countByUsernameContaining(String username) {
        return sessionFactory.getCurrentSession()
                .createQuery("SELECT COUNT(c.id) FROM Customer c WHERE role = 'USER' AND username LIKE CONCAT('%', :username, '%')", Long.class)
                .setParameter("username", username)
                .uniqueResult();
    }
}