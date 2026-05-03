package com.fst.cabinet.controller;

import com.fst.cabinet.entity.RendezVous;
import com.fst.cabinet.service.RendezVousService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class DashboardController {

    private final RendezVousService rendezVousService;

    @GetMapping({"/", "/dashboard"})
    public String dashboard(Model model) {
        LocalDate today = LocalDate.now();
        LocalDate lundi = today.with(DayOfWeek.MONDAY);

        List<RendezVous> rdvJour = rendezVousService.findDuJour(today);
        List<RendezVous> rdvSemaine = rendezVousService.findDeLaSemaine(lundi);
        List<RendezVous> enAttente = rendezVousService.findEnAttente();

        model.addAttribute("rdvJour", rdvJour);
        model.addAttribute("rdvSemaine", rdvSemaine);
        model.addAttribute("enAttente", enAttente);
        model.addAttribute("today", today);
        model.addAttribute("countJour", rdvJour.size());
        model.addAttribute("countSemaine", rdvSemaine.size());
        model.addAttribute("countAttente", enAttente.size());

        return "dashboard";
    }
}
