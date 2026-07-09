package com.example.playlistmanager.service;

import com.example.playlistmanager.model.AppUser;
import com.example.playlistmanager.repository.AppUserRepository;
import org.springframework.stereotype.Service;

@Service
public class AppUserService {

    private final AppUserRepository appUserRepository;

    public AppUserService(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    public AppUser authenticate(String username, String password) {
        if (username == null || password == null) {
            return null;
        }
        return appUserRepository.findByUsernameIgnoreCase(username.trim())
                .filter(user -> password.equals(user.getPassword()))
                .orElse(null);
    }

    public void createDemoUserIfMissing() {
        appUserRepository.findByUsernameIgnoreCase("demo").orElseGet(() -> {
            AppUser user = new AppUser();
            user.setUsername("demo");
            user.setPassword("1234");
            user.setDisplayName("Demo Kullanıcı");
            return appUserRepository.save(user);
        });
    }
}
