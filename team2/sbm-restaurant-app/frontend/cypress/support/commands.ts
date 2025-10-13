// cypress/support/commands.ts
// Custom Cypress commands

/// <reference types="cypress" />

// Example custom command
Cypress.Commands.add('login', (username: string, password: string) => {
  // Add login logic here
  cy.log(`Logging in with ${username}`)
})

// Extend the Cypress namespace
declare global {
  namespace Cypress {
    interface Chainable {
      login(username: string, password: string): Chainable<Element>
    }
  }
}

export {}