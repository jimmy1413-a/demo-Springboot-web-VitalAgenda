package controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import entity.Cita;
import repository.CitaRepository;

@RestController
@RequestMapping("/api/citas")
@CrossOrigin(origins = "*")
public class CitaController {

    @Autowired
    private CitaRepository citaRepository;

    // 🔹 Listar todas las citas
    @GetMapping
    public List<Cita> listarCitas() {
        return citaRepository.findAll();
    }

    // 🔹 Buscar por ID
    @GetMapping("/{id}")
    public ResponseEntity<Cita> obtenerCitaPorId(@PathVariable Long id) {
        Optional<Cita> cita = citaRepository.findById(id);
        return cita.map(ResponseEntity::ok)
                   .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 🔹 Crear una nueva cita
    @PostMapping
    public Cita crearCita(@RequestBody Cita cita) {
        return citaRepository.save(cita);
    }

    // 🔹 Actualizar cita existente
    @PutMapping("/{id}")
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

    // 🔹 Eliminar cita
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCita(@PathVariable Long id) {
        if (citaRepository.existsById(id)) {
            citaRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}