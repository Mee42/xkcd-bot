package com.carson.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


/**
 * Example program to list links from a URL.
 */


public class PhotoStream {
    public List<String> photos = new ArrayList<String>();


    public static List<String> getUrl(String url) throws IOException{


//        String url = "https://www.google.com/search?q=circle&source=lnms&tbm=isch&sa=X&ved=0ahUKEwjWivHhtaHaAhXBnuAKHdNgBhIQ_AUICigB&biw=1680&bih=895";
//      String url = "https://www.ifunny.co";

        print("Fetching %s...", url);
        List<String> s = new ArrayList<String>();
        Document doc = Jsoup.connect(url).get();
        Elements links = doc.select("a[href]");
        Elements media = doc.select("[src]");
        Elements imports = doc.select("link[href]");

        print("\nMedia: (%d)", media.size());
        for (Element src : media) {
            if (src.tagName().equals("img")) {
                print(" * %s: <%s> %sx%s (%s)",
                        src.tagName(), src.attr("abs:src"), src.attr("width"), src.attr("height"),
                        trim(src.attr("alt"), 20));
                s.add(src.absUrl("abs:src"));

            }else {
                print(" * %s: <%s>", src.tagName(), src.attr("abs:src"));
            }
        }

        print("\nImports: (%d)", imports.size());
        for (Element link : imports) {
            print(" * %s <%s> (%s)", link.tagName(),link.attr("abs:href"), link.attr("rel"));
        }

        print("\nLinks: (%d)", links.size());
        for (Element link : links) {
            print(" * a: <%s>  (%s)", link.attr("abs:href"), trim(link.text(), 35));
        }
        return s;

    }


    private static void print(String msg, Object... args) {
        // System.out.println(String.format(msg, args));
    }

    private static  String trim(String s, int width) {
        if (s.length() > width)
            return s.substring(0, width-1) + ".";
        else
            return s;
    }
}