package dossee.droids;

import java.util.UUID;

public class UniqueID_Generator {
	private static UniqueID_Generator instance;
	private String uniqueID;
	
	public UniqueID_Generator() {
		uniqueID = UUID.randomUUID().toString();
	}
	
	/**
     * getInstance of UniqueID_Generator Singleton
     * @return
     */
    public static UniqueID_Generator getInstance() {
    	if(instance == null)
    		instance = new UniqueID_Generator();
    	return instance;
    }
    
    public String getUniqueID() {
    	return uniqueID;
    }
}
