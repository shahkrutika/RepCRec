package edu.nyu.adbms.repcrec;


public class Site {
  
	int id;
	boolean isAvailable;
//	List<Variable> variables = new ArrayList<Variable>();
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
	 if(isReadOnly && !stableVar.isObsolete) {
	   System.out.println("Transaction " + T.getId() + " read " +stableVar.name+
	       " : " +stableVar.value);
	   return 1; //readonly success
	   //add all sites down code
	 }
	 else {
	   //if(!stableVar.hasExclusiveLock)
	     int result = this.dataLockManager.checkGrantSharedLock(T, stableVar);
	     if(result == 1) {
	       System.out.println("Transaction " + T.getId() + " read " +stableVar.name+
	            " : " +stableVar.value);  
	       return 1; //read success
	     }
	    return result;
	 }
	}	 

	public int W(Transaction T){
	  String requestedVar = T.getCurrentInstruction().getVariable();
	  Variable stableVar = this.dataLockManager.stableStorage.get(requestedVar);

	  int result = this.dataLockManager.checkGrantWriteLock(T, stableVar);
	  if(result == 1) {
	    System.out.println("Transaction " + T.getId() + " wrote " +stableVar.name+
          " : " +stableVar.value);
     return 1; //read success
	  }
	  else 
	    return result;
	    
	  }
	  
	  public boolean end(Transaction T) {
	    return (this.dataLockManager.end(T));
	    
	  }
	  
	  public boolean dump(Transaction T) {
	    for (Variable var : this.dataLockManager.stableStorage.values())
      {
        System.out.println("Site: " +this.id+ " variable "+var.name+" = "+var.value);
      }
	    return true;
	  }
	

}
