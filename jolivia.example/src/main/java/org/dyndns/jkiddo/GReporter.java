package org.dyndns.jkiddo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.common.io.Closeables;
import com.google.gdata.client.spreadsheet.FeedURLFactory;
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.spreadsheet.ListEntry;
import com.google.gdata.data.spreadsheet.WorksheetEntry;
import com.google.gdata.data.spreadsheet.WorksheetFeed;
import com.google.gdata.util.ServiceException;

public class GReporter
{
	public GReporter(String username) throws IOException, ServiceException
	{
		String applicationName = "Jolivia";
		String key = "0AgNbnXkaY4ktdFRtMExWRGxITUR4cmlKS09DZDZwNXc";
		SpreadsheetService service = new SpreadsheetService(applicationName);
		service.setUserCredentials("joliviajolivia@gmail.com", "1Ohmnibus");
		service.setProtocolVersion(SpreadsheetService.Versions.V3);

		URL url = FeedURLFactory.getDefault().getWorksheetFeedUrl(key, "private", "full");
		WorksheetEntry worksheetEntry = service.getFeed(url, WorksheetFeed.class).getEntries().get(0);

		ListEntry newRow = new ListEntry();
		newRow.getCustomElements().setValueLocal("ReportingIP", getIpv2());
		newRow.getCustomElements().setValueLocal("Time", format.format(new Date()));
		newRow.getCustomElements().setValueLocal("User", username);
		service.insert(worksheetEntry.getListFeedUrl(), newRow);
	}

	SimpleDateFormat format = new SimpleDateFormat();

	public static String getIpv2() throws IOException
	{
		URL whatismyip = new URL("http://checkip.amazonaws.com");
		BufferedReader in = new BufferedReader(new InputStreamReader(whatismyip.openStream()));
		String ip = in.readLine();
		Closeables.close(in, true);
		return ip;
	}
}
