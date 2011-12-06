package edu.nyu.adbms.repcrec;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Pattern;

public class RepCRecTest {

  /**
   * @param args
   * @throws IOException 
   */
  public static void main(String[] args) throws IOException {
   
    SiteManager siteManager = new SiteManager();
    ArrayList<Instruction> allInstructions = new ArrayList<Instruction>();

    TransactionManager transactionManager = new TransactionManager(siteManager,
        allInstructions);
    String operationType = null;
    Integer transactionId = 0;
    String variable = null;
    Integer failId = 0;
    Integer recoverId =0;
    Integer value = 0;
    Integer dumpId = 0;
    Integer siteId = 0;
    Instruction instruction;
    FileReader fileReader = new FileReader("Test1");
    BufferedReader bufferedReader = new BufferedReader(fileReader);   
    Scanner scanInputFile = new Scanner(bufferedReader);
    Pattern p = Pattern.compile("[\\)\\(\\,;]+");
    Pattern findOperationType = Pattern.compile("[a-zA-Z]+");
    Pattern findTransaction = Pattern.compile("T[0-9]");
    Pattern findTransactionId = Pattern.compile("T");
    Pattern findVariable = Pattern.compile("x[0-9]+");
    Pattern findValue = Pattern.compile("[0-9]+"); 
    while(scanInputFile.hasNext()) {  
      operationType = null;
      transactionId = 0;
      failId = 0;
      recoverId = 0;
      dumpId = 0;
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
          if(operationType.equals("fail"))
            failId = Integer.parseInt(eachItem);
          else if(operationType.equals("recover"))
            recoverId = Integer.parseInt(eachItem);
          else if(operationType.equals("dump"))
            dumpId = Integer.parseInt(eachItem);
          //if(operationType.equals("dump") || operationType.equals("fail") || 
           //   operationType.equals("recover")) 
           // siteId = Integer.parseInt(eachItem);
          else
            value = Integer.parseInt(eachItem);
      }       
      }
      instruction = 
        new Instruction(operationType, transactionId, variable, value,siteId,failId,recoverId,dumpId);
      allInstructions.add(instruction);
   
     // transactionManager.executeTransaction(instruction);
     }
    
   
     //transactionManager.start();
    int st = 0;
    while(allInstructions.size() != 0 || transactionManager.getBlockedQueue().size() != 0) {
       
     
     if(!transactionManager.getBlockedQueue().isEmpty()) {
      
       Transaction blockT = transactionManager.getBlockedQueue().get(0);
       System.out.println(blockT.getId() + " in blocked Queue");
       int result = transactionManager.executeTransaction(blockT.getCurrentInstruction());
       if(result == 1) {
         transactionManager.getBlockedQueue().remove(0);
       }
       else if(result == 2) {
         int a=0;
         try {
           a = transactionManager.executeTransaction(allInstructions.get(st));
         //  System.out.println(a);
            }
            catch (NullPointerException e) {
              e.printStackTrace();
            }
            if (a != 3)
              allInstructions.remove(0);         
       }       
     }
     else {
       int a=0;
       try {
      a = transactionManager.executeTransaction(allInstructions.get(st));
 //    System.out.println("a = "+a);
       }
       catch (NullPointerException e) {
         e.printStackTrace();
       }
       if(a !=3)
         allInstructions.remove(0);
     //  st++;
      
     
    } 
 
    }
   }
}
      
      
      
 
  

