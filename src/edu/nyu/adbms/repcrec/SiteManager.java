package edu.nyu.adbms.repcrec;

import java.util.HashMap;
import java.util.Map;

public class SiteManager {  
  
	private Map<Site, Boolean> sites;
	
	/*
	 * The SiteManager (SM) is responsible for managing the sites. It assigns variables to each individual site.
	 * It also keeps track of the current status of each site, whether it is down or up.
	 * 
	 * The SM handles the updating of values of the variables after each transaction commits.
	 * When a transaction commits, the final value of a variable written by that transaction is
	 * broadcasted to all sites which maintain that variable.
	 * 
	 * The flow of actions is as follows:
	 * 	- The SM gets a request from the TransactionManager (TM) for a R(xj) or W(Ti, xj, v).
	 * 	- If it is a R request, the SM checks if there is any site available which can provide the
	 * 		current value of xj. If yes, then SM returns that value, else an error is returned.
	 * 	- If its a W request, SM checks if Ti is allowed to write on xj.
	 * 		-- If Ti currently holds the lock on xj, then allow the write.
	 * 		-- Else fail / block Tj. 
	 * 
	 */
	public SiteManager() {
		this.sites = new HashMap<Site, Boolean>();
		init();
	}
	
	/*
	 * In a real-life scenario, we would read a config file which describes the
	 * resource allocation scheme for each site.
	 */
	private void init() {
		
	}
  
}
