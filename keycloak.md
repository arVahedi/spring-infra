### KeyCloak configuration:

- Create a new realm regarding our domain(s)
- Create corresponding clients regarding our microservice(s) and make sure `Client authentication` in the client setting is on and all `authentication flows` are activated.
- Make sure that all clients have `openid` scope, otherwise add it to their scope or if the scope named `openid` doesn't exist itself, create it and then add it to all clients. Also make sure the option `Include in token scope` of `openid` scope is **on**.
- Create corresponding users (end-user who wants to sign in to your application)
- As an additional and optional step, we can also create different groups for our users due to be able to manage their permission group-wise easily 
- Create roles and permissions: 
  - Since the concept of role is like what we know as permission and the Keycloak doesn't support permission grouping (like what we usually do when grouping multiple different permissions as a role), we have to define our permissions as role **with a `_AUTHORITY` postfix** (see springinfra.assets.Constant#AUTHORITY_POSTFIX) and then for grouping them as a role, we should use a composite Keycloak role that actually is group of other roles (our permissions). As a result we would have all permissions plus the composite role itself in our ID token.
  - After creating roles and permission, we can assign them to clients, users, or user groups (if we've created them)
