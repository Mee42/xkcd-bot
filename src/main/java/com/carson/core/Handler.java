package com.carson.core;

import com.carson.Googler;
import com.carson.XKCD;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.RequestBuffer;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.util.List;

public class Handler {
    @EventSubscriber
    public void onMessage(MessageReceivedEvent event){
        String content = event.getMessage().getContent();
        System.out.println(event.getAuthor().getName() + "  :  " + content);
        if(!content.startsWith("xkcd"))
            return;
        try {

            if (content.equals("xkcd")) {
                runRandom(event);
            }else {
                try {
                    int i = Integer.parseInt(content.split(" ", 2)[1]);
                    runWithInt(event, i);
                    return;
                } catch (NumberFormatException e) { }catch (ArrayIndexOutOfBoundsException e){ }
                
                String str = content.replaceFirst("xkcd ","");
                String url = Googler.Google("site:xkcd.com   " + str);
                String number = url.replaceFirst("http|https","")
                        .replaceFirst("www","")
                        .replaceFirst("xkcd.com/","");//TODO TEST
                int numberInt = Integer.parseInt(number);
                runWithInt(event,numberInt);
                return;
            }
        }catch(Exception e){//TODO FIX
            e.printStackTrace();
            sendMessage(event, "there was a problem processing your request.");
        }

    }

    private void runRandom(MessageReceivedEvent event) throws IOException {
        runWithInt(event,(int)(Math.random()*2051+1));
    }


    private void runWithInt(MessageReceivedEvent event, int no) throws IOException {
//		int no =(int)(Math.random() * 1800 + 100);
        File file = new File("/home/carson/java/files/xkcd/" + no + ".png");
        File alt = new File("/home/carson/java/files/xkcd/alt/" + no);

        if(file.exists() && alt.exists()) {
            sendFile(no, event,file,alt);
            return;
        }

        file.createNewFile();
        alt.createNewFile();

        List<String> pics;
        pics = PhotoStream.getUrl("https://www.xkcd.com/" + no  );
        for(String pic : pics) {
            if(pic.startsWith("https://imgs.xkcd.com/comics")){
                saveImage(pic, file.getPath());
                String altText = XKCD.getAlt(no);
                Files.write(alt.toPath(),altText.getBytes());
            }

        }
        sendFile(no, event, file,alt);
    }


    private void sendFile(int no, MessageReceivedEvent event, File file, File alt) {
        try {
            event.getChannel().sendFile(file);
            sendMessage(event, "`" + no + ":`"  + Files.readAllLines(alt.toPath()).get(0));
        } catch (FileNotFoundException e) {
            sendMessage(event, "tryed to find a file we know exists. problem");
            System.out.println("ERROR: missread a file");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendMessage(MessageReceivedEvent event, String s) {
        RequestBuffer.request(() -> event.getChannel().sendMessage(s));
    }

    public void saveImage(String imageUrl, String destinationFile) throws IOException {
        URL url = new URL(imageUrl);
        InputStream is = url.openStream();
        OutputStream os = new FileOutputStream(destinationFile);

        byte[] b = new byte[2048];
        int length;

        while ((length = is.read(b)) != -1) {
            os.write(b, 0, length);
        }

        is.close();
        os.close();
    }


}
