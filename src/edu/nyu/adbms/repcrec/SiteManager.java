package edu.nyu.adbms.repcrec;

import java.util.AbstractQueue;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Random;

public class SiteManager {  
  
  private Map<Site, Boolean> sites;
	private Map<Integer,Site> sitesInt;
	private Site site;
	private DataLockManager evenSiteDataLockManager;
	private DataLockManager oddSiteDataLockManager;
	
	
	//many tranaction belong to one site but one T belogn to one S
	private Map<Transaction,Integer> siteTrasactionRel;
	
	
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
		this.siteTrasactionRel = new HashMap<Transaction , Integer>();
		this.sitesInt = new HashMap<Integer, Site>();
		oddSiteDataLockManager = new DataLockManager();
		evenSiteDataLockManager = new DataLockManager();
		Variable v;
		String name;
		for(Integer i = 1; i <= 20; i++) {
      if(i % 2 == 0) {
        name = "x" + i;
        v = new Variable(name, 10 * i);
        //oddSiteDataLockManager.variables.put(name, v);  
        oddSiteDataLockManager.stableStorage.put(name, v);
      }
    }
		
	  for(Integer i = 1; i <= 20; i++) {
      name = "x" + i;
      v = new Variable(name, 10 * i);
      //evenSiteDataLockManager.variables.put(name, v);
      evenSiteDataLockManager.volatileMemory.put(name, v);
    }
		
		init();
	}
	
	/*
	 * In a real-life scenario, we would read a config file which describes the
	 * resource allocation scheme for each site.
	 */
	private void init() {
	  
	  for(Integer i = 1; i <= 10; i++) {
      if(i % 2 == 0) {
        site = new Site(i,evenSiteDataLockManager);
        sites.put(site, true);
        sitesInt.put(i, site);
      }
      else {
        site = new Site(i,oddSiteDataLockManager);
        sites.put(site, true);
        sitesInt.put(i, site);
      }
      
    }
	}
	
	
	public void assignSites(Transaction T) {
	  Site selectedSite;
	  for(Integer i = 0; i<sitesInt.size(); i++) {
	    selectedSite = sitesInt.get(i);
	    if(selectedSite.isAvailable) {
	      siteTrasactionRel.put(T, i);
	    }
	  }
	}
	
	public int executeInstruction(Transaction transaction) {
	 
	  Instruction curInst = transaction.getCurrentInstruction();
	  Integer siteId = siteTrasactionRel.get(transaction);
	  Site executingSite = sitesInt.get(siteId);
	  if(curInst.getOperationType().equals("R")) {
	    return (executingSite.R(transaction, transaction.isReadOnly()));	    
	  }
	  else if(curInst.getOperationType().equals("W")) {
	    int result = executingSite.W(transaction);
	    if (result == 1) {
	      for(Integer i =0 ;i<sitesInt.size();i++) {
	        Site site = sitesInt.get(i);
	        Variable v = site.dataLockManager
	          .stableStorage.get(transaction.getCurrentInstruction().getVariable());
	        v.value = transaction.getCurrentInstruction().getValue();
	      }
	    }
	    return (result);	    
	  }
	//  return true;*/
	  return 100;
	} 
}
