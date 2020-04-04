package ihm.accidents.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListAccidentModel {


    ArrayList<AccidentModel> accidents;

    public ListAccidentModel() {
        accidents= new ArrayList<>();
    }

    public ListAccidentModel(ArrayList<AccidentModel> accidents){
        this.accidents=accidents;
    }
    public void setListFromServeur(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println("json");
                    getListFromWeb();
                }catch (MalformedURLException e){
                    e.printStackTrace();
                }
            }
        }).start();
    }
    private void getListFromWeb() throws MalformedURLException {
        URL url = new URL("http://yassin.dinelhost.com:9428/api/accidents/");
        System.out.println("jsonURL"+url);
        try {
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            try  {
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                String jsonString = readStream(in);
                ObjectMapper mapper= new ObjectMapper();
                ListAccidentModel ListServ = mapper.readValue(jsonString,ListAccidentModel.class);
                this.accidents= ListServ.accidents;

                System.out.println("json"+ListServ.accidents);
            }finally {
                urlConnection.disconnect();
            }
        }catch (IOException e){
            System.out.println("json catch"+e.toString());
            e.printStackTrace();
        }
    }
    private String readStream(InputStream in) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader buffer = new BufferedReader(new InputStreamReader(in));
        for(String line = buffer.readLine(); line != null; line = buffer.readLine()){
            stringBuilder.append(line);
        }
        in.close();
        return stringBuilder.toString();
    }

    public ArrayList<AccidentModel> getAccident() {
        return accidents;
    }
}
