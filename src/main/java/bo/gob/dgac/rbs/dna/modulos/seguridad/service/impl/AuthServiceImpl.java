package bo.gob.dgac.rbs.dna.modulos.seguridad.service.impl;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import bo.gob.dgac.rbs.dna.common.exception.ResourceNotFoundException;
import bo.gob.dgac.rbs.dna.config.security.JwtTokenProvider;
import bo.gob.dgac.rbs.dna.modulos.seguridad.dto.LoginRequestDto;
import bo.gob.dgac.rbs.dna.modulos.seguridad.dto.LoginResponseDto;
import bo.gob.dgac.rbs.dna.modulos.seguridad.dto.RefreshTokenRequestDto;
import bo.gob.dgac.rbs.dna.modulos.seguridad.dto.RolResponseDto;
import bo.gob.dgac.rbs.dna.modulos.seguridad.model.Persona;
import bo.gob.dgac.rbs.dna.modulos.seguridad.model.SesionToken;
import bo.gob.dgac.rbs.dna.modulos.seguridad.model.Usuario;
import bo.gob.dgac.rbs.dna.modulos.seguridad.repository.SesionTokenRepository;
import bo.gob.dgac.rbs.dna.modulos.seguridad.repository.UsuarioRepository;
import bo.gob.dgac.rbs.dna.modulos.seguridad.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UsuarioRepository usuarioRepository;
    private final SesionTokenRepository sesionTokenRepository;
    private final JwtTokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final HttpServletRequest httpRequest; // Para obtener la IP de origen

    @Override
    @Transactional
    public LoginResponseDto login(LoginRequestDto request) {
        // 1. Validar usuario
        Usuario usuario = usuarioRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("Credenciales inválidas"));

        if (Boolean.TRUE.equals(usuario.getBloqueado())) {
            throw new IllegalStateException("El usuario se encuentra bloqueado");
        }

        if (!passwordEncoder.matches(request.getPassword(), usuario.getPasswordHash())) {
            throw new IllegalArgumentException("Credenciales inválidas");
        }

        // 2. Actualizar último login
        usuario.setUltimoLogin(OffsetDateTime.now());
        usuarioRepository.save(usuario);

        // 3. Obtener roles
     // ... paso 3: obtener roles activos
        List<RolResponseDto> roles = usuarioRepository.findRolesActivosByUsuarioId(usuario.getId());
        
        
        List<String> codigosRoles = roles.stream().map(RolResponseDto::getCodigo).collect(Collectors.toList());


        // paso 4: generar tokens pasando directamente la lista de objetos RolResponseDto
        String accessToken = tokenProvider.generarToken(usuario.getUsername(), usuario.getId(), roles);
        String refreshToken = tokenProvider.generarRefreshToken();
        
        
        

        // 5. REGISTRAR EN LA TABLA sesiones_tokens
        SesionToken sesion = SesionToken.builder()
                .usuario(usuario)
                .refreshToken(refreshToken)
                .ipOrigen(obtenerClientIp())
                .expiraEn(OffsetDateTime.now().plusDays(7)) // Expira en 7 días
                .revocado(false)
                .build();
        
        sesionTokenRepository.save(sesion);

        // 6. Construir respuesta
        Persona persona = usuario.getPersona();
        String nombreCompleto = String.format("%s %s %s",
                persona.getNombres(),
                persona.getPrimerApellido(),
                persona.getSegundoApellido() != null ? persona.getSegundoApellido() : "").trim();

        return LoginResponseDto.builder()
                .id(usuario.getId())
                .username(usuario.getUsername())
                .nombreCompleto(nombreCompleto)
                .roles(roles)
                .token(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    private String obtenerClientIp() {
        String remoteAddr = httpRequest.getHeader("X-Forwarded-For");
        if (remoteAddr == null || remoteAddr.isEmpty()) {
            remoteAddr = httpRequest.getRemoteAddr();
        }
        return remoteAddr;
    }
    
    
    @Override
    @Transactional
    public LoginResponseDto refreshToken(RefreshTokenRequestDto request) {
        // 1. Buscar el refreshToken en la tabla sesiones_tokens
        SesionToken sesion = sesionTokenRepository.findByRefreshTokenAndRevocadoFalse(request.getRefreshToken())
                .orElseThrow(() -> new ResourceNotFoundException("El Refresh Token es inválido o ha sido revocado"));

        // 2. Verificar si la sesión ya expiró
        if (sesion.getExpiraEn().isBefore(OffsetDateTime.now())) {
            sesion.setRevocado(true); // Se revoca la sesión vencida
            sesionTokenRepository.save(sesion);
            throw new IllegalStateException("La sesión ha expirado. Inicie sesión nuevamente.");
        }

        Usuario usuario = sesion.getUsuario();

        // 3. Obtener los roles activos del usuario
        List<RolResponseDto> roles = usuarioRepository.findRolesActivosByUsuarioId(usuario.getId());

        // 4. Generar un NUEVO Access Token
        String nuevoAccessToken = tokenProvider.generarToken(usuario.getUsername(), usuario.getId(), roles);

        // 5. Construir nombre completo
        Persona persona = usuario.getPersona();
        String nombreCompleto = String.format("%s %s %s",
                persona.getNombres(),
                persona.getPrimerApellido(),
                persona.getSegundoApellido() != null ? persona.getSegundoApellido() : "").trim();

        return LoginResponseDto.builder()
                .id(usuario.getId())
                .username(usuario.getUsername())
                .nombreCompleto(nombreCompleto)
                .roles(roles)
                .token(nuevoAccessToken)
                .refreshToken(sesion.getRefreshToken()) // Conserva o rota el refreshToken
                .build();
    }
    
    
    
    
}

