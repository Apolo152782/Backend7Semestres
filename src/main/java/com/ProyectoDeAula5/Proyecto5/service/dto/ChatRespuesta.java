package com.ProyectoDeAula5.Proyecto5.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChatRespuesta {
    private Long conversacionId;
    private String respuesta;
    private boolean nuevaConversacion;
}