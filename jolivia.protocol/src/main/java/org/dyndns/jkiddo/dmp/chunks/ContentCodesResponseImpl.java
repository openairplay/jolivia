/*******************************************************************************
 * Copyright (c) 2013 Jens Kristian Villadsen.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     Jens Kristian Villadsen - Lead developer, owner and creator
 ******************************************************************************/
/*
 * Digital Audio Access Protocol (DAAP) Library
 * Copyright (C) 2004-2010 Roger Kapsi
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.dyndns.jkiddo.dmp.chunks;

import org.dyndns.jkiddo.dmp.chunks.media.ContentCodesResponse;
import org.dyndns.jkiddo.dmp.chunks.media.Status;

public final class ContentCodesResponseImpl extends ContentCodesResponse
{
	public ContentCodesResponseImpl()
	{
		super();
		add(new Status(200));
		add(new ContentCode(0x6D736175, "dmap.authenticationmethod", 1)); // msau
		add(new ContentCode(0x6D736173, "dmap.authenticationschemes", 1)); // msas
		add(new ContentCode(0x6D62636C, "dmap.bag", 12)); // mbcl
		add(new ContentCode(0x6162706C, "daap.baseplaylist", 1)); // abpl
		add(new ContentCode(0x6162616C, "daap.browsealbumlisting", 12)); // abal
		add(new ContentCode(0x61626172, "daap.browseartistlisting", 12)); // abar
		add(new ContentCode(0x61626370, "daap.browsecomposerlisting", 12)); // abcp
		add(new ContentCode(0x6162676E, "daap.browsegenrelisting", 12)); // abgn
		add(new ContentCode(0x6D636F6E, "dmap.container", 12)); // mcon
		add(new ContentCode(0x6D637463, "dmap.containercount", 5)); // mctc
		add(new ContentCode(0x6D637469, "dmap.containeritemid", 5)); // mcti
		add(new ContentCode(0x6D636E61, "dmap.contentcodesname", 9)); // mcna
		add(new ContentCode(0x6D636E6D, "dmap.contentcodesnumber", 5)); // mcnm
		add(new ContentCode(0x6D636372, "dmap.contentcodesresponse", 12)); // mccr
		add(new ContentCode(0x6D637479, "dmap.contentcodestype", 3)); // mcty
		add(new ContentCode(0x6170726F, "daap.protocolversion", 11)); // apro
		add(new ContentCode(0x6162726F, "daap.databasebrowse", 12)); // abro
		add(new ContentCode(0x6D736463, "dmap.databasescount", 5)); // msdc
		add(new ContentCode(0x61706C79, "daap.databaseplaylists", 12)); // aply
		add(new ContentCode(0x61646273, "daap.databasesongs", 12)); // adbs
		add(new ContentCode(0x6D75646C, "dmap.deletedidlisting", 12)); // mudl
		add(new ContentCode(0x6D64636C, "dmap.dictionary", 12)); // mdcl
		add(new ContentCode(0x6D70726F, "dmap.protocolversion", 11)); // mpro
		add(new ContentCode(0x668D6368, "dmap.haschildcontainers", 1)); // f?ch
		add(new ContentCode(0x61654856, "com.apple.itunes.has-video", 1)); // aeHV
		add(new ContentCode(0x6D696D63, "dmap.itemcount", 5)); // mimc
		add(new ContentCode(0x6D696964, "dmap.itemid", 5)); // miid
		add(new ContentCode(0x6D696B64, "dmap.itemkind", 1)); // mikd
		add(new ContentCode(0x6D696E6D, "dmap.itemname", 9)); // minm
		add(new ContentCode(0x61654149, "com.apple.itunes.itms-artistid", 5)); // aeAI
		add(new ContentCode(0x61654349, "com.apple.itunes.itms-composerid", 5)); // aeCI
		add(new ContentCode(0x61654749, "com.apple.itunes.itms-genreid", 5)); // aeGI
		add(new ContentCode(0x61655049, "com.apple.itunes.itms-playlistid", 5)); // aePI
		add(new ContentCode(0x61655349, "com.apple.itunes.itms-songid", 5)); // aeSI
		add(new ContentCode(0x61655346, "com.apple.itunes.itms-storefrontid", 5)); // aeSF
		add(new ContentCode(0x6D6C636C, "dmap.listing", 12)); // mlcl
		add(new ContentCode(0x6D6C6974, "dmap.listingitem", 12)); // mlit
		add(new ContentCode(0x6D736C72, "dmap.loginrequired", 1)); // mslr
		add(new ContentCode(0x6D6C6F67, "dmap.loginresponse", 12)); // mlog
		add(new ContentCode(0x61655356, "com.apple.itunes.music-sharing-version", 5)); // aeSV
		add(new ContentCode(0x61654E56, "com.apple.itunes.norm-volume", 5)); // aeNV
		add(new ContentCode(0x6D70636F, "dmap.parentcontainerid", 5)); // mpco
		add(new ContentCode(0x6D706572, "dmap.persistentid", 7)); // mper
		add(new ContentCode(0x6170726D, "daap.playlistrepeatmode", 1)); // aprm
		add(new ContentCode(0x6170736D, "daap.playlistshufflemode", 1)); // apsm
		add(new ContentCode(0x6170736F, "daap.playlistsongs", 12)); // apso
		add(new ContentCode(0x61655043, "com.apple.itunes.is-podcast", 1)); // aePC
		add(new ContentCode(0x61655050, "com.apple.itunes.is-podcast-playlist", 1)); // aePP
		add(new ContentCode(0x61727376, "daap.resolve", 12)); // arsv
		add(new ContentCode(0x61726966, "daap.resolveinfo", 12)); // arif
		add(new ContentCode(0x6D72636F, "dmap.returnedcount", 5)); // mrco
		add(new ContentCode(0x61766462, "daap.serverdatabases", 12)); // avdb
		add(new ContentCode(0x6D737276, "dmap.serverinforesponse", 12)); // msrv
		add(new ContentCode(0x6D757372, "dmap.serverrevision", 5)); // musr
		add(new ContentCode(0x6D6C6964, "dmap.sessionid", 5)); // mlid
		add(new ContentCode(0x61655350, "com.apple.itunes.smart-playlist", 1)); // aeSP
		add(new ContentCode(0x6173616C, "daap.songalbum", 9)); // asal
		add(new ContentCode(0x61736172, "daap.songartist", 9)); // asar
		add(new ContentCode(0x61736274, "daap.songbeatsperminute", 3)); // asbt
		add(new ContentCode(0x61736272, "daap.songbitrate", 3)); // asbr
		add(new ContentCode(0x61736374, "daap.songcategory", 9)); // asct
		add(new ContentCode(0x61736373, "daap.songcodecsubtype", 5)); // ascs
		add(new ContentCode(0x61736364, "daap.songcodectype", 5)); // ascd
		add(new ContentCode(0x6173636D, "daap.songcomment", 9)); // ascm
		add(new ContentCode(0x6173636F, "daap.songcompilation", 1)); // asco
		add(new ContentCode(0x61736370, "daap.songcomposer", 9)); // ascp
		add(new ContentCode(0x6173636E, "daap.songcontentdescription", 9)); // ascn
		add(new ContentCode(0x61736372, "daap.songcontentrating", 1)); // ascr
		add(new ContentCode(0x6173646B, "daap.songdatakind", 1)); // asdk
		add(new ContentCode(0x6173756C, "daap.songdataurl", 9)); // asul
		add(new ContentCode(0x61736461, "daap.songdateadded", 10)); // asda
		add(new ContentCode(0x6173646D, "daap.songdatemodified", 10)); // asdm
		add(new ContentCode(0x61736474, "daap.songdescription", 9)); // asdt
		add(new ContentCode(0x61736462, "daap.songdisabled", 1)); // asdb
		add(new ContentCode(0x61736463, "daap.songdisccount", 3)); // asdc
		add(new ContentCode(0x6173646E, "daap.songdiscnumber", 3)); // asdn
		add(new ContentCode(0x61736571, "daap.songeqpreset", 9)); // aseq
		add(new ContentCode(0x6173666D, "daap.songformat", 9)); // asfm
		add(new ContentCode(0x6173676E, "daap.songgenre", 9)); // asgn
		add(new ContentCode(0x61677270, "daap.songgrouping", 9)); // agrp
		add(new ContentCode(0x61736B79, "daap.songkeywords", 9)); // asky
		add(new ContentCode(0x61736C63, "daap.songlongcontentdescription", 9)); // aslc
		add(new ContentCode(0x61737276, "daap.songrelativevolume", 2)); // asrv
		add(new ContentCode(0x61737372, "daap.songsamplerate", 5)); // assr
		add(new ContentCode(0x6173737A, "daap.songsize", 5)); // assz
		add(new ContentCode(0x61737374, "daap.songstarttime", 5)); // asst
		add(new ContentCode(0x61737370, "daap.songstoptime", 5)); // assp
		add(new ContentCode(0x6173746D, "daap.songtime", 5)); // astm
		add(new ContentCode(0x61737463, "daap.songtrackcount", 3)); // astc
		add(new ContentCode(0x6173746E, "daap.songtracknumber", 3)); // astn
		add(new ContentCode(0x61737572, "daap.songuserrating", 1)); // asur
		add(new ContentCode(0x61737972, "daap.songyear", 3)); // asyr
		add(new ContentCode(0x6D74636F, "dmap.specifiedtotalcount", 5)); // mtco
		add(new ContentCode(0x6D737474, "dmap.status", 5)); // mstt
		add(new ContentCode(0x6D737473, "dmap.statusstring", 9)); // msts
		add(new ContentCode(0x6D73616C, "dmap.supportsautologout", 1)); // msal
		add(new ContentCode(0x6D736272, "dmap.supportsbrowse", 1)); // msbr
		add(new ContentCode(0x6D736578, "dmap.supportsextensions", 1)); // msex
		add(new ContentCode(0x6D736978, "dmap.supportsindex", 1)); // msix
		add(new ContentCode(0x6D737069, "dmap.supportspersistentids", 1)); // mspi
		add(new ContentCode(0x6D737179, "dmap.supportsquery", 1)); // msqy
		add(new ContentCode(0x6D737273, "dmap.supportsresolve", 1)); // msrs
		add(new ContentCode(0x6D737570, "dmap.supportsupdate", 1)); // msup
		add(new ContentCode(0x6D73746D, "dmap.timeoutinterval", 5)); // mstm
		add(new ContentCode(0x6D757064, "dmap.updateresponse", 12)); // mupd
		add(new ContentCode(0x6D757479, "dmap.updatetype", 1)); // muty
	}
}
