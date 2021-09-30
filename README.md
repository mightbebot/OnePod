# OnePod -- a podcasting app

A podcasting app that does one thing and one thing only: podcasting.

## Design Summary

We kept the design of our app as simple as possible. Visually everything is
made up of standard Android UI elements (apart from the player, where we made
the elements ourselves because there were no suitable ones). We made use of
nice light colours, and did away with any complicated menus. A user can get
from opening our app to listening to a podcast in two clicks: one to select a
podcast, and the other to select an episode.

Some of our design choices were inspired by other podcasting apps which we use
in regularly: Pocket Casts, iTunes, PodcastAddict.

## UI Guide

We've tried to make our app as simple (in the UI sense) as possible. Everything
has only one purpose, there aren't any complicated menus, the screens are
presented in a hierarchical fashion.

1. First either select a podcast from our own curated selection, or input your
   own RSS feed URL
2. Then choose the specific episode you want to listen to in the episodes
   screen, read the description, etc.
3. Now you can listen to your podcast episode! How easy was that.

## Code Guide

The app is broken up into three activities (`WelcomeActivity`,
`EpisodesActivity`, `PlayerActivity`), with each one owning a specific stage of
the apps user experience. Then there are two supporting classes
`EpisodesAdapter` (which is a utility we use for working with `RecyclerViews`
and `Podcast` (which has our basic data structures in it).

### WelcomeActivity

WelcomeActivity is the first screen a user sees when they open our app. It
presents them with a bunch of different podcasts, and then also gives them the
option to choose their own by pasting in a URL.

### EpisodesActivity

EpisodesActivity is the screen users get taken to after they have selected the
podcast. When this screen is first opened it makes a **network request** to the
selected podcasts RSS feed URL, and then parses it into a Podcast class. Once
it has the information in a Podcast class it loads up the `RecyclerView` with
episodes and sets the title, description, and title image for the screen.

### PlayerActivity

PlayerActivity is where the actual listening of the podcast happens. It is
passed the information of the podcast episode from the `EpisodesActivity` and
uses that to make a **network request** to fetch the podcast MP3 and display
the title/description.

### EpisodesAdapter

EpisodesAdapter is a simple helper class that `EpisodesActivity` uses to map
episodes onto elements in the episode selection recycler list.

### Podcast

Our Podcast class provides a standardised data structure for our app to talk
about podcasts and episodes with. When we were first working on the app we were
always passing around ArrayLists of strings for different things, so we
designed some classes to convey all the information in these lists much more
neatly.

```
class Podcast {
  name string
  description string
  imageURL string

  episodes []Episode
}
```

```
class Episode {
  title string
  description string
  imageURL string
  mp3URL string
}
```

It's also responsible for the requesting the podcast feed and then parsing it
into the structures presented above.
