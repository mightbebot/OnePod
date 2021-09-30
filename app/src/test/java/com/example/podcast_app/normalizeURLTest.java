package com.example.podcast_app;

import org.junit.Test;
import static org.junit.Assert.*;

public class normalizeURLTest {
    PlayerActivity player = new PlayerActivity();

    @Test
    public void normalizeURL(){
        assertEquals("http://joeroganexp.joerogan.libsynpro.com/rss",
                player.normalizeURL("https://joeroganexp.joerogan.libsynpro.com/rss"));

        assertEquals("http://feeds.feedburner.com/TEDTalks_audio?format=xml",
                player.normalizeURL("feeds.feedburner.com/TEDTalks_audio?format=xml"));

        assertEquals("http://rss.whooshkaa.com/rss/podcast/id/1791",
                player.normalizeURL("http://rss.whooshkaa.com/rss/podcast/id/1791"));

        assertTrue(player.normalizeURL("www.abc.net.au/radio/programs/russia-if-youre-listening/feed/9593214/podcast.xml").equals("http://www.abc.net.au/radio/programs/russia-if-youre-listening/feed/9593214/podcast.xml"));
    }

}
