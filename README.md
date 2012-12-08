jolivia
=======

A Java application/library implementation of the DMAP family (DAAP, DACP, DPAP) and RAOP with Guice + Jetty + Zeroconf/Bonojur (through [jmDNS](http://sourceforge.net/projects/jmdns/)). The functionality is planned to resemble what is provided by eg. [dmapd](http://www.flyn.org/projects/dmapd/index.html).

 - Digital Media Access Protocol (DMAP)
 - Digital Audio Access Protocol (DAAP)
 - Digital Photo Access Protocol (DPAP)
 - Digital Audio Control Protocol (DACP)
 - Remote Audio Output Protocol (RAOP)

## Current functionality ##

 * Ordinary DAAP share as provided by iTunes including Zeroconf service discovery/publication.

## Planned functionality ##

 * Support for DACP and RAOP, effectively making jolivia a Headless OS-independent version of iTunes.
  * DACP could be implemented as in [TunesRemote+](http://code.google.com/p/tunesremote-plus/) or [TunesRemote SE](http://code.google.com/p/tunesremote-se/).
  * RAOP could be implemented as in [RPlay](https://github.com/bencall/RPlay), [AirReceiver](https://github.com/fgp/AirReceiver), [AP4J-Player](https://github.com/carsonmcdonald/AP4J-Player) or [JAirPort](https://github.com/froks/JAirPort).

## Near future functionality ##

 * DPAP implementation.

## Far future functionality ##
 * DLNA/DMAP Gateway translation.
 
This project is licensed under the license presented in the license.txt file with exception to code in the namespace org.ardverk.daap.*, which is licensed under the Apache License 2.0 (LICENSE-2.0.txt)

Copyright 2012 [Jens Kristian Villadsen](http://www.genuswillehadus.net). 