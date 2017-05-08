insert into Category values('cat1', '9', '3');
insert into Borrower values('12345', 'Aardvark', 'Anthony', 'cat1');
insert into Book_info values('66666', 'book6', 'SC');
insert into Book_info values('77777', 'book7', 'SC');
insert into Book_info values('88888', 'book8', 'HC');
insert into Book_info values('99999', 'book9', 'HC');
insert into Book_author values('66666', 'Boris Buffalo');
insert into Book_author values('77777', 'Boris Buffalo');
insert into Book_author values('88888', 'Boris Buffalo');
insert into Book_author values('99999', 'Boris Buffalo');
insert into Book_keyword values('66666', 'history');
insert into Book_keyword values('77777', 'math');
insert into Book_keyword values('88888', 'english');
insert into Book_keyword values('99999', 'science');
insert into Book values('66666', '1', '1');
insert into Book values('66666', '2', '1');
insert into Book values('77777', '1', '1');
insert into Book values('88888', '1', '1');
insert into Book values('99999', '1', '1');
insert into Book values('99999', '2', '1');
insert into Checked_out values('99999', '1', '12345', '2017-4-10');
--- improper format type
insert into Book_info values('00000', 'book0', 'ZZ');
select * from Book_info;
--- improper keyword
insert into Book_info values('00000', 'book0', 'HC');
insert into Book_keyword values('00000', 'two words');
select * from Book_keyword;
--- no renew overdue book
insert into Checked_out values('66666', '1', '12345', '2017-3-20');
select * from Checked_out;
update Checked_out set date_due = '2017-4-10'
    where call_number = '66666' and copy_number = '1' and borrower_id = '12345';
select * from Checked_out;
--- create fine
update Checked_out set date_due = '2017-3-10'
    where call_number = '66666' and copy_number = '1' and borrower_id = '12345';
delete from Checked_out
    where call_number = '66666' and copy_number = '1' and borrower_id = '12345';
select * from fine;
--- exceed max checkout
insert into Checked_out values('66666', '2', '12345', '2017-4-10');
insert into Checked_out values('77777', '1', '12345', '2017-4-10');
insert into Checked_out values('88888', '1', '12345', '2017-4-10');
insert into Checked_out values('99999', '2', '12345', '2017-4-10');
select * from Checked_out;
--- remove book info with no copies
delete from Book where call_number = '77777';
select * from Book_info;
select * from Book_author;
select * from Book_keyword;
