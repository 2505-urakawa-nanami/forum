package com.example.forum.controller;

import com.example.forum.controller.form.CommentForm;
import com.example.forum.controller.form.ReportForm;
import com.example.forum.service.CommentService;
import com.example.forum.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.List;

@Controller
public class ForumController {
    @Autowired
    ReportService reportService;
    @Autowired
    CommentService commentService;

    /*
     * 投稿内容表示処理・コメント内容表示処理
     */
    @GetMapping
    public ModelAndView top(@ModelAttribute("start") String start,
                            @ModelAttribute("end") String end) throws ParseException{
        ModelAndView mav = new ModelAndView();
        List<ReportForm> contentData;

        if(start == null || start.isBlank() && end == null || end.isBlank()) {
            // 投稿を全件取得
            contentData = reportService.findAllReport();
        }else{
            contentData = reportService.findDaysReport(start, end);
            if (!start.isBlank()){
                mav.addObject("start", start);
            }
            if(!end.isBlank()){
                mav.addObject("end", end);
            }
        }
        //コメント内容を全件取得
        List<CommentForm> commentData = commentService.findAllComment();
        // 画面遷移先を指定
        mav.setViewName("/top");
        // 投稿データオブジェクトを保管
        mav.addObject("contents", contentData);
        //コメントデータオブジェクトを保管
        mav.addObject("comments", commentData);
        mav.addObject("formModel", new CommentForm());
        return mav;
    }

    /*
     * 新規投稿画面表示
     */
    @GetMapping("/new")
    public ModelAndView newContent() {
        ModelAndView mav = new ModelAndView();
        // form用の空のentityを準備
        ReportForm reportForm = new ReportForm();
        // 画面遷移先を指定
        mav.setViewName("/new");
        // 準備した空のFormを保管
        mav.addObject("formModel", reportForm);
        return mav;
    }

    /*
     * 新規投稿処理
     */
    @PostMapping("/add")
    public ModelAndView addContent(@ModelAttribute("formModel") ReportForm reportForm) {
        // 投稿をテーブルに格納
        reportService.saveReport(reportForm);
        // rootへリダイレクト
        return new ModelAndView("redirect:/");
    }

    /*
     * 投稿削除処理
     */
    @DeleteMapping("/delete/{id}")
    public ModelAndView deleteContent(@PathVariable Integer id) {
        // 投稿をテーブルに格納
        reportService.deleteReport(id);
        // rootへリダイレクト
        return new ModelAndView("redirect:/");
    }

    /*
     * 編集画面表示処理
     */
    @GetMapping("/edit/{id}")
    public ModelAndView editContent(@PathVariable Integer id) {
        ModelAndView mav = new ModelAndView();
        // 編集する投稿を取得
        ReportForm report = reportService.editReport(id);
        // 編集する投稿をセット
        mav.addObject("formModel", report);
        // 画面遷移先を指定
        mav.setViewName("/edit");
        return mav;
    }

    /*
     * 編集処理
     */
    @PutMapping("/update/{id}")
    public ModelAndView updateContent(@PathVariable Integer id,
                                      @ModelAttribute("formModel") ReportForm report) {
        // UrlParameterのidを更新するentityにセット
        report.setId(id);

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        report.setUpdateDate(timestamp);
        // 編集した投稿を更新
        reportService.saveReport(report);
        // rootへリダイレクト
        return new ModelAndView("redirect:/");
    }


}
