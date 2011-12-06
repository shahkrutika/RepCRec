package edu.nyu.adbms.repcrec;


public class Site {
  
	int id;
	boolean isAvailable;
	DataLockManager dataLockManager;
	
	public Site(int id, DataLockManager dmManager) {
		this.id = id;
		this.isAvailable = true;
		dataLockManager = dmManager;
		
	}
	
	//read
	
	public int R(Transaction T, boolean isReadOnly){
	 String requestedVar = T.getCurrentInstruction().getVariable();
	 Variable stableVar = this.dataLockManager.stableStorage.get(requestedVar);
	 Variable voltVar = this.dataLockManager.volatileMemory.get(requestedVar);
	 if(isReadOnly) {
	   if(!stableVar.isObsolete) {
	     
	   
	   System.out.println("Transaction " + T.getId() + " read " +stableVar.name+
	       " : " +stableVar.value);
	   return 1; //readonly success
	   }
	   else 
	     return 4; //add all sites down code
	 }
	 else {
	   //if(!stableVar.hasExclusiveLock)
	     int result = this.dataLockManager.checkGrantSharedLock(T, voltVar,stableVar);
	     if(result == 1) {
	       System.out.println("Transaction " + T.getId() + " read " +stableVar.name+
	            " : " +stableVar.value+ " from site " + this.id);  
	       return 1; //read success
	     }
	     
	    return result;
	 }
	}	 

	public int W(Transaction T){
	  String requestedVar = T.getCurrentInstruction().getVariable();
	  Variable volatileVar = this.dataLockManager.volatileMemory.get(requestedVar);

	  int result = this.dataLockManager.checkGrantWriteLock(T, volatileVar);
	  //success
	  if(result == 1) {
	    
	    volatileVar.owner = T;
      System.out.println("T" +T.getId()+ " has a Ex lock on " + volatileVar.name);
      volatileVar.value = T.getCurrentInstruction().getValue();
      volatileVar.hasExclusiveLock = true;
      //siteVar.isObsolete = false;
	    System.out.println("Transaction " + T.getId() + " wrote " +volatileVar.name+
          " : " +volatileVar.value+ " on site " +this.id);
     return 1; //read success
	  }
	  
	  //abort
	  else if(result == -1) {
	    for (Variable var : this.dataLockManager.volatileMemory.values()) {
        if(var.owner != null){
        //    for(int i = 0; i<allVariables.size();i++) { 
             // Variable v = allVariables.get(i);
              if(var.owner.equals(T) || var.sharedOwners.contains(T)) {
                var.owner = null;
                var.sharedOwners.remove(T);
              }
            }
      }
	    
	  }
	  //blocked
	 // else if(result == 2)  {
	    
	 // }
	    return result;
	    
	  }
	  
	  public int end(Transaction T) {
	    return (this.dataLockManager.end(T));
	    
	  }
	  
	  public boolean dump(Transaction T) {
	    for(int k = 1;k<=20;k++)
      {
          String name = "x"+k;
          if(this.dataLockManager.stableStorage.containsKey(name)) {
	  //  for (Variable var : this.dataLockManager.stableStorage.values())
    //  {

            Variable var = this.dataLockManager.stableStorage.get(name);
        System.out.println("Site: " +this.id+ " variable "+var.name+" = "+var.value);
      }
      }
	    return true;
	  }
	

}
