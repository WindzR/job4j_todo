package ru.job4j.servlets;

import ru.job4j.entity.User;
import ru.job4j.store.UserHbnStore;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RegServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("registration.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String name = req.getParameter("name");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        UserHbnStore store = UserHbnStore.instOf();
        if (store.findByEmail(email) != null) {
            req.setAttribute("message", "Пользователь с таким email уже зарегистрирован");
            req.getRequestDispatcher("registration.jsp").forward(req, resp);
        } else {
            System.out.println("Регистрируем нового пользователя");
            store.save(new User(name, email, password));
            resp.sendRedirect(req.getContextPath() + "/auth.do");
        }
    }
}
