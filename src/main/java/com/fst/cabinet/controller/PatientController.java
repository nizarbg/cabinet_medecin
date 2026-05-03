package com.fst.cabinet.controller;

import com.fst.cabinet.dto.PatientDto;
import com.fst.cabinet.entity.Patient;
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
@RequestMapping("/patients")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;
    private final RendezVousService rendezVousService;

    @GetMapping
    public String list(@RequestParam(required = false) String q, Model model) {
        if (q != null && !q.isBlank()) {
            model.addAttribute("patients", patientService.rechercher(q));
            model.addAttribute("q", q);
        } else {
            model.addAttribute("patients", patientService.findAll());
        }
        return "patients/list";
    }

    @GetMapping("/nouveau")
    public String createForm(Model model) {
        model.addAttribute("patientDto", new PatientDto());
        return "patients/form";
    }

    @PostMapping("/nouveau")
    public String create(@Valid @ModelAttribute PatientDto patientDto,
                         BindingResult result, Model model,
                         RedirectAttributes redirect) {
        if (result.hasErrors()) {
            return "patients/form";
        }
        try {
            patientService.save(patientDto);
            redirect.addFlashAttribute("success", "Patient créé avec succès.");
        } catch (IllegalStateException e) {
            model.addAttribute("error", e.getMessage());
            return "patients/form";
        }
        return "redirect:/patients";
    }

    @GetMapping("/{id}")
    public String fiche(@PathVariable Long id, Model model) {
        Patient patient = patientService.findById(id);
        model.addAttribute("patient", patient);
        model.addAttribute("rdvList", rendezVousService.findByPatient(id));
        return "patients/fiche";
    }

    @GetMapping("/{id}/modifier")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("patientDto", patientService.toDto(patientService.findById(id)));
        return "patients/form";
    }

    @PostMapping("/{id}/modifier")
    public String edit(@PathVariable Long id,
                       @Valid @ModelAttribute PatientDto patientDto,
                       BindingResult result, Model model,
                       RedirectAttributes redirect) {
        if (result.hasErrors()) {
            return "patients/form";
        }
        try {
            patientService.update(id, patientDto);
            redirect.addFlashAttribute("success", "Patient modifié avec succès.");
        } catch (IllegalStateException e) {
            model.addAttribute("error", e.getMessage());
            return "patients/form";
        }
        return "redirect:/patients/" + id;
    }

    @PostMapping("/{id}/supprimer")
    public String delete(@PathVariable Long id, RedirectAttributes redirect) {
        patientService.delete(id);
        redirect.addFlashAttribute("success", "Patient supprimé.");
        return "redirect:/patients";
    }
}
