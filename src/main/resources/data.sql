
-- Insert initial data
-- INSERT INTO app_user (username, password, roles) VALUES
--                                                        ('user', 'password', 'USER'),
--                                                        ('admin', 'admin', 'ADMIN');

-- Insert initial data with hashed passwords
INSERT INTO app_user (username, password, roles) VALUES
                                                     ('user', '$2a$10$jeOVcS2h6oMgsc94wJ093Ocwws1Mrn3GyMvhuCiVxji7feJuQ3/qS', 'USER'),
                                                     ('admin', '$2a$10$QvEH4qBBWtn84.bcyw5KwOJ9bDEqfpIwir2Dgbztz6LgVadi9jKu6', 'ADMIN');
