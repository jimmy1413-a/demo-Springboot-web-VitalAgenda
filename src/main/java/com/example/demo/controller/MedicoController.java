package com.example.demo.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.Medico;
import com.example.demo.repository.MedicoRepository;

@RestController
@RequestMapping("/api/medicos")
public class MedicoController {

    @Autowired
    private MedicoRepository medicoRepository;

    // Obtener todos los médicos
    @GetMapping
    public List<Medico> listarMedicos() {
        return medicoRepository.findAll();
    }

    // Obtener médico por ID
    @GetMapping("/{id}")
    public ResponseEntity<Medico> obtenerMedicoPorId(@PathVariable Long id) {
        Optional<Medico> medico = medicoRepository.findById(id);
        return medico.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Crear nuevo médico
    @PostMapping
    public Medico crearMedico(@RequestBody Medico medico) {
        return medicoRepository.save(medico);
    }

    // Actualizar médico existente
    @PutMapping("/{id}")
    public ResponseEntity<Medico> actualizarMedico(@PathVariable Long id, @RequestBody Medico datosActualizados) {
        return medicoRepository.findById(id).map(medico -> {
            medico.setEspecialidad(datosActualizados.getEspecialidad());
            medico.setConsultorio(datosActualizados.getConsultorio());
            return ResponseEntity.ok(medicoRepository.save(medico));
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Eliminar médico
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarMedico(@PathVariable Long id) {
        if (medicoRepository.existsById(id)) {
            medicoRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    // Buscar por especialidad
    @GetMapping("/especialidad/{especialidad}")
    public List<Medico> buscarPorEspecialidad(@PathVariable String especialidad) {
        return medicoRepository.findByEspecialidad(especialidad);
    }

    // Buscar por nombre (parcial, sin distinción de mayúsculas)
    @GetMapping("/nombre/{nombre}")
    public List<Medico> buscarPorNombre(@PathVariable String nombre) {
        return medicoRepository.findByNombreContainingIgnoreCase(nombre);
    }

    // Buscar por consultorio
    @GetMapping("/consultorio/{consultorio}")
    public List<Medico> buscarPorConsultorio(@PathVariable String consultorio) {
        return medicoRepository.findByConsultorio(consultorio);
    }
}