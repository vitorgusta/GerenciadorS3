package br.com.equals.fileManagerS3.controller;

import br.com.equals.fileManagerS3.models.FileConfig;
import br.com.equals.fileManagerS3.service.S3Client;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/storage/")
public class S3Controller {


    private S3Client s3Client;


    @Autowired
    S3Controller(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    @PostMapping("/uploadfile")
    public String uploadFileS3(@RequestPart(value = "file") MultipartFile file) {
        return this.s3Client.uploadFile(new FileConfig(file));
    }


    @DeleteMapping("deleteFile")
    public String deleteFile(@RequestBody String file) {
        Gson gson = new Gson();
        FileConfig fileConfig = gson.fromJson(file, FileConfig.class);
        return this.s3Client.deleteFileFromS3Bucket(fileConfig);
    }

    @GetMapping("downloadFileS3/{filename}")
    public String downloadFileFromS3(@RequestBody String file) {
        Gson gson = new Gson();
        FileConfig fileConfig = gson.fromJson(file, FileConfig.class);
        return this.s3Client.downloadFileFromS3(fileConfig);
    }

    @PostMapping("downloadFileServer")
    public String downloadFileFromServer(@RequestBody String file) {
        Gson gson = new Gson();
        FileConfig fileConfig = gson.fromJson(file, FileConfig.class);
        return this.s3Client.downloadFileFromServer(fileConfig);
    }


}
