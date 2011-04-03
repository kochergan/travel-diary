package mobop.traveldiary;

import java.util.Calendar;

import mobop.traveldiary.bs.HttpRequestPerformer;
import mobop.traveldiary.bs.Response;
import mobop.traveldiary.pd.Location;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

public class TravelDiary extends Activity {
    
	private static final int MENU_POST = 0x1;
	private static final int MENU_GET = 0x2;
	private static final int MENU_DELETE = 0x4;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.chooseit);
        
        etId = (EditText) findViewById(R.id.etID);
        etTitle = (EditText) findViewById(R.id.etTitle);
        etText = (EditText) findViewById(R.id.etText);
        etLat = (EditText) findViewById(R.id.etLat);
        etLong = (EditText) findViewById(R.id.etLong);
        //etResponse = (EditText) findViewById(R.id.etResponse);
        
        tvStatus = (TextView) findViewById(R.id.tvStatus);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	menu.add(0, MENU_POST, 0, "Post");
    	menu.add(0, MENU_GET, 0, "Get");
    	menu.add(0, MENU_DELETE, 0, "Delete");
    	
    	return true;
    }
    
    public boolean onOptionsItemSelected(MenuItem item) {
    	Location location = null;
    	Response response = null;
    	Calendar calendar = Calendar.getInstance();
    	
    	try {
	    	switch(item.getItemId()) {
	    	case MENU_POST:
	    		location = new Location(0, etTitle.getText().toString(), 
	    				calendar.getTime().getTime(), etText.getText().toString(),  
	    				Double.parseDouble(etLat.getText().toString()), 
	    				Double.parseDouble(etLong.getText().toString()));
	    		
	    		response = HttpRequestPerformer.postInfo(location);
	    		if(response == null) {
	    			Log.w("Response", "Empty response");
	    			return false;	    			
	    		}
	    		else {
	    			tvStatus.setText(String.valueOf(response.getStatus()));
	    			//etResponse.setText(response.getXmlString());
	    			etId.setText(String.valueOf(response.getLocation().getId()));
	    		}
	    		
	    		break;
	    	case MENU_GET:
	    		location = new Location(Integer.parseInt(etId.getText().toString()));
	    		response = HttpRequestPerformer.getInfo(location);
	    		if(response == null) {
	    			Log.w("Response", "Empty response");
	    			return false;	    			
	    		}
	    		else {
	    			tvStatus.setText(String.valueOf(response.getStatus()));
	    			
	    			etTitle.setText(response.getLocation().getTitle());
	    			etText.setText(response.getLocation().getText());
	    			etLat.setText(String.valueOf(response.getLocation().getLatitude()));
	    			etLong.setText(String.valueOf(response.getLocation().getLongitude()));
	    		}
	    		
	    		break;
	    	case MENU_DELETE:
	    		location = new Location(Integer.parseInt(etId.getText().toString()));
	    		response = HttpRequestPerformer.deleteInfo(location);
	    		if(response == null) {
	    			Log.w("Response", "Empty response");
	    			return false;	    			
	    		}
	    		else {
	    			tvStatus.setText(String.valueOf(response.getStatus()));
	    		}
	    		
	    		break;
	    	}
    	} catch (Exception e) {
    		Log.e("Main", "Error: " + e.getMessage());
    	}
    	return true;
    }
    
    //Events
    
    //Objects and Variables
    private EditText etId = null;
    private EditText etTitle = null;
    private EditText etText = null;
    private EditText etLat = null;
    private EditText etLong = null;
    //private EditText etResponse = null;
    private TextView tvStatus = null;
}