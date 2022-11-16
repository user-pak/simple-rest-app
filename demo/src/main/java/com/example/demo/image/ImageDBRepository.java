package com.example.demo.image;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel="apiImages", path="apiImages")
public interface ImageDBRepository extends JpaRepository<Image, Long>{

}
