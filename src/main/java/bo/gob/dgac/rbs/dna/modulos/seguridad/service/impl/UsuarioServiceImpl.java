package bo.gob.dgac.rbs.dna.modulos.seguridad.service.impl;


import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import bo.gob.dgac.rbs.dna.modulos.seguridad.dto.CambiarPasswordRequestDto;
import bo.gob.dgac.rbs.dna.modulos.seguridad.dto.UsuarioPersonaResponseDto;
import bo.gob.dgac.rbs.dna.modulos.seguridad.model.Usuario;
import bo.gob.dgac.rbs.dna.modulos.seguridad.repository.UsuarioRepository;
import bo.gob.dgac.rbs.dna.modulos.seguridad.service.UsuarioService;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void cambiarPassword(String username, CambiarPasswordRequestDto dto) {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + username));

        // 1. Validar que la contraseña actual enviada coincida con el hash almacenado
        if (!passwordEncoder.matches(dto.getPasswordActual(), usuario.getPasswordHash())) {
            throw new IllegalArgumentException("La contraseña actual es incorrecta");
        }

        // 2. Encriptar y actualizar con la nueva contraseña
        String passwordEncriptada = passwordEncoder.encode(dto.getPasswordNueva());
        usuario.setPasswordHash(passwordEncriptada);
        usuarioRepository.save(usuario);
    }

	
	@Override
    @Transactional(readOnly = true)
    public List<UsuarioPersonaResponseDto> obtenerUsuariosPorRolEspecialidadArea(Long rolId,Long idEspecialidad, Long idArea, Long idUnidad) {
        return usuarioRepository.obtenerUsuariosPorRolEspecialidadArea(rolId,idEspecialidad,idArea, idUnidad);
    }
	
	
	
	@Override
	@Transactional
	public void cambiarEstadoUsuario(Long usuarioId, Boolean bloqueado) {
	    Usuario usuario = usuarioRepository.findById(usuarioId)
	            .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + usuarioId));

	    usuario.setBloqueado(bloqueado);
	    usuarioRepository.save(usuario);
	}
	
}