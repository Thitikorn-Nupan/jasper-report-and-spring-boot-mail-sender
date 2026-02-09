package com.ttknp.springmailsenderandjasper.controller;

import com.ttknp.springmailsenderandjasper.entities.Information;
import com.ttknp.springmailsenderandjasper.services.GmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${main.prefix.controller}")
public class GmailControl {

    private final GmailService gmailService;

    @Autowired
    public GmailControl(GmailService gmailService) {
        this.gmailService = gmailService;
    }

    @PostMapping(value = "/send/v1")
    private ResponseEntity<Boolean> sendGmailPdfFile(@RequestBody Information information) {
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(gmailService.sendEmailContentAsPdfFile(information.subject,information.receiptEmail));
    }

}
