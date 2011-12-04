package edu.nyu.adbms.repcrec;

public class Instruction {
  private String operationType;
  private Integer transactionId;
  private String variable;
  private Integer value;
  private Integer siteId;
  private static int i=0;
  
  
  public Instruction(String operationType, Integer transactionId, String variable,
      Integer value, Integer siteId) {
    this.operationType = operationType;
    this.transactionId = transactionId;
    this.variable = variable;
    this.value = value;
    //this.siteId = siteId;
    i=i+1;
    /*
   System.out.println(" i - " + i +
        " operation - " +operationType +
                       " transactin id - " +transactionId +
                         " variable - " + variable +
                         " value - " + value +
                         " site id - " + siteId );*/
  }
  

  public String getOperationType() {
    return operationType;
  }
  public Integer getTransactionId() {
    return transactionId;
  }
  public String getVariable() {
    return variable;
  }
  public Integer getValue() {
    return value;
  }
  public Integer getSiteId() {
    return siteId;
  }
  
  public void setSiteId(Integer siteId) {
    this.siteId = siteId;
  }
  public static int getI() {
    return i;
  }
  
}
