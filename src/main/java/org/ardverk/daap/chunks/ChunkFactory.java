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

package org.ardverk.daap.chunks;

import java.util.HashMap;
import java.util.Map;

import org.ardverk.daap.DaapUtil;

public final class ChunkFactory
{

	private final Map<Integer, Class<? extends Chunk>> map = new HashMap<Integer, Class<? extends Chunk>>();

	public ChunkFactory()
	{
		map.put(new Integer(0x61654373), org.ardverk.daap.chunks.impl.ArtworkChecksum.class); // aeCs
		map.put(new Integer(0x6D736175), org.ardverk.daap.chunks.impl.AuthenticationMethod.class); // msau
		map.put(new Integer(0x6D736173), org.ardverk.daap.chunks.impl.AuthenticationSchemes.class); // msas
		map.put(new Integer(0x6D62636C), org.ardverk.daap.chunks.impl.Bag.class); // mbcl
		map.put(new Integer(0x6162706C), org.ardverk.daap.chunks.impl.BasePlaylist.class); // abpl
		map.put(new Integer(0x6162616C), org.ardverk.daap.chunks.impl.BrowseAlbumListing.class); // abal
		map.put(new Integer(0x61626172), org.ardverk.daap.chunks.impl.BrowseArtistListing.class); // abar
		map.put(new Integer(0x61626370), org.ardverk.daap.chunks.impl.BrowseComposerListing.class); // abcp
		map.put(new Integer(0x6162676E), org.ardverk.daap.chunks.impl.BrowseGenreListing.class); // abgn
		map.put(new Integer(0x6D636F6E), org.ardverk.daap.chunks.impl.Container.class); // mcon
		map.put(new Integer(0x6D637463), org.ardverk.daap.chunks.impl.ContainerCount.class); // mctc
		map.put(new Integer(0x6D637469), org.ardverk.daap.chunks.impl.ContainerItemId.class); // mcti
		map.put(new Integer(0x6D636E61), org.ardverk.daap.chunks.impl.ContentCodesName.class); // mcna
		map.put(new Integer(0x6D636E6D), org.ardverk.daap.chunks.impl.ContentCodesNumber.class); // mcnm
		map.put(new Integer(0x6D636372), org.ardverk.daap.chunks.impl.ContentCodesResponse.class); // mccr
		map.put(new Integer(0x6D637479), org.ardverk.daap.chunks.impl.ContentCodesType.class); // mcty
		map.put(new Integer(0x6170726F), org.ardverk.daap.chunks.impl.DaapProtocolVersion.class); // apro
		map.put(new Integer(0x6162726F), org.ardverk.daap.chunks.impl.DatabaseBrowse.class); // abro
		map.put(new Integer(0x6D736463), org.ardverk.daap.chunks.impl.DatabaseCount.class); // msdc
		map.put(new Integer(0x61706C79), org.ardverk.daap.chunks.impl.DatabasePlaylists.class); // aply
		map.put(new Integer(0x6D64626B), org.ardverk.daap.chunks.impl.DatabaseShareType.class); // mdbk
		map.put(new Integer(0x61646273), org.ardverk.daap.chunks.impl.DatabaseSongs.class); // adbs
		map.put(new Integer(0x6D75646C), org.ardverk.daap.chunks.impl.DeletedIdListing.class); // mudl
		map.put(new Integer(0x6D64636C), org.ardverk.daap.chunks.impl.Dictionary.class); // mdcl
		map.put(new Integer(0x6D70726F), org.ardverk.daap.chunks.impl.DmapProtocolVersion.class); // mpro
		map.put(new Integer(0x6D656473), org.ardverk.daap.chunks.impl.EditCommandSupported.class); // meds
		map.put(new Integer(0x61654D6B), org.ardverk.daap.chunks.impl.EMediaKind.class); // aeMk
		map.put(new Integer(0x668D6368), org.ardverk.daap.chunks.impl.HasChildContainers.class); // f?ch
		map.put(new Integer(0x61654856), org.ardverk.daap.chunks.impl.HasVideo.class); // aeHV
		map.put(new Integer(0x6D696D63), org.ardverk.daap.chunks.impl.ItemCount.class); // mimc
		map.put(new Integer(0x6D696964), org.ardverk.daap.chunks.impl.ItemId.class); // miid
		map.put(new Integer(0x6D696B64), org.ardverk.daap.chunks.impl.ItemKind.class); // mikd
		map.put(new Integer(0x6D696E6D), org.ardverk.daap.chunks.impl.ItemName.class); // minm
		map.put(new Integer(0x61654149), org.ardverk.daap.chunks.impl.ITMSArtistId.class); // aeAI
		map.put(new Integer(0x61654349), org.ardverk.daap.chunks.impl.ITMSComposerId.class); // aeCI
		map.put(new Integer(0x61654749), org.ardverk.daap.chunks.impl.ITMSGenreId.class); // aeGI
		map.put(new Integer(0x61655049), org.ardverk.daap.chunks.impl.ITMSPlaylistId.class); // aePI
		map.put(new Integer(0x61655349), org.ardverk.daap.chunks.impl.ITMSSongId.class); // aeSI
		map.put(new Integer(0x61655346), org.ardverk.daap.chunks.impl.ITMSStorefrontId.class); // aeSF
		map.put(new Integer(0x6D6C636C), org.ardverk.daap.chunks.impl.Listing.class); // mlcl
		map.put(new Integer(0x6D6C6974), org.ardverk.daap.chunks.impl.ListingItem.class); // mlit
		map.put(new Integer(0x6D736C72), org.ardverk.daap.chunks.impl.LoginRequired.class); // mslr
		map.put(new Integer(0x6D6C6F67), org.ardverk.daap.chunks.impl.LoginResponse.class); // mlog
		map.put(new Integer(0x61654D4B), org.ardverk.daap.chunks.impl.MediaKind.class); // aeMK
		map.put(new Integer(0x61655356), org.ardverk.daap.chunks.impl.MusicSharingVersion.class); // aeSV
		map.put(new Integer(0x61654E56), org.ardverk.daap.chunks.impl.NormVolume.class); // aeNV
		map.put(new Integer(0x6D70636F), org.ardverk.daap.chunks.impl.ParentContainerId.class); // mpco
		map.put(new Integer(0x6D706572), org.ardverk.daap.chunks.impl.PersistentId.class); // mper
		map.put(new Integer(0x6170726D), org.ardverk.daap.chunks.impl.PlaylistRepeatMode.class); // aprm
		map.put(new Integer(0x6170736D), org.ardverk.daap.chunks.impl.PlaylistShuffleMode.class); // apsm
		map.put(new Integer(0x6170736F), org.ardverk.daap.chunks.impl.PlaylistSongs.class); // apso
		map.put(new Integer(0x61655043), org.ardverk.daap.chunks.impl.Podcast.class); // aePC
		map.put(new Integer(0x61655050), org.ardverk.daap.chunks.impl.PodcastPlaylist.class); // aePP
		map.put(new Integer(0x636D766F), org.ardverk.daap.chunks.impl.RelativeVolume.class); // cmvo
		map.put(new Integer(0x6D727072), org.ardverk.daap.chunks.impl.RemotePersistentID.class); // mrpr
		map.put(new Integer(0x61727376), org.ardverk.daap.chunks.impl.Resolve.class); // arsv
		map.put(new Integer(0x61726966), org.ardverk.daap.chunks.impl.ResolveInfo.class); // arif
		map.put(new Integer(0x6D72636F), org.ardverk.daap.chunks.impl.ReturnedCount.class); // mrco
		map.put(new Integer(0x61766462), org.ardverk.daap.chunks.impl.daap.ServerDatabases.class); // avdb
		map.put(new Integer(0x6D737276), org.ardverk.daap.chunks.impl.dmap.ServerInfoResponse.class); // msrv
		map.put(new Integer(0x6D757372), org.ardverk.daap.chunks.impl.dmap.ServerRevision.class); // musr
		map.put(new Integer(0x6D6C6964), org.ardverk.daap.chunks.impl.dmap.SessionId.class); // mlid
		map.put(new Integer(0x61655350), org.ardverk.daap.chunks.impl.com.apple.itunes.SmartPlaylist.class); // aeSP
		map.put(new Integer(0x6173616C), org.ardverk.daap.chunks.impl.daap.SongAlbum.class); // asal
		map.put(new Integer(0x61736172), org.ardverk.daap.chunks.impl.daap.SongArtist.class); // asar
		map.put(new Integer(0x61736274), org.ardverk.daap.chunks.impl.daap.SongBeatsPerMinute.class); // asbt
		map.put(new Integer(0x61736272), org.ardverk.daap.chunks.impl.daap.SongBitrate.class); // asbr
		map.put(new Integer(0x61736374), org.ardverk.daap.chunks.impl.daap.SongCategory.class); // asct
		map.put(new Integer(0x61736373), org.ardverk.daap.chunks.impl.daap.SongCodecSubtype.class); // ascs
		map.put(new Integer(0x61736364), org.ardverk.daap.chunks.impl.daap.SongCodecType.class); // ascd
		map.put(new Integer(0x6173636D), org.ardverk.daap.chunks.impl.daap.SongComment.class); // ascm
		map.put(new Integer(0x6173636F), org.ardverk.daap.chunks.impl.daap.SongCompilation.class); // asco
		map.put(new Integer(0x61736370), org.ardverk.daap.chunks.impl.daap.SongComposer.class); // ascp
		map.put(new Integer(0x6173636E), org.ardverk.daap.chunks.impl.daap.SongContentDescription.class); // ascn
		map.put(new Integer(0x61736372), org.ardverk.daap.chunks.impl.daap.SongContentRating.class); // ascr
		map.put(new Integer(0x6173646B), org.ardverk.daap.chunks.impl.daap.SongDataKind.class); // asdk
		map.put(new Integer(0x6173756C), org.ardverk.daap.chunks.impl.daap.SongDataUrl.class); // asul
		map.put(new Integer(0x61736461), org.ardverk.daap.chunks.impl.daap.SongDateAdded.class); // asda
		map.put(new Integer(0x6173646D), org.ardverk.daap.chunks.impl.daap.SongDateModified.class); // asdm
		map.put(new Integer(0x61736474), org.ardverk.daap.chunks.impl.daap.SongDescription.class); // asdt
		map.put(new Integer(0x61736462), org.ardverk.daap.chunks.impl.daap.SongDisabled.class); // asdb
		map.put(new Integer(0x61736463), org.ardverk.daap.chunks.impl.daap.SongDiscCount.class); // asdc
		map.put(new Integer(0x6173646E), org.ardverk.daap.chunks.impl.daap.SongDiscNumber.class); // asdn
		map.put(new Integer(0x61736571), org.ardverk.daap.chunks.impl.daap.SongEqPreset.class); // aseq
		map.put(new Integer(0x6173666D), org.ardverk.daap.chunks.impl.daap.SongFormat.class); // asfm
		map.put(new Integer(0x6173676E), org.ardverk.daap.chunks.impl.daap.SongGenre.class); // asgn
		map.put(new Integer(0x61677270), org.ardverk.daap.chunks.impl.daap.SongGrouping.class); // agrp
		map.put(new Integer(0x61736B79), org.ardverk.daap.chunks.impl.daap.SongKeywords.class); // asky
		map.put(new Integer(0x61736C63), org.ardverk.daap.chunks.impl.daap.SongLongDescription.class); // aslc
		map.put(new Integer(0x61737276), org.ardverk.daap.chunks.impl.daap.SongRelativeVolume.class); // asrv
		map.put(new Integer(0x61737372), org.ardverk.daap.chunks.impl.daap.SongSampleRate.class); // assr
		map.put(new Integer(0x6173737A), org.ardverk.daap.chunks.impl.daap.SongSize.class); // assz
		map.put(new Integer(0x61737374), org.ardverk.daap.chunks.impl.daap.SongStartTime.class); // asst
		map.put(new Integer(0x61737370), org.ardverk.daap.chunks.impl.daap.SongStopTime.class); // assp
		map.put(new Integer(0x6173746D), org.ardverk.daap.chunks.impl.daap.SongTime.class); // astm
		map.put(new Integer(0x61737463), org.ardverk.daap.chunks.impl.daap.SongTrackCount.class); // astc
		map.put(new Integer(0x6173746E), org.ardverk.daap.chunks.impl.daap.SongTrackNumber.class); // astn
		map.put(new Integer(0x61737572), org.ardverk.daap.chunks.impl.daap.SongUserRating.class); // asur
		map.put(new Integer(0x61737972), org.ardverk.daap.chunks.impl.daap.SongYear.class); // asyr
		map.put(new Integer(0x63616961), org.ardverk.daap.chunks.impl.unknown.SpeakerActive.class); // caia
		map.put(new Integer(0x63617370), org.ardverk.daap.chunks.impl.unknown.SpeakerList.class); // casp
		map.put(new Integer(0x61655053), org.ardverk.daap.chunks.impl.com.apple.itunes.SpecialPlaylist.class); // aePS
		map.put(new Integer(0x6D74636F), org.ardverk.daap.chunks.impl.dmap.SpecifiedTotalCount.class); // mtco
		map.put(new Integer(0x6D737474), org.ardverk.daap.chunks.impl.dmap.Status.class); // mstt
		map.put(new Integer(0x6D737473), org.ardverk.daap.chunks.impl.dmap.StatusString.class); // msts
		map.put(new Integer(0x6D73616C), org.ardverk.daap.chunks.impl.dmap.SupportsAutoLogout.class); // msal
		map.put(new Integer(0x6D736272), org.ardverk.daap.chunks.impl.dmap.SupportsBrowse.class); // msbr
		map.put(new Integer(0x6D736578), org.ardverk.daap.chunks.impl.dmap.SupportsExtensions.class); // msex
		map.put(new Integer(0x6D736978), org.ardverk.daap.chunks.impl.dmap.SupportsIndex.class); // msix
		map.put(new Integer(0x6D737069), org.ardverk.daap.chunks.impl.dmap.SupportsPersistentIds.class); // mspi
		map.put(new Integer(0x6D737179), org.ardverk.daap.chunks.impl.dmap.SupportsQuery.class); // msqy
		map.put(new Integer(0x6D737273), org.ardverk.daap.chunks.impl.dmap.SupportsResolve.class); // msrs
		map.put(new Integer(0x6D737570), org.ardverk.daap.chunks.impl.dmap.SupportsUpdate.class); // msup
		map.put(new Integer(0x6D73746D), org.ardverk.daap.chunks.impl.dmap.TimeoutInterval.class); // mstm
		map.put(new Integer(0x636D6774), org.ardverk.daap.chunks.impl.unknown.UnknownGT.class); // cmgt
		map.put(new Integer(0x6165494D), org.ardverk.daap.chunks.impl.unknown.UnknownIM.class); // aeIM
		map.put(new Integer(0x6D736D61), org.ardverk.daap.chunks.impl.unknown.UnknownMA.class); // msma
		map.put(new Integer(0x6165524D), org.ardverk.daap.chunks.impl.unknown.UnknownRM.class); // aeRM
		map.put(new Integer(0x63617664), org.ardverk.daap.chunks.impl.unknown.UnknownVD.class); // cavd
		map.put(new Integer(0x6D757064), org.ardverk.daap.chunks.impl.dmap.UpdateResponse.class); // mupd
		map.put(new Integer(0x6D757479), org.ardverk.daap.chunks.impl.dmap.UpdateType.class); // muty
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
			throw new RuntimeException(DaapUtil.toContentCodeString(contentCode), err);
		}
	}
}
