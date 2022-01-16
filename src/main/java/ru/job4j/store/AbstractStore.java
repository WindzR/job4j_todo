package ru.job4j.store;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.List;

public abstract class AbstractStore<T> implements AutoCloseable {

    private final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
            .configure().build();

    private final SessionFactory sf = new MetadataSources(registry)
            .buildMetadata().buildSessionFactory();

    public SessionFactory getSf() {
        return sf;
    }

    abstract T save(T entity);

    abstract T update(int id, T entity);

    abstract boolean delete(int id);

    abstract List<T> findAll();

    abstract T findEntityById(int id);

    @Override
    public void close() throws Exception {
        try {
            if (sf.isOpen()) {
                sf.close();
            }
        } catch (HibernateException ex) {
            ex.printStackTrace();
        }
    }
}
