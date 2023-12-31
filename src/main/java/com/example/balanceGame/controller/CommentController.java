package com.example.balanceGame.controller;

import com.example.balanceGame.controller.http.request.BoardReportRequest;
import com.example.balanceGame.controller.http.request.CommentDeleteRequest;
import com.example.balanceGame.controller.http.request.CommentRegistRequest;
import com.example.balanceGame.controller.http.request.CommentReportRequest;
import com.example.balanceGame.exception.Message;
import com.example.balanceGame.service.CommentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@Tag(name="댓글")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {
    private final CommentService commentService;
    @PostMapping("/regist")
    public ResponseEntity<Long> regist(@RequestBody CommentRegistRequest commentRegistRequest, Principal principal) {
        long userKey = Long.parseLong(principal.getName());
        try {
            Long registCommentKey = commentService.regist(commentRegistRequest, userKey);// 등록 시도

            if (registCommentKey != null) {
                return ResponseEntity.status(HttpStatus.OK).body(registCommentKey); // 등록 성공
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null); // 등록 실패
            }
        } catch (Exception e) {
            log.info(e.getMessage()); // 에러 정보
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); // 실패 메시지 리턴
        }
    }

    @PostMapping("/delete")
    public ResponseEntity delete(@RequestBody CommentDeleteRequest commentDeleteRequest, Principal principal) {
        long userKey = Long.parseLong(principal.getName());
        try {
            boolean delete = commentService.delete(commentDeleteRequest, userKey);
            if (delete) {
                return new ResponseEntity(Message.DELETE_COMMENT, HttpStatus.OK);
            } else {
                return new ResponseEntity(Message.DELETE_COMMENT_FAILED, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            return new ResponseEntity(Message.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/report")
    public ResponseEntity report(@RequestBody CommentReportRequest commentReportRequest, Principal principal) {
        try {
            Long userKey = Long.valueOf(principal.getName());
            boolean report = commentService.report(commentReportRequest, userKey);
            if (report) {
                return new ResponseEntity(Message.REPORT_REGIST_SUCCESS, HttpStatus.OK);
            } else {
                return new ResponseEntity(Message.REPORT_REGIST_FAILED, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            return new ResponseEntity(Message.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
