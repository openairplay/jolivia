//
//	@Override
//	protected ServiceInfo getServiceInfoToRegister()
//	{
//		String hash = Integer.toHexString(hostname.hashCode()).toUpperCase();
//		hash = (hash + hash).substring(0, 13);
//		HashMap<String, String> records = new HashMap<String, String>();
//		records.put(TXT_VERSION_KEY, TXT_VERSION);
//		records.put(DPAP_VERSION_KEY, DmapUtil.PPRO_VERSION_200 + "");
//		records.put(IPSH_VERSION_KEY, DmapUtil.MUSIC_SHARING_VERSION_200 + "");
//		records.put(MACHINE_ID_KEY, hash);
//		records.put(PASSWORD_KEY, "0");
//		return ServiceInfo.create(DPAP_SERVICE_TYPE, name, port, 0, 0, records);
//	}
//
//	@Override
//	@Path("/server-info")
//	@GET
//	public Response serverInfo(@Context HttpServletRequest httpServletRequest, @Context HttpServletResponse httpServletResponse, @Context UriInfo info) throws IOException
//	{
//		ServerInfoResponse serverInfoResponse = new ServerInfoResponse();
//		serverInfoResponse.add(new Status(200));
//		serverInfoResponse.add(new DmapProtocolVersion(DmapUtil.PPRO_VERSION_200));
//		serverInfoResponse.add(new ProtocolVersion(DmapUtil.DPAP_VERSION_101));
//		serverInfoResponse.add(new ItemName(imageLibraryManager.getLibraryName()));
//		serverInfoResponse.add(new LoginRequired(false));
//		serverInfoResponse.add(new TimeoutInterval(1800));
//		serverInfoResponse.add(new SupportsAutoLogout(false));
//		serverInfoResponse.add(new SupportsIndex(false));
//		serverInfoResponse.add(new DatabaseCount(imageLibraryManager.getNrOfDatabases()));
//
//		return Util.buildResponse(serverInfoResponse, DMAP_KEY, imageLibraryManager.getLibraryName());
//	}

