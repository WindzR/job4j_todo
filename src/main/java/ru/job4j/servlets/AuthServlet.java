package ru.job4j.servlets;

import ru.job4j.entity.User;
import ru.job4j.store.UserHbnStore;
import ru.job4j.store.UserStore;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Objects;

public class AuthServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        UserStore store = UserHbnStore.instOf();
        User user = store.findByEmail(email);
        if (user == null) {
            req.setAttribute("error", "Пользователь с таким email не зарегистрирован");
            req.getRequestDispatcher("login.jsp").forward(req, resp);
        }
        if (Objects.equals(password, Objects.requireNonNull(user).getPassword())) {
            HttpSession session = req.getSession();
            session.setAttribute("user", user);
            resp.sendRedirect(req.getContextPath() + "/");
        } else {
            req.setAttribute("error", "Неверный пароль!");
            req.getRequestDispatcher("login.jsp").forward(req, resp);
        }
    }
}
