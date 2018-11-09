package com.rengu.project.board.Service;

import com.rengu.project.board.Entity.BoardEntity;
import com.rengu.project.board.Repository.BoardRepository;
import com.rengu.project.board.Utils.ApplicationMessages;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Slf4j
@Service
@Transactional
public class BoardService {

    private final BoardRepository boardRepository;

    @Autowired
    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    // 保存看板信息
    @CachePut(value = "Board_Cache", key = "#boardEntity.getId()")
    public BoardEntity saveBoard(BoardEntity boardEntity) {
        if (StringUtils.isEmpty(boardEntity.getName())) {
            throw new RuntimeException(ApplicationMessages.BOARD_NAME_ARGS_NOT_FOUND);
        }
        if (StringUtils.isEmpty(boardEntity.getIp())) {
            throw new RuntimeException(ApplicationMessages.BOARD_IP_ARGS_NOT_FOUND);
        }
        if (hasBoardByIP(boardEntity.getIp())) {
            throw new RuntimeException(ApplicationMessages.BOARD_IP_EXISTED + boardEntity.getIp());
        }
        return boardRepository.save(boardEntity);
    }

    // 根据Id删除看板
    public BoardEntity deleteBoardById(String boardId) {
        BoardEntity boardEntity = getBoardById(boardId);
        boardRepository.delete(boardEntity);
        return boardEntity;
    }

    // 根据Id修改看板信息
    public BoardEntity updateBoardById(String boardId, BoardEntity boardArgs) {
        BoardEntity boardEntity = getBoardById(boardId);
        BeanUtils.copyProperties(boardArgs, boardEntity, "id", "createTime");
        return boardRepository.save(boardEntity);
    }

    // 根据Id查询看板
    public BoardEntity getBoardById(String boardId) {
        if (StringUtils.isEmpty(boardId)) {
            throw new RuntimeException(ApplicationMessages.BOARD_ID_ARGS_NOT_FOUND);
        }
        Optional<BoardEntity> boardEntityOptional = boardRepository.findById(boardId);
        if (!boardEntityOptional.isPresent()) {
            throw new RuntimeException(ApplicationMessages.BOARD_ID_NOT_FOUND + boardId);
        }
        return boardEntityOptional.get();
    }

    // 根据IP查询看板
    public BoardEntity getBoardByIP(String boardIP) {
        if (StringUtils.isEmpty(boardIP)) {
            throw new RuntimeException(ApplicationMessages.BOARD_IP_ARGS_NOT_FOUND);
        }
        Optional<BoardEntity> boardEntityOptional = boardRepository.findAllByIp(boardIP);
        if (!boardEntityOptional.isPresent()) {
            throw new RuntimeException(ApplicationMessages.BOARD_IP_NOT_FOUND + boardIP);
        }
        return boardEntityOptional.get();
    }

    // 查询全部看板信息
    public Page<BoardEntity> getBoards(Pageable pageable) {
        return boardRepository.findAll(pageable);
    }

    // 根据Id查看看板是否存在
    public boolean hasBoardByIP(String IP) {
        if (StringUtils.isEmpty(IP)) {
            return false;
        }
        return boardRepository.existsAllByIp(IP);
    }

    // 根据Id查看看板是否存在
    public boolean hasBoardById(String boardId) {
        if (StringUtils.isEmpty(boardId)) {
            return false;
        }
        return boardRepository.existsById(boardId);
    }
}
