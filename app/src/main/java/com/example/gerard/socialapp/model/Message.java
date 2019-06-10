package com.example.gerard.socialapp.model;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;
import com.google.firebase.database.Exclude;
import java.util.HashMap;

public class Message {

    public String uid;
    public String postUid;
    public String author;
    public String content;


    public Map<String, Boolean> likes = new HashMap<>();

    public Message() {}

    public Message(String uid, String author, String content) {

        this.uid = uid;
        this.author = author;
        this.content = content;

    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("author", author);
        result.put("content", content);

        return result;
    }
}
