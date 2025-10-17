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

import entity.Horario;
import repository.HorarioRepository;

@RestController
@RequestMapping("/api/horarios")
@CrossOrigin(origins = "*")
public class HorarioController {

    @Autowired
    private HorarioRepository horarioRepository;

    // 🟢 Listar todos los horarios
    @GetMapping
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
