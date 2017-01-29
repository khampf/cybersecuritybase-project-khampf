-- PlaintextPasswordEncoded
INSERT INTO User (id, username, password, enabled) VALUES (1, 'admin','admin', true);
INSERT INTO User (id, username, password, enabled) VALUES (2, 'ted','president', true);
INSERT INTO User (id, username, password, enabled) VALUES (3, 'disabled','disabled', false);

-- BCryptPasswordEncoded
-- INSERT INTO User (id, username, password, enabled) VALUES (1, 'admin','$2a$10$VlQHfbbJdEH.Q2jcEfufn.e32mGCnKFGXooxS1s6xMEM7u6/3zHr.', true);
-- INSERT INTO User (id, username, password, enabled) VALUES (2, 'ted','$2a$10$nKOFU.4/iK9CqDIlBkmMm.WZxy2XKdUSlImsG8iKsAP57GMcXwLTS', true);
-- INSERT INTO User (id, username, password, enabled) VALUES (3, 'disabled','$2a$10$nKOFU.4/iK9CqDIlBkmMm.WZxy2XKdUSlImsG8iKsAP57GMcXwLTS', false);

INSERT INTO Role (id, name) VALUES (1, 'USER');
INSERT INTO Role (id, name) VALUES (2, 'ADMIN');

INSERT INTO User_Role (user_id, role_id) VALUES (1,1);
INSERT INTO User_Role (user_id, role_id) VALUES (1,2);
INSERT INTO User_Role (user_id, role_id) VALUES (2,1);
INSERT INTO User_Role (user_id, role_id) VALUES (3,1);

INSERT INTO Signup (name, address) VALUES ('Donald Duck', 'donald.duck@disney.com');
INSERT INTO Signup (name, address) VALUES ('Ronny Random', 'ronnie@random.net');
INSERT INTO Signup (name, address) VALUES ('Donald Trump', 'donald.trump@whitehouse.gov');
