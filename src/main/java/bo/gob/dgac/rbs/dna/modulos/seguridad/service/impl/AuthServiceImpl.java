package bo.gob.dgac.rbs.dna.modulos.seguridad.service.impl;

import bo.gob.dgac.rbs.dna.common.exception.ResourceNotFoundException;
import bo.gob.dgac.rbs.dna.modulos.seguridad.dto.LoginRequestDto;
import bo.gob.dgac.rbs.dna.modulos.seguridad.dto.LoginResponseDto;
import bo.gob.dgac.rbs.dna.modulos.seguridad.dto.RolResponseDto;
import bo.gob.dgac.rbs.dna.modulos.seguridad.model.Persona;
import bo.gob.dgac.rbs.dna.modulos.seguridad.model.Usuario;
import bo.gob.dgac.rbs.dna.modulos.seguridad.repository.UsuarioRepository;
import bo.gob.dgac.rbs.dna.modulos.seguridad.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UsuarioRepository usuarioRepository;

    @Override
    @Transactional
    public LoginResponseDto login(LoginRequestDto request) {
        // 1. Buscar usuario por username
        Usuario usuario = usuarioRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("Credenciales inválidas"));

        // 2. Validar estado de bloqueo
        if (Boolean.TRUE.equals(usuario.getBloqueado())) {
            throw new IllegalStateException("El usuario se encuentra bloqueado");
        }

        // 3. Validar contraseña
        if (!validarPassword(request.getPassword(), usuario.getPasswordHash())) {
            throw new IllegalArgumentException("Credenciales inválidas");
        }

        // 4. Actualizar fecha de último login
        usuario.setUltimoLogin(OffsetDateTime.now());
        usuarioRepository.save(usuario);

        // 5. Obtener roles activos con su ID y Código
        List<RolResponseDto> roles = usuarioRepository.findRolesActivosByUsuarioId(usuario.getId());

        // 6. Construir nombre completo
        Persona persona = usuario.getPersona();
        String nombreCompleto = String.format("%s %s %s",
                persona.getNombres(),
                persona.getPrimerApellido(),
                persona.getSegundoApellido() != null ? persona.getSegundoApellido() : "").trim();

        // 7. Retornar respuesta
        return LoginResponseDto.builder()
                .id(usuario.getId())
                .username(usuario.getUsername())
                .nombreCompleto(nombreCompleto)
                .roles(roles)
                .build();
    }

    private boolean validarPassword(String passwordPlana, String passwordHash) {
        return passwordPlana.equals(passwordHash); 
    }
}

/*
package bo.gob.dgac.rbs.dna.modulos.seguridad.service.impl;

import bo.gob.dgac.rbs.dna.common.exception.ResourceNotFoundException;
import bo.gob.dgac.rbs.dna.modulos.seguridad.dto.LoginRequestDto;
import bo.gob.dgac.rbs.dna.modulos.seguridad.dto.LoginResponseDto;
import bo.gob.dgac.rbs.dna.modulos.seguridad.model.Persona;
import bo.gob.dgac.rbs.dna.modulos.seguridad.model.Usuario;
import bo.gob.dgac.rbs.dna.modulos.seguridad.repository.UsuarioRepository;
import bo.gob.dgac.rbs.dna.modulos.seguridad.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UsuarioRepository usuarioRepository;

    @Override
    @Transactional
    public LoginResponseDto login(LoginRequestDto request) {
        // 1. Buscar usuario por username
        Usuario usuario = usuarioRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("Credenciales inválidas"));

        // 2. Validar estado de bloqueo
        if (Boolean.TRUE.equals(usuario.getBloqueado())) {
            throw new IllegalStateException("El usuario se encuentra bloqueado");
        }

        // 3. Validar contraseña (Ajustar según la estrategia de Hash/BCrypt utilizada)
        if (!validarPassword(request.getPassword(), usuario.getPasswordHash())) {
            throw new IllegalArgumentException("Credenciales inválidas");
        }

        // 4. Actualizar fecha de último login
        usuario.setUltimoLogin(OffsetDateTime.now());
        usuarioRepository.save(usuario);

        // 5. Obtener roles activos
        List<String> roles = usuarioRepository.findCodigosRolesActivosByUsuarioId(usuario.getId());

        // 6. Construir nombre completo desde la entidad Persona
        Persona persona = usuario.getPersona();
        String nombreCompleto = String.format("%s %s %s",
                persona.getNombres(),
                persona.getPrimerApellido(),
                persona.getSegundoApellido() != null ? persona.getSegundoApellido() : "").trim();

        // 7. Retornar respuesta
        return LoginResponseDto.builder()
                .id(usuario.getId())
                .username(usuario.getUsername())
                .nombreCompleto(nombreCompleto)
                .roles(roles)
                .build();
    }

    private boolean validarPassword(String passwordPlana, String passwordHash) {
        // TODO: Integrar con PasswordEncoder (ej. BCryptPasswordEncoder)
        return passwordPlana.equals(passwordHash); 
    }
}*/