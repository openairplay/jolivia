package org.dyndns.jkiddo.dmp;

import org.dyndns.jkiddo.dmp.chunks.AbstractChunk;
import org.dyndns.jkiddo.dmp.chunks.unknown.Unknownal;
import org.dyndns.jkiddo.dmp.chunks.unknown.UnknowncA;

public interface IDmapProtocolDefinition
{
	public enum DmapTypeDefinition
	{
		U_BYTE_TYPE(1,1 ),
		BYTE_TYPE(2,1 ),
		U_SHORT_TYPE(3,2 ),
		SHORT_TYPE(4,2 ),
		U_INT_TYPE(5,4 ),
		INT_TYPE(6,4 ),
		U_LONG_TYPE(7,8 ),
		LONG_TYPE(8,8 ),
		STRING_TYPE(9,-1 ),
		DATE_TYPE(10,4 ),
		VERSION_TYPE(11,4 ),
		CONTAINER_TYPE(12,-1 ),
		RAW_TYPE(13,-1 );

		private final int type;
		private final int length;

		DmapTypeDefinition(final int type, final int length)
		{
			this.type = type;
			this.length = length;
		}

		public int getType()
		{
			return type;
		}

		public int getLength()
		{
			return length;
		}
		
		public static DmapTypeDefinition fromInteger(final int i)
		{
			for(final DmapTypeDefinition v : DmapTypeDefinition.values())
			{
				if(v.getType() == i)
					return v;
			}
			throw new RuntimeException(i + " could not be mapped to a type");
		}
	} 
	public static enum DmapChunkDefinition implements IDmapProtocolDefinition
	{
		asaa("asaa","daap.songalbumartist",DmapTypeDefinition.STRING_TYPE,org.dyndns.jkiddo.dmap.chunks.audio.AlbumArtist.class),
		agal("agal","com.apple.itunes.unknown-al",DmapTypeDefinition.CONTAINER_TYPE,org.dyndns.jkiddo.dmap.chunks.audio.AlbumSearchContainer.class),
		agar("agar","unknown.ar",DmapTypeDefinition.CONTAINER_TYPE,org.dyndns.jkiddo.dmap.chunks.audio.ArtistSearchContainer.class),
		apro("apro","daap.protocolversion",DmapTypeDefinition.VERSION_TYPE,org.dyndns.jkiddo.dmap.chunks.audio.AudioProtocolVersion.class),
		abpl("abpl","daap.baseplaylist",DmapTypeDefinition.U_BYTE_TYPE,org.dyndns.jkiddo.dmap.chunks.audio.BaseContainer.class),
		abal("abal","daap.browsealbumlisting",DmapTypeDefinition.CONTAINER_TYPE,org.dyndns.jkiddo.dmap.chunks.audio.BrowseAlbumListing.class),
		abar("abar","daap.browseartistlisting",DmapTypeDefinition.CONTAINER_TYPE,org.dyndns.jkiddo.dmap.chunks.audio.BrowseArtistListing.class),
		abcp("abcp","daap.browsecomposerlisting",DmapTypeDefinition.CONTAINER_TYPE,org.dyndns.jkiddo.dmap.chunks.audio.BrowseComposerListing.class),
		abgn("abgn","daap.browsegenrelisting",DmapTypeDefinition.CONTAINER_TYPE,org.dyndns.jkiddo.dmap.chunks.audio.BrowseGenreListing.class),
		abro("abro","daap.databasebrowse",DmapTypeDefinition.CONTAINER_TYPE,org.dyndns.jkiddo.dmap.chunks.audio.DatabaseBrowse.class),
		aply("aply","daap.databaseplaylists",DmapTypeDefinition.CONTAINER_TYPE,org.dyndns.jkiddo.dmap.chunks.audio.DatabaseContainerns.class),
		adbs("adbs","daap.databasesongs",DmapTypeDefinition.CONTAINER_TYPE,org.dyndns.jkiddo.dmap.chunks.audio.DatabaseItems.class),
		aeCs("aeCs","com.apple.itunes.artworkchecksum",DmapTypeDefinition.U_INT_TYPE,org.dyndns.jkiddo.dmap.chunks.audio.extension.ArtworkChecksum.class),
		aeCF("aeCF","com.apple.itunes.cloud-flavor-id",DmapTypeDefinition.U_LONG_TYPE,org.dyndns.jkiddo.dmap.chunks.audio.extension.CloudFlavorID.class),
		aeCd("aeCd","com.apple.itunes.cloud-id",DmapTypeDefinition.U_LONG_TYPE,org.dyndns.jkiddo.dmap.chunks.audio.extension.CloudID.class),
		aeCK("aeCK","com.apple.itunes.cloud-library-kind",DmapTypeDefinition.U_BYTE_TYPE,org.dyndns.jkiddo.dmap.chunks.audio.extension.CloudLibraryKind.class),
		aeCM("aeCM","com.apple.itunes.cloud-match-type",DmapTypeDefinition.U_BYTE_TYPE,org.dyndns.jkiddo.dmap.chunks.audio.extension.CloudMatchType.class),
		aeCU("aeCU","com.apple.itunes.cloud-user-id",DmapTypeDefinition.U_LONG_TYPE,org.dyndns.jkiddo.dmap.chunks.audio.extension.CloudUserID.class),
		aecp("aecp","com.apple.itunes.collection-description",DmapTypeDefinition.STRING_TYPE,org.dyndns.jkiddo.dmap.chunks.audio.extension.CollectionDescription.class),
		aeCR("aeCR","com.apple.itunes.content-rating",DmapTypeDefinition.STRING_TYPE,org.dyndns.jkiddo.dmap.chunks.audio.extension.ContentRating.class),
		aeK1("aeK1","com.apple.itunes.drm-key1-id",DmapTypeDefinition.U_LONG_TYPE,org.dyndns.jkiddo.dmap.chunks.audio.extension.DRMKey1Id.class),
		aeK2("aeK2","com.apple.itunes.drm-key2-id",DmapTypeDefinition.U_LONG_TYPE,org.dyndns.jkiddo.dmap.chunks.audio.extension.DRMKey2Id.class),
		aeDP("aeDP","com.apple.itunes.drm-platform-id",DmapTypeDefinition.U_INT_TYPE,org.dyndns.jkiddo.dmap.chunks.audio.extension.DRMPlatformId.class),
		aeDR("aeDR","com.apple.itunes.drm-user-id",DmapTypeDefinition.U_LONG_TYPE,org.dyndns.jkiddo.dmap.chunks.audio.extension.DRMUserId.class),
		aeDV("aeDV","com.apple.itunes.drm-versions",DmapTypeDefinition.U_INT_TYPE,org.dyndns.jkiddo.dmap.chunks.audio.extension.DRMVersions.class),
		aeEN("aeEN","com.apple.itunes.episode-num-str",DmapTypeDefinition.STRING_TYPE,org.dyndns.jkiddo.dmap.chunks.audio.extension.EpisodeNumberString.class),
		aeES("aeES","com.apple.itunes.episode-sort",DmapTypeDefinition.U_INT_TYPE,org.dyndns.jkiddo.dmap.chunks.audio.extension.EpisodeSort.class),
		aeMk("aeMk","com.apple.itunes.extended-media-kind",DmapTypeDefinition.U_INT_TYPE,org.dyndns.jkiddo.dmap.chunks.audio.extension.ExtendedMediaKind.class),
		aeGU("aeGU","com.apple.itunes.gapless-dur",DmapTypeDefinition.U_LONG_TYPE,org.dyndns.jkiddo.dmap.chunks.audio.extension.GaplessDuration.class),
		aeGE("aeGE","com.apple.itunes.gapless-enc-del",DmapTypeDefinition.U_INT_TYPE,org.dyndns.jkiddo.dmap.chunks.audio.extension.GaplessEncodingDel.class),
		aeGD("aeGD","com.apple.itunes.gapless-enc-dr",DmapTypeDefinition.U_INT_TYPE,org.dyndns.jkiddo.dmap.chunks.audio.extension.GaplessEncodingDr.class),
		aeGH("aeGH","com.apple.itunes.gapless-heur",DmapTypeDefinition.U_INT_TYPE,org.dyndns.jkiddo.dmap.chunks.audio.extension.GaplessHeur.class),
		aeGR("aeGR","com.apple.itunes.gapless-resy",DmapTypeDefinition.U_LONG_TYPE,org.dyndns.jkiddo.dmap.chunks.audio.extension.GaplessResy.class),
		aeGs("aeGs","com.apple.itunes.can-be-genius-seed",DmapTypeDefinition.U_BYTE_TYPE,org.dyndns.jkiddo.dmap.chunks.audio.extension.GeniusSeed.class),
		aeHV("aeHV","com.apple.itunes.has-video",DmapTypeDefinition.U_BYTE_TYPE,org.dyndns.jkiddo.dmap.chunks.audio.extension.HasVideo.class),
		aeHD("aeHD","com.apple.itunes.is-hd-video",DmapTypeDefinition.U_BYTE_TYPE,org.dyndns.jkiddo.dmap.chunks.audio.extension.IsHDVideo.class),
		aeAI("aeAI","com.apple.itunes.itms-artistid",DmapTypeDefinition.U_INT_TYPE,org.dyndns.jkiddo.dmap.chunks.audio.extension.ITMSArtistId.class),
		aeCI("aeCI","com.apple.itunes.itms-composerid",DmapTypeDefinition.U_INT_TYPE,org.dyndns.jkiddo.dmap.chunks.audio.extension.ITMSComposerId.class),
		aeGI("aeGI","com.apple.itunes.itms-genreid",DmapTypeDefinition.U_INT_TYPE,org.dyndns.jkiddo.dmap.chunks.audio.extension.ITMSGenreId.class),
		aePI("aePI","com.apple.itunes.itms-playlistid",DmapTypeDefinition.U_INT_TYPE,org.dyndns.jkiddo.dmap.chunks.audio.extension.ITMSPlaylistId.class),
		aeSI("aeSI","com.apple.itunes.itms-songid",DmapTypeDefinition.U_LONG_TYPE,org.dyndns.jkiddo.dmap.chunks.audio.extension.ITMSSongId.class),
		aeSF("aeSF","com.apple.itunes.itms-storefrontid",DmapTypeDefinition.U_INT_TYPE,org.dyndns.jkiddo.dmap.chunks.audio.extension.ITMSStorefrontId.class),
		aels("aels","com.apple.itunes.liked-state",DmapTypeDefinition.U_BYTE_TYPE,org.dyndns.jkiddo.dmap.chunks.audio.extension.LikedState.class),
		aeMK("aeMK","com.apple.itunes.mediakind",DmapTypeDefinition.U_BYTE_TYPE,org.dyndns.jkiddo.dmap.chunks.audio.extension.MediaKind.class),
		aeml("aeml","com.apple.itunes.media-kind-listing",DmapTypeDefinition.CONTAINER_TYPE,org.dyndns.jkiddo.dmap.chunks.audio.extension.MediaKindListening.class),
		aemi("aemi","com.apple.itunes.media-kind-listing-item",DmapTypeDefinition.CONTAINER_TYPE,org.dyndns.jkiddo.dmap.chunks.audio.extension.MediaKindListeningItem.class),
		aeMX("aeMX","com.apple.itunes.movie-info-xml",DmapTypeDefinition.STRING_TYPE,org.dyndns.jkiddo.dmap.chunks.audio.extension.MovieInfoXml.class),
		aeSV("aeSV","com.apple.itunes.music-sharing-version",DmapTypeDefinition.U_INT_TYPE,org.dyndns.jkiddo.dmap.chunks.audio.extension.MusicSharingVersion.class),
		aeNN("aeNN","com.apple.itunes.network-name",DmapTypeDefinition.STRING_TYPE,org.dyndns.jkiddo.dmap.chunks.audio.extension.NetworkName.class),
		aeND("aeND","com.apple.itunes.non-drm-user-id",DmapTypeDefinition.U_LONG_TYPE,org.dyndns.jkiddo.dmap.chunks.audio.extension.NonDRMUserId.class),
		aeNV("aeNV","com.apple.itunes.norm-volume",DmapTypeDefinition.U_INT_TYPE,org.dyndns.jkiddo.dmap.chunks.audio.extension.NormVolume.class),
		aePC("aePC","com.apple.itunes.is-podcast",DmapTypeDefinition.U_BYTE_TYPE,org.dyndns.jkiddo.dmap.chunks.audio.extension.Podcast.class),
		aePP("aePP","com.apple.itunes.is-podcast-playlist",DmapTypeDefinition.U_BYTE_TYPE,org.dyndns.jkiddo.dmap.chunks.audio.extension.PodcastPlaylist.class),
		aeSG("aeSG","com.apple.itunes.saved-genius",DmapTypeDefinition.U_BYTE_TYPE,org.dyndns.jkiddo.dmap.chunks.audio.extension.SavedGenius.class),
		aeSU("aeSU","com.apple.itunes.season-num",DmapTypeDefinition.U_INT_TYPE,org.dyndns.jkiddo.dmap.chunks.audio.extension.SeasonNumber.class),
		aeSN("aeSN","com.apple.itunes.series-name",DmapTypeDefinition.STRING_TYPE,org.dyndns.jkiddo.dmap.chunks.audio.extension.SeriesName.class),
		aeSP("aeSP","com.apple.itunes.smart-playlist",DmapTypeDefinition.U_BYTE_TYPE,org.dyndns.jkiddo.dmap.chunks.audio.extension.SmartPlaylist.class),
		asrs("asrs","daap.songuserratingstatus",DmapTypeDefinition.U_BYTE_TYPE,org.dyndns.jkiddo.dmap.chunks.audio.extension.SongUserRatingStatus.class),
		aePS("aePS","com.apple.itunes.special-playlist",DmapTypeDefinition.U_BYTE_TYPE,org.dyndns.jkiddo.dmap.chunks.audio.extension.SpecialPlaylist.class),
		aeSE("aeSE","com.apple.itunes.store-pers-id",DmapTypeDefinition.U_LONG_TYPE,org.dyndns.jkiddo.dmap.chunks.audio.extension.StorePersistentId.class),
		aeFP("aeFP","com.apple.itunes.unknown-FP",DmapTypeDefinition.U_BYTE_TYPE,org.dyndns.jkiddo.dmap.chunks.audio.extension.SupportsFairPlay.class),
		aeAK("aeAK","unknown",DmapTypeDefinition.STRING_TYPE,org.dyndns.jkiddo.dmap.chunks.audio.extension.UnknownAK.class),
		aeCS("aeCS","com.apple.itunes.artworkchecksum",DmapTypeDefinition.U_INT_TYPE,org.dyndns.jkiddo.dmap.chunks.audio.extension._ArtworkChecksum.class),
		aeFR("aeFR","com.apple.itunes.unknown-FR",DmapTypeDefinition.U_BYTE_TYPE,org.dyndns.jkiddo.dmap.chunks.audio.extension.UnknownFR.class),
		aeIM("aeIM","com.apple.itunes.unknown-IM",DmapTypeDefinition.U_LONG_TYPE,org.dyndns.jkiddo.dmap.chunks.audio.extension.UnknownIM.class),
		aeMQ("aeMQ","com.apple.itunes.unknown-MQ",DmapTypeDefinition.U_BYTE_TYPE,org.dyndns.jkiddo.dmap.chunks.audio.extension.UnknownMQ.class),
		aeRM("aeRM","com.apple.itunes.unknown-RM",DmapTypeDefinition.U_LONG_TYPE,org.dyndns.jkiddo.dmap.chunks.audio.extension.UnknownRM.class),
		aeSL("aeSL","com.apple.itunes.unknown-SL",DmapTypeDefinition.U_BYTE_TYPE,org.dyndns.jkiddo.dmap.chunks.audio.extension.UnknownSL.class),
		aeSR("aeSR","com.apple.itunes.unknown-SR",DmapTypeDefinition.U_BYTE_TYPE,org.dyndns.jkiddo.dmap.chunks.audio.extension.UnknownSR.class),
		aeSX("aeSX","com.apple.itunes.unknown-SX",DmapTypeDefinition.U_LONG_TYPE,org.dyndns.jkiddo.dmap.chunks.audio.extension.UnknownSX.class),
		aeTr("aeTr","com.apple.itunes.unknown-Tr",DmapTypeDefinition.U_BYTE_TYPE,org.dyndns.jkiddo.dmap.chunks.audio.extension.UnknownTr.class),
		aeXD("aeXD","com.apple.itunes.xid",DmapTypeDefinition.STRING_TYPE,org.dyndns.jkiddo.dmap.chunks.audio.extension.Xid.class),
		agac("agac","daap.groupalbumcount",DmapTypeDefinition.U_INT_TYPE,org.dyndns.jkiddo.dmap.chunks.audio.GroupAlbumCount.class),
		apso("apso","daap.playlistsongs",DmapTypeDefinition.CONTAINER_TYPE,org.dyndns.jkiddo.dmap.chunks.audio.ItemsContainer.class),
		aprm("aprm","daap.playlistrepeatmode",DmapTypeDefinition.U_BYTE_TYPE,org.dyndns.jkiddo.dmap.chunks.audio.PlaylistRepeatMode.class),
		apsm("apsm","daap.playlistshufflemode",DmapTypeDefinition.U_BYTE_TYPE,org.dyndns.jkiddo.dmap.chunks.audio.PlaylistShuffleMode.class),
		arsv("arsv","daap.resolve",DmapTypeDefinition.CONTAINER_TYPE,org.dyndns.jkiddo.dmap.chunks.audio.Resolve.class),
		arif("arif","daap.resolveinfo",DmapTypeDefinition.CONTAINER_TYPE,org.dyndns.jkiddo.dmap.chunks.audio.ResolveInfo.class),
		avdb("avdb","daap.serverdatabases",DmapTypeDefinition.CONTAINER_TYPE,org.dyndns.jkiddo.dmap.chunks.audio.ServerDatabases.class),
		asal("asal","daap.songalbum",DmapTypeDefinition.STRING_TYPE,org.dyndns.jkiddo.dmap.chunks.audio.SongAlbum.class),
		asai("asai","daap.songalbumid",DmapTypeDefinition.U_LONG_TYPE,org.dyndns.jkiddo.dmap.chunks.audio.SongAlbumId.class),
		aslr("aslr","daap.songalbumuserrating",DmapTypeDefinition.U_BYTE_TYPE,org.dyndns.jkiddo.dmap.chunks.audio.SongAlbumUserRating.class),
		asas("asas","daap.songalbumuserratingstatus",DmapTypeDefinition.U_BYTE_TYPE,org.dyndns.jkiddo.dmap.chunks.audio.SongAlbumUserRatingStatus.class),
		asar("asar","daap.songartist",DmapTypeDefinition.STRING_TYPE,org.dyndns.jkiddo.dmap.chunks.audio.SongArtist.class),
		asri("asri","daap.songartistid",DmapTypeDefinition.U_LONG_TYPE,org.dyndns.jkiddo.dmap.chunks.audio.SongArtistId.class),
		asac("asac","daap.songartworkcount",DmapTypeDefinition.U_SHORT_TYPE,org.dyndns.jkiddo.dmap.chunks.audio.SongArtworkCount.class),
		asbt("asbt","daap.songbeatsperminute",DmapTypeDefinition.U_SHORT_TYPE,org.dyndns.jkiddo.dmap.chunks.audio.SongBeatsPerMinute.class),
		asbr("asbr","daap.songbitrate",DmapTypeDefinition.U_SHORT_TYPE,org.dyndns.jkiddo.dmap.chunks.audio.SongBitrate.class),
		asct("asct","daap.songcategory",DmapTypeDefinition.STRING_TYPE,org.dyndns.jkiddo.dmap.chunks.audio.SongCategory.class),
		ascs("ascs","daap.songcodecsubtype",DmapTypeDefinition.U_INT_TYPE,org.dyndns.jkiddo.dmap.chunks.audio.SongCodecSubtype.class),
		ascd("ascd","daap.songcodectype",DmapTypeDefinition.U_INT_TYPE,org.dyndns.jkiddo.dmap.chunks.audio.SongCodecType.class),
		ascm("ascm","daap.songcomment",DmapTypeDefinition.STRING_TYPE,org.dyndns.jkiddo.dmap.chunks.audio.SongComment.class),
		asco("asco","daap.songcompilation",DmapTypeDefinition.U_BYTE_TYPE,org.dyndns.jkiddo.dmap.chunks.audio.SongCompilation.class),
		ascp("ascp","daap.songcomposer",DmapTypeDefinition.STRING_TYPE,org.dyndns.jkiddo.dmap.chunks.audio.SongComposer.class),
		ascn("ascn","daap.songcontentdescription",DmapTypeDefinition.STRING_TYPE,org.dyndns.jkiddo.dmap.chunks.audio.SongContentDescription.class),
		ascr("ascr","daap.songcontentrating",DmapTypeDefinition.U_BYTE_TYPE,org.dyndns.jkiddo.dmap.chunks.audio.SongContentRating.class),
		asdk("asdk","daap.songdatakind",DmapTypeDefinition.U_BYTE_TYPE,org.dyndns.jkiddo.dmap.chunks.audio.SongDataKind.class),
		asul("asul","daap.songdataurl",DmapTypeDefinition.STRING_TYPE,org.dyndns.jkiddo.dmap.chunks.audio.SongDataUrl.class),
		asda("asda","daap.songdateadded",DmapTypeDefinition.DATE_TYPE,org.dyndns.jkiddo.dmap.chunks.audio.SongDateAdded.class),
		asdm("asdm","daap.songdatemodified",DmapTypeDefinition.DATE_TYPE,org.dyndns.jkiddo.dmap.chunks.audio.SongDateModified.class),
		aspl("aspl","daap.songdateplayed",DmapTypeDefinition.DATE_TYPE,org.dyndns.jkiddo.dmap.chunks.audio.SongDatePlayed.class),
		asdp("asdp","daap.songdatepurchased",DmapTypeDefinition.DATE_TYPE,org.dyndns.jkiddo.dmap.chunks.audio.SongDatePurchased.class),
		asdr("asdr","daap.songdatereleased",DmapTypeDefinition.DATE_TYPE,org.dyndns.jkiddo.dmap.chunks.audio.SongDateReleased.class),
		asdt("asdt","daap.songdescription",DmapTypeDefinition.STRING_TYPE,org.dyndns.jkiddo.dmap.chunks.audio.SongDescription.class),
		asdb("asdb","daap.songdisabled",DmapTypeDefinition.U_BYTE_TYPE,org.dyndns.jkiddo.dmap.chunks.audio.SongDisabled.class),
		asdc("asdc","daap.songdisccount",DmapTypeDefinition.U_SHORT_TYPE,org.dyndns.jkiddo.dmap.chunks.audio.SongDiscCount.class),
		asdn("asdn","daap.songdiscnumber",DmapTypeDefinition.U_SHORT_TYPE,org.dyndns.jkiddo.dmap.chunks.audio.SongDiscNumber.class),
		aseq("aseq","daap.songeqpreset",DmapTypeDefinition.STRING_TYPE,org.dyndns.jkiddo.dmap.chunks.audio.SongEqPreset.class),
		ases("ases","daap.songexcludefromshuffle",DmapTypeDefinition.U_BYTE_TYPE,org.dyndns.jkiddo.dmap.chunks.audio.SongExcludeFromShuffle.class),
		ased("ased","daap.songextradata",DmapTypeDefinition.U_SHORT_TYPE,org.dyndns.jkiddo.dmap.chunks.audio.SongExtraData.class),
		asfm("asfm","daap.songformat",DmapTypeDefinition.STRING_TYPE,org.dyndns.jkiddo.dmap.chunks.audio.SongFormat.class),
		asgp("asgp","daap.songgapless",DmapTypeDefinition.U_BYTE_TYPE,org.dyndns.jkiddo.dmap.chunks.audio.SongGapless.class),
		asgn("asgn","daap.songgenre",DmapTypeDefinition.STRING_TYPE,org.dyndns.jkiddo.dmap.chunks.audio.SongGenre.class),
		agrp("agrp","daap.songgrouping",DmapTypeDefinition.STRING_TYPE,org.dyndns.jkiddo.dmap.chunks.audio.SongGrouping.class),
		ashp("ashp","daap.songhasbeenplayed",DmapTypeDefinition.U_BYTE_TYPE,org.dyndns.jkiddo.dmap.chunks.audio.SongHasBeenPlayed.class),
		asky("asky","daap.songkeywords",DmapTypeDefinition.STRING_TYPE,org.dyndns.jkiddo.dmap.chunks.audio.SongKeywords.class),
		askd("askd","daap.songlastskipdate",DmapTypeDefinition.DATE_TYPE,org.dyndns.jkiddo.dmap.chunks.audio.SongLastSkipDate.class),
		aslc("aslc","daap.songlongcontentdescription",DmapTypeDefinition.STRING_TYPE,org.dyndns.jkiddo.dmap.chunks.audio.SongLongDescription.class),
		asls("asls","daap.songlongsize",DmapTypeDefinition.U_LONG_TYPE,org.dyndns.jkiddo.dmap.chunks.audio.SongLongSize.class),
		aspu("aspu","daap.songpodcasturl",DmapTypeDefinition.STRING_TYPE,org.dyndns.jkiddo.dmap.chunks.audio.SongPodcastUrl.class),
		asrv("asrv","daap.songrelativevolume",DmapTypeDefinition.BYTE_TYPE,org.dyndns.jkiddo.dmap.chunks.audio.SongRelativeVolume.class),
		assr("assr","daap.songsamplerate",DmapTypeDefinition.U_INT_TYPE,org.dyndns.jkiddo.dmap.chunks.audio.SongSampleRate.class),
		assz("assz","daap.songsize",DmapTypeDefinition.U_INT_TYPE,org.dyndns.jkiddo.dmap.chunks.audio.SongSize.class),
		asst("asst","daap.songstarttime",DmapTypeDefinition.U_INT_TYPE,org.dyndns.jkiddo.dmap.chunks.audio.SongStartTime.class),
		assp("assp","daap.songstoptime",DmapTypeDefinition.U_INT_TYPE,org.dyndns.jkiddo.dmap.chunks.audio.SongStopTime.class),
		astm("astm","daap.songtime",DmapTypeDefinition.U_INT_TYPE,org.dyndns.jkiddo.dmap.chunks.audio.SongTime.class),
		astc("astc","daap.songtrackcount",DmapTypeDefinition.U_SHORT_TYPE,org.dyndns.jkiddo.dmap.chunks.audio.SongTrackCount.class),
		astn("astn","daap.songtracknumber",DmapTypeDefinition.U_SHORT_TYPE,org.dyndns.jkiddo.dmap.chunks.audio.SongTrackNumber.class),
		aspc("aspc","daap.songuserplaycount",DmapTypeDefinition.U_INT_TYPE,org.dyndns.jkiddo.dmap.chunks.audio.SongUserPlayCount.class),
		asur("asur","daap.songuserrating",DmapTypeDefinition.U_BYTE_TYPE,org.dyndns.jkiddo.dmap.chunks.audio.SongUserRating.class),
		askp("askp","daap.songuserskipcount",DmapTypeDefinition.U_INT_TYPE,org.dyndns.jkiddo.dmap.chunks.audio.SongUserSkipCount.class),
		asyr("asyr","daap.songyear",DmapTypeDefinition.U_SHORT_TYPE,org.dyndns.jkiddo.dmap.chunks.audio.SongYear.class),
		assu("assu","daap.sortalbum",DmapTypeDefinition.STRING_TYPE,org.dyndns.jkiddo.dmap.chunks.audio.SortAlbum.class),
		assl("assl","daap.sortalbumartist",DmapTypeDefinition.STRING_TYPE,org.dyndns.jkiddo.dmap.chunks.audio.SortAlbumArtist.class),
		assa("assa","daap.sortartist",DmapTypeDefinition.STRING_TYPE,org.dyndns.jkiddo.dmap.chunks.audio.SortArtist.class),
		asbk("asbk","daap.bookmarkable",DmapTypeDefinition.U_BYTE_TYPE,org.dyndns.jkiddo.dmap.chunks.audio.SortBookmarkable.class),
		assc("assc","daap.sortcomposer",DmapTypeDefinition.STRING_TYPE,org.dyndns.jkiddo.dmap.chunks.audio.SortComposer.class),
		assn("assn","daap.sortname",DmapTypeDefinition.STRING_TYPE,org.dyndns.jkiddo.dmap.chunks.audio.SortName.class),
		asss("asss","daap.sortseriesname",DmapTypeDefinition.STRING_TYPE,org.dyndns.jkiddo.dmap.chunks.audio.SortSeriesName.class),
		ated("ated","daap.supportsextradata",DmapTypeDefinition.U_SHORT_TYPE,org.dyndns.jkiddo.dmap.chunks.audio.SupportsExtraData.class),
		asgr("asgr","daap.supportsgroups",DmapTypeDefinition.U_SHORT_TYPE,org.dyndns.jkiddo.dmap.chunks.audio.SupportsGroups.class),
		mscu("mscu","unknown-cu",DmapTypeDefinition.U_LONG_TYPE,org.dyndns.jkiddo.dmap.chunks.audio.UnknownCU.class),
		asse("asse","com.apple.itunes.unknown-se",DmapTypeDefinition.U_LONG_TYPE,org.dyndns.jkiddo.dmap.chunks.audio.UnknownSE.class),
		capr("capr","dacp.protocolversion",DmapTypeDefinition.VERSION_TYPE,org.dyndns.jkiddo.dmcp.chunks.media.audio.AudioControlProtocolVersion.class),
		caar("caar","dacp.availablerepeatstates",DmapTypeDefinition.U_INT_TYPE,org.dyndns.jkiddo.dmcp.chunks.media.audio.AvailableRepeatStates.class),
		caas("caas","dacp.availableshufflestates",DmapTypeDefinition.U_INT_TYPE,org.dyndns.jkiddo.dmcp.chunks.media.audio.AvailableShuffleStates.class),
		caci("caci","dacp.controlint",DmapTypeDefinition.CONTAINER_TYPE,org.dyndns.jkiddo.dmcp.chunks.media.audio.DataControlInt.class),
		cafe("cafe","dacp.fullscreenenabled",DmapTypeDefinition.U_BYTE_TYPE,org.dyndns.jkiddo.dmcp.chunks.media.audio.FullScreenEnabled.class),
		cafs("cafs","dacp.fullscreen",DmapTypeDefinition.U_BYTE_TYPE,org.dyndns.jkiddo.dmcp.chunks.media.audio.FullscreenStatus.class),
		canp("canp","dacp.nowplayingids",DmapTypeDefinition.RAW_TYPE,org.dyndns.jkiddo.dmcp.chunks.media.audio.NowPlaying.class),
		canl("canl","dacp.nowplayingalbum",DmapTypeDefinition.STRING_TYPE,org.dyndns.jkiddo.dmcp.chunks.media.audio.NowPlayingAlbum.class),
		cana("cana","dacp.nowplayingartist",DmapTypeDefinition.STRING_TYPE,org.dyndns.jkiddo.dmcp.chunks.media.audio.NowPlayingArtist.class),
		cang("cang","dacp.nowplayinggenre",DmapTypeDefinition.STRING_TYPE,org.dyndns.jkiddo.dmcp.chunks.media.audio.NowPlayingGenre.class),
		cann("cann","dacp.nowplayingname",DmapTypeDefinition.STRING_TYPE,org.dyndns.jkiddo.dmcp.chunks.media.audio.NowPlayingName.class),
		ceQR("ceQR","com.apple.itunes.playqueue-contents-response",DmapTypeDefinition.CONTAINER_TYPE,org.dyndns.jkiddo.dmcp.chunks.media.audio.PlayQueueContentsResponse.class),
		caps("caps","dacp.playerstate",DmapTypeDefinition.U_BYTE_TYPE,org.dyndns.jkiddo.dmcp.chunks.media.audio.PlayStatus.class),
		cant("cant","dacp.nowplayingtime",DmapTypeDefinition.U_INT_TYPE,org.dyndns.jkiddo.dmcp.chunks.media.audio.ProgressRemain.class),
		cast("cast","dacp.songtime",DmapTypeDefinition.U_INT_TYPE,org.dyndns.jkiddo.dmcp.chunks.media.audio.ProgressTotal.class),
		carp("carp","dacp.repeatstate",DmapTypeDefinition.U_BYTE_TYPE,org.dyndns.jkiddo.dmcp.chunks.media.audio.RepeatStatus.class),
		cash("cash","dacp.shufflestate",DmapTypeDefinition.U_BYTE_TYPE,org.dyndns.jkiddo.dmcp.chunks.media.audio.ShuffleStatus.class),
		caia("caia","dacp.isactive",DmapTypeDefinition.U_BYTE_TYPE,org.dyndns.jkiddo.dmcp.chunks.media.audio.SpeakerActive.class),
		casp("casp","dacp.speakers",DmapTypeDefinition.CONTAINER_TYPE,org.dyndns.jkiddo.dmcp.chunks.media.audio.SpeakerList.class),
		cads("cads","unknown-ds",DmapTypeDefinition.U_INT_TYPE,org.dyndns.jkiddo.dmcp.chunks.media.audio.UnknownDS.class),
		caip("caip","com.apple.itunes.unknown-ip",DmapTypeDefinition.U_BYTE_TYPE,org.dyndns.jkiddo.dmcp.chunks.media.audio.UnknownIP.class),
		caiv("caiv","com.apple.itunes.unknown-iv",DmapTypeDefinition.U_BYTE_TYPE,org.dyndns.jkiddo.dmcp.chunks.media.audio.UnknownIV.class),
		caks("caks","unknown.ss",DmapTypeDefinition.U_BYTE_TYPE,org.dyndns.jkiddo.dmcp.chunks.media.audio.UnknownKs.class),
		caov("caov","unknown.ov",DmapTypeDefinition.U_BYTE_TYPE,org.dyndns.jkiddo.dmcp.chunks.media.audio.UnknownOV.class),
		casa("casa","com.apple.itunes.unknown-sa",DmapTypeDefinition.U_INT_TYPE,org.dyndns.jkiddo.dmcp.chunks.media.audio.UnknownSA.class),
		casc("casc","unknown.ss",DmapTypeDefinition.U_BYTE_TYPE,org.dyndns.jkiddo.dmcp.chunks.media.audio.UnknownSC.class),
		cass("cass","unknown.ss",DmapTypeDefinition.U_BYTE_TYPE,org.dyndns.jkiddo.dmcp.chunks.media.audio.UnknownSS.class),
		casu("casu","com.apple.itunes.unknown-su",DmapTypeDefinition.U_BYTE_TYPE,org.dyndns.jkiddo.dmcp.chunks.media.audio.UnknownSU.class),
		cavd("cavd","com.apple.itunes.unknown-vd",DmapTypeDefinition.U_BYTE_TYPE,org.dyndns.jkiddo.dmcp.chunks.media.audio.UnknownVD.class),
		cave("cave","dacp.visualizerenabled",DmapTypeDefinition.U_BYTE_TYPE,org.dyndns.jkiddo.dmcp.chunks.media.audio.VisualizerEnabled.class),
		cavs("cavs","dacp.visualizer",DmapTypeDefinition.U_BYTE_TYPE,org.dyndns.jkiddo.dmcp.chunks.media.audio.VisualizerStatus.class),
		cavc("cavc","dacp.volumecontrollable",DmapTypeDefinition.U_INT_TYPE,org.dyndns.jkiddo.dmcp.chunks.media.audio.VolumeControllable.class),
		cmmk("cmmk","dmcp.mediakind",DmapTypeDefinition.U_INT_TYPE,org.dyndns.jkiddo.dmcp.chunks.media.CMediaKind.class),
		cmnm("cmnm","unknown-nm",DmapTypeDefinition.STRING_TYPE,org.dyndns.jkiddo.dmcp.chunks.media.DeviceName.class),
		cmty("cmty","unknown-ty",DmapTypeDefinition.STRING_TYPE,org.dyndns.jkiddo.dmcp.chunks.media.DeviceType.class),
		ceGS("ceGS","com.apple.itunes.genius-selectable",DmapTypeDefinition.U_BYTE_TYPE,org.dyndns.jkiddo.dmcp.chunks.media.extension.GeniusSelectable.class),
		ceQa("ceQa","com.apple.itunes.playqueue-album",DmapTypeDefinition.STRING_TYPE,org.dyndns.jkiddo.dmcp.chunks.media.extension.PlayQueueAlbum.class),
		ceQr("ceQr","com.apple.itunes.playqueue-artist",DmapTypeDefinition.STRING_TYPE,org.dyndns.jkiddo.dmcp.chunks.media.extension.PlayQueueArtist.class),
		ceQg("ceQg","com.apple.itunes.playqueue-genre",DmapTypeDefinition.STRING_TYPE,org.dyndns.jkiddo.dmcp.chunks.media.extension.PlayQueueGenre.class),
		ceQs("ceQs","com.apple.itunes.playqueue-id",DmapTypeDefinition.STRING_TYPE,org.dyndns.jkiddo.dmcp.chunks.media.extension.PlayQueueId.class),
		ceQn("ceQn","com.apple.itunes.playqueue-name",DmapTypeDefinition.STRING_TYPE,org.dyndns.jkiddo.dmcp.chunks.media.extension.PlayQueueName.class),
		ceSG("ceSG","com.apple.itunes.saved-genius",DmapTypeDefinition.U_BYTE_TYPE,org.dyndns.jkiddo.dmcp.chunks.media.extension.SavedGenius.class),
		ceQI("ceQI","unknown",DmapTypeDefinition.U_INT_TYPE,org.dyndns.jkiddo.dmcp.chunks.media.extension.UnknownceQI.class),
		ceSX("ceSX","unknown.sx",DmapTypeDefinition.U_LONG_TYPE,org.dyndns.jkiddo.dmcp.chunks.media.extension.UnknownCESX.class),
		ceQh("ceQh","unknown",DmapTypeDefinition.STRING_TYPE,org.dyndns.jkiddo.dmcp.chunks.media.extension.UnknownQh.class),
		ceQi("ceQi","unknown",DmapTypeDefinition.U_INT_TYPE,org.dyndns.jkiddo.dmcp.chunks.media.extension.UnknownQi.class),
		ceQk("ceQk","unknown",DmapTypeDefinition.STRING_TYPE,org.dyndns.jkiddo.dmcp.chunks.media.extension.UnknownQk.class),
		ceQl("ceQl","unknown",DmapTypeDefinition.STRING_TYPE,org.dyndns.jkiddo.dmcp.chunks.media.extension.UnknownQl.class),
		ceQm("ceQm","unknown",DmapTypeDefinition.U_INT_TYPE,org.dyndns.jkiddo.dmcp.chunks.media.extension.UnknownQm.class),
		ceQS("ceQS","com.apple.itunes.playqueue-content-unknown",DmapTypeDefinition.CONTAINER_TYPE,org.dyndns.jkiddo.dmcp.chunks.media.extension.UnknownQS.class),
		ceQu("ceQu","com.apple.itunes.unknown-Qu",DmapTypeDefinition.U_BYTE_TYPE,org.dyndns.jkiddo.dmcp.chunks.media.extension.UnknownQU.class),
		cmpr("cmpr","dmcp.protocolversion",DmapTypeDefinition.VERSION_TYPE,org.dyndns.jkiddo.dmcp.chunks.media.MediaControlProtocolVersion.class),
		cmpa("cmpa","unknown.pa",DmapTypeDefinition.CONTAINER_TYPE,org.dyndns.jkiddo.dmcp.chunks.media.PairingContainer.class),
		cmpg("cmpg","com.apple.itunes.unknown-pg",DmapTypeDefinition.RAW_TYPE,org.dyndns.jkiddo.dmcp.chunks.media.PairingGuid.class),
		cmst("cmst","dmcp.playstatus",DmapTypeDefinition.CONTAINER_TYPE,org.dyndns.jkiddo.dmcp.chunks.media.PlayingStatus.class),
		cmgt("cmgt","dmcp.getpropertyresponse",DmapTypeDefinition.CONTAINER_TYPE,org.dyndns.jkiddo.dmcp.chunks.media.PropertyResponse.class),
		cmvo("cmvo","dmcp.volume",DmapTypeDefinition.U_INT_TYPE,org.dyndns.jkiddo.dmcp.chunks.media.RelativeVolume.class),
		cmsr("cmsr","dmcp.serverrevision",DmapTypeDefinition.U_INT_TYPE,org.dyndns.jkiddo.dmcp.chunks.media.StatusRevision.class),
		cmik("cmik","unknown-ik",DmapTypeDefinition.U_BYTE_TYPE,org.dyndns.jkiddo.dmcp.chunks.media.UnknownIK.class),
		cmrl("cmrl","unknown.rl",DmapTypeDefinition.U_BYTE_TYPE,org.dyndns.jkiddo.dmcp.chunks.media.UnknownRL.class),
		cmsp("cmsp","unknown-sp",DmapTypeDefinition.U_BYTE_TYPE,org.dyndns.jkiddo.dmcp.chunks.media.UnknownSP.class),
		cmsv("cmsv","unknown.sv",DmapTypeDefinition.U_BYTE_TYPE,org.dyndns.jkiddo.dmcp.chunks.media.UnknownSV.class),
		msau("msau","dmap.authenticationmethod",DmapTypeDefinition.U_BYTE_TYPE,org.dyndns.jkiddo.dmp.chunks.media.AuthenticationMethod.class),
		msas("msas","dmap.authenticationschemes",DmapTypeDefinition.U_BYTE_TYPE,org.dyndns.jkiddo.dmp.chunks.media.AuthenticationSchemes.class),
		mbcl("mbcl","dmap.bag",DmapTypeDefinition.CONTAINER_TYPE,org.dyndns.jkiddo.dmp.chunks.media.Bag.class),
		mcon("mcon","dmap.container",DmapTypeDefinition.CONTAINER_TYPE,org.dyndns.jkiddo.dmp.chunks.media.Container.class),
		mctc("mctc","dmap.containercount",DmapTypeDefinition.U_INT_TYPE,org.dyndns.jkiddo.dmp.chunks.media.ContainerCount.class),
		mcti("mcti","dmap.containeritemid",DmapTypeDefinition.U_INT_TYPE,org.dyndns.jkiddo.dmp.chunks.media.ContainerItemId.class),
		mcna("mcna","dmap.contentcodesname",DmapTypeDefinition.STRING_TYPE,org.dyndns.jkiddo.dmp.chunks.media.ContentCodesName.class),
		mcnm("mcnm","dmap.contentcodesnumber",DmapTypeDefinition.U_INT_TYPE,org.dyndns.jkiddo.dmp.chunks.media.ContentCodesNumber.class),
		mccr("mccr","dmap.contentcodesresponse",DmapTypeDefinition.CONTAINER_TYPE,org.dyndns.jkiddo.dmp.chunks.media.ContentCodesResponse.class),
		mcty("mcty","dmap.contentcodestype",DmapTypeDefinition.U_SHORT_TYPE,org.dyndns.jkiddo.dmp.chunks.media.ContentCodesType.class),
		msdc("msdc","dmap.databasescount",DmapTypeDefinition.U_INT_TYPE,org.dyndns.jkiddo.dmp.chunks.media.DatabaseCount.class),
		mdbk("mdbk","dmap.databasesharetype",DmapTypeDefinition.U_INT_TYPE,org.dyndns.jkiddo.dmp.chunks.media.DatabaseShareType.class),
		mudl("mudl","dmap.deletedidlisting",DmapTypeDefinition.CONTAINER_TYPE,org.dyndns.jkiddo.dmp.chunks.media.DeletedIdListing.class),
		mdcl("mdcl","dmap.dictionary",DmapTypeDefinition.CONTAINER_TYPE,org.dyndns.jkiddo.dmp.chunks.media.Dictionary.class),
		mdst("mdst","dmap.downloadstatus",DmapTypeDefinition.U_BYTE_TYPE,org.dyndns.jkiddo.dmp.chunks.media.DownloadStatus.class),
		meds("meds","dmap.editcommandssupported",DmapTypeDefinition.U_INT_TYPE,org.dyndns.jkiddo.dmp.chunks.media.EditCommandSupported.class),
		mimc("mimc","dmap.itemcount",DmapTypeDefinition.U_INT_TYPE,org.dyndns.jkiddo.dmp.chunks.media.ItemCount.class),
		miid("miid","dmap.itemid",DmapTypeDefinition.U_INT_TYPE,org.dyndns.jkiddo.dmp.chunks.media.ItemId.class),
		mikd("mikd","dmap.itemkind",DmapTypeDefinition.U_BYTE_TYPE,org.dyndns.jkiddo.dmp.chunks.media.ItemKind.class),
		minm("minm","dmap.itemname",DmapTypeDefinition.STRING_TYPE,org.dyndns.jkiddo.dmp.chunks.media.ItemName.class),
		mlcl("mlcl","dmap.listing",DmapTypeDefinition.CONTAINER_TYPE,org.dyndns.jkiddo.dmp.chunks.media.Listing.class),
		mlit("mlit","dmap.listingitem",DmapTypeDefinition.CONTAINER_TYPE,org.dyndns.jkiddo.dmp.chunks.media.ListingItem.class),
		mslr("mslr","dmap.loginrequired",DmapTypeDefinition.U_BYTE_TYPE,org.dyndns.jkiddo.dmp.chunks.media.LoginRequired.class),
		mlog("mlog","dmap.loginresponse",DmapTypeDefinition.CONTAINER_TYPE,org.dyndns.jkiddo.dmp.chunks.media.LoginResponse.class),
		mpro("mpro","dmap.protocolversion",DmapTypeDefinition.VERSION_TYPE,org.dyndns.jkiddo.dmp.chunks.media.MediaProtocolVersion.class),
		mpco("mpco","dmap.parentcontainerid",DmapTypeDefinition.U_INT_TYPE,org.dyndns.jkiddo.dmp.chunks.media.ParentContainerId.class),
		mper("mper","dmap.persistentid",DmapTypeDefinition.U_LONG_TYPE,org.dyndns.jkiddo.dmp.chunks.media.PersistentId.class),
		mrpr("mrpr","dmap.remotepersistentid",DmapTypeDefinition.U_LONG_TYPE,org.dyndns.jkiddo.dmp.chunks.media.RemotePersistentID.class),
		mrco("mrco","dmap.returnedcount",DmapTypeDefinition.U_INT_TYPE,org.dyndns.jkiddo.dmp.chunks.media.ReturnedCount.class),
		msrv("msrv","dmap.serverinforesponse",DmapTypeDefinition.CONTAINER_TYPE,org.dyndns.jkiddo.dmp.chunks.media.ServerInfoResponse.class),
		musr("musr","dmap.serverrevision",DmapTypeDefinition.U_INT_TYPE,org.dyndns.jkiddo.dmp.chunks.media.ServerRevision.class),
		mlid("mlid","dmap.sessionid",DmapTypeDefinition.U_INT_TYPE,org.dyndns.jkiddo.dmp.chunks.media.SessionId.class),
		mshc("mshc","dmap.sortingheaderchar",DmapTypeDefinition.U_SHORT_TYPE,org.dyndns.jkiddo.dmp.chunks.media.SortingHeaderChar.class),
		mshi("mshi","dmap.sortingheaderindex",DmapTypeDefinition.U_INT_TYPE,org.dyndns.jkiddo.dmp.chunks.media.SortingHeaderIndex.class),
		mshl("mshl","dmap.sortingheaderlisting",DmapTypeDefinition.CONTAINER_TYPE,org.dyndns.jkiddo.dmp.chunks.media.SortingHeaderListing.class),
		mshn("mshn","dmap.sortingheadernumber",DmapTypeDefinition.U_INT_TYPE,org.dyndns.jkiddo.dmp.chunks.media.SortingHeaderNumber.class),
		msma("msma","dmap.machineaddress",DmapTypeDefinition.U_LONG_TYPE,org.dyndns.jkiddo.dmp.chunks.media.MachineAddress.class),
		mtco("mtco","dmap.specifiedtotalcount",DmapTypeDefinition.U_INT_TYPE,org.dyndns.jkiddo.dmp.chunks.media.SpecifiedTotalCount.class),
		mstt("mstt","dmap.status",DmapTypeDefinition.U_INT_TYPE,org.dyndns.jkiddo.dmp.chunks.media.Status.class),
		msts("msts","dmap.statusstring",DmapTypeDefinition.STRING_TYPE,org.dyndns.jkiddo.dmp.chunks.media.StatusString.class),
		msal("msal","dmap.supportsautologout",DmapTypeDefinition.U_BYTE_TYPE,org.dyndns.jkiddo.dmp.chunks.media.SupportsAutoLogout.class),
		msbr("msbr","dmap.supportsbrowse",DmapTypeDefinition.U_BYTE_TYPE,org.dyndns.jkiddo.dmp.chunks.media.SupportsBrowse.class),
		msex("msex","dmap.supportsextensions",DmapTypeDefinition.U_BYTE_TYPE,org.dyndns.jkiddo.dmp.chunks.media.SupportsExtensions.class),
		msix("msix","dmap.supportsindex",DmapTypeDefinition.U_BYTE_TYPE,org.dyndns.jkiddo.dmp.chunks.media.SupportsIndex.class),
		mspi("mspi","dmap.supportspersistentids",DmapTypeDefinition.U_BYTE_TYPE,org.dyndns.jkiddo.dmp.chunks.media.SupportsPersistentIds.class),
		msed("msed","com.apple.itunes.unknown-ed",DmapTypeDefinition.U_BYTE_TYPE,org.dyndns.jkiddo.dmp.chunks.media.SupportsPlaylistEdit.class),
		msqy("msqy","dmap.supportsquery",DmapTypeDefinition.U_BYTE_TYPE,org.dyndns.jkiddo.dmp.chunks.media.SupportsQuery.class),
		msrs("msrs","dmap.supportsresolve",DmapTypeDefinition.U_BYTE_TYPE,org.dyndns.jkiddo.dmp.chunks.media.SupportsResolve.class),
		msup("msup","dmap.supportsupdate",DmapTypeDefinition.U_BYTE_TYPE,org.dyndns.jkiddo.dmp.chunks.media.SupportsUpdate.class),
		mstm("mstm","dmap.timeoutinterval",DmapTypeDefinition.U_INT_TYPE,org.dyndns.jkiddo.dmp.chunks.media.TimeoutInterval.class),
		msml("msml","dmap.speakermachinelist",DmapTypeDefinition.CONTAINER_TYPE,org.dyndns.jkiddo.dmp.chunks.media.SpeakerMachineList.class),
		mupd("mupd","dmap.updateresponse",DmapTypeDefinition.CONTAINER_TYPE,org.dyndns.jkiddo.dmp.chunks.media.UpdateResponse.class),
		muty("muty","dmap.updatetype",DmapTypeDefinition.U_BYTE_TYPE,org.dyndns.jkiddo.dmp.chunks.media.UpdateType.class),
		mstc("mstc","dmap.utctime",DmapTypeDefinition.DATE_TYPE,org.dyndns.jkiddo.dmp.chunks.media.UTCTime.class),
		msto("msto","dmap.utcoffset",DmapTypeDefinition.INT_TYPE,org.dyndns.jkiddo.dmp.chunks.media.UTCTimeOffset.class),
		fch("f�ch","dmap.contentcodesresponse",DmapTypeDefinition.CONTAINER_TYPE,org.dyndns.jkiddo.dmp.chunks.unknown.HasChildContainers.class),
		____("____","com.apple.itunes.req-fplay",DmapTypeDefinition.U_BYTE_TYPE,org.dyndns.jkiddo.dmp.chunks.unknown.ReqFplay.class),
		ceVO("ceVO","com.apple.itunes.unknown-voting",DmapTypeDefinition.U_BYTE_TYPE,org.dyndns.jkiddo.dmp.chunks.unknown.Voting.class),
		pasp("pasp","dpap.aspectratio",DmapTypeDefinition.STRING_TYPE,org.dyndns.jkiddo.dpap.chunks.picture.AspectRatio.class),
		picd("picd","dpap.creationdate",DmapTypeDefinition.DATE_TYPE,org.dyndns.jkiddo.dpap.chunks.picture.CreationDate.class),
		peak("peak","com.apple.itunes.photos.album-kind",DmapTypeDefinition.U_INT_TYPE,org.dyndns.jkiddo.dpap.chunks.picture.extension.AlbumKind.class),
		peed("peed","com.apple.itunes.photos.exposure-date",DmapTypeDefinition.DATE_TYPE,org.dyndns.jkiddo.dpap.chunks.picture.extension.ExposureDate.class),
		pefc("pefc","com.apple.itunes.photos.faces",DmapTypeDefinition.CONTAINER_TYPE,org.dyndns.jkiddo.dpap.chunks.picture.extension.Faces.class),
		peki("peki","com.apple.itunes.photos.key-image-id",DmapTypeDefinition.U_INT_TYPE,org.dyndns.jkiddo.dpap.chunks.picture.extension.KeyImageId.class),
		pemd("pemd","com.apple.itunes.photos.modification-date",DmapTypeDefinition.DATE_TYPE,org.dyndns.jkiddo.dpap.chunks.picture.extension.ModificationDate.class),
		pfai("pfai","dpap.failureids",DmapTypeDefinition.CONTAINER_TYPE,org.dyndns.jkiddo.dpap.chunks.picture.FailureIds.class),
		pfdt("pfdt","dpap.filedata",DmapTypeDefinition.RAW_TYPE,org.dyndns.jkiddo.dpap.chunks.picture.FileData.class),
		pcmt("pcmt","dpap.imagecomments",DmapTypeDefinition.STRING_TYPE,org.dyndns.jkiddo.dpap.chunks.picture.ImageComment.class),
		pimf("pimf","dpap.imagefilename",DmapTypeDefinition.STRING_TYPE,org.dyndns.jkiddo.dpap.chunks.picture.ImageFilename.class),
		pifs("pifs","dpap.imagefilesize",DmapTypeDefinition.U_INT_TYPE,org.dyndns.jkiddo.dpap.chunks.picture.ImageFileSize.class),
		pfmt("pfmt","dpap.imageformat",DmapTypeDefinition.STRING_TYPE,org.dyndns.jkiddo.dpap.chunks.picture.ImageFormat.class),
		plsz("plsz","dpap.imagelargefilesize",DmapTypeDefinition.U_LONG_TYPE,org.dyndns.jkiddo.dpap.chunks.picture.ImageLargeFileSize.class),
		phgt("phgt","dpap.imagepixelheight",DmapTypeDefinition.U_INT_TYPE,org.dyndns.jkiddo.dpap.chunks.picture.ImagePixelHeight.class),
		pwth("pwth","dpap.imagepixelwidth",DmapTypeDefinition.U_INT_TYPE,org.dyndns.jkiddo.dpap.chunks.picture.ImagePixelWidth.class),
		prat("prat","dpap.imagerating",DmapTypeDefinition.U_INT_TYPE,org.dyndns.jkiddo.dpap.chunks.picture.ImageRating.class),
		ppro("ppro","dpap.protocolversion",DmapTypeDefinition.VERSION_TYPE,org.dyndns.jkiddo.dpap.chunks.picture.PictureProtocolVersion.class),
		pret("pret","dpap.retryids",DmapTypeDefinition.CONTAINER_TYPE,org.dyndns.jkiddo.dpap.chunks.picture.RetryIds.class),
		ipsa("ipsa","dpap.iphotoslideshowadvancedoptions",DmapTypeDefinition.CONTAINER_TYPE,org.dyndns.jkiddo.dpap.chunks.picture.SlideShowAdvancedOptions.class),
		ipsl("ipsl","dpap.iphotoslideshowoptions",DmapTypeDefinition.CONTAINER_TYPE,org.dyndns.jkiddo.dpap.chunks.picture.SlideShowOptions.class),
		aeFA("aeFA", "com.apple.itunes.drm-family-id",DmapTypeDefinition.U_LONG_TYPE, org.dyndns.jkiddo.dmap.chunks.audio.extension.DRMFamilyId.class),
		aeDL("aeDL", "com.apple.itunes.drm-downloader-user-id",DmapTypeDefinition.U_LONG_TYPE, org.dyndns.jkiddo.dmap.chunks.audio.extension.DRMDownloaderUserId.class),
		ajcA("ajcA","com.apple.itunes.unknown-cA",DmapTypeDefinition.BYTE_TYPE, UnknowncA.class),
		ajal("ajal","com.apple.itunes.unknown-al",DmapTypeDefinition.BYTE_TYPE, Unknownal.class);

		private final String shortname;
		private final String longname;
		private final DmapTypeDefinition type;
		private final Class<? extends AbstractChunk> clazz;

		DmapChunkDefinition(final String shortname, final String longname, final DmapTypeDefinition type, final Class<? extends AbstractChunk> c)
		{
			this.shortname = shortname;
			this.longname = longname;
			this.type = type;
			this.clazz = c;
		}

		@Override
		public String getLongname()
		{
			return longname;
		}

		@Override
		public String getShortname()
		{
			return shortname;
		}

		@Override
		public DmapTypeDefinition getType()
		{
			return type;
		}

		public Class<? extends AbstractChunk> getClazz() {
			return clazz;
		}
		
		@Override
		public String toString() {
			return "DmapTypeDefinition [longname=" + longname + ", shortname=" + shortname + ", type=" + type + ", clazz=" + clazz+ "]";
		}
		
	}

	String getLongname();
	String getShortname();
	DmapTypeDefinition getType();
}
