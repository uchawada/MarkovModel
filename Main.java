package hw4;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
	
	int minLen = 0;
	int maxLen = 0;
	int numNames = 0;
	int p = 0;
	int order;
	static String boys = "src\\hw4\\namesBoys.txt";
	static String girls = "src\\hw4\\namesGirls.txt";
	
	
	enum Person {BOY, GIRL};
	Person person;
	
	public static void main(String[] args) throws IOException{
		Main main = new Main();
		Model model = new Model();

		String nameGirls[];
		String nameBoys[];
		main.inputs();
		
		nameBoys = readfile(boys);
		nameGirls = readfile(girls);
		
		
		ArrayList<String> markovNames = new ArrayList<String>();
		
		//if the person type input is boy, then it generates names for boys, else for girls. 
		if(main.person == Person.BOY){
			model.markovModel(nameBoys, main.order);
			markovNames = model.getNames(main.minLen, main.maxLen, main.numNames, main.order, nameBoys);
		}
		else if(main.person == Person.GIRL){
			model.markovModel(nameGirls, main.order);
			markovNames = model.getNames(main.minLen, main.maxLen, main.numNames, main.order, nameGirls);
		}
		
		//Prints out the names stored in the markovNames String[] 
		for(int i = 0; i < markovNames.size(); i++){
			System.out.println(markovNames.get(i));
		}	
	}
	
	public void inputs() throws IOException{
		
		/*
		 * Asks for input from the user 
		 * Boy or girl?
		 * min and max len of the name 
		 * number of names to generate and the order
		 */
		BufferedReader reader =  new BufferedReader(new InputStreamReader(System.in)); 
		System.out.println("Boy or Girl? (Enter a number): ");
		System.out.println("Boy = 1");
		System.out.println("Girl = 2");
		p = Integer.parseInt(reader.readLine());
		if(p == 1){
			person = Person.BOY;
		}
		if(p == 2){
			person = Person.GIRL;
		}
		while(p < 1 || p > 2){
			askInput("person");
		}
		
		System.out.print("Minimum length of the name: ");
		minLen = Integer.parseInt(reader.readLine());
		
		//it keeps on asking the input until the numbers are valid 
		
		while(minLen < 2 || minLen > 12){
			askInput("minLen");
		}
		
		System.out.print("Maximum length of the name: ");
		maxLen = Integer.parseInt(reader.readLine());
		while(maxLen < 2 || maxLen > 12){
			askInput("maxLen");
		}
		
		while(maxLen <= minLen){
			askInput("maxLTmin");
		}
		
		System.out.print("Number of names to generate: ");
		numNames = Integer.parseInt(reader.readLine());	
		
		System.out.print("Order for the Markov Model: ");
		order = Integer.parseInt(reader.readLine());
		
		while(order < 2 || order > 12){
			askInput("order");
		}
	}
	
	/*
	 * This function takes a string as an input, and asks the user questions depending on that input
	 * if the input is minlen then it asks the user the minlen again
	 * it does the same of invalid inputs of maxlen, person type and number of names 
	 */

	public int askInput(String type) throws NumberFormatException, IOException{
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		
		if(type == "minLen"){
			System.out.print("Please enter a number between 2 and 12: ");
			minLen = Integer.parseInt(reader.readLine());
			return minLen;
		}
		else if(type == "maxLen"){
			System.out.print("Please enter a number between 2 and 12: ");
			maxLen = Integer.parseInt(reader.readLine());
			return maxLen;
		}
		
		else if(type == "person"){
			System.out.print("Please enter 1 or 2: ");
			p = Integer.parseInt(reader.readLine());
			
			return p;
		}
		else if(type == "maxLTmin"){
			System.out.println("Max cannot be less than min. Please enter a greater number than " + minLen);
			System.out.print("Maximum length of the name: ");
			maxLen = Integer.parseInt(reader.readLine());
		}
		else if(type == "order"){
			System.out.print("Please enter a number between 2 and 12: ");
			order = Integer.parseInt(reader.readLine());
			return order;
		}
		return 0;				
	}
	
	/*
	 * Reads the file and saves the input in the String[] and returns it
	 */
	public static String[] readfile(String filename) throws FileNotFoundException{
		
		String[] data = new String[1000];
		
		Scanner scanner = new Scanner(new FileReader(filename));
		
		for(int i = 0; i < data.length; i++){
			if(scanner.hasNextLine()){
				data[i] = scanner.nextLine();
			}
		}
		scanner.close();
		return data;
	}
}
