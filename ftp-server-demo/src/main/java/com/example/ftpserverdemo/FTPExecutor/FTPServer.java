package com.example.ftpserverdemo.FTPExecutor;

import org.apache.commons.net.ftp.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

@Configuration
@PropertySource("classpath:/ftp.properties")
@Component
public class FTPServer {

    @Value("${ftp.ip}")
    private String ip;

    @Value("${ftp.port}")
    private String port;

    @Value("${ftp.username}")
    private String username;

    @Value("${ftp.password}")
    private String password;

    public void connect(FTPClient ftpClient) throws IOException {
        ftpClient.connect(ip);
        int res = ftpClient.getReplyCode();
        if(!FTPReply.isPositiveCompletion(res)){
            throw new IOException("Could not connect");
        }
        boolean isLogin = ftpClient.login(username,password);
        if(!isLogin){
            throw new IOException("Could not login");
        }
        System.out.println("Success!");
    }

    public boolean checkFileExist(FTPClient ftpClient,String filePath) throws IOException{
        InputStream inputStream = ftpClient.retrieveFileStream(filePath);
        int res = ftpClient.getReplyCode();
        if( inputStream == null && res == 550){
            return false;
        }
        return true;
    }

    public boolean checkDirExist(FTPClient  ftpClient, String dirPath) throws IOException{
        ftpClient.changeWorkingDirectory(dirPath);
        int res = ftpClient.getReplyCode();
        if( res == 550){
            return false;
        }
        return true;
    }

    public void uploadFile(FTPClient ftpClient, String localPath, String remotePath) throws IOException{
        if(checkFileExist(ftpClient,remotePath)){
            ftpClient.enterLocalPassiveMode();
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

            // upload file
            File localFile = new File(localPath);
            InputStream inputStream = new FileInputStream(localFile);
            System.out.println("Start uploading first file!");
            boolean done = ftpClient.storeFile(remotePath, inputStream);
            inputStream.close();;
            if(done){
                System.out.println(" The first file is uploaded successfully!");
            }
        }else{
            System.out.println("File is exist");
        }
    }

    public void downloadFile(FTPClient ftpClient, String localFilePath, String remoteFilePath) throws Exception{
        ftpClient.enterLocalPassiveMode();
        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
        File downloadFile1 = new File(localFilePath);
        OutputStream outputStream1 = new BufferedOutputStream(new FileOutputStream(downloadFile1));
        boolean check = ftpClient.retrieveFile(remoteFilePath, outputStream1);
        outputStream1.close();
        if (check) {
            System.out.println("File has been downloaded successfully.");
        }
    }

    private void search( FTPClient ftpClient, String dirToSearch, String fileName) throws IOException{
        // search
        ftpClient.enterLocalPassiveMode();
        FTPFileFilter filter = new FTPFileFilter() {
            @Override
            public boolean accept(FTPFile file) {
                return (file.isFile() && file.getName().contains(fileName));
            }
        };

        FTPFile[] result = ftpClient.listFiles(dirToSearch,filter);
        if(result!= null && result.length >0){
            System.out.println("SEARCH RESULT: ");
            for (FTPFile file : result){
                System.out.println(file.getName());
            }
        }else{
            System.out.println("Not foud!");
        }
    }

    private void listFile(FTPClient ftpClient) throws IOException{
        // Lists files and directories
        FTPFile[] files1 = ftpClient.listFiles("/20082021083535/1474709");
        printFileDetails(files1);

        // uses simpler methods
        String[] files2 = ftpClient.listNames();
        printNames(files2);
    }

    private void printFileDetails(FTPFile[] files) {
        System.out.println("List file print detail");
        DateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (FTPFile file : files) {
            String details = file.getName();
            if (file.isDirectory()) {
                details = "[" + details + "]";
            }
            details += "\t\t" + file.getSize();
            details += "\t\t" + dateFormater.format(file.getTimestamp().getTime());

            System.out.println(details);
        }
    }

    private void printNames(String files[]) {
        System.out.println("List file name");
        if (files != null && files.length > 0) {
            for (String aFile: files) {
                System.out.println(aFile);
            }
        }
    }
    public void createDictionary(FTPClient ftpClient, String dirToCreate) throws IOException {
        if(checkDirExist(ftpClient,dirToCreate)){
            Boolean success = ftpClient.makeDirectory(dirToCreate);
            showServerReply(ftpClient);
            if (success) {
                System.out.println("Successfully created directory: " + dirToCreate);
            } else {
                System.out.println("Failed to create directory. See server's reply.");
            }
        }else{
            System.out.println("Directory is exist!");
        }

    }

    private void showServerReply(FTPClient ftpClient) {
        String[] replies = ftpClient.getReplyStrings();
        if (replies != null && replies.length > 0) {
            for (String aReply : replies) {
                System.out.println("SERVER: " + aReply);
            }
        }
    }
}
