CybersecurityBase Project Report
================================

**by khampf**

Based on the NetBeans [project starter code provided on Github](https://github.com/cybersecuritybase/cybersecuritybase-project)

Installation guidelines
-----------------------
Import project into netbeans and the application will be run on [localhost:8080](http://localhost:8080)

The default account usernames/passwords are:

* ted/ted (with role USER, able to read signup list)
* president/president (with role USER and EDIT, able to view and delete signups)
* admin/admin (with roles USER, EDIT and ADMIN able to do all of the above but can also view theese user accounts)
* disabled/disabled (with role USER but account set to disabled)

Flaws
=====

Issue 1: SQL Injection
--------------------
#### OWASP A1-Injection
##### Steps to reproduce:
1. Visit [sign up page](http://localhost:8080)
2. Enter SQL below in name input field:
  1. `E','F'); UPDATE USER SET enabled=true WHERE id=4; --`
3. Submit the form
4. The SQL is injected in the backend database and user account "disabled" is now enabled

##### Another example:
1. Visit [sign up page](http://localhost:8080)
2. Enter SQL below in name input field:
  1. `E','F'); INSERT INTO USER_ROLE (user_id,role_id) VALUES (2,2); --`
3. Submit the form
4. The SQL is injected in the backend database and the user "ted" gets ADMIN role added

##### Steps to fix
1. Open src/main/java/sec/project/controller/DefaultController.java
2. Comment out the unsafe code marked by "Issue 1"
3. Enable the code below it marked as safer by using parameterized queries or the code evern further below using Repository mechanisms instead of native queries

Issue 2: Session Cookie
-----------------------
#### OWASP
##### Steps to reproduce:
1. `<script>alert(document.cookie);</script>`



