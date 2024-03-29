package com.example.incrementum;

import androidx.appcompat.app.AppCompatActivity;

public class only_habits extends AppCompatActivity {
//
//    String username;
//    String completeString;
//    String email;
//    String id;
//    int counter = 0;
//
//    TextView h1;
//    TextView t1;
//    TextView st1;
//    TextView h2;
//    TextView t2;
//    TextView st2;
//    TextView h3;
//    TextView t3;
//    TextView st3;
//    TextView total;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_only_habits);
//
//        UserInfo user = (UserInfo) getApplication();
//        username = user.getUserName();
//        email = user.getEmail();
//        id = user.getUserId();
//        completeString = "Habit Stats For " + username;
//
//        TextView userText = findViewById(R.id.usertext);
//        userText.setText(completeString);
//
//        h1 = findViewById(R.id.H1);
//        h1.setVisibility(View.INVISIBLE);
//        h2 = findViewById(R.id.H2);
//        h2.setVisibility(View.INVISIBLE);
//        h3 = findViewById(R.id.H3);
//        h3.setVisibility(View.INVISIBLE);
//
//        t1 = findViewById(R.id.T1);
//        t1.setVisibility(View.INVISIBLE);
//        t2 = findViewById(R.id.T2);
//        t2.setVisibility(View.INVISIBLE);
//        t3 = findViewById(R.id.T3);
//        t3.setVisibility(View.INVISIBLE);
//
//        st1 = findViewById(R.id.ST1);
//        st1.setVisibility(View.INVISIBLE);
//        st2 = findViewById(R.id.ST2);
//        st2.setVisibility(View.INVISIBLE);
//        st3 = findViewById(R.id.ST3);
//        st3.setVisibility(View.INVISIBLE);
//
//        total = findViewById(R.id.TTL);
//
//        //getData(email);
//
//        final Button backButton = findViewById(R.id.mainbutton);
//        final Button hobbyButton = findViewById(R.id.hobbybutton);
//
//        backButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                goBack();
//                sendData(username, 1);
//            }
//        });
//
//        hobbyButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                goHobby();
//                sendData(username, 2);
//            }
//        });
//        DatabaseLoad ld = new DatabaseLoad();
//        ld.execute();
//    }
//
//
//    private class DatabaseLoad extends AsyncTask<Void,Void,Void> {
//        RemoteFindIterable <Document>  results;
//        @Override
//        protected void onPreExecute() {
//            RemoteFindIterable<Document> habitresults;
//
//            final StitchAppClient client =
//                    Stitch.getAppClient("incrementum-xjkms");
//            final RemoteMongoClient mongoClient =
//                    client.getServiceClient(RemoteMongoClient.factory, "mongodb-atlas");
//
//            final RemoteMongoCollection<Document> habitcoll =
//                    mongoClient.getDatabase("Incrementum").getCollection("Habits");
//
//            results = habitcoll.find(Filters.eq("userId",id))
//                    .projection(
//                            new Document());
//            super.onPreExecute();
//        }
//        @Override
//        protected Void doInBackground(Void... voids) {
//            Log.d("BACKGROUND","************************************");
//            results.forEach(item ->{
//                try{
//                    String name ="";
//                    JSONObject obj = new JSONObject(item.toJson());
//                    Log.d("**********",obj.toString());
//                    name = obj.getString("name");
//                    Toast.makeText(getBaseContext(), name, Toast.LENGTH_LONG).show();
//                    JSONArray triggArr = new JSONArray();
//                    triggArr = obj.getJSONArray("Triggers");
//                    JSONArray timesArr = new JSONArray();
//
//                    ArrayList<String> trigList = new ArrayList();
//                    for(int i = 0; i < triggArr.length(); i++)
//                    {
//                        trigList.add(triggArr.get(i).toString());
//                    }
//
//                    ArrayList<String> timeList = new ArrayList();
//                    for(int i = 0; i < timesArr.length(); i++)
//                    {
//                        timeList.add(timesArr.get(i).toString());
//                    }
//                    updateUI(name, trigList, timeList);
//                }
//                catch(JSONException e){
//                    Log.d("JSON exception:",e.toString());
//                }
//            });
//            return null;
//        }
//        @Override
//        protected void onPostExecute(Void aVoid) {
//            runOnUiThread(() -> {
//
//
//            });
//            super.onPostExecute(aVoid);
//        }
//    }
//
//
////    public void getData(String email) {
////
////        RemoteFindIterable<Document> habitresults;
////
////        final StitchAppClient client =
////                Stitch.getAppClient("incrementum-xjkms");
////        final RemoteMongoClient mongoClient =
////                client.getServiceClient(RemoteMongoClient.factory, "mongodb-atlas");
////
////        final RemoteMongoCollection<Document> habitcoll =
////                mongoClient.getDatabase("Incrementum").getCollection("Habits");
////
////        habitresults = habitcoll.find(Filters.eq("userId",id))
////                .projection(
////                        new Document());
////        habitresults.forEach(item -> {
////            String name = "";
////            try {
////                JSONObject obj = new JSONObject(item.toJson());
////                Log.d("**********",obj.toString());
////                name = obj.getString("name");
////                Toast.makeText(getBaseContext(), name, Toast.LENGTH_LONG).show();
////                JSONArray triggArr = new JSONArray();
////                triggArr = obj.getJSONArray("Triggers");
////                JSONArray timesArr = new JSONArray();
////
////                ArrayList<String> trigList = new ArrayList();
////                for(int i = 0; i < triggArr.length(); i++)
////                {
////                    trigList.add(triggArr.get(i).toString());
////                }
////
////                ArrayList<String> timeList = new ArrayList();
////                for(int i = 0; i < timesArr.length(); i++)
////                {
////                    timeList.add(timesArr.get(i).toString());
////                }
////
////
////            updateUI(name, trigList, timeList);
////
////        } catch (JSONException e) {
////                e.printStackTrace();
////            }
////        });
////    }
//
//    public void sendData(String username, int location)
//    {
//        if(location == 1) {
//            Intent intent = new Intent(getApplicationContext(), Hobby_Stats.class);
//            startActivity(intent);
//        }
//        else if(location == 2) {
//            Intent intent = new Intent(getApplicationContext(), only_hobbies.class);
//            startActivity(intent);
//        }
//    }
//
//    public void updateUI(String name, ArrayList triggArr, ArrayList timesArr)
//    {
//        String newString;
//        if(counter == 0) {
//            newString = "Habit One: " + name;
//            h1.setText(newString);
//            h1.setVisibility(View.VISIBLE);
//
//            newString = "Triggers: ";
//            for(int i = 0; i < triggArr.size(); i++)
//            {
//                newString = newString + i + ": " + triggArr.get(i);
//            }
//            t1.setText(newString);
//            t1.setVisibility(View.VISIBLE);
//
//            newString = "Times: ";
//            for(int i = 0; i < timesArr.size(); i++)
//            {
//                newString = newString + i + ": " + timesArr.get(i);
//            }
//            st1.setText(newString);
//            st1.setVisibility(View.VISIBLE);
//
//            total.setText("Total Habits: " + counter);
//            counter++;
//        } else if(counter == 1) {
//            newString = "Habit Two: " + name;
//            h2.setText(newString);
//            h2.setVisibility(View.VISIBLE);
//
//            newString = "Triggers: ";
//            for(int i = 0; i < triggArr.size(); i++)
//            {
//                newString = newString + i + ": " + triggArr.get(i);
//            }
//            t2.setText(newString);
//            t2.setVisibility(View.VISIBLE);
//
//            newString = "Times: ";
//            for(int i = 0; i < timesArr.size(); i++)
//            {
//                newString = newString + i + ": " + timesArr.get(i);
//            }
//            st2.setText(newString);
//            st2.setVisibility(View.VISIBLE);
//
//            total.setText("Total Habits: " + counter);
//            counter++;
//        } else if(counter == 2) {
//            newString = "Habit Three: " + name;
//            h3.setText(newString);
//            h3.setVisibility(View.VISIBLE);
//
//            newString = "Triggers: ";
//            for(int i = 0; i < triggArr.size(); i++)
//            {
//                newString = newString + i + ": " + triggArr.get(i);
//            }
//            t3.setText(newString);
//            t3.setVisibility(View.VISIBLE);
//
//            newString = "Times: ";
//            for(int i = 0; i < timesArr.size(); i++)
//            {
//                newString = newString + i + ": " + timesArr.get(i);
//            }
//            st3.setText(newString);
//            st3.setVisibility(View.VISIBLE);
//
//            total.setText("Total Habits: " + counter);
//            counter++;
//        }
//    }
//
//    public void goBack() {
//        Intent intent = new Intent(this, Hobby_Stats.class);
//        startActivity(intent);
//    }
//
//    public void goHobby() {
//        Intent intent = new Intent(this, only_hobbies.class);
//        startActivity(intent);
//    }
}
