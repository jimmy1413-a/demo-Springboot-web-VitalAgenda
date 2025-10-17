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

import entity.HistorialClinico;
import repository.HistorialClinicoRepository;

@RestController
@RequestMapping("/api/historiales")
@CrossOrigin(origins = "*")
public class HistorialClinicoController {

    @Autowired
    private HistorialClinicoRepository historialRepository;

    // 🟢 Listar todos los historiales
    @GetMapping
    public List<HistorialClinico> listarHistoriales() {
        return historialRepository.findAll();
    }

    // 🔵 Obtener historial por ID
    @GetMapping("/{id}")
    public Optional<HistorialClinico> obtenerHistorial(@PathVariable Long id) {
        return historialRepository.findById(id);
    }

    // 🟡 Crear nuevo historial
    @PostMapping
    public HistorialClinico crearHistorial(@RequestBody HistorialClinico historial) {
        return historialRepository.save(historial);
    }

    // 🟠 Actualizar historial existente
    @PutMapping("/{id}")
    public HistorialClinico actualizarHistorial(@PathVariable Long id, @RequestBody HistorialClinico actualizado) {
        return historialRepository.findById(id).map(historial -> {
            historial.setDiagnostico(actualizado.getDiagnostico());
            historial.setTratamiento(actualizado.getTratamiento());
            historial.setNotas(actualizado.getNotas());
            historial.setFecha(actualizado.getFecha());
            historial.setPaciente(actualizado.getPaciente());
            historial.setMedico(actualizado.getMedico());
            return historialRepository.save(historial);
        }).orElseGet(() -> {
            actualizado.setId(id);
            return historialRepository.save(actualizado);
        });
    }

    // 🔴 Eliminar historial
    @DeleteMapping("/{id}")
    public void eliminarHistorial(@PathVariable Long id) {
        historialRepository.deleteById(id);
    }
}
