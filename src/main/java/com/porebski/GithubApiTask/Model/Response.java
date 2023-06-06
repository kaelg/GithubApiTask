package com.porebski.GithubApiTask.Model;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class Response {
    private String repositroyName;
    private String ownerLogin;
    private List<BranchesInfo> branchesInfo;
}
