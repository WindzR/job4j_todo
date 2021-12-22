package ru.job4j.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "itemList")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int id;

    @Column
    private String description;

    @Column
    private LocalDateTime created;

    @Column
    private boolean done;

    public Item() {
    }

    public Item(String description, LocalDateTime created, boolean done) {
        this.description = description;
        this.created = created;
        this.done = done;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    @Override
    public String toString() {
        return "Item{"
                + "id=" + id
                + ", description='" + description + '\''
                + ", created=" + created
                + ", done=" + done
                + '}';
    }
}
