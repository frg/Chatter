package server_clientDemo2;

import java.io.*;
import java.util.*;

public class Employee implements Serializable {

   private int employeeNumber;
   private String employeeName;

   Employee(int num, String name) {
      employeeNumber = num;
      employeeName= name;
   }

   public int getEmployeeNumber() {
      return employeeNumber ;
   }

   public void setEmployeeNumber(int num) {
      employeeNumber = num;
   }

   public String getEmployeeName() {
      return employeeName ;
   }

   public void setEmployeeName(String name) {
      employeeName = name;
   }
}
