-- Users for authentication
INSERT INTO app_user (id, email, password, role) VALUES ('a1b2c3d4-e5f6-7890-abcd-ef1234567890', 'librarian@bookvault.com', '$2a$10$XpvWSbcjYmQgv3iFIt0LIeWtu9nqm0JRkjLwH7u79jnK6UPSN4zVG', 'LIBRARIAN');

INSERT INTO app_user (id, email, password, role) VALUES ('b2c3d4e5-f6a7-8901-bcde-f12345678901', 'member@bookvault.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lh7y', 'MEMBER');

-- Members
INSERT INTO member (id, email, name, membership_status, joined_at) VALUES ('c3d4e5f6-a7b8-9012-cdef-123456789012', 'member@bookvault.com', 'John Doe', 'ACTIVE', CURRENT_TIMESTAMP);

-- Books
INSERT INTO book (id, isbn, title, author, genre, total_copies, available_copies) VALUES ('d4e5f6a7-b8c9-0123-defa-234567890123', '978-0061964360', 'The Hobbit', 'J.R.R. Tolkien', 'Fantasy', 3, 3);

INSERT INTO book (id, isbn, title, author, genre, total_copies, available_copies) VALUES ('e5f6a7b8-c9d0-1234-efab-345678901234', '978-0451524935', '1984', 'George Orwell', 'Dystopian', 2, 2);

INSERT INTO book (id, isbn, title, author, genre, total_copies, available_copies) VALUES ('f6a7b8c9-d0e1-2345-fabc-456789012345', '978-0743273565', 'The Great Gatsby', 'F. Scott Fitzgerald', 'Classic', 2, 2);

INSERT INTO book (id, isbn, title, author, genre, total_copies, available_copies) VALUES ('a7b8c9d0-e1f2-3456-abcd-567890123456', '978-0316769174', 'The Catcher in the Rye', 'J.D. Salinger', 'Classic', 1, 1);