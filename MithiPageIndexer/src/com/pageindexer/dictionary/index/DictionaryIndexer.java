package com.pageindexer.dictionary.index;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import com.pageindexer.dictionary.utils.FileIOUtility;

public class DictionaryIndexer {

    private Set<String> exclude_words = new HashSet<>();
    private Map<String, Set<Integer>> index = new TreeMap<>();

    public void readExcludeWords(String filename) throws IOException 
    {
    	
    	FileReader file_reader_obj= new FileReader(filename);
    	//It istherefore advisable to wrap a BufferedReader around any Reader whose read()operations may be costly, 
    	//such as FileReaders and InputStreamReaders
    	//BufferedReader reads data in chunks (buffers), which reduces the number of system calls and improves performance
        try (BufferedReader reader = new BufferedReader(file_reader_obj)) 
        {
            String line;
            while ((line = reader.readLine()) != null) 
            {
                String word = line.trim().toLowerCase(); // Convert to lower case and trim whitespace from leading and trailing spaces
                if (!word.isEmpty()) 
                {
                	exclude_words.add(word); // Add the word to the exclude_words set
                }
            }
        }
    }

    public void indexPages(String[] page_file_paths) throws IOException //page_file_paths has 3 paths of those files, p1,p2,p3
    {
    	//System.out.println("__________________Inside indexPages of String[] page_file_paths________________");
        for (int i = 0; i < page_file_paths.length; i++) 
        {
        	//3 pages have been passed to readPages() through iteration one by one
            String page = FileIOUtility.readPages(page_file_paths[i]);
            
            //System.out.println("String page:"+page);
            //passing each of those pages along with i+1 to note it's page number
            indexPages(page, i + 1);
        }
    }

    private void indexPages(String words_in_page, int page_number) 
    {
		//System.out.println("__________________Inside indexPages of String words_in_page, int page_number________________");

   
		//System.out.println("*****words_page:*****"+words_in_page);
    	
		String[] words = words_in_page.split("\\s+");
    	//System.out.println("String[] words:: "+words);
        
    	for (String word : words) //Reading per word from that single page
        {
        	//System.out.println("String word"+word);
        	
            word = word.replaceAll("[^a-zA-Z]", "").toLowerCase();
            
            if (!word.isEmpty() && !exclude_words.contains(word)) 
            {
            	
            	//Check if the word is already in the index map
            	Set<Integer> page_numbers = index.get(word);

            	// If the word is not already in the index map, create a new set for it
            	if (page_numbers == null) 
            	{
            		page_numbers = new HashSet<>();
            	    index.put(word, page_numbers);
            	}

            	// Add the current page_number to the set associated with the word
            	page_numbers.add(page_number);
            }
        }
        
    }

    public void writeIndex(String output_filename) throws IOException 
    {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(output_filename))) 
        {
            for (Map.Entry<String, Set<Integer>> entry : index.entrySet()) 
            {
            	
            	List<Integer> sorted_page_numbers = new ArrayList<>(entry.getValue()); // Convert the set to a list
                Collections.sort(sorted_page_numbers); // Sort the list
                
                StringBuilder page_numbers= new StringBuilder();

                
                for(int i=0;i<sorted_page_numbers.size();i++)
                {
                	if(i==0 && i==sorted_page_numbers.size()-1)
                	{
                		page_numbers.append(sorted_page_numbers.get(i));
                	}
                	else if(i == 0 && i!=sorted_page_numbers.size()-1)
                	{
                		page_numbers.append(sorted_page_numbers.get(i));
                		page_numbers.append(",");
                	}
                	else if(i >0 && i!=sorted_page_numbers.size()-1)
                	{
                		page_numbers.append(sorted_page_numbers.get(i));
                		page_numbers.append(",");
                	}
                	else
                	{
                		page_numbers.append(sorted_page_numbers.get(i));
                	}
                }
                
                String str_page_number = page_numbers.toString();
                writer.write(entry.getKey() + " : " + str_page_number);
                page_numbers.setLength(0);
                
                writer.newLine(); // Write each word and its corresponding pages to the file
            }
        }
    }
}