package com.example.demo;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.demo.entity.HistorialClinico;
import com.example.demo.entity.Medico;
import com.example.demo.entity.Paciente;
import com.example.demo.entity.Roles;
import com.example.demo.entity.Usuario;
import com.example.demo.repository.CitaRepository;
import com.example.demo.repository.HistorialClinicoRepository;
import com.example.demo.repository.MedicoRepository;
import com.example.demo.repository.PacienteRepository;
import com.example.demo.repository.RecordatorioRepository;
import com.example.demo.repository.UsuarioRepository;
import com.example.demo.service.NotificationService;

@SpringBootApplication
@EnableScheduling
public class DemoApplication implements CommandLineRunner {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private MedicoRepository medicoRepository;

	@Autowired
	private PacienteRepository pacienteRepository;

	@Autowired
	private CitaRepository citaRepository;

	@Autowired
	private HistorialClinicoRepository historialRepository;

	@Autowired
	private RecordatorioRepository recordatorioRepository;

	@Autowired
	private NotificationService notificationService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// Crear usuarios de prueba si no existen
		crearUsuariosDePrueba();
	}

	private void crearUsuariosDePrueba() {
		// Verificar y crear administrador
		if (usuarioRepository.findByEmail("admin@hospital.com") == null) {
			Usuario admin = new Usuario();
			admin.setNombre("Administrador");
			admin.setEmail("admin@hospital.com");
			admin.setContrasena(passwordEncoder.encode("admin123"));
			admin.setRol(Roles.ADMIN);
			usuarioRepository.save(admin);
			System.out.println("✓ Administrador creado: admin@hospital.com");
		}

		// Verificar y crear/actualizar médico
		Usuario usuarioExistente = usuarioRepository.findByEmail("medico@hospital.com");
		if (usuarioExistente == null) {
			Medico medico = new Medico();
			medico.setNombre("Dra. Luna");
			medico.setEmail("medico@hospital.com");
			medico.setContrasena(passwordEncoder.encode("medico123"));
			medico.setRol(Roles.MEDICO);
			medico.setEspecialidad("Medicina General");
			medico.setConsultorio("Consultorio 101");
			medicoRepository.save(medico);
			System.out.println("✓ Médico creado: medico@hospital.com");
		} else {
			// Actualizar el médico existente
			usuarioExistente.setNombre("Dra. Luna");
			usuarioRepository.save(usuarioExistente);
			System.out.println("✓ Médico actualizado: medico@hospital.com");
		}

		// Verificar y crear paciente
		if (usuarioRepository.findByEmail("paciente@hospital.com") == null) {
			Paciente paciente = new Paciente();
			paciente.setNombre("María García");
			paciente.setEmail("paciente@hospital.com");
			paciente.setContrasena(passwordEncoder.encode("paciente123"));
			paciente.setRol(Roles.PACIENTE);
			paciente.setFechaNacimiento(LocalDate.of(1990, 5, 15));
			paciente.setTelefono("555-0123");
			paciente.setDireccion("Calle Principal 123");
			pacienteRepository.save(paciente);
			System.out.println("✓ Paciente creado: paciente@hospital.com");
		}

		// Verificar paciente Juan (usar el que ya existe en la base de datos)
		Usuario pacienteJuanExistente = usuarioRepository.findByEmail("juan@example.com");
		if (pacienteJuanExistente != null) {
			// Asegurar que tenga el rol PACIENTE
			if (pacienteJuanExistente.getRol() != Roles.PACIENTE) {
				pacienteJuanExistente.setRol(Roles.PACIENTE);
				System.out.println("✓ Rol PACIENTE asignado a juan@example.com");
			}

			// FORZAR actualización de contraseña a "123456" encriptada
			String nuevaContrasenaEncriptada = passwordEncoder.encode("123456");
			pacienteJuanExistente.setContrasena(nuevaContrasenaEncriptada);
			usuarioRepository.save(pacienteJuanExistente);

			System.out.println("✓ Paciente configurado: juan@example.com");
			System.out.println("   📧 Email: juan@example.com");
			System.out.println("   🔑 Contraseña: 123456 (forzada)");
			System.out.println("   👤 Rol: " + pacienteJuanExistente.getRol());
			System.out.println("   🔐 Hash generado: " + nuevaContrasenaEncriptada.substring(0, 20) + "...");
		} else {
			System.out.println("⚠ Paciente juan@example.com no encontrado en la base de datos");
		}

		// Verificar y crear/actualizar médico Laura Martínez (con datos existentes)
		Usuario lauraExistente = usuarioRepository.findByEmail("laura.martinez@hospital.com");
		if (lauraExistente == null) {
			Medico laura = new Medico();
			laura.setNombre("Dra. Laura Martínez");
			laura.setEmail("laura.martinez@hospital.com");
			laura.setContrasena(passwordEncoder.encode("laura123"));
			laura.setRol(Roles.MEDICO);
			laura.setEspecialidad("Cardiología");
			laura.setConsultorio("Consultorio 201");
			medicoRepository.save(laura);
			System.out.println("✓ Médico Laura creado: laura.martinez@hospital.com");
		} else {
			// Actualizar contraseña si no está encriptada
			if (!lauraExistente.getContrasena().startsWith("$2a$") &&
				!lauraExistente.getContrasena().startsWith("$2b$") &&
				!lauraExistente.getContrasena().startsWith("$2y$")) {
				lauraExistente.setContrasena(passwordEncoder.encode("laura123"));
				usuarioRepository.save(lauraExistente);
				System.out.println("✓ Contraseña de Laura actualizada a BCrypt");
			}
			System.out.println("✓ Médico Laura ya existe: laura.martinez@hospital.com");
		}

		// Crear datos de prueba para Laura
		crearDatosDePruebaLaura();

		System.out.println("=== USUARIOS DE PRUEBA LISTOS ===");
		System.out.println("Administrador: admin@hospital.com / admin123");
		System.out.println("Médico: medico@hospital.com / medico123");
		System.out.println("Médico Laura: laura.martinez@hospital.com / laura123");
		System.out.println("Paciente: paciente@hospital.com / paciente123");
		System.out.println("==================================");
		System.out.println("💡 Datos de prueba creados para Laura Martínez");
	}

	private void crearDatosDePruebaLaura() {
		try {
			// Obtener médico Laura
			Usuario lauraUsuario = usuarioRepository.findByEmail("laura.martinez@hospital.com");
			if (lauraUsuario != null) {
				Medico laura = medicoRepository.findById(lauraUsuario.getId()).orElse(null);
				if (laura != null) {
					// Crear pacientes para Laura si no existen y sus historiales
					crearHistorialParaPaciente(laura, "carlos.rodriguez@email.com", "Carlos Rodríguez",
						LocalDate.of(1985, 3, 20), "555-0456", "Avenida Central 456",
						"Hipertensión arterial", "Medicación antihipertensiva, dieta baja en sal",
						"Paciente con buen control de la presión arterial", LocalDate.now().minusDays(30));

					crearHistorialParaPaciente(laura, "ana.lopez@email.com", "Ana López",
						LocalDate.of(1992, 7, 15), "555-0789", "Plaza Mayor 789",
						"Arritmia cardíaca", "Medicación antiarrítmica, monitoreo continuo",
						"Paciente requiere seguimiento mensual", LocalDate.now().minusDays(15));

					// Crear historial para Juan Pérez (usando paciente existente de BD)
					Usuario usuarioJuan = usuarioRepository.findByEmail("juan@example.com");
					if (usuarioJuan != null) {
						Paciente pacienteJuan = pacienteRepository.findById(usuarioJuan.getId()).orElse(null);
						if (pacienteJuan != null) {
							// Verificar si ya existe historial para este paciente con este médico
							boolean historialExiste = historialRepository.findAll().stream()
								.anyMatch(h -> h.getPaciente() != null && h.getPaciente().getId().equals(pacienteJuan.getId())
									&& h.getMedico() != null && h.getMedico().getId().equals(laura.getId()));

							if (!historialExiste) {
								HistorialClinico historialJuan = new HistorialClinico();
								historialJuan.setPaciente(pacienteJuan);
								historialJuan.setMedico(laura);
								historialJuan.setDiagnostico("Dolor de cabeza recurrente");
								historialJuan.setTratamiento("Analgésicos, descanso adecuado");
								historialJuan.setNotas("Paciente con migrañas frecuentes, recomendar seguimiento neurológico");
								historialJuan.setFecha(LocalDate.now().minusDays(7));
								historialRepository.save(historialJuan);
								System.out.println("✓ Historial creado para Juan Pérez (de BD) con Dra. Laura Martínez");
							} else {
								System.out.println("✓ Historial ya existe para Juan Pérez con Dra. Laura Martínez");
							}
						}
					}

					System.out.println("✓ Pacientes y historial creados para Dra. Laura Martínez");
				}
			}
		} catch (Exception e) {
			System.out.println("⚠ Error creando datos de prueba: " + e.getMessage());
		}
	}

	/**
	 * Método auxiliar para crear un paciente y su historial médico si no existen
	 */
	private void crearHistorialParaPaciente(Medico medico, String email, String nombre,
			LocalDate fechaNacimiento, String telefono, String direccion,
			String diagnostico, String tratamiento, String notas, LocalDate fechaHistorial) {
		try {
			// Buscar o crear paciente
			Usuario usuarioExistente = usuarioRepository.findByEmail(email);
			final Long pacienteId;

			if (usuarioExistente == null) {
				Paciente nuevoPaciente = new Paciente();
				nuevoPaciente.setNombre(nombre);
				nuevoPaciente.setEmail(email);
				nuevoPaciente.setContrasena(passwordEncoder.encode("paciente123"));
				nuevoPaciente.setRol(Roles.PACIENTE);
				nuevoPaciente.setFechaNacimiento(fechaNacimiento);
				nuevoPaciente.setTelefono(telefono);
				nuevoPaciente.setDireccion(direccion);
				Paciente pacienteGuardado = pacienteRepository.save(nuevoPaciente);
				pacienteId = pacienteGuardado.getId();
			} else {
				pacienteId = usuarioExistente.getId();
			}

			// Crear historial si no existe
			final Long finalPacienteId = pacienteId;
			boolean historialExiste = historialRepository.findAll().stream()
				.anyMatch(h -> h.getPaciente() != null && h.getPaciente().getId().equals(finalPacienteId)
					&& h.getMedico() != null && h.getMedico().getId().equals(medico.getId())
					&& diagnostico.equals(h.getDiagnostico()));

			if (!historialExiste) {
				Paciente paciente = pacienteRepository.findById(pacienteId).orElse(null);
				if (paciente != null) {
					HistorialClinico historial = new HistorialClinico();
					historial.setPaciente(paciente);
					historial.setMedico(medico);
					historial.setDiagnostico(diagnostico);
					historial.setTratamiento(tratamiento);
					historial.setNotas(notas);
					historial.setFecha(fechaHistorial);
					historialRepository.save(historial);
				}
			}
		} catch (Exception e) {
			System.out.println("⚠ Error creando historial para " + email + ": " + e.getMessage());
		}
	}

	/**
		* Tarea programada que se ejecuta todos los días a las 7:00 AM
		* para crear notificaciones de citas del día
		*/
	@Scheduled(cron = "0 0 7 * * *") // Todos los días a las 7:00 AM
	public void crearNotificacionesDiarias() {
		try {
			System.out.println("🔔 Creando notificaciones para citas de hoy...");
			notificationService.crearNotificacionesParaHoy();
			System.out.println("✅ Notificaciones creadas exitosamente");
		} catch (Exception e) {
			System.out.println("❌ Error creando notificaciones diarias: " + e.getMessage());
		}
	}
}
