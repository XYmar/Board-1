package com.rengu.project.board.Repository;

import com.rengu.project.board.Entity.LayoutDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @program: board
 * @author: hanch
 * @create: 2018-11-08 19:17
 **/

@Repository
public interface LayoutDetailRepository extends JpaRepository<LayoutDetailEntity, String> {
}
