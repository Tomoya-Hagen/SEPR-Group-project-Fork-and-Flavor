# How to setup your environment for Deployment or Development
## Deployment
Open the file: `/backend/src/main/resources/application.yml`
In Line `27` and `41` is the config for the application:
```yml
spring:

  [...]

  mail:
    host: "your.smtp.server"
    port: 465
    username: "smtp username"
    password: "smtp password"
    properties:
      mail:
        smtp:
          auth: true
          starttls:
            enable: false
          ssl:
            enable: true

mail:
  from-address: "no-reply.fromAdress@yourdomain.com"
  frontend-url: "https://your.application-frontend.com"
  admin-email: "your.admin.mail@email.com"
```

## Development
Open the file: `/backend/src/main/resources/application.yml`
In Line `41` is the config for the application:
```yml
mail:
  from-address: "no-reply.fromAdress@yourdomain.com"
  frontend-url: "https://your.application-frontend.com"
#  admin-email: "your.admin.mail@email.com"
```
If the line `admin-email` is commented out the testusers are generated. Have a look at `/backend/src/main/java/at/ac/tuwien/sepr/groupphase/backend/datagenerator/DataGenerator.java` for more information.

