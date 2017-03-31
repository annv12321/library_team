-- Modified by: Anna Pelletier and Krista Christie

create table Category(
	category_name char(10) not null,
	checkout_period integer not null,
	max_books_out integer not null,
	check(category_name in 'Peon', 'BigCheese'),
	check(checkout_period in '14', '1000'),
	check(max_books_out in '5', '100')
);

create table Borrower(
	borrower_id char(10) not null,
	last_name char(20) not null,
	first_name char(20) not null,
	category_name char(10) not null
);

create table Borrower_phone(
    borrower_id char(10) not null,
    phone char(20) not null
);

create table Book_info(
	call_number char(20) not null,
	title char(50) not null,
	format char(2) not null,
	check(format in 'HC', 'SC', 'CD', 'MF', 'PE')
);

-- The code supplied below for accession_number will cause it to be generated
-- automatically when a new Book is added to the database

create table Book(
	call_number char(20) not null,
	copy_number smallint not null,
	accession_number integer  not null generated by default as identity
);

create table Book_author(
	call_number char(20) not null,
	author_name char(20) not null
);

create table Book_keyword(
    call_number char(20) not null,
    keyword varchar(20) not null
);

create table Checked_out(
	call_number char(20) not null,
	copy_number smallint not null,
	borrower_id char(10) not null,
	date_due date not null
);

create table Fine(
	borrower_id char(10) not null,
	title char(50) not null,
	date_due date not null,
	date_returned date not null,
	amount numeric(10,2) not null
);
