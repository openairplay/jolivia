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
import org.dyndns.jkiddo.daap.client.RequestHelper;
import org.dyndns.jkiddo.protocol.dmap.chunks.dmap.ContentCodesResponse;
import org.dyndns.jkiddo.protocol.dmap.chunks.dmap.ServerInfoResponse;
import org.junit.Test;

public class TestClient
{
	@Test
	public void testGetContentCodes() throws Exception
	{
		ContentCodesResponse ccr = RequestHelper.requestParsed("http://localhost:3689/content-codes");
		System.out.println(ccr);
		ServerInfoResponse sir = RequestHelper.requestParsed("http://localhost:3689/server-info");
		System.out.println(sir);
	}
}
