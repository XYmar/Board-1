package com.rengu.project.board.Repository;

import com.rengu.project.board.Entity.BoardEntity;
import com.rengu.project.board.Entity.LayoutEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @program: board
 * @author: hanch
 * @create: 2018-11-08 14:52
 **/

@Repository
public interface LayoutRepository extends JpaRepository<LayoutEntity, String> {

    Optional<LayoutEntity> findAllByBoardEntity(BoardEntity boardEntity);

    boolean existsAllByBoardEntity(BoardEntity boardEntity);
}
