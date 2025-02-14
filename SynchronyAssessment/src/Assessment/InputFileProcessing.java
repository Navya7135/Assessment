package Assessment;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
public class InputFileProcessing {
	public static void main(String[] args)
	{
		try {
		   File file = new File("input.txt"); // reading the file input.txt
		   Scanner fileReader = new Scanner(file);
		   List<Map<String,String>> keyValuePairs = new ArrayList<>();
		   Set<String> columnNames = new LinkedHashSet<>();
		   while(fileReader.hasNextLine())
		   {
			   String temp = fileReader.nextLine();
			   Map<String,String> row = parseLine(temp); //parsing each line in the file
			   keyValuePairs.add(row);  //adding each all the key values of specific row
			   columnNames.addAll(row.keySet()); // maintaining the set of all column names
		   }
		   fileReader.close();
		   //printing key and values
		   System.out.println(String.join("|", columnNames));
		   for(Map<String, String> pair:keyValuePairs)
		   {
			   List<String> r = new ArrayList<>();
	            for (String column : columnNames) {
	                r.add(pair.getOrDefault(column, ""));
	            }
	            System.out.println(String.join("|", r));
		   }
		}catch(FileNotFoundException e)
		{
			e.printStackTrace();
		}
		
	}

	private static Map<String, String> parseLine(String temp) {
		Map<String,String> rows = new LinkedHashMap<>();
		String[] pairs = temp.split("\\^\\^"); // splitting string according to delimiter
		String lastKey = null;
		for(String pair:pairs) //traversing thorugh each pair
		{
			if(pair.contains("=")) // checking if the string is a key value pair 
			{
				String[] keyValue = pair.split("=",2); // splitting the key value pair
			    String key = keyValue[0].trim();
			    if(!key.isEmpty()) // making sure that key is not empty
			    {
			    	String value = keyValue[1].replaceAll("[\\^=]", "");
			    	rows.put(key, value);
			    	lastKey = key;
			    }
			}else if(lastKey!=null) // if string doesn't contain = appending it to last key value
			{
				rows.put(lastKey, rows.get(lastKey)+""+pair.replaceAll("[\\^=]", ""));
			}
		}
		return rows;
	}

}
