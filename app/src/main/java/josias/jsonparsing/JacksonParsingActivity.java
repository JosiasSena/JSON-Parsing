package josias.jsonparsing;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class JacksonParsingActivity extends ListActivity implements Runnable {

    // JSON Node names
    private static final String TAG_CONTACTS = "contacts";
    private static final String TAG_ID = "id";
    private static final String TAG_NAME = "name";
    private static final String TAG_EMAIL = "email";
    private static final String TAG_ADDRESS = "address";
    private static final String TAG_GENDER = "gender";
    private static final String TAG_PHONE = "phone";
    private static final String TAG_PHONE_MOBILE = "mobile";
    private static final String TAG_PHONE_HOME = "home";
    private static final String TAG_PHONE_OFFICE = "office";

    // Hashmap for ListView
    private ArrayList<HashMap<String, String>> contactList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.the_list);

        Thread mThread = new Thread(this);
        mThread.start();
        try {
            mThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        init();
    }

    private void init() {
        // Updating parsed JSON data into ListView
        ListAdapter adapter = new SimpleAdapter(
                this, // Context
                contactList, // The arrayList holding the information
                R.layout.list_item, // ListView item layout to use
                new String[]{TAG_NAME, TAG_EMAIL, TAG_PHONE_MOBILE}, // from these nodes in the hashmap
                new int[]{R.id.name, R.id.email, R.id.mobile}); // to these TextViews

        // Setting the adapter --^ to the ListView
        setListAdapter(adapter);
    }

    @Override
    public void run() {
        ObjectMapper mapper = new ObjectMapper();
        contactList = new ArrayList<>();

        try {
            String url = "http://api.androidhive.info/contacts/";
            JsonNode rootNode = mapper.readTree(new URL(url)); // the entire json data (the tree)
            JsonNode contacts = rootNode.get(TAG_CONTACTS); // the contacts array inside the tree

            for (int i = 0; i < contacts.size(); i++) {
                JsonNode contact = contacts.get(i); // a node location inside the contacts array
                HashMap<String, String> contactMap = new HashMap<>(); // this is where we will store all the data we get

                String name = contact.findValue(TAG_NAME).textValue();
                String email = contact.findValue(TAG_EMAIL).textValue();
                String mobilePhone = contact.findValue(TAG_PHONE_MOBILE).textValue();

                contactMap.put(TAG_NAME, name);
                contactMap.put(TAG_EMAIL, email);
                contactMap.put(TAG_PHONE_MOBILE, mobilePhone);
                contactList.add(contactMap); // add the hashmap to the arraylist
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
