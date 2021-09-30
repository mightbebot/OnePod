package com.example.podcast_app;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class Podcast {
    public static String TAG = "Podcast";

    public static class Episode {
        private String title;
        private String description;
        private String mp3URL;
        private String imageURL;

        public Episode(String title, String description, String url, String imageURL) {
            this.title = title;
            this.description = description;
            this.mp3URL = url;
            this.imageURL = imageURL;
        }

        public String getTitle() { return title; }

        public String getDescription() { return description; }

        public String getMP3URL() {
            return mp3URL;
        }

        public String getImageURL() {
            return imageURL;
        }

        public String toString() {
            return "Episode(" + title + ", " + description + ", " + mp3URL  + ", " + imageURL + ")";
        }
    }

    private String title;
    private String description;
    private String imageURL;

    private ArrayList<Episode> episodes;

    public Podcast(String title, String description, String imageURL) {
        this.title = title;
        this.description = description;
        this.imageURL = imageURL;

        this.episodes = new ArrayList<>();
    }

    public Podcast() {
        this.episodes = new ArrayList<>();
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void addEpisode(Episode e) {
        episodes.add(e);
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public ArrayList<Episode> getEpisodes() {
        return episodes;
    }

    public String toString() {
        return "Podcast(" + title + "," + description + ")";
    }


    public static Podcast from(String rawURL) {
        Podcast p = new Podcast();

        try {
            URL url = new URL(rawURL);

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(false);

            XmlPullParser xpp = factory.newPullParser();
            URLConnection yc = url.openConnection();
            url.openConnection();
            InputStream is = yc.getInputStream();
            xpp.setInput(is, "UTF_8");

            boolean insideItem = false;

            int eventType = xpp.getEventType(); //loop control variable

            String episodeTitle = null;
            String episodeMP3URL = null;
            String episodeDescription = null;
            String podcastImageURL = null;

            while (eventType != XmlPullParser.END_DOCUMENT) {

                if (eventType == XmlPullParser.START_TAG) {
                    if (xpp.getName().equalsIgnoreCase("item")) {
                        insideItem = true;
                    } else if (xpp.getName().equalsIgnoreCase("title")) {
                        if (insideItem) {
                            episodeTitle = xpp.nextText();
                        } else {
                            p.setTitle(xpp.nextText());
                        }
                    } else if (xpp.getName().equalsIgnoreCase("description")) {
                        if (insideItem) {
                            episodeDescription = xpp.nextText();
                        } else {
                            p.setDescription(xpp.nextText());
                        }
                    } else if (xpp.getName().equalsIgnoreCase("enclosure")) {
                        if (insideItem) {
                            String lk = (String) xpp.getAttributeValue(null, "url");
                            episodeMP3URL = lk;
                        }
                    }

                    else if (xpp.getName().equalsIgnoreCase("itunes:image")){
                        podcastImageURL = xpp.getAttributeValue(null, "href");

                        if (p.getImageURL() == null) {
                            p.setImageURL(podcastImageURL);
                        }
                    }
                } else if (eventType == XmlPullParser.END_TAG && xpp.getName().equalsIgnoreCase("item")) {
                    insideItem = false;
                }

                if (episodeMP3URL != null && episodeTitle != null) {
                    String imageURL = podcastImageURL;
                    if (imageURL == null) {
                        imageURL = p.getImageURL();
                    }

                    Podcast.Episode ep = new Podcast.Episode(episodeTitle, episodeDescription, episodeMP3URL, imageURL);

                    p.addEpisode(ep);

                    episodeMP3URL = null;
                    episodeTitle = null;
                    episodeDescription = null;
                    podcastImageURL = null;
                }

                eventType = xpp.next(); //move to next element
            }


        } catch (MalformedURLException e) {

            Log.v(TAG, "URL ERROR");
        }
        catch (XmlPullParserException e)
        {
            Log.v(TAG, "XML Pull Parser Exception");

        }
        catch (IOException e)
        {
            Log.v(TAG, "IOException" + e.getMessage());
        }

        return p;
    }
}
