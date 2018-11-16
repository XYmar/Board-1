package com.rengu.project.board.Controller;

import com.rengu.project.board.Entity.*;
import com.rengu.project.board.Service.BoardService;
import com.rengu.project.board.Service.LayoutService;
import com.rengu.project.board.Service.ResultService;
import com.rengu.project.board.Utils.IPUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(value = "/boards")
public class BoardController {

    private final BoardService boardService;
    private final LayoutService layoutService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    public BoardController(BoardService boardService, LayoutService layoutService, SimpMessagingTemplate simpMessagingTemplate) {
        this.boardService = boardService;
        this.layoutService = layoutService;
        this.simpMessagingTemplate = simpMessagingTemplate;
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

    // 保存看板部署
    @PostMapping(value = "/{boardId}/layout")
    public ResultEntity saveLayoutByBoard(@PathVariable(value = "boardId") String boardId, @RequestBody List<LayoutDetailEntity> layoutDetailEntityList) {
        BoardEntity boardEntity = boardService.getBoardById(boardId);
        LayoutEntity layoutEntity = layoutService.saveLayoutByBoard(boardEntity, layoutDetailEntityList);
        simpMessagingTemplate.convertAndSend("/refresh/" + boardEntity.getIp(), "true");
        return ResultService.build(layoutEntity);
    }

    // 查询看板布局
    @GetMapping(value = "/{boardId}/layout")
    public ResultEntity getLayoutByBoard(@PathVariable(value = "boardId") String boardId) {
        return ResultService.build(layoutService.getLayoutByBoard(boardService.getBoardById(boardId)));
    }

    // 加载看板布局
    @GetMapping(value = "/show-layout")
    public ResultEntity showBoardayout(HttpServletRequest httpServletRequest) {
        return ResultService.build(layoutService.getLayoutByBoard(boardService.getBoardByIP(IPUtils.getRemoteIP(httpServletRequest))));
    }

    @PostMapping(value = "/push-message")
    public void pushMessageByBoard(@RequestParam(value = "boardIds") String[] boardIds, PushMessageEntity pushMessageEntity) {
        boardService.pushMessageByBoard(boardIds, pushMessageEntity);
    }

    // 获取看板IP
    @GetMapping(value = "/IP")
    public ResultEntity getIPByBoard(HttpServletRequest httpServletRequest) {
        return ResultService.build(IPUtils.getRemoteIP(httpServletRequest));
    }
}
