package kozelek.authservice.service;

import kozelek.authservice.model.User;

import java.util.Optional;

public interface UserService {
    Optional<User> findByEmail(String email);
}
