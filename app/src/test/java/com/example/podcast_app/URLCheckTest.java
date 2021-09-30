package com.example.podcast_app;


import org.junit.Test;
import static org.junit.Assert.*;

public class URLCheckTest {

    MainActivity main = new MainActivity();



    // Tests a real RSS URL
    @Test
    public void realRSSTest (){

        assertFalse(main.URLchecker("http://www.podcastone.com.au/podcast?categoryID2=8055"));

    }
    // Tests a real URL that is not an RSS
    @Test
    public void realURLTest (){

        assertFalse(main.URLchecker("www.google.com"));

    }
    // Tests a fake URL format (should fail)
    @Test
    public void fakeURLTest (){

        assertFalse(main.URLchecker("www.fakewebsite258723vejne3g73gn.com"));

    }
    // Tests random characters (should fail)
    @Test
    public void garbageEntryTest (){

        assertFalse(main.URLchecker("adfg8734t89hfwf"));

    }

}
