package br.com.equals.fileManagerS3.service;


import br.com.equals.fileManagerS3.models.FileConfig;

public interface S3Client {
    String downloadFileFromS3(FileConfig fileConfigName);
    String uploadFile(FileConfig multipartFileConfig);
    String deleteFileFromS3Bucket(FileConfig fileConfigUrl);
    String downloadFileFromServer(FileConfig fileConfigName);

}
