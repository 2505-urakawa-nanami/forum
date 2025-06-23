package com.example.forum.controller;

import com.example.forum.controller.form.CommentForm;
import com.example.forum.controller.form.ReportForm;
import com.example.forum.service.CommentService;
import com.example.forum.service.ReportService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.sql.Timestamp;
import java.util.List;

@Controller
public class CommentController {
    @Autowired
    ReportService reportService;
    @Autowired
    CommentService commentService;

    /*
     * 新規コメント処理
     */
    @PostMapping("/comment/add/{id}")
    public ModelAndView addComment(@PathVariable ("id") Integer reportId,
                                   @Valid  @ModelAttribute("formModel") CommentForm commentForm, BindingResult result) {
        //エラー処理
        if(result.hasErrors()){
            ModelAndView mav = new ModelAndView("top");
            List<ReportForm> contentData = reportService.findAllByOrderByUpdateDateDesc();
            // 投稿データオブジェクトを保管
            mav.addObject("contents", contentData);
            //コメント内容を全件取得
            List<CommentForm> commentData = commentService.findAllComment();
            //コメントデータオブジェクトを保管
            mav.addObject("comments", commentData);
            mav.addObject("formModel", commentForm);
            return mav;
        }
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
                                      @Valid @ModelAttribute("formModel") CommentForm comment, BindingResult result) {
        //エラー処理
        if(result.hasErrors()){
            ModelAndView mav = new ModelAndView("edit_comment");
            //新規投稿画面に戻る
            mav.addObject("formModel", comment);
            return mav;
        }
        // UrlParameterのidを更新するentityにセット
        comment.setId(id);
        // 編集した投稿を更新
        commentService.saveComment(comment);
        // rootへリダイレクト
        return new ModelAndView("redirect:/");
    }
}
