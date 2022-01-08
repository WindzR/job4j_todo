package ru.job4j.store;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import ru.job4j.entity.User;

import javax.persistence.Query;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

public class UserHbnStore implements UserStore, AutoCloseable {

    private final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
            .configure().build();

    private final SessionFactory sf = new MetadataSources(registry)
            .buildMetadata().buildSessionFactory();

    private static final class Lazy {
        private static final UserStore INST = new UserHbnStore();
    }

    public static UserStore instOf() {
        return UserHbnStore.Lazy.INST;
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
    public User update(User user) {
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
    public Collection<User> findAllUsers() {
        return this.tx(
                session -> {
                    List<User> users = null;
                    users = session.createQuery("from User")
                            .getResultList();
                    return users;
                }
        );
    }

    @Override
    public User findByEmail(String email) {
        return this.tx(
                session -> {
                    User user = null;
                    final Query query = session.createQuery("from User where email = :emailParam");
                    query.setParameter("emailParam", email);
                    user = (User) query.getResultList().get(0);
                    return user;
                }
        );
    }

    @Override
    public User findById(int id) {
        return this.tx(
                session -> {
                    User user = null;
                    user = session.get(User.class, id);
                    return user;
                }
        );
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
}
