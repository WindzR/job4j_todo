package ru.job4j.store;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import ru.job4j.entity.Item;

import javax.persistence.Query;
import java.util.List;
import java.util.function.Function;

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
        return this.tx(
            session -> {
                session.saveOrUpdate(item);
                return item;
            }
        );
    }

    @Override
    public boolean replace(int id, Item item) {
        return this.tx(
                session -> {
                    boolean rsl;
                    final Query query = session.createQuery(
                "update Item set"
                        + " created = :createdParam,"
                        + " done = :doneParam"
                        + " where id = :idParam");
                    query.setParameter("createdParam", item.getCreated());
                    query.setParameter("doneParam", item.isDone());
                    query.setParameter("idParam", id);
                    int update = query.executeUpdate();
                    rsl = update > 0;
                    return rsl;
                }
        );
    }

    @Override
    public boolean delete(int id) {
        return this.tx(
                session -> {
                    boolean rsl;
                    final Query query = session.createQuery("delete Item where id = :idParam");
                    query.setParameter("idParam", id);
                    int delete = query.executeUpdate();
                    rsl = delete > 0;
                    return rsl;
                }
        );
    }

    @Override
    public List<Item> findAll() {
        return this.tx(
                session -> {
                    List<Item> items = null;
                    items = session.createQuery("from Item")
                            .getResultList();
                    return items;
                }
        );
    }

    @Override
    public List<Item> findAllUnfinished() {
        return this.tx(
                session -> {
                    List<Item> items = null;
                    items = session.createQuery("from Item where done = false")
                            .getResultList();
                    return items;
                }
        );
    }

    @Override
    public List<Item> findByName(String name) {
        return this.tx(
                session -> {
                    List<Item> items = null;
                    final Query query = session.createQuery("from Item where name = :nameParam");
                    query.setParameter("nameParam", name);
                    items = query.getResultList();
                    return items;
                }
        );
    }

    @Override
    public Item findById(int id) {
        return this.tx(
                session -> {
                    Item item = null;
                    item = session.get(Item.class, id);
                    return item;
                }
        );
    }

    private <T> T tx(final Function<Session, T> command) {
        Session session = sf.getCurrentSession();
        Transaction tx = session.beginTransaction();
        try {
            T rsl = command.apply(session);
            tx.commit();
            return rsl;
        } catch (final Exception ex) {
            session.getTransaction().rollback();
            throw ex;
        } finally {
            session.close();
        }
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
