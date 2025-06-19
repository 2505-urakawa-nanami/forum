package com.example.forum.controller;

import com.example.forum.controller.form.CommentForm;
import com.example.forum.controller.form.ReportForm;
import com.example.forum.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class CommentController {
    @Autowired
    CommentService commentService;

    /*
     * 新規コメント処理
     */
    @PostMapping("/comment/add/{id}")
    public ModelAndView addComment(@PathVariable ("id") Integer reportId,
                                   @ModelAttribute("formModel") CommentForm commentForm) {
        commentForm.setReportId(reportId);
        // 投稿をテーブルに格納
        commentService.saveComment(commentForm);
        // rootへリダイレクト
        return new ModelAndView("redirect:/");
    }
    /*
     * コメント削除処理
     */
    @DeleteMapping("/delete/comment/{id}")
    public ModelAndView deleteComment(@PathVariable Integer id) {
        // 投稿をテーブルに格納
        commentService.deleteComment(id);
        // rootへリダイレクト
        return new ModelAndView("redirect:/");
    }
    /*
     * コメント編集画面表示処理
     */
    @GetMapping("/edit/comment/{id}")
    public ModelAndView editComment(@PathVariable Integer id) {
        ModelAndView mav = new ModelAndView();
        // 編集する投稿を取得
        CommentForm comment = commentService.editComment(id);
        // 編集する投稿をセット
        mav.addObject("formModel", comment);
        // 画面遷移先を指定
        mav.setViewName("edit_comment");
        return mav;
    }

    /*
     * コメント編集処理
     */
    @PutMapping("/comment/update/{id}")
    public ModelAndView updateComment(@PathVariable Integer id,
                                      @ModelAttribute("formModel") CommentForm comment) {
        // UrlParameterのidを更新するentityにセット
        comment.setId(id);
        // 編集した投稿を更新
        commentService.saveComment(comment);
        // rootへリダイレクト
        return new ModelAndView("redirect:/");
    }
}
