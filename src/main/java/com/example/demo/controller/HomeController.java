package com.example.demo.controller;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.entity.Cita;
import com.example.demo.entity.HistorialClinico;
import com.example.demo.entity.Medico;
import com.example.demo.entity.Paciente;
import com.example.demo.entity.Usuario;
import com.example.demo.repository.CitaRepository;
import com.example.demo.repository.HistorialClinicoRepository;
import com.example.demo.repository.MedicoRepository;
import com.example.demo.repository.PacienteRepository;
import com.example.demo.repository.UsuarioRepository;

@Controller
public class HomeController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private CitaRepository citaRepository;

    @Autowired
    private HistorialClinicoRepository historialRepository;

    @GetMapping("/")
    public String home(Authentication authentication) {
        if (authentication != null) {
            String role = authentication.getAuthorities().iterator().next().getAuthority();
            if (role.equals("ROLE_PACIENTE")) {
                return "redirect:/paciente/dashboard";
            } else if (role.equals("ROLE_MEDICO")) {
                return "redirect:/medico/dashboard";
            } else if (role.equals("ROLE_ADMIN")) {
                return "redirect:/admin/dashboard";
            }
        }
        return "redirect:/login";
    }

    @GetMapping("/paciente/dashboard")
    public String pacienteDashboard(Model model, Principal principal) {
        Usuario usuario = usuarioRepository.findByEmail(principal.getName());
        Paciente paciente = pacienteRepository.findById(usuario.getId()).orElse(null);

        if (paciente != null) {
            // Citas del paciente
            List<Cita> citas = paciente.getCitas();
            List<Cita> citasPendientes = citas.stream()
                .filter(c -> c.getEstado() != null && c.getEstado().toString().equals("PENDIENTE"))
                .collect(Collectors.toList());
            List<Cita> citasConfirmadas = citas.stream()
                .filter(c -> c.getEstado() != null && c.getEstado().toString().equals("CONFIRMADA"))
                .collect(Collectors.toList());

            // Historial clínico del paciente (ordenado por fecha descendente)
            List<HistorialClinico> historialClinico = historialRepository.findByPaciente(paciente).stream()
                .filter(h -> h.getFecha() != null)
                .sorted((h1, h2) -> h2.getFecha().compareTo(h1.getFecha()))
                .collect(Collectors.toList());

            model.addAttribute("paciente", paciente);
            model.addAttribute("citasPendientes", citasPendientes);
            model.addAttribute("citasConfirmadas", citasConfirmadas);
            model.addAttribute("totalCitas", citas.size());
            model.addAttribute("historialClinico", historialClinico);
        }

        return "paciente/dashboard/dashboard";
    }

    @GetMapping("/paciente/citas")
    public String pacienteCitas(Model model, Principal principal) {
        Usuario usuario = usuarioRepository.findByEmail(principal.getName());
        Paciente paciente = pacienteRepository.findById(usuario.getId()).orElse(null);

        if (paciente != null) {
            // Citas del paciente ordenadas por fecha (más recientes primero)
            List<Cita> citas = paciente.getCitas().stream()
                .sorted((c1, c2) -> c2.getFecha().compareTo(c1.getFecha()))
                .collect(Collectors.toList());

            List<Cita> citasPendientes = citas.stream()
                .filter(c -> c.getEstado() != null && c.getEstado().toString().equals("PENDIENTE"))
                .collect(Collectors.toList());
            List<Cita> citasConfirmadas = citas.stream()
                .filter(c -> c.getEstado() != null && c.getEstado().toString().equals("CONFIRMADA"))
                .collect(Collectors.toList());

            model.addAttribute("paciente", paciente);
            model.addAttribute("citas", citas);
            model.addAttribute("citasPendientes", citasPendientes);
            model.addAttribute("citasConfirmadas", citasConfirmadas);
            model.addAttribute("totalCitas", citas.size());
        }

        return "paciente/citas";
    }

    @GetMapping("/paciente/historial")
    public String pacienteHistorial(Model model, Principal principal) {
        Usuario usuario = usuarioRepository.findByEmail(principal.getName());
        Paciente paciente = pacienteRepository.findById(usuario.getId()).orElse(null);

        if (paciente != null) {
            // Historial clínico del paciente (ordenado por fecha descendente)
            List<HistorialClinico> historialClinico = historialRepository.findByPaciente(paciente).stream()
                .filter(h -> h.getFecha() != null)
                .sorted((h1, h2) -> h2.getFecha().compareTo(h1.getFecha()))
                .collect(Collectors.toList());

            // Contar médicos únicos
            long medicosUnicos = historialClinico.stream()
                .map(h -> h.getMedico())
                .distinct()
                .count();

            model.addAttribute("paciente", paciente);
            model.addAttribute("historialClinico", historialClinico);
            model.addAttribute("medicosUnicos", medicosUnicos);
        }

        return "paciente/historial";
    }

    @GetMapping("/medico/dashboard")
    public String medicoDashboard(Model model, Principal principal) {
        Usuario usuario = usuarioRepository.findByEmail(principal.getName());
        Medico medico = medicoRepository.findById(usuario.getId()).orElse(null);

        if (medico != null) {
            // Todas las citas del médico ordenadas por fecha (más recientes primero)
            List<Cita> todasLasCitas = medico.getCitas().stream()
                .sorted((c1, c2) -> c2.getFecha().compareTo(c1.getFecha()))
                .collect(Collectors.toList());

            // Citas pendientes
            List<Cita> citasPendientes = medico.getCitas().stream()
                .filter(c -> c.getEstado() != null && c.getEstado().toString().equals("PENDIENTE"))
                .collect(Collectors.toList());

            // Citas del día actual
            LocalDate hoy = LocalDate.now();
            List<Cita> citasHoy = medico.getCitas().stream()
                .filter(c -> c.getFecha().equals(hoy))
                .collect(Collectors.toList());

            // Historiales recientes del médico (últimos 3, ordenados por fecha descendente)
            List<HistorialClinico> historialesRecientes = historialRepository.findByMedico(medico).stream()
                .filter(h -> h.getPaciente() != null && h.getFecha() != null)
                .sorted((h1, h2) -> h2.getFecha().compareTo(h1.getFecha()))
                .limit(3)
                .collect(Collectors.toList());

            model.addAttribute("medico", medico);
            model.addAttribute("todasLasCitas", todasLasCitas);
            model.addAttribute("citasHoy", citasHoy);
            model.addAttribute("citasPendientes", citasPendientes);
            model.addAttribute("historialesRecientes", historialesRecientes);
            model.addAttribute("totalPacientes", medico.getCitas().stream()
                .map(c -> c.getPaciente())
                .distinct()
                .count());
        }

        return "medico/dashboard/dashboard";
    }

    @GetMapping("/admin/dashboard")
    public String adminDashboard(Model model) {
        // Estadísticas generales
        long totalPacientes = pacienteRepository.count();
        long totalMedicos = medicoRepository.count();
        long totalCitas = citaRepository.count();
        long citasPendientes = citaRepository.findAll().stream()
            .filter(c -> c.getEstado() != null && c.getEstado().toString().equals("PENDIENTE"))
            .count();

        model.addAttribute("totalPacientes", totalPacientes);
        model.addAttribute("totalMedicos", totalMedicos);
        model.addAttribute("totalCitas", totalCitas);
        model.addAttribute("citasPendientes", citasPendientes);

        return "admin/dashboard/dashboard";
    }
}