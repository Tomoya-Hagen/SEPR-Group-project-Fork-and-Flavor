Cypress.Commands.add('loginAdmin', () => {
    cy.fixture('settings').then(settings => {
        cy.visit(settings.baseUrl);
        cy.contains('a', 'Login').click();
        cy.get('input[name="username"]').type(settings.adminUser);
        cy.get('input[name="password"]').type(settings.adminPw);
        cy.contains('button', 'Login').click();
    })
})

Cypress.Commands.add('registerUser', () => {
    cy.fixture('settings').then(settings => {
        cy.visit(settings.baseUrl);
        cy.contains('a', 'Login').click();
        cy.contains('button', 'Register').click();
        cy.get('input[id="username"]').type('user' + new Date().getTime());
        cy.get('input[id="email"]').type('email' + new Date().getTime() + '@mail.com');
        cy.get('input[id="password"]').type('password');
        cy.get('input[id="repeatPassword"]').type('password');
        cy.contains('button', 'Register').click();
        cy.contains('a', 'Logout').should('be.visible').click();
    })
})

Cypress.Commands.add('registerDuplicateEmail', () => {
    cy.fixture('settings').then(settings => {
        cy.visit(settings.baseUrl);
        cy.contains('a', 'Login').click();
        cy.contains('button', 'Register').click();
        cy.get('input[id="username"]').type('user' + new Date().getTime());
        cy.get('input[id="email"]').type('admin@email.com');
        cy.get('input[id="password"]').type('password');
        cy.get('input[id="repeatPassword"]').type('password');
        cy.contains('button', 'Register').click();
        cy.contains('div', 'Email already exists').should('be.visible');
    })
})

Cypress.Commands.add('registerDuplicateUser', () => {
    cy.fixture('settings').then(settings => {
        cy.visit(settings.baseUrl);
        cy.contains('a', 'Login').click();
        cy.contains('button', 'Register').click();
        cy.get('input[id="username"]').type('admin');
        cy.get('input[id="email"]').type('email' + new Date().getTime() + '@mail.com');
        cy.get('input[id="password"]').type('password');
        cy.get('input[id="repeatPassword"]').type('password');
        cy.contains('button', 'Register').click();
        cy.contains('div', 'Username already exists').should('be.visible');
    })
})