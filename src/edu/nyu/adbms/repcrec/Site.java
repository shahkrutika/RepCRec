package edu.nyu.adbms.repcrec;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Site {
  
  static Map<Integer, Site> availableSites = new HashMap<Integer, Site>();
  static Map<Integer, Site> failedSites = new HashMap<Integer, Site>();
  static Map<Integer, Site> recoveredSites = new HashMap<Integer, Site>();
  Integer id;
  /**
   * Gets a randomly chosen available site 
   * @param instruction is the current instruction to be executed
   */
  public void executeInstruction(Instruction instruction) {
    Random generator = new Random();
    int length = availableSites.size();
    Integer randomNumber = 1 + generator.nextInt(length);   
    Site site;
    site = availableSites.get(randomNumber);
    
  }
}
