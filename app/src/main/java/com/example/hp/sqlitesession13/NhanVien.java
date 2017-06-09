package com.example.hp.sqlitesession13;

import java.io.Serializable;

/**
 * Created by hp on 06/06/2017.
 */

public class NhanVien implements Serializable {
    private String username;
    private String email;
    private String phone;

    public NhanVien(String username, String email, String phone) {
        this.username = username;
        this.email = email;
        this.phone = phone;
    }

    public NhanVien() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmal(String emal) {
        this.email = emal;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
