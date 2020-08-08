Feature: AppUser Authorization

  Scenario Outline: Call authorize - <description>
    And sending http request from file /requests/authorize.json using the following transformations:
      | path              | value        |
      | $.schema          | http         |
      | $.host            | <host>       |
      | $.body.username   | <username>   |
      | $.body.password   | <password>   |
      | $.body.rememberMe | <rememberMe> |
    And verifying status code is <statusCode>
    And verifying response body matches <response>

    @local
    Examples:
      | description             | host      | username                          | password | rememberMe | statusCode | response                |
      | email positive scenario | localhost | michael.lifschitz+admin@gmail.com | admin    | true       | 200        | [^.\s]*.[^.\s]*.[^.\s]* |
      | email positive scenario | localhost | Michael.Lifschitz+admin@gmail.com | admin    | true       | 200        | [^.\s]*.[^.\s]*.[^.\s]* |
    @dev
    Examples:
      | description       | host                         | username                          | password | rememberMe | statusCode | response                |
      | positive scenario | api-dev.metrofoxsecurity.com | michael.lifschitz+admin@gmail.com | admin    | true       | 200        | [^.\s]*.[^.\s]*.[^.\s]* |

    @qa
    Examples:
      | description       | host                        | username                          | password | rememberMe | statusCode | response                |
      | positive scenario | api-qa.metrofoxsecurity.com | michael.lifschitz+admin@gmail.com | admin    | true       | 200        | [^.\s]*.[^.\s]*.[^.\s]* |

  Scenario Outline: Call getCustomerInfo without authorize - <description>
    When store random userEmail in context memento
    When sending http request from file /requests/create-customer.json using the following transformations:
      | path             | value         |
      | $.schema         | http          |
      | $.host           | <host>        |
      | $.body.agencyId  | [[agencyId]]  |
      | $.body.accountId | [[accountId]] |
      | $.body.email     | [[userEmail]] |
    And verifying status code is 200
    And sending http request from file /requests/comm-account-info.json using the following transformations:
      | path     | value                              |
      | $.schema | http                               |
      | $.host   | <host>                             |
      | $.path   | /api/getCommAccountInfo/[[userId]] |
    And verifying status code is 403
    And verifying response body paths
      | path        | op | value         |
      | $.app.build |    | Access Denied |


    #TODO create test against preloaded customer + added logout function removing token from context
    @local2
    Examples:
      | description       | endpoint              | response      |
      | negative scenario | http://localhost:8080 | Access Denied |

   #TODO see above @dev
    Examples:
      | description       | endpoint                                 | response      |
      | negative scenario | http://api-dev.metrofoxsecurity.com:8080 | Access Denied |


