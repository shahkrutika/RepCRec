package edu.nyu.adbms.repcrec;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class RepCRecTest {

  /**
   * @param args
   * @throws IOException 
   */
  public static void main(String[] args) throws IOException {
    FileReader fileReader = new FileReader("Test1");
    BufferedReader bufferedReader = new BufferedReader(fileReader);   
    Scanner scanInputFile = new Scanner(bufferedReader);
    while(scanInputFile.hasNext()) {
      //System.out.println(scanInputFile.next());
      String str = scanInputFile.next();
      Scanner scanstr = new Scanner(str);
    //  Instruction i = new Instruction();
      
      scanstr.useDelimiter("\\(");
      System.out.println(scanstr.next());
   //   System.out.println(scanstr.next());
}
  }

}
