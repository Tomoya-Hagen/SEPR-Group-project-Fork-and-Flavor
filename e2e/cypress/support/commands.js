Cypress.Commands.add('loginAdmin', () => {
    cy.fixture('settings').then(settings => {
        cy.visit(settings.baseUrl);
        cy.contains('a', 'Login').click();
        cy.get('input[name="username"]').type(settings.adminUser);
        cy.get('input[name="password"]').type(settings.adminPw);
        cy.contains('button', 'Login').click();
    })
})

Cypress.Commands.add('createMessage', (msg) => {
    cy.fixture('settings').then(settings => {
        cy.contains('a', 'Message');
        cy.contains('button', 'Add message').click();
        cy.get('input[name="title"]').type('title' +  msg);
        cy.get('textarea[name="summary"]').type('summary' +  msg);
        cy.get('textarea[name="text"]').type('text' +  msg);
        cy.get('button[id="add-msg"]').click();
        cy.get('button[id="close-modal-btn"]').click();

        cy.contains('title' +  msg).should('be.visible');
        cy.contains('summary' +  msg).should('be.visible');
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