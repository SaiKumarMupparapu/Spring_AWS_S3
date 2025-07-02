package org.scaleorange.spring_s3_app.controller;

import java.util.List;

import org.scaleorange.spring_s3_app.service.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller // not @RestController
public class S3Controller {

    @Autowired
    private S3Service service;

    @GetMapping("/")
    public String home(Model model) {
        List<String> files = service.allFilesList();
        model.addAttribute("files", files);
        return "index"; // refers to templates/index.html
    }

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file, Model model) {
        String message = service.saveFile(file);
        model.addAttribute("message", "File uploaded ");
        model.addAttribute("files", service.allFilesList());
        return "index";
    }

    @GetMapping("/download/{filename}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable String filename) {
        byte[] file = service.dowloadFile(filename);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", filename);
        return ResponseEntity.ok().headers(headers).body(file);
    }

    @PostMapping("/delete")
    public String deleteFile(@RequestParam String filename, Model model) {
        String message = service.deletFile(filename);
        model.addAttribute("message", message);
        model.addAttribute("files", service.allFilesList());
        return "index";
    }
}
