package com.carson;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Googler {



    public static String Google(String keyword) {
        try {
            String google = "http://www.google.com/search?q=";
            String search = keyword;
            String charset = "UTF-8";
            String userAgent = "chrome"; // Change this to your company's name and bot homepage!

            Elements links = Jsoup.connect(google + URLEncoder.encode(search, charset)).userAgent(userAgent).get().select(".g>.r>a");

            for (Element link : links) {
                String title = link.text();
                String url = link.absUrl("href"); // Google returns URLs in format "http://www.google.com/url?q=<url>&sa=U&ei=<someKey>".
                url = URLDecoder.decode(url.substring(url.indexOf('=') + 1, url.indexOf('&')), "UTF-8");

                if (!url.startsWith("http")) {
                    continue; // Ads/news/etc.
                }

//                System.out.println("Title: " + title);
//                System.out.println("URL: " + url);
                return url;

            }



        } catch (IOException e) {
            e.printStackTrace();
        }


        return "";

    }

    public static String GoogleYoutube(String keyword) {
        System.out.println("keyword:" + keyword);
        keyword = "site:www.youtube.com  \"watch\" " + keyword;
        //site:www.youtube.com "watch" ping sound


        try {
            String google = "http://www.google.com/search?q=";
            String search = keyword;
            String charset = "UTF-8";
            String userAgent = "Carson-bot"; // Change this to your company's name and bot homepage!

            Elements links = Jsoup.connect(google + URLEncoder.encode(search, charset)).userAgent(userAgent).get().getAllElements();

            for (Element link : links) {
                String url = link.absUrl("href"); // Google returns URLs in format "http://www.google.com/url?q=<url>&sa=U&ei=<someKey>".
                try {
                    url = URLDecoder.decode(url.substring(url.indexOf('=') + 1, url.indexOf('&')), "UTF-8");
                }catch(StringIndexOutOfBoundsException x) {
                    //x.printStackTrace();
                }

                if(url.startsWith("https://www.youtube.com/watch")) {
                    return url;
                }



            }





        } catch (IOException e) {
            e.printStackTrace();
        }


        return "";

    }




}
