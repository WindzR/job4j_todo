package ru.job4j.servlets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ru.job4j.entity.Item;
import ru.job4j.store.HbmStore;
import ru.job4j.store.Store;

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
        Store store = HbmStore.instOf();
        List<Item> tasks = new CopyOnWriteArrayList<>();
        resp.setContentType("application/json; charset=utf-8");
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
        System.out.println("Prepared item for BD > " + itemFromJSON);
        if (itemFromJSON.getName() == null || itemFromJSON.getDescription() == null) {
            Item itemFromDB = updateToDatabase(itemFromJSON);
            jsonResponse(resp, itemFromDB);
        } else {
            Item itemFromDB = addToDatabase(itemFromJSON);
            jsonResponse(resp, itemFromDB);
        }
    }

    private void jsonResponse(HttpServletResponse resp, Item item)
            throws IOException {
        resp.setContentType("application/json; charset=utf-8");
        OutputStream output = resp.getOutputStream();
        String json = GSON.toJson(item);
        output.write(json.getBytes(StandardCharsets.UTF_8));
        output.flush();
        output.close();
    }

    private Item updateToDatabase(Item item) {
        Store store = HbmStore.instOf();
        store.replace(item.getId(), item);
        Item itemFromDB = store.findById(item.getId());
        System.out.println("itemFromBD --> " + itemFromDB);
        return itemFromDB;
    }

    private Item addToDatabase(Item item) {
        Store store = HbmStore.instOf();
        return store.add(item);
    }
}
