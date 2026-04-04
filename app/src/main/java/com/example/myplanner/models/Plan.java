package com.example.myplanner.models;

import com.google.firebase.Timestamp;

public class Plan {
    private String id;
    private String baslik;
    private String detay; // Adapter'da 'icerik' diye çağırdığın yer burası
    private String renk;
    private String sahibi;
    private boolean tamamlandi;
    private Timestamp tarih;

    // 1. Boş Constructor (Firebase'in veriyi nesneye dönüştürmesi için ŞART!)
    public Plan() {
    }

    // 2. Dolu Constructor (Opsiyonel ama işimizi kolaylaştırır)
    public Plan(String baslik, String detay, String renk, String sahibi, Timestamp tarih) {
        this.baslik = baslik;
        this.detay = detay;
        this.renk = renk;
        this.sahibi = sahibi;
        this.tarih = tarih;
        this.tamamlandi = false;
    }

    // 3. Getter ve Setter Metotları
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getBaslik() { return baslik; }
    public void setBaslik(String baslik) { this.baslik = baslik; }

    // Adapter'da 'plan.getIcerik()' yazmıştık, hata almamak için bu ismi koruyoruz:
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

    // EN ÖNEMLİ KISIM: Timestamp Tipinde Tarih
    public Timestamp getTarih() { return tarih; }
    public void setTarih(Timestamp tarih) { this.tarih = tarih; }
}