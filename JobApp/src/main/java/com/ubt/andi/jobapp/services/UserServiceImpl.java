package com.ubt.andi.jobapp.services;
import com.ubt.andi.jobapp.dto.UserDto;
import com.ubt.andi.jobapp.models.AppUser;
import com.ubt.andi.jobapp.models.Role;
import com.ubt.andi.jobapp.repositories.RoleRepository;
import com.ubt.andi.jobapp.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    public UserServiceImpl(UserRepository userRepository,RoleRepository roleRepository,PasswordEncoder passwordEncoder){
        this.userRepository=userRepository;
        this.roleRepository=roleRepository;
        this.passwordEncoder=passwordEncoder;
    }
    @Override
    public void createUser(UserDto userDto) {
        if(userDto == null) return;

        AppUser appUser = mapToUser(userDto);
        if(appUser == null) return;

        Role jobSeeker = roleRepository.findRoleByName("Jobseeker");
        Role employer = roleRepository.findRoleByName("Employer");

        if(!appUser.getRoles().contains(jobSeeker) || !appUser.getRoles().contains(employer)){
            if(appUser.getRoleAccount()){
                appUser.getRoles().add(jobSeeker);
            } else {
                appUser.getRoles().add(employer);
            }
        }
        userRepository.save(appUser);
    }
    public AppUser mapToUser(UserDto userDto){
        AppUser appUser = new AppUser();
        appUser.setUsername(userDto.getUsername());
        appUser.setPassword(userDto.getPassword());
        appUser.setEmail(userDto.getEmail());
        appUser.setRoleAccount(userDto.getRoleAccount());
        return appUser;
    }

    @Override
    public AppUser findUserByUsername(String username) {
        if(username.trim().equals("") || username == null) return null;
        return userRepository.findAppUserByUsername(username);
    }
}
