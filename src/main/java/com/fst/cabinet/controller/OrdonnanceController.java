package com.fst.cabinet.controller;

import com.fst.cabinet.dto.LigneMedicamentDto;
import com.fst.cabinet.dto.OrdonnanceDto;
import com.fst.cabinet.entity.Ordonnance;
import com.fst.cabinet.entity.RendezVous;
import com.fst.cabinet.service.OrdonnanceService;
import com.fst.cabinet.service.PdfService;
import com.fst.cabinet.service.RendezVousService;
import com.itextpdf.text.DocumentException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;

@Controller
@RequestMapping("/ordonnances")
@RequiredArgsConstructor
public class OrdonnanceController {

    private final OrdonnanceService ordonnanceService;
    private final RendezVousService rendezVousService;
    private final PdfService pdfService;

    @GetMapping("/nouveau")
    public String createForm(@RequestParam Long rdvId, Model model) {
        RendezVous rdv = rendezVousService.findById(rdvId);
        OrdonnanceDto dto = new OrdonnanceDto();
        dto.setRendezVousId(rdvId);
        dto.setMedicaments(new ArrayList<>());
        dto.getMedicaments().add(new LigneMedicamentDto());
        model.addAttribute("ordonnanceDto", dto);
        model.addAttribute("rdv", rdv);
        return "ordonnances/form";
    }

    @PostMapping("/nouveau")
    public String create(@Valid @ModelAttribute OrdonnanceDto ordonnanceDto,
                         BindingResult result, Model model,
                         RedirectAttributes redirect) {
        if (result.hasErrors()) {
            model.addAttribute("rdv", rendezVousService.findById(ordonnanceDto.getRendezVousId()));
            return "ordonnances/form";
        }
        try {
            Ordonnance saved = ordonnanceService.save(ordonnanceDto);
            redirect.addFlashAttribute("success", "Ordonnance créée avec succès.");
            return "redirect:/ordonnances/" + saved.getId();
        } catch (IllegalStateException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("rdv", rendezVousService.findById(ordonnanceDto.getRendezVousId()));
            return "ordonnances/form";
        }
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        model.addAttribute("ordonnance", ordonnanceService.findById(id));
        return "ordonnances/detail";
    }

    @GetMapping("/{id}/modifier")
    public String editForm(@PathVariable Long id, Model model) {
        Ordonnance o = ordonnanceService.findById(id);
        OrdonnanceDto dto = ordonnanceService.toDto(o);
        if (dto.getMedicaments().isEmpty()) {
            dto.getMedicaments().add(new LigneMedicamentDto());
        }
        model.addAttribute("ordonnanceDto", dto);
        model.addAttribute("rdv", o.getRendezVous());
        return "ordonnances/form";
    }

    @PostMapping("/{id}/modifier")
    public String edit(@PathVariable Long id,
                       @Valid @ModelAttribute OrdonnanceDto ordonnanceDto,
                       BindingResult result, Model model,
                       RedirectAttributes redirect) {
        if (result.hasErrors()) {
            model.addAttribute("rdv", rendezVousService.findById(ordonnanceDto.getRendezVousId()));
            return "ordonnances/form";
        }
        ordonnanceService.update(id, ordonnanceDto);
        redirect.addFlashAttribute("success", "Ordonnance mise à jour.");
        return "redirect:/ordonnances/" + id;
    }

    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> downloadPdf(@PathVariable Long id) throws DocumentException {
        Ordonnance ordonnance = ordonnanceService.findById(id);
        byte[] pdf = pdfService.generateOrdonnancePdf(ordonnance);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=ordonnance-" + id + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
}
