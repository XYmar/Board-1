package com.rengu.project.board.Repository;

import com.rengu.project.board.Entity.BoardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BoardRepository extends JpaRepository<BoardEntity, String> {

    boolean existsAllByIp(String IP);

    Optional<BoardEntity> findAllByIp(String boardIP);
}
