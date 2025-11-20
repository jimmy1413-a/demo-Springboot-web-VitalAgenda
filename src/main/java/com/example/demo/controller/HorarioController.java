package com.example.demo.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
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

import com.example.demo.entity.Horario;
import com.example.demo.entity.Medico;
import com.example.demo.entity.Usuario;
import com.example.demo.repository.HorarioRepository;
import com.example.demo.repository.MedicoRepository;
import com.example.demo.repository.UsuarioRepository;

@Controller
@RequestMapping("/horarios")
public class HorarioController {

    @Autowired
    private HorarioRepository horarioRepository;

    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    // Web methods
    @GetMapping("")
    public String listarHorariosWeb(Authentication authentication, Model model) {
        // Get current logged-in medico
        String email = authentication.getName();
        Usuario usuario = usuarioRepository.findByEmail(email);
        if (usuario instanceof Medico) {
            Medico medico = (Medico) usuario;
            model.addAttribute("horarios", medico.getHorarios());
            model.addAttribute("medico", medico);
            return "horario/horario-list";
        }
        return "redirect:/";
    }

    @GetMapping("/form")
    public String mostrarFormularioHorario(Authentication authentication, Model model) {
        String email = authentication.getName();
        Usuario usuario = usuarioRepository.findByEmail(email);
        if (usuario instanceof Medico) {
            model.addAttribute("horario", new Horario());
            model.addAttribute("medico", usuario);
            return "horario/form";
        }
        return "redirect:/";
    }

    @GetMapping("/{id}/edit")
    public String editarHorario(@PathVariable Long id, Authentication authentication, Model model) {
        String email = authentication.getName();
        Usuario usuario = usuarioRepository.findByEmail(email);
        if (usuario instanceof Medico) {
            Optional<Horario> horario = horarioRepository.findById(id);
            if (horario.isPresent() && horario.get().getMedico().getId().equals(usuario.getId())) {
                model.addAttribute("horario", horario.get());
                model.addAttribute("medico", usuario);
                return "horario/form";
            }
        }
        return "redirect:/horarios";
    }

    // API methods
    @GetMapping("/api")
    @ResponseBody
    public List<Horario> listarHorarios() {
        return horarioRepository.findAll();
    }

    // 🔵 Obtener horario por ID
    @GetMapping("/{id}")
    public Optional<Horario> obtenerHorario(@PathVariable Long id) {
        return horarioRepository.findById(id);
    }

    // 🟡 Crear nuevo horario
    @PostMapping
    public Horario crearHorario(@RequestBody Horario horario) {
        return horarioRepository.save(horario);
    }

    // 🟠 Actualizar horario existente
    @PutMapping("/{id}")
    public Horario actualizarHorario(@PathVariable Long id, @RequestBody Horario actualizado) {
        return horarioRepository.findById(id).map(horario -> {
            horario.setDiaSemana(actualizado.getDiaSemana());
            horario.setHoraInicio(actualizado.getHoraInicio());
            horario.setHoraFin(actualizado.getHoraFin());
            horario.setMedico(actualizado.getMedico());
            return horarioRepository.save(horario);
        }).orElseGet(() -> {
            actualizado.setId(id);
            return horarioRepository.save(actualizado);
        });
    }

    // 🔴 Eliminar horario
    @DeleteMapping("/{id}")
    public void eliminarHorario(@PathVariable Long id) {
        horarioRepository.deleteById(id);
    }
}
