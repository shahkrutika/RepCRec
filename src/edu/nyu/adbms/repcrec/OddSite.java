package edu.nyu.adbms.repcrec;

import java.util.HashMap;
import java.util.Map;

public class OddSite extends Site {
  
  public OddSite(int id) {
		super(id);
		// TODO Auto-generated constructor stub
	}

public Map<String,Integer> variables = new HashMap<String,Integer>();
  
  
  /**
   * Constructor initializes the variables arraylist with all even variables 
   * each with an initial value of 10*i where i is the variable number
   * @param Id is the Id of the site
   */
  
  /*
  public OddSite(Integer Id) {
    String key;
    this.id = Id;
    for(Integer i = 1; i <= 20; i++) {
      if(i % 2 == 0) {
        key = "x" + i;
        variables.put(key, 10*i);
      }
    }
    availableSites.put(Id, this);
  
  }
  
  */

}
