package com.example.demo.controller;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.entity.HistorialClinico;
import com.example.demo.entity.Medico;
import com.example.demo.entity.Paciente;
import com.example.demo.entity.Usuario;
import com.example.demo.repository.HistorialClinicoRepository;
import com.example.demo.repository.MedicoRepository;
import com.example.demo.repository.PacienteRepository;
import com.example.demo.repository.UsuarioRepository;

@Controller
@RequestMapping("/historiales")
public class HistorialClinicoController {

    @Autowired
    private HistorialClinicoRepository historialRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private MedicoRepository medicoRepository;

    // General list for doctors
    @GetMapping("")
    public String listarHistoriales(Principal principal, Model model) {
        Usuario usuario = usuarioRepository.findByEmail(principal.getName());
        Medico medico = medicoRepository.findById(usuario.getId()).orElse(null);

        if (medico != null) {
            List<HistorialClinico> historiales = historialRepository.findAll().stream()
                .filter(h -> h.getMedico() != null && h.getMedico().getId().equals(medico.getId())
                         && h.getPaciente() != null && h.getFecha() != null)
                .sorted((h1, h2) -> h2.getFecha().compareTo(h1.getFecha())) // Más reciente primero
                .collect(Collectors.toList());

            model.addAttribute("medico", medico);
            model.addAttribute("historiales", historiales);
        }

        return "historialclinico/historial-list";
    }

    // Web methods for patients
    @GetMapping("/paciente")
    public String verMiHistorial(Principal principal, Model model) {
        Usuario usuario = usuarioRepository.findByEmail(principal.getName());
        Paciente paciente = pacienteRepository.findById(usuario.getId()).orElse(null);

        if (paciente != null) {
            List<HistorialClinico> historiales = historialRepository.findAll().stream()
                .filter(h -> h.getPaciente() != null && h.getPaciente().getId().equals(paciente.getId()) && h.getFecha() != null)
                .sorted((h1, h2) -> h2.getFecha().compareTo(h1.getFecha())) // Más reciente primero
                .collect(Collectors.toList());

            model.addAttribute("paciente", paciente);
            model.addAttribute("historiales", historiales);
        }

        return "historialclinico/paciente-historial";
    }

    // Web methods for doctors
    @GetMapping("/paciente/{pacienteId}")
    public String verHistorialPaciente(@PathVariable Long pacienteId, Principal principal, Model model) {
        Usuario usuario = usuarioRepository.findByEmail(principal.getName());
        Medico medico = medicoRepository.findById(usuario.getId()).orElse(null);
        Paciente paciente = pacienteRepository.findById(pacienteId).orElse(null);

        if (medico != null && paciente != null) {
            List<HistorialClinico> historiales = historialRepository.findAll().stream()
                .filter(h -> h.getPaciente() != null && h.getPaciente().getId().equals(pacienteId) && h.getFecha() != null)
                .sorted((h1, h2) -> h2.getFecha().compareTo(h1.getFecha())) // Más reciente primero
                .collect(Collectors.toList());

            model.addAttribute("medico", medico);
            model.addAttribute("paciente", paciente);
            model.addAttribute("historiales", historiales);
        }

        return "historialclinico/medico-paciente-historial";
    }

    @GetMapping("/paciente/{pacienteId}/nuevo")
    public String nuevoHistorialPaciente(@PathVariable Long pacienteId, Principal principal, Model model) {
        Usuario usuario = usuarioRepository.findByEmail(principal.getName());
        Medico medico = medicoRepository.findById(usuario.getId()).orElse(null);
        Paciente paciente = pacienteRepository.findById(pacienteId).orElse(null);

        if (medico != null && paciente != null) {
            HistorialClinico historial = new HistorialClinico();
            historial.setPaciente(paciente);
            historial.setMedico(medico);

            model.addAttribute("medico", medico);
            model.addAttribute("paciente", paciente);
            model.addAttribute("historial", historial);
        }

        return "historialclinico/form";
    }

    @GetMapping("/{id}/edit")
    public String editarHistorial(@PathVariable Long id, Principal principal, Model model) {
        Usuario usuario = usuarioRepository.findByEmail(principal.getName());
        Medico medico = medicoRepository.findById(usuario.getId()).orElse(null);
        HistorialClinico historial = historialRepository.findById(id).orElse(null);

        if (medico != null && historial != null && historial.getMedico().getId().equals(medico.getId())) {
            model.addAttribute("medico", medico);
            model.addAttribute("paciente", historial.getPaciente());
            model.addAttribute("historial", historial);
            return "historialclinico/form";
        }

        return "redirect:/medico/dashboard";
    }

    // 🟢 Listar todos los historiales (API)
    @GetMapping("/api")
    public List<HistorialClinico> listarHistoriales() {
        return historialRepository.findAll();
    }

    // 🔵 Obtener historial por ID
    @GetMapping("/{id}")
    public Optional<HistorialClinico> obtenerHistorial(@PathVariable Long id) {
        return historialRepository.findById(id);
    }

    // 🟡 Crear nuevo historial
    @PostMapping
    @ResponseBody
    public HistorialClinico crearHistorial(@RequestBody HistorialClinico historial) {
        System.out.println("📝 Creando historial clínico:");
        System.out.println("   - Fecha: " + historial.getFecha());
        System.out.println("   - Diagnóstico: " + historial.getDiagnostico());
        System.out.println("   - Tratamiento: " + historial.getTratamiento());
        System.out.println("   - Paciente ID: " + (historial.getPaciente() != null ? historial.getPaciente().getId() : "null"));
        System.out.println("   - Médico ID: " + (historial.getMedico() != null ? historial.getMedico().getId() : "null"));

        HistorialClinico guardado = historialRepository.save(historial);
        System.out.println("✅ Historial guardado con ID: " + guardado.getId());
        return guardado;
    }

    // 🟠 Actualizar historial existente
    @PutMapping("/{id}")
    public HistorialClinico actualizarHistorial(@PathVariable Long id, @RequestBody HistorialClinico actualizado) {
        return historialRepository.findById(id).map(historial -> {
            historial.setDiagnostico(actualizado.getDiagnostico());
            historial.setTratamiento(actualizado.getTratamiento());
            historial.setNotas(actualizado.getNotas());
            historial.setFecha(actualizado.getFecha());
            historial.setPaciente(actualizado.getPaciente());
            historial.setMedico(actualizado.getMedico());
            return historialRepository.save(historial);
        }).orElseGet(() -> {
            actualizado.setId(id);
            return historialRepository.save(actualizado);
        });
    }

    // 🔴 Eliminar historial
    @DeleteMapping("/{id}")
    public void eliminarHistorial(@PathVariable Long id) {
        historialRepository.deleteById(id);
    }
}
