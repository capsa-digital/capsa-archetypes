Feature: Customer Registration

  Scenario Outline: Call createCustomer - <description>
    And sending http request from file /requests/authorize.json using the following transformations:
      | path            | value     |
      | $.schema        | http      |
      | $.host          | <host>    |
      | $.body.username | admin@en  |
      | $.body.password | Password1 |
    And verifying status code is 200
    And saving response parts to context memento
      | path    | variable |
      | $.token | jwtToken |
    And sending http request from file /requests/inflate-account-topology.json using the following transformations:
      | path     | value  |
      | $.schema | http   |
      | $.host   | <host> |

    When store random userEmail in context memento
    And marking emails as seen, imapHost <imapHost>, imapPort <imapPort>, to [[userEmail]]

    When sending http request from file /requests/create-customer.json using the following transformations:
      | path             | value         |
      | $.schema         | http          |
      | $.host           | <host>        |
      | $.body.agencyId  | [[agencyId]]  |
      | $.body.accountId | [[accountId]] |
      | $.body.email     | [[userEmail]] |
    Then verifying status code is 200
    And sleep <waitTime> ms
    And verifying email, imapHost <imapHost>, imapPort <imapPort>, to [[userEmail]], from noreply@metrofoxsecurity.com, subject Metrofox account activation, content Metrofox account activation
    And sending http request from file /requests/activate-app-user.json using the following transformations:
      | path       | value                  |
      | $.schema   | http                   |
      | $.host     | <host>                 |
      | $.body.key | [[activateAppUserKey]] |
    Then verifying status code is 200

    When sleep <waitTime> ms
    And sending http request from file /requests/authorize.json using the following transformations:
      | path              | value         |
      | $.schema          | http          |
      | $.host            | <host>        |
      | $.body.username   | [[userEmail]] |
      | $.body.password   | Password1     |
      | $.body.rememberMe | {false}       |
    And verifying status code is 200
    And saving response parts to context memento
      | path    | variable |
      | $.token | jwtToken |
    And sending http request from file /requests/comm-account-info.json using the following transformations:
      | path     | value                              |
      | $.schema | http                               |
      | $.host   | <host>                             |
      | $.path   | /api/getCommAccountInfo/[[userId]] |
    Then verifying status code is 200
    And verifying response body paths
      | path     | op    | value                                                                           |
      | $.userId | regex | [0-9a-fA-F]{8}\-[0-9a-fA-F]{4}\-[0-9a-fA-F]{4}\-[0-9a-fA-F]{4}\-[0-9a-fA-F]{12} |
      | $.email  | regex | (.+)@(.+)                                                                       |
      | $.phone  |       | {null}                                                                          |
    When sending http request from file /requests/create-customer.json using the following transformations:
      | path             | value         |
      | $.schema         | http          |
      | $.host           | <host>        |
      | $.body.agencyId  | [[agencyId]]  |
      | $.body.accountId | [[accountId]] |
      | $.body.email     | [[userEmail]] |
    Then verifying status code is 400
    And verifying response body paths
      | path      | op    | value                   |
      | $.status  |       | {400}                   |
      | $.error   |       | Email is not unique     |
      | $.message | regex | Email .* is not unique. |

    @local
    Examples:
      | description       | host      | imapHost  | imapPort | waitTime |
      | positive scenario | localhost | localhost | 3143     | 100      |

    @dev
    Examples:
      | description       | host                         | imapHost                          | imapPort | waitTime |
      | positive scenario | api-dev.metrofoxsecurity.com | test-doubles.metrofoxsecurity.com | 3143     | 2000     |


