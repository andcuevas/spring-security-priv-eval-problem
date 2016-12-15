package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.WebInvocationPrivilegeEvaluator;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class WebController {

    @Autowired
    private WebInvocationPrivilegeEvaluator webPrivEvaluator;

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "/bug/greetings", method = RequestMethod.GET)
    public String bugGreetings() {
        return "Hi, you are testing bugGreetings.";
    }

    @RequestMapping(value = "/ok/greetings", method = RequestMethod.GET)
    public String okGreetings() {
        return "Hi, you are testing okGreetings.";
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public List<LinkAccess> getAccessList(Authentication authentication) {

        List<LinkAccess> linkAccessList = new ArrayList<LinkAccess>(2);

        LinkAccess okLinkAccess = new LinkAccess();
        okLinkAccess.setUrl("/ok/greetings");
        okLinkAccess.setVerb("GET");
        okLinkAccess.setAccess(webPrivEvaluator.isAllowed(null, okLinkAccess.getUrl(), okLinkAccess.getVerb(), authentication));

        linkAccessList.add(okLinkAccess);

        LinkAccess bugLinkAccess = new LinkAccess();
        bugLinkAccess.setUrl("/bug/greetings");
        bugLinkAccess.setVerb("GET");
        bugLinkAccess.setAccess(webPrivEvaluator.isAllowed(null, bugLinkAccess.getUrl(), bugLinkAccess.getVerb(), authentication));

        linkAccessList.add(bugLinkAccess);

        return linkAccessList;
    }

}