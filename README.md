# Account Service

JetBrains Academy. Project: Anti-Fraud System.

</br>In sixth stage added information security events:

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
