package insetec.backend.controllers;

import insetec.backend.enums.UserStatus;
import insetec.backend.models.Address;
import insetec.backend.models.Contact;
import insetec.backend.models.User;
import insetec.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserRepository repository;

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        if(this.repository.findByLogin(user.getLogin()) != null) return ResponseEntity.badRequest().build();

        String encryptedPassword = new BCryptPasswordEncoder().encode(user.getPassword());
        user.setPassword(encryptedPassword);

        User savedUser = repository.save(user);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = repository.findAll();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable String id) {
        Optional<User> optionalUser = repository.findById(id);
        return optionalUser.map(user -> new ResponseEntity<>(user, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable String id, @RequestBody User updatedUser) {
        Optional<User> optionalUser = repository.findById(id);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        User existingUser = optionalUser.get();
        updateBasicUserInfo(existingUser, updatedUser);
        updateAddress(existingUser, updatedUser);
        updateContact(existingUser, updatedUser);

        User savedUser = repository.save(existingUser);
        return ResponseEntity.ok(savedUser);
    }

    private void updateBasicUserInfo(User existingUser, User updatedUser) {
        existingUser.setName(updatedUser.getName());
        existingUser.setStatus(updatedUser.getStatus());
    }

    private void updateAddress(User existingUser, User updatedUser) {
        Address updatedAddress = updatedUser.getAddress();
        if (updatedAddress != null) {
            Address existingAddress = existingUser.getAddress();
            if (existingAddress == null) {
                existingAddress = new Address();
                existingAddress.setUser(existingUser);
                existingUser.setAddress(existingAddress);
            }
            existingAddress.setZip(updatedAddress.getZip());
            existingAddress.setStreet(updatedAddress.getStreet());
            existingAddress.setNeighborhood(updatedAddress.getNeighborhood());
            existingAddress.setNumber(updatedAddress.getNumber());
            existingAddress.setCity(updatedAddress.getCity());
            existingAddress.setState(updatedAddress.getState());
        }
    }

    private void updateContact(User existingUser, User updatedUser) {
        Contact updatedContact = updatedUser.getContact();
        if (updatedContact != null) {
            Contact existingContact = existingUser.getContact();
            if (existingContact == null) {
                existingContact = new Contact();
                existingContact.setUser(existingUser);
                existingUser.setContact(existingContact);
            }
            existingContact.setPhoneNumber(updatedContact.getPhoneNumber());
            existingContact.setVerified(false);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        Optional<User> optionalUser = repository.findById(id);
        if (optionalUser.isPresent()) {
            repository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/current")
    @Secured("ROLE_USER")
    public ResponseEntity<User> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String login = authentication.getName();

        Optional<User> optionalUser = repository.getByLogin(login);
        return optionalUser.map(user -> new ResponseEntity<>(user, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/block/{id}")
    public ResponseEntity<User> blockUser(@PathVariable String id) {
        Optional<User> optionalUser = repository.findById(id);
        if (optionalUser.isPresent()) {
            User existingUser = optionalUser.get();
            existingUser.setStatus(UserStatus.BLOCKED);

            User savedUser = repository.save(existingUser);
            return new ResponseEntity<>(savedUser, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/unlock/{id}")
    public ResponseEntity<User> unlockUser(@PathVariable String id) {
        Optional<User> optionalUser = repository.findById(id);
        if (optionalUser.isPresent()) {
            User existingUser = optionalUser.get();
            existingUser.setStatus(UserStatus.ACTIVE);

            User savedUser = repository.save(existingUser);
            return new ResponseEntity<>(savedUser, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
