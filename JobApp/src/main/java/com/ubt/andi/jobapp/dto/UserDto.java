package com.ubt.andi.jobapp.dto;
import lombok.Data;

@Data
public class UserDto {
    private String username;
    private String email;
    private String password;
    private Boolean roleAccount;

}
