package josias.jsonparsing;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


public class NormalJSONParsing extends ListActivity {

    private ProgressDialog pDialog;

    // URL to get contacts JSON
    private static String url = "http://api.androidhive.info/contacts/";

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

    // contacts JSONArray
    JSONArray contacts = null;

    // Hashmap for ListView
    ArrayList<HashMap<String, String>> contactList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.the_list);

        contactList = new ArrayList<HashMap<String, String>>();

        ListView lv = getListView();

        // Calling async task to get json
        new GetContacts().execute();
    }

    /**
     * Async task class to get json by making HTTP call
     */
    private class GetContacts extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            // Showing progress dialog
            pDialog = new ProgressDialog(NormalJSONParsing.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            /** Making a request to url and getting response
             * url = the url where we are getting the information from
             */
            String jsonStr = makeServiceCall(url);

            // Show all of the data in logcat
            Log.d("Response: ", "> " + jsonStr);

            if (jsonStr != null) {
                try {
                    // create a JSON object from the string received
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array
                    contacts = jsonObj.getJSONArray(TAG_CONTACTS);

                    // looping through All Contacts
                    for (int i = 0; i < contacts.length(); i++) {
                        JSONObject jsonObject = contacts.getJSONObject(i);

                        // Getting the information from the nodes in the JSON Array
                        String id = jsonObject.getString(TAG_ID);
                        String name = jsonObject.getString(TAG_NAME);
                        String email = jsonObject.getString(TAG_EMAIL);
                        String address = jsonObject.getString(TAG_ADDRESS);
                        String gender = jsonObject.getString(TAG_GENDER);

                        // Phone node is JSON Object
                        JSONObject phone = jsonObject.getJSONObject(TAG_PHONE);
                        String mobile = phone.getString(TAG_PHONE_MOBILE);
                        String home = phone.getString(TAG_PHONE_HOME);
                        String office = phone.getString(TAG_PHONE_OFFICE);

                        // Temporary HashMap for single contact
                        HashMap<String, String> contact = new HashMap<String, String>();

                        // adding each child node to HashMap key => value
                        contact.put(TAG_ID, id);
                        contact.put(TAG_NAME, name);
                        contact.put(TAG_EMAIL, email);
                        contact.put(TAG_PHONE_MOBILE, mobile);

                        // adding the contact to contact list
                        contactList.add(contact);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            // Dismiss the progress dialog
            if (pDialog.isShowing()) {
                pDialog.dismiss();
            }

            // Updating parsed JSON data into ListView
            ListAdapter adapter = new SimpleAdapter(
                    getApplicationContext(), // Context
                    contactList, // The arrayList holding the information
                    R.layout.list_item, // ListView item layout to use
                    new String[]{TAG_NAME, TAG_EMAIL, TAG_PHONE_MOBILE}, // from these nodes
                    new int[]{R.id.name, R.id.email, R.id.mobile}); // to these TextViews

            // Setting the adapter --^ to the ListView
            setListAdapter(adapter);
        }

    }

    /**
     * Making service call
     *
     * @url - url to make request
     * @method - http request method
     * @params - http request params
     */
    public String makeServiceCall(String url) {
        String response = null;

        try {
            // HTTP client
            DefaultHttpClient httpClient = new DefaultHttpClient();

            // Lets use the GET method
            HttpGet httpGet = new HttpGet(url);

            // Execute the client and get the response
            HttpResponse httpResponse = httpClient.execute(httpGet);
            HttpEntity httpEntity = httpResponse.getEntity();

            // Turn the entity into a string to use
            response = EntityUtils.toString(httpEntity);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response;
    }
}
