package com.pageindexer.dictionary.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileIOUtility {
	
    public static String readPages(String file_path) throws IOException 
    {
        String page=null;
        FileReader file_reader_obj= new FileReader(file_path);
        
        try
        {
        	BufferedReader reader = new BufferedReader(file_reader_obj);
        			
            StringBuilder page_builder = new StringBuilder();
            String line;
            
            //The readLine() method returns the line that was read, or null if all lines have been read and the end of the stream has been reached
            while ((line = reader.readLine()) != null) 
            {
                if (line.trim().isEmpty()) 
                {
                    if (page_builder.length() > 0) 
                    {
                        page=page_builder.toString(); //we convert entire page_builder to string, since its easier to use inbuilt methods on strings
                        page_builder.setLength(0); // clear all content that this string builder holds
                    }
                } 
                else 
                {
                	page_builder.append(line).append("\n"); 
                }
            }
            
        }
        catch(IOException e) //need IOException because exception might occur while readingLine()
        {
        	e.printStackTrace();
        }
        //return the entire single page in a string format
        return page;
    }

    public static List<File> listPageFiles(String directory_path) 
    {
        List<File> page_files = new ArrayList<>(); //List type is File, because we are adding multiple files
        File directory = new File(directory_path);

        if (directory.exists() && directory.isDirectory())  //This condition checks if the specified directory exists and if it is indeed a directory.
        {
            File[] files = directory.listFiles(); //we obtain an array of File objects representing the files contained within the directory.
            
            for (File file : files) //loop over each File object in the files array.
            {
            	//This condition checks if the current File object represents a file (not a directory) 
            	//and if its name matches the pattern of a page file.
            	//isPageFile method is a helper method that determines if the file name matches the expected format for a page file.
                if (file.isFile() && isPageFile(file.getName())) 
                {
                	
                	//If the file meets the conditions (it is a file and its name matches the page file pattern), 
                	//it is added to the page_files list.
                	page_files.add(file);
                }
            }
        }

        return page_files;
    }

    private static boolean isPageFile(String filename) 
    {
    	//This part of the regular expression matches the literal string "page".
    	//\\d+: This part matches one or more digits (\d+). 
    	//The + quantifier means one or more occurrences of the preceding digit (\d) character.
    
        return filename.toLowerCase().matches("page\\d+\\.txt");
    }
}