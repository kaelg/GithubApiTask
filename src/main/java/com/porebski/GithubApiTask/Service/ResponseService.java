package com.porebski.GithubApiTask.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.porebski.GithubApiTask.Exception.GitHubUserNotFoundException;
import com.porebski.GithubApiTask.Exception.NotAcceptableTypeException;
import com.porebski.GithubApiTask.Model.BranchesInfo;
import com.porebski.GithubApiTask.Model.Response;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class ResponseService {
    RestTemplate restTemplate = new RestTemplate();

    public List<Response> getResponse(String username, String header) throws GitHubUserNotFoundException, JsonProcessingException, NotAcceptableTypeException {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        if (!"json".equals(header)) {
            throw new NotAcceptableTypeException("Only Application:application/json header is allowed! (The only way to get response is 'json' in dataFormat parameter)");
        }
        headers.add("Application", "application/" + header);
        headers.add("User-Agent", "kaelg");
        HttpEntity httpEntity = new HttpEntity(headers);
        String responses;
        try {
            responses = restTemplate.getForObject("https://api.github.com/search/repositories?q=fork:false+user:" + username,
                    String.class, httpEntity);
        } catch (HttpClientErrorException e) {
            throw new GitHubUserNotFoundException("Repositories for user with login: " + username + " cannot be searched either because the resources do not exist or you do not have permission to view them.");
        }
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = null;
        try {
            rootNode = objectMapper.readTree(responses);
        } catch (JsonProcessingException e) {
            throw new GitHubUserNotFoundException("UserNotFound");
        }
        List<Response> responsesList = new ArrayList<>();
        List<BranchesInfo> branchesInfos = new ArrayList<>();
        JsonNode itemsNode = rootNode.get("items");

        for (JsonNode repositoriesNode : itemsNode) {
            branchesInfos = new ArrayList<>();
            JsonNode repositoryNameNode = repositoriesNode.get("name");
            String branchUrl = repositoriesNode.get("branches_url").asText().replace("{/branch}", "");
            JsonNode ownerNode = repositoriesNode.path("owner");
            JsonNode ownerLogin = ownerNode.get("login");
            final String branchResponses = restTemplate.getForObject(branchUrl, String.class, httpEntity);
            JsonNode branchRootNode = null;

            branchRootNode = objectMapper.readTree(branchResponses);


            for (JsonNode branchesNode : branchRootNode) {
                JsonNode branchShaNode = branchesNode.get("commit").get("sha");
                JsonNode branchNameNode = branchesNode.get("name");
                branchesInfos.add(BranchesInfo.builder()
                        .branchName(branchNameNode.asText())
                        .branchSha(branchShaNode.asText())
                        .build());
            }
            responsesList.add(Response.builder()
                    .repositroyName(repositoryNameNode.asText())
                    .ownerLogin(ownerLogin.asText())
                    .branchesInfo(branchesInfos)
                    .build());
        }
        return responsesList;
    }
}