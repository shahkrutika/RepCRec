package edu.nyu.adbms.repcrec;

import java.util.Random;

public class TransactionManager {
  
  /**
   * executeTransaction selects a randomly chosen available site to execute the
   * instruction just read from the input file
   * 
   * @param instruction is the current instruction to be executed
   */
  public void executeTransaction(Instruction instruction) {
    
    Site site = new Site();
    site.executeInstruction(instruction);
    
    
  }

}
