package com.example.myplanner.models;

public class Plan {
    // Kriter: Encapsulation (Kapsülleme)
    private String id;
    private String baslik;
    private String icerik;
    private String tarih;
    private boolean tamamlandi;

    // Firebase için boş constructor şart bebüş!
    public Plan() {}

    public Plan(String id, String baslik, String icerik, String tarih, boolean tamamlandi) {
        this.id = id;
        this.baslik = baslik;
        this.icerik = icerik;
        this.tarih = tarih;
        this.tamamlandi = tamamlandi;
    }

    // Getter ve Setter metotları (Hoca bunları mutlaka kontrol eder)
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getBaslik() { return baslik; }
    public void setBaslik(String baslik) { this.baslik = baslik; }

    public String getIcerik() { return icerik; }
    public void setIcerik(String icerik) { this.icerik = icerik; }

    public String getTarih() { return tarih; }
    public void setTarih(String tarih) { this.tarih = tarih; }

    public boolean isTamamlandi() { return tamamlandi; }
    public void setTamamlandi(boolean tamamlandi) { this.tamamlandi = tamamlandi; }
}