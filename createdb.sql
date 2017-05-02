-- Modified by: Anna Pelletier, Krista Christie and James Kempf

create table Category(
	category_name char(10) not null,
	checkout_period integer not null,
	max_books_out integer not null,
	constraint category_key primary key(category_name)
);

create table Borrower(
	borrower_id char(10) not null,
	last_name char(20) not null,
	first_name char(20) not null,
	category_name char(10) not null,
	constraint borrower_key primary key(borrower_id),
	constraint borrower_foreign foreign key(category_name) references Category
			on delete cascade

);

create table Borrower_phone(
    borrower_id char(10) not null,
    phone char(20) not null,
	constraint borrower_phone_key primary key(borrower_id, phone),
	constraint borrower_phone_foreign foreign key(borrower_id)
			references Borrower on delete cascade
);

create table Book_info(
	call_number char(20) not null,
	title char(50) not null,
	format char(2) not null,
	constraint book_info_key primary key(call_number),
	constraint format_type check(format in ('HC', 'SC', 'CD', 'MF', 'PE'))
);

-- The code supplied below for accession_number will cause it to be generated
-- automatically when a new Book is added to the database

create table Book(
	call_number char(20) not null,
	copy_number smallint not null,
	barcode integer not null generated by default as identity,
	constraint book_key primary key(call_number, copy_number),
	constraint book_foreign foreign key(call_number) references Book_info
			on delete cascade
);

create table Book_author(
	call_number char(20) not null,
	author_name char(20) not null,
	constraint book_author_key primary key(call_number, author_name),
	constraint book_author_foreign foreign key(call_number)
			references Book_info on delete cascade
);

create table Book_keyword(
    call_number char(20) not null,
    keyword varchar(20) not null,
	constraint book_keyword_key primary key(call_number, keyword),
	constraint book_keyword_foreign foreign key(call_number)
			references Book_info on delete cascade,
	constraint valid_keyword check(keyword not like '% %')
);

create table Checked_out(
	call_number char(20) not null,
	copy_number smallint not null,
	borrower_id char(10) not null,
	date_due date not null,
	constraint checked_out_key primary key(call_number, copy_number),
	constraint checked_out_foreign_prim foreign key(call_number, copy_number)
			references Book on delete cascade,
	constraint checked_out_foreign_bor foreign key(borrower_id)
			references Borrower
);

create table Fine(
	borrower_id char(10) not null,
	title char(50) not null,
	date_due date not null,
	date_returned date not null,
	amount numeric(10,2) not null,
	constraint fine_primary primary key(borrower_id, title, date_due),
	constraint fine_foreign foreign key(borrower_id) references Borrower
			on delete cascade
);

-- create trigger no_renew_if_overdue
-- 	before update of date_due on Checked_out
-- 		referencing OLD as old_date
-- 	for each row
-- 	mode db2sql
-- 	when (old_date.date_due < current_date)
-- 	signal sqlstate value '71001'
-- 	set message_text = 'Cannot renew when overdue.';

-- create trigger create_fine
-- 	after delete on Checked_out
-- 	referencing OLD as old_checkout
-- 	for each row
-- 	mode db2sql
-- 	when (old_checkout.date_due < current_date)
-- 		insert into Fine values(
-- 			old_checkout.borrower_id,
-- 			(select Book_info.title from Book_info
-- 				where Book_info.call_number = old_checkout.call_number),
-- 			old_checkout.date_due,
-- 			current_date,
-- 			'1.00');    -- We didn't know how much to assess. Not in requirements.

create trigger max_checkedout
	no cascade before insert on Checked_out
	referencing NEW as new_checkout
	for each row
	mode db2sql
	when ((select count(call_number) from Checked_out
	where Checked_out.borrower_id = new_checkout.borrower_id) >=
		(select Category.max_books_out from Category where Category.category_name =
			(select Borrower.category_name from Borrower where
				Borrower.borrower_id = new_checkout.borrower_id)))
	signal sqlstate value '71001'
	set message_text = 'Cannot check out any more items.';

create trigger remove_book_info_when_no_copies
	after delete on Book
	referencing OLD as old_book
	for each row
	mode db2sql
	when (old_book.call_number not in (select Book.call_number from Book))
	delete from book_info where book_info.call_number = old_book.call_number;


grant all on Category to user anna;
grant all on Category to user james;
grant all on Category to user bjork;
grant all on Borrower to user anna;
grant all on Borrower to user james;
grant all on Borrower to user bjork;
grant all on Borrower_phone to user anna;
grant all on Borrower_phone to user james;
grant all on Borrower_phone to user bjork;
grant all on Book to user anna;
grant all on Book to user james;
grant all on Book to user bjork;
grant all on Book_info to user anna;
grant all on Book_info to user james;
grant all on Book_info to user bjork;
grant all on Book_author to user anna;
grant all on Book_author to user james;
grant all on Book_author to user bjork;
grant all on Book_keyword to user anna;
grant all on Book_keyword to user james;
grant all on Book_keyword to user bjork;
grant all on Checked_out to user anna;
grant all on Checked_out to user james;
grant all on Checked_out to user bjork;
grant all on Fine to user anna;
grant all on Fine to user james;
grant all on Fine to user bjork;
