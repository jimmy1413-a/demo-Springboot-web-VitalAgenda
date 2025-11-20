package com.example.demo.controller;

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

import com.example.demo.entity.Medico;
import com.example.demo.repository.MedicoRepository;

@Controller
@RequestMapping("/medicos")
public class MedicoController {

    @Autowired
    private MedicoRepository medicoRepository;

    // Web methods
    @GetMapping("")
    public String listarMedicosWeb(Model model) {
        model.addAttribute("medicos", medicoRepository.findAll());
        model.addAttribute("esMedico", false); // Los administradores ven esta página
        return "medico/medico-list";
    }

    @GetMapping("/form")
    public String mostrarFormularioMedico(Model model) {
        model.addAttribute("medico", new Medico());
        return "medico/form";
    }

    @GetMapping("/{id}/edit")
    public String editarMedico(@PathVariable Long id, Model model) {
        Optional<Medico> medico = medicoRepository.findById(id);
        if (medico.isPresent()) {
            model.addAttribute("medico", medico.get());
            return "medico/form";
        }
        return "redirect:/medicos";
    }

    @GetMapping("/{id}")
    public String verDetalleMedico(@PathVariable Long id, Model model) {
        Optional<Medico> medico = medicoRepository.findById(id);
        if (medico.isPresent()) {
            model.addAttribute("medico", medico.get());
            return "medico/medico-detail";
        }
        return "redirect:/medicos";
    }

    @PostMapping("")
    public String crearMedicoWeb(@ModelAttribute Medico medico) {
        medicoRepository.save(medico);
        return "redirect:/medicos";
    }

    @PutMapping("/{id}")
    public String actualizarMedicoWeb(@PathVariable Long id, @ModelAttribute Medico medico) {
        medico.setId(id);
        medicoRepository.save(medico);
        return "redirect:/medicos";
    }

    // API methods
    @GetMapping("/api")
    @ResponseBody
    public List<Medico> listarMedicos() {
        return medicoRepository.findAll();
    }

    @GetMapping("/api/{id}")
    @ResponseBody
    public ResponseEntity<Medico> obtenerMedicoPorId(@PathVariable Long id) {
        Optional<Medico> medico = medicoRepository.findById(id);
        return medico.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/api")
    @ResponseBody
    public Medico crearMedico(@RequestBody Medico medico) {
        return medicoRepository.save(medico);
    }

    @PutMapping("/api/{id}")
    @ResponseBody
    public ResponseEntity<Medico> actualizarMedico(@PathVariable Long id, @RequestBody Medico datosActualizados) {
        return medicoRepository.findById(id).map(medico -> {
            medico.setEspecialidad(datosActualizados.getEspecialidad());
            medico.setConsultorio(datosActualizados.getConsultorio());
            return ResponseEntity.ok(medicoRepository.save(medico));
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/api/{id}")
    @ResponseBody
    public ResponseEntity<Void> eliminarMedico(@PathVariable Long id) {
        if (medicoRepository.existsById(id)) {
            medicoRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    // Buscar por especialidad
    @GetMapping("/api/especialidad/{especialidad}")
    @ResponseBody
    public List<Medico> buscarPorEspecialidad(@PathVariable String especialidad) {
        return medicoRepository.findByEspecialidad(especialidad);
    }

    // Buscar por nombre (parcial, sin distinción de mayúsculas)
    @GetMapping("/api/nombre/{nombre}")
    @ResponseBody
    public List<Medico> buscarPorNombre(@PathVariable String nombre) {
        return medicoRepository.findByNombreContainingIgnoreCase(nombre);
    }

    // Buscar por consultorio
    @GetMapping("/api/consultorio/{consultorio}")
    @ResponseBody
    public List<Medico> buscarPorConsultorio(@PathVariable String consultorio) {
        return medicoRepository.findByConsultorio(consultorio);
    }
}