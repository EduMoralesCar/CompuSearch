package com.universidad.compusearch.service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.universidad.compusearch.entity.EstadoToken;
import com.universidad.compusearch.entity.TipoToken;
import com.universidad.compusearch.entity.Token;
import com.universidad.compusearch.entity.Usuario;
import com.universidad.compusearch.exception.TokenException;
import com.universidad.compusearch.jwt.JwtConfigHelper;
import com.universidad.compusearch.repository.TokenRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Servicio para manejar los tokens de reseteo de contraseña.
 * <p>
 * Permite crear, actualizar, validar y revocar tokens de tipo RESET para usuarios.
 * Se asegura que los tokens estén activos y no expirados antes de ser usados.
 * </p>
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ResetTokenService {

    private final TokenRepository tokenRepository;
    private final JwtConfigHelper jwtConfigHelper;

    /**
     * Crea o actualiza un token de reseteo para un usuario en un dispositivo específico.
     * <p>
     * Si ya existe un token válido se devuelve, si está expirado o revocado se actualiza,
     * y si no existe se crea uno nuevo.
     * </p>
     *
     * @param usuario    el usuario asociado al token
     * @param dispositivo identificador del dispositivo
     * @return token de reseteo activo
     */
    public Token createOrUpdateResetToken(Usuario usuario, String dispositivo) {
        log.info("Procesando token de reseteo para usuario {} en dispositivo {}", usuario.getIdUsuario(), dispositivo);

        return findByUsuarioAndDispositivo(usuario, dispositivo)
                .map(existingToken -> {
                    if (existingToken.isExpired() || existingToken.getEstado() == EstadoToken.REVOCADO) {
                        return updateResetToken(existingToken);
                    }
                    return existingToken;
                })
                .orElseGet(() -> {
                    log.info("No se encontró token de reseteo, creando nuevo para usuario {}", usuario.getIdUsuario());
                    return createResetToken(usuario, dispositivo);
                });
    }

    /**
     * Actualiza un token de reseteo existente, renovando su valor y fecha de expiración.
     *
     * @param existingToken token a actualizar
     * @return token actualizado
     */
    private Token updateResetToken(Token existingToken) {
        log.info("Actualizando token de reseteo ID={} para usuario {}", existingToken.getIdToken(), existingToken.getUsuario().getIdUsuario());

        existingToken.setToken(UUID.randomUUID().toString());
        existingToken.setFechaCreacion(Instant.now());
        existingToken.setFechaExpiracion(Instant.now().plusMillis(jwtConfigHelper.getResetTokenExpiration()));
        existingToken.setEstado(EstadoToken.ACTIVO);

        return save(existingToken);
    }

    /**
     * Crea un nuevo token de reseteo para un usuario y dispositivo.
     *
     * @param usuario    usuario asociado
     * @param dispositivo identificador del dispositivo
     * @return token creado
     */
    private Token createResetToken(Usuario usuario, String dispositivo) {
        log.info("Creando nuevo token de reseteo para usuario {} en dispositivo {}", usuario.getIdUsuario(), dispositivo);

        Token resetToken = new Token();
        resetToken.setUsuario(usuario);
        resetToken.setIpDispositivo(dispositivo);
        resetToken.setToken(UUID.randomUUID().toString());
        resetToken.setFechaCreacion(Instant.now());
        resetToken.setFechaExpiracion(Instant.now().plusMillis(jwtConfigHelper.getResetTokenExpiration()));
        resetToken.setTipo(TipoToken.RESET);
        resetToken.setEstado(EstadoToken.ACTIVO);

        return save(resetToken);
    }

    /**
     * Valida que un token de reseteo esté activo y no haya expirado.
     *
     * @param token token recibido
     * @return token válido
     * @throws TokenException si el token es inválido o expirado
     */
    public Token validateAndGetResetToken(String token) {
        log.debug("Validando token de reseteo: {}", token);

        return findByToken(token)
                .filter(t -> !t.isExpired() && t.getEstado() == EstadoToken.ACTIVO)
                .orElseThrow(() -> TokenException.invalid("Reset"));
    }

    /**
     * Revoca un token de reseteo, impidiendo su uso futuro.
     *
     * @param token token a revocar
     * @throws TokenException si el token no existe
     */
    public void revokeResetToken(String token) {
        log.warn("Revocando token de reseteo: {}", token);

        Token resetToken = findByToken(token)
                .orElseThrow(() -> TokenException.invalid("Reset"));

        resetToken.setEstado(EstadoToken.REVOCADO);
        save(resetToken);
    }

    /**
     * Guarda un token en la base de datos.
     *
     * @param token token a guardar
     * @return token guardado
     */
    public Token save(Token token) {
        log.debug("Guardando token de reseteo ID={}", token.getIdToken());
        return tokenRepository.save(token);
    }

    /**
     * Busca un token de reseteo por su valor.
     *
     * @param token valor del token
     * @return token encontrado
     */
    public Optional<Token> findByToken(String token) {
        log.debug("Buscando token de reseteo: {}", token);
        return tokenRepository.findByTokenAndTipo(token, TipoToken.RESET);
    }

    /**
     * Busca un token de reseteo por usuario, dispositivo y tipo.
     *
     * @param usuario    usuario asociado
     * @param dispositivo dispositivo asociado
     * @return token encontrado
     */
    public Optional<Token> findByUsuarioAndDispositivo(Usuario usuario, String dispositivo) {
        log.debug("Buscando token de reseteo para usuario {} en dispositivo {}", usuario.getIdUsuario(), dispositivo);
        return tokenRepository.findByUsuario_IdUsuarioAndIpDispositivoAndTipo(usuario.getIdUsuario(), dispositivo, TipoToken.RESET);
    }
}
