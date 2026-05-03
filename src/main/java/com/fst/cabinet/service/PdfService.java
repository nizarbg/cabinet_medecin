package com.fst.cabinet.service;

import com.fst.cabinet.entity.Ordonnance;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.draw.LineSeparator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class PdfService {

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public byte[] generateOrdonnancePdf(Ordonnance ordonnance) throws DocumentException {
        Document document = new Document(PageSize.A4, 50, 50, 80, 50);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        PdfWriter.getInstance(document, out);
        document.open();

        // En-tête cabinet
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.DARK_GRAY);
        Font subtitleFont = FontFactory.getFont(FontFactory.HELVETICA, 11, BaseColor.GRAY);
        Font labelFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11);
        Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 11);

        Paragraph title = new Paragraph("Cabinet Médical — FST", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);

        Paragraph sub = new Paragraph("Ordonnance Médicale", subtitleFont);
        sub.setAlignment(Element.ALIGN_CENTER);
        sub.setSpacingAfter(20);
        document.add(sub);

        LineSeparator ls = new LineSeparator();
        document.add(new Chunk(ls));

        // Informations patient et médecin
        var rdv = ordonnance.getRendezVous();
        var patient = rdv.getPatient();
        var medecin = rdv.getMedecin();

        document.add(new Paragraph(" "));
        document.add(new Paragraph("Date : " + ordonnance.getDateEmission().format(DATE_FMT), normalFont));
        document.add(new Paragraph("Médecin : Dr. " + medecin.getPrenom() + " " + medecin.getNom() +
                " (" + medecin.getSpecialite() + ")", normalFont));
        document.add(new Paragraph("Patient : " + patient.getPrenom() + " " + patient.getNom() +
                " — CIN : " + patient.getCin(), normalFont));
        document.add(new Paragraph("Date de naissance : " + patient.getDateNaissance().format(DATE_FMT), normalFont));
        document.add(new Paragraph(" "));
        document.add(new Chunk(ls));
        document.add(new Paragraph(" "));

        // Médicaments
        Paragraph medTitle = new Paragraph("Prescription :", labelFont);
        medTitle.setSpacingAfter(10);
        document.add(medTitle);

        int i = 1;
        for (var ligne : ordonnance.getMedicaments()) {
            Paragraph med = new Paragraph(i + ". " + ligne.getNomMedicament(), labelFont);
            document.add(med);
            if (ligne.getPosologie() != null && !ligne.getPosologie().isBlank()) {
                document.add(new Paragraph("   Posologie : " + ligne.getPosologie(), normalFont));
            }
            if (ligne.getDuree() != null && !ligne.getDuree().isBlank()) {
                document.add(new Paragraph("   Durée : " + ligne.getDuree(), normalFont));
            }
            document.add(new Paragraph(" "));
            i++;
        }

        // Observations
        if (ordonnance.getObservations() != null && !ordonnance.getObservations().isBlank()) {
            document.add(new Chunk(ls));
            document.add(new Paragraph(" "));
            Paragraph obs = new Paragraph("Observations :", labelFont);
            document.add(obs);
            document.add(new Paragraph(ordonnance.getObservations(), normalFont));
        }

        // Signature
        document.add(new Paragraph(" "));
        document.add(new Paragraph(" "));
        Paragraph sign = new Paragraph("Signature du médecin :", normalFont);
        sign.setAlignment(Element.ALIGN_RIGHT);
        document.add(sign);

        document.close();
        return out.toByteArray();
    }
}
