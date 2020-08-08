Feature: AppUser Management - AM

  Scenario Outline: Call createAgency - <description>
    When store random userEmail in context memento
    And marking emails as seen, imapHost <imapHost>, imapPort <imapPort>, to [[userEmail]]

    Then sending http request from file /requests/authorize.json using the following transformations:
      | path            | value     |
      | $.schema        | http      |
      | $.host          | <host>    |
      | $.body.username | admin@en  |
      | $.body.password | Password1 |
    And verifying status code is 200
    And saving response parts to context memento
      | path    | variable |
      | $.token | jwtToken |

    Then sending http request from file /requests/inflate-account-topology.json using the following transformations:
      | path     | value  |
      | $.schema | http   |
      | $.host   | <host> |
    And sending http request from file /requests/create-customer.json using the following transformations:
      | path             | value         |
      | $.schema         | http          |
      | $.host           | <host>        |
      | $.body.agencyId  | [[agencyId]]  |
      | $.body.accountId | [[accountId]] |
      | $.body.email     | [[userEmail]] |
    And verifying status code is 200

    Then sleep <waitTime> ms

    Then sending http request from file /requests/authorize.json using the following transformations:
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

    Then sending http request from file /requests/comm-account-info.json using the following transformations:
      | path     | value                              |
      | $.schema | http                               |
      | $.host   | <host>                             |
      | $.path   | /api/getCommAccountInfo/[[userId]] |
    And verifying status code is 200
    And verifying response body paths
      | path     | op    | value                                                                           |
      | $.userId | regex | [0-9a-fA-F]{8}\-[0-9a-fA-F]{4}\-[0-9a-fA-F]{4}\-[0-9a-fA-F]{4}\-[0-9a-fA-F]{12} |
      | $.email  | regex | (.+)@(.+)                                                                       |
      | $.phone  |       | {null}                                                                          |

    Then sleep <waitTime> ms

    Then verifying email, imapHost <imapHost>, imapPort <imapPort>, to [[userEmail]], from noreply@metrofoxsecurity.com, subject Metrofox account activation, content Metrofox account activation

    @local
    Examples:
      | description               | host      | imapHost  | imapPort | waitTime |
      | (AM101) positive scenario | localhost | localhost | 3143     | 100      |

    @dev
    Examples:
      | description               | host                         | imapHost                          | imapPort | waitTime |
      | (AM101) positive scenario | api-dev.metrofoxsecurity.com | test-doubles.metrofoxsecurity.com | 3143     | 2000     |


  Scenario Outline: Change Customer password - <description>
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
    And verifying status code is 200

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

    When sleep <waitTime> ms
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
    And sending http request from file /requests/change-password.json using the following transformations:
      | path                   | value        |
      | $.schema               | http         |
      | $.host                 | <host>       |
      | $.body.currentPassword | Password1    |
      | $.body.newPassword     | 123456qwerty |
    Then verifying status code is 200

    And sleep <waitTime> ms

    And sending http request from file /requests/authorize.json using the following transformations:
      | path              | value         |
      | $.schema          | http          |
      | $.host            | <host>        |
      | $.body.username   | [[userEmail]] |
      | $.body.password   | 123456qwerty  |
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

    @local
    Examples:
      | description               | host      | imapHost  | imapPort | waitTime |
      | (AM201) positive scenario | localhost | localhost | 3143     | 100      |

    @dev
    Examples:
      | description               | host                         | imapHost                          | imapPort | waitTime |
      | (AM201) positive scenario | api-dev.metrofoxsecurity.com | test-doubles.metrofoxsecurity.com | 3143     | 2000     |


  Scenario Outline: Reset Customer password - <description>
    When store random userEmail in context memento
    And marking emails as seen, imapHost <imapHost>, imapPort <imapPort>, to [[userEmail]]
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
    And verifying status code is 200
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

    When sleep <waitTime> ms
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

    And sending http request from file /requests/init-password-reset.json using the following transformations:
      | path         | value         |
      | $.schema     | http          |
      | $.host       | <host>        |
      | $.body.email | [[userEmail]] |
    Then verifying status code is 200
    And sleep <waitTime> ms
    And verifying email, imapHost <imapHost>, imapPort <imapPort>, to [[userEmail]], from noreply@metrofoxsecurity.com, subject Metrofox password reset, content For your Metrofox account a password reset was requested

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
    And sending http request from file /requests/complete-password-reset.json using the following transformations:
      | path            | value                |
      | $.schema        | http                 |
      | $.host          | <host>               |
      | $.body.key      | [[passwordResetKey]] |
      | $.body.password | 123456qwerty         |
    Then verifying status code is 200
    When sleep <waitTime> ms
    And sending http request from file /requests/authorize.json using the following transformations:
      | path              | value         |
      | $.schema          | http          |
      | $.host            | <host>        |
      | $.body.username   | [[userEmail]] |
      | $.body.password   | 123456qwerty  |
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

    @local
    Examples:
      | description               | endpoint              | host      | imapHost  | imapPort | waitTime |
      | (AM301) positive scenario | http://localhost:8080 | localhost | localhost | 3143     | 100      |

    @dev
    Examples:
      | description               | endpoint                                 | host                         | imapHost                          | imapPort | waitTime |
      | (AM301) positive scenario | http://api-dev.metrofoxsecurity.com:8080 | api-dev.metrofoxsecurity.com | test-doubles.metrofoxsecurity.com | 3143     | 2000     |






