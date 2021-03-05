
/* Pavan Ghuge cs610 4875 prp */

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Arrays;

public class hits_4875 {
    static Integer vertices=null;
    static Integer edges=null;
    static int[][] adjMatrix=null;
    static double[] init_authority;
    static double[] init_hub;
    static double[] prev_authority;
    static double[] prev_hub;
    static double target_err=1;
    static double curr_err=2;
    static double sq_sum_hubs=0.0;
    static double sq_sum_authorities=0.0;
    static DecimalFormat decimalFormat = new DecimalFormat("0.0000000");



    public static void main(String[] args)throws IOException{
        FileReader inputFile=null;
        if (args.length !=3){
            System.out.println("Invalid number of arguments!");
            System.out.println("Need to pass 3 arguments: iterations,initial value and graph_filename");
            return;
        }
       
        System.out.println();
        System.out.println();
        System.out.println();

        // Assign arguments to local variables
        Integer iterations =Integer.parseInt(args[0]);
        Integer initial_value =Integer.parseInt(args[1]);
        String fname = args[2];

            // Check for intial values
        if(initial_value<-2 || initial_value>1){
            System.out.println("Invalid intial value !!! ");
            System.out.println("Initial Value can be only -2, -1, 0 or 1");
            return;
        }

        try
        {
            inputFile = new FileReader(fname);
        }
        catch (FileNotFoundException e)
        {
            System.out.println("File name:"+fname+" not found !!!");
        }
                
                
        BufferedReader filereader=new BufferedReader(inputFile);
        String vertex_edges= filereader.readLine(); // reading  lines in file ; first line gives vertices and edges
        String[] split_item = vertex_edges.split(" "); // Splitting the values in graph file
        vertices = Integer.parseInt(split_item [0]);// number of vertices
        edges = Integer.parseInt(split_item [1]); //number of edges
        adjMatrix = createAdjMatrix(filereader, vertices,edges); // intializing matrix to 1
        init_hub = init_hub_values(initial_value,vertices); // Setting hub  values as per intial value  
        init_authority =init_authority_values(initial_value,vertices);   // Setting hub  values as per intial value  
       
        if (iterations<=0){ 
            if(iterations==0){
                target_err = Math.pow(10.0,-5+0.0);// inializing target error to very small value
                iterations = -1; //setting iterations to -1
            }
            else{
                target_err=Math.pow(10.0,iterations+0.0);
            }
            curr_err = 1;
        }

        if(vertices>10){
            target_err =Math.pow(10.0,-5+0.0); // inializing target eoor to very small value
            init_hub = init_hub_values(-1,vertices);
            init_authority = init_authority_values(-1,vertices);
            iterations =-1;
        }
        display(0,false); // printing base case
        int curr_iter =1;

        while(iterations !=0 && curr_err>=target_err){
            // Copying the values in new variables for hubs
            prev_hub = init_hub.clone(); 
            // Copying the values in new variables for authority
            prev_authority = init_authority.clone();
            // Updating the authority values
            update_authority();
             // Updating the hub values
            update_hubs();
            //
            curr_err = scaling(iterations);
            display(curr_iter, (curr_err < target_err ||iterations==1) && vertices>10);
            curr_iter += 1;
            iterations--;
        }

    } 
// end of main function

// Function to create and assign values to adjecency matrix
    public static int[][] createAdjMatrix(BufferedReader file,int vertices,int edges )throws IOException{
        int [][] adjMatrix = new int[vertices][vertices];
        String item ; 

        while ((item =file.readLine())!=null){
            String [] splitted_item=item.split(" ");
            // initializing values in matrix to 1
            adjMatrix[Integer.parseInt(splitted_item[0])][Integer.parseInt(splitted_item[1])] = 1;
        }
        return adjMatrix;
    }
// Function ends

// Function to assign hub values
    public static double[] init_hub_values(int initial_value, int vertices ){
        // array of possible values
        double[] possible_values = {(1/Math.sqrt(vertices)),(1/(vertices+0.0)),0,1}; 
        // initializing array of hubs
        double[] hubs = new double[vertices]; 
        for(int i=0;i<vertices;i++){
            // initializing hub values as per initial value  
            hubs[i] = possible_values[2+initial_value];
        }
        return hubs;
    }
// Function ends

// Function to assign authority values
    public static double[] init_authority_values(int initial_value, int vertices ){
        // array of possible values
        double[] possible_values = {(1/Math.sqrt(vertices)),(1/(vertices+0.0)),0,1};
        // initializing array of authorities
        double[] authority = new double[vertices]; 
        for(int i=0;i<vertices;i++){
            // initializing authority values as per initial value  
            authority[i] = possible_values[2+initial_value];
        }
        return authority;
    }
// Function ends

// Function for displaying output
    public static void display(int iter, boolean last_iteration ){
        String output ="";
        if(iter==0){
            output += "Base: " + iter+ " :";
        }
        else{
            output+= "Iter: " + iter+ " :";
            if(vertices>10&& last_iteration){
                System.out.println("Iter: "+iter);
            }
        }

        for (int i=0;i<init_hub.length;i++){
            output += "A/H ["+i+ "] = "+decimalFormat.format(init_authority[i]) +"/" +decimalFormat.format(init_hub[i])+" ";
            if (vertices>10 && last_iteration){
                System.out.println("A/H ["+i+ "] = "+decimalFormat.format(init_authority[i]) +"/" +decimalFormat.format(init_hub[i])+" ");
            }
        }
        if(vertices<=10){
            System.out.println(output);
        }
        else if(last_iteration){
            System.out.println();
        }
    }
    // Function ends

    // Function to update hub values
    static void update_hubs(){
        sq_sum_hubs=0.0;
        for(int i=0;i<adjMatrix.length;i++){
            double sum = 0.0;
            for (int j=0;j<adjMatrix[0].length;j++){
                //Updating sum 
                sum += adjMatrix[i][j]*init_authority[j]; 
            }
            init_hub[i] = sum;
            //calculating squared sum
            sq_sum_hubs +=init_hub[i]*init_hub[i];
        }
    }   
    // Function ends

    //Function to update authority values
    static  void update_authority(){
        sq_sum_authorities=0.0;
        for(int i=0;i<adjMatrix.length;i++){
            double sum = 0.0;
            for (int j=0;j<adjMatrix[0].length;j++){
                sum += adjMatrix[j][i]*init_hub[j];//Updating sum
            }
            init_authority[i]= sum;
             //calculating squared sum
            sq_sum_authorities += init_authority[i]*init_authority[i];
        }
    }
    // Function ends

    // Function for scaling down the values of hubs and authorities
    public static double scaling(int iterations){
        double max_value=-1;
        for(int i=0; i<init_hub.length;i++){
            // dividing the hub value by sqr root of sum of squares of hub values
            init_hub[i] = init_hub[i]/Math.sqrt(sq_sum_hubs );
             // dividing the authority value by sqr root of sum of squares of authority values
            init_authority[i] = init_authority[i]/Math.sqrt(sq_sum_authorities);
            // calculating max value
            max_value = Math.max(Math.max(Math.abs(init_hub[i]-prev_hub[i]),Math.abs(init_authority[i]-prev_authority[i])),max_value);
        }
     
        if(iterations<=0){
            return max_value;
        } 
        return 2;
    }


}