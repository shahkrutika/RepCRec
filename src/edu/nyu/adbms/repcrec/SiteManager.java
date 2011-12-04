package edu.nyu.adbms.repcrec;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class SiteManager {  
  
 // private Map<Site, Boolean> sites;
	private Map<Integer,Site> sitesInt;
	private Site site;
	private DataLockManager evenSiteDataLockManager;
	private DataLockManager oddSiteDataLockManager;
	
	
	//many tranaction belong to one site but one T belogn to one S
	private Map<Transaction,Integer> siteTrasactionRel;
	
	
	public Map<Transaction, Integer> getSiteTrasactionRel() {
    return siteTrasactionRel;
  }

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
//		this.sites = new HashMap<Site, Boolean>();
		this.siteTrasactionRel = new HashMap<Transaction , Integer>();
		this.sitesInt = new HashMap<Integer, Site>();
		
		for(Integer i = 1; i <=10 ; i++) {
		  init(i);
		}
		
	}
	
	/*
	 * In a real-life scenario, we would read a config file which describes the
	 * resource allocation scheme for each site.
	 */
	private void init(Integer siteId) {
	  
	  oddSiteDataLockManager = new DataLockManager();
    evenSiteDataLockManager = new DataLockManager();
    Variable v,volV;
    //String name;
    
    if(siteId %2 != 0) {      
    for(Integer i = 1; i <= 20; i++) {
      if(i % 2 == 0) {
      String  name = "x" + i;
        v = new Variable(name, 10 * i);
        volV = new Variable (name);
        v.isObsolete = false;
       // oddSiteDataLockManager.variables.put(name, v);  
       oddSiteDataLockManager.stableStorage.put(name, v);
       oddSiteDataLockManager.volatileMemory.put(name, volV);


      }
    }
    site = new Site(siteId,oddSiteDataLockManager);

    }
    else {    
    for(Integer i = 1; i <= 20; i++) {
     String name = "x" + i;
      v = new Variable(name, 10 * i);
      volV = new Variable (name);
      v.isObsolete = false;
      //evenSiteDataLockManager.variables.put(name, v);
      evenSiteDataLockManager.stableStorage.put(name, v);
      evenSiteDataLockManager.volatileMemory.put(name, volV);

    }
    site = new Site(siteId,evenSiteDataLockManager);

    }
	 
        //sites.put(site, true);
        sitesInt.put(siteId, site);    
	}
	
	
	public void assignSites(Transaction T) {
	  Site selectedSite;
	  
	  if(T.getCurrentInstruction().getOperationType().equals("dump")) {
	    siteTrasactionRel.put(T, T.getCurrentInstruction().getSiteId());
	  }
	  
	  else if (T.getCurrentInstruction().getOperationType().equalsIgnoreCase("end")){
	    for(Integer i = 1; i<=sitesInt.size(); i++) {
	      selectedSite = sitesInt.get(i);
	      if(selectedSite.isAvailable) {
	        T.getCurrentInstruction().setSiteId(selectedSite.id);
	        siteTrasactionRel.put(T, i);
	        break;
	      }
	    }
	  }
	  else {
	  for(Integer i = 1; i<=sitesInt.size(); i++) {
	    selectedSite = sitesInt.get(i);
	    if(selectedSite.isAvailable && selectedSite.dataLockManager.stableStorage.containsKey(T.getCurrentInstruction().getVariable())) {
	      T.getCurrentInstruction().setSiteId(selectedSite.id);
	      siteTrasactionRel.put(T, i);
	      break;
	    }
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
	  
	  //write abort left
	  else if(curInst.getOperationType().equals("W")) {
	    int result = executingSite.W(transaction);
	    if (result == 1) {
	      String var = transaction.getCurrentInstruction().getVariable();
	      for(Integer i =1 ;i<=sitesInt.size();i++) {
	        Site site = sitesInt.get(i);
	        if(site.dataLockManager.volatileMemory.containsKey(var) && site.isAvailable) {
	        Variable v = site.dataLockManager.volatileMemory.get(var);
	        v.value = transaction.getCurrentInstruction().getValue();
	        v.owner = transaction;
	        v.hasExclusiveLock = true;
	      }
	      }
	    }
	    //release locks on all variables on all sites whic have owner/shared lock as T 
	    else if(result == -1) {
	       for(Integer i =1 ;i<=sitesInt.size();i++) {
	          Site site = sitesInt.get(i);
	          if(site.isAvailable) {
	            for (Variable var : site.dataLockManager.volatileMemory.values()) {
	              if(var.owner != null) {
	               if (var.owner.equals(transaction)) {
	                 var.owner=null;
	                 var.hasExclusiveLock = false;
	                 var.sharedOwners.remove(transaction);
	               }
	          }
	            }
	        
	      }
	    }
	  }
	     return (result);      

	  }
	  
	  else if(curInst.getOperationType().equals("end")) {
	    for(Integer i = 1; i<=sitesInt.size(); i++) {
	      Site site = sitesInt.get(i);
	      if(site.isAvailable) {
	     // site.dataLockManager.end(transaction);
	      site.end(transaction);
	      }
	    }
	    return 55;
	  }
	  
	  else if(curInst.getOperationType().equals("dump")) {
	    if(executingSite.isAvailable) {
	       executingSite.dump(transaction);
	    }
	    else
	      return 1; //failure
	  }
	  
	  //else if(curInst.getOperationType().equals("fail")) {
	    
	   // fail();
	//  return true;*/
	  return 100;
	}
	
	public Set<Transaction> fail(Instruction curInst) {
    Integer siteID = curInst.getSiteId();
    Site failSite = sitesInt.get(siteID);
    failSite.isAvailable = false;
    for (Variable var : failSite.dataLockManager.volatileMemory.values()) {
      var.value = -100;
      var.hasExclusiveLock = false;
      var.owner = null;
      var.sharedOwners.remove(var);
      var.isObsolete = true;
      
    }
    //Map<Transaction, Integer> siteTransRel = .getSiteTrasactionRel();
    Set<Transaction> keys = new HashSet<Transaction>();
    for(Entry<Transaction, Integer> entry : siteTrasactionRel.entrySet()) {
      
      if(siteID ==  entry.getValue()) {
        keys.add(entry.getKey());  //transactoin tat have to be aborted
        
      }
    }
    
    for(Transaction T : keys) {
      for(Integer s = 1;s<=sitesInt.size();s++) {
        Site site = sitesInt.get(s);
        if(site.isAvailable) {
        DataLockManager dlm = site.dataLockManager;
        for (Variable var : dlm.volatileMemory.values()) {
          if(var.owner.equals(T)) {
            var.owner = null;
          }
         // while(var.sharedOwners.contains(T)) {
          var.sharedOwners.remove(T);
         // }
        }
        }
      }}
    return keys;
     // blockedQueue.remove(T);
     // transactions.remove(T);
    //  System.out.println("Transaction " +T.getId()+ " aborted since " +
    //      "site " + instruction.getSiteId() + " failed");
    }
    
   

	public void recover(Integer id) {
	  sitesInt.get(id).isAvailable = true;
    for (Variable var : sitesInt.get(id).dataLockManager.stableStorage.values()) {
      var.isObsolete = true;
    }

	}
	
	
	public int dump() {
    for (int i=1;i<=sitesInt.size();i++){
      Site site = sitesInt.get(i);
      if (site.isAvailable){
   //   for (Variable var : site.dataLockManager.stableStorage.values())
        for(int k = 1;k<=20;k++)
      {
          String name = "x"+k;
          if(site.dataLockManager.stableStorage.containsKey(name)) {
          Variable v = site.dataLockManager.stableStorage.get(name);
        System.out.println("Site: " +i+ " variable "+v.name+" = "+v.value);
      }
      }
      }
      else{
        System.out.println("Site: "+i+" is down");
      }
    }
    return 1;
  }
	
	public void dump(String xj){

    //how to find whether xj is odd or even? 
    for (int i=1;i<=sitesInt.size();i++){
      Site site = sitesInt.get(i);
      if (site.dataLockManager.stableStorage.containsKey(xj)){
        if (site.isAvailable){
          System.out.println("Site: " +i+ " value= " +site.dataLockManager.stableStorage.get(xj));
        }
        else{ 
          System.out.println("Site: "+i+ " is down");
        }
      }
    }
  }

}
