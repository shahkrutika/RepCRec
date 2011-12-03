package edu.nyu.adbms.repcrec;

import java.util.Date;

import javax.print.attribute.standard.DateTimeAtCreation;

public class Transaction {
  
  //private String name;
  private Integer id;
  private boolean isReadOnly;
  private Instruction currentInstruction;
  private Date creationTime;

  public Date getCreationTime() {
    return creationTime;
  }


  public void setCreationTime(Date creationTime) {
    this.creationTime = creationTime;
  }


  public Transaction(Integer transactionId, boolean isReadOnly) {
    id = transactionId;
    this.isReadOnly = isReadOnly;
  }


  public Integer getId() {
    return id;
  }


  public boolean isReadOnly() {
    return isReadOnly;
  }


  public Instruction getCurrentInstruction() {
    return currentInstruction;
  }

  public void setCurrentInstruction(Instruction currentInstruction) {
    this.currentInstruction = currentInstruction;
  }
  
  
}
