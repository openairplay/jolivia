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

package org.dyndns.jkiddo.dmap.chunks;

import java.util.HashMap;
import java.util.Map;

import org.dyndns.jkiddo.dmap.DmapUtil;

public final class ChunkFactory
{

	private final Map<Integer, Class<? extends Chunk>> map = new HashMap<Integer, Class<? extends Chunk>>();

	public ChunkFactory()
	{
		map.put(new Integer(0x61654373), org.dyndns.jkiddo.dmap.chunks.com.apple.itunes.ArtworkChecksum.class); // aeCs
		map.put(new Integer(0x6D736175), org.dyndns.jkiddo.dmap.chunks.dmap.AuthenticationMethod.class); // msau
		map.put(new Integer(0x6D736173), org.dyndns.jkiddo.dmap.chunks.dmap.AuthenticationSchemes.class); // msas
		map.put(new Integer(0x6D62636C), org.dyndns.jkiddo.dmap.chunks.dmap.Bag.class); // mbcl
		map.put(new Integer(0x6162706C), org.dyndns.jkiddo.dmap.chunks.daap.BasePlaylist.class); // abpl
		map.put(new Integer(0x6162616C), org.dyndns.jkiddo.dmap.chunks.daap.BrowseAlbumListing.class); // abal
		map.put(new Integer(0x61626172), org.dyndns.jkiddo.dmap.chunks.daap.BrowseArtistListing.class); // abar
		map.put(new Integer(0x61626370), org.dyndns.jkiddo.dmap.chunks.daap.BrowseComposerListing.class); // abcp
		map.put(new Integer(0x6162676E), org.dyndns.jkiddo.dmap.chunks.daap.BrowseGenreListing.class); // abgn
		map.put(new Integer(0x6D636F6E), org.dyndns.jkiddo.dmap.chunks.dmap.Container.class); // mcon
		map.put(new Integer(0x6D637463), org.dyndns.jkiddo.dmap.chunks.dmap.ContainerCount.class); // mctc
		map.put(new Integer(0x6D637469), org.dyndns.jkiddo.dmap.chunks.dmap.ContainerItemId.class); // mcti
		map.put(new Integer(0x6D636E61), org.dyndns.jkiddo.dmap.chunks.dmap.ContentCodesName.class); // mcna
		map.put(new Integer(0x6D636E6D), org.dyndns.jkiddo.dmap.chunks.dmap.ContentCodesNumber.class); // mcnm
		map.put(new Integer(0x6D636372), org.dyndns.jkiddo.dmap.chunks.dmap.ContentCodesResponse.class); // mccr
		map.put(new Integer(0x6D637479), org.dyndns.jkiddo.dmap.chunks.dmap.ContentCodesType.class); // mcty
		map.put(new Integer(0x6170726F), org.dyndns.jkiddo.dmap.chunks.daap.DaapProtocolVersion.class); // apro
		map.put(new Integer(0x6162726F), org.dyndns.jkiddo.dmap.chunks.daap.DatabaseBrowse.class); // abro
		map.put(new Integer(0x6D736463), org.dyndns.jkiddo.dmap.chunks.dmap.DatabaseCount.class); // msdc
		map.put(new Integer(0x61706C79), org.dyndns.jkiddo.dmap.chunks.daap.DatabasePlaylists.class); // aply
		map.put(new Integer(0x6D64626B), org.dyndns.jkiddo.dmap.chunks.dmap.DatabaseShareType.class); // mdbk
		map.put(new Integer(0x61646273), org.dyndns.jkiddo.dmap.chunks.daap.DatabaseSongs.class); // adbs
		map.put(new Integer(0x6D75646C), org.dyndns.jkiddo.dmap.chunks.dmap.DeletedIdListing.class); // mudl
		map.put(new Integer(0x6D64636C), org.dyndns.jkiddo.dmap.chunks.dmap.Dictionary.class); // mdcl
		map.put(new Integer(0x6D70726F), org.dyndns.jkiddo.dmap.chunks.dmap.DmapProtocolVersion.class); // mpro
		map.put(new Integer(0x6D656473), org.dyndns.jkiddo.dmap.chunks.dmap.EditCommandSupported.class); // meds
		map.put(new Integer(0x61654D6B), org.dyndns.jkiddo.dmap.chunks.com.apple.itunes.EMediaKind.class); // aeMk
		map.put(new Integer(0x668D6368), org.dyndns.jkiddo.dmap.chunks.dmap.HasChildContainers.class); // f?ch
		map.put(new Integer(0x61654856), org.dyndns.jkiddo.dmap.chunks.com.apple.itunes.HasVideo.class); // aeHV
		map.put(new Integer(0x6D696D63), org.dyndns.jkiddo.dmap.chunks.dmap.ItemCount.class); // mimc
		map.put(new Integer(0x6D696964), org.dyndns.jkiddo.dmap.chunks.dmap.ItemId.class); // miid
		map.put(new Integer(0x6D696B64), org.dyndns.jkiddo.dmap.chunks.dmap.ItemKind.class); // mikd
		map.put(new Integer(0x6D696E6D), org.dyndns.jkiddo.dmap.chunks.dmap.ItemName.class); // minm
		map.put(new Integer(0x61654149), org.dyndns.jkiddo.dmap.chunks.com.apple.itunes.ITMSArtistId.class); // aeAI
		map.put(new Integer(0x61654349), org.dyndns.jkiddo.dmap.chunks.com.apple.itunes.ITMSComposerId.class); // aeCI
		map.put(new Integer(0x61654749), org.dyndns.jkiddo.dmap.chunks.com.apple.itunes.ITMSGenreId.class); // aeGI
		map.put(new Integer(0x61655049), org.dyndns.jkiddo.dmap.chunks.com.apple.itunes.ITMSPlaylistId.class); // aePI
		map.put(new Integer(0x61655349), org.dyndns.jkiddo.dmap.chunks.com.apple.itunes.ITMSSongId.class); // aeSI
		map.put(new Integer(0x61655346), org.dyndns.jkiddo.dmap.chunks.com.apple.itunes.ITMSStorefrontId.class); // aeSF
		map.put(new Integer(0x6D6C636C), org.dyndns.jkiddo.dmap.chunks.dmap.Listing.class); // mlcl
		map.put(new Integer(0x6D6C6974), org.dyndns.jkiddo.dmap.chunks.dmap.ListingItem.class); // mlit
		map.put(new Integer(0x6D736C72), org.dyndns.jkiddo.dmap.chunks.dmap.LoginRequired.class); // mslr
		map.put(new Integer(0x6D6C6F67), org.dyndns.jkiddo.dmap.chunks.dmap.LoginResponse.class); // mlog
		map.put(new Integer(0x61654D4B), org.dyndns.jkiddo.dmap.chunks.com.apple.itunes.MediaKind.class); // aeMK
		map.put(new Integer(0x61655356), org.dyndns.jkiddo.dmap.chunks.com.apple.itunes.MusicSharingVersion.class); // aeSV
		map.put(new Integer(0x61654E56), org.dyndns.jkiddo.dmap.chunks.com.apple.itunes.NormVolume.class); // aeNV
		map.put(new Integer(0x6D70636F), org.dyndns.jkiddo.dmap.chunks.dmap.ParentContainerId.class); // mpco
		map.put(new Integer(0x6D706572), org.dyndns.jkiddo.dmap.chunks.dmap.PersistentId.class); // mper
		map.put(new Integer(0x6170726D), org.dyndns.jkiddo.dmap.chunks.daap.PlaylistRepeatMode.class); // aprm
		map.put(new Integer(0x6170736D), org.dyndns.jkiddo.dmap.chunks.daap.PlaylistShuffleMode.class); // apsm
		map.put(new Integer(0x6170736F), org.dyndns.jkiddo.dmap.chunks.daap.PlaylistSongs.class); // apso
		map.put(new Integer(0x61655043), org.dyndns.jkiddo.dmap.chunks.com.apple.itunes.Podcast.class); // aePC
		map.put(new Integer(0x61655050), org.dyndns.jkiddo.dmap.chunks.com.apple.itunes.PodcastPlaylist.class); // aePP
		map.put(new Integer(0x636D766F), org.dyndns.jkiddo.dmap.chunks.dacp.RelativeVolume.class); // cmvo
		map.put(new Integer(0x6D727072), org.dyndns.jkiddo.dmap.chunks.dmap.RemotePersistentID.class); // mrpr
		map.put(new Integer(0x61727376), org.dyndns.jkiddo.dmap.chunks.daap.Resolve.class); // arsv
		map.put(new Integer(0x61726966), org.dyndns.jkiddo.dmap.chunks.daap.ResolveInfo.class); // arif
		map.put(new Integer(0x6D72636F), org.dyndns.jkiddo.dmap.chunks.dmap.ReturnedCount.class); // mrco
		map.put(new Integer(0x61766462), org.dyndns.jkiddo.dmap.chunks.daap.ServerDatabases.class); // avdb
		map.put(new Integer(0x6D737276), org.dyndns.jkiddo.dmap.chunks.dmap.ServerInfoResponse.class); // msrv
		map.put(new Integer(0x6D757372), org.dyndns.jkiddo.dmap.chunks.dmap.ServerRevision.class); // musr
		map.put(new Integer(0x6D6C6964), org.dyndns.jkiddo.dmap.chunks.dmap.SessionId.class); // mlid
		map.put(new Integer(0x61655350), org.dyndns.jkiddo.dmap.chunks.com.apple.itunes.SmartPlaylist.class); // aeSP
		map.put(new Integer(0x6173616C), org.dyndns.jkiddo.dmap.chunks.daap.SongAlbum.class); // asal
		map.put(new Integer(0x61736172), org.dyndns.jkiddo.dmap.chunks.daap.SongArtist.class); // asar
		map.put(new Integer(0x61736274), org.dyndns.jkiddo.dmap.chunks.daap.SongBeatsPerMinute.class); // asbt
		map.put(new Integer(0x61736272), org.dyndns.jkiddo.dmap.chunks.daap.SongBitrate.class); // asbr
		map.put(new Integer(0x61736374), org.dyndns.jkiddo.dmap.chunks.daap.SongCategory.class); // asct
		map.put(new Integer(0x61736373), org.dyndns.jkiddo.dmap.chunks.daap.SongCodecSubtype.class); // ascs
		map.put(new Integer(0x61736364), org.dyndns.jkiddo.dmap.chunks.daap.SongCodecType.class); // ascd
		map.put(new Integer(0x6173636D), org.dyndns.jkiddo.dmap.chunks.daap.SongComment.class); // ascm
		map.put(new Integer(0x6173636F), org.dyndns.jkiddo.dmap.chunks.daap.SongCompilation.class); // asco
		map.put(new Integer(0x61736370), org.dyndns.jkiddo.dmap.chunks.daap.SongComposer.class); // ascp
		map.put(new Integer(0x6173636E), org.dyndns.jkiddo.dmap.chunks.daap.SongContentDescription.class); // ascn
		map.put(new Integer(0x61736372), org.dyndns.jkiddo.dmap.chunks.daap.SongContentRating.class); // ascr
		map.put(new Integer(0x6173646B), org.dyndns.jkiddo.dmap.chunks.daap.SongDataKind.class); // asdk
		map.put(new Integer(0x6173756C), org.dyndns.jkiddo.dmap.chunks.daap.SongDataUrl.class); // asul
		map.put(new Integer(0x61736461), org.dyndns.jkiddo.dmap.chunks.daap.SongDateAdded.class); // asda
		map.put(new Integer(0x6173646D), org.dyndns.jkiddo.dmap.chunks.daap.SongDateModified.class); // asdm
		map.put(new Integer(0x61736474), org.dyndns.jkiddo.dmap.chunks.daap.SongDescription.class); // asdt
		map.put(new Integer(0x61736462), org.dyndns.jkiddo.dmap.chunks.daap.SongDisabled.class); // asdb
		map.put(new Integer(0x61736463), org.dyndns.jkiddo.dmap.chunks.daap.SongDiscCount.class); // asdc
		map.put(new Integer(0x6173646E), org.dyndns.jkiddo.dmap.chunks.daap.SongDiscNumber.class); // asdn
		map.put(new Integer(0x61736571), org.dyndns.jkiddo.dmap.chunks.daap.SongEqPreset.class); // aseq
		map.put(new Integer(0x6173666D), org.dyndns.jkiddo.dmap.chunks.daap.SongFormat.class); // asfm
		map.put(new Integer(0x6173676E), org.dyndns.jkiddo.dmap.chunks.daap.SongGenre.class); // asgn
		map.put(new Integer(0x61677270), org.dyndns.jkiddo.dmap.chunks.daap.SongGrouping.class); // agrp
		map.put(new Integer(0x61736B79), org.dyndns.jkiddo.dmap.chunks.daap.SongKeywords.class); // asky
		map.put(new Integer(0x61736C63), org.dyndns.jkiddo.dmap.chunks.daap.SongLongDescription.class); // aslc
		map.put(new Integer(0x61737276), org.dyndns.jkiddo.dmap.chunks.daap.SongRelativeVolume.class); // asrv
		map.put(new Integer(0x61737372), org.dyndns.jkiddo.dmap.chunks.daap.SongSampleRate.class); // assr
		map.put(new Integer(0x6173737A), org.dyndns.jkiddo.dmap.chunks.daap.SongSize.class); // assz
		map.put(new Integer(0x61737374), org.dyndns.jkiddo.dmap.chunks.daap.SongStartTime.class); // asst
		map.put(new Integer(0x61737370), org.dyndns.jkiddo.dmap.chunks.daap.SongStopTime.class); // assp
		map.put(new Integer(0x6173746D), org.dyndns.jkiddo.dmap.chunks.daap.SongTime.class); // astm
		map.put(new Integer(0x61737463), org.dyndns.jkiddo.dmap.chunks.daap.SongTrackCount.class); // astc
		map.put(new Integer(0x6173746E), org.dyndns.jkiddo.dmap.chunks.daap.SongTrackNumber.class); // astn
		map.put(new Integer(0x61737572), org.dyndns.jkiddo.dmap.chunks.daap.SongUserRating.class); // asur
		map.put(new Integer(0x61737972), org.dyndns.jkiddo.dmap.chunks.daap.SongYear.class); // asyr
		map.put(new Integer(0x63616961), org.dyndns.jkiddo.dmap.chunks.dacp.SpeakerActive.class); // caia
		map.put(new Integer(0x63617370), org.dyndns.jkiddo.dmap.chunks.dacp.SpeakerList.class); // casp
		map.put(new Integer(0x61655053), org.dyndns.jkiddo.dmap.chunks.com.apple.itunes.SpecialPlaylist.class); // aePS
		map.put(new Integer(0x6D74636F), org.dyndns.jkiddo.dmap.chunks.dmap.SpecifiedTotalCount.class); // mtco
		map.put(new Integer(0x6D737474), org.dyndns.jkiddo.dmap.chunks.dmap.Status.class); // mstt
		map.put(new Integer(0x6D737473), org.dyndns.jkiddo.dmap.chunks.dmap.StatusString.class); // msts
		map.put(new Integer(0x6D73616C), org.dyndns.jkiddo.dmap.chunks.dmap.SupportsAutoLogout.class); // msal
		map.put(new Integer(0x6D736272), org.dyndns.jkiddo.dmap.chunks.dmap.SupportsBrowse.class); // msbr
		map.put(new Integer(0x6D736578), org.dyndns.jkiddo.dmap.chunks.dmap.SupportsExtensions.class); // msex
		map.put(new Integer(0x6D736978), org.dyndns.jkiddo.dmap.chunks.dmap.SupportsIndex.class); // msix
		map.put(new Integer(0x6D737069), org.dyndns.jkiddo.dmap.chunks.dmap.SupportsPersistentIds.class); // mspi
		map.put(new Integer(0x6D737179), org.dyndns.jkiddo.dmap.chunks.dmap.SupportsQuery.class); // msqy
		map.put(new Integer(0x6D737273), org.dyndns.jkiddo.dmap.chunks.dmap.SupportsResolve.class); // msrs
		map.put(new Integer(0x6D737570), org.dyndns.jkiddo.dmap.chunks.dmap.SupportsUpdate.class); // msup
		map.put(new Integer(0x6D73746D), org.dyndns.jkiddo.dmap.chunks.dmap.TimeoutInterval.class); // mstm
		map.put(new Integer(0x636D6774), org.dyndns.jkiddo.dmap.chunks.unknown.UnknownGT.class); // cmgt
		map.put(new Integer(0x6165494D), org.dyndns.jkiddo.dmap.chunks.unknown.UnknownIM.class); // aeIM
		map.put(new Integer(0x6D736D61), org.dyndns.jkiddo.dmap.chunks.unknown.UnknownMA.class); // msma
		map.put(new Integer(0x6165524D), org.dyndns.jkiddo.dmap.chunks.unknown.UnknownRM.class); // aeRM
		map.put(new Integer(0x63617664), org.dyndns.jkiddo.dmap.chunks.unknown.UnknownVD.class); // cavd
		map.put(new Integer(0x6D757064), org.dyndns.jkiddo.dmap.chunks.dmap.UpdateResponse.class); // mupd
		map.put(new Integer(0x6D757479), org.dyndns.jkiddo.dmap.chunks.dmap.UpdateType.class); // muty
		map.put(new Integer(0x636D7374), org.dyndns.jkiddo.dmap.chunks.unknown.UnknownST.class); // cmst
		map.put(new Integer(0x636D7372), org.dyndns.jkiddo.dmap.chunks.dacp.StatusRevision.class); // cmsr
		map.put(new Integer(0x63617073), org.dyndns.jkiddo.dmap.chunks.dacp.PlayStatus.class); // caps
		map.put(new Integer(0x63617368), org.dyndns.jkiddo.dmap.chunks.dacp.ShuffleStatus.class); // cash
		map.put(new Integer(0x63617270), org.dyndns.jkiddo.dmap.chunks.dacp.RepeatStatus.class); // carp
		map.put(new Integer(0x63616673), org.dyndns.jkiddo.dmap.chunks.dacp.FullscreenStatus.class); // cafs
		map.put(new Integer(0x63617673), org.dyndns.jkiddo.dmap.chunks.dacp.VisualizerStatus.class); // cavs
		map.put(new Integer(0x63617663), org.dyndns.jkiddo.dmap.chunks.unknown.UnknownVC.class); // cavc
		map.put(new Integer(0x63616173), org.dyndns.jkiddo.dmap.chunks.unknown.UnknownAS.class); // caas
		map.put(new Integer(0x63616172), org.dyndns.jkiddo.dmap.chunks.unknown.UnknownAR.class); // caar
		map.put(new Integer(0x63616665), org.dyndns.jkiddo.dmap.chunks.unknown.UnknownFE.class); // cafe
		map.put(new Integer(0x63617665), org.dyndns.jkiddo.dmap.chunks.unknown.UnknownVE.class); // cave
		map.put(new Integer(0x63617375), org.dyndns.jkiddo.dmap.chunks.unknown.UnknownSU.class); // casu
		map.put(new Integer(0x63655175), org.dyndns.jkiddo.dmap.chunks.unknown.UnknownQU.class); // ceQu
		map.put(new Integer(0x63616e70), org.dyndns.jkiddo.dmap.chunks.dacp.NowPlaying.class); // canp
		map.put(new Integer(0x63616e6e), org.dyndns.jkiddo.dmap.chunks.dacp.TrackName.class); // cann
		map.put(new Integer(0x63616e61), org.dyndns.jkiddo.dmap.chunks.dacp.TrackArtist.class); // cana
		map.put(new Integer(0x63616e6c), org.dyndns.jkiddo.dmap.chunks.dacp.TrackAlbum.class); // canl
		map.put(new Integer(0x63616e67), org.dyndns.jkiddo.dmap.chunks.dacp.TrackGenre.class); // cang
		map.put(new Integer(0x61736169), org.dyndns.jkiddo.dmap.chunks.daap.SongAlbumId.class); // asai
		map.put(new Integer(0x636d6d6b), org.dyndns.jkiddo.dmap.chunks.unknown.UnknownMK.class); // cmmk
		map.put(new Integer(0x61654773), org.dyndns.jkiddo.dmap.chunks.dmap.GeniusSeed.class); // aeGs
		map.put(new Integer(0x63654753), org.dyndns.jkiddo.dmap.chunks.dacp.GeniusSelectable.class); // ceGs
		map.put(new Integer(0x63617361), org.dyndns.jkiddo.dmap.chunks.unknown.UnknownSA.class); // casa
		map.put(new Integer(0x63616e74), org.dyndns.jkiddo.dmap.chunks.dacp.ProgressRemain.class); // cant
		map.put(new Integer(0x63617374), org.dyndns.jkiddo.dmap.chunks.dacp.ProgressTotal.class); // cant
		map.put(new Integer(0x6d73686c), org.dyndns.jkiddo.dmap.chunks.unknown.UnknownHL.class); // mshl
		map.put(new Integer(0x6d736863), org.dyndns.jkiddo.dmap.chunks.unknown.UnknownHC.class); // mshc
		map.put(new Integer(0x6d736869), org.dyndns.jkiddo.dmap.chunks.unknown.UnknownHI.class); // mshi
		map.put(new Integer(0x6d73686e), org.dyndns.jkiddo.dmap.chunks.unknown.UnknownHN.class); // mshn
		map.put(new Integer(0x6167616c), org.dyndns.jkiddo.dmap.chunks.unknown.UnknownAL.class); // agal
		map.put(new Integer(0x61736161), org.dyndns.jkiddo.dmap.chunks.dacp.AlbumArtist.class); // asaa
		map.put(new Integer(0x61746564), org.dyndns.jkiddo.dmap.chunks.daap.SupportsExtraData.class); // ated
		map.put(new Integer(0x61736772), org.dyndns.jkiddo.dmap.chunks.daap.SupportsGroups.class); // asgr
		map.put(new Integer(0x61737365), org.dyndns.jkiddo.dmap.chunks.unknown.UnknownSE.class); // asse
		map.put(new Integer(0x61654d51), org.dyndns.jkiddo.dmap.chunks.unknown.UnknownMQ.class); // aeMQ
		map.put(new Integer(0x61654652), org.dyndns.jkiddo.dmap.chunks.unknown.UnknownFR.class); // aeFR
		map.put(new Integer(0x61655472), org.dyndns.jkiddo.dmap.chunks.unknown.UnknownTr.class); // aeTr
		map.put(new Integer(0x6165534c), org.dyndns.jkiddo.dmap.chunks.unknown.UnknownSL.class); // aeSL
		map.put(new Integer(0x61655352), org.dyndns.jkiddo.dmap.chunks.unknown.UnknownSR.class); // aeSR
		map.put(new Integer(0x61654650), org.dyndns.jkiddo.dmap.chunks.unknown.UnknownFP.class); // aeFP
		map.put(new Integer(0x61655358), org.dyndns.jkiddo.dmap.chunks.unknown.UnknownSX.class); // aeSX
		map.put(new Integer(0x7070726f), org.dyndns.jkiddo.dmap.chunks.dpap.ProtocolVersion.class); // ppro
		map.put(new Integer(0x6d736564), org.dyndns.jkiddo.dmap.chunks.unknown.Unknowned.class); // msed
		map.put(new Integer(0x6d736d6c), org.dyndns.jkiddo.dmap.chunks.unknown.Unknownml.class); // msml
		map.put(new Integer(0x6d737463), org.dyndns.jkiddo.dmap.chunks.unknown.Unknowntc.class); // mstc
		map.put(new Integer(0x6d73746f), org.dyndns.jkiddo.dmap.chunks.unknown.Unknownto.class); // msto

	}

	public Class<? extends Chunk> getChunkClass(Integer contentCode)
	{
		return map.get(contentCode);
	}

	public Chunk newChunk(int contentCode)
	{
		Class<? extends Chunk> clazz = getChunkClass(new Integer(contentCode));
		try
		{
			return clazz.newInstance();
		}
		catch(Exception err)
		{
			throw new RuntimeException("Content code: " + DmapUtil.toContentCodeString(contentCode) + " not found. Hash is 0x" + Integer.toHexString(new Integer(contentCode)), err);
		}
	}
}
