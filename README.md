# Account Service

JetBrains Academy. Project: Anti-Fraud System.

</br><b>In first stage</b>, created SpringBoot web application for our service and 
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

<b>In second stage</b>, added the Spring security to project and configured</br>
the HTTP basic authentication;

For storing users and passwords, added a JDBC implementation of</br>
UserDetailsService with an H2 database;

In third stage implemented the following password checks when registering a</br>
user or changing a password:</br>
Passwords contain at least 12 characters;</br>
Check the submitted passwords against the set of breached passwords.</br>

Used BCryptPasswordEncoder with a strength of  13 to store the passwords in the database.

</br><b>In third stage</b> created 3 endpoints:

POST api/acct/payments uploads payrolls;</br>
PUT api/acct/payments changes the salary of a specific user;</br>
GET api/empl/payment gives access to the payroll of an employee.</br>

Information about the salary of employees is transmitted as an array of JSON objects. This</br>
operation must be transactional. That is, if an error occurs during an update, perform a rollback</br>
to the original state. The following requirements are imposed on the data:

An employee must be among the users of our service;</br>
The period for which the salary is paid must be unique for each employee (for POST),</br>
Salary is calculated in cents and cannot be negative.</br>

Changing the salary must be done in a separate corrective operation, PUT. The previous data</br>
requirements remain, except for the uniqueness requirement. In this stage, we are not concerned</br>
with an employee-period pair.

</br><b>In fourth stage</b> created 3 endpoints:

POST api/acct/payments uploads payrolls;</br>
PUT api/acct/payments changes the salary of a specific user;</br>
GET api/empl/payment gives access to the payroll of an employee.</br>

Information about the salary of employees is transmitted as an array of JSON objects. This</br>
operation must be transactional. That is, if an error occurs during an update, perform a rollback</br>
to the original state. The following requirements are imposed on the data:

An employee must be among the users of our service;</br>
The period for which the salary is paid must be unique for each employee (for POST),</br>
Salary is calculated in cents and cannot be negative.</br>

Changing the salary must be done in a separate corrective operation, PUT. The previous data</br>
requirements remain, except for the uniqueness requirement. In this stage, we are not concerned</br>
with an employee-period pair.

<b>In fifth stage</b> added authorization. Implement the role model that developed earlier:

<table>
    <tbody>
    <tr>
        <td> </td>
        <td>Anonymous</td>
        <td>User</td>
        <td>Accountant</td>
        <td>Administrator</td>
    </tr>
    <tr>
        <td><code class="language-java">POST api/auth/signup</code></td>
        <td>+</td>
        <td>+</td>
        <td>+</td>
        <td>+</td>
    </tr>
    <tr>
        <td><code class="language-java">POST api/auth/changepass</code></td>
        <td> </td>
        <td>+</td>
        <td>+</td>
        <td>+</td>
    </tr>
    <tr>
        <td><code class="language-java">GET api/empl/payment</code></td>
        <td>-</td>
        <td>+</td>
        <td>+</td>
        <td>-</td>
    </tr>
    <tr>
        <td><code class="language-java">POST api/acct/payments</code></td>
        <td>-</td>
        <td>-</td>
        <td>+</td>
        <td>-</td>
    </tr>
    <tr>
        <td><code class="language-java">PUT api/acct/payments</code></td>
        <td>-</td>
        <td>-</td>
        <td>+</td>
        <td>-</td>
    </tr>
    <tr>
        <td><code class="language-java">GET api/admin/user</code></td>
        <td>-</td>
        <td>-</td>
        <td>-</td>
        <td>+</td>
    </tr>
    <tr>
        <td><code class="language-java">DELETE api/admin/user</code></td>
        <td>-</td>
        <td>-</td>
        <td>-</td>
        <td>+</td>
    </tr>
    <tr>
        <td><code class="language-java">PUT api/admin/user/role</code></td>
        <td>-</td>
        <td>-</td>
        <td>-</td>
        <td>+</td>
    </tr>
    </tbody>
</table>

</br><b>In sixth stage</b> added information security events:

<table>
<tbody>
<tr>
<td>Description</td>
<td>Event Name</td>
</tr>
<tr>
<td>A user has been successfully registered</td>
<td><code class="language-json">CREATE_USER</code></td>
</tr>
<tr>
<td>A user has changed the password successfully</td>
<td><code class="language-json">CHANGE_PASSWORD</code></td>
</tr>
<tr>
<td>A user is trying to access a resource without access rights</td>
<td><code class="language-json">ACCESS_DENIED</code></td>
</tr>
<tr>
<td>Failed authentication</td>
<td><code class="language-json">LOGIN_FAILED</code></td>
</tr>
<tr>
<td>A role is granted to a user</td>
<td><code class="language-json">GRANT_ROLE</code></td>
</tr>
<tr>
<td>A role has been revoked</td>
<td><code class="language-json">REMOVE_ROLE</code></td>
</tr>
<tr>
<td>The Administrator has locked the user</td>
<td><code class="language-json">LOCK_USER</code></td>
</tr>
<tr>
<td>The Administrator has unlocked a user</td>
<td><code class="language-json">UNLOCK_USER</code></td>
</tr>
<tr>
<td>The Administrator has deleted a user</td>
<td><code class="language-json">DELETE_USER</code></td>
</tr>
<tr>
<td>A user has been blocked on suspicion of a brute force attack</td>
<td><code class="language-json">BRUTE_FORCE</code></td>
</tr>
</tbody>
</table>

Also implemented a simple rule for detecting a brute force attack. If there are more than 5</br>
consecutive attempts to enter an incorrect password, an entry about this should appear in the</br>
security events. Also, the user account must be blocked.

Added the PUT api/admin/user/access endpoint that locks/unlocks users. It accepts the following</br>
JSON body:

{</br>
"user": "<String value, not empty>",</br>
"operation": "<[LOCK, UNLOCK]>"</br>
}
