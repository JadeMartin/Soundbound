Feature: Create a user profile
  As a user
  I want to be able to create a new user profile
  So that I can access the full website


  Scenario: Create valid user profile
    Given I am on the landing page
    When I press the create user button
    And I enter "James" into the "firstName" field
    And I enter "Johnston" into the "lastName" field
    And I enter "james@johnston.com" into the "email" field
    And I enter "password" into the "password" field
    And I enter "1969-05-05" into the "birthDate" field
    And I enter "Male" into the "gender" field
    And I enter "New Zealand", "Europe" into the "nationalitiesForm" field
    And I enter "Backpacker" into the "travellerTypesForm" field
    And I enter "Argentina", "Belarus" into the "passportsForm" field
    Then I save my new profile
    And My user profile is saved in the database
    And my passports are "Belarus, Argentina" or "Argentina, Belarus"
    And my nationalities are "New Zealand,Europe"
