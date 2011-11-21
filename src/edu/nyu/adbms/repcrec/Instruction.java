package edu.nyu.adbms.repcrec;

public class Instruction {
  
  private String operationType;
  private int transactionId;
  private String variable;
  private Integer value;
  private Integer siteId;
  private static int i=0;
  public Instruction(String operationType, int transactionId, String variable,
      Integer value, Integer siteId) {
    this.operationType = operationType;
    this.transactionId = transactionId;
    this.variable = variable;
    this.value = value;
    i=i+1;
   /* System.out.println(" i - " + i +
        " operation - " +operationType +
                       " transactin id - " +transactionId +
                         " variable - " + variable +
                         " value - " + value +
                         " site id - " + siteId );*/
  }
  
}
