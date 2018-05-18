package br.com.equals.fileManagerS3.serviceImpl;

import br.com.equals.fileManagerS3.models.FileConfig;
import br.com.equals.fileManagerS3.service.S3Client;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.Date;

@Service
public class S3ClientImpl implements S3Client {

    private AmazonS3Client s3client;

    @Value("${amazonProperties.endpointUrl}")
    private String endpointUrl;

    @Value("${amazonProperties.bucketName}")
    private String bucketName;

    @Value("${amazonProperties.accessKey}")
    private String accessKey;

    @Value("${amazonProperties.secretKey}")
    private String secretKey;

    @Value("${folder.destination}")


    @PostConstruct
    private void initializeAmazon() {
        AWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);
        this.s3client = new AmazonS3Client(credentials);
    }

    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }

    private String generateFileName(MultipartFile multiPart) {
        return new Date().getTime() + "-" + multiPart.getOriginalFilename().replace(" ", "_");
    }

    private void uploadFileTos3bucket(String fileName, File file) {
        s3client.putObject(new PutObjectRequest(bucketName, fileName, file)
                .withCannedAcl(CannedAccessControlList.PublicRead));
    }

    @Override
    public String uploadFile(FileConfig fileConfig) {
        String fileUrl = "";
        try {
            File file = convertMultiPartToFile(fileConfig.getMultipartFile());
            String fileName = generateFileName(fileConfig.getMultipartFile());
            fileUrl = endpointUrl + "/" + bucketName + "/" + fileName;
            uploadFileTos3bucket(fileName, file);
            file.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileUrl;
    }

    @Override
    public String deleteFileFromS3Bucket(FileConfig fileConfig) {
        String fileName = fileConfig.getUrl().substring(fileConfig.getUrl().lastIndexOf("/") + 1);
        s3client.deleteObject(new DeleteObjectRequest(bucketName, fileName));
        return "Successfully deleted";
    }

    @Override
    public String downloadFileFromServer(FileConfig file) {
        try{
        URL url = new URL(file.getUrl());
        ReadableByteChannel rbc;
        rbc = Channels.newChannel(url.openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public String downloadFileFromS3(FileConfig file) {
        try {
            S3Object s3Object = s3client.getObject(new GetObjectRequest(bucketName, file.getUrl()));
            S3ObjectInputStream s3ObjectInputStream = s3Object.getObjectContent();
            FileUtils.copyInputStreamToFile(s3ObjectInputStream, new File("/home/vitorgarcia/Pictures/" + file.getUrl()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Successfully download";
    }




}


