/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appchallenge;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 *
 * @author stephen.ojerinde
 */
public class AppChallenge {
    
    static ArrayList<String> User = new ArrayList<String>();
    static ArrayList<String> resultUserCount = new ArrayList<String>();
    static ArrayList<String> resultUserDate = new ArrayList<String>();
    static ArrayList<Integer> SubmissionCount = new ArrayList<Integer>();
    static ArrayList<Integer> CommentCount = new ArrayList<Integer>();
    static ArrayList<Integer> CreatedAt = new ArrayList<Integer>();

    static int n;

    public static void main(String[] args) throws Exception{
        
        Scanner input = new Scanner(System.in);

        System.out.println("How many pages does the API contain?");
        n = input.nextInt();

        AppChallenge obj = new AppChallenge();
        System.out.println("Testing - Send HTTP GET request");
        obj.sendGet();
        
        System.out.println("Highest Comment Count is " + getUsernameWithHigestComment());

        System.out.println("What is the threshold to get submission count?");
        int threshold = input.nextInt();

        System.out.println("The submission count greater than " + threshold + " = " + getUsernames(threshold));

        System.out.println("What is the threshold to sort date?");
        int threshold2 = input.nextInt();

        System.out.println("The record dates greater or equal to " + threshold2 + " = " + getUsernamesSortedByRecordDate(threshold2));

    }

    private void sendGet() throws Exception {
        String url = "https://jsonmock.hackerrank.com/api/article_users/search?page=";

        for (int i = 1; i <= n; i++) {

            HttpURLConnection httpClient = (HttpURLConnection) new URL(url + i).openConnection();

            //setting GET method
            httpClient.setRequestMethod("GET");

            int responseCode = httpClient.getResponseCode();
            System.out.println("Sending GET request ");
            System.out.println("Response Code " + responseCode);

            try (BufferedReader in = new BufferedReader(
                    new InputStreamReader(httpClient.getInputStream()))) {

                StringBuilder response = new StringBuilder();
                String line;

                while ((line = in.readLine()) != null) {
                    response.append(line);
                }
                System.out.println(response.toString());

                try {

                    JSONObject jsonObject = new JSONObject(response.toString());

                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    for (int j = 0; j < jsonArray.length(); j++) {

                        JSONObject jsonObject1 = jsonArray.getJSONObject(j);

                        int submissionCount = jsonObject1.getInt("submission_count");

                        String username = jsonObject1.getString("username");

                        int commentCount = jsonObject1.getInt("comment_count");

                        int createdAt = jsonObject1.getInt("created_at");

                        User.add(username);

                        CreatedAt.add(createdAt);

                        CommentCount.add(commentCount);

                        SubmissionCount.add(submissionCount);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }

//        System.out.println(SubmissionCount);
    }

    public static List<String> getUsernames(int threshold) {
        for (int i = 0; i < SubmissionCount.size(); i++) {
            if (SubmissionCount.get(i) >= threshold) {
                resultUserCount.add(User.get(i) + " - " + SubmissionCount.get(i).toString());
            }
        }
        return resultUserCount;
    }

    public static String getUsernameWithHigestComment() {
        return Collections.max(CommentCount).toString();
    }

    public static List<String> getUsernamesSortedByRecordDate(int threshold) {
        Collections.sort(CreatedAt);
        for (int i = 0; i < CreatedAt.size(); i++) {
            if (CreatedAt.get(i) >= threshold) {
                resultUserDate.add(User.get(i) + " - " + CreatedAt.get(i).toString());
            }
        }
        return resultUserDate;

    }
    
}
