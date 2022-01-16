package ru.job4j.store;

import org.hibernate.Session;
import org.hibernate.Transaction;
import ru.job4j.entity.User;

import javax.persistence.Query;
import java.util.List;
import java.util.function.Function;

public class UserHbnStore extends AbstractStore<User> {

    private static final class Lazy {
        private static final UserHbnStore INST = new UserHbnStore();
    }

    public static UserHbnStore instOf() {
        return Lazy.INST;
    }

    @Override
    public User save(User user) {
        return this.tx(
            session -> {
                session.save(user);
                return user;
            }
        );
    }

    @Override
    public User update(int id, User user) {
        return this.tx(
                session -> {
                    session.update(user);
                    return user;
                }
        );
    }

    @Override
    public boolean delete(int id) {
        return this.tx(
                session -> {
                    boolean rsl;
                    final Query query = session.createQuery(
                            "delete User where id = :idParam"
                    );
                    query.setParameter("idParam", id);
                    int delete = query.executeUpdate();
                    rsl = delete > 0;
                    return rsl;
                }
        );
    }

    @Override
    public List<User> findAll() {
        return this.tx(
                session -> {
                    List<User> users = session.createQuery("from User")
                            .getResultList();
                    return users.size() != 0 ? users : null;
                }
        );
    }

    public User findByEmail(String email) {
        return this.tx(
                session -> (User) session.createQuery("from User where email = :emailParam")
                         .setParameter("emailParam", email)
                         .uniqueResult()
        );
    }

    @Override
    public User findEntityById(int id) {
        return this.tx(
                session -> session.get(User.class, id)
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
