package com.ProyectoDeAula5.Proyecto5.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ProyectoDeAula5.Proyecto5.model.Usuario;
import com.ProyectoDeAula5.Proyecto5.repository.UsuarioRepository;

import java.util.Optional;
import java.util.List;

@Service
public class UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    public Optional<Usuario> login(String correo, String pass) {
        return usuarioRepository.findByCorreoAndPass(correo, pass);
    }

    public List<Usuario> getAllUsuarios() {
        return usuarioRepository.findAll();
    }

    public Usuario saveUsuario(Usuario usuario) {
        return usuarioRepository.save(usuario);

    }
}
