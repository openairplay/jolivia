jolivia
=======

A Java application/library implementation of the DMAP family (DAAP, DACP, DPAP) and RAOP with Guice + Jetty + Zeroconf/Bonojur (through [jmDNS](http://sourceforge.net/projects/jmdns/)). The functionality is planned to resemble what is provided by eg. [dmapd](http://www.flyn.org/projects/dmapd/index.html). It should however be thought of as an integration library, not a final application itself. jolivia is scoped to support the following proprietary protocols:

 - Digital Media Access Protocol (DMAP)
 - Digital Audio Access Protocol (DAAP)
 - Digital Photo Access Protocol (DPAP)
 - Digital Audio Control Protocol (DACP)
 - Remote Audio Output Protocol (RAOP)

## Current functionality ##

 * DAAP share as provided by iTunes including Zeroconf service discovery/publication. (*Have a look at classes implementing the interface org.dyndns.jkiddo.dmap.service.ILibraryResource*)
 * DACP pairing and remote control functions. (*Have a look at classes implementing the interface org.dyndns.jkiddo.dacp.client.IPairingResource and org.dyndns.jkiddo.daap.client.IAutomatedClientSessionCreator*)

## Planned functionality ##

 * Support for RAOP, effectively making jolivia a Headless OS-independent version of iTunes.
  * RAOP could be implemented as in [RPlay](https://github.com/bencall/RPlay), [AirReceiver](https://github.com/fgp/AirReceiver), [AP4J-Player](https://github.com/carsonmcdonald/AP4J-Player) or [JAirPort](https://github.com/froks/JAirPort).
 * DPAP implementation (ongoing ... ).

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
 - [JAirPort](https://github.com/froks/JAirPort).

This project is licensed under the license presented in the license.txt file.

Copyright 2013 [Jens Kristian Villadsen](http://www.genuswillehadus.net). 