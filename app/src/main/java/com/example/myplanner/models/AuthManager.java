package com.example.myplanner.models;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class AuthManager {
    private FirebaseFirestore db;

    public AuthManager() {
        db = FirebaseFirestore.getInstance();
    }

    public void kullaniciGiris(String username, String password, AuthCallback callback) {
        // KRİTER: Koleksiyon adı Firebase'deki gibi "Users" olmalı!
        db.collection("Users")
                .whereEqualTo("username", username)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        DocumentSnapshot document = task.getResult().getDocuments().get(0);
                        String dbPassword = document.getString("password");

                        // Şifre kontrolü (Büyük/küçük harf duyarlıdır!)
                        if (dbPassword != null && dbPassword.equals(password)) {
                            callback.onSuccess("Giriş Başarılı! Hoş geldin " + username);
                        } else {
                            callback.onFailure("Hatalı şifre girdin bebüş!");
                        }
                    } else {
                        callback.onFailure("Kullanıcı bulunamadı! (Koleksiyon: Users)");
                    }
                })
                .addOnFailureListener(e -> callback.onFailure("Hata: " + e.getMessage()));
    }

    // Kriter: Model-Driven Development (User nesnesini komple alıyoruz)
    public void kullaniciKaydet(User yeniUser, AuthCallback callback) {
        // Firebase'deki koleksiyon adın "Users" (Büyük U)
        db.collection("Users")
                .add(yeniUser) // Paketi olduğu gibi Firebase'e fırlatıyoruz
                .addOnSuccessListener(documentReference -> {
                    callback.onSuccess("Kayıt Başarılı! Hoş geldin " + yeniUser.getUsername());
                })
                .addOnFailureListener(e -> {
                    callback.onFailure("Kayıt Hatası: " + e.getMessage());
                });
    }
}