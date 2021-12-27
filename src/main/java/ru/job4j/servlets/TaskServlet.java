package ru.job4j.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ru.job4j.entity.Item;
import ru.job4j.store.HbmStore;
import ru.job4j.store.Store;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class TaskServlet extends HttpServlet {

    private static final Gson GSON = new GsonBuilder().create();

    private final Store store = HbmStore.instOf();

    private List<Item> tasks = new CopyOnWriteArrayList<>();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("application/json; charset=utf-8");
//        initTasks();
        tasks = store.findAll();
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
//        int itemId = tasks.get(tasks.size() - 1).getId();
//        item.setId(itemId++);
        System.out.println("Prepared item for BD > " + itemFromJSON);
        Item itemFromDB = store.add(itemFromJSON);
        tasks.add(itemFromDB);
        tasks.forEach(itemList -> {
            System.out.println("Item -> " + itemList);
        });

        resp.setContentType("application/json; charset=utf-8");
        OutputStream output = resp.getOutputStream();
        String json = GSON.toJson(itemFromDB);
        output.write(json.getBytes(StandardCharsets.UTF_8));
        output.flush();
        output.close();
    }

    private void initTasks() {
        LocalDateTime time = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        Item item1 = new Item("Go for a work", "Иначе вынесут тебя", time, true);
        item1.setId(1);
        Item item2 = new Item("Play with dog", "Засрет квартиру", time, true);
        item2.setId(2);
        Item item3 = new Item("Develop instagram",
                "Нужно обеспечить свою старость", time, false);
        item3.setId(3);
        tasks.add(item1);
        tasks.add(item2);
        tasks.add(item3);
    }
}
