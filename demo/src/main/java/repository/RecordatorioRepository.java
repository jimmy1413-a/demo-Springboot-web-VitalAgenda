package repository;

import org.springframework.data.jpa.repository.JpaRepository;

import entity.Recordatorio;

public interface RecordatorioRepository extends JpaRepository<Recordatorio, Long> {
}

