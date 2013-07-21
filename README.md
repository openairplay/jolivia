jolivia
=======

A Java application/library implementation of the DMAP family (DAAP, DACP, DPAP) and RAOP with Guice + Jetty + Zeroconf/Bonojur (through [jmDNS](http://sourceforge.net/projects/jmdns/)). The functionality is planned to resemble what is provided by eg. [dmapd](http://www.flyn.org/projects/dmapd/index.html). It should however be thought of as an integration library, not a final application itself. jolivia is scoped to support the following proprietary protocols:

 - Digital Media Access Protocol (DMAP)
 - Digital Audio Access Protocol (DAAP)
 - Digital Photo Access Protocol (DPAP)
 - Digital Audio Control Protocol (DACP)
 - Remote Audio Output Protocol (RAOP)

## Q/A (How do I ... -)

##### Q: How do I use the DACP feature?
##### A:
I'll just give you a brief introduction to how the DACP protocol works (you can read more on [http://jsharkey.org/blog/2009/06/21/itunes-dacp-pairing-hash-is-broken/](http://jsharkey.org/blog/2009/06/21/itunes-dacp-pairing-hash-is-broken/), [http://dacp.jsharkey.org/](http://dacp.jsharkey.org/), [http://jinxidoru.blogspot.dk/2009/06/itunes-remote-pairing-code.html](http://jinxidoru.blogspot.dk/2009/06/itunes-remote-pairing-code.html) and [http://www.awilco.net/doku/dacp](http://www.awilco.net/doku/dacp)).

1. The client side of DACP announces itself through [Bonjour](http://en.wikipedia.org/wiki/Bonjour_(software) ). This is done by Jolivia
2. The announcement is 'caught/read' by iTunes. At this point you should be able to see the 'like to pair?'-button in iTunes
3. iTunes makes a request to the webservice that the client is required to implement. This service is implemented by Jolivia.
4. The service responds with a OK/NotOK if the correct pairing code is submitted and a negotiation key is stored in both Jolivia and iTunes. This is implemented by Jolivia.
5. iTunes announces itself through Bonjour as paired.
6. Joliva detects the iTunes announcement. If the iTunes library/user is detected as a paired iTunes, Jolivia initiates a session and calls the registered ClientSessionListener that is registered in the constuctor of Jolivia.

The session is your 'remote control' instance. On a session you can do the remote control stuff or eg. traverse the library. See the following code example:

		// As soon as you have entered the pairing code (defaults to '1337') in iTunes the
		// registerNewSession will be invoked and the pairing will be stored in
		// a local db file and in iTunes as well. Clear the pairing in iTunes by
		// clearing all remotes in iTunes as usual. Clear the pairing in Jolivia
		// by deleting the db
		// file. Once paired every time you start iTunes this method will be
		// called. Every time the iTunes instance is
		// closed the tearDownSession will be invoked.
		new Jolivia(new IClientSessionListener() {

			private Session session;

			@Override
			public void tearDownSession(String server, int port) {
				// Maybe do some clean up?
				try {
					session.logout();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			@SuppressWarnings("unused")
			@Override
			public void registerNewSession(Session session) throws Exception {

				// Showcase on some actions you can do on a session ...
				// ////////////////////////////////////////
				this.session = session;

				// getUpdateBlocking blocks until an event happens in iTunes -
				// eg. pressing play, pause, etc. ...
				UpdateResponse response = this.session.getUpdateBlocking();

				Database itunesDatabase = this.session.getDatabase();

				// Get all playlists. For now the playlists only contains the
				// master playlist. This is to be expanded
				Collection<Container> playlists = itunesDatabase
						.getContainers();

				// Traverse the library for eg. all tracks
				for (SongArtist artist : this.session.getLibrary()
						.getAllArtists().getBrowseArtistListing()
						.getSongArtists()) {
					System.out.println(artist.getValue());
				}

				// Extract information from a generic listing
				for (ListingItem item : this.session.getLibrary()
						.getAllTracks().getListing().getListingItems()) {
					System.out.println(item.getSpecificChunk(SongAlbum.class)
							.getValue());
					System.out.println(item.getSpecificChunk(SongArtist.class)
							.getValue());
					System.out.println(item.getSpecificChunk(SongTime.class)
							.getValue());
					System.out.println(item.getSpecificChunk(
							SongTrackNumber.class).getValue());
					System.out.println(item.getSpecificChunk(
							SongUserRating.class).getValue());
					System.out.println(item.getSpecificChunk(ItemName.class)
							.getValue());
					System.out.println(item.getSpecificChunk(ItemKind.class)
							.getValue());
					System.out.println(item.getSpecificChunk(ItemId.class)
							.getValue());
				}

				// Showcase on some actions you can do on speakers ...
				// ////////////////////////////////////////
				RemoteControl remoteControl = this.session.getRemoteControl();
				// Set min volume
				remoteControl.setVolume(0);
				// Set max volume
				remoteControl.setVolume(100);
				// Get the master volume
				remoteControl.getMasterVolume();

				// Get all speakers visible to iTunes instance
				Collection<Speaker> speakers = remoteControl.getSpeakers();

				// Mark all speakers active meaning they are prepared for being
				// used for the iTunes instance
				for (Speaker s : speakers) {
					s.setActive(true);
				}
				// Assign all the active speakers to the iTunes instance. This
				// means that all the speakers will now be used for output
				remoteControl.setSpeakers(speakers);

				// Change the volume individually on each speaker
				speakers = remoteControl.getSpeakers();
				for (Speaker s : speakers) {
					remoteControl.setSpeakerVolume(s.getId(), 60, 50, 40, 30,
							100);
				}
			}
		});
	}

## Current functionality ##

 * DAAP share as provided by iTunes including Zeroconf service discovery/publication.
 * DACP pairing and remote control functions. Jolivia implements serverside and clientside, meaning that you can use Jolivia for remote control but you can also use eg. Apple Remote against it.
 * RAOP Streaming aka. Airport Express emulation.
 * DPAP share.

## Planned functionality ##

 * RAOP client - could be implemented as in [RPlay](https://github.com/bencall/RPlay), [AirReceiver](https://github.com/fgp/AirReceiver), [AP4J-Player](https://github.com/carsonmcdonald/AP4J-Player), [qtunes](https://launchpad.net/qtunes) or [JAirPort](https://github.com/froks/JAirPort).

## Far future functionality ##
 * DLNA/DMAP Gateway translation.

## Inspiration
This project has found great inspiration in many projects and the people behind them, among those are the following:

 - [TunesRemote+](http://code.google.com/p/tunesremote-plus/)
 - [TunesRemote SE](http://code.google.com/p/tunesremote-se/)
 - [jems - Java Extensible Media Server](http://code.google.com/p/jems/)
 - [ytrack](http://code.google.com/p/ytrack/)
 - [RPlay](https://github.com/bencall/RPlay)
 - [AirReceiver](https://github.com/fgp/AirReceiver)
 - [AP4J-Player](https://github.com/carsonmcdonald/AP4J-Player)
 - [JAirPort](https://github.com/froks/JAirPort)
 - [rkapsi/daap](https://github.com/rkapsi/daap)
 - [qtunes](https://launchpad.net/qtunes)

This project is licensed under the license presented in the license.txt file. Anyone who uses Jolivia in any kind of software - being commercial or not must notify the author and submit a comment on this project site stating its usage and purpose.

Copyright 2013 [Jens Kristian Villadsen](http://www.genuswillehadus.net). 
