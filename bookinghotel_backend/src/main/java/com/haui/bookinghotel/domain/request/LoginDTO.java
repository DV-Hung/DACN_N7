package com.haui.bookinghotel.domain.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginDTO {
    @NotBlank(message = "username khong duoc de trong")
    private String username;
    @NotBlank(message = "password khong duoc de trong")
    private String password;

}
