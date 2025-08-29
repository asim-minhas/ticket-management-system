Feature: Auth basics

  Background:
    * url API_ROOT

  Scenario: Login returns JWT
    Given path 'public', 'signin'
    And request {"email":"platform-admin@yourcorp.io","password":"StartupP@ss!"}
    When method post
    Then status 200
    And match response == { jwt: '#string', username: '#string', role: '#string' }
