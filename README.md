# Spring-Infra
Infrastructure for any project based on spring framework

### How to run it:

for the **first time** you should use `docker-compose up -d` to bring all dependencies up in a docker environment. then for 
next times this would be enough:

`docker-compose start`

After starting the project, the `flyway` would try to initialize the database automatically.  
