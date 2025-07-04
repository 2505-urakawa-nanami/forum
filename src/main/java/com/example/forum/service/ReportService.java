package com.example.forum.service;

import com.example.forum.controller.form.ReportForm;
import com.example.forum.repository.ReportRepository;
import com.example.forum.repository.entity.Report;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ReportService {
    @Autowired
    ReportRepository reportRepository;

    /*
     * レコード全件取得処理
     */
    public List<ReportForm> findAllByOrderByUpdateDateDesc() {
        List<Report> results = reportRepository.findAllByOrderByUpdateDateDesc();
        List<ReportForm> reports = setReportForm(results);
        return reports;
    }

    /*
    *指定期間で絞り込み取得
     */
    public List<ReportForm> findDaysReport(String start, String end) throws ParseException{
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Timestamp tsStart = null;
        Timestamp tsEnd = null;

        //開始日の処理
        if(!start.isBlank()){
            Date date = sdf.parse(start + " 00:00:00");
            tsStart = new Timestamp(date.getTime());
        }else{
            Date date = sdf.parse("2020-01-01 00:00:00");
            tsStart = new Timestamp(date.getTime());
        }
        //終了日の設定
        if(!end.isBlank()){
            Date date = sdf.parse(end + " 23:59:59");
            tsEnd = new Timestamp(date.getTime());
        }else{
            Date date = sdf.parse("2500-01-01 23:59:59");
            tsEnd = new Timestamp(date.getTime());
        }
        List<Report> results =reportRepository.findAllByUpdateDateBetweenOrderByUpdateDateDesc(tsStart, tsEnd);
        List<ReportForm> reports = setReportForm(results);
        return reports;
    }

    /*
     * DBから取得したデータをFormに設定
     */
    private List<ReportForm> setReportForm(List<Report> results) {
        List<ReportForm> reports = new ArrayList<>();

        for (int i = 0; i < results.size(); i++) {
            ReportForm report = new ReportForm();
            Report result = results.get(i);
            report.setId(result.getId());
            report.setContent(result.getContent());
            reports.add(report);
        }
        return reports;
    }

    /*
     * レコード追加
     */
    public void saveReport(ReportForm reqReport) {
        Report saveReport = setReportEntity(reqReport);
        reportRepository.save(saveReport);
    }

    /*
     * リクエストから取得した情報をEntityに設定
     */
    private Report setReportEntity(ReportForm reqReport) {
        Report report = new Report();
        report.setId(reqReport.getId());
        report.setUpdateDate(reqReport.getUpdateDate());
        report.setContent(reqReport.getContent());
        return report;
    }

    /*
     *投稿を削除
     */
    public void deleteReport(Integer id) {
        reportRepository.deleteById(id);
    }

    /*
     * レコード1件取得
     */
    public ReportForm editReport(Integer id) {
        List<Report> results = new ArrayList<>();
        results.add((Report) reportRepository.findById(id).orElse(null));
        List<ReportForm> reports = setReportForm(results);
        return reports.get(0);
    }


}
