package com.easybbs.controller;

import com.easybbs.entity.po.ForumBoard;
import com.easybbs.entity.vo.ResponseVO;
import com.easybbs.service.ForumBoardService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/board")
public class ForumBoardController extends ABaseController {

    @Resource
    private ForumBoardService forumBoardService;

    @RequestMapping("/loadBoard")
    public ResponseVO loadBoard() {
        Integer postType = 0;
        List<ForumBoard> boardList = forumBoardService.getBoardTree(null);
        return getSuccessResponseVO(boardList);
    }

}
