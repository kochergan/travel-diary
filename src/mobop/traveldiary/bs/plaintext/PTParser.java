package mobop.traveldiary.bs.plaintext;

import android.util.Log;

import mobop.traveldiary.bs.Response;
import mobop.traveldiary.pd.Location;

public class PTParser {
	
	public static Response parsePost(String ptResponse) throws NumberFormatException {
		return parsePlainText(ptResponse);
	}

	private static Response parsePlainText(String ptResponse)throws NumberFormatException {
		Response response = null;
		Location location = null;
		
		String[] params = ptResponse.split("\\|");
		if(params == null)
			return null;
		for(int i=0; i<params.length; i++)
			Log.i("Response", "Param[" + i + "]: " + params[i]);
		if(params[0].equalsIgnoreCase("0")) {
			response = new Response(0, params[1], params[2]);
		} else if(params[0].equalsIgnoreCase("1")) {
			int type = Integer.parseInt(params[1]);
			switch(type) {
			case 1://insert
				location = new Location(Integer.parseInt(params[2]));
				response = new Response(1, 1, location, params[3]);
				
				break;
			case 2://view
				location = new Location(params[2], Long.parseLong(params[3]), 
						params[4], Double.parseDouble(params[5]), Double.parseDouble(params[6]));
				response = new Response(1, 2, location);
				
				break;
			case 3://delete
				location = new Location(Integer.parseInt(params[2]));
				response = new Response(1, 3, location, params[3]);
				
				break;
			}
		}
		
		return response;
	}
	
	
}