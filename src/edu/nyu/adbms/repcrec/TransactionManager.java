package edu.nyu.adbms.repcrec;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class TransactionManager {

  private SiteManager siteManager;
  Transaction transaction;
  private Map<Integer,Transaction> transactions;
  private List<Transaction> blockedQueue;
  private ArrayList<Instruction> allInstructions;
  
  public TransactionManager(SiteManager siteManager
      ,ArrayList<Instruction> allInstructions) {
    this.siteManager = siteManager;
    blockedQueue = new ArrayList<Transaction>();
    transactions = new HashMap<Integer,Transaction>();
    this.allInstructions = allInstructions;
    
  }
  
  

  /**
   * executeTransaction selects a randomly chosen available site to execute the
   * instruction just read from the input file
   * 
   * @param instruction is the current instruction to be executed
   */
  public int executeTransaction(Instruction instruction) {
  
    if(instruction.getOperationType().equals("begin")) {
      transaction = new Transaction(instruction.getTransactionId(), false);
//      siteManager.assignSites(transaction);
      Date d = new Date();
     // transaction.setCreationTime(d);
      long t = d.getTime();
      transaction.setCreationTime(t); 
       transactions.put(instruction.getTransactionId(), transaction);
    }
    else if(instruction.getOperationType().equals("beginRO")) {
      transaction = new Transaction(instruction.getTransactionId(), true);
  //    siteManager.assignSites(transaction);
      Date d = new Date();
      long t = d.getTime();
      transaction.setCreationTime(t);
      transactions.put(instruction.getTransactionId(), transaction);
    }
    else if(instruction.getOperationType().equals("R")) {
      transaction = transactions.get(instruction.getTransactionId());
      transaction.setCurrentInstruction(instruction);
      siteManager.assignSites(transaction);
      int result = siteManager.executeInstruction(transaction); 
      if(result == 2) {
        blockedQueue.add(transaction);
      } 
      else if(result == -1) {
        //abort
        transactions.remove(transaction);
        blockedQueue.remove(transaction);
      }
      return result;
    }
    else if(instruction.getOperationType().equals("W")) {
      transaction = transactions.get(instruction.getTransactionId());
      transaction.setCurrentInstruction(instruction);
      siteManager.assignSites(transaction);
      int result = siteManager.executeInstruction(transaction);
      if(result == 2 && !blockedQueue.contains(transaction)) {
        blockedQueue.add(transaction);
      } 
      else if(result == -1) {
        //abort
        System.out.println("Transaction " +transaction.getId()+ " aborted because no lock" +
        		"on variable " +instruction.getVariable() + " could be obtained" );
        
        transactions.remove(transaction);
        blockedQueue.remove(transaction);
      }
      return result;
    }
    else if(instruction.getOperationType().equals("end")) {
      transaction = transactions.get(instruction.getTransactionId());
      transaction.setCurrentInstruction(instruction);
      siteManager.assignSites(transaction);
      return (siteManager.executeInstruction(transaction));

    }
    
    else if(instruction.getOperationType().equals("dump")) {
      transaction.setCurrentInstruction(instruction);

      if(instruction.getSiteId() != null) {
        siteManager.assignSites(transaction);
        siteManager.executeInstruction(transaction);
      }
      else if(instruction.getVariable() != null) {
        siteManager.dump(instruction.getVariable());
      }
      else {
        return (siteManager.dump());
      }

    }
    
    else if(instruction.getOperationType().equals("fail")) {
      //transaction.setCurrentInstruction(instruction);
      //siteManager.executeInstruction(transaction);
      Set<Transaction> result = siteManager.fail(instruction);
      for(Transaction t : result) {
        transactions.remove(t);
        blockedQueue.remove(t);
        System.out.println("Transaction " +t.getId()+ " aborted since " +
            "site " + instruction.getSiteId() + " failed");
      }
   }
    
    else if(instruction.getOperationType().equals("recover")) {
      siteManager.recover(instruction.getSiteId());
    }
    return 0;
  }
/*
  public void start() {
    int st = 0;
   while(allInstructions.size() != 0 || blockedQueue.size() != 0) {
      
    
    if(!blockedQueue.isEmpty()) {
     
      Transaction blockT = blockedQueue.get(0);
      int result = executeTransaction(blockT.getCurrentInstruction());
      if(result == 1) {
        blockedQueue.remove(0);
      }
      
   
      
    }
    else {
      try {
     int a = executeTransaction(allInstructions.get(st));
     System.out.println(a);
      }
      catch (NullPointerException e) {}
      allInstructions.remove(0);
    //  st++;
    }
    
    }
  
  }
*/
  public List<Transaction> getBlockedQueue() {
    return blockedQueue;
  }

}
