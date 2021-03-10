package com.dembla.spring.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    boolean existsByUserName(String userName) ;

    User findByUserName(String userName) ;

    @Transactional
    void deleteByUserName(String userName) ;
}
