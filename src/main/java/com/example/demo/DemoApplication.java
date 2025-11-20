package com.example.demo;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
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
import com.example.demo.repository.UsuarioRepository;

@SpringBootApplication
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
					// Crear pacientes para Laura
					Paciente paciente1 = new Paciente();
					paciente1.setNombre("Carlos Rodríguez");
					paciente1.setEmail("carlos.rodriguez@email.com");
					paciente1.setContrasena(passwordEncoder.encode("paciente123"));
					paciente1.setRol(Roles.PACIENTE);
					paciente1.setFechaNacimiento(LocalDate.of(1985, 3, 20));
					paciente1.setTelefono("555-0456");
					paciente1.setDireccion("Avenida Central 456");
					pacienteRepository.save(paciente1);

					Paciente paciente2 = new Paciente();
					paciente2.setNombre("Ana López");
					paciente2.setEmail("ana.lopez@email.com");
					paciente2.setContrasena(passwordEncoder.encode("paciente123"));
					paciente2.setRol(Roles.PACIENTE);
					paciente2.setFechaNacimiento(LocalDate.of(1992, 7, 15));
					paciente2.setTelefono("555-0789");
					paciente2.setDireccion("Plaza Mayor 789");
					pacienteRepository.save(paciente2);

					// Crear historial clínico para estos pacientes con Laura
					HistorialClinico historial1 = new HistorialClinico();
					historial1.setPaciente(paciente1);
					historial1.setMedico(laura);
					historial1.setDiagnostico("Hipertensión arterial");
					historial1.setTratamiento("Medicación antihipertensiva, dieta baja en sal");
					historial1.setNotas("Paciente con buen control de la presión arterial");
					historial1.setFecha(LocalDate.now().minusDays(30));
					historialRepository.save(historial1);

					HistorialClinico historial2 = new HistorialClinico();
					historial2.setPaciente(paciente2);
					historial2.setMedico(laura);
					historial2.setDiagnostico("Arritmia cardíaca");
					historial2.setTratamiento("Medicación antiarrítmica, monitoreo continuo");
					historial2.setNotas("Paciente requiere seguimiento mensual");
					historial2.setFecha(LocalDate.now().minusDays(15));
					historialRepository.save(historial2);

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
}
