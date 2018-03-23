package com.jsonchecker.JsonChecker.controller;

import com.google.gson.*;
import com.jsonchecker.JsonChecker.jsonAction.JsonReader;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
public class JsonCheckerController {

    public String pathOfFile;
    public String output;
    public JsonReader readJson = new JsonReader();
    private static String UPLOADED_FOLDER = "/var/folders/57/f1yzs66s0g9bxyqw1j3m4t5c0000gp/T/";


    @GetMapping("/")
    public String home(){

        return "jsonChecker";
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



}
