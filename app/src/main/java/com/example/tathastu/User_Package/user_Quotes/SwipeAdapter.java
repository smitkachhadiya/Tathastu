package com.example.tathastu.User_Package.user_Quotes;

import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class SwipeAdapter {
    private final Context context;
    private final SwipeAdapterListener listener;
    private final List<itemmodel> items;

    public SwipeAdapter(Context context, SwipeAdapterListener listener) {
        this.context = context;
        this.listener = listener;
        this.items = new ArrayList<>();
    }

    public void fetchQuotes() {
        new FetchQuotesTask().execute();
    }

    public void clearData() {
        this.items.clear();
    }

    private class FetchQuotesTask extends AsyncTask<Void, Void, List<itemmodel>> {
        @Override
        protected List<itemmodel> doInBackground(Void... voids) {
            List<itemmodel> fetchedQuotes = new ArrayList<>();

            try {
                // Specify the URL of your API
                URL url = new URL("https://meetk1803.github.io/tathastu_quotes_api/tathastu_quotes.json");

                // Open the connection to the URL
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuilder builder = new StringBuilder();
                if (inputStream == null) {
                    return null;
                }
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line).append("\n");
                }
                if (builder.length() == 0) {
                    return null;
                }
                String quoteJsonString = builder.toString();

                // Check if the JSON response is null or empty
                if (quoteJsonString != null && !quoteJsonString.isEmpty()) {
                    // Parse the JSON array and extract quotes
                    JSONArray quotesArray = new JSONArray(quoteJsonString);
                    for (int i = 0; i < quotesArray.length(); i++) {
                        JSONObject quoteObject = quotesArray.getJSONObject(i);
                        int id = quoteObject.getInt("id");
                        String quote = quoteObject.getString("quote");
                        // Assuming you have some logic to create itemmodel instances
                        itemmodel currentItem = new itemmodel();
                        currentItem.setId(id);
                        currentItem.setQuote(quote);
                        fetchedQuotes.add(currentItem);
                    }
                }
            } catch (IOException e) {
                // Handle network-related errors
                e.printStackTrace();
            } catch (JSONException e) {
                // Handle JSON parsing errors
                e.printStackTrace();
            } catch (Exception e) {
                // Handle other exceptions
                e.printStackTrace();
            }

            return fetchedQuotes;
        }

        @Override
        protected void onPostExecute(List<itemmodel> result) {
            if (result != null) {
                listener.onQuotesFetched(result);
            }
        }
    }

    public interface SwipeAdapterListener {
        void onQuotesFetched(List<itemmodel> quotes);
    }
}
