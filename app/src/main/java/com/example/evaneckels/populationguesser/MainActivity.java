package com.example.evaneckels.populationguesser;

import android.content.DialogInterface;
import android.graphics.Color;
import android.preference.DialogPreference;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.IOException;
import java.util.Random;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import android.util.Log;
import android.widget.TextView;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;


public class MainActivity extends AppCompatActivity {

    private TextView pop1;
    private TextView pop2;
    private TextView name1;
    private TextView name2;
    Button button1;
    Button button2;
    Game newGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        newGame = new Game();
        String[] stateArray = {"01", "02", "04", "05", "06", "08", "09", "10", "11", "12", "13", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "44", "45", "46", "47", "48", "49", "50", "51", "53", "54", "55", "56"};
        Random rand1 = new Random();
        int firstIndex = rand1.nextInt(stateArray.length);
        Random rand2 = new Random();
        int secondIndex = rand2.nextInt(stateArray.length);
        while (firstIndex == secondIndex) {
            Random randTemp = new Random();
            secondIndex = randTemp.nextInt(stateArray.length);
        }

        name1 = (TextView)findViewById(R.id.left);
        pop1 = (TextView)findViewById(R.id.right);
        name2 = (TextView)findViewById(R.id.left2);
        pop2 = (TextView)findViewById(R.id.right2);
        button1 = (Button)findViewById(R.id.firstbutton);
        button2 = (Button)findViewById(R.id.secondbutton);

        OkHttpClient client = new OkHttpClient();
        String url = "https://api.census.gov/data/2017/pep/population?get=POP,GEONAME&for=state:" + stateArray[firstIndex] + "&key=79b74d7e5eef63833ecf3a8c67297f458ca8f621";
        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String myResponse = response.body().string();
                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            int counter = 31;
                            while (myResponse.charAt(counter) != '"') {
                                counter++;
                            }
                            String parsedStringPopulation = myResponse.substring(30, counter);
                            int counter2 = counter + 4;
                            while (myResponse.charAt(counter2) != '"') {
                                counter2++;
                            }
                            String parsedName = myResponse.substring(counter + 3, counter2);
                            name1.setText(parsedName);
                            pop1.setText(parsedStringPopulation);
                            button1.setText(parsedName);
                        }
                    });
                }
            }
        });

        OkHttpClient client2 = new OkHttpClient();
        String url2 = "https://api.census.gov/data/2017/pep/population?get=POP,GEONAME&for=state:" + stateArray[secondIndex] + "&key=79b74d7e5eef63833ecf3a8c67297f458ca8f621";
        Request request2 = new Request.Builder()
                .url(url2)
                .build();

        client2.newCall(request2).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String myResponse = response.body().string();
                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            int counter = 31;
                            while (myResponse.charAt(counter) != '"') {
                                counter++;
                            }
                            String parsedStringPopulation = myResponse.substring(30, counter);
                            int counter2 = counter + 4;
                            while (myResponse.charAt(counter2) != '"') {
                                counter2++;
                            }
                            String parsedName = myResponse.substring(counter + 3, counter2);
                            name2.setText(parsedName);
                            pop2.setText(parsedStringPopulation);
                            button2.setText(parsedName);
                        }
                    });
                }
            }
        });
        final String usePop1 = pop1.getText().toString();
        final String usePop2 = pop2.getText().toString();
        final String useName1 = name1.getText().toString();
        final String useName2 = name2.getText().toString();

        newGame.question.setState1(useName1, usePop1);
        newGame.question.setState2(useName2, usePop2);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int first = Integer.parseInt(pop1.getText().toString());
                int second = Integer.parseInt(pop2.getText().toString());
                name1.setTextColor(Color.BLACK);
                name2.setTextColor(Color.BLACK);
                pop1.setTextColor(Color.BLACK);
                pop2.setTextColor(Color.BLACK);
                if (first > second) {
                    button1.setBackgroundColor(Color.GREEN);
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Correct!");
                    builder.setMessage("You really know your State populations!");
                    builder.setPositiveButton("Play Again", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                            startActivity(getIntent());
                        }
                    });
                    builder.create();
                    builder.show();
                } else {
                    button1.setBackgroundColor(Color.RED);
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Wrong!");
                    builder.setMessage("Better luck next time!");
                    builder.setPositiveButton("Play Again", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                            startActivity(getIntent());
                        }
                    });
                    builder.create();
                    builder.show();
                }
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int first = Integer.parseInt(pop1.getText().toString());
                int second = Integer.parseInt(pop2.getText().toString());
                name1.setTextColor(Color.BLACK);
                name2.setTextColor(Color.BLACK);
                pop1.setTextColor(Color.BLACK);
                pop2.setTextColor(Color.BLACK);
                if (first < second) {
                    button2.setBackgroundColor(Color.GREEN);
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Correct!");
                    builder.setMessage("You really know your State populations!");
                    builder.setPositiveButton("Play Again", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                            startActivity(getIntent());
                        }
                    });
                    builder.create();
                    builder.show();
                } else {
                    button2.setBackgroundColor(Color.RED);
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Wrong!");
                    builder.setMessage("Better luck next time!");
                    builder.setPositiveButton("Play Again", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                            startActivity(getIntent());
                        }
                    });
                    builder.create();
                    builder.show();
                }
            }
        });
    }

    public class State {
        private String name;
        private String population;
        State(String newName, String newPop) {
            name = newName;
            population = newPop;
        }

        public void setName(String newName) {
            this.name = newName;
        }

        public String getName() {
            return name;
        }

        public void setPopulation(String newPopulation) {
            this.population = newPopulation;
        }

        public String getPopulation() {
            return population;
        }
    }

    public class Question {
        private State state1;
        private State state2;
        Question() {

        }

        public State getState1() {
            return state1;
        }

        public State getState2() {
            return state2;
        }

        public void setState1(String name, String pop) {
            this.state1 = new State(name, pop);
        }

        public void setState2(String name, String pop) {
            this.state2 = new State(name, pop);
        }
    }

    public class Game {
        Question question;
        String title;
        Game() {
            question = new Question();
        }
    }

}