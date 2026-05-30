package com.example.myplanner.activities;

import com.example.myplanner.models.Plan;
import java.util.List;

public interface IPlanSirala {
    // OOP Sözleşmesi: Bu interface'i uygulayan sınıf planları önceliğe göre dizmek zorundadır!
    void planlariOnceligeGoreSirala(List<Plan> liste);
}