package insetec.backend.DTOs;

import insetec.backend.enums.UserRole;

public record RegisterDTO(String login, String password, UserRole role) {
}
