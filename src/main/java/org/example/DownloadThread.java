package org.example;

import org.models.FileInfo;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DownloadThread extends  Thread{

    FileInfo file;
    DownloadManager manager;

    public DownloadThread(FileInfo file, DownloadManager manager)
    {
        this.file = file;
        this.manager = manager;
    }
    @Override
    public void run() {

        this.file.setStatus("DOWNLOADING...");
        this.manager.updateUI(this.file);
            //download logic
        try {
            //Files.copy(new URL(this.file.getUrl()).openStream(), Paths.get(this.file.getPath()));
            URL url = new URL(this.file.getUrl());
            URLConnection urlConnection = url.openConnection();
            int fileSize = urlConnection.getContentLength();

            int countByte = 0;
            double perc = 0.0;
            double byteSum = 0.0;
            BufferedInputStream bufferedInputStream = new BufferedInputStream(url.openStream());
            FileOutputStream fileOutputStream = new FileOutputStream(this.file.getPath());
            byte data[] = new byte[1024];
            while(true)
            {
               countByte =  bufferedInputStream.read(data,0,1024);
               if(countByte == -1)
               {
                   break;
               }
               fileOutputStream.write(data,0,countByte);
                byteSum = byteSum + countByte;
               if (fileSize > 0)
               {
                   perc = (byteSum / fileSize *100);
                   System.out.println(perc);
                   this.file.setPerc(perc +"");
                   this.manager.updateUI(file);
               }

            }
            fileOutputStream.close();
            bufferedInputStream.close();
            this.file.setPerc(100 +"");
            this.file.setStatus("DOWNLOADED");
        } catch (IOException e) {
            this.file.setStatus("FAILED..");
            System.out.println("Downloading Error.....");
            e.printStackTrace();
        }
        this.manager.updateUI(this.file);
    }
}
