package com.railopt.ai.repository.user;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.railopt.ai.entity.user.User;

/** Repository interface for {@link User}. */
@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
}
