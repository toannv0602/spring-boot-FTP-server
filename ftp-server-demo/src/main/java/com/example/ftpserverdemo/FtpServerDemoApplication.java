package com.example.ftpserverdemo;

import com.example.ftpserverdemo.FTPExecutor.FTPServer;
import org.apache.commons.net.ftp.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

@SpringBootApplication
public class FtpServerDemoApplication {


    public static void main(String[] args) {
        ApplicationContext context =  SpringApplication.run(FtpServerDemoApplication.class, args);
        FTPClient ftpClient = new FTPClient();
        FTPServer ftpServer = context.getBean(FTPServer.class);
        try {
            ftpServer.connect(ftpClient);

        }catch (IOException ex) {
            System.out.println("Oops! Something wrong happened");
            ex.printStackTrace();
        }

    }
//
//    private static void showServerReply(FTPClient ftpClient) {
//        String[] replies = ftpClient.getReplyStrings();
//        if (replies != null && replies.length > 0) {
//            for (String aReply : replies) {
//                System.out.println("SERVER: " + aReply);
//            }
//        }
//    }
//
//    public static void connectFTP(){
//
//        FTPClient ftpClient = new FTPClient();
//        try {
//            ftpClient.connect(host);
//            showServerReply(ftpClient);
//            int replyCode = ftpClient.getReplyCode();
//            if (!FTPReply.isPositiveCompletion(replyCode)) {
//                System.out.println("Operation failed. Server reply code: " + replyCode);
//                return;
//            }
//            boolean success = ftpClient.login(username, password);
//            showServerReply(ftpClient);
//            if (!success) {
//                System.out.println("Could not login to the server");
//                return;
//            }
//
//
//
////            listFile(ftpClient);
//
////              // CHECK  exist dictionary
////            boolean check = ftpClient.changeWorkingDirectory("/20082021083535");
////            showServerReply(ftpClient);
////
////            if (check) {
////                System.out.println("Successfully changed working directory.");
////            } else {
////                System.out.println("Failed to change working directory. See server's reply.");
////            }
//
//            // logs out
//            ftpClient.logout();
//            ftpClient.disconnect();
//
//        } catch (IOException ex) {
//            System.out.println("Oops! Something wrong happened");
//            ex.printStackTrace();
//        }
////        finally {
////            // logs out and disconnects from server
////            try {
////                if (ftpClient.isConnected()) {
////                    ftpClient.logout();
////                    ftpClient.disconnect();
////                }
////            } catch (IOException ex) {
////                ex.printStackTrace();
////            }
////        }
//
////        FTPClient client = new FTPClient();
////
////        try{
////            client.connect(host);
////            client.login(username,password);
////            if(client.isConnected()){
////                String[] names = client.listNames();
////                for(String name : names){
////                    System.out.println("Name= "+name);
////                }
////                FTPFile[] ftpFiles = client.listFiles();
////                for (FTPFile ftpFile : ftpFiles){
////                    if(ftpFile.getType() == FTPFile.FILE_TYPE){
////                        System.out.printf("FTPFile: %s; %s%n",
////                                ftpFile.getName(), FileUtils.byteCountToDisplaySize(ftpFile.getSize()));
////                    }
////                }
////            }
////            client.logout();
////        }catch (IOException e){
////            e.printStackTrace();
////        }finally {
////            try{
////                client.disconnect();
////            }catch (IOException e){
////                e.printStackTrace();
////            }
////        }
//    }
//
//    public void uploadFile(FTPClient ftpClient) throws IOException{
//        ftpClient.enterLocalPassiveMode();
//        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
//
//        // upload file
//        File localFile = new File("D:/download/Test/Test.txt");
//        String remoteFileName = "/upload/test/Test.txt";
//        InputStream inputStream = new FileInputStream(localFile);
//        System.out.println("Start uploading first file!");
//        boolean done = ftpClient.storeFile(remoteFileName, inputStream);
//        inputStream.close();;
//        if(done){
//            System.out.println(" The first file is uploaded successfully!");
//        }
//    }
//
//    public static void downloadFile(FTPClient ftpClient) throws Exception{
//        ftpClient.enterLocalPassiveMode();
//        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
//        String fileName = "1474709_102_THAI THI THUY NHUNG_220598425_270420211140051.PDF";
//        // APPROACH #1: using retrieveFile(String, OutputStream)
//        String remoteFile1 = "/20082021083535/1474709/"+fileName;
//        File downloadFile1 = new File("D:/download/Test/"+fileName);
//        OutputStream outputStream1 = new BufferedOutputStream(new FileOutputStream(downloadFile1));
//        boolean check = ftpClient.retrieveFile(remoteFile1, outputStream1);
//        outputStream1.close();
//
//        if (check) {
//            System.out.println("File has been downloaded successfully.");
//        }
//    }
//
//    private static void search( FTPClient ftpClient, String fileName, String path) throws IOException{
//        // search
//        ftpClient.enterLocalPassiveMode();
//        String dirToSearch = "/20082021083535/1474709";
//        FTPFileFilter filter = new FTPFileFilter() {
//            @Override
//            public boolean accept(FTPFile file) {
//                return (file.isFile() && file.getName().contains("NHUNG"));
//            }
//        };
//
//        FTPFile[] result = ftpClient.listFiles(dirToSearch,filter);
//        if(result!= null && result.length >0){
//            System.out.println("SEARCH RESULT: ");
//            for (FTPFile file : result){
//                System.out.println(file.getName());
//            }
//        }
//    }
//
//    private static void listFile(FTPClient ftpClient) throws IOException{
//        // Lists files and directories
//        FTPFile[] files1 = ftpClient.listFiles("/20082021083535/1474709");
//        printFileDetails(files1);
//
//        // uses simpler methods
//        String[] files2 = ftpClient.listNames();
//        printNames(files2);
//    }
//
//    private static void printFileDetails(FTPFile[] files) {
//        System.out.println("List file print detail");
//        DateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        for (FTPFile file : files) {
//            String details = file.getName();
//            if (file.isDirectory()) {
//                details = "[" + details + "]";
//            }
//            details += "\t\t" + file.getSize();
//            details += "\t\t" + dateFormater.format(file.getTimestamp().getTime());
//
//            System.out.println(details);
//        }
//    }
//
//    private static void printNames(String files[]) {
//        System.out.println("List file name");
//        if (files != null && files.length > 0) {
//            for (String aFile: files) {
//                System.out.println(aFile);
//            }
//        }
//    }
//
//
//    public void createDictionary(FTPClient ftpClient, String dirToCreate) throws IOException {
//        Boolean success = ftpClient.makeDirectory(dirToCreate);
//        showServerReply(ftpClient);
//        if (success) {
//            System.out.println("Successfully created directory: " + dirToCreate);
//        } else {
//            System.out.println("Failed to create directory. See server's reply.");
//        }
//    }
}
