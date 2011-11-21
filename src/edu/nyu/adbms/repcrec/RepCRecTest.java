package edu.nyu.adbms.repcrec;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.plaf.SliderUI;

public class RepCRecTest {

  /**
   * @param args
   * @throws IOException 
   */
  public static void main(String[] args) throws IOException {
   
    TransactionManager transactionManager = new TransactionManager();
    Site site = new Site();
    String operationType = null;
    int transactionId = 0;
    String trans = null;
    String variable = null;
    Integer value = 0;
    Integer siteId = 0;
    Instruction instruction;
    
    for(Integer i = 1; i <= 10; i++) {
      if(i % 2 == 0)
        site = new EvenSite(i);
      else
        site = new OddSite(i);       
    }
 /*   EvenSite e = null;
    OddSite o = null;
    for(Integer i = 1; i <= 5; i++) {
      site = site.availableSites.get(i);
      if (site instanceof EvenSite) {
        e = (EvenSite) site;
        System.out.println(e.variables.toString());
      }
      else{
         o = (OddSite)site;
      System.out.println(o.variables.toString());
       }
    }*/
    FileReader fileReader = new FileReader("Test1");
    String[] splitTransactionId;
    BufferedReader bufferedReader = new BufferedReader(fileReader);   
    Scanner scanInputFile = new Scanner(bufferedReader);
    Pattern p = Pattern.compile("[\\)\\(\\,;]+");
    Pattern findOperationType = Pattern.compile("[a-zA-Z]+");
    Pattern findTransaction = Pattern.compile("T[0-9]");
    Pattern findTransactionId = Pattern.compile("T");
    Pattern findVariable = Pattern.compile("x[0-9]+");
    Pattern findValue = Pattern.compile("[0-9]+"); 
    Matcher m ;
    while(scanInputFile.hasNext()) {  
      operationType = null;
      transactionId = 0;
      variable = null;
      value = 0;
      siteId = 0;
      String str = scanInputFile.next();
      String[] splitEachInstruction = p.split(str);
      for(String eachItem : splitEachInstruction) {
        if(findOperationType.matcher(eachItem).matches()) 
          operationType = eachItem;
        
        else if(findTransaction.matcher(eachItem).matches()) {
          
          String[] q = findTransactionId.split(eachItem);
          transactionId = Integer.parseInt(q[1]);
        } 
        else if(findVariable.matcher(eachItem).matches())
          variable = eachItem;
        else if(findValue.matcher(eachItem).matches()) {
          if(operationType.equals("dump") || operationType.equals("fail") || 
              operationType.equals("recover")) 
            siteId = Integer.parseInt(eachItem);
          else
            value = Integer.parseInt(eachItem)  ;
      }
     
       
      }
      instruction = 
        new Instruction(operationType, transactionId, variable, value,siteId);
      transactionManager.executeTransaction(instruction);
     }
     
    } 
 
    }
      
      
      
      
 
  

