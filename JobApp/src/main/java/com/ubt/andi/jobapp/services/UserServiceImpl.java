package com.ubt.andi.jobapp.services;
import com.ubt.andi.jobapp.dto.UserDto;
import com.ubt.andi.jobapp.models.AppUser;
import com.ubt.andi.jobapp.models.Role;
import com.ubt.andi.jobapp.repositories.RoleRepository;
import com.ubt.andi.jobapp.repositories.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    public UserServiceImpl(UserRepository userRepository,RoleRepository roleRepository){
        this.userRepository=userRepository;
        this.roleRepository=roleRepository;
    }
    @Override
    public void createUser(UserDto userDto) {
        if(userDto == null) return;
        AppUser user = mapToUser(userDto);
        if(user.getRoleAccount()){
            Role jobSeeker = roleRepository.findRoleByName("Jobseeker");
            user.getRoles().add(jobSeeker);
        } else {
            Role employer = roleRepository.findRoleByName("Employer");
            user.getRoles().add(employer);
        }
        userRepository.save(user);
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
