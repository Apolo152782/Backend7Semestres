package com.ProyectoDeAula5.Proyecto5.service.dto;

import lombok.Data;

@Data
public class ChatPeticion {
    private Long conversacionId; // null si es nueva
    private String usuarioId;
    private String mensaje;
}