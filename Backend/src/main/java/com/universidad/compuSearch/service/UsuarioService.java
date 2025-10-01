package com.universidad.compuSearch.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.universidad.compuSearch.entity.Usuario;
import com.universidad.compuSearch.exception.UserException;
import com.universidad.compuSearch.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UsuarioService implements UserDetailsService{
    
    private final UsuarioRepository usuarioRepository;

    //Sobre escribiendo metodo de UserDetailServie
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        // Busca al usuario por email
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> UserException.notFoundEmail());

        // Si encuentra al usuario lo devuelve
        return usuario;
    }
}
