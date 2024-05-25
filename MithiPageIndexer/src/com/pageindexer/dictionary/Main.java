package com.pageindexer.dictionary;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.pageindexer.dictionary.index.DictionaryIndexer;
import com.pageindexer.dictionary.utils.FileIOUtility;

public class Main {
	
    public static void main(String[] args) {

        String exclude_words_file = "D:\\mithi_assignment\\exclude-words.txt";
        String output_index_file = "D:\\mithi_assignment\\index.txt";
        String pages_folder_path = "D:\\mithi_assignment";

        
        //page_files has all  the pages inn it
        List<File> page_files = FileIOUtility.listPageFiles(pages_folder_path);
        if (page_files.isEmpty()) 
        {
            System.out.println("No page files found in the specified folder.");
            return;
        }

        // Create an array to hold the absolute paths
        String[] page_file_paths = new String[page_files.size()];

        for (int i = 0; i < page_files.size(); i++) 
        {
        	//contains array of paths
            page_file_paths[i] = page_files.get(i).getAbsolutePath(); // Get the absolute path of each file and store it in the array
        }
        
        DictionaryIndexer indexer = new DictionaryIndexer();
        
        try 
        {
            indexer.readExcludeWords(exclude_words_file);
            indexer.indexPages(page_file_paths);
            indexer.writeIndex(output_index_file);
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }
    }
}	