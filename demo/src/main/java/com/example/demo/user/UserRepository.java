package com.example.demo.user;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@RepositoryRestResource(collectionResourceRel="apiUsers", path="apiUsers",excerptProjection =UserOnlyContainsNickname.class)
public interface UserRepository extends CrudRepository<User,Integer>{
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	User findByNickname(@Param(value = "nickname") String nickname);
	
}
