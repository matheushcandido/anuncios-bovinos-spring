package insetec.backend.services;

import insetec.backend.DTOs.AuthenticationDTO;
import insetec.backend.DTOs.LoginResponseDTO;
import insetec.backend.DTOs.RegisterDTO;
import insetec.backend.models.User;
import insetec.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository repository;

    @Autowired
    private TokenService tokenService;

    public LoginResponseDTO login(AuthenticationDTO data) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.login(), data.password());
        var auth = this.authenticationManager.authenticate(usernamePassword);
        var token = tokenService.generateToken((User) auth.getPrincipal());
        return new LoginResponseDTO(token);
    }

    public void register(RegisterDTO data) {
        if (this.repository.findByLogin(data.login()) != null) {
            throw new IllegalArgumentException("User already exists");
        }

        String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());
        User newUser = new User(data.login(), encryptedPassword, data.role(), data.name());
        this.repository.save(newUser);
    }
}