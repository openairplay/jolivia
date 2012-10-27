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
import org.ardverk.daap.chunks.impl.*;

public final class ChunkFactory
{

	private final Map<Integer, Class<? extends Chunk>> map = new HashMap<Integer, Class<? extends Chunk>>();

	public ChunkFactory()
	{
		map.put(new Integer(0x6D736175), AuthenticationMethod.class); // msau
		map.put(new Integer(0x6D736173), AuthenticationSchemes.class); // msas
		map.put(new Integer(0x6D62636C), Bag.class); // mbcl
		map.put(new Integer(0x6162706C), BasePlaylist.class); // abpl
		map.put(new Integer(0x6162616C), BrowseAlbumListing.class); // abal
		map.put(new Integer(0x61626172), BrowseArtistListing.class); // abar
		map.put(new Integer(0x61626370), BrowseComposerListing.class); // abcp
		map.put(new Integer(0x6162676E), BrowseGenreListing.class); // abgn
		map.put(new Integer(0x6D636F6E), Container.class); // mcon
		map.put(new Integer(0x6D637463), ContainerCount.class); // mctc
		map.put(new Integer(0x6D637469), ContainerItemId.class); // mcti
		map.put(new Integer(0x6D636E61), ContentCodesName.class); // mcna
		map.put(new Integer(0x6D636E6D), ContentCodesNumber.class); // mcnm
		map.put(new Integer(0x6D636372), ContentCodesResponse.class); // mccr
		map.put(new Integer(0x6D637479), ContentCodesType.class); // mcty
		map.put(new Integer(0x6170726F), DaapProtocolVersion.class); // apro
		map.put(new Integer(0x6162726F), DatabaseBrowse.class); // abro
		map.put(new Integer(0x6D736463), DatabaseCount.class); // msdc
		map.put(new Integer(0x61706C79), DatabasePlaylists.class); // aply
		map.put(new Integer(0x61646273), DatabaseSongs.class); // adbs
		map.put(new Integer(0x6D75646C), DeletedIdListing.class); // mudl
		map.put(new Integer(0x6D64636C), Dictionary.class); // mdcl
		map.put(new Integer(0x6D70726F), DmapProtocolVersion.class); // mpro
		map.put(new Integer(0x668D6368), HasChildContainers.class); // f?ch
		map.put(new Integer(0x61654856), HasVideo.class); // aeHV
		map.put(new Integer(0x6D696D63), ItemCount.class); // mimc
		map.put(new Integer(0x6D696964), ItemId.class); // miid
		map.put(new Integer(0x6D696B64), ItemKind.class); // mikd
		map.put(new Integer(0x6D696E6D), ItemName.class); // minm
		map.put(new Integer(0x61654149), ITMSArtistId.class); // aeAI
		map.put(new Integer(0x61654349), ITMSComposerId.class); // aeCI
		map.put(new Integer(0x61654749), ITMSGenreId.class); // aeGI
		map.put(new Integer(0x61655049), ITMSPlaylistId.class); // aePI
		map.put(new Integer(0x61655349), ITMSSongId.class); // aeSI
		map.put(new Integer(0x61655346), ITMSStorefrontId.class); // aeSF
		map.put(new Integer(0x6D6C636C), Listing.class); // mlcl
		map.put(new Integer(0x6D6C6974), ListingItem.class); // mlit
		map.put(new Integer(0x6D736C72), LoginRequired.class); // mslr
		map.put(new Integer(0x6D6C6F67), LoginResponse.class); // mlog
		map.put(new Integer(0x61655356), MusicSharingVersion.class); // aeSV
		map.put(new Integer(0x61654E56), NormVolume.class); // aeNV
		map.put(new Integer(0x6D70636F), ParentContainerId.class); // mpco
		map.put(new Integer(0x6D706572), PersistentId.class); // mper
		map.put(new Integer(0x6170726D), PlaylistRepeatMode.class); // aprm
		map.put(new Integer(0x6170736D), PlaylistShuffleMode.class); // apsm
		map.put(new Integer(0x6170736F), PlaylistSongs.class); // apso
		map.put(new Integer(0x61655043), Podcast.class); // aePC
		map.put(new Integer(0x61655050), PodcastPlaylist.class); // aePP
		map.put(new Integer(0x61727376), Resolve.class); // arsv
		map.put(new Integer(0x61726966), ResolveInfo.class); // arif
		map.put(new Integer(0x6D72636F), ReturnedCount.class); // mrco
		map.put(new Integer(0x61766462), ServerDatabases.class); // avdb
		map.put(new Integer(0x6D737276), ServerInfoResponse.class); // msrv
		map.put(new Integer(0x6D757372), ServerRevision.class); // musr
		map.put(new Integer(0x6D6C6964), SessionId.class); // mlid
		map.put(new Integer(0x61655350), SmartPlaylist.class); // aeSP
		map.put(new Integer(0x6173616C), SongAlbum.class); // asal
		map.put(new Integer(0x61736172), SongArtist.class); // asar
		map.put(new Integer(0x61736274), SongBeatsPerMinute.class); // asbt
		map.put(new Integer(0x61736272), SongBitrate.class); // asbr
		map.put(new Integer(0x61736374), SongCategory.class); // asct
		map.put(new Integer(0x61736373), SongCodecSubtype.class); // ascs
		map.put(new Integer(0x61736364), SongCodecType.class); // ascd
		map.put(new Integer(0x6173636D), SongComment.class); // ascm
		map.put(new Integer(0x6173636F), SongCompilation.class); // asco
		map.put(new Integer(0x61736370), SongComposer.class); // ascp
		map.put(new Integer(0x6173636E), SongContentDescription.class); // ascn
		map.put(new Integer(0x61736372), SongContentRating.class); // ascr
		map.put(new Integer(0x6173646B), SongDataKind.class); // asdk
		map.put(new Integer(0x6173756C), SongDataUrl.class); // asul
		map.put(new Integer(0x61736461), SongDateAdded.class); // asda
		map.put(new Integer(0x6173646D), SongDateModified.class); // asdm
		map.put(new Integer(0x61736474), SongDescription.class); // asdt
		map.put(new Integer(0x61736462), SongDisabled.class); // asdb
		map.put(new Integer(0x61736463), SongDiscCount.class); // asdc
		map.put(new Integer(0x6173646E), SongDiscNumber.class); // asdn
		map.put(new Integer(0x61736571), SongEqPreset.class); // aseq
		map.put(new Integer(0x6173666D), SongFormat.class); // asfm
		map.put(new Integer(0x6173676E), SongGenre.class); // asgn
		map.put(new Integer(0x61677270), SongGrouping.class); // agrp
		map.put(new Integer(0x61736B79), SongKeywords.class); // asky
		map.put(new Integer(0x61736C63), SongLongDescription.class); // aslc
		map.put(new Integer(0x61737276), SongRelativeVolume.class); // asrv
		map.put(new Integer(0x61737372), SongSampleRate.class); // assr
		map.put(new Integer(0x6173737A), SongSize.class); // assz
		map.put(new Integer(0x61737374), SongStartTime.class); // asst
		map.put(new Integer(0x61737370), SongStopTime.class); // assp
		map.put(new Integer(0x6173746D), SongTime.class); // astm
		map.put(new Integer(0x61737463), SongTrackCount.class); // astc
		map.put(new Integer(0x6173746E), SongTrackNumber.class); // astn
		map.put(new Integer(0x61737572), SongUserRating.class); // asur
		map.put(new Integer(0x61737972), SongYear.class); // asyr
		map.put(new Integer(0x6D74636F), SpecifiedTotalCount.class); // mtco
		map.put(new Integer(0x6D737474), Status.class); // mstt
		map.put(new Integer(0x6D737473), StatusString.class); // msts
		map.put(new Integer(0x6D73616C), SupportsAutoLogout.class); // msal
		map.put(new Integer(0x6D736272), SupportsBrowse.class); // msbr
		map.put(new Integer(0x6D736578), SupportsExtensions.class); // msex
		map.put(new Integer(0x6D736978), SupportsIndex.class); // msix
		map.put(new Integer(0x6D737069), SupportsPersistentIds.class); // mspi
		map.put(new Integer(0x6D737179), SupportsQuery.class); // msqy
		map.put(new Integer(0x6D737273), SupportsResolve.class); // msrs
		map.put(new Integer(0x6D737570), SupportsUpdate.class); // msup
		map.put(new Integer(0x6D73746D), TimeoutInterval.class); // mstm
		map.put(new Integer(0x6D757064), UpdateResponse.class); // mupd
		map.put(new Integer(0x6D757479), UpdateType.class); // muty
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
