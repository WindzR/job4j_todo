package ru.job4j.store;

import org.hibernate.Session;
import org.hibernate.Transaction;
import ru.job4j.entity.Category;

import java.util.List;
import java.util.function.Function;

public class CategoryHbnStore extends AbstractStore<Category> {

    private static final class Lazy {
        private static final CategoryHbnStore INST = new CategoryHbnStore();
    }

    public static CategoryHbnStore instOf() {
        return CategoryHbnStore.Lazy.INST;
    }

    @Override
    protected Category save(Category entity) {
        return null;
    }

    @Override
    protected Category update(int id, Category entity) {
        return null;
    }

    @Override
    protected boolean delete(int id) {
        return false;
    }

    @Override
    public List<Category> findAll() {
        return this.tx(
                session -> {
                    List<Category> categories = null;
                    categories = session.createQuery("from Category").getResultList();
                    return categories;
                }
        );
    }

    @Override
    protected Category findEntityById(int id) {
        return null;
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
