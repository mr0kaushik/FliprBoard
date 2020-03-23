package com.board.flipr.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class User {
    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("color")
    @Expose
    private Integer color;

    @SerializedName("boards")
    @Expose
    private List<UserBoard> boards = new ArrayList<>();


    public List<UserBoard> getBoards() {
        return boards;
    }

    public void setBoards(List<UserBoard> boards) {
        this.boards = boards;
    }

    public User() {
    }


    public User(String id, String name, String email, Integer color) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.color = color;
        this.boards = new ArrayList<>();
    }

    public User(String id, String name, String email, Integer color, List<UserBoard> boards) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.color = color;
        this.boards = boards;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Map<String, Object> toMap(){
        Map<String, Object> map = new HashMap<>();
        map.put("user_id", id);
        map.put("name", name);
        map.put("email_id", email);

        return map;
    }

    public List<UserBoard> getUserBoards() {
        return boards;
    }

    public void setUserBoards(List<UserBoard> userBoards) {
        this.boards = userBoards;
    }

    public Integer getColor() {
        return color;
    }

    public void setColor(Integer color) {
        this.color = color;
    }
}
