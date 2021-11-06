Feature: allows creating, deleting and listing screenings
  Background: 
    Given the application is started
    And the prompt containing "Ticket service>" is printed
    And the user types the "sign in privileged admin admin" command
    And the user types the "create room Pedersoli 20 10" command
    And the user types the "create room Girotti 10 10" command
    And the user types the "create movie Sátántangó drama 450" command
    And the user types the "create movie \"Spirited Away\" animation 125" command
    And the user types the "sign out" command