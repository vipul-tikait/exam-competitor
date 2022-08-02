package com.exam.competitor.admin.restcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.exam.competitor.admin.service.StreamingService;

import reactor.core.publisher.Mono;

@RestController
public class StreamingRestController {

    @Autowired
    private StreamingService service;

    @GetMapping(value = "videos/video/{title}", produces = "video/mp4")
    public Mono<Resource> getVideo(@PathVariable String title, @RequestHeader("Range") String range) {
        System.out.println(range);
        
        return service.getVideo(title);
    }
    
    
    
    

}