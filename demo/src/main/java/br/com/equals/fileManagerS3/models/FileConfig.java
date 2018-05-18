package br.com.equals.fileManagerS3.models;

import org.springframework.web.multipart.MultipartFile;

public class FileConfig {

    private String url;
    private MultipartFile multipartFile;

    public FileConfig(){}

    public FileConfig(MultipartFile multipartFile) {
        this.multipartFile = multipartFile;
    }

    public FileConfig(String url, MultipartFile multipartFile) {
        this.url = url;
        this.multipartFile = multipartFile;
    }

    public MultipartFile getMultipartFile() {
        return multipartFile;
    }

    public void setMultipartFile(MultipartFile multipartFile) {
        this.multipartFile = multipartFile;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
