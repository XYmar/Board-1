package com.rengu.project.board.Repository;

import com.rengu.project.board.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> {

    Optional<UserEntity> findAllByUsername(String username);

    boolean existsAllByUsername(String username);
}
