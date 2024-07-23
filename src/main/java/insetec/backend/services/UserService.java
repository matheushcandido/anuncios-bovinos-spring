package insetec.backend.services;

import insetec.backend.enums.UserStatus;
import insetec.backend.models.Address;
import insetec.backend.models.Contact;
import insetec.backend.models.User;
import insetec.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    public User createUser(User user) {
        if (repository.findByLogin(user.getLogin()) != null) {
            throw new IllegalArgumentException("User already exists");
        }

        String encryptedPassword = new BCryptPasswordEncoder().encode(user.getPassword());
        user.setPassword(encryptedPassword);

        return repository.save(user);
    }

    public List<User> getAllUsers() {
        return repository.findAll();
    }

    public Optional<User> getUserById(String id) {
        return repository.findById(id);
    }

    public User updateUser(String id, User updatedUser) {
        Optional<User> optionalUser = repository.findById(id);
        if (optionalUser.isEmpty()) {
            throw new IllegalArgumentException("User not found");
        }

        User existingUser = optionalUser.get();
        updateBasicUserInfo(existingUser, updatedUser);
        updateAddress(existingUser, updatedUser);
        updateContact(existingUser, updatedUser);

        return repository.save(existingUser);
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

    public void deleteUser(String id) {
        Optional<User> optionalUser = repository.findById(id);
        if (optionalUser.isPresent()) {
            repository.deleteById(id);
        } else {
            throw new IllegalArgumentException("User not found");
        }
    }

    public Optional<User> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String login = authentication.getName();

        return repository.getByLogin(login);
    }

    public User blockUser(String id) {
        Optional<User> optionalUser = repository.findById(id);
        if (optionalUser.isPresent()) {
            User existingUser = optionalUser.get();
            existingUser.setStatus(UserStatus.BLOCKED);

            return repository.save(existingUser);
        } else {
            throw new IllegalArgumentException("User not found");
        }
    }

    public User unlockUser(String id) {
        Optional<User> optionalUser = repository.findById(id);
        if (optionalUser.isPresent()) {
            User existingUser = optionalUser.get();
            existingUser.setStatus(UserStatus.ACTIVE);

            return repository.save(existingUser);
        } else {
            throw new IllegalArgumentException("User not found");
        }
    }
}
