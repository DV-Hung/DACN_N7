package com.haui.bookinghotel.domain.response.user;

import com.haui.bookinghotel.util.constant.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResUserDTO {
    private long id;
    private String userName;
    private String email;
    private String phoneNumber;
    private Instant createAt;
    private Role role;
}
