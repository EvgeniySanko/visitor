package com.sanko.visitor.entities;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank(message = "Please, fill the message")
    @Size(max = 2048, message = "Message too long")
    private String text;
    @NotBlank(message = "Please, fill the tag")
    @Size(max = 255, message = "Tag too long")
    private String tag;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User author;
    private String filename;
    public Message() {
    }

    public Message(String text, String tag, User user) {
        this.text = text;
        this.tag = tag;
        this.author = user;
    }

    public String getAuthorName(){
        return author != null ? author.getUsername() : "<none>";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
}
