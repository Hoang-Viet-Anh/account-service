#Account Service

JetBrains Academy. Project: Anti-Fraud System.

</br>In first stage, created SpringBoot web application for our service and 
test it with one endpoint.

Created SpringBoot application on the 28852 port;

Created the POST api/auth/signup endpoint that accepts data in the JSON format:

{</br>
"name": "<String value, not empty>",</br>
"lastname": "<String value, not empty>",</br>
"email": "<String value, not empty>",</br>
"password": "<String value, not empty>"</br>
}</br>

It should return a response in the JSON format (without the password field):

{</br>
"name": "<String value>",</br>
"lastname": "<String value>",</br>
"email": "<String value>"</br>
}

If the status is HTTP OK (200), then all fields are correct.</br>
If it's HTTP Bad Request (400), then something is wrong.</br>
Our service accept only corporate emails that end with @acme.com.</br>
In this stage, we do not check the authentication, so the password</br>
field may contain anything (but not empty).

In second stage, added the Spring security to project and configured</br>
the HTTP basic authentication;

For storing users and passwords, added a JDBC implementation of</br>
UserDetailsService with an H2 database;


