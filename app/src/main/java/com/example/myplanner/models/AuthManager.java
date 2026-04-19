package com.example.myplanner.models;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class AuthManager {
    private FirebaseFirestore db;

    public AuthManager() {
        db = FirebaseFirestore.getInstance();
    }

    public void kullaniciGiris(String username, String password, AuthCallback callback) {
        // Giriş yaparken de direkt döküman adına göre bakmak daha hızlıdır
        db.collection("Users")
                .document(username) // Direkt kullanıcı adını (ID) arıyoruz
                .get()
                .addOnSuccessListener(document -> {
                    if (document.exists()) {
                        String dbPassword = document.getString("password");
                        if (dbPassword != null && dbPassword.equals(password)) {
                            callback.onSuccess("Giriş Başarılı! Hoş geldin " + username);
                        } else {
                            callback.onFailure("Hatalı şifre girdin bebüş!");
                        }
                    } else {
                        callback.onFailure("Kullanıcı bulunamadı!");
                    }
                })
                .addOnFailureListener(e -> callback.onFailure("Hata: " + e.getMessage()));
    }

    // 🔥 İŞTE HER ŞEYİ DÜZELTEN O KRİTİK METOT
    public void kullaniciKaydet(User yeniUser, AuthCallback callback) {
        // .add(yeniUser) yerine .document(yeniUser.getKadi()).set(yeniUser) kullanıyoruz.
        // Böylece Firestore'daki döküman ID'si kullanıcının adı (Örn: gulsertoglu) oluyor.
        db.collection("Users")
                .document(yeniUser.getUsername()) // Kapı numarası artık kullanıcı adı!
                .set(yeniUser) // Verileri içeri yerleştiriyoruz
                .addOnSuccessListener(aVoid -> {
                    callback.onSuccess("Kayıt Başarılı! Hoş geldin " + yeniUser.getUsername());
                })
                .addOnFailureListener(e -> {
                    callback.onFailure("Kayıt Hatası: " + e.getMessage());
                });
    }
}