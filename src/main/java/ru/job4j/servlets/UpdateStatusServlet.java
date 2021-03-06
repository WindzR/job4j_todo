package ru.job4j.servlets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ru.job4j.entity.Item;
import ru.job4j.store.ItemHbmStore;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class UpdateStatusServlet extends HttpServlet {

    private static final Gson GSON = new GsonBuilder().create();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Item itemFromJSON = GSON.fromJson(req.getReader(), Item.class);
        System.out.println("Prepared item for BD > " + itemFromJSON);
        ItemHbmStore store = ItemHbmStore.instOf();
        store.update(itemFromJSON.getId(), itemFromJSON);
        OutputStream output = resp.getOutputStream();
        String json = GSON.toJson(itemFromJSON);
        output.write(json.getBytes(StandardCharsets.UTF_8));
        output.flush();
        output.close();
    }
}
