-- ISSUE 2: Comment out the INSERT passwords here and uncomment the BCryptPasswordEncoder ones below this block
--          Remember to also edit SecurityConfiguration.java
-- PlaintextPasswordEncoder
INSERT INTO User (id, username, password, enabled) VALUES (1, 'admin','admin', true);
INSERT INTO User (id, username, password, enabled) VALUES (2, 'ted','ted', true);
INSERT INTO User (id, username, password, enabled) VALUES (3, 'president','president', true);
INSERT INTO User (id, username, password, enabled) VALUES (4, 'disabled','disabled', false);

---- BCryptPasswordEncoder
--INSERT INTO User (id, username, password, enabled) VALUES (1, 'admin','$2a$10$VlQHfbbJdEH.Q2jcEfufn.e32mGCnKFGXooxS1s6xMEM7u6/3zHr.', true);
--INSERT INTO User (id, username, password, enabled) VALUES (2, 'ted','$2a$06$rtacOjuBuSlhnqMO2GKxW.Bs8J6KI0kYjw/gtF0bfErYgFyNTZRDm', true);
--INSERT INTO User (id, username, password, enabled) VALUES (3, 'president', '$2a$10$nKOFU.4/iK9CqDIlBkmMm.WZxy2XKdUSlImsG8iKsAP57GMcXwLTS', true);
--INSERT INTO User (id, username, password, enabled) VALUES (4, 'disabled','$2a$10$4Nhgx6BCvSsZ1c6gju11neAPYSoGcbtBTU0P9U.dEKc92YGkZCfvq', false);

---- MD5PasswordEncoder
--INSERT INTO User (id, username, password, enabled) VALUES (1, 'admin','21232f297a57a5a743894a0e4a801fc3', true);
--INSERT INTO User (id, username, password, enabled) VALUES (2, 'ted','870fa8ee962d90af50c7eaed792b075a', true);
--INSERT INTO User (id, username, password, enabled) VALUES (3, 'president','c8d56be998c94089ea6e1147dc9253c1', true);
--INSERT INTO User (id, username, password, enabled) VALUES (4, 'disabled','075ae3d2fc31640504f814f60e5ef713', false);

-- User granted roles
INSERT INTO Role (id, name) VALUES (1, 'USER');
INSERT INTO Role (id, name) VALUES (2, 'ADMIN');
INSERT INTO Role (id, name) VALUES (3, 'EDIT');

-- User to role mappings
INSERT INTO User_Role (user_id, role_id) VALUES (1,1);
INSERT INTO User_Role (user_id, role_id) VALUES (1,2);
INSERT INTO User_Role (user_id, role_id) VALUES (1,3);
INSERT INTO User_Role (user_id, role_id) VALUES (2,1);
INSERT INTO User_Role (user_id, role_id) VALUES (3,1);
INSERT INTO User_Role (user_id, role_id) VALUES (3,3);
INSERT INTO User_Role (user_id, role_id) VALUES (4,1);

-- Preload some signups
INSERT INTO Signup (name, address) VALUES ('Donald Duck', 'donald.duck@disney.com');
INSERT INTO Signup (name, address) VALUES ('Ronny Random', 'ronnie@random.net');
INSERT INTO Signup (name, address) VALUES ('Donald Trump', 'donald.trump@whitehouse.gov');
