// cypress/e2e/homepage.cy.ts
describe('Homepage', () => {
  it('should display the homepage', () => {
    cy.visit('/')
    cy.contains('Get started by editing')
  })

  it('should have the correct title', () => {
    cy.visit('/')
    cy.title().should('include', 'Create Next App')
  })
})