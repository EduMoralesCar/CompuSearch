package com.universidad.compuSearch.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.universidad.compuSearch.entity.TipoUsuario;
import com.universidad.compuSearch.entity.Usuario;
import com.universidad.compuSearch.exception.EmailAlreadyRegisteredException;
import com.universidad.compuSearch.exception.InvalidPasswordException;
import com.universidad.compuSearch.exception.UserException;
import com.universidad.compuSearch.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final LoginAttemptService loginAttemptService;
    private final JwtService jwtService;

    // Autentica al usuario
    public Usuario authenticate(String email, String password) {
        
        // Si el usuario tiene muchos intentos envio un error
        if (loginAttemptService.isBlocked(email)) {
            throw UserException.blocked();
        }

        // Busca el usuario en la base de datos por su email
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> UserException.notFound());

        // Valida la constraseña ingresada con la que esta en la base de datos
        if (!passwordEncoder.matches(password, usuario.getContrasena())) {
            loginAttemptService.loginFailed(email);
            throw new InvalidPasswordException();
        }

        // Si la autenticacion fue exitosa borra los intentos del cache
        loginAttemptService.loginSucceeded(email);

        // Retorna al usuario
        return usuario;
    }

    // Registra al usuario
    public Usuario register(String email, String contrasena, TipoUsuario tipoUsuario) {

        // Valida si el email del usuario ya esta registrado
        if (usuarioRepository.findByEmail(email).isPresent()) {
            throw new EmailAlreadyRegisteredException();
        }

        // Creamos un usuario y le asignamos sus datos
        Usuario usuario = new Usuario();
        usuario.setEmail(email);
        usuario.setContrasena(passwordEncoder.encode(contrasena));
        usuario.setTipoUsuario(tipoUsuario);

        // Guardamos al usuario en la base de datos
        return usuarioRepository.save(usuario);
    }

    // Implementamos el metodo del respositorio usuario
    public Usuario findByEmail(String email) {
        return usuarioRepository.findByEmail(email).orElse(null);
    }

    // Actualizamos la contraseña del usuario
    public Usuario updatePassword(Usuario usuario, String nuevaContrasena) {
        usuario.setContrasena(passwordEncoder.encode(nuevaContrasena));
        return usuarioRepository.save(usuario);
    }

    // Generar un token del usuario
    public String generateJwtToken(Usuario usuario) {
        return jwtService.generateAccessToken(usuario);
    }

    // Generar un token de refresco del usuario
    public String generateRefreshToken(Usuario usuario) {
        return jwtService.generateRefreshToken(usuario);
    }
}
