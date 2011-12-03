package edu.nyu.adbms.repcrec;

import java.util.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.Random;

import javax.rmi.CORBA.Util;

public class TransactionManager {

  private SiteManager siteManager;
  Transaction transaction;
  private Map<Integer,Transaction> transactions;
  private List<Transaction> blockedQueue;
  
  public TransactionManager(SiteManager siteManager) {
    this.siteManager = siteManager;
    blockedQueue = new ArrayList<Transaction>();
    transactions = new HashMap<Integer,Transaction>();
    
  }
  
  /**
   * executeTransaction selects a randomly chosen available site to execute the
   * instruction just read from the input file
   * 
   * @param instruction is the current instruction to be executed
   */
  public void executeTransaction(Instruction instruction) {
    
    if(instruction.getOperationType().equals(Operations.begin)) {
      transaction = new Transaction(instruction.getTransactionId(), false);
      siteManager.assignSites(transaction);
      Date d = new Date();
      transaction.setCreationTime(d);
      transactions.put(instruction.getTransactionId(), transaction);
    }
    else if(instruction.getOperationType().equals(Operations.beginRO)) {
      transaction = new Transaction(instruction.getTransactionId(), true);
      siteManager.assignSites(transaction);
      Date d = new Date();
      transaction.setCreationTime(d);
      transactions.put(instruction.getTransactionId(), transaction);
    }
    else if(instruction.getOperationType().equals(Operations.R)) {
      transaction = transactions.get(instruction.getTransactionId());
      siteManager.assignSites(transaction);
      transaction.setCurrentInstruction(instruction);
      int result = siteManager.executeInstruction(transaction); 
      if(result == 2) {
        blockedQueue.add(transaction);
      } 
      else if(result == -1) {
        //abort
        transactions.remove(transaction);
        blockedQueue.remove(transaction);
      }
    }
    else if(instruction.getOperationType().equals(Operations.W)) {
      transaction = transactions.get(instruction.getTransactionId());
      siteManager.assignSites(transaction);
      transaction.setCurrentInstruction(instruction);
      int result = siteManager.executeInstruction(transaction);
      if(result == 2) {
        blockedQueue.add(transaction);
      } 
      else if(result == -1) {
        //abort
        transactions.remove(transaction);
        blockedQueue.remove(transaction);
      }
    }
    else if(instruction.getOperationType().equals(Operations.end)) {
      transaction = transactions.get(instruction.getTransactionId());
      siteManager.assignSites(transaction);
      transaction.setCurrentInstruction(instruction);
      int result = siteManager.executeInstruction(transaction);
    }
  }

}
