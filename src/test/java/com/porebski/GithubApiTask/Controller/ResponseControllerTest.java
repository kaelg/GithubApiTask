package com.porebski.GithubApiTask.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.porebski.GithubApiTask.Model.BranchesInfo;
import com.porebski.GithubApiTask.Model.Response;
import com.porebski.GithubApiTask.Service.ResponseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@WebMvcTest(ResponseController.class)
class ResponseControllerTest {
    private static final String TEST_REPOSITORY_NAME = "test repository name";
    private static final String TEST_OWNER_LOGIN = "test owner login";
    private static final String TEST_BRANCH_NAME = "test branch name";
    private static final String TEST_BRANCH_SHA = "test branch sha";
    private static final String TEST_USERNAME = "username";
    private static final BranchesInfo TEST_BRANCHES_INFO = BranchesInfo.builder()
            .branchName(TEST_BRANCH_NAME)
            .branchSha(TEST_BRANCH_SHA)
            .build();
    private static final ObjectMapper mapper = new ObjectMapper();
    @MockBean
    private ResponseService responseService;
    @Autowired
    private MockMvc mockMvc;

    private List<BranchesInfo> testBranchesInfoList = new ArrayList<>();
    private List<Response> testResponsesList = new ArrayList<>();

    @BeforeEach
    void setup() {
        testBranchesInfoList = new ArrayList<>();
        testResponsesList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            testBranchesInfoList.add(TEST_BRANCHES_INFO);
        }
        Response response = Response.builder()
                .repositroyName(TEST_REPOSITORY_NAME)
                .ownerLogin(TEST_OWNER_LOGIN)
                .branchesInfo(testBranchesInfoList)
                .build();
        for (int i = 0; i < 5; i++) {
            testResponsesList.add(response);
        }
    }

    @Test
    void getResponse() throws Exception {
        //given
        when(responseService.getResponse(anyString(), anyString())).thenReturn(testResponsesList);
        ///when
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/response/{USERNAME}", TEST_USERNAME)
                        .param("dataFormat", "json")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsBytes(testResponsesList)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        //then
        String response = mvcResult.getResponse().getContentAsString();
        assertEquals(response, mapper.writeValueAsString(testResponsesList));
    }
}
