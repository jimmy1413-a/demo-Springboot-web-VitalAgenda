package com.example.demo.controller;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.entity.Cita;
import com.example.demo.entity.Medico;
import com.example.demo.entity.Usuario;
import com.example.demo.repository.CitaRepository;
import com.example.demo.repository.MedicoRepository;
import com.example.demo.repository.UsuarioRepository;
import com.example.demo.service.NotificationService;

@Controller
@RequestMapping("/citas")
public class CitaController {

    @Autowired
    private CitaRepository citaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private NotificationService notificationService;

    // Home page
    @GetMapping("/")
    public String home() {
        return "redirect:/citas";
    }

    // Web methods
    @GetMapping("")
    public String listarCitasWeb(Model model, Principal principal) {
        Usuario usuario = usuarioRepository.findByEmail(principal.getName());

        if (usuario != null && "MEDICO".equals(usuario.getRol().name())) {
            // Para médicos, mostrar todas las citas
            Medico medico = medicoRepository.findById(usuario.getId()).orElse(null);
            model.addAttribute("citas", citaRepository.findAll());
            model.addAttribute("medico", medico);
            model.addAttribute("navFragment", "fragments/medico-nav.html");
        } else {
            // Para administradores, mostrar todas las citas
            model.addAttribute("citas", citaRepository.findAll());
            model.addAttribute("navFragment", "fragments/admin-nav.html");
        }

        return "cita/cita-list";
    }

    @GetMapping("/form")
    public String mostrarFormularioCita(Principal principal, Model model) {
        Usuario usuario = usuarioRepository.findByEmail(principal.getName());

        // Crear cita con médico preseleccionado si el usuario es médico
        Cita cita = new Cita();
        if (usuario != null && "MEDICO".equals(usuario.getRol().name())) {
            Medico medico = medicoRepository.findById(usuario.getId()).orElse(null);
            if (medico != null) {
                cita.setMedico(medico);
            }
            model.addAttribute("navFragment", "fragments/medico-nav.html");
            model.addAttribute("isMedico", true);
        } else {
            model.addAttribute("navFragment", "fragments/admin-nav.html");
            model.addAttribute("isMedico", false);
        }

        model.addAttribute("cita", cita);
        return "cita/form";
    }

    @GetMapping("/{id}/edit")
    public String editarCita(@PathVariable Long id, Principal principal, Model model) {
        Optional<Cita> citaOpt = citaRepository.findById(id);
        if (citaOpt.isPresent()) {
            Usuario usuario = usuarioRepository.findByEmail(principal.getName());
            if (usuario != null && "MEDICO".equals(usuario.getRol().name())) {
                model.addAttribute("navFragment", "fragments/medico-nav.html");
                model.addAttribute("isMedico", true);
            } else {
                model.addAttribute("navFragment", "fragments/admin-nav.html");
                model.addAttribute("isMedico", false);
            }
            model.addAttribute("cita", citaOpt.get());
            return "cita/form";
        }
        return "redirect:/citas";
    }

    @GetMapping("/{id}")
    public String verDetalleCita(@PathVariable Long id, Model model) {
        Optional<Cita> cita = citaRepository.findById(id);
        if (cita.isPresent()) {
            model.addAttribute("cita", cita.get());
            return "cita/cita-detail";
        }
        return "redirect:/citas";
    }

    @PostMapping("")
    public String crearCitaWeb(@ModelAttribute Cita cita) {
        citaRepository.save(cita);
        return "redirect:/citas";
    }

    @PutMapping("/{id}")
    public String actualizarCitaWeb(@PathVariable Long id, @ModelAttribute Cita cita) {
        cita.setId(id);
        citaRepository.save(cita);
        return "redirect:/citas";
    }

    // API methods
    @GetMapping("/api")
    @ResponseBody
    public List<Cita> listarCitas() {
        return citaRepository.findAll();
    }

    @GetMapping("/api/{id}")
    @ResponseBody
    public ResponseEntity<Cita> obtenerCitaPorId(@PathVariable Long id) {
        Optional<Cita> cita = citaRepository.findById(id);
        return cita.map(ResponseEntity::ok)
                   .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/api")
    @ResponseBody
    public Cita crearCita(@RequestBody Cita cita) {
        Cita citaGuardada = citaRepository.save(cita);
        // Crear notificaciones para la cita
        notificationService.crearNotificacionesParaCita(citaGuardada);
        return citaGuardada;
    }

    @PutMapping("/api/{id}")
    @ResponseBody
    public ResponseEntity<Cita> actualizarCita(@PathVariable Long id, @RequestBody Cita nuevaCita) {
        return citaRepository.findById(id)
                .map(cita -> {
                    cita.setFecha(nuevaCita.getFecha());
                    cita.setHora(nuevaCita.getHora());
                    cita.setEstado(nuevaCita.getEstado());
                    cita.setMotivo(nuevaCita.getMotivo());
                    cita.setPaciente(nuevaCita.getPaciente());
                    cita.setMedico(nuevaCita.getMedico());
                    return ResponseEntity.ok(citaRepository.save(cita));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/api/{id}")
    @ResponseBody
    public ResponseEntity<Void> eliminarCita(@PathVariable Long id) {
        if (citaRepository.existsById(id)) {
            citaRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}