---insert data into user table-----
insert into user (email, fname, lname,password) values ('sonu@gamil.com','Sonu','Yadav','1234');
insert into user (email, fname, lname,password) values ('sumanpal@gamil.com','sumanpal','Yadav','5678');
insert into user (email, fname, lname,password) values ('monu@gamil.com','monu','Yadav','abcd');
Insert into user (email, fname, lname,password) values ('nitin@gamil.com','nitin','Yadav','sonu');
-----insert data into wallet table-----
insert into wallet values (1,1);
insert into wallet values (2,2);
insert into wallet values (3,3);
-- --insert data into account table------
insert into account (account_number, balance, account_holder_user_id, wallet_holder_wallet_id) values (1000, 1000, 1, 1 );
insert into account (account_number, balance, account_holder_user_id, wallet_holder_wallet_id) values (2000, 2000, 2, 2 );
insert into account (account_number, balance, account_holder_user_id, wallet_holder_wallet_id) values (3000, 3000, 3, 3 );
