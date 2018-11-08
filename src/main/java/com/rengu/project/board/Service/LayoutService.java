package com.rengu.project.board.Service;

import com.rengu.project.board.Entity.BoardEntity;
import com.rengu.project.board.Entity.LayoutDetailEntity;
import com.rengu.project.board.Entity.LayoutEntity;
import com.rengu.project.board.Repository.LayoutDetailRepository;
import com.rengu.project.board.Repository.LayoutRepository;
import com.rengu.project.board.Utils.ApplicationMessages;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * @program: board
 * @author: hanch
 * @create: 2018-11-08 14:53
 **/

@Slf4j
@Service
@Transactional
public class LayoutService {

    private final LayoutRepository layoutRepository;
    private final LayoutDetailRepository layoutDetailRepository;

    @Autowired
    public LayoutService(LayoutRepository layoutRepository, LayoutDetailRepository layoutDetailRepository) {
        this.layoutRepository = layoutRepository;
        this.layoutDetailRepository = layoutDetailRepository;
    }

    // 保存看版布局
    public LayoutEntity saveLayoutByBoard(BoardEntity boardEntity, List<LayoutDetailEntity> layoutDetails) {
        LayoutEntity layoutEntity = null;
        if (hasLayoutByBoard(boardEntity)) {
            layoutEntity = getLayoutByBoard(boardEntity);
        } else {
            layoutEntity = new LayoutEntity();
            layoutEntity.setBoardEntity(boardEntity);
        }
        if (layoutDetails.size() == 0) {
            throw new RuntimeException(ApplicationMessages.LAYOUT_DETAIL_ARGS_NOT_FOUND);
        }
        // 清除已存在的布局
        layoutDetailRepository.deleteAll(layoutEntity.getLayoutDetailEntities());
        layoutEntity.setLayoutDetailEntities(layoutDetails);
        return layoutRepository.save(layoutEntity);
    }

    // 根据看板信息查询布局信息是否存在
    public boolean hasLayoutByBoard(BoardEntity boardEntity) {
        if (boardEntity == null) {
            return false;
        }
        return layoutRepository.existsAllByBoardEntity(boardEntity);
    }

    // 根据看板信息查看布局信息
    public LayoutEntity getLayoutByBoard(BoardEntity boardEntity) {
        Optional<LayoutEntity> layoutEntityOptional = layoutRepository.findAllByBoardEntity(boardEntity);
        if (!layoutEntityOptional.isPresent()) {
            throw new RuntimeException(ApplicationMessages.BOARD_ID_NOT_FOUND + boardEntity.getId());
        }
        return layoutEntityOptional.get();
    }
}
