package com.example.demo.controller;


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

import com.example.demo.entity.Recordatorio;
import com.example.demo.repository.RecordatorioRepository;

@RestController
@RequestMapping("/api/recordatorios")
@CrossOrigin(origins = "*")
public class RecordatorioController {

    @Autowired
    private RecordatorioRepository recordatorioRepository;

    // 🟢 Listar todos los recordatorios
    @GetMapping
    public List<Recordatorio> listarRecordatorios() {
        return recordatorioRepository.findAll();
    }

    // 🔵 Obtener recordatorio por ID
    @GetMapping("/{id}")
    public Optional<Recordatorio> obtenerRecordatorio(@PathVariable Long id) {
        return recordatorioRepository.findById(id);
    }

    // 🟡 Crear nuevo recordatorio
    @PostMapping
    public Recordatorio crearRecordatorio(@RequestBody Recordatorio recordatorio) {
        return recordatorioRepository.save(recordatorio);
    }

    // 🟠 Actualizar recordatorio existente
    @PutMapping("/{id}")
    public Recordatorio actualizarRecordatorio(@PathVariable Long id, @RequestBody Recordatorio actualizado) {
        return recordatorioRepository.findById(id).map(recordatorio -> {
            recordatorio.setMensaje(actualizado.getMensaje());
            recordatorio.setFechaEnvio(actualizado.getFechaEnvio());
            recordatorio.setEnviado(actualizado.isEnviado());
            recordatorio.setCita(actualizado.getCita());
            return recordatorioRepository.save(recordatorio);
        }).orElseGet(() -> {
            actualizado.setId(id);
            return recordatorioRepository.save(actualizado);
        });
    }

    // 🔴 Eliminar recordatorio
    @DeleteMapping("/{id}")
    public void eliminarRecordatorio(@PathVariable Long id) {
        recordatorioRepository.deleteById(id);
    }
}