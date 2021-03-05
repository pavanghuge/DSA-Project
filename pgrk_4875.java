/* Pavan Ghuge cs610 4875 prp*/

import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.BufferedReader;
import java.util.Arrays;
import java.text.DecimalFormat;
import java.io.IOException;

public class pgrk_4875 {

    static double const_d=0.85;
    static double[] pgrk;
    static int [] out_degree={};
    static double target_err = 1;
    static double current_err= 2;
    static Integer vertices =null;
    static Integer edges =null;
    static double[] temp;
    static int[][] adjMatrix= null;
    static DecimalFormat to_decimal = new DecimalFormat("0.0000000");

    // Main Function

    public static void main(String[] args)throws IOException{
        FileReader graphfile =null;
         if (args.length!=3){
             System.out.println("Enter Valid Arguments");
             System.out.println("Enter ProgramFile, iterations,initial value"+
             " and graph_file");
             return;
         }
        System.out.println();
        System.out.println();
        System.out.println();

         // creating local variables for input
         Integer iter =Integer.parseInt(args[0]);// No of iterations
         Integer init_value = Integer.parseInt(args[1]);
         String filename = args[2];

         // check for initial value
         if (init_value>1 || init_value<-2){
            System.out.println("Invalid intial value !!! ");
            System.out.println("Initial Value can be only -2, -1, 0 or 1");
            return;
         }

         try{
             graphfile =new FileReader(filename);

         }catch(FileNotFoundException e)
         {
             System.out.println("File : " +filename+ "not found !!!"); 
         }

         BufferedReader filereader = new BufferedReader(graphfile);
         String vertex_edge = filereader.readLine(); // reading lines in file
         String[] split_item = vertex_edge.split(" "); // splitting the input in graph file 
         vertices = Integer.parseInt(split_item[0]); // number of vertices
       
         edges = Integer.parseInt(split_item[1]);// number of edges
         
         adjMatrix = createAdjMatrix(filereader,vertices,edges);// created Adjecency Matrix  from graph
        
        int[] out_degree = new int[vertices];
        for (int i =0;i<vertices;i++){
            out_degree[i]=0;
            for(int j=0;j<vertices;j++){
                out_degree[i] += adjMatrix[i][j];
            }
        }

         init_pgrk(vertices,init_value);// Initiailzing page_rank values

        if(iter<=0){
            if(iter==0){
                target_err = Math.pow(10.0,-5.0); // initializing target error to 0.00001 we want to minimize error to 5 decimal 
                iter=-1;
            }
            else{
                target_err = Math.pow(10.0,iter+0.0);// initializing target error to very low we want to minimize error to 5 decimal 
            }
                
            current_err = 1; // initializing current error 1 which is considerably bigger   
        }

        if(vertices>10){
            target_err = Math.pow(10.0,-5+0.0);
            iter= -1;
            init_pgrk(vertices,-1);
        }
        
        display(0,false); // display base case

        temp = new double[vertices]; // to make copy of page rank array
        int curr_iter =1; // to keep track of iteration number

        while(iter!=0 && current_err>target_err){
            Arrays.fill(temp,0.0); // to fill the array temp with 0 
            current_err = page_rank(vertices,iter,out_degree);
            pgrk = temp.clone(); // we store of pagrk in a temp array
            // we display untill current error is smaller than target error or iterations is 1; for vertices >10
            display(curr_iter,(current_err<target_err || iter==1)&& vertices>10); 
            curr_iter ++;
            iter--;
        }
        

    } // end of main


    // Function to create adjecency matrix
    
    static int [][] createAdjMatrix(BufferedReader file, int vertices,int edges)throws IOException{
        int[][] adjMatrix = new int[vertices][vertices];
        String item;

        while((item=file.readLine())!=null){
            String [] split_items = item.split(" ");
            // initializing values for matrix to 1
            adjMatrix[Integer.parseInt(split_items[0])][Integer.parseInt(split_items[1])]=1;
        }
        return adjMatrix;
    } // End of function

    // Function to initialize pages
   static void init_pgrk(int vertices,int init_value){

        double[] values ={(1/Math.sqrt(vertices)),(1/(vertices+0.0)),0,1};
        pgrk = new double[vertices];
        Arrays.fill(pgrk,values[2+init_value]); 
    } // End of Function

    // Function to display the end result
    static void display(int iter, boolean last_iter){
        String output="";

        if (iter==0){    //Base Case
            output += "Base : " + iter + " :";
                
        }
        else{
            output += "Iter : " + iter+ " :";
            if(vertices>10 && last_iter){ //if vertices greater than 10 we simply print iterations 
                System.out.println("Iter ; " + iter+ " :");
            }
        }

        for(int i = 0; i < pgrk.length; i++){
            output += "P [" +i+"] = "+to_decimal.format(pgrk[i])+" ";
            if(vertices>10 && last_iter){
                System.out.println("P [" +i+"] = "+to_decimal.format(pgrk[i])+" ");
            }
        }
        if(vertices<=10){
            System.out.println(output);
        }
        else if(last_iter){
            System.out.println();
        }
    } //End of function

    // Calculate page rank  
    static double page_rank(int vertices, int iterations, int[] out_degree){

        for (int i =0;i<vertices;i++){
            for(int j=0;j<vertices;j++){
                if(adjMatrix[j][i]!=0){
                    //storing pg rank values in for temporary purpose
                    temp[i] += pgrk[j]/out_degree[j]; 
                }

            }
        }

        double max_err =-1;
        for(int i=0;i<vertices;i++){
            // formula for calculating page rank 
            temp[i] = const_d* temp[i] +((1-const_d)/vertices); 
            if(Math.abs(temp[i]-pgrk[i])>max_err){
                //  we calculate error for between current pg rank value and previous pgrank value
                max_err=Math.abs(temp[i]-pgrk[i]); 
            }
        }

        if(iterations<=0){
            return max_err;
        }
        return 2;

    }// End of page rank function

}// end of class