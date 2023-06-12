package com.test.empik.payload.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserDetailsResponse {
    private String id;
    private String login;
    private String name;
    private String type;
    private String avatarUrl;
    private String createdAt;
    private String calculations;
}
