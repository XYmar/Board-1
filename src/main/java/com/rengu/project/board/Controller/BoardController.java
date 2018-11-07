package com.rengu.project.board.Controller;

import com.rengu.project.board.Entity.BoardEntity;
import com.rengu.project.board.Entity.ResultEntity;
import com.rengu.project.board.Service.BoardService;
import com.rengu.project.board.Service.ResultService;
import com.rengu.project.board.Utils.IPUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/boards")
public class BoardController {

    private final BoardService boardService;

    @Autowired
    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    // 保存看板信息
    @PostMapping(value = "/boards")
    public ResultEntity saveBoard(BoardEntity boardEntity) {
        return ResultService.build(boardService.saveBoard(boardEntity));
    }

    // 注册看板信息
    @PostMapping(value = "/register")
    public ResultEntity registerBoard(HttpServletRequest httpServletRequest) {
        String remoteIP = IPUtils.getRemoteIP(httpServletRequest);
        BoardEntity boardEntity = new BoardEntity();
        boardEntity.setName(remoteIP);
        boardEntity.setIp(remoteIP);
        return ResultService.build(boardService.saveBoard(boardEntity));
    }

    //根据Id删除看板
    @DeleteMapping(value = "/{boardId}")
    public ResultEntity deleteBoardById(@PathVariable(value = "boardId") String boardId) {
        return ResultService.build(boardService.deleteBoardById(boardId));
    }

    // 修改看板信息
    @PatchMapping(value = "/{boardId}")
    public ResultEntity updateBoardById(@PathVariable(value = "boardId") String boardId, BoardEntity boardArgs) {
        return ResultService.build(boardService.updateBoardById(boardId, boardArgs));
    }

    // 根据Id查询看板
    @GetMapping(value = "/{boardId}")
    public ResultEntity getBoardById(@PathVariable(value = "boardId") String boardId) {
        return ResultService.build(boardService.getBoardById(boardId));
    }

    // 查询全部看板
    @GetMapping
    public ResultEntity getBoards(@PageableDefault(sort = "createTime", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResultService.build(boardService.getBoards(pageable));
    }
}