//	@Override
//	@Path("/databases/{databaseId}/items")
//	@GET
//	public Response items(@Context HttpServletRequest httpServletRequest, @Context HttpServletResponse httpServletResponse, @PathParam("databaseId") long databaseId, @QueryParam("session-id") long sessionId, @QueryParam("revision-number") long revisionNumber, @QueryParam("delta") long delta, @QueryParam("type") String type, @QueryParam("meta") String meta, @QueryParam("query") String query) throws Exception
//	{
//		// http://192.168.1.2dpap://192.168.1.2:8770/databases/1/items?session-id=1101478641&meta=dpap.thumb,dmap.itemid,dpap.filedata&query=('dmap.itemid:2730','dmap.itemid:2731','dmap.itemid:2732','dmap.itemid:2733','dmap.itemid:2734','dmap.itemid:2735','dmap.itemid:2736','dmap.itemid:2737','dmap.itemid:2738','dmap.itemid:2739','dmap.itemid:2740','dmap.itemid:2741','dmap.itemid:2742','dmap.itemid:2743','dmap.itemid:2744','dmap.itemid:2745','dmap.itemid:2746','dmap.itemid:2747','dmap.itemid:2748','dmap.itemid:2749')
//		// Picture request
//		// GET dpap://192.168.1.2:8770/databases/1/items?session-id=1101478641&meta=dpap.hires,dmap.itemid,dpap.filedata&query=('dmap.itemid:2742') HTTP/1.1
//		//
//		// 5487
//		// HTTP/1.1 200 OK
//		// Date: Fri, 18 Jan 2013 18:57:33 GMT
//		// DPAP-Server: iPhoto/9.2.1 (Mac OS X)
//		// Content-Type: application/x-dmap-tagged
//		// Content-Length: 6510908
//
//		// Number of listings should be returned according to query string
//
//		Set<Item> images = imageLibraryManager.getDatabase(databaseId).getItems();
//		Iterable<String> parameters = DmapUtil.parseMeta(meta);
//
//		DatabaseItems databaseImages = new DatabaseItems();
//		databaseImages.add(new Status(200));
//		databaseImages.add(new UpdateType(0));
//		databaseImages.add(new SpecifiedTotalCount(images.size()));
//		databaseImages.add(new ReturnedCount(images.size()));
//
//		Listing listing = new Listing();
//		for(Item item : images)
//		{
//			ListingItem listingItem = new ListingItem();
//
//			for(String key : parameters)
//			{
//				Chunk chunk = item.getChunk(key);
//
//				if(chunk != null)
//				{
//					listingItem.add(chunk);
//				}
//				else
//				{
//					logger.debug("Unknown chunk type: " + key);
//				}
//			}
//
//			// TODO Remember to add FileData/pfdt - maybe resized
//
//			listing.add(listingItem);
//		}
//
//		databaseImages.add(listing);
//
//		// if(request.isUpdateType() && deletedSongs != null)
//		// {
//		// DeletedIdListing deletedListing = new DeletedIdListing();
//		//
//		// for(Song song : deletedSongs)
//		// {
//		// deletedListing.add(song.getChunk("dmap.itemid"));
//		// }
//		//
//		// databaseSongs.add(deletedListing);
//		// }
//
//		return Util.buildResponse(databaseImages, DMAP_KEY, imageLibraryManager.getLibraryName());
//	}
//
//	@Override
//	@Path("/databases/{databaseId}/containers")
//	@GET
//	public Response containers(@Context HttpServletRequest httpServletRequest, @Context HttpServletResponse httpServletResponse, @PathParam("databaseId") long databaseId, @QueryParam("session-id") long sessionId, @QueryParam("revision-number") long revisionNumber, @QueryParam("delta") long delta, @QueryParam("meta") String meta) throws IOException
//	{
//		// See how it is done in MusicLibraryResource
//		// aply
//		// mstt
//		// muty
//		// mtco
//		// mrco
//		// mlcl
//		// mlitL
//		// mikd
//		// miid
//		// minmSome Name bibliotek
//		// abpl
//		// mimc
//		// mlit3
//		// mikd
//		// miid
//		// minm03/04/2007
//		// mimc
//		// mlit4
//		// mikd
//		// miid
//		// minmLast Import
//		// mimc
//		// mlit7
//		// mikd
//		// miid
//		// minm
//		// Last 12 Monthsmimcmlit2mikdmiidminm tom mappemimcmlit:mikdmiidminmAlbum uden navn 3mimcmlit4mikdmiid9minmFamiliefestmimcmlit?mikdmiidminmEmner til fremkaldelsemimc0mlit:mikdmiid%minmAlbum uden navn 2mimcmlit0mikdmiidminmBettinamimcmlit1mikdmiidminmKalendermimc1mlit2mikdmiid!minm Fastelavnmimcmlit3mikdmiidminm
//		// Kbenhavnmimcmlit0mikdmiidminmFlaggedmimcmlit.mikdmiidminmTrashmimc
//
//		Collection<Container> playlists = imageLibraryManager.getDatabase(databaseId).getContainers();
//		Iterable<String> parameters = DmapUtil.parseMeta(meta);
//
//		DatabaseContainerns databasePlaylists = new DatabaseContainerns();
//
//		databasePlaylists.add(new Status(200));
//		databasePlaylists.add(new UpdateType(0));// Maybe used
//
//		databasePlaylists.add(new SpecifiedTotalCount(playlists.size()));
//		// databasePlaylists.add(new SpecifiedTotalCount(0));
//		databasePlaylists.add(new ReturnedCount(playlists.size()));
//
//		Listing listing = new Listing();
//
//		// iTunes got a bug - if playlists is a empty set, it keeps quering
//		for(Container playlist : playlists)
//		{
//			ListingItem listingItem = new ListingItem();
//
//			for(String key : parameters)
//			{
//				Chunk chunk = playlist.getChunk(key);
//
//				if(chunk != null)
//				{
//					listingItem.add(chunk);
//				}
//				else
//				{
//					logger.debug("Unknown chunk type: " + key);
//				}
//			}
//
//			listing.add(listingItem);
//
//			// TODO if playlist is a 'base playlist', remember to add BasePlaylist/abpl
//		}
//
//		databasePlaylists.add(listing);
//
//		// if(request.isUpdateType() && deletedPlaylists != null)
//		// {
//		// DeletedIdListing deletedListing = new DeletedIdListing();
//		//
//		// for(Playlist playlist : deletedPlaylists)
//		// {
//		// deletedListing.add(new ItemId(playlist.getItemId()));
//		// }
//		//
//		// databasePlaylists.add(deletedListing);
//		// }
//
//		return Util.buildResponse(databasePlaylists, DMAP_KEY, imageLibraryManager.getLibraryName());
//	}
//
//	@Override
//	@Path("/databases/{databaseId}/containers/{containerId}/items")
//	@GET
//	public Response containerItems(@Context HttpServletRequest httpServletRequest, @Context HttpServletResponse httpServletResponse, @PathParam("containerId") long containerId, @PathParam("databaseId") long databaseId, @QueryParam("session-id") long sessionId, @QueryParam("revision-number") long revisionNumber, @QueryParam("delta") long delta, @QueryParam("meta") String meta, @QueryParam("type") String type, @QueryParam("group-type") String group_type, @QueryParam("sort") String sort, @QueryParam("include-sort-headers") String include_sort_headers, @QueryParam("query") String query, @QueryParam("index") String index) throws IOException
//	{
//		// http://192.168.1.2dpap://192.168.1.2:8770/databases/1/containers/5292/items?session-id=1101478641&meta=dpap.aspectratio,dmap.itemid,dmap.itemname,dpap.imagefilename,dpap.imagefilesize,dpap.creationdate,dpap.imagepixelwidth,dpap.imagepixelheight,dpap.imageformat,dpap.imagerating,dpap.imagecomments,dpap.imagelargefilesize&type=photo
//		// See how it is done in MusicLibraryResource
//		// apso)
//		// mstt
//		// muty
//		// mtco
//		// mrco
//		// mlcl)z
//		// mlit
//		// mikd
//		// pasp1.5
//		// picd8m5p
//		// miid
//		// pimfIMG_0041.JPG
//		// minmIMG_0041.JPG
//		// pifsf[
//		// pwth
//		// phgt
//		// pfmtJPEG
//		// prat
//		// plszf[
//		// mlitmikdpasp1.5picd8m5pmiid
//		// pimfIMG_0043.JPGminmIMG_0043.JPGpifsuYpwthphgt pfmtJPEGpratplszuYmlitmikdpasp1.5picd8m5qmiid
//		// pimfIMG_0026.JPGminmIMG_0026.JPGpifsJpwthphgt pfmtJPEGpratplszJmlitmikdpasp1.5picd8m5qmiid
//		// pimfIMG_0039.JPGminmIMG_0039.JPGpifs]3pwthphgt pfmtJPEGpratplsz]3mlitmikdpasp1.5picd8m5qmiid
//		// pimfIMG_0047.JPGminmIMG_0047.JPGpifsnpwthphgt pfmtJPEGpratplsznmlitmikdpasp1.5picd8m5qmiid
//		// pimfIMG_0065.JPGminmIMG_0065.JPGpifs`pwthphgt pfmtJPEGpratplsz`mlitmikdpasp1.5picd8m5qmiid
//		// pimfIMG_0077.JPGminmIMG_0077.JPGpifs?"pwthphgt pfmtJPEGpratplsz?"mlitmikdpasp1.5picd8m5rmiid
//		// pimfIMG_0045.JPGminmIMG_0045.JPGpifs`\pwthphgt pfmtJPEGpratplsz`\mlitmikdpasp1.5picd8m5rm
//		// throw new NotImplementedException();
//		// /databases/0/containers/1/items?session-id=1570434761&revision-number=2&delta=0&type=music&meta=dmap.itemkind,dmap.itemid,dmap.containeritemid
//		Container container = imageLibraryManager.getDatabase(databaseId).getPlaylist(containerId);
//		Iterable<String> parameters = DmapUtil.parseMeta(meta);
//		ItemsContainer itemsContainer = new ItemsContainer();
//
//		itemsContainer.add(new Status(200));
//		// playlistSongs.add(new UpdateType(request.isUpdateType() ? 1 : 0));
//		itemsContainer.add(new UpdateType(0));
//		itemsContainer.add(new SpecifiedTotalCount(container.getItems().size()));
//		itemsContainer.add(new ReturnedCount(container.getItems().size()));
//
//		Listing listing = new Listing();
//
//		for(Item item : container.getItems())
//		{
//			ListingItem listingItem = new ListingItem();
//
//			for(String key : parameters)
//			{
//				Chunk chunk = item.getChunk(key);
//
//				if(chunk != null)
//				{
//					listingItem.add(chunk);
//				}
//				else
//				{
//					logger.debug("Unknown chunk type: " + key);
//				}
//			}
//
//			listing.add(listingItem);
//
//			// TODO
//			// Each listing should contain the following:
//			// pasp1.5
//			// picd8m5p
//			// miid
//			// pimfIMG_0041.JPG
//			// minmIMG_0041.JPG
//			// pifsf[
//			// pwth
//			// phgt
//			// pfmtJPEG
//			// prat
//			// plszf[
//		}
//
//		itemsContainer.add(listing);
//
//		// if(request.isUpdateType() && deletedSongs != null)
//		// {
//		// DeletedIdListing deletedListing = new DeletedIdListing();
//		//
//		// for(Song song : deletedSongs)
//		// {
//		// deletedListing.add(song.getChunk("dmap.itemid"));
//		// }
//		// playlistSongs.add(deletedListing);
//		// }
//
//		return Util.buildResponse(itemsContainer, DMAP_KEY, imageLibraryManager.getLibraryName());
//	}

//}
