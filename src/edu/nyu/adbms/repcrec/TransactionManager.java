package edu.nyu.adbms.repcrec;

import java.security.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.plaf.SliderUI;


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
   * @throws InterruptedException 
   */
  public int executeTransaction(Instruction instruction)  {
      if(instruction.getOperationType().equals("begin")) {
      transaction = new Transaction(instruction.getTransactionId(), false);
//      siteManager.assignSites(transaction);
      Date d = new Date();
     // transaction.setCreationTime(d);
      long t = d.getTime();
  
      transaction.setCreationTime(System.nanoTime()); 
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
        Iterator<Instruction> iter  = allInstructions.iterator();
        while(iter.hasNext()) {
          Instruction i = iter.next();
          if(i.getTransactionId().equals(transaction.getId()) ){
            iter.remove();
          }
        }
        transactions.remove(transaction.getId());
        blockedQueue.remove(transaction);
        return 3;
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
        System.out.println("Transaction " +transaction.getId()+ " aborted because no lock " +
        		"on variable " +instruction.getVariable() + " could be obtained" );
      //  for(Instruction inst : allInstructions) {
/*          Instruction tempInst = allInstructions.get(l);
          if (tempInst.getTransactionId() == transaction.getId()) {
            allInstructions.remove(l);
          }
              
  */
        Iterator<Instruction> iter  = allInstructions.iterator();
        while(iter.hasNext()) {
          Instruction i = iter.next();
          if(i.getTransactionId().equals(transaction.getId()) ){
            iter.remove();
          }
        }
        transactions.remove(transaction.getId());
        blockedQueue.remove(transaction);
        return 3;
      }
      return result;
    }
    else if(instruction.getOperationType().equals("end")) {
      transaction = transactions.get(instruction.getTransactionId());
      transaction.setCurrentInstruction(instruction);
      siteManager.assignSites(transaction);
      int r = siteManager.executeInstruction(transaction);
        System.out.println("T" +transaction.getId()+ "committed successfully");
      return r;

    }
    
    else if(instruction.getOperationType().equals("dump")) {
      transaction.setCurrentInstruction(instruction);

      //all v at all sites
      if(instruction.getDumpId() != 0) {
       // siteManager.assignSites(transaction);
      }
      else if(instruction.getVariable() != null) {
        siteManager.dump(instruction.getVariable());
      }
      else {
        return (siteManager.dump());
      }

    }
    
    else if(instruction.getOperationType().equals("fail")) {
      transaction.setCurrentInstruction(instruction);
      //siteManager.executeInstruction(transaction);
      Set<Transaction> result = siteManager.fail(transaction);
      for(Transaction t : result) {
        transactions.remove(t.getId());
        blockedQueue.remove(t);
        System.out.println("Transaction " +t.getId()+ " aborted since " +
            "site " + instruction.getFailId() + " failed");
      
      Iterator<Instruction> iter  = allInstructions.iterator();
      while(iter.hasNext()) {
        Instruction i = iter.next();
        if(i.getTransactionId().equals(t.getId()) ){
          iter.remove();
        }
        }
      }
   }
    
    else if(instruction.getOperationType().equals("recover")) {
      siteManager.recover(instruction.getRecoverId());
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
