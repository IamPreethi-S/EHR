package com.example.ehr.insurance.worker;

import android.os.AsyncTask;

import com.example.ehr.insurance.InsuranceSubscribersFragment;
import com.example.ehr.insurance.model.InsuranceSubscriberModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class BackgroundInsuranceSubscriberWorker extends AsyncTask<Object, Void, String> {

    InsuranceSubscribersFragment insuranceSubscribersFragment;

    String actionType;
    public BackgroundInsuranceSubscriberWorker(InsuranceSubscribersFragment fragment) {
        this.insuranceSubscribersFragment = fragment;
    }

    @Override
    protected String doInBackground(Object... params) {
        actionType = (String) params[0];
        String baseUrl = "https://kxp9181.uta.cloud";

        try {
            if (actionType.equals("get_subscribers")) {
                String id = (String) params[1];

                String urlString = baseUrl + "/getInsuranceSubscribers.php?id=" + id;
                URL url = new URL(urlString);

                return WorkerHelper.handleGetRequest(url);
            }
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected void onPostExecute(String resultString) {
        handleResponse(resultString);
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    private void handleResponse(String resultString) {
        try {
            if (actionType.equals("get_subscribers")) {
                JSONArray resultArr = new JSONArray(resultString);

                List<InsuranceSubscriberModel> subscribers = new ArrayList<>();

                for (int i=0; i<resultArr.length(); i++) {
                    JSONObject claimObj = (JSONObject) resultArr.get(i);
                    String patientId = claimObj.getString("patientId");
                    String firstName = claimObj.getString("firstName");
                    String lastName = claimObj.getString("lastName");
                    String expiry = claimObj.getString("expiry");

                    InsuranceSubscriberModel subscriber = new InsuranceSubscriberModel(patientId,firstName, lastName, expiry);
                    subscribers.add(subscriber);
                }

                this.insuranceSubscribersFragment.onLoadSuccess(subscribers);
            }
        } catch (JSONException e) {
            if (actionType.equals("update_claim")) {
                this.insuranceSubscribersFragment.onLoadFailed("Something went wrong");
            }
        }
    }

}
