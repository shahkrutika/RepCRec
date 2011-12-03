package edu.nyu.adbms.repcrec;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataLockManager {
  //string - name of variable
   Map<String, Variable> volatileMemory;
   Map<String, Variable> stableStorage;
  
  public DataLockManager() {
    volatileMemory = new HashMap<String, Variable>();
    stableStorage = new HashMap<String, Variable>();
  }
  
  public int checkGrantSharedLock(Transaction T, Variable siteVar) {
    if(!siteVar.hasExclusiveLock && !siteVar.isObsolete) {
      siteVar.sharedOwners.add(T);
      return 1; //read
    }
    else {
      int results = siteVar.owner.getCreationTime().compareTo( T.getCreationTime());
      //blocked
      if (results > 0) {
        return 2;
      }
      
      //abort
      else if(results < 0) {
        List<Variable> allVariables = new ArrayList<Variable>();
        allVariables = (ArrayList<Variable>)volatileMemory.values();
        for(int i = 0; i<allVariables.size();i++) { 
          Variable v = allVariables.get(i);
          if(v.owner.equals(T) || v.sharedOwners.contains(T)) {
            v.owner = null;
            v.sharedOwners.remove(i);
          }
        }
        if(siteVar.owner.equals(T)) {
          siteVar.owner.equals(null);
        }
        return -1;
      }
    }
    //default
    return 10;
  }

  public int checkGrantWriteLock(Transaction T, Variable siteVar) {
    if(!siteVar.hasExclusiveLock) {
      siteVar.owner = T;
      siteVar.value = T.getCurrentInstruction().getValue();
      return 1; //sucess
    }
    else {
      int results = siteVar.owner.getCreationTime().compareTo( T.getCreationTime());
      //blocked
      if (results > 0) {
        return 2;
      }
      
      //abort
      else if(results < 0) {
        List<Variable> allVariables = new ArrayList<Variable>();
        allVariables = (ArrayList<Variable>)volatileMemory.values();
        for(int i = 0; i<allVariables.size();i++) { 
          Variable v = allVariables.get(i);
          if(v.owner.equals(T) || v.sharedOwners.contains(T)) {
            v.owner = null;
            v.sharedOwners.remove(i);
          }
        }
        if(siteVar.owner.equals(T)) {
          siteVar.owner.equals(null);
        }
        return -1;
      }
    }
    //default
    return 10;
    }
    
 
}
