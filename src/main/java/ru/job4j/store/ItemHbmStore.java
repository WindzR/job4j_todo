package ru.job4j.store;

import org.hibernate.Session;
import org.hibernate.Transaction;
import ru.job4j.entity.Item;

import javax.persistence.Query;
import java.util.List;
import java.util.function.Function;

public class ItemHbmStore extends AbstractStore<Item> {

    private static final class Lazy {
        private static final ItemHbmStore INST = new ItemHbmStore();
    }

    public static ItemHbmStore instOf() {
        return ItemHbmStore.Lazy.INST;
    }

    @Override
    public Item save(Item item) {
        return this.tx(
            session -> {
                session.save(item);
                return item;
            }
        );
    }

    @Override
    public Item update(int id, Item item) {
        return this.tx(
                session -> {
                    final Query query = session.createQuery(
                            "update Item SET done = :doneParam WHERE id = :idParam"
                    )
                            .setParameter("doneParam", true)
                            .setParameter("idParam", id);
                    query.executeUpdate();
                    return item;
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
                    items = session.createQuery(
                            "select distinct item from Item item"
                                    + " join fetch item.categories order by item.created")
                            .getResultList();
                    return items;
                }
        );
    }

    public List<Item> findAllUnfinished() {
        return this.tx(
                session -> {
                    List<Item> items = null;
                    items = session.createQuery(
                            "select distinct item from Item item "
                                    + "join fetch item.categories "
                                    + "where item.done = false order by item.created")
                            .getResultList();
                    return items;
                }
        );
    }

    public List<Item> findByName(String name) {
        return this.tx(
                session -> {
                    List<Item> items = null;
                    final Query query = session.createQuery(
                            "select distinct item from Item item "
                                    + "join fetch item.categories"
                                    + " where item.name = :nameParam order by item.created")
                            .setParameter("nameParam", name);
                    items = query.getResultList();
                    return items;
                }
        );
    }

    @Override
    public Item findEntityById(int id) {
        return this.tx(
                session -> {
                    Item item = null;
                    item = (Item) session.createQuery("select distinct item from Item item "
                            + "join fetch item.categories"
                            + " where item.id = :idParam")
                            .setParameter("idParam", id)
                            .uniqueResult();
                    return item;
                }
        );
    }

    private <T> T tx(final Function<Session, T> command) {
        Session session = getSf().getCurrentSession();
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
}
