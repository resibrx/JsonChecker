package com.jsonchecker.JsonChecker.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.*;
import com.jsonchecker.JsonChecker.jsonAction.Greeting;
import com.jsonchecker.JsonChecker.jsonAction.JsonReader;
import com.sun.org.apache.bcel.internal.generic.IMPDEP1;
import jdk.nashorn.internal.parser.JSONParser;
import org.apache.commons.lang.StringEscapeUtils;
import org.json.XML;
import org.json.simple.JSONArray;
import org.springframework.ui.Model;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonWriter;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
public class JsonCheckerController {

    JsonReader readJson = new JsonReader();
    private static String UPLOADED_FOLDER = "/var/folders/57/f1yzs66s0g9bxyqw1j3m4t5c0000gp/T/";
    FileWriter fw;

    String pathOfFile;
    String output;

    @GetMapping("/")
    public String index(){
        return "upload";
    }

    @GetMapping("/xmlConverter")
    public ModelAndView xmlConverter(){
        ModelAndView mav = new ModelAndView("xmlConverter");
        return mav;
    }

    @GetMapping("/jsonBuilder")
    public String jsonBuilder(Model model){
        model.addAttribute("greeting", new Greeting());
        return "jsonBuilder";
    }

    @PostMapping("/jsonBuilder")
    public ModelAndView greetingSubmit(@ModelAttribute Greeting greeting) {
        String jsonKey = greeting.getKey();
        String jsonValue = greeting.getValue();
        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add(jsonKey, jsonValue);
        JsonObject jo = builder.build();

        try {
            fw = new FileWriter("generated-JSON.txt");
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


    @PostMapping("/upload")
    public String singleFileUpload(@RequestParam("file")MultipartFile file, RedirectAttributes redirectAttributes) {
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
            return "redirect:/";
        }

        try {

            byte[] bytes = file.getBytes();
            Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
            pathOfFile=path.toString();
            Files.write(path, bytes);
            output = readJson.getJson(pathOfFile);
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            JsonParser jp = new JsonParser();
            JsonElement je = jp.parse(output);
            String prettyJsonString = gson.toJson(je);


            redirectAttributes.addFlashAttribute("status", "You successfully uploaded '" + file.getOriginalFilename() + "'");
            redirectAttributes.addFlashAttribute("message",
                     prettyJsonString);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return "redirect:/";
    }

    @PostMapping("/convert")
    public String convertFile(@RequestParam("file")MultipartFile file, RedirectAttributes redirectAttributes){
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
            return "redirect:/";
        }
        try {

            byte[] bytes = file.getBytes();
            Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
            pathOfFile=path.toString();
            Files.write(path, bytes);
            output = readJson.getJson(pathOfFile);

            JsonParser parser = new JsonParser();
            JsonObject json = (JsonObject) parser.parse(output);
            String xml = XML.toString(json);



            redirectAttributes.addFlashAttribute("status", "You successfully uploaded '" + file.getOriginalFilename() + "'");
            redirectAttributes.addFlashAttribute("message",
                    StringEscapeUtils.unescapeXml(xml));

        } catch (IOException e) {
            e.printStackTrace();
        }
            return "redirect:/";
    }

    /*
    public int countJsonKeys(String json){
        JSONArray array = new JSONArray()
    }
    */

    @GetMapping("/uploadStatus")
    public String uploadStatus() {
        return "uploadStatus";
    }


    @GetMapping("/JsonOutput")
    public ModelAndView greeting() {
        ModelAndView mav = new ModelAndView("OutputPage");
        mav.addObject("out", readJson.getJson(""));
        return mav;
    }

    @GetMapping("/empty")
    public String showEmpty() {
        return "Fehler";
    }
}
