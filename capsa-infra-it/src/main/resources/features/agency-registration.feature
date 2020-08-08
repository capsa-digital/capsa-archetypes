Feature: Agency Registration

  Scenario Outline: Call createAgency - <description>
    When store random userEmail in context memento
    And sending http request from file /requests/create-account.json using the following transformations:
      | path              | value              |
      | $.schema          | http               |
      | $.host            | <host>             |
      | $.body.email      | [[userEmail]]      |
      | $.body.password   | Password1          |
      | $.body.agencyName | Best Guards Agency |
    And verifying status code is 200

    And sleep <waitTime> ms

    And sending http request from file /requests/authorize.json using the following transformations:
      | path            | value         |
      | $.schema        | http          |
      | $.host          | <host>        |
      | $.body.username | [[userEmail]] |
      | $.body.password | Password1     |
    And verifying status code is 200
    And saving response parts to context memento
      | path    | variable |
      | $.token | jwtToken |

    And sending http request from file /requests/agency-info.json using the following transformations:
      | path     | value                           |
      | $.schema | http                            |
      | $.host   | <host>                          |
      | $.path   | /api/getAgencyInfo/[[agencyId]] |
    And verifying status code is 200
    And verifying response body paths
      | path         | value              |
      | $.agencyName | Best Guards Agency |

    @local
    Examples:
      | description       | host      | waitTime |
      | positive scenario | localhost | 100      |

    @dev
    Examples:
      | description       | host                         | waitTime |
      | positive scenario | api-dev.metrofoxsecurity.com | 2000     |


  Scenario Outline: Activate AppUser - <description>
    When store random userEmail in context memento
    And marking emails as seen, imapHost <imapHost>, imapPort <imapPort>, to [[userEmail]]
    And sending http request from file /requests/create-account.json using the following transformations:
      | path            | value         |
      | $.schema        | http          |
      | $.host          | <host>        |
      | $.body.email    | [[userEmail]] |
      | $.body.password | Password1     |
    And verifying status code is 200
    And sleep <waitTime> ms
    And verifying email, imapHost <imapHost>, imapPort <imapPort>, to [[userEmail]], from noreply@metrofoxsecurity.com, subject Metrofox account activation, content Metrofox account activation
    And sending http request from file /requests/activate-app-user.json using the following transformations:
      | path       | value                  |
      | $.schema   | http                   |
      | $.host     | <host>                 |
      | $.body.key | [[activateAppUserKey]] |
    And verifying status code is 200

    And sleep <waitTime> ms

    Then sending http request from file /requests/authorize.json using the following transformations:
      | path              | value         |
      | $.schema          | http          |
      | $.host            | <host>        |
      | $.body.username   | [[userEmail]] |
      | $.body.password   | Password1     |
      | $.body.rememberMe | {false}       |
    And verifying status code is 200
    And verifying response body matches [^.\s]*.[^.\s]*.[^.\s]*

    Then sending http request from file /requests/authorize.json using the following transformations:
      | path              | value         |
      | $.schema          | http          |
      | $.host            | <host>        |
      | $.body.username   | [[userEmail]] |
      | $.body.password   | Password1     |
      | $.body.rememberMe | {false}       |
    And verifying status code is 200
    And verifying response body matches [^.\s]*.[^.\s]*.[^.\s]*

    @local
    Examples:
      | description       | host      | imapHost  | imapPort | waitTime |
      | positive scenario | localhost | localhost | 3143     | 100      |

    @dev
    Examples:
      | description       | host                         | imapHost                          | imapPort | waitTime |
      | positive scenario | api-dev.metrofoxsecurity.com | test-doubles.metrofoxsecurity.com | 3143     | 2000     |





