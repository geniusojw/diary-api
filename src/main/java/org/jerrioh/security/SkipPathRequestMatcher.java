package org.jerrioh.security;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

public class SkipPathRequestMatcher implements RequestMatcher {  
	private RequestMatcher processingMatcher;
    private OrRequestMatcher skipMatchers;

    public SkipPathRequestMatcher(String processingPath, List<String> pathsToSkip) {
        processingMatcher = new AntPathRequestMatcher(processingPath);
        skipMatchers = new OrRequestMatcher(pathsToSkip.stream().map(AntPathRequestMatcher::new).collect(Collectors.toList()));
    }

    @Override
    public boolean matches(HttpServletRequest request) {
        return processingMatcher.matches(request) && !skipMatchers.matches(request);
    }
}