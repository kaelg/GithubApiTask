package com.porebski.GithubApiTask.Model;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class BranchesInfo {
    private String branchName;
    private String branchSha;
}
