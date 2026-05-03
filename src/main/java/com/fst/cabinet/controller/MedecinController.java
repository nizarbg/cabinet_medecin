package com.fst.cabinet.controller;

import com.fst.cabinet.dto.MedecinDto;
import com.fst.cabinet.entity.Medecin;
import com.fst.cabinet.service.MedecinService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/medecins")
@RequiredArgsConstructor
public class MedecinController {

    private final MedecinService medecinService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("medecins", medecinService.findAll());
        return "medecins/list";
    }

    @GetMapping("/nouveau")
    public String createForm(Model model) {
        model.addAttribute("medecinDto", new MedecinDto());
        return "medecins/form";
    }

    @PostMapping("/nouveau")
    public String create(@Valid @ModelAttribute MedecinDto medecinDto,
                         BindingResult result, Model model,
                         RedirectAttributes redirect) {
        if (result.hasErrors()) return "medecins/form";
        try {
            medecinService.save(medecinDto);
            redirect.addFlashAttribute("success", "Médecin créé avec succès.");
        } catch (IllegalStateException e) {
            model.addAttribute("error", e.getMessage());
            return "medecins/form";
        }
        return "redirect:/medecins";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        Medecin medecin = medecinService.findById(id);
        model.addAttribute("medecin", medecin);
        return "medecins/detail";
    }

    @GetMapping("/{id}/modifier")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("medecinDto", medecinService.toDto(medecinService.findById(id)));
        return "medecins/form";
    }

    @PostMapping("/{id}/modifier")
    public String edit(@PathVariable Long id,
                       @Valid @ModelAttribute MedecinDto medecinDto,
                       BindingResult result, Model model,
                       RedirectAttributes redirect) {
        if (result.hasErrors()) return "medecins/form";
        try {
            medecinService.update(id, medecinDto);
            redirect.addFlashAttribute("success", "Médecin modifié avec succès.");
        } catch (IllegalStateException e) {
            model.addAttribute("error", e.getMessage());
            return "medecins/form";
        }
        return "redirect:/medecins/" + id;
    }

    @PostMapping("/{id}/supprimer")
    public String delete(@PathVariable Long id, RedirectAttributes redirect) {
        medecinService.delete(id);
        redirect.addFlashAttribute("success", "Médecin supprimé.");
        return "redirect:/medecins";
    }
}
