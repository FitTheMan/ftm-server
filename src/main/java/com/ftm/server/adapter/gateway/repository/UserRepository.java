package com.ftm.server.adapter.gateway.repository;

import com.ftm.server.entity.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {}
