package ru.job4j.store;

import ru.job4j.entity.User;

import java.util.Collection;

public interface UserStore extends AutoCloseable {

    User save(User user);

    User update(User user);

    boolean delete(int id);

    Collection<User> findAllUsers();

    User findByEmail(String email);

    User findById(int id);
}
