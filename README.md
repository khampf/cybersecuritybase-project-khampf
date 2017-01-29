[CybersecurityBase Project Report](https://github.com/khampf/cybersecuritybase-project-khampf)
================================

**by khampf**

Based on the NetBeans [project starter code provided on Github](https://github.com/cybersecuritybase/cybersecuritybase-project)
Formatting of this report is in markdown but it should also be quite readable as plain text.

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

Issue #1: SQL Injection
-----------------------
**OWASP 2013 Top 10 A1,A2,(A5?),A6,A9**

A1-Injection of executable SQL leading to A2-Broken Authentication / compromised passwords, privilege escalation and account manipulation, A6-Sensitive Data exposure due to A5-Security Misconfiguration and A9-Using Components with Known Vulnerabilities.
##### Steps to reproduce:
1. Visit [sign up page](http://localhost:8080/form)
2. Enter SQL below in name input field:
  1. `',''); UPDATE USER SET enabled=true WHERE id=4; --`
3. Submit the form
4. The SQL is injected in the backend database and user account "disabled" is now enabled

##### Another example adding roles to user
1. Visit [sign up page](http://localhost:8080/form)
2. Enter SQL below in name input field:
  1. `',''); INSERT INTO USER_ROLE (user_id,role_id) VALUES (2,2); --`
3. Submit the form
4. The SQL is injected in the backend database and the user "ted" gets ADMIN role added

##### Third example dumping stored passwords
1. Visit [sign up page](http://localhost:8080/form)
2. Enter SQL below in name input field:
  1. `',''); INSERT INTO SIGNUP (name, address) SELECT username, password FROM USER; --`
3. Submit the form
4. Go to the [raw list](http://localhost:8080/list)
5. All usernames and passwords are added as signup names and addresses

##### Steps to fix
1. Open `src/main/java/sec/project/controller/DefaultController.java`
2. Find the comment "ISSUE #1" on line 74 and comment out the unsafe code right after it"
3. Enable the safer code below the comment. That code uses parameterised queries (or enable the code even further below using Repository mechanisms instead of native queries)

Issue #2: Passwords stored in cleartext
---------------------------------------
**OWASP 2013 Top 10 A2,A5,A6, A9**

A2-Broken Authentication due to A5-Security Misconfiguration and A9 Using Components with Known Vulnerabilities leading to easier A6-Sensitive Data Exposure.
##### Steps to reproduce
1. Reproduce the previous issue by the third example or log in as admin/admin and go to [list users](http://localhost:8080/users)
2. All passwords are readable as they are stored in plain text

##### Steps to fix the issue
1. Open `src/main/java/sec/project/config/SecurityConfiguration.java`
2. Find the comment "ISSUE #2" on line 33 and comment out the javabean using PlaintextPasswordEncoder()
3. Uncomment the javabean on line 40 which uses the BCryptPasswordEncoder() instead
4. Open `src/main/resources/import.sql`
5. Find the comment "ISSUE #2" on the first line (1) and comment out the block containing passwords in cleartext
6. Uncomment the block below (line 9 and on) so BCryptPasswords get loaded at start instead

Issue #3: Raw unscrubbed output
-------------------------------
**OWASP 2013 Top 10 A3,A4,A5,A6,A9**

A3-Cross-Site Scripting (XSS) by output of unscrubbed/unescaped data using A4-Insecure Direct Object References, A4-Security Misconfiguration and A9-Using Components with Known Vulnerabilities and leading to A6-Sensitive Data Exposure.
##### Steps to reproduce
1. Visit [sign up page](http://localhost:8080)
2. Enter Javascript below in name input field:
  1. `<script>alert("document.cookie=" + document.cookie);</script>`
3. Submit the form
4. Visit [raw list](http://localhost:8080/list)
5. Javascript is executed on the client and in this case an alert is shown

##### Steps to fix the issue
1. Open `src/main/java/sec/project/controller/DefaultController.java`
2. Find the comment "ISSUE #3" on line 124 and comment out the line above (123)
3. Uncomment the line below "ISSUE #3" (line 125) and HTML inside stored strings will be escaped

Issue #4: Session ID length
---------------------------
**OWASP 2013 Top 10 A2,A4,A5,A9**

A2-Broken Session Management by A4-Insecure Direct Object References and A5-Security Misconfiguration, A9-Using Components with Known Vulnerabilities.
##### Steps to reproduce
1. Authenticate as a user on the [login page](http://localhost:8080/login)
2. Reproduce the previous issue #3 or open your browser inspector (Ctrl+Shift+I or F12)
3. The JSESSION session cookie value is only 2 characters (hexadecimal between 00-FF)

##### Steps to fix the issue
1. Open `src/main/java/sec/project/config/DefaultController.java`
2. Find the comment "ISSUE #4" on line 225
3. Change the setSessionIdLength(1) parameter to setSessionIdLength(8)
4. Delete the session cookie JSESSIONID or logout
5. The JSESSION id will now be generated as 16 characters
6. This issue can also be fixed by following the steps for fixing the following issue #5

Issue #5: httpOnly
------------------
**OWASP 2013 Top 10 A2,A4,A5,A6,A8,A9**

A2-Broken Session Management due to A4-Insecure Direct Object References and A5-Security Misconfiguration, A9-Using Components with Known Vulnerabilities. Leads to A6-Sensitive Data Exposure which in turn makes A8-Cross Site Request Forgery (CSRF) easier.
##### Steps to reproduce
1. Authenticate as a user on the [login page](http://localhost:8080/login)
2. Reproduce issue #3 or open your browser inspector (Ctrl+Shift+I or F12)
3. The JSESSION session cookie does not have the httpOnly flag set and is readable by client scripts

##### Steps to fix the issue
1. Open `src/main/java/sec/project/config/DefaultController.java`
2. Find the comment "ISSUE #5 START" on line 219
3. Comment out all code between "ISSUE #5 START" and "ISSUE #5 END" on line 238
4. Default springboot security will now enforce the httpOnly flag on session cookie JSESSIONID
5. The JSESSION id will also now be generated with default setSessionIdLength() so previous issue #4 gets fixed too

Issue #6: No CSRF-token, HSTS and X-Frame-Options not set
---------------------------------------------------------
**OWASP 2013 Top 10 A5,A6,A8**

Multiple A5-Security Misconfigurations leading to A6-Sensitive Data Exposure and allowing for A8-Cross-Site Request Forgery (CSRF).
##### Steps to reproduce
1. Analyse application HTTP headers trough Zaproxy or similar tool
2. Submit [signup form](http://localhost:8080/form)
3. CRSF tokens are not used
4. HTTP Strict Transport security is not enabled
5. HTTP Header X-Frame-Options is not set

##### Steps to fix the issue
1. Open `src/main/java/sec/project/config/SecurityConfiguration.java`
2. Find the comment "ISSUE #6" on line 56 and comment out lines 59-61
3. CSRF-tokens will now be passed when submitting forms
4. HTTP Strict Transport Security is now enabled
5. HTTP Header X-Frame-Options is now set to DENY

Issue #7: User account data in the open
---------------------------------------
**OWASP 2013 Top 10 A5,A6,A7**

A7-Missing Function Level Access Control and A5-Security Misconfiguration leading to A6-Sensitive Data Exposure.
##### Steps to reproduce
1. Go to [/dumpusers](http://localhost:8080/dumpusers)
2. All user accounts are listed

##### Steps to fix
1. Open `src/main/java/sec/project/config/SecurityConfiguration.java`
2. Find the comment "ISSUE #7" on line 75
3. Comment out the line 74 above it
4. Uncomment line 76 below it
5. You now have to be authenticated with the role ADMIN to view the list