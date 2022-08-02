package com.exam.competitor.admin.security;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListOfFiles {
   public static void main(String args[]) throws IOException {
      //Creating a File object for directory
      
      List<File> lstFile = listf("D:\\Data\\Vipul\\Data\\Workspace\\Workspace_AdminLTE\\ZoopkanDaily\\exam-competitor-webparent\\product-images");
      System.out.println("count"+lstFile.size());
   }
   
   public static List<File> listf(String directoryName) {
       File directory = new File(directoryName);

       List<File> resultList = new ArrayList<File>();

       // get all the files from a directory
       File[] fList = directory.listFiles();
       resultList.addAll(Arrays.asList(fList));
       for (File file : fList) {
           if (file.isFile()) {
               System.out.println(file.getAbsolutePath());
               System.out.println(file.getName());
               String filename = file.getName().replace(" ", "");
               File newFile = new File(file.getPath().replace(file.getName(),filename));
               boolean flag = file.renameTo(newFile);
               
               if (flag == true) {
                   System.out.println("File Successfully Rename");
               }
           } else if (file.isDirectory()) {
               resultList.addAll(listf(file.getAbsolutePath()));
           }
       }
       //System.out.println(fList);
       return resultList;
   } 
}