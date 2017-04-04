
package copart_zipDistance;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

public class CustomerService {

	
	static String userZip;
	static HashMap<String,String> map = new HashMap<String, String>();
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		URL obj;
		try {
			//We have to manually make an entry in the text file. This is the Zipcode from which the nearest 
			//copart facililty distance will be calculated.
			File file = new File("zip.txt");
			FileReader fileReader = new FileReader(file);
			@SuppressWarnings("resource")
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			userZip = bufferedReader.readLine();
			
			try {
				@SuppressWarnings("resource")
				// Using this text file as a database to lookup the pincodes of the copart facilties. 
				// It contains only the Zipcode and location information for the testing purposes
				BufferedReader br = new BufferedReader(new FileReader("Zipcode.txt"));
				String line =  null;
				
				 while((line=br.readLine())!=null){
				        String str[] = line.split("\t");
				  //Creating a hashmap for all the values in the database to perform easier lookup. 
				  //Should have Arranged the keys in ascending order here for better efficiency
			            map.put(str[0], str[1]);
			            
				 }
				       // System.out.println(map);
				 	        
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			if(userZip != null)
			{
				//System.out.println(userZip);
				System.out.println(map.keySet());
				//Checking if the given Zipcode is already present in the Database, if present the location is returned corresponding to that key.
				if (map.containsKey(userZip))
				{
				System.out.println("The value is :" + map.get(userZip));
				}
				//if the given Zipcode is not present we need to look for the nearest facility.
				//To achieve this we look for concurrent Zipcodes in the Database and calculate the distance of each concurrent zipcode.
				else
				{
					int result = Integer.parseInt(userZip);
					result++ ;
					for (int i = 0 ; i < map.size() ; i++)
					{
					String X = Integer.toString(result);
					System.out.println(X);
					if (map.containsKey(X))
					{
						System.out.println("The Value is :" + map.get(X));
					}
					try {

						//used an api which looks up zipcode and returns the distance between two Zipcodes
				  obj = new URL("https://www.zipcodeapi.com/rest/wuBC8LO1XFIu4BX8FLXGYvTvF6XVr7cYBNAKzpLw4t195YhLKesIKldqsbmUiySU/distance.json/"+userZip+"/"+ X +"/mile");

					HttpURLConnection con = (HttpURLConnection) obj.openConnection();

					con.setRequestMethod("GET");

					//con.setRequestProperty("User-Agent", USER_AGENT);

					int responseCode = con.getResponseCode();

					//System.out.println("GET Response Code :: " + responseCode);

					if (responseCode == HttpURLConnection.HTTP_OK) { // success

						BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
						String inputLine;
						StringBuffer response = new StringBuffer();

						while ((inputLine = in.readLine()) != null) {
							response.append(inputLine);
						}
						in.close();
						
						// prints the distance of the mentioned zipcode to all other zipcodes stored in the hashmap
						JSONObject responseJSON = new JSONObject(response.toString());
						System.out.println("Distance between two cities : " + responseJSON.get("distance"));
						
						//I wish to make an array of all the distances and then return the minimum distance. 
						//The minimum distance will the closest copart facility
						JSONArray jArray = new JSONArray();
				
					}

					} catch (Exception e) {}
				result++ ;
				}
			}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		
		}
	}

}
