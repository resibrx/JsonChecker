package com.jsonchecker.JsonChecker.controller;

import com.jsonchecker.JsonChecker.jsonAction.JsonReader;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
public class JsonCheckerController {

    JsonReader readJson = new JsonReader();
    private static String UPLOADED_FOLDER = "/var/folders/57/f1yzs66s0g9bxyqw1j3m4t5c0000gp/T/";

    String pathOfFile;

    @GetMapping("/")
    public String index(){
        return "upload";
    }

    @PostMapping("/upload")
    public String singleFileUpload(@RequestParam("file")MultipartFile file, RedirectAttributes redirectAttributes) {
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
            return "redirect:uploadStatus";
        }

        try {

            // Get the file and save it somewhere
            byte[] bytes = file.getBytes();
            Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
            pathOfFile=path.toString();
            Files.write(path, bytes);

            redirectAttributes.addFlashAttribute("message",
                    "You successfully uploaded '" + file.getOriginalFilename() + "'");

        } catch (IOException e) {
            e.printStackTrace();
        }

        return "redirect:/uploadStatus" + readJson.getJson(pathOfFile);
    }

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
