package ru.job4j.store;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import ru.job4j.entity.Item;

import javax.persistence.Query;
import java.util.List;

public class HbmStore implements Store, AutoCloseable {

    private final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
            .configure().build();

    private final SessionFactory sf = new MetadataSources(registry)
            .buildMetadata().buildSessionFactory();

    private static final class Lazy {
        private static final Store INST = new HbmStore();
    }

    public static Store instOf() {
        return Lazy.INST;
    }

    @Override
    public Item add(Item item) {
        Session session = sf.getCurrentSession();
        session.beginTransaction();
        session.save(item);
        session.getTransaction().commit();
        return item;
    }

    @Override
    public boolean replace(int id, Item item) {
        boolean rsl;
        Session session = sf.getCurrentSession();
        session.beginTransaction();
        Query query = session.createQuery(
                "update Item set name = :nameParam,"
                        + " description = :descriptionParam,"
                        + " created = :createdParam,"
                        + " done = :doneParam"
                        + " where id = :idParam");
        query.setParameter("nameParam", item.getName());
        query.setParameter("descriptionParam", item.getDescription());
        query.setParameter("createdParam", item.getCreated());
        query.setParameter("doneParam", item.isDone());
        query.setParameter("idParam", id);
        int update = query.executeUpdate();
        session.getTransaction().commit();
        rsl = update > 0;
        return rsl;
    }

    @Override
    public boolean delete(int id) {
        boolean rsl;
        Session session = sf.getCurrentSession();
        session.beginTransaction();
        Query query = session.createQuery("delete Item where id = :idParam");
        query.setParameter("idParam", id);
        int delete = query.executeUpdate();
        session.getTransaction().commit();
        rsl = delete > 0;
        return rsl;
    }

    @Override
    public List<Item> findAll() {
        List<Item> items = null;
        Session session = sf.getCurrentSession();
        session.beginTransaction();
        items = session.createQuery("from Item")
                .getResultList();
        session.getTransaction().commit();
        return items;
    }

    @Override
    public List<Item> findAllUnfinished() {
        List<Item> items = null;
        Session session = sf.getCurrentSession();
        session.beginTransaction();
        items = session.createQuery("from Item where done = false")
                .getResultList();
        session.getTransaction().commit();
        return items;
    }

    @Override
    public List<Item> findByName(String name) {
        List<Item> items = null;
        Session session = sf.getCurrentSession();
        session.beginTransaction();
        Query query = session.createQuery("from Item where name = :nameParam");
        query.setParameter("nameParam", name);
        items = query.getResultList();
        session.getTransaction().commit();
        return items;
    }

    @Override
    public Item findById(int id) {
        Item item = null;
        Session session = sf.getCurrentSession();
        session.beginTransaction();
        item = session.get(Item.class, id);
        session.getTransaction().commit();
        return item;
    }

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
