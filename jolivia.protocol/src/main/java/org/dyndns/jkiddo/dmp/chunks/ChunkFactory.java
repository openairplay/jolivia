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

import java.util.HashMap;
import java.util.Map;

import org.dyndns.jkiddo.dmp.DmapUtil;
import org.dyndns.jkiddo.dmp.ProtocolViolationException;

public final class ChunkFactory
{

	private final Map<Integer, Class<? extends Chunk>> map = new HashMap<Integer, Class<? extends Chunk>>();

	public ChunkFactory()
	{
		map.put(new Integer(0x668D6368), org.dyndns.jkiddo.dmp.chunks.unknown.HasChildContainers.class); // f?ch
		map.put(new Integer(0x00000000), org.dyndns.jkiddo.dmp.chunks.unknown.ReqFplay.class); // ????

		map.put(new Integer(0x6D736175), org.dyndns.jkiddo.dmp.chunks.media.AuthenticationMethod.class); // msau
		map.put(new Integer(0x6D736173), org.dyndns.jkiddo.dmp.chunks.media.AuthenticationSchemes.class); // msas
		map.put(new Integer(0x6D62636C), org.dyndns.jkiddo.dmp.chunks.media.Bag.class); // mbcl
		map.put(new Integer(0x6D636F6E), org.dyndns.jkiddo.dmp.chunks.media.Container.class); // mcon
		map.put(new Integer(0x6D637463), org.dyndns.jkiddo.dmp.chunks.media.ContainerCount.class); // mctc
		map.put(new Integer(0x6D637469), org.dyndns.jkiddo.dmp.chunks.media.ContainerItemId.class); // mcti
		map.put(new Integer(0x6D636E61), org.dyndns.jkiddo.dmp.chunks.media.ContentCodesName.class); // mcna
		map.put(new Integer(0x6D636E6D), org.dyndns.jkiddo.dmp.chunks.media.ContentCodesNumber.class); // mcnm
		map.put(new Integer(0x6D636372), org.dyndns.jkiddo.dmp.chunks.media.ContentCodesResponse.class); // mccr
		map.put(new Integer(0x6D637479), org.dyndns.jkiddo.dmp.chunks.media.ContentCodesType.class); // mcty
		map.put(new Integer(0x6D736463), org.dyndns.jkiddo.dmp.chunks.media.DatabaseCount.class); // msdc
		map.put(new Integer(0x6D64626B), org.dyndns.jkiddo.dmp.chunks.media.DatabaseShareType.class); // mdbk
		map.put(new Integer(0x6D75646C), org.dyndns.jkiddo.dmp.chunks.media.DeletedIdListing.class); // mudl
		map.put(new Integer(0x6D64636C), org.dyndns.jkiddo.dmp.chunks.media.Dictionary.class); // mdcl
		map.put(new Integer(0x6D70726F), org.dyndns.jkiddo.dmp.chunks.media.MediaProtocolVersion.class); // mpro
		map.put(new Integer(0x6D656473), org.dyndns.jkiddo.dmp.chunks.media.EditCommandSupported.class); // meds
		map.put(new Integer(0x6D696D63), org.dyndns.jkiddo.dmp.chunks.media.ItemCount.class); // mimc
		map.put(new Integer(0x6D696964), org.dyndns.jkiddo.dmp.chunks.media.ItemId.class); // miid
		map.put(new Integer(0x6D696B64), org.dyndns.jkiddo.dmp.chunks.media.ItemKind.class); // mikd
		map.put(new Integer(0x6D696E6D), org.dyndns.jkiddo.dmp.chunks.media.ItemName.class); // minm
		map.put(new Integer(0x6D6C636C), org.dyndns.jkiddo.dmp.chunks.media.Listing.class); // mlcl
		map.put(new Integer(0x6D6C6974), org.dyndns.jkiddo.dmp.chunks.media.ListingItem.class); // mlit
		map.put(new Integer(0x6D736C72), org.dyndns.jkiddo.dmp.chunks.media.LoginRequired.class); // mslr
		map.put(new Integer(0x6D6C6F67), org.dyndns.jkiddo.dmp.chunks.media.LoginResponse.class); // mlog
		map.put(new Integer(0x6D70636F), org.dyndns.jkiddo.dmp.chunks.media.ParentContainerId.class); // mpco
		map.put(new Integer(0x6D706572), org.dyndns.jkiddo.dmp.chunks.media.PersistentId.class); // mper
		map.put(new Integer(0x6D727072), org.dyndns.jkiddo.dmp.chunks.media.RemotePersistentID.class); // mrpr
		map.put(new Integer(0x6D72636F), org.dyndns.jkiddo.dmp.chunks.media.ReturnedCount.class); // mrco
		map.put(new Integer(0x6D737276), org.dyndns.jkiddo.dmp.chunks.media.ServerInfoResponse.class); // msrv
		map.put(new Integer(0x6D757372), org.dyndns.jkiddo.dmp.chunks.media.ServerRevision.class); // musr
		map.put(new Integer(0x6D6C6964), org.dyndns.jkiddo.dmp.chunks.media.SessionId.class); // mlid
		map.put(new Integer(0x6D74636F), org.dyndns.jkiddo.dmp.chunks.media.SpecifiedTotalCount.class); // mtco
		map.put(new Integer(0x6D737474), org.dyndns.jkiddo.dmp.chunks.media.Status.class); // mstt
		map.put(new Integer(0x6D737473), org.dyndns.jkiddo.dmp.chunks.media.StatusString.class); // msts
		map.put(new Integer(0x6D73616C), org.dyndns.jkiddo.dmp.chunks.media.SupportsAutoLogout.class); // msal
		map.put(new Integer(0x6D736272), org.dyndns.jkiddo.dmp.chunks.media.SupportsBrowse.class); // msbr
		map.put(new Integer(0x6D736578), org.dyndns.jkiddo.dmp.chunks.media.SupportsExtensions.class); // msex
		map.put(new Integer(0x6D736978), org.dyndns.jkiddo.dmp.chunks.media.SupportsIndex.class); // msix
		map.put(new Integer(0x6D737069), org.dyndns.jkiddo.dmp.chunks.media.SupportsPersistentIds.class); // mspi
		map.put(new Integer(0x6D737179), org.dyndns.jkiddo.dmp.chunks.media.SupportsQuery.class); // msqy
		map.put(new Integer(0x6D737273), org.dyndns.jkiddo.dmp.chunks.media.SupportsResolve.class); // msrs
		map.put(new Integer(0x6D737570), org.dyndns.jkiddo.dmp.chunks.media.SupportsUpdate.class); // msup
		map.put(new Integer(0x6D73746D), org.dyndns.jkiddo.dmp.chunks.media.TimeoutInterval.class); // mstm
		map.put(new Integer(0x6D736D61), org.dyndns.jkiddo.dmp.chunks.media.SpeakerMacAddress.class); // msma
		map.put(new Integer(0x6D757064), org.dyndns.jkiddo.dmp.chunks.media.UpdateResponse.class); // mupd
		map.put(new Integer(0x6D757479), org.dyndns.jkiddo.dmp.chunks.media.UpdateType.class); // muty
		map.put(new Integer(0x6d73686c), org.dyndns.jkiddo.dmp.chunks.media.UnknownHL.class); // mshl
		map.put(new Integer(0x6d736863), org.dyndns.jkiddo.dmp.chunks.media.UnknownHC.class); // mshc
		map.put(new Integer(0x6d736869), org.dyndns.jkiddo.dmp.chunks.media.UnknownHI.class); // mshi
		map.put(new Integer(0x6d73686e), org.dyndns.jkiddo.dmp.chunks.media.UnknownHN.class); // mshn
		map.put(new Integer(0x6d736564), org.dyndns.jkiddo.dmp.chunks.media.Unknowned.class); // msed
		map.put(new Integer(0x6d736d6c), org.dyndns.jkiddo.dmp.chunks.media.Unknownml.class); // msml
		map.put(new Integer(0x6d737463), org.dyndns.jkiddo.dmp.chunks.media.UTCTime.class); // mstc
		map.put(new Integer(0x6d73746f), org.dyndns.jkiddo.dmp.chunks.media.UTCTimeOffset.class); // msto
		map.put(new Integer(0x6d647374), org.dyndns.jkiddo.dmp.chunks.media.DownloadStatus.class); // mdst

		map.put(new Integer(0x636D766F), org.dyndns.jkiddo.dmcp.chunks.media.RelativeVolume.class); // cmvo
		map.put(new Integer(0x63616961), org.dyndns.jkiddo.dmcp.chunks.media.audio.SpeakerActive.class); // caia
		map.put(new Integer(0x63617370), org.dyndns.jkiddo.dmcp.chunks.media.audio.SpeakerList.class); // casp
		map.put(new Integer(0x636D6774), org.dyndns.jkiddo.dmcp.chunks.media.PropertyResponse.class); // cmgt
		map.put(new Integer(0x63617664), org.dyndns.jkiddo.dmcp.chunks.media.audio.UnknownVD.class); // cavd
		map.put(new Integer(0x636D7374), org.dyndns.jkiddo.dmcp.chunks.media.PlayingStatus.class); // cmst
		map.put(new Integer(0x636D7372), org.dyndns.jkiddo.dmcp.chunks.media.StatusRevision.class); // cmsr
		map.put(new Integer(0x63617073), org.dyndns.jkiddo.dmcp.chunks.media.audio.PlayStatus.class); // caps
		map.put(new Integer(0x63617368), org.dyndns.jkiddo.dmcp.chunks.media.audio.ShuffleStatus.class); // cash
		map.put(new Integer(0x63617270), org.dyndns.jkiddo.dmcp.chunks.media.audio.RepeatStatus.class); // carp
		map.put(new Integer(0x63616673), org.dyndns.jkiddo.dmcp.chunks.media.audio.FullscreenStatus.class); // cafs
		map.put(new Integer(0x63617673), org.dyndns.jkiddo.dmcp.chunks.media.audio.VisualizerStatus.class); // cavs
		map.put(new Integer(0x63617663), org.dyndns.jkiddo.dmcp.chunks.media.audio.VolumeControllable.class); // cavc
		map.put(new Integer(0x63616173), org.dyndns.jkiddo.dmcp.chunks.media.audio.AvailableShuffleStates.class); // caas
		map.put(new Integer(0x63616172), org.dyndns.jkiddo.dmcp.chunks.media.audio.AvailableRepeatStates.class); // caar
		map.put(new Integer(0x63616665), org.dyndns.jkiddo.dmcp.chunks.media.audio.FullScreenEnabled.class); // cafe
		map.put(new Integer(0x63617665), org.dyndns.jkiddo.dmcp.chunks.media.audio.VisualizerEnabled.class); // cave
		map.put(new Integer(0x63617375), org.dyndns.jkiddo.dmcp.chunks.media.audio.UnknownSU.class); // casu
		map.put(new Integer(0x63616e70), org.dyndns.jkiddo.dmcp.chunks.media.audio.NowPlaying.class); // canp
		map.put(new Integer(0x63616e6e), org.dyndns.jkiddo.dmcp.chunks.media.audio.NowPlayingName.class); // cann
		map.put(new Integer(0x63616e61), org.dyndns.jkiddo.dmcp.chunks.media.audio.NowPlayingArtist.class); // cana
		map.put(new Integer(0x63616e6c), org.dyndns.jkiddo.dmcp.chunks.media.audio.NowPlayingAlbum.class); // canl
		map.put(new Integer(0x63616e67), org.dyndns.jkiddo.dmcp.chunks.media.audio.NowPlayingGenre.class); // cang
		map.put(new Integer(0x636d6d6b), org.dyndns.jkiddo.dmcp.chunks.media.CMediaKind.class); // cmmk
		map.put(new Integer(0x63617361), org.dyndns.jkiddo.dmcp.chunks.media.audio.UnknownSA.class); // casa
		map.put(new Integer(0x63616e74), org.dyndns.jkiddo.dmcp.chunks.media.audio.ProgressRemain.class); // cant
		map.put(new Integer(0x63617374), org.dyndns.jkiddo.dmcp.chunks.media.audio.ProgressTotal.class); // cant
		map.put(new Integer(0x63616473), org.dyndns.jkiddo.dmcp.chunks.media.audio.UnknownDS.class); // cant
		map.put(new Integer(0x63655152), org.dyndns.jkiddo.dmcp.chunks.media.audio.PlayQueueContentsResponse.class); // ceQR
		map.put(new Integer(0x63617363), org.dyndns.jkiddo.dmcp.chunks.media.audio.UnknownSC.class); // casc
		map.put(new Integer(0x63616b73), org.dyndns.jkiddo.dmcp.chunks.media.audio.UnknownKs.class); // caks
		
		
		map.put(new Integer(0x63655153), org.dyndns.jkiddo.dmcp.chunks.media.extension.UnknownQS.class); // ceQS
		
		map.put(new Integer(0x63655173), org.dyndns.jkiddo.dmcp.chunks.media.extension.PlayQueueId.class); // ceQs
		map.put(new Integer(0x6365516e), org.dyndns.jkiddo.dmcp.chunks.media.extension.PlayQueueName.class); // ceQn
		map.put(new Integer(0x6365516b), org.dyndns.jkiddo.dmcp.chunks.media.extension.UnknownQk.class); // ceQk
		map.put(new Integer(0x63655169), org.dyndns.jkiddo.dmcp.chunks.media.extension.UnknownQi.class); // ceQi
		map.put(new Integer(0x6365516d), org.dyndns.jkiddo.dmcp.chunks.media.extension.UnknownQm.class); // ceQm
		map.put(new Integer(0x6365516c), org.dyndns.jkiddo.dmcp.chunks.media.extension.UnknownQl.class); // ceQl
		map.put(new Integer(0x63655168), org.dyndns.jkiddo.dmcp.chunks.media.extension.UnknownQh.class); // ceQh
		map.put(new Integer(0x63655172), org.dyndns.jkiddo.dmcp.chunks.media.extension.PlayQueueArtist.class); // ceQr
		map.put(new Integer(0x63655161), org.dyndns.jkiddo.dmcp.chunks.media.extension.PlayQueueAlbum.class); // ceQa
		map.put(new Integer(0x63655167), org.dyndns.jkiddo.dmcp.chunks.media.extension.PlayQueueGenre.class); // ceQg
		map.put(new Integer(0x63655149), org.dyndns.jkiddo.dmcp.chunks.media.extension.UnknownceQI.class); // ceQI
		

		map.put(new Integer(0x63655175), org.dyndns.jkiddo.dmcp.chunks.media.extension.UnknownQU.class); // ceQu
		map.put(new Integer(0x63654753), org.dyndns.jkiddo.dmcp.chunks.media.extension.GeniusSelectable.class); // ceGs

		map.put(new Integer(0x70617370), org.dyndns.jkiddo.dpap.chunks.picture.AspectRatio.class); // pasp
		map.put(new Integer(0x70696364), org.dyndns.jkiddo.dpap.chunks.picture.CreationDate.class); // picd
		map.put(new Integer(0x70666474), org.dyndns.jkiddo.dpap.chunks.picture.FileData.class); // pfdt
		map.put(new Integer(0x70636d74), org.dyndns.jkiddo.dpap.chunks.picture.ImageComment.class); // pcmt
		map.put(new Integer(0x70696d66), org.dyndns.jkiddo.dpap.chunks.picture.ImageFilename.class); // pimf
		map.put(new Integer(0x70696673), org.dyndns.jkiddo.dpap.chunks.picture.ImageFileSize.class); // pifs
		map.put(new Integer(0x70666d74), org.dyndns.jkiddo.dpap.chunks.picture.ImageFormat.class); // pfmt
		map.put(new Integer(0x706c737a), org.dyndns.jkiddo.dpap.chunks.picture.ImageLargeFileSize.class); // plsz
		map.put(new Integer(0x70686774), org.dyndns.jkiddo.dpap.chunks.picture.ImagePixelHeight.class); // phgt
		map.put(new Integer(0x70777468), org.dyndns.jkiddo.dpap.chunks.picture.ImagePixelWidth.class); // pwth
		map.put(new Integer(0x70726174), org.dyndns.jkiddo.dpap.chunks.picture.ImageRating.class); // prat
		map.put(new Integer(0x7070726f), org.dyndns.jkiddo.dpap.chunks.picture.PictureProtocolVersion.class); // ppro

		map.put(new Integer(0x636d7061), org.dyndns.jkiddo.dmcp.chunks.media.PairingContainer.class); // cmpa
		map.put(new Integer(0x636d7067), org.dyndns.jkiddo.dmcp.chunks.media.PairingGuid.class); // cmpg
		map.put(new Integer(0x636d6e6d), org.dyndns.jkiddo.dmcp.chunks.media.DeviceName.class); // cmnm
		map.put(new Integer(0x636d7479), org.dyndns.jkiddo.dmcp.chunks.media.DeviceType.class); // cmty
		map.put(new Integer(0x63616369), org.dyndns.jkiddo.dmcp.chunks.media.audio.DataControlInt.class); // caci
		map.put(new Integer(0x636d696b), org.dyndns.jkiddo.dmcp.chunks.media.UnknownIK.class); // cmik
		map.put(new Integer(0x636d7072), org.dyndns.jkiddo.dmcp.chunks.media.MediaControlProtocolVersion.class); // cmpr
		map.put(new Integer(0x63617072), org.dyndns.jkiddo.dmcp.chunks.media.audio.AudioControlProtocolVersion.class); // capr
		map.put(new Integer(0x636d7370), org.dyndns.jkiddo.dmcp.chunks.media.UnknownSP.class); // cmsp
		map.put(new Integer(0x636d7376), org.dyndns.jkiddo.dmcp.chunks.media.UnknownSV.class); // cmsv
		map.put(new Integer(0x63617373), org.dyndns.jkiddo.dmcp.chunks.media.audio.UnknownSS.class); // cass
		map.put(new Integer(0x63616f76), org.dyndns.jkiddo.dmcp.chunks.media.audio.UnknownOV.class); // caov
		map.put(new Integer(0x636d726c), org.dyndns.jkiddo.dmcp.chunks.media.UnknownRL.class); // cmrl
		map.put(new Integer(0x63616976), org.dyndns.jkiddo.dmcp.chunks.media.audio.UnknownIV.class); // caiv
		map.put(new Integer(0x63616970), org.dyndns.jkiddo.dmcp.chunks.media.audio.UnknownIP.class); // caip

		map.put(new Integer(0x63655347), org.dyndns.jkiddo.dmcp.chunks.media.extension.SavedGenius.class); // ceSG
		map.put(new Integer(0x63655358), org.dyndns.jkiddo.dmcp.chunks.media.extension.UnknownCESX.class); // ceSX

		map.put(new Integer(0x61736161), org.dyndns.jkiddo.dmap.chunks.audio.AlbumArtist.class); // asaa
		map.put(new Integer(0x6162706C), org.dyndns.jkiddo.dmap.chunks.audio.BaseContainer.class); // abpl
		map.put(new Integer(0x6162616C), org.dyndns.jkiddo.dmap.chunks.audio.BrowseAlbumListing.class); // abal
		map.put(new Integer(0x61626172), org.dyndns.jkiddo.dmap.chunks.audio.BrowseArtistListing.class); // abar
		map.put(new Integer(0x61626370), org.dyndns.jkiddo.dmap.chunks.audio.BrowseComposerListing.class); // abcp
		map.put(new Integer(0x6162676E), org.dyndns.jkiddo.dmap.chunks.audio.BrowseGenreListing.class); // abgn
		map.put(new Integer(0x6170726F), org.dyndns.jkiddo.dmap.chunks.audio.AudioProtocolVersion.class); // apro
		map.put(new Integer(0x6162726F), org.dyndns.jkiddo.dmap.chunks.audio.DatabaseBrowse.class); // abro
		map.put(new Integer(0x61706C79), org.dyndns.jkiddo.dmap.chunks.audio.DatabaseContainerns.class); // aply
		map.put(new Integer(0x61646273), org.dyndns.jkiddo.dmap.chunks.audio.DatabaseItems.class); // adbs
		map.put(new Integer(0x6170726D), org.dyndns.jkiddo.dmap.chunks.audio.PlaylistRepeatMode.class); // aprm
		map.put(new Integer(0x6170736D), org.dyndns.jkiddo.dmap.chunks.audio.PlaylistShuffleMode.class); // apsm
		map.put(new Integer(0x6170736F), org.dyndns.jkiddo.dmap.chunks.audio.ItemsContainer.class); // apso
		map.put(new Integer(0x61727376), org.dyndns.jkiddo.dmap.chunks.audio.Resolve.class); // arsv
		map.put(new Integer(0x61726966), org.dyndns.jkiddo.dmap.chunks.audio.ResolveInfo.class); // arif
		map.put(new Integer(0x61766462), org.dyndns.jkiddo.dmap.chunks.audio.ServerDatabases.class); // avdb
		map.put(new Integer(0x6173616C), org.dyndns.jkiddo.dmap.chunks.audio.SongAlbum.class); // asal
		map.put(new Integer(0x61736172), org.dyndns.jkiddo.dmap.chunks.audio.SongArtist.class); // asar
		map.put(new Integer(0x61736274), org.dyndns.jkiddo.dmap.chunks.audio.SongBeatsPerMinute.class); // asbt
		map.put(new Integer(0x61736272), org.dyndns.jkiddo.dmap.chunks.audio.SongBitrate.class); // asbr
		map.put(new Integer(0x61736374), org.dyndns.jkiddo.dmap.chunks.audio.SongCategory.class); // asct
		map.put(new Integer(0x61736373), org.dyndns.jkiddo.dmap.chunks.audio.SongCodecSubtype.class); // ascs
		map.put(new Integer(0x61736364), org.dyndns.jkiddo.dmap.chunks.audio.SongCodecType.class); // ascd
		map.put(new Integer(0x6173636D), org.dyndns.jkiddo.dmap.chunks.audio.SongComment.class); // ascm
		map.put(new Integer(0x6173636F), org.dyndns.jkiddo.dmap.chunks.audio.SongCompilation.class); // asco
		map.put(new Integer(0x61736370), org.dyndns.jkiddo.dmap.chunks.audio.SongComposer.class); // ascp
		map.put(new Integer(0x6173636E), org.dyndns.jkiddo.dmap.chunks.audio.SongContentDescription.class); // ascn
		map.put(new Integer(0x61736372), org.dyndns.jkiddo.dmap.chunks.audio.SongContentRating.class); // ascr
		map.put(new Integer(0x6173646B), org.dyndns.jkiddo.dmap.chunks.audio.SongDataKind.class); // asdk
		map.put(new Integer(0x6173756C), org.dyndns.jkiddo.dmap.chunks.audio.SongDataUrl.class); // asul
		map.put(new Integer(0x61736461), org.dyndns.jkiddo.dmap.chunks.audio.SongDateAdded.class); // asda
		map.put(new Integer(0x6173646D), org.dyndns.jkiddo.dmap.chunks.audio.SongDateModified.class); // asdm
		map.put(new Integer(0x61736474), org.dyndns.jkiddo.dmap.chunks.audio.SongDescription.class); // asdt
		map.put(new Integer(0x61736462), org.dyndns.jkiddo.dmap.chunks.audio.SongDisabled.class); // asdb
		map.put(new Integer(0x61736463), org.dyndns.jkiddo.dmap.chunks.audio.SongDiscCount.class); // asdc
		map.put(new Integer(0x6173646E), org.dyndns.jkiddo.dmap.chunks.audio.SongDiscNumber.class); // asdn
		map.put(new Integer(0x61736571), org.dyndns.jkiddo.dmap.chunks.audio.SongEqPreset.class); // aseq
		map.put(new Integer(0x6173666D), org.dyndns.jkiddo.dmap.chunks.audio.SongFormat.class); // asfm
		map.put(new Integer(0x6173676E), org.dyndns.jkiddo.dmap.chunks.audio.SongGenre.class); // asgn
		map.put(new Integer(0x61677270), org.dyndns.jkiddo.dmap.chunks.audio.SongGrouping.class); // agrp
		map.put(new Integer(0x61736B79), org.dyndns.jkiddo.dmap.chunks.audio.SongKeywords.class); // asky
		map.put(new Integer(0x61736C63), org.dyndns.jkiddo.dmap.chunks.audio.SongLongDescription.class); // aslc
		map.put(new Integer(0x61737276), org.dyndns.jkiddo.dmap.chunks.audio.SongRelativeVolume.class); // asrv
		map.put(new Integer(0x61737372), org.dyndns.jkiddo.dmap.chunks.audio.SongSampleRate.class); // assr
		map.put(new Integer(0x6173737A), org.dyndns.jkiddo.dmap.chunks.audio.SongSize.class); // assz
		map.put(new Integer(0x61737374), org.dyndns.jkiddo.dmap.chunks.audio.SongStartTime.class); // asst
		map.put(new Integer(0x61737370), org.dyndns.jkiddo.dmap.chunks.audio.SongStopTime.class); // assp
		map.put(new Integer(0x6173746D), org.dyndns.jkiddo.dmap.chunks.audio.SongTime.class); // astm
		map.put(new Integer(0x61737463), org.dyndns.jkiddo.dmap.chunks.audio.SongTrackCount.class); // astc
		map.put(new Integer(0x6173746E), org.dyndns.jkiddo.dmap.chunks.audio.SongTrackNumber.class); // astn
		map.put(new Integer(0x61737572), org.dyndns.jkiddo.dmap.chunks.audio.SongUserRating.class); // asur
		map.put(new Integer(0x61737972), org.dyndns.jkiddo.dmap.chunks.audio.SongYear.class); // asyr
		map.put(new Integer(0x61736169), org.dyndns.jkiddo.dmap.chunks.audio.SongAlbumId.class); // asai
		map.put(new Integer(0x6167616c), org.dyndns.jkiddo.dmap.chunks.audio.AlbumSearchContainer.class); // agal
		map.put(new Integer(0x61746564), org.dyndns.jkiddo.dmap.chunks.audio.SupportsExtraData.class); // ated
		map.put(new Integer(0x61736772), org.dyndns.jkiddo.dmap.chunks.audio.SupportsGroups.class); // asgr
		map.put(new Integer(0x61737365), org.dyndns.jkiddo.dmap.chunks.audio.UnknownSE.class); // asse
		map.put(new Integer(0x61676163), org.dyndns.jkiddo.dmap.chunks.audio.GroupAlbumCount.class); // agac
		map.put(new Integer(0x61737269), org.dyndns.jkiddo.dmap.chunks.audio.SongArtistId.class); // asri
		map.put(new Integer(0x61736770), org.dyndns.jkiddo.dmap.chunks.audio.SongGapless.class); // asgp
		map.put(new Integer(0x61736564), org.dyndns.jkiddo.dmap.chunks.audio.SongExtraData.class); // ased
		map.put(new Integer(0x61736870), org.dyndns.jkiddo.dmap.chunks.audio.SongHasBeenPlayed.class); // ashp
		map.put(new Integer(0x6173736e), org.dyndns.jkiddo.dmap.chunks.audio.SortName.class); // assn
		map.put(new Integer(0x61737361), org.dyndns.jkiddo.dmap.chunks.audio.SortArtist.class); // assa
		map.put(new Integer(0x6173736c), org.dyndns.jkiddo.dmap.chunks.audio.SortAlbumArtist.class); // assl
		map.put(new Integer(0x61737375), org.dyndns.jkiddo.dmap.chunks.audio.SortAlbum.class); // assu
		map.put(new Integer(0x61737363), org.dyndns.jkiddo.dmap.chunks.audio.SortComposer.class); // assc
		map.put(new Integer(0x61737373), org.dyndns.jkiddo.dmap.chunks.audio.SortSeriesName.class); // asss
		map.put(new Integer(0x6173626b), org.dyndns.jkiddo.dmap.chunks.audio.SortBookmarkable.class); // asbk
		map.put(new Integer(0x61737075), org.dyndns.jkiddo.dmap.chunks.audio.SongPodcastUrl.class); // aspu
		map.put(new Integer(0x61736c73), org.dyndns.jkiddo.dmap.chunks.audio.SongLongSize.class); // asls
		map.put(new Integer(0x61736173), org.dyndns.jkiddo.dmap.chunks.audio.SongAlbumUserRatingStatus.class); // asas
		map.put(new Integer(0x61736c72), org.dyndns.jkiddo.dmap.chunks.audio.SongAlbumUserRating.class); // aslr
		map.put(new Integer(0x61736573), org.dyndns.jkiddo.dmap.chunks.audio.SongExcludeFromShuffle.class); // ases
		map.put(new Integer(0x61736b64), org.dyndns.jkiddo.dmap.chunks.audio.SongLastSkipDate.class); // askd
		map.put(new Integer(0x61736163), org.dyndns.jkiddo.dmap.chunks.audio.SongArtworkCount.class); // asac
		map.put(new Integer(0x61736b70), org.dyndns.jkiddo.dmap.chunks.audio.SongUserSkipCount.class); // askp
		map.put(new Integer(0x6173706c), org.dyndns.jkiddo.dmap.chunks.audio.SongDatePlayed.class); // aspl
		map.put(new Integer(0x61737063), org.dyndns.jkiddo.dmap.chunks.audio.SongUserPlayCount.class); // aspc
		map.put(new Integer(0x61676172), org.dyndns.jkiddo.dmap.chunks.audio.ArtistSearchContainer.class); // agar
		map.put(new Integer(0x61736472), org.dyndns.jkiddo.dmap.chunks.audio.SongDateReleased.class); // asdr
		map.put(new Integer(0x61736470), org.dyndns.jkiddo.dmap.chunks.audio.SongDatePurchased.class); // asdp
		
		map.put(new Integer(0x6d736375), org.dyndns.jkiddo.dmap.chunks.audio.UnknownCU.class); // mscu

		map.put(new Integer(0x61655347), org.dyndns.jkiddo.dmap.chunks.audio.extension.SavedGenius.class); // aeSG
		map.put(new Integer(0x61654353), org.dyndns.jkiddo.dmap.chunks.audio.extension.ArtworkChecksum.class); // aeCS
		map.put(new Integer(0x61655043), org.dyndns.jkiddo.dmap.chunks.audio.extension.Podcast.class); // aePC
		map.put(new Integer(0x61655050), org.dyndns.jkiddo.dmap.chunks.audio.extension.PodcastPlaylist.class); // aePP
		map.put(new Integer(0x61655350), org.dyndns.jkiddo.dmap.chunks.audio.extension.SmartPlaylist.class); // aeSP
		map.put(new Integer(0x61655053), org.dyndns.jkiddo.dmap.chunks.audio.extension.SpecialPlaylist.class); // aePS
		map.put(new Integer(0x6165494D), org.dyndns.jkiddo.dmap.chunks.audio.extension.UnknownIM.class); // aeIM
		map.put(new Integer(0x6165524D), org.dyndns.jkiddo.dmap.chunks.audio.extension.UnknownRM.class); // aeRM
		map.put(new Integer(0x61654773), org.dyndns.jkiddo.dmap.chunks.audio.extension.GeniusSeed.class); // aeGs
		map.put(new Integer(0x61654d51), org.dyndns.jkiddo.dmap.chunks.audio.extension.UnknownMQ.class); // aeMQ
		map.put(new Integer(0x61654652), org.dyndns.jkiddo.dmap.chunks.audio.extension.UnknownFR.class); // aeFR
		map.put(new Integer(0x61655472), org.dyndns.jkiddo.dmap.chunks.audio.extension.UnknownTr.class); // aeTr
		map.put(new Integer(0x6165534c), org.dyndns.jkiddo.dmap.chunks.audio.extension.UnknownSL.class); // aeSL
		map.put(new Integer(0x61655352), org.dyndns.jkiddo.dmap.chunks.audio.extension.UnknownSR.class); // aeSR
		map.put(new Integer(0x61654650), org.dyndns.jkiddo.dmap.chunks.audio.extension.SupportsFairPlay.class); // aeFP
		map.put(new Integer(0x61655358), org.dyndns.jkiddo.dmap.chunks.audio.extension.UnknownSX.class); // aeSX
		map.put(new Integer(0x6165534e), org.dyndns.jkiddo.dmap.chunks.audio.extension.SeriesName.class); // aeSN
		map.put(new Integer(0x61654e4e), org.dyndns.jkiddo.dmap.chunks.audio.extension.NetworkName.class); // aeNN
		map.put(new Integer(0x6165454e), org.dyndns.jkiddo.dmap.chunks.audio.extension.EpisodeNumberString.class); // aeEN
		map.put(new Integer(0x61654553), org.dyndns.jkiddo.dmap.chunks.audio.extension.EpisodeSort.class); // aeES
		map.put(new Integer(0x61655355), org.dyndns.jkiddo.dmap.chunks.audio.extension.SeasonNumber.class); // aeSU
		map.put(new Integer(0x61654748), org.dyndns.jkiddo.dmap.chunks.audio.extension.GaplessHeur.class); // aeGH
		map.put(new Integer(0x61654744), org.dyndns.jkiddo.dmap.chunks.audio.extension.GaplessEncodingDr.class); // aeGD
		map.put(new Integer(0x61654755), org.dyndns.jkiddo.dmap.chunks.audio.extension.GaplessDuration.class); // aeGU
		map.put(new Integer(0x61654752), org.dyndns.jkiddo.dmap.chunks.audio.extension.GaplessResy.class); // aeGR
		map.put(new Integer(0x61654745), org.dyndns.jkiddo.dmap.chunks.audio.extension.GaplessEncodingDel.class); // aeGE
		map.put(new Integer(0x61654352), org.dyndns.jkiddo.dmap.chunks.audio.extension.ContentRating.class); // aeCR
		map.put(new Integer(0x61655345), org.dyndns.jkiddo.dmap.chunks.audio.extension.StorePersistentId.class); // aeSE
		map.put(new Integer(0x61654456), org.dyndns.jkiddo.dmap.chunks.audio.extension.DRMVersions.class); // aeDV
		map.put(new Integer(0x61654450), org.dyndns.jkiddo.dmap.chunks.audio.extension.DRMPlatformId.class); // aeDP
		map.put(new Integer(0x61737273), org.dyndns.jkiddo.dmap.chunks.audio.extension.SongUserRatingStatus.class); // asrs
		map.put(new Integer(0x61654d58), org.dyndns.jkiddo.dmap.chunks.audio.extension.MovieInfoXml.class); // aeMX
		map.put(new Integer(0x61655844), org.dyndns.jkiddo.dmap.chunks.audio.extension.Xid.class); // aeXD
		map.put(new Integer(0x61654b32), org.dyndns.jkiddo.dmap.chunks.audio.extension.DRMKey2Id.class); // aeK2
		map.put(new Integer(0x61654b31), org.dyndns.jkiddo.dmap.chunks.audio.extension.DRMKey1Id.class); // aeK1
		map.put(new Integer(0x61654e44), org.dyndns.jkiddo.dmap.chunks.audio.extension.NonDRMUserId.class); // aeND
		map.put(new Integer(0x61654452), org.dyndns.jkiddo.dmap.chunks.audio.extension.DRMUserId.class); // aeDR
		map.put(new Integer(0x61654D6B), org.dyndns.jkiddo.dmap.chunks.audio.extension.ExtendedMediaKind.class); // aeMk
		map.put(new Integer(0x61654856), org.dyndns.jkiddo.dmap.chunks.audio.extension.HasVideo.class); // aeHV
		map.put(new Integer(0x61654149), org.dyndns.jkiddo.dmap.chunks.audio.extension.ITMSArtistId.class); // aeAI
		map.put(new Integer(0x61654349), org.dyndns.jkiddo.dmap.chunks.audio.extension.ITMSComposerId.class); // aeCI
		map.put(new Integer(0x61654749), org.dyndns.jkiddo.dmap.chunks.audio.extension.ITMSGenreId.class); // aeGI
		map.put(new Integer(0x61655049), org.dyndns.jkiddo.dmap.chunks.audio.extension.ITMSPlaylistId.class); // aePI
		map.put(new Integer(0x61655349), org.dyndns.jkiddo.dmap.chunks.audio.extension.ITMSSongId.class); // aeSI
		map.put(new Integer(0x61655346), org.dyndns.jkiddo.dmap.chunks.audio.extension.ITMSStorefrontId.class); // aeSF
		map.put(new Integer(0x61654D4B), org.dyndns.jkiddo.dmap.chunks.audio.extension.MediaKind.class); // aeMK
		map.put(new Integer(0x61655356), org.dyndns.jkiddo.dmap.chunks.audio.extension.MusicSharingVersion.class); // aeSV
		map.put(new Integer(0x61654E56), org.dyndns.jkiddo.dmap.chunks.audio.extension.NormVolume.class); // aeNV
		map.put(new Integer(0x61654373), org.dyndns.jkiddo.dmap.chunks.audio.extension.UnknownCS.class); // aeCs
		map.put(new Integer(0x61654844), org.dyndns.jkiddo.dmap.chunks.audio.extension.UnknownHD.class); // aeHD
		
	}

	public Class<? extends Chunk> getChunkClass(Integer contentCode)
	{
		return map.get(contentCode);
	}

	public Chunk newChunk(int contentCode) throws ProtocolViolationException
	{
		Class<? extends Chunk> clazz = getChunkClass(new Integer(contentCode));
		try
		{
			return clazz.newInstance();
		}
		catch(Exception err)
		{ 
			throw new ProtocolViolationException("Content code: " + DmapUtil.toContentCodeString(contentCode) + " not found. Hash is 0x" + Integer.toHexString(new Integer(contentCode)), err);
		}
	}
}
