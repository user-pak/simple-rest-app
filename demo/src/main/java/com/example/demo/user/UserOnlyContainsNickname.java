package com.example.demo.user;

import org.springframework.data.rest.core.config.Projection;

@Projection(types = { User.class })
public interface UserOnlyContainsNickname {

	String getNickname();
}
