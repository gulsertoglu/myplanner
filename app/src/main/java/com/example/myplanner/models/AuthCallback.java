package com.example.myplanner.models;

public interface AuthCallback {
    void onSuccess(String mesaj);
    void onFailure(String hata);
}
