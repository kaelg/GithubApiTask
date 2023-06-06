package com.porebski.GithubApiTask.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.porebski.GithubApiTask.Exception.GitHubUserNotFoundException;
import com.porebski.GithubApiTask.Exception.NotAcceptableTypeException;
import com.porebski.GithubApiTask.Exception.RestExceptionsHandler;
import com.porebski.GithubApiTask.Model.Response;
import com.porebski.GithubApiTask.Service.ResponseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/response")

public class ResponseController {
    @Autowired
    ResponseService responseService;

    @ExceptionHandler(RestExceptionsHandler.class)
    @GetMapping(value = "/{username}")
    public List<Response> get(@PathVariable String username, @RequestParam String dataFormat) throws GitHubUserNotFoundException, JsonProcessingException, NotAcceptableTypeException {
        return responseService.getResponse(username, dataFormat);
    }
}
