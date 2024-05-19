context('register User', () => {

  it('register valid User', () => {
    cy.registerUser();
  })

  it('register duplicate Email', () => {
    cy.registerDuplicateEmail();
  })

  it('register duplicate Username', () => {
    cy.registerDuplicateUser();
  })

});
