package dossee.droids;

import java.util.UUID;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.content.ContextWrapper;

public class UniqueID_Generator {
	private static UniqueID_Generator instance;
	private String uniqueID;
	
	public UniqueID_Generator(ContextWrapper cw) {
		
		WifiManager wifiMan = (WifiManager)cw.getSystemService(
				Context.WIFI_SERVICE);
		WifiInfo wifiInf = wifiMan.getConnectionInfo();
		String macAddr = wifiInf.getMacAddress();
		
		if(macAddr == null || macAddr.trim().length() == 0) {
			uniqueID = UUID.randomUUID().toString();
		} else {
			uniqueID = macAddr;
		}
	}
	
	/**
     * getInstance of UniqueID_Generator Singleton
     * @return
     */
    public static UniqueID_Generator getInstance(ContextWrapper cw) {
    	if(instance == null)
    		instance = new UniqueID_Generator(cw);
    	return instance;
    }
    
    public String getUniqueID() {
    	return uniqueID;
    }
}
