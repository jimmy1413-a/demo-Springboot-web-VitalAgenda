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

import entity.Cita;
import repository.CitaRepository;

@RestController
@RequestMapping("/api/citas")
@CrossOrigin(origins = "*")
public class CitaController {

    @Autowired
    private CitaRepository citaRepository;

    // 🟢 Obtener todas las citas
    @GetMapping
    public List<Cita> listarCitas() {
        return citaRepository.findAll();
    }

    // 🔵 Obtener una cita por ID
    @GetMapping("/{id}")
    public Optional<Cita> obtenerCita(@PathVariable Long id) {
        return citaRepository.findById(id);
    }

    // 🟡 Crear una nueva cita
    @PostMapping
    public Cita crearCita(@RequestBody Cita cita) {
        return citaRepository.save(cita);
    }

    // 🟠 Actualizar una cita existente
    @PutMapping("/{id}")
    public Cita actualizarCita(@PathVariable Long id, @RequestBody Cita citaActualizada) {
        return citaRepository.findById(id).map(cita -> {
            cita.setFecha(citaActualizada.getFecha());
            cita.setHora(citaActualizada.getHora());
            cita.setEstado(citaActualizada.getEstado());
            cita.setMotivo(citaActualizada.getMotivo());
            cita.setPaciente(citaActualizada.getPaciente());
            cita.setMedico(citaActualizada.getMedico());
            return citaRepository.save(cita);
        }).orElseGet(() -> {
            citaActualizada.setId(id);
            return citaRepository.save(citaActualizada);
        });
    }

    // 🔴 Eliminar una cita
    @DeleteMapping("/{id}")
    public void eliminarCita(@PathVariable Long id) {
        citaRepository.deleteById(id);
    }
}
