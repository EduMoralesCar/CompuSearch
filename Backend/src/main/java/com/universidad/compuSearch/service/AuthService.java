package com.universidad.compuSearch.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.universidad.compuSearch.entity.TipoUsuario;
import com.universidad.compuSearch.entity.Usuario;
import com.universidad.compuSearch.exception.AuthException;
import com.universidad.compuSearch.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final LoginAttemptService loginAttemptService;
    private final JwtService jwtService;

    public Usuario authenticate(String email, String password) {
        if (loginAttemptService.isBlocked(email)) {
            throw new AuthException("Usuario bloqueado temporalmente por múltiples intentos fallidos");
        }

        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new AuthException("Usuario no encontrado"));

        if (!passwordEncoder.matches(password, usuario.getContrasena())) {
            loginAttemptService.loginFailed(email);
            throw new AuthException("Contraseña incorrecta");
        }

        loginAttemptService.loginSucceeded(email);
        return usuario;
    }

    public Usuario register(String email, String contrasena, TipoUsuario tipoUsuario) {
        if (usuarioRepository.findByEmail(email).isPresent()) {
            throw new AuthException("El email ya está registrado");
        }

        Usuario usuario = new Usuario();
        usuario.setEmail(email);
        usuario.setContrasena(passwordEncoder.encode(contrasena));
        usuario.setTipoUsuario(tipoUsuario);

        return usuarioRepository.save(usuario);
    }

    public Usuario findByEmail(String email) {
        return usuarioRepository.findByEmail(email).orElse(null);
    }

    public Usuario updatePassword(Usuario usuario, String nuevaContrasena) {
        usuario.setContrasena(passwordEncoder.encode(nuevaContrasena));
        return usuarioRepository.save(usuario);
    }

    public String generateJwtToken(Usuario usuario) {
        return jwtService.generateAccessToken(usuario);
    }

    public String generateRefreshToken(Usuario usuario) {
        return jwtService.generateRefreshToken(usuario);
    }
}
