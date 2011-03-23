package dossee.droids;

import java.util.UUID;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.content.ContextWrapper;

/**
 * @author Erik Telepovsky
 *
 * This class generates the unique ID.
 * It can be the MAC address of device
 * or random UUID.
 */
public class UniqueID_Generator {
	private static UniqueID_Generator instance;
	private String uniqueID;
	
	public UniqueID_Generator(ContextWrapper cw) {
		
		//get configuration of device wireless settings
		WifiManager wifiMan = (WifiManager)cw.getSystemService(
				Context.WIFI_SERVICE);
		WifiInfo wifiInf = wifiMan.getConnectionInfo();
		String macAddr = wifiInf.getMacAddress();
		
		//if the wireless connection is not established
		//or the MAC address of device is not in correct format
		//generate a random UUID
		if(macAddr == null || macAddr.trim().length() == 0) {
			uniqueID = UUID.randomUUID().toString();
		
		//otherwise return the MAC address
		} else {
			uniqueID = macAddr;
		}
	}
	
	/**
     * get instance of UniqueID_Generator Singleton
     * @return
     */
    public static UniqueID_Generator getInstance(ContextWrapper cw) {
    	if(instance == null)
    		instance = new UniqueID_Generator(cw);
    	return instance;
    }
    
    /**
     * get the unique ID of the UniqueID_Generator instance
     * @return unique ID
     */
    public String getUniqueID() {
    	return uniqueID;
    }
}
