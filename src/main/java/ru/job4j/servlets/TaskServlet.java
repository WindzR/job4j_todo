package ru.job4j.servlets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ru.job4j.entity.Item;
import ru.job4j.store.ItemHbmStore;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class TaskServlet extends HttpServlet {

    private static final Gson GSON = new GsonBuilder().create();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setAttribute("user", req.getSession().getAttribute("user"));
        ItemHbmStore store = ItemHbmStore.instOf();
        List<Item> tasks = new CopyOnWriteArrayList<>();
        resp.setContentType("application/json; charset=utf-8");
        tasks = store.findAll();
        System.out.println("Список заданий --> " + tasks);
        OutputStream output = resp.getOutputStream();
        String json = GSON.toJson(tasks);
        output.write(json.getBytes(StandardCharsets.UTF_8));
        output.flush();
        output.close();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Item itemFromJSON = GSON.fromJson(req.getReader(), Item.class);
        System.out.println("Object from JSON > " + itemFromJSON);
        LocalDateTime time = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        itemFromJSON.setCreated(time);
        System.out.println("Prepared item for BD > " + itemFromJSON);
        ItemHbmStore store = ItemHbmStore.instOf();
        Item itemFromDB = store.save(itemFromJSON);
        resp.setContentType("application/json; charset=utf-8");
        OutputStream output = resp.getOutputStream();
        String json = GSON.toJson(itemFromDB);
        output.write(json.getBytes(StandardCharsets.UTF_8));
        output.flush();
        output.close();
    }
}
