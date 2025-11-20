package com.example.demo.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

import com.example.demo.entity.Cita;
import com.example.demo.entity.HistorialClinico;
import com.example.demo.entity.Medico;
import com.example.demo.entity.Paciente;
import com.example.demo.entity.Roles;
import com.example.demo.entity.Usuario;
import com.example.demo.repository.CitaRepository;
import com.example.demo.repository.HistorialClinicoRepository;
import com.example.demo.repository.MedicoRepository;
import com.example.demo.repository.PacienteRepository;
import com.example.demo.repository.UsuarioRepository;

@Controller
@RequestMapping("/pacientes")
public class PacienteController {

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private CitaRepository citaRepository;

    @Autowired
    private HistorialClinicoRepository historialRepository;

    // Web methods
    @GetMapping("")
    public String listarPacientesWeb(Principal principal, Model model) {
        Usuario usuario = usuarioRepository.findByEmail(principal.getName());

        if (usuario != null && "MEDICO".equals(usuario.getRol().name())) {
            // Para médicos, mostrar pacientes con citas o historial clínico
            Medico medico = medicoRepository.findById(usuario.getId()).orElse(null);
            if (medico != null) {
                // Pacientes con citas
                List<Paciente> pacientesConCitas = citaRepository.findAll().stream()
                    .filter(cita -> cita.getMedico() != null && cita.getMedico().getId().equals(medico.getId()))
                    .filter(cita -> cita.getPaciente() != null)
                    .map(Cita::getPaciente)
                    .distinct()
                    .collect(Collectors.toList());

                // Pacientes con historial clínico
                List<Paciente> pacientesConHistorial = historialRepository.findAll().stream()
                    .filter(historial -> historial.getMedico() != null && historial.getMedico().getId().equals(medico.getId()))
                    .filter(historial -> historial.getPaciente() != null)
                    .map(HistorialClinico::getPaciente)
                    .distinct()
                    .collect(Collectors.toList());

                // Combinar ambas listas y eliminar duplicados
                List<Paciente> todosLosPacientes = new ArrayList<>();
                todosLosPacientes.addAll(pacientesConCitas);
                todosLosPacientes.addAll(pacientesConHistorial);
                List<Paciente> pacientesUnicos = todosLosPacientes.stream()
                    .distinct()
                    .collect(Collectors.toList());

                model.addAttribute("pacientes", pacientesUnicos);
                model.addAttribute("esMedico", true);
            }
        } else {
            // Para administradores, mostrar todos los pacientes
            model.addAttribute("pacientes", pacienteRepository.findAll());
            model.addAttribute("esMedico", false);
        }

        return "paciente/paciente-list";
    }

    @GetMapping("/form")
    public String mostrarFormularioPaciente(Principal principal, Model model) {
        Usuario usuario = usuarioRepository.findByEmail(principal.getName());
        if (!"ADMIN".equals(usuario.getRol().name())) {
            return "redirect:/pacientes";
        }
        model.addAttribute("paciente", new Paciente());
        return "paciente/form";
    }

    @GetMapping("/{id}/edit")
    public String editarPaciente(@PathVariable Long id, Model model) {
        Optional<Paciente> paciente = pacienteRepository.findById(id);
        if (paciente.isPresent()) {
            model.addAttribute("paciente", paciente.get());
            return "paciente/form";
        }
        return "redirect:/pacientes";
    }

    @GetMapping("/{id}")
    public String verDetallePaciente(@PathVariable Long id, Model model) {
        Optional<Paciente> paciente = pacienteRepository.findById(id);
        if (paciente.isPresent()) {
            model.addAttribute("paciente", paciente.get());
            return "paciente/paciente-detail";
        }
        return "redirect:/pacientes";
    }

    // API methods
    @GetMapping("/api")
    @ResponseBody
    public List<Paciente> obtenerTodos() {
        return pacienteRepository.findAll();
    }

    @GetMapping("/api/{id}")
    @ResponseBody
    public ResponseEntity<Paciente> obtenerPorId(@PathVariable Long id) {
        return pacienteRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping(consumes = "application/json")
    public ResponseEntity<Paciente> crearPaciente(@RequestBody Paciente paciente, Principal principal) {
        Usuario usuarioActual = usuarioRepository.findByEmail(principal.getName());
        if (!"ADMIN".equals(usuarioActual.getRol().name())) {
            return ResponseEntity.status(403).build();
        }
        paciente.setRol(Roles.PACIENTE); // Asegurar que el rol sea PACIENTE
        // Guardar usando usuarioRepository para que se guarde correctamente en ambas tablas
        Usuario nuevoUsuario = usuarioRepository.save(paciente);
        Paciente nuevoPaciente = (Paciente) nuevoUsuario;
        return ResponseEntity.ok(nuevoPaciente);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Paciente> actualizarPaciente(@PathVariable Long id,  @RequestBody Paciente datosActualizados) {
        Optional<Paciente> pacienteOpt = pacienteRepository.findById(id);
        if (pacienteOpt.isPresent()) {
            Paciente paciente = pacienteOpt.get();
            paciente.setFechaNacimiento(datosActualizados.getFechaNacimiento());
            paciente.setTelefono(datosActualizados.getTelefono());
            paciente.setDireccion(datosActualizados.getDireccion());
            // Usar usuarioRepository para guardar correctamente
            Usuario actualizado = usuarioRepository.save(paciente);
            return ResponseEntity.ok((Paciente) actualizado);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPaciente(@PathVariable Long id) {
        if (pacienteRepository.existsById(id)) {
            pacienteRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/citas")
    public ResponseEntity<List<Cita>> verCitas(@PathVariable Long id) {
        return pacienteRepository.findById(id)
                .map(p -> ResponseEntity.ok(p.verCitas()))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/historial")
    public ResponseEntity<List<HistorialClinico>> verHistorial(@PathVariable Long id) {
        return pacienteRepository.findById(id)
                .map(p -> ResponseEntity.ok(p.verHistorial()))
                .orElse(ResponseEntity.notFound().build());
    }
}