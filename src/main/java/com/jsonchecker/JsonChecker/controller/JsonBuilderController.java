package com.jsonchecker.JsonChecker.controller;

import com.jsonchecker.JsonChecker.jsonAction.JsonInput;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonWriter;
import java.io.FileWriter;
import java.io.IOException;

@Controller
public class JsonBuilderController {

    FileWriter fileWriter;

    @GetMapping("/jsonBuilder")
    public ModelAndView getJsonBuilderForm(){
        ModelAndView model = new ModelAndView("jsonBuilder");
        model.addObject("jsonInput", new JsonInput());
        return model;
    }

    @PostMapping("/jsonBuilder")
    public ModelAndView postCreatedJson(@ModelAttribute JsonInput jsonInput) {
        String jsonKey = jsonInput.getKey();
        String jsonValue = jsonInput.getValue();
        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add(jsonKey, jsonValue);
        JsonObject jsonObject = builder.build();

        try {
            fileWriter = new FileWriter("testfiles/generated-JSON.txt");
            JsonWriter jsonWriter = Json.createWriter(fileWriter);
            jsonWriter.writeObject(jsonObject);
            jsonWriter.close();
            fileWriter.close();
        } catch (IOException e){
            e.printStackTrace();
        }

        ModelAndView mav = new ModelAndView("jsonBuilder");
        mav.addObject("downloadFile", "testfiles/generated-JSON.txt");
        mav.addObject("generatedJson", jsonObject.toString());
        return mav;
    }
}
