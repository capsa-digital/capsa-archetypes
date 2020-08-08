Feature: All in One Smoke Test

  Scenario Outline: Agency basic setting - <description>
    #============= Create Account =============#
    When store random userEmail in context memento
    And sending http request from file /requests/create-account.json using the following transformations:
      | path              | value              |
      | $.schema          | http               |
      | $.host            | <host>             |
      | $.body.email      | [[userEmail]]      |
      | $.body.password   | Password1          |
      | $.body.agencyName | Best Guards Agency |
    And verifying status code is 200

    When sleep <waitTime> ms

    #============= Login as Account Owner first time =============#
    Then sending http request from file /requests/authorize.json using the following transformations:
      | path            | value         |
      | $.schema        | http          |
      | $.host          | <host>        |
      | $.body.username | [[userEmail]] |
      | $.body.password | Password1     |
    And verifying status code is 200
    And verifying response body paths
      | path    | op  | value                     |
      | $.token | jwt | auth, Owner:[[accountId]] |

    #============= Verify that activation email sent =============#
    And verifying email, imapHost <imapHost>, imapPort <imapPort>, to [[userEmail]], from noreply@metrofoxsecurity.com, subject Metrofox account activation, content Metrofox account activation

    When sleep 10000 ms

    #============= Login as Account Owner second time after 10 sec delay =============#
    Then sending http request from file /requests/authorize.json using the following transformations:
      | path            | value         |
      | $.schema        | http          |
      | $.host          | <host>        |
      | $.body.username | [[userEmail]] |
      | $.body.password | Password1     |
    And verifying status code is 200
    And verifying response body paths
      | path    | op  | value              |
      | $.token | jwt | auth, NotValidated |
    And saving response parts to context memento
      | path    | variable |
      | $.token | jwtToken |

    When sleep <waitTime> ms

    #============= Verify that validation email sent =============#
    And verifying email, imapHost <imapHost>, imapPort <imapPort>, to [[userEmail]], from noreply@metrofoxsecurity.com, subject Metrofox account validation, content Metrofox email verification code is

    #============= Validate Account Owner =============#
    Then sending http request from file /requests/validate.json using the following transformations:
      | path                  | value                   |
      | $.schema              | http                    |
      | $.host                | <host>                  |
      | $.body.validationCode | [[validateAppUserCode]] |
    And verifying status code is 200
    And verifying response body paths
      | path            | op  | value                     |
      | $.payload.token | jwt | auth, Owner:[[accountId]] |
    And saving response parts to context memento
      | path            | variable |
      | $.payload.token | jwtToken |

    #============= Get Agency Info =============#
    Then sending http request from file /requests/agency-info.json using the following transformations:
      | path     | value                           |
      | $.schema | http                            |
      | $.host   | <host>                          |
      | $.path   | /api/getAgencyInfo/[[agencyId]] |
    And verifying status code is 200
    And verifying response body paths
      | path         | value              |
      | $.agencyName | Best Guards Agency |

    #============= Get Agency Topology =============#
    Then sending http request from file /requests/inflate-account-topology.json using the following transformations:
      | path     | value  |
      | $.schema | http   |
      | $.host   | <host> |
    And verifying status code is 200
    And verifying response body paths
      | path                     | value              |
      | $.agencies[*].agencyName | Best Guards Agency |

    #============= Login as Account Owner after validation =============#
    Then sending http request from file /requests/authorize.json using the following transformations:
      | path            | value         |
      | $.schema        | http          |
      | $.host          | <host>        |
      | $.body.username | [[userEmail]] |
      | $.body.password | Password1     |
    And verifying status code is 200
    And saving response parts to context memento
      | path    | variable |
      | $.token | jwtToken |

    #============= Create Customer =============#
    When store random userEmail in context memento
    And sending http request from file /requests/create-customer.json using the following transformations:
      | path             | value         |
      | $.schema         | http          |
      | $.host           | <host>        |
      | $.body.agencyId  | [[agencyId]]  |
      | $.body.accountId | [[accountId]] |
      | $.body.email     | [[userEmail]] |
      | $.body.password  | password2     |
    And verifying status code is 200

    When sleep <waitTime> ms

    #============= Login as Customer =============#
    Then sending http request from file /requests/authorize.json using the following transformations:
      | path            | value         |
      | $.schema        | http          |
      | $.host          | <host>        |
      | $.body.username | [[userEmail]] |
      | $.body.password | password2     |
    And verifying status code is 200

    @local @smoke
    Examples:
      | description       | host      | imapHost  | imapPort | waitTime |
      | positive scenario | localhost | localhost | 3143     | 100      |

    @dev
    Examples:
      | description       | host                         | imapHost                          | imapPort | waitTime |
      | positive scenario | api-dev.metrofoxsecurity.com | test-doubles.metrofoxsecurity.com | 3143     | 2000     |


