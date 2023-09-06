package com.example.ftpclient;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;


import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btnConnect).setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                connectFTP();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void connectFTP() {
        new Thread(() -> {
            String server = "192.168.254.30";
            int PORT = 2221;
            String username = "LECIP";
            String password = "LECIP";

            FTPClient ftpClient = new FTPClient();
            try {// FTPサーバーに接続
                ftpClient.connect(server, PORT);
                // ログイン
                boolean loginSuccess = ftpClient.login(username, password);
                if (loginSuccess) {
                    Log.d(TAG,"ログインに成功しました。");


                    // ファイル一覧を取得
//                    String localFilePath = "/log/khoi/";

//                    FTPFile[] files = ftpClient.listFiles();
//                    for (FTPFile file : files) {
////                        file.setPermission(FTPFile.USER_ACCESS, FTPFile.WRITE_PERMISSION, true);
//                        Log.d(TAG,file.getName());
//                    }



//                    FTPFile current = ftpClient.listFiles(".csv")[0];
//                    current.setPermission(FTPFile.USER_ACCESS, FTPFile.READ_PERMISSION, true);



                    String remoteFilePath = "LIVUManager/app/AppVersion.csv";
                    String localFilePath = "/log/Server/download/app/tmpAppVersion.csv";
                    File fileObj = new File(localFilePath);
                    if(fileObj.delete())Files.createFile(fileObj.toPath());

                    try (OutputStream os =new BufferedOutputStream(new FileOutputStream(fileObj))){
                        boolean isFileRetrieve = ftpClient.retrieveFile(remoteFilePath, os);
                        if (isFileRetrieve) {
                            Log.d(TAG,"ファイルをダウンロードしました。");
                        } else {
                            Log.d(TAG,"ファイルのダウンロードに失敗しました。");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                     //ファイルをダウンロード
//                    String localFilePath = "/log/khoi/AppVersion.csv";
//                    String remoteFilePath = "AppVersion.csv";
//
//                    FileOutputStream fos = new FileOutputStream(localFilePath);
//                    OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(localFilePath));
//                    InputStream inputStream = ftpClient.retrieveFileStream(remoteFilePath);
//                    byte[] bytes = new byte[4096];
//                    int byteRead = -1;
//                    while ((byteRead = inputStream.read(bytes)) != -1) {
//                        outputStream.write(bytes, 0, byteRead);
//                    }
//                    boolean downloadSuccess = ftpClient.completePendingCommand();
//                    outputStream.close();
//                    inputStream.close();
//                    ftpClient.setFileType(FTP.BINARY_FILE_TYPE); // バイナリモードで転送

//                    OutputStream outputStream = new BufferedOutputStream(new FileOutputStream())
//                    boolean downloadSuccess = ftpClient.retrieveFile(remoteFilePath, fos);
//                    fos.close();
//                    if (downloadSuccess) {
//                        Log.d(TAG,"ファイルをダウンロードしました。");
//                    } else {
//                        Log.d(TAG,"ファイルのダウンロードに失敗しました。");
//                    }

                    // ログアウトして接続を閉じる
                    ftpClient.logout();
                    ftpClient.disconnect();
                } else {
                    Log.d(TAG,"ログインに失敗しました。");
                }
            }  catch(IOException e){
                e.printStackTrace();
            }
        }).start();
    }
}
