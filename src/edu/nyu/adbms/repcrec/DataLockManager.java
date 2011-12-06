package edu.nyu.adbms.repcrec;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class DataLockManager {
  //string - name of variable
   Map<String, Variable> volatileMemory;
   Map<String, Variable> stableStorage;
  
  public DataLockManager() {
    volatileMemory = new TreeMap<String, Variable>();
    stableStorage = new TreeMap<String, Variable>();
    //volatileMemory = new HashMap<Integer, Variable>();
    //stableStorage = new HashMap<Integer, Variable>();
 
  }
  
  public int checkGrantSharedLock(Transaction T, Variable siteVar, Variable sVar) {
    
    //can read!
    if(!siteVar.hasExclusiveLock && !sVar.isObsolete) {
      //System.out.println("T" +T.getId()+ " got an exclusinve lock on ");
     // siteVar.sharedOwners.add(T);
      return 1; //read
    }
    //for a just recovered site
    else if(!siteVar.hasExclusiveLock && sVar.isObsolete) {
      //can read only if the var is not replicated 
      if(!siteVar.isReplicated) {
       // siteVar.sharedOwners.add(T);
        return 1;
      }
      return 4;
      
    }   
    //block
    else {
      //int results = siteVar.owner.getCreationTime().compareTo( T.getCreationTime());
      long results = siteVar.owner.getCreationTime() - T.getCreationTime();
      //blocked
      if (results > 0) {
        return 2;
      }
      
      //abort
      else if(results < 0) {
        for (Variable var : this.volatileMemory.values()) {
          if(var.owner != null){

          //    for(int i = 0; i<allVariables.size();i++) { 
               // Variable v = allVariables.get(i);
                if(var.owner.equals(T) || var.sharedOwners.contains(T)) {
                  var.owner = null;
                  var.sharedOwners.remove(T);
                }
              }
//        if(siteVar.owner.equals(T)) {
 //         siteVar.owner.equals(null);
 //       }
        return -1;
      }
      }
    }
    //default
    return 10;
  }

  public int checkGrantWriteLock(Transaction T, Variable siteVar) {
    int flag =0;
    if(!siteVar.hasExclusiveLock && siteVar.sharedOwners.isEmpty()) {
     /* siteVar.owner = T;
      System.out.println("T" +T.getId()+ " has a Ex lock on " + siteVar.name);
      siteVar.value = T.getCurrentInstruction().getValue();
      siteVar.hasExclusiveLock = true;
      //siteVar.isObsolete = false;*/
      return 1; //success
    }
    else {
      if(siteVar.sharedOwners.size()==1){
        if(siteVar.sharedOwners.get(0).getId().equals(T.getId()))
        {
        /*  siteVar.owner = T;
          System.out.println("T" +T.getId()+ " has a Ex lock on " + siteVar.name);
          siteVar.value = T.getCurrentInstruction().getValue();
          siteVar.hasExclusiveLock = true;
          //siteVar.isObsolete = false;*/
          return 1; //success
        }
      }
      else if (siteVar.sharedOwners.size()>1){
        for (Transaction SO : siteVar.sharedOwners){
          if(T.getCreationTime() > SO.getCreationTime()){
            flag =1;
          break;
          }
        }
        if(flag==1)
          return -1;
        else
          return 2;
      }
      else{
      long results = siteVar.owner.getCreationTime() - T.getCreationTime();
  //    System.out.println("result: "+results);
      //blocked
      if (results > 0) {
        return 2;
      }
      
      //abort
      /**
       * aborting the transaction, all the locks held by this T should be
       * released from all data lock managers, remove transaction from 'transactions',
       * shared owners and exclusive owners.
       */
      else if(results < 0) {
        return -1;

      //  List<Variable> allVariables = new ArrayList<Variable>(volatileMemory.values());
        //allVariables = (ArrayList<Variable>)volatileMemory.values();
      /*  for (Variable var : this.volatileMemory.values()) {
          if(var.owner != null){
          //    for(int i = 0; i<allVariables.size();i++) { 
               // Variable v = allVariables.get(i);
                if(var.owner.equals(T) || var.sharedOwners.contains(T)) {
                  var.owner = null;
                  var.sharedOwners.remove(T);
                }
              }
        }*/
        /*for(int i = 0; i<allVariables.size();i++) { 
          Variable v = allVariables.get(i);
          if(v.owner.equals(T) || v.sharedOwners.contains(T)) {
            v.owner = null;
            v.sharedOwners.remove(i);
          }
        }*/
       // if(siteVar.owner.equals(T)) {
         // siteVar.owner.equals(null);
        //}
      }
      }
    }
    //default
    return 10;
    }
  
  public int end(Transaction T){
    Variable vStableStorage;
    for (Variable vVolatileMemory : this.volatileMemory.values()) {
   // for(Integer i = 0; i<volatileMemory.size();i++) {
     // Variable vVolatileMemory = volatileMemory.get(i);
      if(vVolatileMemory.owner != null && vVolatileMemory.owner.equals(T)) {
        vStableStorage = stableStorage.get(vVolatileMemory.name);
        vStableStorage.value = vVolatileMemory.value;
        vVolatileMemory.owner = null;
        vVolatileMemory.hasExclusiveLock = false;
        vStableStorage.isObsolete = false;
      }
      if(vVolatileMemory.sharedOwners.contains(T)) {
      vVolatileMemory.sharedOwners.remove(T);
      }
      }

    return  55;
    }
  }
