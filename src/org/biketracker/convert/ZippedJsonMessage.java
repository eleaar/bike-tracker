package org.biketracker.convert;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.zip.GZIPOutputStream;

import com.google.gson.Gson;

import android.location.Location;

@SuppressWarnings("unused")
public class ZippedJsonMessage {
	private final UUID uuid;
	private final String locations;

	public ZippedJsonMessage(List<Location> locations) throws IOException {
		List<Object[]> list = new ArrayList<Object[]>();
		for (Location location : locations) {
			list.add(convertLocation(location));
		}
		
		this.locations = bytesToHex(compress(new Gson().toJson(list)));
		this.uuid = UUID.randomUUID();
	}
	
	private Object[] convertLocation(Location location) {
		return new Object[] { 
				location.getTime(),
				location.getLatitude(), 
				location.getLongitude() };
	}
	
	public byte[] compress(String string) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PrintStream out = new PrintStream(new GZIPOutputStream(baos));
		out.print(string);
		out.flush();
		out.close();
		return baos.toByteArray();
	}
	
	public static String bytesToHex(byte[] bytes) {
	    final char[] hexArray = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
	    char[] hexChars = new char[bytes.length * 2];
	    int v;
	    for ( int j = 0; j < bytes.length; j++ ) {
	        v = bytes[j] & 0xFF;
	        hexChars[j * 2] = hexArray[v >>> 4];
	        hexChars[j * 2 + 1] = hexArray[v & 0x0F];
	    }
	    return new String(hexChars);
	}
}
