/* Pavan Ghuge  cs610 4875 prp*/ 

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.File;
import java.util.Scanner;


class Lexicon{

    String[] keys;
    char[] values;
    
    String str = "";

    int currentSize, maxSize,num_entries,len=0;

    //Constructor of the class  
    public Lexicon(String wrd2){ 
        
        this.currentSize =0;
        keys = new String[Integer.parseInt(wrd2)];
        values =new char[Integer.parseInt(wrd2)*15];
        this.maxSize = Integer.parseInt(wrd2);
    }

    // Function to clear hash table 
    public void makeEmpty(){ 
        currentSize =0;
        keys = new String[maxSize];
        values = new char[maxSize*15];
    }

    // Function to get size of hash table
    public int getSize(){
        return currentSize;
    }

    //Function to check if hash table is full
    public boolean isFull()
    {
        return currentSize == maxSize;
    } 

    // Function to check if hash table is empty
    public boolean isEmpty(){
        return getSize()==0;
    }
    // setter function
    public void setString(String str1){
        str = str1;
    }
    
    // getter function
    public String getString(){
        return str;
    }

    // Function to get hash code of given key
    public int hash(String wrd2){
        int keylength = wrd2.length(); 
			int sum =0;
		    for(int i = 0; i < keylength ; i++){      
		        char character = wrd2.charAt(i); 
		        int ascii = (int) character; 
		         sum = sum + ascii; 
		    }
		    return sum % maxSize;
    }

    // Function to insert 
    public int insert(String word3){
        int hashvalue = hash(word3); 

        int i =hashvalue, h=1;

        do{
            if(keys[i]==null) {
                keys[i] =Integer.toString(str.length());
                currentSize++;

                String str = getString() + word3 + "\\";

                setString(str);

                values = str.toCharArray();

                return len;

            }

            i =(i+h*h) % maxSize;
        }while(i!=hashvalue);
        return len;
    } // End of insert Function

    // Function to display table
    public void print(){
        System.out.println("\n Current Size of the table is: " + getSize() + "\n");
        System.out.println();
        System.out.print("      T                   ");

        String arrayA = new String(values);
        int len_A = arrayA.length()-1;
        System.out.print("A: " +arrayA.substring(0,len_A));
        System.out.println();
        
        for (int i =0; i<keys.length; i++){
            if (keys[i]==null){
                keys[i] =" ";
            }
            System.out.println(i + ":" + keys[i]);
        }

    }// End of print Function

    // Function to search for given word
    public String search(String wrd){
        String temp = "";
        num_entries = 0;
        String result = new String(values);
        String[] words = null;
        words = result.split("\\\\");
        
        for (int i=0;i<words.length;i++){
            if(wrd.equals(words[i])){
                temp = words[i];
                loc(temp);
            }
        }
        if(temp == ""){
            System.out.print("");
        }
        return temp;
    }// Search function ends

    // Function to find location
    public int loc(String temp){
        int hash =hash(temp);
        if(keys[hash] != null) {

			for(int i = 0;i<values.length;i++)
			{
			    if(temp.charAt(0) == values[i]){

				    for(int j= 0;j<keys.length;j++) {
					
					    if(keys[j] !=  null && keys[j].equals(Integer.toString(i))) 
						    num_entries = j;
		
					}
	
				}
					
			}		

	    }

        
        return -1;
    }// End of loc function

    // Function to return number of entries
    public int num_Entries() {
		return num_entries;
	} // End of function 

    // Function to delete key and its value
    public void delete(String wrd){
        num_entries =0;

        String result =new String(values);
        String[] words =null;
        words = result.split("\\\\");
        for(int j=0;j<words.length;j++){
            if(wrd.equals(words[j])){

                loc(words[j]);
                for (int i=0;i<keys.length;i++)
                {
                    if(keys[num_entries]!=null && i==hash(wrd))
                    keys[num_entries] =null;
                }
                currentSize--;
                

                StringBuilder sb = new StringBuilder();
		        	for (int idx = 0; idx != words[j].length(); ++idx)
		        	    sb.append("*");
		        words[j] = words[j].replaceAll(wrd, sb.toString()); 

            } 
            String s = "";
	            for (String n:words)
	                s+= n+ "\\";
	            values = s.toCharArray();
        } 

    }// end of delete function

    // Function to print comment
    public void comment(String wrd){
        System.out.println("Comment: " + wrd);
    }// End of print commentFunction 

}// End of Lexicon class



public class hashing_4875 {
    public static void main(String[] args )throws IOException{
        FileReader graphfile =null;
        Lexicon lexicon =null; //object of Class Lexicon
        // char ch;
        // Scanner scan = new Scanner(System.in);
        if(args.length!=1){
            System.out.println("Invalid Number of Arguments!");
        return;
        }
        
        String filename =args[0];
        
        try{
            graphfile = new FileReader(filename); 
        } 
        catch(FileNotFoundException e){
            System.out.println("File : " +filename+ "not found !!!");
        }

        BufferedReader filereader =new BufferedReader(graphfile);
        String item;
        
        while((item=filereader.readLine())!=null){
            String [] split_item =item.split(" "); // Splitting the graph file

            int command =Integer.parseInt(split_item[0]);
            
              
            if(command!=13){
                String word = split_item[1].toString();
               switch(command){
                    case 10:
                        lexicon.insert(word);// Insert the word
                        break;
                    
                    case 11:
                        if (lexicon.search(word).equals(word)){
                            lexicon.delete(word);// Delete the word only when found
                            int count =lexicon.num_Entries();
                            System.out.println(word +"    deleted from slot: "+count);
                        }
                        else{
                            System.out.println(word + " not found in table.\n");
                        }
                            
                        break;
                    
                    case 12: // search call
                        if(lexicon.search(word).equals(word)){
                            int count1= lexicon.num_Entries();
                            System.out.println(word + " found at slot: " +count1);
                       
                        }
                        else{
                            System.out.println(word +  "   not found in table.\n");
                        }
                        break;
                    
                    case 14: // Creation of a lexicon
                        lexicon = new Lexicon(word); 
                        break;
                    
                    case 15: // display the comment
                    lexicon.comment(word);
                    break;


                }// end of switch
            }
         else if(command==13){   
            lexicon.print();// print table 
            continue;
       }
           
        }// end of while loop
       

    }// End of main


}// End of Class