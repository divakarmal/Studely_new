package com.example.studely.classes;

import java.io.Serializable;

public class User implements Serializable {
    private String name;
    private String email;
    private String phoneNum;
    private String teleHandle;

    public User(String name, String email, String phoneNum, String teleHandle) {
        this.name = name;
        this.email = email;
        this.phoneNum = phoneNum;
        this.teleHandle = teleHandle;
    }
}
