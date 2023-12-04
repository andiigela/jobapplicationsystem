package com.ubt.andi.jobapp.services;
import com.ubt.andi.jobapp.dto.UserDto;
import com.ubt.andi.jobapp.models.AppUser;
import com.ubt.andi.jobapp.repositories.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    public UserServiceImpl(UserRepository userRepository){
        this.userRepository=userRepository;
    }
    @Override
    public void createUser(UserDto userDto) {
        if(userDto == null) return;
        AppUser user = mapToUser(userDto);
        if(user.getRoleAccount()){

        }
        userRepository.save(user);
    }
    public AppUser mapToUser(UserDto userDto){
        AppUser appUser = new AppUser();
        appUser.setUsername(userDto.getEmail());
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
