package edu.nyu.adbms.repcrec;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Pattern;

public class RepCRecTest {

  /**
   * @param args
   * @throws IOException 
   */
  public static void main(String[] args) throws IOException {
   
    SiteManager siteManager = new SiteManager();
    TransactionManager transactionManager = new TransactionManager(siteManager);

    String operationType = null;
    Integer transactionId = 0;
    String variable = null;
    Integer value = 0;
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
      
      
      
      
 
  

