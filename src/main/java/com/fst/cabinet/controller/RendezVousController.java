package com.fst.cabinet.controller;

import com.fst.cabinet.dto.RendezVousDto;
import com.fst.cabinet.entity.StatutRdv;
import com.fst.cabinet.service.MedecinService;
import com.fst.cabinet.service.PatientService;
import com.fst.cabinet.service.RendezVousService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/rendez-vous")
@RequiredArgsConstructor
public class RendezVousController {

    private final RendezVousService rendezVousService;
    private final PatientService patientService;
    private final MedecinService medecinService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("rdvList", rendezVousService.findAll());
        return "rendezVous/list";
    }

    @GetMapping("/nouveau")
    public String createForm(@RequestParam(required = false) Long patientId,
                             @RequestParam(required = false) Long medecinId,
                             Model model) {
        RendezVousDto dto = new RendezVousDto();
        if (patientId != null) dto.setPatientId(patientId);
        if (medecinId != null) dto.setMedecinId(medecinId);
        model.addAttribute("rendezVousDto", dto);
        model.addAttribute("patients", patientService.findAll());
        model.addAttribute("medecins", medecinService.findActifs());
        model.addAttribute("statuts", StatutRdv.values());
        return "rendezVous/form";
    }

    @PostMapping("/nouveau")
    public String create(@Valid @ModelAttribute RendezVousDto rendezVousDto,
                         BindingResult result, Model model,
                         RedirectAttributes redirect) {
        if (result.hasErrors()) {
            model.addAttribute("patients", patientService.findAll());
            model.addAttribute("medecins", medecinService.findActifs());
            model.addAttribute("statuts", StatutRdv.values());
            return "rendezVous/form";
        }
        try {
            rendezVousService.save(rendezVousDto);
            redirect.addFlashAttribute("success", "Rendez-vous créé avec succès.");
        } catch (IllegalStateException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("patients", patientService.findAll());
            model.addAttribute("medecins", medecinService.findActifs());
            model.addAttribute("statuts", StatutRdv.values());
            return "rendezVous/form";
        }
        return "redirect:/rendez-vous";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        model.addAttribute("rdv", rendezVousService.findById(id));
        return "rendezVous/detail";
    }

    @GetMapping("/{id}/modifier")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("rendezVousDto", rendezVousService.toDto(rendezVousService.findById(id)));
        model.addAttribute("patients", patientService.findAll());
        model.addAttribute("medecins", medecinService.findActifs());
        model.addAttribute("statuts", StatutRdv.values());
        return "rendezVous/form";
    }

    @PostMapping("/{id}/modifier")
    public String edit(@PathVariable Long id,
                       @Valid @ModelAttribute RendezVousDto rendezVousDto,
                       BindingResult result, Model model,
                       RedirectAttributes redirect) {
        if (result.hasErrors()) {
            model.addAttribute("patients", patientService.findAll());
            model.addAttribute("medecins", medecinService.findActifs());
            model.addAttribute("statuts", StatutRdv.values());
            return "rendezVous/form";
        }
        try {
            rendezVousService.update(id, rendezVousDto);
            redirect.addFlashAttribute("success", "Rendez-vous modifié.");
        } catch (IllegalStateException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("patients", patientService.findAll());
            model.addAttribute("medecins", medecinService.findActifs());
            model.addAttribute("statuts", StatutRdv.values());
            return "rendezVous/form";
        }
        return "redirect:/rendez-vous/" + id;
    }

    @PostMapping("/{id}/annuler")
    public String annuler(@PathVariable Long id, RedirectAttributes redirect) {
        rendezVousService.annuler(id);
        redirect.addFlashAttribute("success", "Rendez-vous annulé.");
        return "redirect:/rendez-vous";
    }

    @PostMapping("/{id}/statut")
    public String changerStatut(@PathVariable Long id,
                                @RequestParam StatutRdv statut,
                                RedirectAttributes redirect) {
        rendezVousService.changerStatut(id, statut);
        redirect.addFlashAttribute("success", "Statut mis à jour.");
        return "redirect:/rendez-vous/" + id;
    }
}
