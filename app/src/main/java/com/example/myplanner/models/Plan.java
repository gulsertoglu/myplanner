package com.example.myplanner.models;

import com.google.firebase.Timestamp;

public class Plan {
    private String id;
    private String baslik;
    private String detay;
    private String renk;
    private String sahibi;
    private boolean tamamlandi;
    private boolean silindi; // <-- HATA ALMAMAK İÇİN BURASI ŞART!
    private Timestamp tarih;

    // 1. Boş Constructor (Firebase için ŞART!)
    public Plan() {
    }

    // 2. Dolu Constructor
    public Plan(String baslik, String detay, String renk, String sahibi, Timestamp tarih) {
        this.baslik = baslik;
        this.detay = detay;
        this.renk = renk;
        this.sahibi = sahibi;
        this.tarih = tarih;
        this.tamamlandi = false;
        this.silindi = false; // Varsayılan olarak silinmemiş
    }

    // 3. Getter ve Setter Metotları
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getBaslik() { return baslik; }
    public void setBaslik(String baslik) { this.baslik = baslik; }

    // Adapter uyumu için icerik metotları
    public String getIcerik() { return detay; }
    public void setIcerik(String icerik) { this.detay = icerik; }

    public String getDetay() { return detay; }
    public void setDetay(String detay) { this.detay = detay; }

    public String getRenk() { return renk; }
    public void setRenk(String renk) { this.renk = renk; }

    public String getSahibi() { return sahibi; }
    public void setSahibi(String sahibi) { this.sahibi = sahibi; }

    public boolean isTamamlandi() { return tamamlandi; }
    public void setTamamlandi(boolean tamamlandi) { this.tamamlandi = tamamlandi; }

    public boolean isSilindi() { return silindi; }
    public void setSilindi(boolean silindi) { this.silindi = silindi; }

    public Timestamp getTarih() { return tarih; }
    public void setTarih(Timestamp tarih) { this.tarih = tarih; }
}