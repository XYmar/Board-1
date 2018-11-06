package com.rengu.project.board.Repository;

import com.rengu.project.board.Entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, String> {

    boolean existsAllByName(String name);

    Optional<RoleEntity> findAllByName(String name);
}