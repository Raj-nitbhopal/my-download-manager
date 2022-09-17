package org.example;

import org.models.FileInfo;

import java.io.IOException;
import java.net.URL;
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
            Files.copy(new URL(this.file.getUrl()).openStream(), Paths.get(this.file.getPath()));
            this.file.setStatus("DOWNLOADED");
        } catch (IOException e) {
            this.file.setStatus("FAILED..");
            System.out.println("Downloading Error.....");
            e.printStackTrace();
        }
        this.manager.updateUI(this.file);
    }
}
