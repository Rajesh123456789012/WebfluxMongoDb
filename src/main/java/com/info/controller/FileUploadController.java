package com.info.controller;


import com.info.entity.Employee;
import com.info.service.FileUploadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.codec.multipart.Part;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.io.IOException;

@RestController
@RequestMapping("/file")
@Slf4j
@Profile("!local")
public class FileUploadController {

    @Autowired
    private FileUploadService fileUploadService;


    @PostMapping(value = "/uploadExcel", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<Employee> uploadHandler(@RequestBody Flux<Part> parts) {
        //log.info("file upload {}", parts);
        return parts
                .filter(part -> part instanceof FilePart) // only retain file parts
                .ofType(FilePart.class) // convert the flux to FilePart
                .flatMap(filePart -> {
                    try {
                        return fileUploadService.saveExcelData(filePart);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
    }
}
