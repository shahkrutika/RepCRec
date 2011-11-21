package edu.nyu.adbms.repcrec;

import java.util.HashMap;
import java.util.Map;

public class SiteManager {
  private Map<Integer, Site> availableSites = new HashMap<Integer, Site>();
  private Map<Integer, Site> failedSites = new HashMap<Integer, Site>();
  private Map<Integer, Site> recoveredSites = new HashMap<Integer, Site>();
  
}
