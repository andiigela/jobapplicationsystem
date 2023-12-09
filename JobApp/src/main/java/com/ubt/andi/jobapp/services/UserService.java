package com.ubt.andi.jobapp.services;

import com.ubt.andi.jobapp.dto.UserDto;
import com.ubt.andi.jobapp.models.AppUser;

public interface UserService {
    void createUser(UserDto userDto);
    void updateUser(UserDto userDto);
    AppUser findUserByUsername(String username);
}
