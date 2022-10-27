package com.example.demo.user;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel="apiUsers", path="apiUsers")
public interface UserRepository extends CrudRepository<User,Integer>{

	User findByNickname(@Param(value = "nickname") String nickname);
	
}
