package controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import entity.Paciente;
import repository.PacienteRepository;

@RestController
@RequestMapping("/api/pacientes")
@CrossOrigin(origins = "*")
public class PacienteController {

    @Autowired
    private PacienteRepository pacienteRepository;

    // 🟢 Listar todos los pacientes
    @GetMapping
    public List<Paciente> listarPacientes() {
        return pacienteRepository.findAll();
    }

    // 🔵 Obtener paciente por ID
    @GetMapping("/{id}")
    public Optional<Paciente> obtenerPaciente(@PathVariable Long id) {
        return pacienteRepository.findById(id);
    }

    // 🟡 Crear nuevo paciente
    @PostMapping
    public Paciente crearPaciente(@RequestBody Paciente paciente) {
        return pacienteRepository.save(paciente);
    }

    // 🟠 Actualizar paciente existente
    @PutMapping("/{id}")
    public Paciente actualizarPaciente(@PathVariable Long id, @RequestBody Paciente actualizado) {
        return pacienteRepository.findById(id).map(paciente -> {
            paciente.setNombre(actualizado.getNombre());
            paciente.setEmail(actualizado.getEmail());
            paciente.setContraseña(actualizado.getContraseña());
            paciente.setRol(actualizado.getRol());
            paciente.setFechaNacimiento(actualizado.getFechaNacimiento());
            paciente.setTelefono(actualizado.getTelefono());
            paciente.setDireccion(actualizado.getDireccion());
            return pacienteRepository.save(paciente);
        }).orElseGet(() -> {
            actualizado.setId(id);
            return pacienteRepository.save(actualizado);
        });
    }

    // 🔴 Eliminar paciente
    @DeleteMapping("/{id}")
    public void eliminarPaciente(@PathVariable Long id) {
        pacienteRepository.deleteById(id);
    }
}