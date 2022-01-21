package ru.job4j.entity;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "itemList")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created")
    private Date created = new Date(System.currentTimeMillis());

    @Column(name = "done")
    private boolean done;

    @ManyToOne(cascade = {CascadeType.PERSIST,
                            CascadeType.MERGE,
                            CascadeType.DETACH,
                            CascadeType.REFRESH})
    @JoinColumn(name = "user_id")
    private User author;

    @ManyToMany(cascade = {CascadeType.PERSIST,
                            CascadeType.MERGE,
                            CascadeType.REFRESH})
    @JoinTable(
            name = "itemlist_categories",
            joinColumns = @JoinColumn(name = "item_id"),
            inverseJoinColumns = @JoinColumn(name = "categories_id")
    )
    private Set<Category> categories = new HashSet<>();

    public Item() {
    }

    public Item(String name, String description, Date created, boolean done) {
        this.name = name;
        this.description = description;
        this.created = created;
        this.done = done;
    }

    public void addCategory(Category category) {
        categories.add(category);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Set<Category> getCategories() {
        return categories;
    }

    public void setCategories(Set<Category> categories) {
        this.categories = categories;
    }

    @Override
    public String toString() {
        return "Item{"
                + "id=" + id
                + ", name='" + name + '\''
                + ", description='" + description + '\''
                + ", created=" + created
                + ", done=" + done
                + ", author=" + author
                + ", categories=" + categories
                + '}';
    }
}
