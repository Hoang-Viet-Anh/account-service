# Account Service

JetBrains Academy. Project: Anti-Fraud System.

</br>In third stage created 3 endpoints:

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


