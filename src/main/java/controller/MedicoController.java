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

import entity.Medico;
import repository.MedicoRepository;

@RestController
@RequestMapping("/api/medicos")
@CrossOrigin(origins = "*")
public class MedicoController {

    @Autowired
    private MedicoRepository medicoRepository;

    // 🟢 Listar todos los médicos
    @GetMapping
    public List<Medico> listarMedicos() {
        return medicoRepository.findAll();
    }

    // 🔵 Obtener médico por ID
    @GetMapping("/{id}")
    public Optional<Medico> obtenerMedico(@PathVariable Long id) {
        return medicoRepository.findById(id);
    }

    // 🟡 Crear nuevo médico
    @PostMapping
    public Medico crearMedico(@RequestBody Medico medico) {
        return medicoRepository.save(medico);
    }

    // 🟠 Actualizar médico existente
    @PutMapping("/{id}")
    public Medico actualizarMedico(@PathVariable Long id, @RequestBody Medico actualizado) {
        return medicoRepository.findById(id).map(medico -> {
            medico.setNombre(actualizado.getNombre());
            medico.setEmail(actualizado.getEmail());
            medico.setContraseña(actualizado.getContraseña());
            medico.setRol(actualizado.getRol());
            medico.setEspecialidad(actualizado.getEspecialidad());
            medico.setConsultorio(actualizado.getConsultorio());
            return medicoRepository.save(medico);
        }).orElseGet(() -> {
            actualizado.setId(id);
            return medicoRepository.save(actualizado);
        });
    }

    // 🔴 Eliminar médico
    @DeleteMapping("/{id}")
    public void eliminarMedico(@PathVariable Long id) {
        medicoRepository.deleteById(id);
    }

    // 🔍 Buscar por especialidad
    @GetMapping("/especialidad/{especialidad}")
    public List<Medico> buscarPorEspecialidad(@PathVariable String especialidad) {
        return medicoRepository.findByEspecialidad(especialidad);
    }
}
