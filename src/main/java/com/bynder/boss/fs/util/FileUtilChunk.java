package com.bynder.boss.fs.util;

import java.io.*;

import com.j256.simplemagic.ContentInfo;
import com.j256.simplemagic.ContentInfoInputStreamWrapper;
import com.j256.simplemagic.ContentInfoUtil;


public class FileUtilChunk {
    public FileUtilChunk() {}
    public ContentInfo contentInfoFromStreamWrapper(File file) throws IOException {
        InputStream resourceStream = null;
        try{
            resourceStream = new FileInputStream(file);
        }catch(Exception e){
            System.out.println("resourceStream "+ e.getMessage());
        }
        //resourceStream = new FileInputStream(file);
        //InputStream resourceStream = getClass().getResourceAsStream(resource);

        ByteArrayOutputStream outputStream;
        try {
            outputStream = new ByteArrayOutputStream();
            // read it in 100 times
            for (int i = 0; i < 100; i++) {
                copyStream(resourceStream, outputStream);
                resourceStream.close();
                //resourceStream = getClass().getResourceAsStream(resource);
                resourceStream = new FileInputStream(file);
            }
        } finally {
            if(resourceStream!=null)
                resourceStream.close();
        }
        byte[] resourceBytes = outputStream.toByteArray();

        ByteArrayInputStream inputSteam = new ByteArrayInputStream(resourceBytes);
        ContentInfoInputStreamWrapper wrappedStream = new ContentInfoInputStreamWrapper(inputSteam);
        try {
            ByteArrayOutputStream checkOutputStream = new ByteArrayOutputStream();

            // read it in 100 times
            for (int i = 0; i < 10; i++) {
                // coverage
                wrappedStream.available();
                wrappedStream.skip(10);
                wrappedStream.read(new byte[10]);
                wrappedStream.read(new byte[10], 2, 5);
                wrappedStream.read();
                wrappedStream.mark(10);
                wrappedStream.reset();
                wrappedStream.markSupported();
                copyStream(wrappedStream, checkOutputStream);
            }

            return wrappedStream.findMatch();
        } finally {
            wrappedStream.close();
        }
    }

    private void copyStream(InputStream input, OutputStream output) throws IOException {
        byte[] buffer = new byte[1024];
        while (true) {
            int numRead = input.read(buffer);
            if (numRead < 0) {
                return;
            }
            output.write(buffer, 0, numRead);
        }
    }

    public ContentInfo contentInfoFromByteArray(File file) throws IOException {
        ContentInfo info = null ;
        try(BufferedInputStream in = new BufferedInputStream(new FileInputStream(file))) {
            byte[] bbuf = new byte[4096*10];
            int len;
            while ((len = in.read(bbuf)) != -1) {
                // process data here: bbuf[0] thru bbuf[len - 1]

                ContentInfoUtil util =   new ContentInfoUtil();
                info = util.findMatch(bbuf);

            }
        }
        return info;
    }
}

