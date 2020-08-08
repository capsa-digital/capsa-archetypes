Feature: Direct Mail

  Scenario Outline: Send email
    When sending http request from file /requests/authorize.json using the following transformations:
      | path              | value                             |
      | $.schema          | http                              |
      | $.host            | <host>                            |
      | $.body.username   | michael.lifschitz+admin@gmail.com |
      | $.body.password   | admin                             |
      | $.body.rememberMe | {false}                           |
    And verifying status code is 200
    And saving response parts to context memento
      | path    | variable |
      | $.token | jwtToken |
    And sending http request from file /requests/send-direct-email.json using the following transformations:
      | path           | value      |
      | $.schema       | http       |
      | $.host         | <host>     |
      | $.body.to      | <to>       |
      | $.body.from    | <fromType> |
      | $.body.subject | <subject>  |
      | $.body.content | <content>  |
    Then verifying status code is 200
    And sleep <waitTime> ms
    And verifying email, imapHost <imapHost>, imapPort <imapPort>, to <to>, from <from>, subject <subject>, content <content>

    @local @local1
    Examples:
      | host      | imapHost  | imapPort | to            | fromType | from                         | subject      | content      | isMultipart | isHtml | waitTime |
      | localhost | localhost | 3143     | foo@localhost | noreply  | noreply@metrofoxsecurity.com | subgect test | content test | false       | false  | 100      |

    @dev
    Examples:
      | host                         | imapHost                          | imapPort | to            | fromType | from                         | subject      | content      | isMultipart | isHtml | waitTime |
      | api-dev.metrofoxsecurity.com | test-doubles.metrofoxsecurity.com | 3143     | foo@localhost | noreply  | noreply@metrofoxsecurity.com | subgect test | content test | false       | false  | 2000     |

  Scenario Outline: Send email to Misha
    When sending http request from file /requests/send-direct-email.json using the following transformations:
      | path           | value      |
      | $.schema       | http       |
      | $.host         | <host>     |
      | $.port         | {<port>}   |
      | $.body.to      | <to>       |
      | $.body.from    | <fromType> |
      | $.body.subject | <subject>  |
      | $.body.content | <content>  |

    @qa
    Examples:
      | host                        | port | to                                            | fromType | subject       | content |
      | api-qa.metrofoxsecurity.com | 8080 | michael.lifschitz+send-direct-email@gmail.com | noreply  | auxilyo build | test OK |

    @prod
    Examples:
      | host                     | port | to                                            | fromType | subject       | content |
      | api.metrofoxsecurity.com | 80   | michael.lifschitz+send-direct-email@gmail.com | noreply  | auxilyo build | test OK |
