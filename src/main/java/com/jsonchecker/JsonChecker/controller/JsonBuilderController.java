package com.jsonchecker.JsonChecker.controller;

import com.jsonchecker.JsonChecker.jsonAction.JsonInput;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

    FileWriter fw;

    @GetMapping("/jsonBuilder")
    public String jsonBuilder(Model model){
        model.addAttribute("jsonInput", new JsonInput());
        return "jsonBuilder";
    }

    @PostMapping("/jsonBuilder")
    public ModelAndView greetingSubmit(@ModelAttribute JsonInput jsonInput) {
        String jsonKey = jsonInput.getKey();
        String jsonValue = jsonInput.getValue();
        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add(jsonKey, jsonValue);
        JsonObject jo = builder.build();

        try {
            fw = new FileWriter("testfiles/generated-JSON.txt");
            JsonWriter jsonWriter = Json.createWriter(fw);
            jsonWriter.writeObject(jo);
            jsonWriter.close();
            fw.close();
        } catch (IOException e){
            e.printStackTrace();
        }

        ModelAndView mav = new ModelAndView("jsonBuilder");
        mav.addObject("downloadFile", "generated-JSON.txt");
        mav.addObject("generatedJson", jo.toString());
        return mav;
    }
}
