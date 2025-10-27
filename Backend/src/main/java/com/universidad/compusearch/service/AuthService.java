package com.universidad.compusearch.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.universidad.compusearch.entity.TipoUsuario;
import com.universidad.compusearch.entity.Usuario;
import com.universidad.compusearch.exception.AlreadyRegisteredException;
import com.universidad.compusearch.exception.InvalidPasswordException;
import com.universidad.compusearch.exception.TooManyAttemptsException;
import com.universidad.compusearch.exception.UserException;
import com.universidad.compusearch.jwt.JwtTokenFactory;
import com.universidad.compusearch.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Servicio responsable de gestionar la autenticación y registro de usuarios.
 * 
 * Incluye validación de credenciales, bloqueo temporal ante múltiples intentos
 * fallidos, registro de nuevos usuarios y generación de tokens JWT de acceso y
 * refresco.
 * 
 *
 * Principales funciones:
 * <ul>
 *   <li>Autenticación por <b>email</b> o <b>username</b>.</li>
 *   <li>Registro de nuevos usuarios con cifrado de contraseña.</li>
 *   <li>Prevención de ataques de fuerza bruta mediante {@link LoginAttemptService}.</li>
 *   <li>Generación de tokens JWT con {@link JwtTokenFactory}.</li>
 * </ul>
 *
 * Seguridad:
 * <ul>
 *   <li>Las contraseñas se almacenan cifradas con {@link PasswordEncoder}.</li>
 *   <li>El servicio bloquea temporalmente al usuario tras varios intentos fallidos.</li>
 * </ul>
 *
 * @see UsuarioService
 * @see LoginAttemptService
 * @see JwtTokenFactory
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final LoginAttemptService loginAttemptService;
    private final JwtTokenFactory jwtTokenFactory;

    /**
     * Autentica a un usuario mediante su email o username.
     * <p>
     * Si el usuario excede los intentos permitidos, se bloquea temporalmente.
     * </p>
     *
     * @param identificador email o nombre de usuario
     * @param password       contraseña en texto plano
     * @return {@link Usuario} autenticado
     * @throws TooManyAttemptsException si el usuario está bloqueado
     * @throws UserException            si no se encuentra el usuario
     * @throws InvalidPasswordException si la contraseña es incorrecta
     */
    public Usuario authenticate(String identificador, String password) {
        log.info("Intentando autenticar con identificador: {}", identificador);

        if (loginAttemptService.isBlocked(identificador)) {
            log.warn("Identificador bloqueado por intentos fallidos: {}", identificador);
            throw TooManyAttemptsException.login();
        }

        Usuario usuario = usuarioRepository.findByEmail(identificador)
                .or(() -> usuarioRepository.findByUsername(identificador))
                .orElseThrow(() -> {
                    log.warn("Usuario no encontrado con identificador: {}", identificador);
                    loginAttemptService.fail(identificador);
                    return UserException.notFound();
                });

        if (!usuario.isActivo()) {
            throw UserException.noActive();
        }

        if (!passwordEncoder.matches(password, usuario.getContrasena())) {
            log.warn("Contraseña inválida para identificador: {}", identificador);
            loginAttemptService.fail(identificador);
            throw new InvalidPasswordException();
        }

        loginAttemptService.success(identificador);
        log.info("Autenticación exitosa para usuario ID: {}", usuario.getIdUsuario());
        return usuario;
    }

    /**
     * Registra un nuevo usuario en el sistema.
     *
     * @param username     nombre de usuario único
     * @param email        dirección de correo única
     * @param contrasena   contraseña sin cifrar
     * @param tipoUsuario  tipo de usuario (CLIENTE, ADMIN, etc.)
     * @return {@link Usuario} registrado
     * @throws AlreadyRegisteredException si el email o username ya existen
     */
    public Usuario register(String username, String email, String contrasena, TipoUsuario tipoUsuario) {
        log.info("Registrando nuevo usuario con email: {} y username: {}", email, username);

        if (usuarioRepository.findByEmail(email).isPresent()) {
            log.warn("Email ya registrado: {}", email);
            throw AlreadyRegisteredException.email();
        }

        if (usuarioRepository.findByUsername(username).isPresent()) {
            log.warn("Username ya registrado: {}", username);
            throw AlreadyRegisteredException.username();
        }

        Usuario usuario = new Usuario();
        usuario.setUsername(username);
        usuario.setEmail(email);
        usuario.setContrasena(passwordEncoder.encode(contrasena));
        usuario.setTipoUsuario(tipoUsuario);

        Usuario saved = usuarioRepository.save(usuario);
        log.info("Usuario registrado exitosamente con ID: {}", saved.getIdUsuario());
        return saved;
    }

    /**
     * Busca un usuario por su email.
     *
     * @param email correo del usuario
     * @return {@link Usuario} o {@code null} si no existe
     */
    public Usuario findByEmail(String email) {
        log.debug("Buscando usuario por email: {}", email);
        return usuarioRepository.findByEmail(email).orElse(null);
    }

    /**
     * Busca un usuario por su nombre de usuario.
     *
     * @param username nombre de usuario
     * @return {@link Usuario} o {@code null} si no existe
     */
    public Usuario findByUsername(String username) {
        log.debug("Buscando usuario por username: {}", username);
        return usuarioRepository.findByUsername(username).orElse(null);
    }

    /**
     * Actualiza la contraseña de un usuario.
     *
     * @param usuario         usuario existente
     * @param nuevaContrasena nueva contraseña en texto plano
     * @return {@link Usuario} actualizado
     */
    public Usuario updatePassword(Usuario usuario, String nuevaContrasena) {
        log.info("Actualizando contraseña para usuario ID: {}", usuario.getIdUsuario());
        usuario.setContrasena(passwordEncoder.encode(nuevaContrasena));
        return usuarioRepository.save(usuario);
    }

    /**
     * Genera un token JWT de acceso para un usuario autenticado.
     *
     * @param usuario usuario autenticado
     * @return token JWT de acceso
     */
    public String generateJwtToken(Usuario usuario) {
        log.debug("Generando token de acceso para usuario ID: {}", usuario.getIdUsuario());
        return jwtTokenFactory.generateAccessToken(usuario);
    }

    /**
     * Genera un token JWT de refresco para un usuario autenticado.
     *
     * @param usuario usuario autenticado
     * @return token JWT de refresco
     */
    public String generateRefreshToken(Usuario usuario) {
        log.debug("Generando token de refresco para usuario ID: {}", usuario.getIdUsuario());
        return jwtTokenFactory.generateRefreshToken(usuario);
    }
}
