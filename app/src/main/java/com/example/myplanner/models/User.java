package com.example.myplanner.models;

public class User {
    private String ad;
    private String soyad;
    private String email;
    private String okulIs;
    private String password;
    private String username;

    public User() {
    }

    public User(String ad, String soyad, String email, String okulIs, String password, String username) {
        this.ad = ad;
        this.soyad = soyad;
        this.email = email;
        this.okulIs = okulIs;
        this.password = password;
        this.username = username;
    }


    public String getAd() { return ad; }
    public void setAd(String ad) { this.ad = ad; }

    public String getSoyad() { return soyad; }
    public void setSoyad(String soyad) { this.soyad = soyad; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getOkulIs() { return okulIs; }
    public void setOkulIs(String okulIs) { this.okulIs = okulIs; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
}
