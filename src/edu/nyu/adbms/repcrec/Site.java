package edu.nyu.adbms.repcrec;

import java.util.List;
import java.util.Random;

public class Site {
  
	int id;
	List<String> variables;
	
	public Site(int id) {
		this.id = id;
	}
	
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
