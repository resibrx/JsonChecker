package com.jsonchecker.JsonChecker.jsonAction;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class JsonReader {

    JSONObject jsonObject;

    public String getJson(String file) {
        JSONParser parser = new JSONParser();

        try {

            Object obj = parser.parse(new FileReader(file));

            jsonObject = (JSONObject) obj;

            String name = (String) jsonObject.get("name");
            //System.out.println("Vorname: " + name);


            long age = (Long) jsonObject.get("age");
            //System.out.println("Alter: " + age);

            JSONObject address = (JSONObject) jsonObject.get("address");
            String city = (String) address.get("city");
            String street = (String) address.get("street");
           // System.out.println("Straße: " + street);
           // System.out.println("Stadt: " + city);

            JSONArray phone = (JSONArray) jsonObject.get("phoneNumber");
            for(int i = 0; i < phone.size(); i++) {
                JSONObject phoneObj = (JSONObject) phone.get(i);
                String type = (String) phoneObj.get("type");
                String number = (String) phoneObj.get("number");
               // System.out.println("Art: " + type);
              //  System.out.println("Nummer: " + number);
            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return jsonObject.toJSONString();

    }
}
