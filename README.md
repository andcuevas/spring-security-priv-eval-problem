# Spring Security Web Invocation Privilege Evaluator Problem Example

This is a small example of a problem I found when using Spring Security WebInvocationPrivilegeEvaluator.

For some reason the evaluator is not using the rules defined with @PreAuthorize annotation.

## How to run
Run the Spring boot application using the class com.example.Application.

To test with the admin user, in the command line execute:

    curl -v http://localhost:8888 -u admin:password

You will see the following result:

    HTTP/1.1 200
    ...
    [
      {
        "url":"/ok/greetings",
        "verb":"GET",
        "access":true
      },
      {
        "url":"/bug/greetings",
        "verb":"GET",
        "access":true
      }
    ]

To test with the noaccess user, in the command line execute:

    curl -v http://localhost:8888 -u noaccess:password

You will see the following result:

    HTTP/1.1 200
    ...
    [
      {
        "url":"/ok/greetings",
        "verb":"GET",
        "access":false
      },
      {
        "url":"/bug/greetings",
        "verb":"GET",
        "access":true
      }
    ]

According to the result the noaccess user has access to /bug/greetings, but if you test this:

    curl -v http://localhost:8888/bug/greetings -u noaccess:password

You will see a forbidden result:

    HTTP/1.1 403
    ...
    {
      "timestamp":1481821624117,
      "status":403,
      "error":"Forbidden",
      "exception":"org.springframework.security.access.AccessDeniedException",
      "message":"Access is denied",
      "path":"/bug/greetings"
    }

The noaccess user has the role NO_ACCESS assigned and /bug/greetings has the restriction @PreAuthorize("hasRole('ADMIN')").

The WebInvocationPrivilegeEvaluator class see the rules that are defined in the WebSecurityConfigurerAdapter but is not using the rules defined with the @PreAuthorize annotation.