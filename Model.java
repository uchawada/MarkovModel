package hw4;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Model {

	HashMap<String, int[]> current = new HashMap<String, int[]>();
	HashMap<String, double[]> prob = new HashMap<String, double[]>();
	
	ArrayList<String> genNames = new ArrayList<String>(); 
	
	
	public void markovModel(String[] names, int order){
		
		//for value reference of the letters
		String words = "abcdefghijklmnopqrstuvwxyz_";
		
		/*
		 * Start with a for loop that goes over 2 letters at a time and incrementing by 1 so a letter is repeated and 
		 * add 1 to the value of the new letter
		 * for example if we have "ae" first , then add 1 to the 5th index of the array and pass that array to the hash map 
		 * for second one - 
		 */
		
		//appends underscore to the names to get the start and the end of the name 
		for(int i = 0; i < names.length; i++){
			for(int j = 0; j < order; j++){
				names[i] = "_" + names[i] + "_"; 
				names[i] = names[i].toLowerCase();
			}
			
			//splits the name and stores the letters to the char array for future use
			char[] name = names[i].toCharArray();
			
			//for iterating through the characters in the name
			//saves two letters of the name 
			for(int c = order; c < name.length; c++){
				
				char[] ky = new char[order];
				
				/*
				 * looks back order times, for example saves the number of letters in the char array 
				 * according to the order number. 
				 * So we have the individual keys 
				 */
				for(int lb = order; lb > 0; lb--){
					ky[order - lb] = name[c - lb];
				}
				
				
				//ky char array is changed to string to get the key of type String

				String key = new String(ky).toLowerCase();
				
				/*
				 * To get the value, I used the alphabetical order of the letters, so I know which letter has been repeated after 
				 * what letter how many times. So if the key is "ea", then the value of that key will be the index of a (that is 1) in the 
				 * alphabetical order. So, to increment the value table, I will increment the number of index 1 in the value table. 
				 * t[1] is the index of the second letter of the key. 
				 */
				
				int value = words.indexOf(name[c]);

				/*
				 * array is the second pair type of the hash table, and the length is the number of alphabets
				 */
			
				int[] valArr;
				
				/*
				 * checks if the key is already in the hash table, and if it is then it increments the value 
				 * of the second letter of the key in the valueArray, and replaces the hash table value 
				 * with the new value array. 
				 */
				if(current.containsKey(key)){
					valArr = current.get(key);
					valArr[value]++;
					current.replace(key, valArr);
				}
				
				/*
				 * if it is not in the hash table then it resets the hash table for that key, 0's all the values of the hash table 
				 * and increments the index value of the second letter of the key in the value Array and then
				 * puts the key and the valueArray in the hash table
				 */
				else{
					valArr = new int[words.length()];
					Arrays.fill(valArr, 0);
					valArr[value]++;
					current.put(key, valArr);
				}
			}
		}
		
		/*
		 * Now, we've got the patterns of the letters and which letter occurs how many times after what letter
		 * Here, for every entry in the map 
		 * We get the probabilities for each of the keys, and we save the probability in the probability 
		 * hash table
		 */
		
		for(String key: current.keySet()){
						
			/*
			 * Gets value from the existing key in the current hash table 
			 * and stores it in the value 
			 */
			int[] value = current.get(key);
			
			double[] probValue = new double[value.length];
			
			int sum = 0;
			
			/*
			 * sums all the values occurring after the key
			 */
			for(int i = 0; i < value.length; i++){
				sum += value[i];
			}
			
			/*
			 * divides every value by sum to get the probability of that letter or sequence of letters 
			 * occurring after the given key
			 */
			for(int i = 0; i < value.length; i++){
				probValue[i] = (double)value[i]/sum;
			}
			//puts the key and the probability value in the probability hash table 
			prob.put(key, probValue);	
			
		}
	}
	
	//gets names according to the probabilities of the given key.
	public ArrayList<String> getNames(int minLen, int maxLen, int numNames, int order, String[] givenNames){
		
		
		//sets novel to true
		boolean novel = true;
		
		String word = "abcdefghijklmnopqrstuvwxyz_";
		char[] words = word.toCharArray();

		/*
		 * stores the generated names in the String[] names and the length is the number specified by the user
		 */
		int count = 0; 
		
		while(count < numNames){
			
			String name = "";

			
			//adds underscores according to the order
			for(int i = 0; i < order; i++){
				name = name + "_";
			}
			
			/*
			 * Returns a random letter and adds it to the name 
			 * It choose the one with the higher probability
			 */
			do{ 				
				String key = name.substring(name.length() - order);

				if(prob.containsKey(key)){
					
					int index = -1;

					double[] value = prob.get(key);

					Random rand = new Random();
					double random = rand.nextDouble();
					for(int i = 0; i < value.length; i++){
						
						//subtracts value from random 
						random -= value[i];
						//the higher the value, the lower the random will be, so choose the one with higher probability
						if(random <= 0.0d){
							index = i;
							break;
						}
					}
					name += words[index];
				}
			}
			/*
			 * Run the while loop until it reaches "_" which is identified by underscore
			 */
			while(name.length() > order && !name.substring(name.length() - 1).equals("_"));
			
			/*
			 * The current name generated is with underscores since that notifies us of the end
			 * therefore it removes the underscores from the name generated 
			 */
			
//			System.out.println("NAME = " + name);
			
			String temp = name.substring(order, name.length() - 1);
			
//			System.out.println("TEMP = " + temp);


			/*
			 * Checks the length of the generated name which should be >= min and <= max. 
			 */
			if(temp.length() >= minLen && temp.length() <= maxLen){
							
				
				for(int i = 0; i < givenNames.length; i++){
					
					/*
					 * if the generated name is already in the list of givenNames then it is not novel 
					 * so novel = false
					 */
					
//					System.out.println("GIVENNAMES = " + givenNames[i]);
					
					if(givenNames[i].toLowerCase().equals(temp.toLowerCase())){
						novel = false;
					}
				}
					/*
					 * If the generated name is novel then then adds that name to the names list 
					 * and increments the count
					 */
				if(novel == true){						
					genNames.add(count, temp);
					count++;
				}
			}
		}	
		return genNames;
	}
	
	
}
