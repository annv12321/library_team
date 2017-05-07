/*@lineinfo:filename=Database*//*@lineinfo:user-code*//*@lineinfo:1^1*//*
 * file: Database.sqlj
 *
 * Originally written by Russell C. Bjork
 * Modified for CS352 Project by: Anna Pelletier, Krista Christie and James Kempf
 *
 */

package semesterproject;

import semesterproject.gui.ErrorMessage;
import java.io.PrintStream;
import java.util.Calendar;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.sql.*;
import java.math.BigDecimal;
import sqlj.runtime.*;
import sqlj.runtime.ref.*;

/**
 * This class manages access to the database for the CS352 Library project
 */

public class Database
{
    /** Constructor
     *
     */
    public Database()
    {
        // Load the db2 driver

        try
        {
            Class.forName(DRIVER_NAME).newInstance();
        }
        catch(Exception e)
        {
            System.err.println("Error loading db2 driver " + e);
            if (e instanceof SQLException)
                System.err.println("Code: " + ((SQLException) e).getErrorCode()
                    + "State: " + ((SQLException) e).getSQLState());
        }
    }

    /* *************************************************************************
     * THE FOLLOWING METHODS ARE CALLED WHEN VARIOUS OPERATIONS ARE SELECTED
     * BY THE USER IN THE GUI.
     * ************************************************************************/

    /** Method used to login to the database - called when user enters a
     *  username and password and clicks OK.  For testing / evaluation, the
     *  special username "none" is recognized (and does not need a password). As
     *  a username, "none" means proceed without establishing a connection to the
     *  database - usable only for demonstrating the GUI without actually
     *  accessing the database, of course.
     *
     *  @param username the username the user typed
     *  @param password the password the user typed
     *
     *  @exception an ErrorMessage is thrown with an appropriate message if
     *          the login fails
     */
    public void login(String username, char [] password) throws ErrorMessage
    {
        Connection connection = null;
        try
        {
            if (! username.equals("none"))
            {
                connection = DriverManager.getConnection(DATABASE_URL,
                                    username, new String(password));
                DefaultContext context = new DefaultContext(connection);
                DefaultContext.setDefaultContext(context);
                connection.setAutoCommit(false);
            }
        }
        catch(SQLException e)
        {
            if (e.getErrorCode() == BAD_PASSWORD_SQL_ERROR)
                throw new ErrorMessage("Incorrect username and/or password");
            else
            {
                throw new ErrorMessage("Unexpected SQL error: " + e.getMessage() +
                      "Code: " + e.getErrorCode() + " State: " + e.getSQLState());
            }
        }
        finally
        {
            for (int i = 0; i < password.length; i ++)
                password[i] = 0;
        }
    }

    /* ************************************************************************
     * Methods called to perform check-out, check-in, and renewal of books.
     * ***********************************************************************/

    /** Get the name of a borrower, given his/her ID
     *
     *  @param borrowerID the ID of the borrower.
     *  @return this borrower's name
     *
     *  @exception an ErrorMessage is thrown with an appropriate message if
     *          the borrower does not exist
     */
    public String getBorrowerName(String borrowerID) throws ErrorMessage
    {
        String last, first;
        try {
            /*@lineinfo:generated-code*//*@lineinfo:113^13*/

//  ************************************************************
//  #sql { select last_name, first_name
//                   INTO :last, :first, 
//                  from borrower
//                  where borrower_id = :borrowerID
//               };
//  ************************************************************

{
  sqlj.runtime.profile.RTResultSet __sJT_rtRs;
  sqlj.runtime.ConnectionContext __sJT_connCtx = sqlj.runtime.ref.DefaultContext.getDefaultContext();
  if (__sJT_connCtx == null) sqlj.runtime.error.RuntimeRefErrors.raise_NULL_DEFAULT_CONN_CTX();
  sqlj.runtime.ExecutionContext __sJT_execCtx = __sJT_connCtx.getExecutionContext();
  if (__sJT_execCtx == null) sqlj.runtime.error.RuntimeRefErrors.raise_NULL_EXEC_CTX();
  synchronized (__sJT_execCtx) {
    sqlj.runtime.profile.RTStatement __sJT_stmt = __sJT_execCtx.registerStatement(__sJT_connCtx, Database_SJProfileKeys.getKey(0), 0);
    try 
    {
      __sJT_stmt.setString(1, borrowerID);
      __sJT_rtRs = __sJT_execCtx.executeQuery();
    }
    finally 
    {
      __sJT_execCtx.releaseStatement();
    }
  }
  try 
  {
    sqlj.runtime.ref.ResultSetIterImpl.checkColumns(__sJT_rtRs, 2);
    if (!__sJT_rtRs.next())
    {
      sqlj.runtime.error.RuntimeRefErrors.raise_NO_ROW_SELECT_INTO();
    }
    last = __sJT_rtRs.getString(1);
    first = __sJT_rtRs.getString(2);
    if (__sJT_rtRs.next())
    {
      sqlj.runtime.error.RuntimeRefErrors.raise_MULTI_ROW_SELECT_INTO();
    }
  }
  finally 
  {
    __sJT_rtRs.close();
  }
}


//  ************************************************************

/*@lineinfo:user-code*//*@lineinfo:117^13*/
            return last + " " + first;
        }
        catch(SQLException e) {
            if (e.getSQLState().equals(NO_ROW_SQL_STATE))
                throw new ErrorMessage("No such borrower");
            else
                throw new ErrorMessage("Unexpected SQL error " + e.getMessage());
        }
        finally {
            rollback();
        }
    }

    /** Check out a book.
     *
     *  @param borrowerID the ID of the borrower to whom book is to be checked
     *         out
     *  @param callNumber the call number of the book to check out
     *  @param copyNumber the copy number of the book to check out
     *  @return the date on which the book will be due
     *
     *  @exception an ErrorMessage is thrown with an appropriate message if
     *          the check out fails
     */
    public Date checkOutBook(String borrowerID,
                             String callNumber,
                             short copyNumber) throws ErrorMessage
    {
        try {
            int checkoutPeriod;
            /*@lineinfo:generated-code*//*@lineinfo:148^13*/

//  ************************************************************
//  #sql { select checkout_period
//                   INTO :checkoutPeriod, 
//                  from category join borrower
//                  on category.category_name = borrower.category_name
//                  where borrower.borrower_id = :borrowerID
//               };
//  ************************************************************

{
  sqlj.runtime.profile.RTResultSet __sJT_rtRs;
  sqlj.runtime.ConnectionContext __sJT_connCtx = sqlj.runtime.ref.DefaultContext.getDefaultContext();
  if (__sJT_connCtx == null) sqlj.runtime.error.RuntimeRefErrors.raise_NULL_DEFAULT_CONN_CTX();
  sqlj.runtime.ExecutionContext __sJT_execCtx = __sJT_connCtx.getExecutionContext();
  if (__sJT_execCtx == null) sqlj.runtime.error.RuntimeRefErrors.raise_NULL_EXEC_CTX();
  synchronized (__sJT_execCtx) {
    sqlj.runtime.profile.RTStatement __sJT_stmt = __sJT_execCtx.registerStatement(__sJT_connCtx, Database_SJProfileKeys.getKey(0), 1);
    try 
    {
      __sJT_stmt.setString(1, borrowerID);
      __sJT_rtRs = __sJT_execCtx.executeQuery();
    }
    finally 
    {
      __sJT_execCtx.releaseStatement();
    }
  }
  try 
  {
    sqlj.runtime.ref.ResultSetIterImpl.checkColumns(__sJT_rtRs, 1);
    if (!__sJT_rtRs.next())
    {
      sqlj.runtime.error.RuntimeRefErrors.raise_NO_ROW_SELECT_INTO();
    }
    checkoutPeriod = __sJT_rtRs.getIntNoNull(1);
    if (__sJT_rtRs.next())
    {
      sqlj.runtime.error.RuntimeRefErrors.raise_MULTI_ROW_SELECT_INTO();
    }
  }
  finally 
  {
    __sJT_rtRs.close();
  }
}


//  ************************************************************

/*@lineinfo:user-code*//*@lineinfo:153^13*/
            Calendar cal = Calendar.getInstance();
            cal.add(cal.DAY_OF_YEAR, deltaDate + checkoutPeriod);
            Date dateDue = new Date(cal.getTimeInMillis());
            /*@lineinfo:generated-code*//*@lineinfo:157^13*/

//  ************************************************************
//  #sql { insert into checked_out
//                  values(:callNumber, :copyNumber, :borrowerID, :dateDue)
//               };
//  ************************************************************

{
  sqlj.runtime.ConnectionContext __sJT_connCtx = sqlj.runtime.ref.DefaultContext.getDefaultContext();
  if (__sJT_connCtx == null) sqlj.runtime.error.RuntimeRefErrors.raise_NULL_DEFAULT_CONN_CTX();
  sqlj.runtime.ExecutionContext __sJT_execCtx = __sJT_connCtx.getExecutionContext();
  if (__sJT_execCtx == null) sqlj.runtime.error.RuntimeRefErrors.raise_NULL_EXEC_CTX();
  synchronized (__sJT_execCtx) {
    sqlj.runtime.profile.RTStatement __sJT_stmt = __sJT_execCtx.registerStatement(__sJT_connCtx, Database_SJProfileKeys.getKey(0), 2);
    try 
    {
      __sJT_stmt.setString(1, callNumber);
      __sJT_stmt.setShort(2, copyNumber);
      __sJT_stmt.setString(3, borrowerID);
      __sJT_stmt.setDate(4, dateDue);
      __sJT_execCtx.executeUpdate();
    }
    finally 
    {
      __sJT_execCtx.releaseStatement();
    }
  }
}


//  ************************************************************

/*@lineinfo:user-code*//*@lineinfo:159^13*/
            /*@lineinfo:generated-code*//*@lineinfo:160^13*/

//  ************************************************************
//  #sql { commit  };
//  ************************************************************

{
  sqlj.runtime.ConnectionContext __sJT_connCtx = sqlj.runtime.ref.DefaultContext.getDefaultContext();
  if (__sJT_connCtx == null) sqlj.runtime.error.RuntimeRefErrors.raise_NULL_DEFAULT_CONN_CTX();
  sqlj.runtime.ExecutionContext __sJT_execCtx = __sJT_connCtx.getExecutionContext();
  if (__sJT_execCtx == null) sqlj.runtime.error.RuntimeRefErrors.raise_NULL_EXEC_CTX();
  synchronized (__sJT_execCtx) {
    sqlj.runtime.profile.RTStatement __sJT_stmt = __sJT_execCtx.registerStatement(__sJT_connCtx, Database_SJProfileKeys.getKey(0), 3);
    try 
    {
      __sJT_execCtx.executeUpdate();
    }
    finally 
    {
      __sJT_execCtx.releaseStatement();
    }
  }
}


//  ************************************************************

/*@lineinfo:user-code*//*@lineinfo:160^27*/
            return dateDue;
        }
        catch(SQLException e) {
            rollback();
            if (e.getSQLState().equals(DUPLICATE_KEY_SQL_ERROR))
                throw new ErrorMessage("Book already checked out");
            else
                throw new ErrorMessage("Unexpected SQL error " + e.getMessage());
        }
    }

    /** Return a book.  If an overdue fine is incurred, it will be recorded
     *  in the database.
     *
     *  @param callNumber the call number of the book to return
     *  @param copyNumber the copy number of the book to return
     *
     *  @exception an ErrorMessage is thrown with an appropriate message if
     *          the return fails.  If a fine is incurred, an ErrorMessage is
     *          also thrown reporting this fact after the fine is recorded in
     *          the database
     */
    public void returnBook(String callNumber, short copyNumber) throws ErrorMessage
    {
        try {
            Date dateDue;
            String borrowerID;
            String bookTitle;
            /*@lineinfo:generated-code*//*@lineinfo:189^13*/

//  ************************************************************
//  #sql { select date_due, borrower_id
//                   INTO :dateDue, :borrowerID, 
//                  from checked_out
//                  where call_number = :callNumber and copy_number = :copyNumber
//               };
//  ************************************************************

{
  sqlj.runtime.profile.RTResultSet __sJT_rtRs;
  sqlj.runtime.ConnectionContext __sJT_connCtx = sqlj.runtime.ref.DefaultContext.getDefaultContext();
  if (__sJT_connCtx == null) sqlj.runtime.error.RuntimeRefErrors.raise_NULL_DEFAULT_CONN_CTX();
  sqlj.runtime.ExecutionContext __sJT_execCtx = __sJT_connCtx.getExecutionContext();
  if (__sJT_execCtx == null) sqlj.runtime.error.RuntimeRefErrors.raise_NULL_EXEC_CTX();
  synchronized (__sJT_execCtx) {
    sqlj.runtime.profile.RTStatement __sJT_stmt = __sJT_execCtx.registerStatement(__sJT_connCtx, Database_SJProfileKeys.getKey(0), 4);
    try 
    {
      __sJT_stmt.setString(1, callNumber);
      __sJT_stmt.setShort(2, copyNumber);
      __sJT_rtRs = __sJT_execCtx.executeQuery();
    }
    finally 
    {
      __sJT_execCtx.releaseStatement();
    }
  }
  try 
  {
    sqlj.runtime.ref.ResultSetIterImpl.checkColumns(__sJT_rtRs, 2);
    if (!__sJT_rtRs.next())
    {
      sqlj.runtime.error.RuntimeRefErrors.raise_NO_ROW_SELECT_INTO();
    }
    dateDue = __sJT_rtRs.getDate(1);
    borrowerID = __sJT_rtRs.getString(2);
    if (__sJT_rtRs.next())
    {
      sqlj.runtime.error.RuntimeRefErrors.raise_MULTI_ROW_SELECT_INTO();
    }
  }
  finally 
  {
    __sJT_rtRs.close();
  }
}


//  ************************************************************

/*@lineinfo:user-code*//*@lineinfo:193^13*/
            /*@lineinfo:generated-code*//*@lineinfo:194^13*/

//  ************************************************************
//  #sql { select title
//                       INTO :bookTitle, 
//                      from book_info
//                      where call_number = :callNumber
//               };
//  ************************************************************

{
  sqlj.runtime.profile.RTResultSet __sJT_rtRs;
  sqlj.runtime.ConnectionContext __sJT_connCtx = sqlj.runtime.ref.DefaultContext.getDefaultContext();
  if (__sJT_connCtx == null) sqlj.runtime.error.RuntimeRefErrors.raise_NULL_DEFAULT_CONN_CTX();
  sqlj.runtime.ExecutionContext __sJT_execCtx = __sJT_connCtx.getExecutionContext();
  if (__sJT_execCtx == null) sqlj.runtime.error.RuntimeRefErrors.raise_NULL_EXEC_CTX();
  synchronized (__sJT_execCtx) {
    sqlj.runtime.profile.RTStatement __sJT_stmt = __sJT_execCtx.registerStatement(__sJT_connCtx, Database_SJProfileKeys.getKey(0), 5);
    try 
    {
      __sJT_stmt.setString(1, callNumber);
      __sJT_rtRs = __sJT_execCtx.executeQuery();
    }
    finally 
    {
      __sJT_execCtx.releaseStatement();
    }
  }
  try 
  {
    sqlj.runtime.ref.ResultSetIterImpl.checkColumns(__sJT_rtRs, 1);
    if (!__sJT_rtRs.next())
    {
      sqlj.runtime.error.RuntimeRefErrors.raise_NO_ROW_SELECT_INTO();
    }
    bookTitle = __sJT_rtRs.getString(1);
    if (__sJT_rtRs.next())
    {
      sqlj.runtime.error.RuntimeRefErrors.raise_MULTI_ROW_SELECT_INTO();
    }
  }
  finally 
  {
    __sJT_rtRs.close();
  }
}


//  ************************************************************

/*@lineinfo:user-code*//*@lineinfo:199^13*/
            Calendar now = Calendar.getInstance();
            now.add(now.DAY_OF_YEAR, deltaDate);
            String dd = dateDue.toString();
            Calendar old = Calendar.getInstance();
            old.set(Integer.valueOf(dd.substring(0,4)), Integer.valueOf(dd.substring(5,7)), Integer.valueOf(dd.substring(8,10)));
            int daysLate = now.compareTo(old);
            Date currentDate = new Date(now.getTimeInMillis());
            if (daysLate > 0) {
                BigDecimal fineAmount = new BigDecimal(Fine.DAILY_FINE_RATE * .01 * daysLate).
                    setScale(2, BigDecimal.ROUND_HALF_UP);
                /*@lineinfo:generated-code*//*@lineinfo:210^17*/

//  ************************************************************
//  #sql { insert into fine
//                      values(:borrowerID, :bookTitle, :dateDue, :currentDate, :fineAmount)
//                   };
//  ************************************************************

{
  sqlj.runtime.ConnectionContext __sJT_connCtx = sqlj.runtime.ref.DefaultContext.getDefaultContext();
  if (__sJT_connCtx == null) sqlj.runtime.error.RuntimeRefErrors.raise_NULL_DEFAULT_CONN_CTX();
  sqlj.runtime.ExecutionContext __sJT_execCtx = __sJT_connCtx.getExecutionContext();
  if (__sJT_execCtx == null) sqlj.runtime.error.RuntimeRefErrors.raise_NULL_EXEC_CTX();
  synchronized (__sJT_execCtx) {
    sqlj.runtime.profile.RTStatement __sJT_stmt = __sJT_execCtx.registerStatement(__sJT_connCtx, Database_SJProfileKeys.getKey(0), 6);
    try 
    {
      __sJT_stmt.setString(1, borrowerID);
      __sJT_stmt.setString(2, bookTitle);
      __sJT_stmt.setDate(3, dateDue);
      __sJT_stmt.setDate(4, currentDate);
      __sJT_stmt.setBigDecimal(5, fineAmount);
      __sJT_execCtx.executeUpdate();
    }
    finally 
    {
      __sJT_execCtx.releaseStatement();
    }
  }
}


//  ************************************************************

/*@lineinfo:user-code*//*@lineinfo:212^17*/
                throw new ErrorMessage("Book is overdue " + daysLate + " days " +
                    "- a fine of $" + fineAmount + " has been assessed");
            }
            /*@lineinfo:generated-code*//*@lineinfo:216^13*/

//  ************************************************************
//  #sql { delete from checked_out
//                  where call_number = :callNumber and copy_number = :copyNumber
//               };
//  ************************************************************

{
  sqlj.runtime.ConnectionContext __sJT_connCtx = sqlj.runtime.ref.DefaultContext.getDefaultContext();
  if (__sJT_connCtx == null) sqlj.runtime.error.RuntimeRefErrors.raise_NULL_DEFAULT_CONN_CTX();
  sqlj.runtime.ExecutionContext __sJT_execCtx = __sJT_connCtx.getExecutionContext();
  if (__sJT_execCtx == null) sqlj.runtime.error.RuntimeRefErrors.raise_NULL_EXEC_CTX();
  synchronized (__sJT_execCtx) {
    sqlj.runtime.profile.RTStatement __sJT_stmt = __sJT_execCtx.registerStatement(__sJT_connCtx, Database_SJProfileKeys.getKey(0), 7);
    try 
    {
      __sJT_stmt.setString(1, callNumber);
      __sJT_stmt.setShort(2, copyNumber);
      __sJT_execCtx.executeUpdate();
    }
    finally 
    {
      __sJT_execCtx.releaseStatement();
    }
  }
}


//  ************************************************************

/*@lineinfo:user-code*//*@lineinfo:218^13*/
            /*@lineinfo:generated-code*//*@lineinfo:219^13*/

//  ************************************************************
//  #sql { commit  };
//  ************************************************************

{
  sqlj.runtime.ConnectionContext __sJT_connCtx = sqlj.runtime.ref.DefaultContext.getDefaultContext();
  if (__sJT_connCtx == null) sqlj.runtime.error.RuntimeRefErrors.raise_NULL_DEFAULT_CONN_CTX();
  sqlj.runtime.ExecutionContext __sJT_execCtx = __sJT_connCtx.getExecutionContext();
  if (__sJT_execCtx == null) sqlj.runtime.error.RuntimeRefErrors.raise_NULL_EXEC_CTX();
  synchronized (__sJT_execCtx) {
    sqlj.runtime.profile.RTStatement __sJT_stmt = __sJT_execCtx.registerStatement(__sJT_connCtx, Database_SJProfileKeys.getKey(0), 8);
    try 
    {
      __sJT_execCtx.executeUpdate();
    }
    finally 
    {
      __sJT_execCtx.releaseStatement();
    }
  }
}


//  ************************************************************

/*@lineinfo:user-code*//*@lineinfo:219^27*/
        }
        catch(SQLException e) {
            rollback();
            if (e.getSQLState().equals(DUPLICATE_KEY_SQL_ERROR))
                throw new ErrorMessage("Book already checked out");
            else
                throw new ErrorMessage("Unexpected SQL error " + e.getMessage());
        }
    }

    /** Renew a book  If there is any problem, an appropriate error
     *  message will be displayed before returning to the caller.
     *
     *  @param callNumber the call number of the book to renew
     *  @param copyNumber the copy number of the book to renew
     *  @return the date on which the book will be due
     *
     *  @exception an ErrorMessage is thrown with an appropriate message if
     *          the renewal fails for any reason.
     */
    public Date renewBook(String callNumber, short copyNumber) throws ErrorMessage
    {
        try {
            int checkoutPeriod;
            Date dateDue;
            String borrowerID;
            /*@lineinfo:generated-code*//*@lineinfo:246^13*/

//  ************************************************************
//  #sql { select date_due, borrower_id
//                   INTO :dateDue, :borrowerID, 
//                  from checked_out
//                  where call_number = :callNumber and copy_number = :copyNumber
//               };
//  ************************************************************

{
  sqlj.runtime.profile.RTResultSet __sJT_rtRs;
  sqlj.runtime.ConnectionContext __sJT_connCtx = sqlj.runtime.ref.DefaultContext.getDefaultContext();
  if (__sJT_connCtx == null) sqlj.runtime.error.RuntimeRefErrors.raise_NULL_DEFAULT_CONN_CTX();
  sqlj.runtime.ExecutionContext __sJT_execCtx = __sJT_connCtx.getExecutionContext();
  if (__sJT_execCtx == null) sqlj.runtime.error.RuntimeRefErrors.raise_NULL_EXEC_CTX();
  synchronized (__sJT_execCtx) {
    sqlj.runtime.profile.RTStatement __sJT_stmt = __sJT_execCtx.registerStatement(__sJT_connCtx, Database_SJProfileKeys.getKey(0), 9);
    try 
    {
      __sJT_stmt.setString(1, callNumber);
      __sJT_stmt.setShort(2, copyNumber);
      __sJT_rtRs = __sJT_execCtx.executeQuery();
    }
    finally 
    {
      __sJT_execCtx.releaseStatement();
    }
  }
  try 
  {
    sqlj.runtime.ref.ResultSetIterImpl.checkColumns(__sJT_rtRs, 2);
    if (!__sJT_rtRs.next())
    {
      sqlj.runtime.error.RuntimeRefErrors.raise_NO_ROW_SELECT_INTO();
    }
    dateDue = __sJT_rtRs.getDate(1);
    borrowerID = __sJT_rtRs.getString(2);
    if (__sJT_rtRs.next())
    {
      sqlj.runtime.error.RuntimeRefErrors.raise_MULTI_ROW_SELECT_INTO();
    }
  }
  finally 
  {
    __sJT_rtRs.close();
  }
}


//  ************************************************************

/*@lineinfo:user-code*//*@lineinfo:250^13*/
            Calendar now = Calendar.getInstance();
            now.add(now.DAY_OF_YEAR, deltaDate);
            String dd = dateDue.toString();
            Calendar old = Calendar.getInstance();
            old.set(Integer.valueOf(dd.substring(0,4)), Integer.valueOf(dd.substring(5,7)), Integer.valueOf(dd.substring(8,10)));
            int daysLate = now.compareTo(old);
            if (daysLate > 0)
            {
                rollback();
                throw new ErrorMessage("Book is overdue " + daysLate +
                    " days and cannot be renewed");
            }
            else {
                /*@lineinfo:generated-code*//*@lineinfo:264^17*/

//  ************************************************************
//  #sql { select category.checkout_period
//                       INTO :checkoutPeriod, 
//                      from category join borrower
//                      on category.category_name = borrower.category_name
//                      where borrower.borrower_id = :borrowerID
//                   };
//  ************************************************************

{
  sqlj.runtime.profile.RTResultSet __sJT_rtRs;
  sqlj.runtime.ConnectionContext __sJT_connCtx = sqlj.runtime.ref.DefaultContext.getDefaultContext();
  if (__sJT_connCtx == null) sqlj.runtime.error.RuntimeRefErrors.raise_NULL_DEFAULT_CONN_CTX();
  sqlj.runtime.ExecutionContext __sJT_execCtx = __sJT_connCtx.getExecutionContext();
  if (__sJT_execCtx == null) sqlj.runtime.error.RuntimeRefErrors.raise_NULL_EXEC_CTX();
  synchronized (__sJT_execCtx) {
    sqlj.runtime.profile.RTStatement __sJT_stmt = __sJT_execCtx.registerStatement(__sJT_connCtx, Database_SJProfileKeys.getKey(0), 10);
    try 
    {
      __sJT_stmt.setString(1, borrowerID);
      __sJT_rtRs = __sJT_execCtx.executeQuery();
    }
    finally 
    {
      __sJT_execCtx.releaseStatement();
    }
  }
  try 
  {
    sqlj.runtime.ref.ResultSetIterImpl.checkColumns(__sJT_rtRs, 1);
    if (!__sJT_rtRs.next())
    {
      sqlj.runtime.error.RuntimeRefErrors.raise_NO_ROW_SELECT_INTO();
    }
    checkoutPeriod = __sJT_rtRs.getIntNoNull(1);
    if (__sJT_rtRs.next())
    {
      sqlj.runtime.error.RuntimeRefErrors.raise_MULTI_ROW_SELECT_INTO();
    }
  }
  finally 
  {
    __sJT_rtRs.close();
  }
}


//  ************************************************************

/*@lineinfo:user-code*//*@lineinfo:269^17*/
                Calendar cal = Calendar.getInstance();
                cal.add(cal.DAY_OF_YEAR, deltaDate + checkoutPeriod);
                Date newDateDue = new Date(cal.getTimeInMillis());
                /*@lineinfo:generated-code*//*@lineinfo:273^17*/

//  ************************************************************
//  #sql { update checked_out
//                      set date_due = :newDateDue
//                      where call_number = :callNumber and copy_number = :copyNumber
//                   };
//  ************************************************************

{
  sqlj.runtime.ConnectionContext __sJT_connCtx = sqlj.runtime.ref.DefaultContext.getDefaultContext();
  if (__sJT_connCtx == null) sqlj.runtime.error.RuntimeRefErrors.raise_NULL_DEFAULT_CONN_CTX();
  sqlj.runtime.ExecutionContext __sJT_execCtx = __sJT_connCtx.getExecutionContext();
  if (__sJT_execCtx == null) sqlj.runtime.error.RuntimeRefErrors.raise_NULL_EXEC_CTX();
  synchronized (__sJT_execCtx) {
    sqlj.runtime.profile.RTStatement __sJT_stmt = __sJT_execCtx.registerStatement(__sJT_connCtx, Database_SJProfileKeys.getKey(0), 11);
    try 
    {
      __sJT_stmt.setDate(1, newDateDue);
      __sJT_stmt.setString(2, callNumber);
      __sJT_stmt.setShort(3, copyNumber);
      __sJT_execCtx.executeUpdate();
    }
    finally 
    {
      __sJT_execCtx.releaseStatement();
    }
  }
}


//  ************************************************************

/*@lineinfo:user-code*//*@lineinfo:276^17*/
                /*@lineinfo:generated-code*//*@lineinfo:277^17*/

//  ************************************************************
//  #sql { commit  };
//  ************************************************************

{
  sqlj.runtime.ConnectionContext __sJT_connCtx = sqlj.runtime.ref.DefaultContext.getDefaultContext();
  if (__sJT_connCtx == null) sqlj.runtime.error.RuntimeRefErrors.raise_NULL_DEFAULT_CONN_CTX();
  sqlj.runtime.ExecutionContext __sJT_execCtx = __sJT_connCtx.getExecutionContext();
  if (__sJT_execCtx == null) sqlj.runtime.error.RuntimeRefErrors.raise_NULL_EXEC_CTX();
  synchronized (__sJT_execCtx) {
    sqlj.runtime.profile.RTStatement __sJT_stmt = __sJT_execCtx.registerStatement(__sJT_connCtx, Database_SJProfileKeys.getKey(0), 12);
    try 
    {
      __sJT_execCtx.executeUpdate();
    }
    finally 
    {
      __sJT_execCtx.releaseStatement();
    }
  }
}


//  ************************************************************

/*@lineinfo:user-code*//*@lineinfo:277^31*/
                return newDateDue;
            }
        }
        catch(SQLException e) {
            rollback();
            if (e.getSQLState().equals(DUPLICATE_KEY_SQL_ERROR))
                throw new ErrorMessage("DUPLICATE_KEY_SQL_ERROR");
            else
                throw new ErrorMessage("Unexpected SQL error " + e.getMessage());
        }
    }

    /* ************************************************************************
     * Methods that support adding/editing/deleting entities.  In most cases,
     * some access to existing information in the database is needed before
     * the user can be asked for all the information needed for the change.
     * ***********************************************************************/

    /** Determine whether or not a book with a given call number already exists
     *
     *  @param callNumber the call number of a book
     *  @return true if an entry exists in the database for this call number
     */
    public boolean bookExists(String callNumber) throws ErrorMessage
    {
        int count = 0;
        try
        {
            /*@lineinfo:generated-code*//*@lineinfo:306^13*/

//  ************************************************************
//  #sql { select count(*)
//                       INTO :count, 
//                      from Book_info
//                      where call_number = :callNumber
//                    };
//  ************************************************************

{
  sqlj.runtime.profile.RTResultSet __sJT_rtRs;
  sqlj.runtime.ConnectionContext __sJT_connCtx = sqlj.runtime.ref.DefaultContext.getDefaultContext();
  if (__sJT_connCtx == null) sqlj.runtime.error.RuntimeRefErrors.raise_NULL_DEFAULT_CONN_CTX();
  sqlj.runtime.ExecutionContext __sJT_execCtx = __sJT_connCtx.getExecutionContext();
  if (__sJT_execCtx == null) sqlj.runtime.error.RuntimeRefErrors.raise_NULL_EXEC_CTX();
  synchronized (__sJT_execCtx) {
    sqlj.runtime.profile.RTStatement __sJT_stmt = __sJT_execCtx.registerStatement(__sJT_connCtx, Database_SJProfileKeys.getKey(0), 13);
    try 
    {
      __sJT_stmt.setString(1, callNumber);
      __sJT_rtRs = __sJT_execCtx.executeQuery();
    }
    finally 
    {
      __sJT_execCtx.releaseStatement();
    }
  }
  try 
  {
    sqlj.runtime.ref.ResultSetIterImpl.checkColumns(__sJT_rtRs, 1);
    if (!__sJT_rtRs.next())
    {
      sqlj.runtime.error.RuntimeRefErrors.raise_NO_ROW_SELECT_INTO();
    }
    count = __sJT_rtRs.getIntNoNull(1);
    if (__sJT_rtRs.next())
    {
      sqlj.runtime.error.RuntimeRefErrors.raise_MULTI_ROW_SELECT_INTO();
    }
  }
  finally 
  {
    __sJT_rtRs.close();
  }
}


//  ************************************************************

/*@lineinfo:user-code*//*@lineinfo:310^18*/
        }
        catch(SQLException e)
        {
            throw new ErrorMessage("Unexpected SQL error " + e.getMessage());
        }
        finally
        {
            rollback();
        }

        return count > 0;
    }

    /** Add a new book to the database.
     *
     *  @param callNumber the call number of the book to add
     *  // The remaining parameters are null when adding a new copy of an existing
     *  // book
     *  @param title the title of the book
     *  @param authors the authors of the book (a List whose elements are strings
     *                 corresponding to the various authors)
     *  @param format the format of the book
     *  @param keywords the keywords of the book (a List whose elements are strings
     *                 corresponding to the various keywords)
     *  // Note that the copy number and accession number are not passed as
     *  // parameters.  The copy number will be 1 if there is no existing
     *  // book of this call number - else 1 more than the highest existing
     *  // number.  The accession number will be generated automatically
     *
     *  @exception an ErrorMessage is thrown with an appropriate message if
     *          the operation fails.  Any failure results in no changes being
     *          made to the database
     */
    public void addBook(String callNumber, String title, List authors,
                        String format, List keywords) throws ErrorMessage
    {
        try
        {
            if (title == null)
            {
                // New copy of an existing book

                /*@lineinfo:generated-code*//*@lineinfo:353^17*/

//  ************************************************************
//  #sql { insert into Book(call_number, copy_number)
//                          values (:callNumber,
//                                  1 + (select max(copy_number)
//                                          from Book
//                                          where call_number = :callNumber))
//                        };
//  ************************************************************

{
  sqlj.runtime.ConnectionContext __sJT_connCtx = sqlj.runtime.ref.DefaultContext.getDefaultContext();
  if (__sJT_connCtx == null) sqlj.runtime.error.RuntimeRefErrors.raise_NULL_DEFAULT_CONN_CTX();
  sqlj.runtime.ExecutionContext __sJT_execCtx = __sJT_connCtx.getExecutionContext();
  if (__sJT_execCtx == null) sqlj.runtime.error.RuntimeRefErrors.raise_NULL_EXEC_CTX();
  synchronized (__sJT_execCtx) {
    sqlj.runtime.profile.RTStatement __sJT_stmt = __sJT_execCtx.registerStatement(__sJT_connCtx, Database_SJProfileKeys.getKey(0), 14);
    try 
    {
      __sJT_stmt.setString(1, callNumber);
      __sJT_stmt.setString(2, callNumber);
      __sJT_execCtx.executeUpdate();
    }
    finally 
    {
      __sJT_execCtx.releaseStatement();
    }
  }
}


//  ************************************************************

/*@lineinfo:user-code*//*@lineinfo:358^22*/

                /*@lineinfo:generated-code*//*@lineinfo:360^17*/

//  ************************************************************
//  #sql { commit  };
//  ************************************************************

{
  sqlj.runtime.ConnectionContext __sJT_connCtx = sqlj.runtime.ref.DefaultContext.getDefaultContext();
  if (__sJT_connCtx == null) sqlj.runtime.error.RuntimeRefErrors.raise_NULL_DEFAULT_CONN_CTX();
  sqlj.runtime.ExecutionContext __sJT_execCtx = __sJT_connCtx.getExecutionContext();
  if (__sJT_execCtx == null) sqlj.runtime.error.RuntimeRefErrors.raise_NULL_EXEC_CTX();
  synchronized (__sJT_execCtx) {
    sqlj.runtime.profile.RTStatement __sJT_stmt = __sJT_execCtx.registerStatement(__sJT_connCtx, Database_SJProfileKeys.getKey(0), 15);
    try 
    {
      __sJT_execCtx.executeUpdate();
    }
    finally 
    {
      __sJT_execCtx.releaseStatement();
    }
  }
}


//  ************************************************************

/*@lineinfo:user-code*//*@lineinfo:360^31*/
            }
            else
            {
                // Totally new book

                /*@lineinfo:generated-code*//*@lineinfo:366^17*/

//  ************************************************************
//  #sql { insert into Book_info
//                          values (:callNumber, :title, :format)
//                        };
//  ************************************************************

{
  sqlj.runtime.ConnectionContext __sJT_connCtx = sqlj.runtime.ref.DefaultContext.getDefaultContext();
  if (__sJT_connCtx == null) sqlj.runtime.error.RuntimeRefErrors.raise_NULL_DEFAULT_CONN_CTX();
  sqlj.runtime.ExecutionContext __sJT_execCtx = __sJT_connCtx.getExecutionContext();
  if (__sJT_execCtx == null) sqlj.runtime.error.RuntimeRefErrors.raise_NULL_EXEC_CTX();
  synchronized (__sJT_execCtx) {
    sqlj.runtime.profile.RTStatement __sJT_stmt = __sJT_execCtx.registerStatement(__sJT_connCtx, Database_SJProfileKeys.getKey(0), 16);
    try 
    {
      __sJT_stmt.setString(1, callNumber);
      __sJT_stmt.setString(2, title);
      __sJT_stmt.setString(3, format);
      __sJT_execCtx.executeUpdate();
    }
    finally 
    {
      __sJT_execCtx.releaseStatement();
    }
  }
}


//  ************************************************************

/*@lineinfo:user-code*//*@lineinfo:368^22*/

                Iterator authorIterator = authors.iterator();
                while (authorIterator.hasNext())
                {
                    String author = (String) authorIterator.next();

                    /*@lineinfo:generated-code*//*@lineinfo:375^21*/

//  ************************************************************
//  #sql { insert into Book_author
//                              values(:callNumber, :author)
//                            };
//  ************************************************************

{
  sqlj.runtime.ConnectionContext __sJT_connCtx = sqlj.runtime.ref.DefaultContext.getDefaultContext();
  if (__sJT_connCtx == null) sqlj.runtime.error.RuntimeRefErrors.raise_NULL_DEFAULT_CONN_CTX();
  sqlj.runtime.ExecutionContext __sJT_execCtx = __sJT_connCtx.getExecutionContext();
  if (__sJT_execCtx == null) sqlj.runtime.error.RuntimeRefErrors.raise_NULL_EXEC_CTX();
  synchronized (__sJT_execCtx) {
    sqlj.runtime.profile.RTStatement __sJT_stmt = __sJT_execCtx.registerStatement(__sJT_connCtx, Database_SJProfileKeys.getKey(0), 17);
    try 
    {
      __sJT_stmt.setString(1, callNumber);
      __sJT_stmt.setString(2, author);
      __sJT_execCtx.executeUpdate();
    }
    finally 
    {
      __sJT_execCtx.releaseStatement();
    }
  }
}


//  ************************************************************

/*@lineinfo:user-code*//*@lineinfo:377^26*/
                }

                Iterator keywordIterator = keywords.iterator();
                while (keywordIterator.hasNext())
                {
                    String keyword = (String) keywordIterator.next();

                    /*@lineinfo:generated-code*//*@lineinfo:385^21*/

//  ************************************************************
//  #sql { insert into Book_keyword
//                              values(:callNumber, :keyword)
//                            };
//  ************************************************************

{
  sqlj.runtime.ConnectionContext __sJT_connCtx = sqlj.runtime.ref.DefaultContext.getDefaultContext();
  if (__sJT_connCtx == null) sqlj.runtime.error.RuntimeRefErrors.raise_NULL_DEFAULT_CONN_CTX();
  sqlj.runtime.ExecutionContext __sJT_execCtx = __sJT_connCtx.getExecutionContext();
  if (__sJT_execCtx == null) sqlj.runtime.error.RuntimeRefErrors.raise_NULL_EXEC_CTX();
  synchronized (__sJT_execCtx) {
    sqlj.runtime.profile.RTStatement __sJT_stmt = __sJT_execCtx.registerStatement(__sJT_connCtx, Database_SJProfileKeys.getKey(0), 18);
    try 
    {
      __sJT_stmt.setString(1, callNumber);
      __sJT_stmt.setString(2, keyword);
      __sJT_execCtx.executeUpdate();
    }
    finally 
    {
      __sJT_execCtx.releaseStatement();
    }
  }
}


//  ************************************************************

/*@lineinfo:user-code*//*@lineinfo:387^26*/
                }

                /*@lineinfo:generated-code*//*@lineinfo:390^17*/

//  ************************************************************
//  #sql { insert into Book(call_number, copy_number)
//                          values (:callNumber, 1)
//                        };
//  ************************************************************

{
  sqlj.runtime.ConnectionContext __sJT_connCtx = sqlj.runtime.ref.DefaultContext.getDefaultContext();
  if (__sJT_connCtx == null) sqlj.runtime.error.RuntimeRefErrors.raise_NULL_DEFAULT_CONN_CTX();
  sqlj.runtime.ExecutionContext __sJT_execCtx = __sJT_connCtx.getExecutionContext();
  if (__sJT_execCtx == null) sqlj.runtime.error.RuntimeRefErrors.raise_NULL_EXEC_CTX();
  synchronized (__sJT_execCtx) {
    sqlj.runtime.profile.RTStatement __sJT_stmt = __sJT_execCtx.registerStatement(__sJT_connCtx, Database_SJProfileKeys.getKey(0), 19);
    try 
    {
      __sJT_stmt.setString(1, callNumber);
      __sJT_execCtx.executeUpdate();
    }
    finally 
    {
      __sJT_execCtx.releaseStatement();
    }
  }
}


//  ************************************************************

/*@lineinfo:user-code*//*@lineinfo:392^22*/

                /*@lineinfo:generated-code*//*@lineinfo:394^17*/

//  ************************************************************
//  #sql { commit  };
//  ************************************************************

{
  sqlj.runtime.ConnectionContext __sJT_connCtx = sqlj.runtime.ref.DefaultContext.getDefaultContext();
  if (__sJT_connCtx == null) sqlj.runtime.error.RuntimeRefErrors.raise_NULL_DEFAULT_CONN_CTX();
  sqlj.runtime.ExecutionContext __sJT_execCtx = __sJT_connCtx.getExecutionContext();
  if (__sJT_execCtx == null) sqlj.runtime.error.RuntimeRefErrors.raise_NULL_EXEC_CTX();
  synchronized (__sJT_execCtx) {
    sqlj.runtime.profile.RTStatement __sJT_stmt = __sJT_execCtx.registerStatement(__sJT_connCtx, Database_SJProfileKeys.getKey(0), 20);
    try 
    {
      __sJT_execCtx.executeUpdate();
    }
    finally 
    {
      __sJT_execCtx.releaseStatement();
    }
  }
}


//  ************************************************************

/*@lineinfo:user-code*//*@lineinfo:394^31*/
            }
        }
        catch(SQLException e)
        {
            rollback();
            if (e.getErrorCode() == DUPLICATE_KEY_SQL_ERROR)
            {
                // A primary key violation cannot occur for the Book table
                // or for the Book_info table, so it must be for the author
                // or keyword table
                if (e.getMessage().indexOf(BOOK_AUTHOR_TABLE_NAME) >= 0)
                    throw new ErrorMessage("Duplicate author for this book");
                else
                    throw new ErrorMessage("Duplicate keyword for this book");
            }
            else if (e.getMessage().indexOf(BOOK_INFO_VALID_FORMAT_CONSTRAINT) >= 0)
                throw new ErrorMessage("Invalid format");
            else if (e.getMessage().indexOf(BOOK_KEYWORD_VALID_KEYWORD_CONSTRAINT) >= 0)
                throw new ErrorMessage("A keyword contains a space");
            else
                throw new ErrorMessage("Unexpected SQL error " + e.getMessage());
        }
    }

    /** Get information on an existing book about to be edited
     *
     *  @param callNumber the call number of the book
     *  @return values recorded in the database for this book.  A single
     *          valued attribute has its value represented as a string; a
     *          multi-valued attribute has its value represented as a
     *          List whose elements are strings representing the various values
     *
     *  @exception an ErrorMessage is thrown with an appropriate message if
     *          the book does not exist
     */
    /*@lineinfo:generated-code*//*@lineinfo:430^5*/

//  ************************************************************
//  SQLJ iterator declaration:
//  ************************************************************

class BookInfoCursor 
extends sqlj.runtime.ref.ResultSetIterImpl
implements sqlj.runtime.PositionedIterator
{
  public BookInfoCursor(sqlj.runtime.profile.RTResultSet resultSet) 
    throws java.sql.SQLException 
  {
    super(resultSet, 1);
  }
  public BookInfoCursor(sqlj.runtime.profile.RTResultSet resultSet, int fetchSize, int maxRows) 
    throws java.sql.SQLException 
  {
    super(resultSet, fetchSize, maxRows, 1);
  }
  public String getCol1() 
    throws java.sql.SQLException 
  {
    return resultSet.getString(1);
  }
}


//  ************************************************************

/*@lineinfo:user-code*//*@lineinfo:430^40*/

    public Object[] getBookInformation(String callNumber) throws ErrorMessage
    {
        List originalAuthors = new ArrayList();
        List originalKeywords = new ArrayList();
        Object[] values = new Object[7];
        values[0] = callNumber;
        values[1] = "(information applies to all copies of this book)";
        values[2] = "(information applies to all copies of this book)";
        try
        {
            String title = null, format = null;

            /*@lineinfo:generated-code*//*@lineinfo:444^13*/

//  ************************************************************
//  #sql { select title, format
//                       INTO :title, :format, 
//                      from Book_info
//                      where call_number = :callNumber
//                    };
//  ************************************************************

{
  sqlj.runtime.profile.RTResultSet __sJT_rtRs;
  sqlj.runtime.ConnectionContext __sJT_connCtx = sqlj.runtime.ref.DefaultContext.getDefaultContext();
  if (__sJT_connCtx == null) sqlj.runtime.error.RuntimeRefErrors.raise_NULL_DEFAULT_CONN_CTX();
  sqlj.runtime.ExecutionContext __sJT_execCtx = __sJT_connCtx.getExecutionContext();
  if (__sJT_execCtx == null) sqlj.runtime.error.RuntimeRefErrors.raise_NULL_EXEC_CTX();
  synchronized (__sJT_execCtx) {
    sqlj.runtime.profile.RTStatement __sJT_stmt = __sJT_execCtx.registerStatement(__sJT_connCtx, Database_SJProfileKeys.getKey(0), 21);
    try 
    {
      __sJT_stmt.setString(1, callNumber);
      __sJT_rtRs = __sJT_execCtx.executeQuery();
    }
    finally 
    {
      __sJT_execCtx.releaseStatement();
    }
  }
  try 
  {
    sqlj.runtime.ref.ResultSetIterImpl.checkColumns(__sJT_rtRs, 2);
    if (!__sJT_rtRs.next())
    {
      sqlj.runtime.error.RuntimeRefErrors.raise_NO_ROW_SELECT_INTO();
    }
    title = __sJT_rtRs.getString(1);
    format = __sJT_rtRs.getString(2);
    if (__sJT_rtRs.next())
    {
      sqlj.runtime.error.RuntimeRefErrors.raise_MULTI_ROW_SELECT_INTO();
    }
  }
  finally 
  {
    __sJT_rtRs.close();
  }
}


//  ************************************************************

/*@lineinfo:user-code*//*@lineinfo:448^18*/

            values[3] = title;
            values[5] = format;

            BookInfoCursor cursor;

            /*@lineinfo:generated-code*//*@lineinfo:455^13*/

//  ************************************************************
//  #sql cursor = { select author_name
//                                  from Book_author
//                                  where call_number = :callNumber
//                             };
//  ************************************************************

{
  sqlj.runtime.ConnectionContext __sJT_connCtx = sqlj.runtime.ref.DefaultContext.getDefaultContext();
  if (__sJT_connCtx == null) sqlj.runtime.error.RuntimeRefErrors.raise_NULL_DEFAULT_CONN_CTX();
  sqlj.runtime.ExecutionContext __sJT_execCtx = __sJT_connCtx.getExecutionContext();
  if (__sJT_execCtx == null) sqlj.runtime.error.RuntimeRefErrors.raise_NULL_EXEC_CTX();
  synchronized (__sJT_execCtx) {
    sqlj.runtime.profile.RTStatement __sJT_stmt = __sJT_execCtx.registerStatement(__sJT_connCtx, Database_SJProfileKeys.getKey(0), 22);
    try 
    {
      __sJT_stmt.setString(1, callNumber);
      cursor = new BookInfoCursor(__sJT_execCtx.executeQuery(), __sJT_execCtx.getFetchSize(), __sJT_execCtx.getMaxRows());
    }
    finally 
    {
      __sJT_execCtx.releaseStatement();
    }
  }
}


//  ************************************************************

/*@lineinfo:user-code*//*@lineinfo:458^27*/

            String author = null;

            while(true)
            {
                /*@lineinfo:generated-code*//*@lineinfo:464^17*/

//  ************************************************************
//  #sql { fetch :cursor  INTO :author, 
//                        };
//  ************************************************************

{
  if (cursor.next())
  {
    author = cursor.getCol1();
  }
}


//  ************************************************************

/*@lineinfo:user-code*//*@lineinfo:465^22*/

                if (cursor.endFetch()) break;

                originalAuthors.add(author);
            }

            values[4] = originalAuthors;

            /*@lineinfo:generated-code*//*@lineinfo:474^13*/

//  ************************************************************
//  #sql cursor = { select keyword
//                                  from Book_keyword
//                                  where call_number = :callNumber
//                             };
//  ************************************************************

{
  sqlj.runtime.ConnectionContext __sJT_connCtx = sqlj.runtime.ref.DefaultContext.getDefaultContext();
  if (__sJT_connCtx == null) sqlj.runtime.error.RuntimeRefErrors.raise_NULL_DEFAULT_CONN_CTX();
  sqlj.runtime.ExecutionContext __sJT_execCtx = __sJT_connCtx.getExecutionContext();
  if (__sJT_execCtx == null) sqlj.runtime.error.RuntimeRefErrors.raise_NULL_EXEC_CTX();
  synchronized (__sJT_execCtx) {
    sqlj.runtime.profile.RTStatement __sJT_stmt = __sJT_execCtx.registerStatement(__sJT_connCtx, Database_SJProfileKeys.getKey(0), 23);
    try 
    {
      __sJT_stmt.setString(1, callNumber);
      cursor = new BookInfoCursor(__sJT_execCtx.executeQuery(), __sJT_execCtx.getFetchSize(), __sJT_execCtx.getMaxRows());
    }
    finally 
    {
      __sJT_execCtx.releaseStatement();
    }
  }
}


//  ************************************************************

/*@lineinfo:user-code*//*@lineinfo:477^27*/

            String keyword = null;

            while(true)
            {
                /*@lineinfo:generated-code*//*@lineinfo:483^17*/

//  ************************************************************
//  #sql { fetch :cursor  INTO :keyword, 
//                        };
//  ************************************************************

{
  if (cursor.next())
  {
    keyword = cursor.getCol1();
  }
}


//  ************************************************************

/*@lineinfo:user-code*//*@lineinfo:484^22*/

                if (cursor.endFetch()) break;

                originalKeywords.add(keyword);
            }

            values[6] = originalKeywords;
        }
        catch(SQLException e)
        {
            if (e.getSQLState().equals(NO_ROW_SQL_STATE))
                throw new ErrorMessage("No such book");
            else
                throw new ErrorMessage("Unexpected SQL error " + e.getMessage());
        }
        finally
        {
            rollback();
        }

        return values;
    }

    /** Update stored information about a book
     *
     *  @param callNumber the call number of the book
     *  @param newTitle the new value of the book's title (may be the same as original)
     *  @param originalAuthors the original list of authors for the book
     *         This will be a list of Strings, each the name of one author
     *  @param newAuthors the new list of the book's authors (may be the same as original)
     *         This will be a list of Strings, each the name of one author
     *  @param newFormat the new value of the book's format (may be the same as original)
     *  @param originalKeywords the original list of keywords for the book
     *         This will be a list of Strings, each the name of one author
     *  @param newKeywords the new list of the book's keywords (may be the same as original)
     *         This will be a list of Strings, each one keyword
     *
     *  @exception an ErrorMessage is thrown with an appropriate message if
     *          the operation fails.  Any failure results in no changes being
     *          made to the database
     */
    public void updateBook(String callNumber,
                           String newTitle,
                           List originalAuthors,
                           List newAuthors,
                           String newFormat,
                           List originalKeywords,
                           List newKeywords) throws ErrorMessage
    {
        try
        {
            /*@lineinfo:generated-code*//*@lineinfo:536^13*/

//  ************************************************************
//  #sql { update Book_info
//                          set title = :newTitle, format = :newFormat
//                          where call_number = :callNumber
//                    };
//  ************************************************************

{
  sqlj.runtime.ConnectionContext __sJT_connCtx = sqlj.runtime.ref.DefaultContext.getDefaultContext();
  if (__sJT_connCtx == null) sqlj.runtime.error.RuntimeRefErrors.raise_NULL_DEFAULT_CONN_CTX();
  sqlj.runtime.ExecutionContext __sJT_execCtx = __sJT_connCtx.getExecutionContext();
  if (__sJT_execCtx == null) sqlj.runtime.error.RuntimeRefErrors.raise_NULL_EXEC_CTX();
  synchronized (__sJT_execCtx) {
    sqlj.runtime.profile.RTStatement __sJT_stmt = __sJT_execCtx.registerStatement(__sJT_connCtx, Database_SJProfileKeys.getKey(0), 24);
    try 
    {
      __sJT_stmt.setString(1, newTitle);
      __sJT_stmt.setString(2, newFormat);
      __sJT_stmt.setString(3, callNumber);
      __sJT_execCtx.executeUpdate();
    }
    finally 
    {
      __sJT_execCtx.releaseStatement();
    }
  }
}


//  ************************************************************

/*@lineinfo:user-code*//*@lineinfo:539^18*/

            Iterator originalAuthorsIterator = originalAuthors.iterator();
            Iterator newAuthorsIterator = newAuthors.iterator();

            while (originalAuthorsIterator.hasNext())
            {
                // Remove any authors no longer listed

                String original = (String) originalAuthorsIterator.next();
                if (! newAuthors.contains(original))

                    /*@lineinfo:generated-code*//*@lineinfo:551^21*/

//  ************************************************************
//  #sql { delete from Book_author
//                                  where call_number = :callNumber and
//                                  author_name = :original
//                            };
//  ************************************************************

{
  sqlj.runtime.ConnectionContext __sJT_connCtx = sqlj.runtime.ref.DefaultContext.getDefaultContext();
  if (__sJT_connCtx == null) sqlj.runtime.error.RuntimeRefErrors.raise_NULL_DEFAULT_CONN_CTX();
  sqlj.runtime.ExecutionContext __sJT_execCtx = __sJT_connCtx.getExecutionContext();
  if (__sJT_execCtx == null) sqlj.runtime.error.RuntimeRefErrors.raise_NULL_EXEC_CTX();
  synchronized (__sJT_execCtx) {
    sqlj.runtime.profile.RTStatement __sJT_stmt = __sJT_execCtx.registerStatement(__sJT_connCtx, Database_SJProfileKeys.getKey(0), 25);
    try 
    {
      __sJT_stmt.setString(1, callNumber);
      __sJT_stmt.setString(2, original);
      __sJT_execCtx.executeUpdate();
    }
    finally 
    {
      __sJT_execCtx.releaseStatement();
    }
  }
}


//  ************************************************************

/*@lineinfo:user-code*//*@lineinfo:554^26*/
            }

            while(newAuthorsIterator.hasNext())
            {
                // Add any authors not there originally

                String newAuthor = (String) newAuthorsIterator.next();
                if (! originalAuthors.contains(newAuthor))

                    /*@lineinfo:generated-code*//*@lineinfo:564^21*/

//  ************************************************************
//  #sql { insert into Book_author
//                                  values(:callNumber, :newAuthor)
//                            };
//  ************************************************************

{
  sqlj.runtime.ConnectionContext __sJT_connCtx = sqlj.runtime.ref.DefaultContext.getDefaultContext();
  if (__sJT_connCtx == null) sqlj.runtime.error.RuntimeRefErrors.raise_NULL_DEFAULT_CONN_CTX();
  sqlj.runtime.ExecutionContext __sJT_execCtx = __sJT_connCtx.getExecutionContext();
  if (__sJT_execCtx == null) sqlj.runtime.error.RuntimeRefErrors.raise_NULL_EXEC_CTX();
  synchronized (__sJT_execCtx) {
    sqlj.runtime.profile.RTStatement __sJT_stmt = __sJT_execCtx.registerStatement(__sJT_connCtx, Database_SJProfileKeys.getKey(0), 26);
    try 
    {
      __sJT_stmt.setString(1, callNumber);
      __sJT_stmt.setString(2, newAuthor);
      __sJT_execCtx.executeUpdate();
    }
    finally 
    {
      __sJT_execCtx.releaseStatement();
    }
  }
}


//  ************************************************************

/*@lineinfo:user-code*//*@lineinfo:566^26*/
            }

            Iterator originalKeywordsIterator = originalKeywords.iterator();
            Iterator newKeywordsIterator = newKeywords.iterator();

            while (originalKeywordsIterator.hasNext())
            {
                // Remove any keywords no longer listed

                String original = (String) originalKeywordsIterator.next();
                if (! newKeywords.contains(original))

                    /*@lineinfo:generated-code*//*@lineinfo:579^21*/

//  ************************************************************
//  #sql { delete from Book_keyword
//                                  where call_number = :callNumber and
//                                  keyword = :original
//                            };
//  ************************************************************

{
  sqlj.runtime.ConnectionContext __sJT_connCtx = sqlj.runtime.ref.DefaultContext.getDefaultContext();
  if (__sJT_connCtx == null) sqlj.runtime.error.RuntimeRefErrors.raise_NULL_DEFAULT_CONN_CTX();
  sqlj.runtime.ExecutionContext __sJT_execCtx = __sJT_connCtx.getExecutionContext();
  if (__sJT_execCtx == null) sqlj.runtime.error.RuntimeRefErrors.raise_NULL_EXEC_CTX();
  synchronized (__sJT_execCtx) {
    sqlj.runtime.profile.RTStatement __sJT_stmt = __sJT_execCtx.registerStatement(__sJT_connCtx, Database_SJProfileKeys.getKey(0), 27);
    try 
    {
      __sJT_stmt.setString(1, callNumber);
      __sJT_stmt.setString(2, original);
      __sJT_execCtx.executeUpdate();
    }
    finally 
    {
      __sJT_execCtx.releaseStatement();
    }
  }
}


//  ************************************************************

/*@lineinfo:user-code*//*@lineinfo:582^26*/
            }

            while(newKeywordsIterator.hasNext())
            {
                // Add any keywords not there originally

                String newKeyword = (String) newKeywordsIterator.next();
                if (! originalKeywords.contains(newKeyword))

                    /*@lineinfo:generated-code*//*@lineinfo:592^21*/

//  ************************************************************
//  #sql { insert into Book_keyword
//                                  values(:callNumber, :newKeyword)
//                            };
//  ************************************************************

{
  sqlj.runtime.ConnectionContext __sJT_connCtx = sqlj.runtime.ref.DefaultContext.getDefaultContext();
  if (__sJT_connCtx == null) sqlj.runtime.error.RuntimeRefErrors.raise_NULL_DEFAULT_CONN_CTX();
  sqlj.runtime.ExecutionContext __sJT_execCtx = __sJT_connCtx.getExecutionContext();
  if (__sJT_execCtx == null) sqlj.runtime.error.RuntimeRefErrors.raise_NULL_EXEC_CTX();
  synchronized (__sJT_execCtx) {
    sqlj.runtime.profile.RTStatement __sJT_stmt = __sJT_execCtx.registerStatement(__sJT_connCtx, Database_SJProfileKeys.getKey(0), 28);
    try 
    {
      __sJT_stmt.setString(1, callNumber);
      __sJT_stmt.setString(2, newKeyword);
      __sJT_execCtx.executeUpdate();
    }
    finally 
    {
      __sJT_execCtx.releaseStatement();
    }
  }
}


//  ************************************************************

/*@lineinfo:user-code*//*@lineinfo:594^26*/
            }

            /*@lineinfo:generated-code*//*@lineinfo:597^13*/

//  ************************************************************
//  #sql { commit  };
//  ************************************************************

{
  sqlj.runtime.ConnectionContext __sJT_connCtx = sqlj.runtime.ref.DefaultContext.getDefaultContext();
  if (__sJT_connCtx == null) sqlj.runtime.error.RuntimeRefErrors.raise_NULL_DEFAULT_CONN_CTX();
  sqlj.runtime.ExecutionContext __sJT_execCtx = __sJT_connCtx.getExecutionContext();
  if (__sJT_execCtx == null) sqlj.runtime.error.RuntimeRefErrors.raise_NULL_EXEC_CTX();
  synchronized (__sJT_execCtx) {
    sqlj.runtime.profile.RTStatement __sJT_stmt = __sJT_execCtx.registerStatement(__sJT_connCtx, Database_SJProfileKeys.getKey(0), 29);
    try 
    {
      __sJT_execCtx.executeUpdate();
    }
    finally 
    {
      __sJT_execCtx.releaseStatement();
    }
  }
}


//  ************************************************************

/*@lineinfo:user-code*//*@lineinfo:597^27*/
        }
        catch(SQLException e)
        {
            rollback();
            if (e.getErrorCode() == DUPLICATE_KEY_SQL_ERROR)
            {
                if (e.getMessage().indexOf(BOOK_AUTHOR_TABLE_NAME) >= 0)
                    throw new ErrorMessage("Duplicate author for this book");
                else
                    throw new ErrorMessage("Duplicate keyword for this book");
            }
            else if (e.getMessage().indexOf(BOOK_INFO_VALID_FORMAT_CONSTRAINT) >= 0)
                throw new ErrorMessage("Invalid format");
            else if (e.getMessage().indexOf(BOOK_KEYWORD_VALID_KEYWORD_CONSTRAINT) >= 0)
                throw new ErrorMessage("A keyword contains a space");
            else
                throw new ErrorMessage("Unexpected SQL error " + e.getMessage());
        }
    }

    /** Get information on an existing book copy about to be deleted
     *
     *  @param callNumber the call number of the book
     *  @param copyNumber the copy number of the book
     *  @return values recorded in the database for this book.  A single
     *          valued attribute has its value represented as a string; a
     *          multi-valued attribute has its value represented as a
     *          List whose elements are strings representing the various values
     *
     *  @exception an ErrorMessage is thrown with an appropriate message if
     *          the book does not exist
     */
    public Object[] getBookCopyInformation(String callNumber, short copyNumber)
                                                            throws ErrorMessage
    {
        List originalAuthors = new ArrayList();
        List originalKeywords = new ArrayList();
        Object[] values = new Object[7];
        values[0] = callNumber;
        values[1] = "" + copyNumber;

        try
        {
            short barCode = 0;
            String title = null, format = null;

            /*@lineinfo:generated-code*//*@lineinfo:644^13*/

//  ************************************************************
//  #sql { select barcode, title, format
//                       INTO :barCode, :title, :format, 
//                      from Book join Book_info
//                          on Book.call_number = Book_info.call_number
//                      where Book.call_number = :callNumber and
//                            Book.copy_number = :copyNumber
//                    };
//  ************************************************************

{
  sqlj.runtime.profile.RTResultSet __sJT_rtRs;
  sqlj.runtime.ConnectionContext __sJT_connCtx = sqlj.runtime.ref.DefaultContext.getDefaultContext();
  if (__sJT_connCtx == null) sqlj.runtime.error.RuntimeRefErrors.raise_NULL_DEFAULT_CONN_CTX();
  sqlj.runtime.ExecutionContext __sJT_execCtx = __sJT_connCtx.getExecutionContext();
  if (__sJT_execCtx == null) sqlj.runtime.error.RuntimeRefErrors.raise_NULL_EXEC_CTX();
  synchronized (__sJT_execCtx) {
    sqlj.runtime.profile.RTStatement __sJT_stmt = __sJT_execCtx.registerStatement(__sJT_connCtx, Database_SJProfileKeys.getKey(0), 30);
    try 
    {
      __sJT_stmt.setString(1, callNumber);
      __sJT_stmt.setShort(2, copyNumber);
      __sJT_rtRs = __sJT_execCtx.executeQuery();
    }
    finally 
    {
      __sJT_execCtx.releaseStatement();
    }
  }
  try 
  {
    sqlj.runtime.ref.ResultSetIterImpl.checkColumns(__sJT_rtRs, 3);
    if (!__sJT_rtRs.next())
    {
      sqlj.runtime.error.RuntimeRefErrors.raise_NO_ROW_SELECT_INTO();
    }
    barCode = __sJT_rtRs.getShortNoNull(1);
    title = __sJT_rtRs.getString(2);
    format = __sJT_rtRs.getString(3);
    if (__sJT_rtRs.next())
    {
      sqlj.runtime.error.RuntimeRefErrors.raise_MULTI_ROW_SELECT_INTO();
    }
  }
  finally 
  {
    __sJT_rtRs.close();
  }
}


//  ************************************************************

/*@lineinfo:user-code*//*@lineinfo:650^18*/

            values[2] = "" + barCode;
            values[3] = title;
            values[5] = format;

            BookInfoCursor cursor;

            /*@lineinfo:generated-code*//*@lineinfo:658^13*/

//  ************************************************************
//  #sql cursor = { select author_name
//                                  from Book_author
//                                  where call_number = :callNumber
//                             };
//  ************************************************************

{
  sqlj.runtime.ConnectionContext __sJT_connCtx = sqlj.runtime.ref.DefaultContext.getDefaultContext();
  if (__sJT_connCtx == null) sqlj.runtime.error.RuntimeRefErrors.raise_NULL_DEFAULT_CONN_CTX();
  sqlj.runtime.ExecutionContext __sJT_execCtx = __sJT_connCtx.getExecutionContext();
  if (__sJT_execCtx == null) sqlj.runtime.error.RuntimeRefErrors.raise_NULL_EXEC_CTX();
  synchronized (__sJT_execCtx) {
    sqlj.runtime.profile.RTStatement __sJT_stmt = __sJT_execCtx.registerStatement(__sJT_connCtx, Database_SJProfileKeys.getKey(0), 31);
    try 
    {
      __sJT_stmt.setString(1, callNumber);
      cursor = new BookInfoCursor(__sJT_execCtx.executeQuery(), __sJT_execCtx.getFetchSize(), __sJT_execCtx.getMaxRows());
    }
    finally 
    {
      __sJT_execCtx.releaseStatement();
    }
  }
}


//  ************************************************************

/*@lineinfo:user-code*//*@lineinfo:661^27*/

            String author = null;

            while(true)
            {
                /*@lineinfo:generated-code*//*@lineinfo:667^17*/

//  ************************************************************
//  #sql { fetch :cursor  INTO :author, 
//                        };
//  ************************************************************

{
  if (cursor.next())
  {
    author = cursor.getCol1();
  }
}


//  ************************************************************

/*@lineinfo:user-code*//*@lineinfo:668^22*/

                if (cursor.endFetch()) break;

                originalAuthors.add(author);
            }

            values[4] = originalAuthors;

            /*@lineinfo:generated-code*//*@lineinfo:677^13*/

//  ************************************************************
//  #sql cursor = { select keyword
//                                  from Book_keyword
//                                  where call_number = :callNumber
//                             };
//  ************************************************************

{
  sqlj.runtime.ConnectionContext __sJT_connCtx = sqlj.runtime.ref.DefaultContext.getDefaultContext();
  if (__sJT_connCtx == null) sqlj.runtime.error.RuntimeRefErrors.raise_NULL_DEFAULT_CONN_CTX();
  sqlj.runtime.ExecutionContext __sJT_execCtx = __sJT_connCtx.getExecutionContext();
  if (__sJT_execCtx == null) sqlj.runtime.error.RuntimeRefErrors.raise_NULL_EXEC_CTX();
  synchronized (__sJT_execCtx) {
    sqlj.runtime.profile.RTStatement __sJT_stmt = __sJT_execCtx.registerStatement(__sJT_connCtx, Database_SJProfileKeys.getKey(0), 32);
    try 
    {
      __sJT_stmt.setString(1, callNumber);
      cursor = new BookInfoCursor(__sJT_execCtx.executeQuery(), __sJT_execCtx.getFetchSize(), __sJT_execCtx.getMaxRows());
    }
    finally 
    {
      __sJT_execCtx.releaseStatement();
    }
  }
}


//  ************************************************************

/*@lineinfo:user-code*//*@lineinfo:680^27*/

            String keyword = null;

            while(true)
            {
                /*@lineinfo:generated-code*//*@lineinfo:686^17*/

//  ************************************************************
//  #sql { fetch :cursor  INTO :keyword, 
//                        };
//  ************************************************************

{
  if (cursor.next())
  {
    keyword = cursor.getCol1();
  }
}


//  ************************************************************

/*@lineinfo:user-code*//*@lineinfo:687^22*/

                if (cursor.endFetch()) break;

                originalKeywords.add(keyword);
            }

            values[6] = originalKeywords;
        }
        catch(SQLException e)
        {
            if (e.getSQLState().equals(NO_ROW_SQL_STATE))
                throw new ErrorMessage("No such book");
            else
                throw new ErrorMessage("Unexpected SQL error " + e.getMessage());
        }
        finally
        {
            rollback();
        }

        return values;
    }

    /** Delete a book
     *
     *  @param callNumber the call number of the book to delete
     *  @param copyNumber the copy number of the book to delete
     *
     *  @exception an ErrorMessage is thrown with an appropriate message if
     *          the operation fails.  Any failure results in no changes being
     *          made to the database
     */
    public void deleteBook(String callNumber, short copyNumber) throws ErrorMessage
    {
        try
        {
            /*@lineinfo:generated-code*//*@lineinfo:724^13*/

//  ************************************************************
//  #sql { delete from Book
//                          where call_number = :callNumber and
//                                copy_number = :copyNumber
//                    };
//  ************************************************************

{
  sqlj.runtime.ConnectionContext __sJT_connCtx = sqlj.runtime.ref.DefaultContext.getDefaultContext();
  if (__sJT_connCtx == null) sqlj.runtime.error.RuntimeRefErrors.raise_NULL_DEFAULT_CONN_CTX();
  sqlj.runtime.ExecutionContext __sJT_execCtx = __sJT_connCtx.getExecutionContext();
  if (__sJT_execCtx == null) sqlj.runtime.error.RuntimeRefErrors.raise_NULL_EXEC_CTX();
  synchronized (__sJT_execCtx) {
    sqlj.runtime.profile.RTStatement __sJT_stmt = __sJT_execCtx.registerStatement(__sJT_connCtx, Database_SJProfileKeys.getKey(0), 33);
    try 
    {
      __sJT_stmt.setString(1, callNumber);
      __sJT_stmt.setShort(2, copyNumber);
      __sJT_execCtx.executeUpdate();
    }
    finally 
    {
      __sJT_execCtx.releaseStatement();
    }
  }
}


//  ************************************************************

/*@lineinfo:user-code*//*@lineinfo:727^18*/

            /*@lineinfo:generated-code*//*@lineinfo:729^13*/

//  ************************************************************
//  #sql { commit  };
//  ************************************************************

{
  sqlj.runtime.ConnectionContext __sJT_connCtx = sqlj.runtime.ref.DefaultContext.getDefaultContext();
  if (__sJT_connCtx == null) sqlj.runtime.error.RuntimeRefErrors.raise_NULL_DEFAULT_CONN_CTX();
  sqlj.runtime.ExecutionContext __sJT_execCtx = __sJT_connCtx.getExecutionContext();
  if (__sJT_execCtx == null) sqlj.runtime.error.RuntimeRefErrors.raise_NULL_EXEC_CTX();
  synchronized (__sJT_execCtx) {
    sqlj.runtime.profile.RTStatement __sJT_stmt = __sJT_execCtx.registerStatement(__sJT_connCtx, Database_SJProfileKeys.getKey(0), 34);
    try 
    {
      __sJT_execCtx.executeUpdate();
    }
    finally 
    {
      __sJT_execCtx.releaseStatement();
    }
  }
}


//  ************************************************************

/*@lineinfo:user-code*//*@lineinfo:729^27*/
        }
        catch(SQLException e)
        {
            rollback();
            if (e.getSQLState().equals(NO_ROW_SQL_STATE))
                throw new ErrorMessage("No such book");
            else
                throw new ErrorMessage("Unexpected SQL error " + e.getMessage());
        }
    }

    /** Add a new category to the database.
     *
     *  @param categoryName the name of the category to add
     *  @param checkoutPeriod the period borrowers in this category can check
     *         books out for
     *  @param maxBooksOut the maximum number of books borrowers in this category
     *         can have out
     *
     *  @exception an ErrorMessage is thrown with an appropriate message if
     *          the operation fails.  Any failure results in no changes being
     *          made to the database
     */
    public void addCategory(String categoryName,
                            int checkoutPeriod,
                            int maxBooksOut) throws ErrorMessage
    {
        try
        {
            /*@lineinfo:generated-code*//*@lineinfo:759^13*/

//  ************************************************************
//  #sql { insert into category
//                          values (:categoryName, :checkoutPeriod, :maxBooksOut)
//                    };
//  ************************************************************

{
  sqlj.runtime.ConnectionContext __sJT_connCtx = sqlj.runtime.ref.DefaultContext.getDefaultContext();
  if (__sJT_connCtx == null) sqlj.runtime.error.RuntimeRefErrors.raise_NULL_DEFAULT_CONN_CTX();
  sqlj.runtime.ExecutionContext __sJT_execCtx = __sJT_connCtx.getExecutionContext();
  if (__sJT_execCtx == null) sqlj.runtime.error.RuntimeRefErrors.raise_NULL_EXEC_CTX();
  synchronized (__sJT_execCtx) {
    sqlj.runtime.profile.RTStatement __sJT_stmt = __sJT_execCtx.registerStatement(__sJT_connCtx, Database_SJProfileKeys.getKey(0), 35);
    try 
    {
      __sJT_stmt.setString(1, categoryName);
      __sJT_stmt.setInt(2, checkoutPeriod);
      __sJT_stmt.setInt(3, maxBooksOut);
      __sJT_execCtx.executeUpdate();
    }
    finally 
    {
      __sJT_execCtx.releaseStatement();
    }
  }
}


//  ************************************************************

/*@lineinfo:user-code*//*@lineinfo:761^18*/
            /*@lineinfo:generated-code*//*@lineinfo:762^13*/

//  ************************************************************
//  #sql { commit  };
//  ************************************************************

{
  sqlj.runtime.ConnectionContext __sJT_connCtx = sqlj.runtime.ref.DefaultContext.getDefaultContext();
  if (__sJT_connCtx == null) sqlj.runtime.error.RuntimeRefErrors.raise_NULL_DEFAULT_CONN_CTX();
  sqlj.runtime.ExecutionContext __sJT_execCtx = __sJT_connCtx.getExecutionContext();
  if (__sJT_execCtx == null) sqlj.runtime.error.RuntimeRefErrors.raise_NULL_EXEC_CTX();
  synchronized (__sJT_execCtx) {
    sqlj.runtime.profile.RTStatement __sJT_stmt = __sJT_execCtx.registerStatement(__sJT_connCtx, Database_SJProfileKeys.getKey(0), 36);
    try 
    {
      __sJT_execCtx.executeUpdate();
    }
    finally 
    {
      __sJT_execCtx.releaseStatement();
    }
  }
}


//  ************************************************************

/*@lineinfo:user-code*//*@lineinfo:762^27*/
        }
        catch(SQLException e)
        {
            rollback();
            if (e.getErrorCode() == DUPLICATE_KEY_SQL_ERROR)
                throw new ErrorMessage(
                    "Category name is the same as an existing category");
            else
                throw new ErrorMessage("Unexpected SQL error " + e.getMessage());
        }
    }

    /** Get information on an existing category about to be edited or deleted
     *
     *  @param categoryName the name of the category
     *  @return values recorded in the database for this category - an array
     *          of strings.
     *
     *  @exception an ErrorMessage is thrown with an appropriate message if
     *          the category does not exist
     */
    public String[] getCategoryInformation(String categoryName) throws ErrorMessage
    {
        String [] values = new String[3];
        values[0] = categoryName;
        int checkoutPeriod, maxBooksOut;
        try
        {
            /*@lineinfo:generated-code*//*@lineinfo:791^13*/

//  ************************************************************
//  #sql { select checkout_period, max_books_out
//                           INTO :checkoutPeriod, :maxBooksOut, 
//                          from category
//                          where category_name = :categoryName
//                    };
//  ************************************************************

{
  sqlj.runtime.profile.RTResultSet __sJT_rtRs;
  sqlj.runtime.ConnectionContext __sJT_connCtx = sqlj.runtime.ref.DefaultContext.getDefaultContext();
  if (__sJT_connCtx == null) sqlj.runtime.error.RuntimeRefErrors.raise_NULL_DEFAULT_CONN_CTX();
  sqlj.runtime.ExecutionContext __sJT_execCtx = __sJT_connCtx.getExecutionContext();
  if (__sJT_execCtx == null) sqlj.runtime.error.RuntimeRefErrors.raise_NULL_EXEC_CTX();
  synchronized (__sJT_execCtx) {
    sqlj.runtime.profile.RTStatement __sJT_stmt = __sJT_execCtx.registerStatement(__sJT_connCtx, Database_SJProfileKeys.getKey(0), 37);
    try 
    {
      __sJT_stmt.setString(1, categoryName);
      __sJT_rtRs = __sJT_execCtx.executeQuery();
    }
    finally 
    {
      __sJT_execCtx.releaseStatement();
    }
  }
  try 
  {
    sqlj.runtime.ref.ResultSetIterImpl.checkColumns(__sJT_rtRs, 2);
    if (!__sJT_rtRs.next())
    {
      sqlj.runtime.error.RuntimeRefErrors.raise_NO_ROW_SELECT_INTO();
    }
    checkoutPeriod = __sJT_rtRs.getIntNoNull(1);
    maxBooksOut = __sJT_rtRs.getIntNoNull(2);
    if (__sJT_rtRs.next())
    {
      sqlj.runtime.error.RuntimeRefErrors.raise_MULTI_ROW_SELECT_INTO();
    }
  }
  finally 
  {
    __sJT_rtRs.close();
  }
}


//  ************************************************************

/*@lineinfo:user-code*//*@lineinfo:795^18*/
            values[1] = "" + checkoutPeriod;
            values[2] = "" + maxBooksOut;
            return values;
        }
        catch(SQLException e)
        {
            if (e.getSQLState().equals(NO_ROW_SQL_STATE))
                throw new ErrorMessage("No such category");
            else
                throw new ErrorMessage("Unexpected SQL error " + e.getMessage());
        }
        finally
        {
            rollback();
        }
    }

    /** Update stored information about a category
     *
     *  @param categoryName the name of the category
     *  @param newCheckoutPeriod the new value of checkoutPeriod (may be the same
     *         as original)
     *  @param newMaxBooksOut the new value of maxBooksOut (may be the same as original)
     *
     *  @exception an ErrorMessage is thrown with an appropriate message if
     *          the operation fails.  Any failure results in no changes being
     *          made to the database
     */
    public void updateCategory(String categoryName,
                               int newCheckoutPeriod,
                               int newMaxBooksOut) throws ErrorMessage
    {
        try
        {
            /*@lineinfo:generated-code*//*@lineinfo:830^13*/

//  ************************************************************
//  #sql { update category
//                          set checkout_period = :newCheckoutPeriod,
//                              max_books_out = :newMaxBooksOut
//                          where category_name = :categoryName
//                    };
//  ************************************************************

{
  sqlj.runtime.ConnectionContext __sJT_connCtx = sqlj.runtime.ref.DefaultContext.getDefaultContext();
  if (__sJT_connCtx == null) sqlj.runtime.error.RuntimeRefErrors.raise_NULL_DEFAULT_CONN_CTX();
  sqlj.runtime.ExecutionContext __sJT_execCtx = __sJT_connCtx.getExecutionContext();
  if (__sJT_execCtx == null) sqlj.runtime.error.RuntimeRefErrors.raise_NULL_EXEC_CTX();
  synchronized (__sJT_execCtx) {
    sqlj.runtime.profile.RTStatement __sJT_stmt = __sJT_execCtx.registerStatement(__sJT_connCtx, Database_SJProfileKeys.getKey(0), 38);
    try 
    {
      __sJT_stmt.setInt(1, newCheckoutPeriod);
      __sJT_stmt.setInt(2, newMaxBooksOut);
      __sJT_stmt.setString(3, categoryName);
      __sJT_execCtx.executeUpdate();
    }
    finally 
    {
      __sJT_execCtx.releaseStatement();
    }
  }
}


//  ************************************************************

/*@lineinfo:user-code*//*@lineinfo:834^18*/
            /*@lineinfo:generated-code*//*@lineinfo:835^13*/

//  ************************************************************
//  #sql { commit  };
//  ************************************************************

{
  sqlj.runtime.ConnectionContext __sJT_connCtx = sqlj.runtime.ref.DefaultContext.getDefaultContext();
  if (__sJT_connCtx == null) sqlj.runtime.error.RuntimeRefErrors.raise_NULL_DEFAULT_CONN_CTX();
  sqlj.runtime.ExecutionContext __sJT_execCtx = __sJT_connCtx.getExecutionContext();
  if (__sJT_execCtx == null) sqlj.runtime.error.RuntimeRefErrors.raise_NULL_EXEC_CTX();
  synchronized (__sJT_execCtx) {
    sqlj.runtime.profile.RTStatement __sJT_stmt = __sJT_execCtx.registerStatement(__sJT_connCtx, Database_SJProfileKeys.getKey(0), 39);
    try 
    {
      __sJT_execCtx.executeUpdate();
    }
    finally 
    {
      __sJT_execCtx.releaseStatement();
    }
  }
}


//  ************************************************************

/*@lineinfo:user-code*//*@lineinfo:835^27*/
        }
        catch(SQLException e)
        {
            rollback();
            // Since we found the row when initiating the operation, an error
            // now is always unexpected
            throw new ErrorMessage("Unexpected SQL error " + e.getMessage());
        }
    }

    /** Delete a category
     *
     *  @param categoryName the name of the category to delete
     *
     *  @exception an ErrorMessage is thrown with an appropriate message if
     *          the operation fails.  Any failure results in no changes being
     *          made to the database
     */
    public void deleteCategory(String categoryName) throws ErrorMessage
    {
        try
        {
            /*@lineinfo:generated-code*//*@lineinfo:858^13*/

//  ************************************************************
//  #sql { delete from category
//                          where category_name = :categoryName
//                    };
//  ************************************************************

{
  sqlj.runtime.ConnectionContext __sJT_connCtx = sqlj.runtime.ref.DefaultContext.getDefaultContext();
  if (__sJT_connCtx == null) sqlj.runtime.error.RuntimeRefErrors.raise_NULL_DEFAULT_CONN_CTX();
  sqlj.runtime.ExecutionContext __sJT_execCtx = __sJT_connCtx.getExecutionContext();
  if (__sJT_execCtx == null) sqlj.runtime.error.RuntimeRefErrors.raise_NULL_EXEC_CTX();
  synchronized (__sJT_execCtx) {
    sqlj.runtime.profile.RTStatement __sJT_stmt = __sJT_execCtx.registerStatement(__sJT_connCtx, Database_SJProfileKeys.getKey(0), 40);
    try 
    {
      __sJT_stmt.setString(1, categoryName);
      __sJT_execCtx.executeUpdate();
    }
    finally 
    {
      __sJT_execCtx.releaseStatement();
    }
  }
}


//  ************************************************************

/*@lineinfo:user-code*//*@lineinfo:860^18*/
            /*@lineinfo:generated-code*//*@lineinfo:861^13*/

//  ************************************************************
//  #sql { commit  };
//  ************************************************************

{
  sqlj.runtime.ConnectionContext __sJT_connCtx = sqlj.runtime.ref.DefaultContext.getDefaultContext();
  if (__sJT_connCtx == null) sqlj.runtime.error.RuntimeRefErrors.raise_NULL_DEFAULT_CONN_CTX();
  sqlj.runtime.ExecutionContext __sJT_execCtx = __sJT_connCtx.getExecutionContext();
  if (__sJT_execCtx == null) sqlj.runtime.error.RuntimeRefErrors.raise_NULL_EXEC_CTX();
  synchronized (__sJT_execCtx) {
    sqlj.runtime.profile.RTStatement __sJT_stmt = __sJT_execCtx.registerStatement(__sJT_connCtx, Database_SJProfileKeys.getKey(0), 41);
    try 
    {
      __sJT_execCtx.executeUpdate();
    }
    finally 
    {
      __sJT_execCtx.releaseStatement();
    }
  }
}


//  ************************************************************

/*@lineinfo:user-code*//*@lineinfo:861^27*/
        }
        catch(SQLException e)
        {
            rollback();
            if (e.getMessage().indexOf(BORROWER_CATEGORY_FOREIGN_CONSTRAINT) >= 0)
                throw new ErrorMessage("There are still borrowers in this category");
            else
                throw new ErrorMessage("Unexpected SQL error " + e.getMessage());
        }
    }

    /** Add a new borrower to the database.
     *
     *  @param borrowerID the ID for the borrower
     *  @param lastName the borrower's last name
     *  @param firstName the borrower's first name
     *  @param phones the borrower's phone number(s) - a List of strings, each of
     *         which represents one phone number
     *  @param category the name of the category to which the borrower will belong
     *
     *  @exception an ErrorMessage is thrown with an appropriate message if
     *          the operation fails.  Any failure results in no changes being
     *          made to the database
     */
    public void addBorrower(String borrowerID,
                            String lastName,
                            String firstName,
                            List phones,
                            String category) throws ErrorMessage
    {
        try {
            /*@lineinfo:generated-code*//*@lineinfo:893^13*/

//  ************************************************************
//  #sql { insert into borrower
//                  values(:borrowerID, :lastName, :firstName, :category)
//               };
//  ************************************************************

{
  sqlj.runtime.ConnectionContext __sJT_connCtx = sqlj.runtime.ref.DefaultContext.getDefaultContext();
  if (__sJT_connCtx == null) sqlj.runtime.error.RuntimeRefErrors.raise_NULL_DEFAULT_CONN_CTX();
  sqlj.runtime.ExecutionContext __sJT_execCtx = __sJT_connCtx.getExecutionContext();
  if (__sJT_execCtx == null) sqlj.runtime.error.RuntimeRefErrors.raise_NULL_EXEC_CTX();
  synchronized (__sJT_execCtx) {
    sqlj.runtime.profile.RTStatement __sJT_stmt = __sJT_execCtx.registerStatement(__sJT_connCtx, Database_SJProfileKeys.getKey(0), 42);
    try 
    {
      __sJT_stmt.setString(1, borrowerID);
      __sJT_stmt.setString(2, lastName);
      __sJT_stmt.setString(3, firstName);
      __sJT_stmt.setString(4, category);
      __sJT_execCtx.executeUpdate();
    }
    finally 
    {
      __sJT_execCtx.releaseStatement();
    }
  }
}


//  ************************************************************

/*@lineinfo:user-code*//*@lineinfo:895^13*/
            for (int i = 0; i < phones.size(); i++) {
                String number = phones.get(i).toString();
                /*@lineinfo:generated-code*//*@lineinfo:898^17*/

//  ************************************************************
//  #sql { insert into borrower_phone
//                      values(:borrowerID, :number)
//                   };
//  ************************************************************

{
  sqlj.runtime.ConnectionContext __sJT_connCtx = sqlj.runtime.ref.DefaultContext.getDefaultContext();
  if (__sJT_connCtx == null) sqlj.runtime.error.RuntimeRefErrors.raise_NULL_DEFAULT_CONN_CTX();
  sqlj.runtime.ExecutionContext __sJT_execCtx = __sJT_connCtx.getExecutionContext();
  if (__sJT_execCtx == null) sqlj.runtime.error.RuntimeRefErrors.raise_NULL_EXEC_CTX();
  synchronized (__sJT_execCtx) {
    sqlj.runtime.profile.RTStatement __sJT_stmt = __sJT_execCtx.registerStatement(__sJT_connCtx, Database_SJProfileKeys.getKey(0), 43);
    try 
    {
      __sJT_stmt.setString(1, borrowerID);
      __sJT_stmt.setString(2, number);
      __sJT_execCtx.executeUpdate();
    }
    finally 
    {
      __sJT_execCtx.releaseStatement();
    }
  }
}


//  ************************************************************

/*@lineinfo:user-code*//*@lineinfo:900^17*/
            }
            /*@lineinfo:generated-code*//*@lineinfo:902^13*/

//  ************************************************************
//  #sql { commit  };
//  ************************************************************

{
  sqlj.runtime.ConnectionContext __sJT_connCtx = sqlj.runtime.ref.DefaultContext.getDefaultContext();
  if (__sJT_connCtx == null) sqlj.runtime.error.RuntimeRefErrors.raise_NULL_DEFAULT_CONN_CTX();
  sqlj.runtime.ExecutionContext __sJT_execCtx = __sJT_connCtx.getExecutionContext();
  if (__sJT_execCtx == null) sqlj.runtime.error.RuntimeRefErrors.raise_NULL_EXEC_CTX();
  synchronized (__sJT_execCtx) {
    sqlj.runtime.profile.RTStatement __sJT_stmt = __sJT_execCtx.registerStatement(__sJT_connCtx, Database_SJProfileKeys.getKey(0), 44);
    try 
    {
      __sJT_execCtx.executeUpdate();
    }
    finally 
    {
      __sJT_execCtx.releaseStatement();
    }
  }
}


//  ************************************************************

/*@lineinfo:user-code*//*@lineinfo:902^27*/
        }
        catch(SQLException e) {
            rollback();
            if (e.getSQLState().equals(DUPLICATE_KEY_SQL_ERROR))
                throw new ErrorMessage("Borrower already exists");
            else
                throw new ErrorMessage("Unexpected SQL error " + e.getMessage());
        }
    }

    /** Get information on an existing borrower about to be edited or deleted
     *
     *  @param borrowerID the borrower ID of the borrower
     *  @return values recorded in the database for this borrower.  A single
     *          valued attribute has its value represented as a string; a
     *          multi-valued attribute has its value represented as a
     *          List whose elements are strings representing the various values
     *
     *  @exception an ErrorMessage is thrown with an appropriate message if
     *          the borrower does not exist
     */
    /*@lineinfo:generated-code*//*@lineinfo:924^5*/

//  ************************************************************
//  SQLJ iterator declaration:
//  ************************************************************

class PhoneCursor 
extends sqlj.runtime.ref.ResultSetIterImpl
implements sqlj.runtime.PositionedIterator
{
  public PhoneCursor(sqlj.runtime.profile.RTResultSet resultSet) 
    throws java.sql.SQLException 
  {
    super(resultSet, 1);
  }
  public PhoneCursor(sqlj.runtime.profile.RTResultSet resultSet, int fetchSize, int maxRows) 
    throws java.sql.SQLException 
  {
    super(resultSet, fetchSize, maxRows, 1);
  }
  public String getCol1() 
    throws java.sql.SQLException 
  {
    return resultSet.getString(1);
  }
}


//  ************************************************************

/*@lineinfo:user-code*//*@lineinfo:924^37*/

    public Object [] getBorrowerInformation(String borrowerID) throws ErrorMessage
    {
        // List originalPhones = new ArrayList();
        // try {
        //     PhoneCursor cursor;
        //     List result = new ArrayList();
        //     String lastName, firstName, categoryName, phoneNumber;
        //     Object [] values = new Object[5];
        //     values[0] = borrowerID;
        //     #sql cursor = { select phone
        //         from borrower_phone
        //         where borrower_id = :borrowerID
        //     };
        //     while(true) {
        //         #sql { fetch :cursor
        //             into :phoneNumber
        //         };
        //         if (cursor.endFetch())
        //             break;
        //         originalPhones.add(phoneNumber);
        //     }
        //     cursor.close();
        //     #sql { select last_name, first_name, category_name
        //         into :lastName, :firstName, :categoryName
        //         from borrower
        //         where borrower_id = :borrowerID
        //     };
        //     values[1] = borrowerID;
        //     values[2] = lastName;
        //     values[3] = firstName;
        //     values[4] = originalPhones;
        //     values[5] = categoryName;
        //     return values;
        // }
        // catch(SQLException e) {
        //     throw new ErrorMessage("Unexcepted SQL error " + e.getMessage());
        // }
        // finally {
        //     rollback();
        // }
        return new Object[5];
    }

    /** Update stored information about a borrower
     *
     *  @param borrowerID the borrower ID of the borrower
     *  @param newLastName the new value of lastName (may be the same as original)
     *  @param newFirstName the new value of firstName (may be the same as original)
     *  @param originalPhones the original list of phone numbers for the borrower
     *         This will be a list of Strings, each one phone number
     *  @param newPhones the new list of phone numbers (may be the same as old)
     *         This will be a list of Strings, each one phone number
     *  @param newCategory the new value of category (may be the same as old)
     *
     *  @exception an ErrorMessage is thrown with an appropriate message if
     *          the operation fails.  Any failure results in no changes being
     *          made to the database
     */
    public void updateBorrower(String borrowerID,
                               String newLastName,
                               String newFirstName,
                               List originalPhones,
                               List newPhones,
                               String newCategory) throws ErrorMessage
    {
        // try {
        //     #sql { update borrower
        //         set last_name = :newLastName,
        //             first_name = :newFirstName,
        //             category_name = :newCategory
        //         where borrower_id = :borrowerID
        //     };
        //     for (int i = 0; i < originalPhones.size(); i ++) {
        //         String phoneNumber = originalPhones.get(i);
        //         #sql { delete form borrower_phone
        //             where phone = :phoneNumber and borrower_id = :borrowerID
        //         };
        //     }
        //     for (int i = 0; i < newPhones.size(); i ++) {
        //         String phoneNumber = newPhones.get(i);
        //         #sql { insert into borrowerID
        //             values(:borrowerID, :phoneNumber
        //         };
        //     }
        //     #sql { commit };
        // }
        // catch(SQLException e) {
        //     rollback();
        //     throw new ErrorMessage("Unexcepted SQL error " + e.getMessage());
        // }
    }

    /** Delete a borrower
     *
     *  @param borrowerID the ID of the borrower to delete
     *
     *  @exception an ErrorMessage is thrown with an appropriate message if
     *          the operation fails.  Any failure results in no changes being
     *          made to the database
     */
    public void deleteBorrower(String borrowerID) throws ErrorMessage
    {
        try {
            /*@lineinfo:generated-code*//*@lineinfo:1029^13*/

//  ************************************************************
//  #sql { delete from borrower
//                  where borrower_id = :borrowerID
//               };
//  ************************************************************

{
  sqlj.runtime.ConnectionContext __sJT_connCtx = sqlj.runtime.ref.DefaultContext.getDefaultContext();
  if (__sJT_connCtx == null) sqlj.runtime.error.RuntimeRefErrors.raise_NULL_DEFAULT_CONN_CTX();
  sqlj.runtime.ExecutionContext __sJT_execCtx = __sJT_connCtx.getExecutionContext();
  if (__sJT_execCtx == null) sqlj.runtime.error.RuntimeRefErrors.raise_NULL_EXEC_CTX();
  synchronized (__sJT_execCtx) {
    sqlj.runtime.profile.RTStatement __sJT_stmt = __sJT_execCtx.registerStatement(__sJT_connCtx, Database_SJProfileKeys.getKey(0), 45);
    try 
    {
      __sJT_stmt.setString(1, borrowerID);
      __sJT_execCtx.executeUpdate();
    }
    finally 
    {
      __sJT_execCtx.releaseStatement();
    }
  }
}


//  ************************************************************

/*@lineinfo:user-code*//*@lineinfo:1031^13*/
            /*@lineinfo:generated-code*//*@lineinfo:1032^13*/

//  ************************************************************
//  #sql { commit  };
//  ************************************************************

{
  sqlj.runtime.ConnectionContext __sJT_connCtx = sqlj.runtime.ref.DefaultContext.getDefaultContext();
  if (__sJT_connCtx == null) sqlj.runtime.error.RuntimeRefErrors.raise_NULL_DEFAULT_CONN_CTX();
  sqlj.runtime.ExecutionContext __sJT_execCtx = __sJT_connCtx.getExecutionContext();
  if (__sJT_execCtx == null) sqlj.runtime.error.RuntimeRefErrors.raise_NULL_EXEC_CTX();
  synchronized (__sJT_execCtx) {
    sqlj.runtime.profile.RTStatement __sJT_stmt = __sJT_execCtx.registerStatement(__sJT_connCtx, Database_SJProfileKeys.getKey(0), 46);
    try 
    {
      __sJT_execCtx.executeUpdate();
    }
    finally 
    {
      __sJT_execCtx.releaseStatement();
    }
  }
}


//  ************************************************************

/*@lineinfo:user-code*//*@lineinfo:1032^27*/
        }
        catch(SQLException e) {
            rollback();
            throw new ErrorMessage("Unexpected SQL error " + e.getMessage());
        }
    }

    /* ************************************************************************
     *  Methods called to manage fines
     * ***********************************************************************/

    /** Get a list of fines owed by a particular borrower
     *
     *  @param borrowerID the borrower whose fines are wanted
     *  @return a list of Fine objects - one for each fine the borrower owes
     *
     *  @exception an ErrorMessage is thrown with an appropriate message if
     *          there is no such borrower, or the borrower has no fines.
     */

    /*@lineinfo:generated-code*//*@lineinfo:1053^5*/

//  ************************************************************
//  SQLJ iterator declaration:
//  ************************************************************

class FineCursor 
extends sqlj.runtime.ref.ResultSetIterImpl
implements sqlj.runtime.PositionedIterator
{
  public FineCursor(sqlj.runtime.profile.RTResultSet resultSet) 
    throws java.sql.SQLException 
  {
    super(resultSet, 4);
  }
  public FineCursor(sqlj.runtime.profile.RTResultSet resultSet, int fetchSize, int maxRows) 
    throws java.sql.SQLException 
  {
    super(resultSet, fetchSize, maxRows, 4);
  }
  public String getCol1() 
    throws java.sql.SQLException 
  {
    return resultSet.getString(1);
  }
  public Date getCol2() 
    throws java.sql.SQLException 
  {
    return resultSet.getDate(2);
  }
  public Date getCol3() 
    throws java.sql.SQLException 
  {
    return resultSet.getDate(3);
  }
  public BigDecimal getCol4() 
    throws java.sql.SQLException 
  {
    return resultSet.getBigDecimal(4);
  }
}


//  ************************************************************

/*@lineinfo:user-code*//*@lineinfo:1053^60*/

    public List getFines(String borrowerID) throws ErrorMessage
    {
        List result = new ArrayList();

        try
        {
            FineCursor cursor;

            // The compiler doesn't catch that the fetch below initializes
            // these variables, so give them default values now to keep the
            // compiler happy

            String title = null;
            Date dateDue = null;
            Date dateReturned = null;
            BigDecimal amount = null;

            /*@lineinfo:generated-code*//*@lineinfo:1072^13*/

//  ************************************************************
//  #sql cursor = { select title, date_due, date_returned, amount
//                                  from fine
//                                  where borrower_id = :borrowerID
//                                  order by date_returned
//                             };
//  ************************************************************

{
  sqlj.runtime.ConnectionContext __sJT_connCtx = sqlj.runtime.ref.DefaultContext.getDefaultContext();
  if (__sJT_connCtx == null) sqlj.runtime.error.RuntimeRefErrors.raise_NULL_DEFAULT_CONN_CTX();
  sqlj.runtime.ExecutionContext __sJT_execCtx = __sJT_connCtx.getExecutionContext();
  if (__sJT_execCtx == null) sqlj.runtime.error.RuntimeRefErrors.raise_NULL_EXEC_CTX();
  synchronized (__sJT_execCtx) {
    sqlj.runtime.profile.RTStatement __sJT_stmt = __sJT_execCtx.registerStatement(__sJT_connCtx, Database_SJProfileKeys.getKey(0), 47);
    try 
    {
      __sJT_stmt.setString(1, borrowerID);
      cursor = new FineCursor(__sJT_execCtx.executeQuery(), __sJT_execCtx.getFetchSize(), __sJT_execCtx.getMaxRows());
    }
    finally 
    {
      __sJT_execCtx.releaseStatement();
    }
  }
}


//  ************************************************************

/*@lineinfo:user-code*//*@lineinfo:1076^27*/

            while(true)
            {
                /*@lineinfo:generated-code*//*@lineinfo:1080^17*/

//  ************************************************************
//  #sql { fetch :cursor  INTO :title, :dateDue, :dateReturned, :amount, 
//                        };
//  ************************************************************

{
  if (cursor.next())
  {
    title = cursor.getCol1();
    dateDue = cursor.getCol2();
    dateReturned = cursor.getCol3();
    amount = cursor.getCol4();
  }
}


//  ************************************************************

/*@lineinfo:user-code*//*@lineinfo:1084^22*/

                if (cursor.endFetch()) break;

                Fine fine = new Fine(title, dateDue, dateReturned, amount);

                result.add(fine);
            }
            cursor.close();
        }
        catch(SQLException e)
        {
            throw new ErrorMessage("Unexpected SQL error " + e.getMessage());
        }
        finally
        {
            rollback();
        }

        if (result.size() == 0)
        {
            // No fines found - either the borrower doesn't exist, or has no fines
            // The following determines which is the case

            boolean borrowerExists;

            String dummy = null;
            try
            {
                /*@lineinfo:generated-code*//*@lineinfo:1113^17*/

//  ************************************************************
//  #sql { select last_name  INTO :dummy, 
//                              from borrower
//                              where borrower_id = :borrowerID  };
//  ************************************************************

{
  sqlj.runtime.profile.RTResultSet __sJT_rtRs;
  sqlj.runtime.ConnectionContext __sJT_connCtx = sqlj.runtime.ref.DefaultContext.getDefaultContext();
  if (__sJT_connCtx == null) sqlj.runtime.error.RuntimeRefErrors.raise_NULL_DEFAULT_CONN_CTX();
  sqlj.runtime.ExecutionContext __sJT_execCtx = __sJT_connCtx.getExecutionContext();
  if (__sJT_execCtx == null) sqlj.runtime.error.RuntimeRefErrors.raise_NULL_EXEC_CTX();
  synchronized (__sJT_execCtx) {
    sqlj.runtime.profile.RTStatement __sJT_stmt = __sJT_execCtx.registerStatement(__sJT_connCtx, Database_SJProfileKeys.getKey(0), 48);
    try 
    {
      __sJT_stmt.setString(1, borrowerID);
      __sJT_rtRs = __sJT_execCtx.executeQuery();
    }
    finally 
    {
      __sJT_execCtx.releaseStatement();
    }
  }
  try 
  {
    sqlj.runtime.ref.ResultSetIterImpl.checkColumns(__sJT_rtRs, 1);
    if (!__sJT_rtRs.next())
    {
      sqlj.runtime.error.RuntimeRefErrors.raise_NO_ROW_SELECT_INTO();
    }
    dummy = __sJT_rtRs.getString(1);
    if (__sJT_rtRs.next())
    {
      sqlj.runtime.error.RuntimeRefErrors.raise_MULTI_ROW_SELECT_INTO();
    }
  }
  finally 
  {
    __sJT_rtRs.close();
  }
}


//  ************************************************************

/*@lineinfo:user-code*//*@lineinfo:1115^62*/
                borrowerExists = true;
            }
            catch(SQLException e)
            {
                // If the above statement failed because no row was found,
                // this means the borrower doesn't exist

                if (e.getSQLState().equals(NO_ROW_SQL_STATE))
                    borrowerExists = false;
                else
                    throw new ErrorMessage("Unexpected SQL error " + e.getMessage());
            }
            finally
            {
                rollback();
            }

            if (borrowerExists)
                throw new ErrorMessage("This borrower has no fines");
            else
                throw new ErrorMessage("No such borrower");
        }
        else
            return result;
    }

    /** Record fine as paid.
     *
     *  @param borrowerID the ID of the borrower owing the fines
     *  @param fine the fine to be recorded as paid
     *
     *  @exception an ErrorMessage is thrown if there is an unexpected error
     *             recording the payment of the fine.
     */
    public void payFine(String borrowerID, Fine fine) throws ErrorMessage
    {
        // try {
        //     #sql { delete from fine
        //         where borrower_id = :borrowerID and
        //             title = :Fine. and
        //             date_due = :Fine.dateDue and
        //             date_returned = :Fine.dateReturned and
        //             amount = :Fine.amount
        //     };
        //     #sql { commit };
        // }
        // catch(SQLException e) {
        //     rollback();
        //     throw new ErrorMessage("Unexpected SQL error " + e.getMessage());
        // }
    }

    /* ************************************************************************
     * Methods called to produce various kinds of report
     * ***********************************************************************/

    /** Produce a list of all books - call number, title, author(s), format,
     *  number of copies owned
     *
     *  @param stream the stream to which to print the report
     *
     *  @exception an ErrorMessage is thrown if there is an unexpected error
     *             accessing the database.
     */

    /*@lineinfo:generated-code*//*@lineinfo:1181^5*/

//  ************************************************************
//  SQLJ iterator declaration:
//  ************************************************************

class BooksReportCursor 
extends sqlj.runtime.ref.ResultSetIterImpl
implements sqlj.runtime.PositionedIterator
{
  public BooksReportCursor(sqlj.runtime.profile.RTResultSet resultSet) 
    throws java.sql.SQLException 
  {
    super(resultSet, 5);
  }
  public BooksReportCursor(sqlj.runtime.profile.RTResultSet resultSet, int fetchSize, int maxRows) 
    throws java.sql.SQLException 
  {
    super(resultSet, fetchSize, maxRows, 5);
  }
  public String getCol1() 
    throws java.sql.SQLException 
  {
    return resultSet.getString(1);
  }
  public String getCol2() 
    throws java.sql.SQLException 
  {
    return resultSet.getString(2);
  }
  public String getCol3() 
    throws java.sql.SQLException 
  {
    return resultSet.getString(3);
  }
  public String getCol4() 
    throws java.sql.SQLException 
  {
    return resultSet.getString(4);
  }
  public int getCol5() 
    throws java.sql.SQLException 
  {
    return resultSet.getIntNoNull(5);
  }
}


//  ************************************************************

/*@lineinfo:user-code*//*@lineinfo:1181^72*/

    public void booksReport(PrintStream stream) throws ErrorMessage
    {
        try
        {
            BooksReportCursor cursor;

            // Left joins are used to ensure we include Book_info even if there
            // is no author or copy recorded
            /*@lineinfo:generated-code*//*@lineinfo:1191^13*/

//  ************************************************************
//  #sql cursor = { select Book_info.call_number,
//                                     title,
//                                     author_name,
//                                     format,
//                                     count(copy_number)
//                                  from Book_info left join Book_author
//                                  on Book_info.call_number = Book_author.call_number
//                                  left join Book
//                                  on Book_info.call_number = Book.call_number
//                                  group by Book_info.call_number,
//                                           title,
//                                           author_name,
//                                           format
//                                  order by Book_info.call_number
//                              };
//  ************************************************************

{
  sqlj.runtime.ConnectionContext __sJT_connCtx = sqlj.runtime.ref.DefaultContext.getDefaultContext();
  if (__sJT_connCtx == null) sqlj.runtime.error.RuntimeRefErrors.raise_NULL_DEFAULT_CONN_CTX();
  sqlj.runtime.ExecutionContext __sJT_execCtx = __sJT_connCtx.getExecutionContext();
  if (__sJT_execCtx == null) sqlj.runtime.error.RuntimeRefErrors.raise_NULL_EXEC_CTX();
  synchronized (__sJT_execCtx) {
    sqlj.runtime.profile.RTStatement __sJT_stmt = __sJT_execCtx.registerStatement(__sJT_connCtx, Database_SJProfileKeys.getKey(0), 49);
    try 
    {
      cursor = new BooksReportCursor(__sJT_execCtx.executeQuery(), __sJT_execCtx.getFetchSize(), __sJT_execCtx.getMaxRows());
    }
    finally 
    {
      __sJT_execCtx.releaseStatement();
    }
  }
}


//  ************************************************************

/*@lineinfo:user-code*//*@lineinfo:1205^28*/

            String [] BOOKS_REPORT_HEADINGS =
                { "Call Number", "Title", "Author", "Format", "# copies" };
            int [] BOOKS_REPORT_COLUMN_WIDTHS =
                { 22, 52, 22, 4, 5 };
            boolean [] BOOKS_REPORT_REPEAT_COLUMNS =
                { true, true, false, true, true };

            produceReport(stream,
                          "Book Information Report",
                          BOOKS_REPORT_HEADINGS,
                          BOOKS_REPORT_COLUMN_WIDTHS,
                          BOOKS_REPORT_REPEAT_COLUMNS,
                          cursor.getResultSet());

            cursor.close();

        }
        catch(SQLException e)
        {
            throw new ErrorMessage("Unexpected SQL error " + e.getMessage());
        }
        finally
        {
            rollback();
        }
    }

    /** Produce a list of all copies of books - accession number, call number,
     *  copy number and title
     *
     *  @param stream the stream to which to print the report
     *
     *  @exception an ErrorMessage is thrown if there is an unexpected error
     *             accessing the database.
     */
     /*@lineinfo:generated-code*//*@lineinfo:1242^6*/

//  ************************************************************
//  SQLJ iterator declaration:
//  ************************************************************

class BookCopiesCursor 
extends sqlj.runtime.ref.ResultSetIterImpl
implements sqlj.runtime.PositionedIterator
{
  public BookCopiesCursor(sqlj.runtime.profile.RTResultSet resultSet) 
    throws java.sql.SQLException 
  {
    super(resultSet, 4);
  }
  public BookCopiesCursor(sqlj.runtime.profile.RTResultSet resultSet, int fetchSize, int maxRows) 
    throws java.sql.SQLException 
  {
    super(resultSet, fetchSize, maxRows, 4);
  }
  public int getCol1() 
    throws java.sql.SQLException 
  {
    return resultSet.getIntNoNull(1);
  }
  public String getCol2() 
    throws java.sql.SQLException 
  {
    return resultSet.getString(2);
  }
  public int getCol3() 
    throws java.sql.SQLException 
  {
    return resultSet.getIntNoNull(3);
  }
  public String getCol4() 
    throws java.sql.SQLException 
  {
    return resultSet.getString(4);
  }
}


//  ************************************************************

/*@lineinfo:user-code*//*@lineinfo:1242^61*/

    public void bookCopiesReport(PrintStream stream) throws ErrorMessage
    {
        try
        {
            BookCopiesCursor cursor;
            /*@lineinfo:generated-code*//*@lineinfo:1249^13*/

//  ************************************************************
//  #sql cursor = { select Book.barcode, Book.call_number,
//                  Book.copy_number, Book_info.title from Book join Book_info
//                  on Book.call_number = Book_info.call_number
//                  order by Book.barcode
//               };
//  ************************************************************

{
  sqlj.runtime.ConnectionContext __sJT_connCtx = sqlj.runtime.ref.DefaultContext.getDefaultContext();
  if (__sJT_connCtx == null) sqlj.runtime.error.RuntimeRefErrors.raise_NULL_DEFAULT_CONN_CTX();
  sqlj.runtime.ExecutionContext __sJT_execCtx = __sJT_connCtx.getExecutionContext();
  if (__sJT_execCtx == null) sqlj.runtime.error.RuntimeRefErrors.raise_NULL_EXEC_CTX();
  synchronized (__sJT_execCtx) {
    sqlj.runtime.profile.RTStatement __sJT_stmt = __sJT_execCtx.registerStatement(__sJT_connCtx, Database_SJProfileKeys.getKey(0), 50);
    try 
    {
      cursor = new BookCopiesCursor(__sJT_execCtx.executeQuery(), __sJT_execCtx.getFetchSize(), __sJT_execCtx.getMaxRows());
    }
    finally 
    {
      __sJT_execCtx.releaseStatement();
    }
  }
}


//  ************************************************************

/*@lineinfo:user-code*//*@lineinfo:1253^13*/

            String [] COPIES_REPORT_HEADINGS =
                {"Bar Code", "Call Number", "Copy #", "Title"};
            int [] COPIES_REPORT_COLUMN_WIDTHS =
                {10, 20, 5, 50};
            produceReport(stream,
                              "Book Copies Report",
                              COPIES_REPORT_HEADINGS,
                              COPIES_REPORT_COLUMN_WIDTHS,
                              null,
                              cursor.getResultSet());
            cursor.close();
        }
        catch(SQLException e)
        {
            throw new ErrorMessage ("Unexpected SQL error " + e.getMessage());
        }
        finally
        {
            rollback();
        }
    }

    /** Produce a keywords report - keywords listed in alphabetical order,
     *  together with the call number and title of each book having that keyword
     *
     *  @param stream the stream to which to print the report
     *
     *  @exception an ErrorMessage is thrown if there is an unexpected error
     *             accessing the database.
     */
    /*@lineinfo:generated-code*//*@lineinfo:1285^5*/

//  ************************************************************
//  SQLJ iterator declaration:
//  ************************************************************

class BookKeywordsReportCursor 
extends sqlj.runtime.ref.ResultSetIterImpl
implements sqlj.runtime.PositionedIterator
{
  public BookKeywordsReportCursor(sqlj.runtime.profile.RTResultSet resultSet) 
    throws java.sql.SQLException 
  {
    super(resultSet, 3);
  }
  public BookKeywordsReportCursor(sqlj.runtime.profile.RTResultSet resultSet, int fetchSize, int maxRows) 
    throws java.sql.SQLException 
  {
    super(resultSet, fetchSize, maxRows, 3);
  }
  public String getCol1() 
    throws java.sql.SQLException 
  {
    return resultSet.getString(1);
  }
  public String getCol2() 
    throws java.sql.SQLException 
  {
    return resultSet.getString(2);
  }
  public String getCol3() 
    throws java.sql.SQLException 
  {
    return resultSet.getString(3);
  }
}


//  ************************************************************

/*@lineinfo:user-code*//*@lineinfo:1285^66*/

    public void keywordsReport(PrintStream stream) throws ErrorMessage
    {
        try
        {
            BookKeywordsReportCursor cursor;

            /*@lineinfo:generated-code*//*@lineinfo:1293^13*/

//  ************************************************************
//  #sql cursor = { select keyword, book_info.call_number, title
//                  from book_info join book_keyword
//                  on book_info.call_number = book_keyword.call_number
//                  order by keyword, book_info.call_number
//               };
//  ************************************************************

{
  sqlj.runtime.ConnectionContext __sJT_connCtx = sqlj.runtime.ref.DefaultContext.getDefaultContext();
  if (__sJT_connCtx == null) sqlj.runtime.error.RuntimeRefErrors.raise_NULL_DEFAULT_CONN_CTX();
  sqlj.runtime.ExecutionContext __sJT_execCtx = __sJT_connCtx.getExecutionContext();
  if (__sJT_execCtx == null) sqlj.runtime.error.RuntimeRefErrors.raise_NULL_EXEC_CTX();
  synchronized (__sJT_execCtx) {
    sqlj.runtime.profile.RTStatement __sJT_stmt = __sJT_execCtx.registerStatement(__sJT_connCtx, Database_SJProfileKeys.getKey(0), 51);
    try 
    {
      cursor = new BookKeywordsReportCursor(__sJT_execCtx.executeQuery(), __sJT_execCtx.getFetchSize(), __sJT_execCtx.getMaxRows());
    }
    finally 
    {
      __sJT_execCtx.releaseStatement();
    }
  }
}


//  ************************************************************

/*@lineinfo:user-code*//*@lineinfo:1297^13*/

            String [] BOOK_KEYWORDS_REPORT_HEADINGS =
                { "Keyword", "Call Number", "Title" };
            int [] BOOK_KEYWORDS_REPORT_COLUMN_WIDTHS =
                { 10, 30, 30 };

            produceReport(stream,
                          "Book Keywords Report",
                          BOOK_KEYWORDS_REPORT_HEADINGS,
                          BOOK_KEYWORDS_REPORT_COLUMN_WIDTHS,
                          null,
                          cursor.getResultSet());

            cursor.close();

        }
        catch(SQLException e)
        {
            throw new ErrorMessage("Unexpected SQL error " + e.getMessage());
        }
        finally
        {
            rollback();
        }
    }

    /** Produce a list of all categories - name, max books out, checkout period
     *
     *  @param stream the stream to which to print the report
     *
     *  @exception an ErrorMessage is thrown if there is an unexpected error
     *             accessing the database.
     */

    /*@lineinfo:generated-code*//*@lineinfo:1332^5*/

//  ************************************************************
//  SQLJ iterator declaration:
//  ************************************************************

class CategoryReportCursor 
extends sqlj.runtime.ref.ResultSetIterImpl
implements sqlj.runtime.PositionedIterator
{
  public CategoryReportCursor(sqlj.runtime.profile.RTResultSet resultSet) 
    throws java.sql.SQLException 
  {
    super(resultSet, 3);
  }
  public CategoryReportCursor(sqlj.runtime.profile.RTResultSet resultSet, int fetchSize, int maxRows) 
    throws java.sql.SQLException 
  {
    super(resultSet, fetchSize, maxRows, 3);
  }
  public String getCol1() 
    throws java.sql.SQLException 
  {
    return resultSet.getString(1);
  }
  public int getCol2() 
    throws java.sql.SQLException 
  {
    return resultSet.getIntNoNull(2);
  }
  public int getCol3() 
    throws java.sql.SQLException 
  {
    return resultSet.getIntNoNull(3);
  }
}


//  ************************************************************

/*@lineinfo:user-code*//*@lineinfo:1332^56*/

    public void categoriesReport(PrintStream stream) throws ErrorMessage
    {
        try
        {
            CategoryReportCursor cursor;

            /*@lineinfo:generated-code*//*@lineinfo:1340^13*/

//  ************************************************************
//  #sql cursor = { select * from category order by category_name  };
//  ************************************************************

{
  sqlj.runtime.ConnectionContext __sJT_connCtx = sqlj.runtime.ref.DefaultContext.getDefaultContext();
  if (__sJT_connCtx == null) sqlj.runtime.error.RuntimeRefErrors.raise_NULL_DEFAULT_CONN_CTX();
  sqlj.runtime.ExecutionContext __sJT_execCtx = __sJT_connCtx.getExecutionContext();
  if (__sJT_execCtx == null) sqlj.runtime.error.RuntimeRefErrors.raise_NULL_EXEC_CTX();
  synchronized (__sJT_execCtx) {
    sqlj.runtime.profile.RTStatement __sJT_stmt = __sJT_execCtx.registerStatement(__sJT_connCtx, Database_SJProfileKeys.getKey(0), 52);
    try 
    {
      cursor = new CategoryReportCursor(__sJT_execCtx.executeQuery(), __sJT_execCtx.getFetchSize(), __sJT_execCtx.getMaxRows());
    }
    finally 
    {
      __sJT_execCtx.releaseStatement();
    }
  }
}


//  ************************************************************

/*@lineinfo:user-code*//*@lineinfo:1340^75*/

            String [] CATEGORY_REPORT_HEADINGS =
                { "Category Name", "Checkout Period", "Maximum Books Out" };
            int [] CATEGORY_REPORT_COLUMN_WIDTHS =
                { 15, 17, 10 };

            produceReport(stream,
                          "Categories Report",
                          CATEGORY_REPORT_HEADINGS,
                          CATEGORY_REPORT_COLUMN_WIDTHS,
                          null,
                          cursor.getResultSet());

            cursor.close();

        }
        catch(SQLException e)
        {
            throw new ErrorMessage("Unexpected SQL error " + e.getMessage());
        }
        finally
        {
            rollback();
        }
    }

    /** Produce a list of all borrowers - id, name, phone number(s), category
     *
     *  @param stream the stream to which to print the report
     *
     *  @exception an ErrorMessage is thrown if there is an unexpected error
     *             accessing the database.
     */
    /*@lineinfo:generated-code*//*@lineinfo:1374^5*/

//  ************************************************************
//  SQLJ iterator declaration:
//  ************************************************************

class BorrowersReportCursor 
extends sqlj.runtime.ref.ResultSetIterImpl
implements sqlj.runtime.PositionedIterator
{
  public BorrowersReportCursor(sqlj.runtime.profile.RTResultSet resultSet) 
    throws java.sql.SQLException 
  {
    super(resultSet, 5);
  }
  public BorrowersReportCursor(sqlj.runtime.profile.RTResultSet resultSet, int fetchSize, int maxRows) 
    throws java.sql.SQLException 
  {
    super(resultSet, fetchSize, maxRows, 5);
  }
  public String getCol1() 
    throws java.sql.SQLException 
  {
    return resultSet.getString(1);
  }
  public String getCol2() 
    throws java.sql.SQLException 
  {
    return resultSet.getString(2);
  }
  public String getCol3() 
    throws java.sql.SQLException 
  {
    return resultSet.getString(3);
  }
  public String getCol4() 
    throws java.sql.SQLException 
  {
    return resultSet.getString(4);
  }
  public String getCol5() 
    throws java.sql.SQLException 
  {
    return resultSet.getString(5);
  }
}


//  ************************************************************

/*@lineinfo:user-code*//*@lineinfo:1374^79*/

    public void borrowersReport(PrintStream stream) throws ErrorMessage
    {
        try {
            BorrowersReportCursor cursor;

            /*@lineinfo:generated-code*//*@lineinfo:1381^13*/

//  ************************************************************
//  #sql cursor = { select borrower.borrower_id, last_name, first_name, phone, category_name
//                  from borrower join borrower_phone
//                  on borrower.borrower_id = borrower_phone.borrower_id
//                  order by last_name  };
//  ************************************************************

{
  sqlj.runtime.ConnectionContext __sJT_connCtx = sqlj.runtime.ref.DefaultContext.getDefaultContext();
  if (__sJT_connCtx == null) sqlj.runtime.error.RuntimeRefErrors.raise_NULL_DEFAULT_CONN_CTX();
  sqlj.runtime.ExecutionContext __sJT_execCtx = __sJT_connCtx.getExecutionContext();
  if (__sJT_execCtx == null) sqlj.runtime.error.RuntimeRefErrors.raise_NULL_EXEC_CTX();
  synchronized (__sJT_execCtx) {
    sqlj.runtime.profile.RTStatement __sJT_stmt = __sJT_execCtx.registerStatement(__sJT_connCtx, Database_SJProfileKeys.getKey(0), 53);
    try 
    {
      cursor = new BorrowersReportCursor(__sJT_execCtx.executeQuery(), __sJT_execCtx.getFetchSize(), __sJT_execCtx.getMaxRows());
    }
    finally 
    {
      __sJT_execCtx.releaseStatement();
    }
  }
}


//  ************************************************************

/*@lineinfo:user-code*//*@lineinfo:1384^36*/

            String [] BORROWER_REPORT_HEADINGS =
                { "Borrower ID", "Last Name", "First Name", "Phone #", "Category" };
            int [] BORROWER_REPORT_COLUMN_WIDTHS =
                { 15, 17, 10, 10, 10 };
            boolean [] BORROWER_REPORT_REPEAT_COLUMNS =
                { false, false, false, true, false };

            produceReport(stream,
                          "Borrowers Report",
                          BORROWER_REPORT_HEADINGS,
                          BORROWER_REPORT_COLUMN_WIDTHS,
                          BORROWER_REPORT_REPEAT_COLUMNS,
                          cursor.getResultSet());

            cursor.close();
        }
        catch(SQLException e) {
            throw new ErrorMessage("Unexpected SQL error " + e.getMessage());
        }
        finally {
            rollback();
        }
    }

    /** Produce a list of all fines outstanding - borrower name and
     *  phone number(s), total of all fines outstanding for this borrower
     *
     *  @param stream the stream to which to print the report
     *
     *  @exception an ErrorMessage is thrown if there is an unexpected error
     *             accessing the database.
     */

     /*@lineinfo:generated-code*//*@lineinfo:1419^6*/

//  ************************************************************
//  SQLJ iterator declaration:
//  ************************************************************

class FinesReportCursor 
extends sqlj.runtime.ref.ResultSetIterImpl
implements sqlj.runtime.PositionedIterator
{
  public FinesReportCursor(sqlj.runtime.profile.RTResultSet resultSet) 
    throws java.sql.SQLException 
  {
    super(resultSet, 4);
  }
  public FinesReportCursor(sqlj.runtime.profile.RTResultSet resultSet, int fetchSize, int maxRows) 
    throws java.sql.SQLException 
  {
    super(resultSet, fetchSize, maxRows, 4);
  }
  public String getCol1() 
    throws java.sql.SQLException 
  {
    return resultSet.getString(1);
  }
  public String getCol2() 
    throws java.sql.SQLException 
  {
    return resultSet.getString(2);
  }
  public String getCol3() 
    throws java.sql.SQLException 
  {
    return resultSet.getString(3);
  }
  public BigDecimal getCol4() 
    throws java.sql.SQLException 
  {
    return resultSet.getBigDecimal(4);
  }
}


//  ************************************************************

/*@lineinfo:user-code*//*@lineinfo:1419^72*/

    public void finesReport(PrintStream stream) throws ErrorMessage
    {
        try
        {
            FinesReportCursor cursor;

            /*@lineinfo:generated-code*//*@lineinfo:1427^13*/

//  ************************************************************
//  #sql cursor = { select Borrower.last_name,
//                  Borrower.first_name,Borrower_phone.phone,
//                  SUM(Fine.amount) as total_fine
//                  from Borrower left outer join Borrower_phone
//                  on Borrower.borrower_id = Borrower_phone.borrower_id
//                  join Fine on Borrower.borrower_id = Fine.borrower_id
//                  group by Borrower.last_name, Borrower.first_name,
//                          Borrower_phone.phone
//                  order by total_fine desc  };
//  ************************************************************

{
  sqlj.runtime.ConnectionContext __sJT_connCtx = sqlj.runtime.ref.DefaultContext.getDefaultContext();
  if (__sJT_connCtx == null) sqlj.runtime.error.RuntimeRefErrors.raise_NULL_DEFAULT_CONN_CTX();
  sqlj.runtime.ExecutionContext __sJT_execCtx = __sJT_connCtx.getExecutionContext();
  if (__sJT_execCtx == null) sqlj.runtime.error.RuntimeRefErrors.raise_NULL_EXEC_CTX();
  synchronized (__sJT_execCtx) {
    sqlj.runtime.profile.RTStatement __sJT_stmt = __sJT_execCtx.registerStatement(__sJT_connCtx, Database_SJProfileKeys.getKey(0), 54);
    try 
    {
      cursor = new FinesReportCursor(__sJT_execCtx.executeQuery(), __sJT_execCtx.getFetchSize(), __sJT_execCtx.getMaxRows());
    }
    finally 
    {
      __sJT_execCtx.releaseStatement();
    }
  }
}


//  ************************************************************

/*@lineinfo:user-code*//*@lineinfo:1435^42*/

            String [] FINES_REPORT_HEADINGS =
                { "Last Name", "First Name", "Phone #", "Total Fines" };
            int [] FINES_REPORT_COLUMN_WIDTHS =
                { 20, 20, 20, 10 };
            boolean [] FINES_REPORT_REPEAT_COLUMNS =
                { true, true, false, true };

            produceReport(stream,
                          "Fines Report",
                          FINES_REPORT_HEADINGS,
                          FINES_REPORT_COLUMN_WIDTHS,
                          FINES_REPORT_REPEAT_COLUMNS,
                          cursor.getResultSet());

            cursor.close();
        }
        catch(SQLException e) {
                throw new ErrorMessage("Unexpected SQL error " + e.getMessage());
        }
        finally
        {
            rollback();
        }
    }

    /** Produce a list of all overdue books - borrower name,
     *  title(s) of overdue books with due date for each.
     *
     *  @param stream the stream to which to print the report
     *
     *  @exception an ErrorMessage is thrown if there is an unexpected error
     *             accessing the database.
     */

      /*@lineinfo:generated-code*//*@lineinfo:1471^7*/

//  ************************************************************
//  SQLJ iterator declaration:
//  ************************************************************

class ODBooksReportCursor 
extends sqlj.runtime.ref.ResultSetIterImpl
implements sqlj.runtime.PositionedIterator
{
  public ODBooksReportCursor(sqlj.runtime.profile.RTResultSet resultSet) 
    throws java.sql.SQLException 
  {
    super(resultSet, 4);
  }
  public ODBooksReportCursor(sqlj.runtime.profile.RTResultSet resultSet, int fetchSize, int maxRows) 
    throws java.sql.SQLException 
  {
    super(resultSet, fetchSize, maxRows, 4);
  }
  public String getCol1() 
    throws java.sql.SQLException 
  {
    return resultSet.getString(1);
  }
  public String getCol2() 
    throws java.sql.SQLException 
  {
    return resultSet.getString(2);
  }
  public String getCol3() 
    throws java.sql.SQLException 
  {
    return resultSet.getString(3);
  }
  public Date getCol4() 
    throws java.sql.SQLException 
  {
    return resultSet.getDate(4);
  }
}


//  ************************************************************

/*@lineinfo:user-code*//*@lineinfo:1471^69*/

    public void overdueBooksReport(PrintStream stream) throws ErrorMessage
    {
        try
        {
            ODBooksReportCursor cursor;

            /*@lineinfo:generated-code*//*@lineinfo:1479^13*/

//  ************************************************************
//  #sql cursor = { select Borrower.last_name, Borrower.first_name,
//                  Book_info.title, Checked_out.date_due
//                  from Borrower join Checked_out
//                  on Borrower.borrower_id = Checked_out.borrower_id
//                  join Book_info on Checked_out.call_number = Book_info.call_number
//                  where current date < Checked_out.date_due
//                  order by Borrower.last_name, Borrower.first_name,
//                      Checked_out.date_due  };
//  ************************************************************

{
  sqlj.runtime.ConnectionContext __sJT_connCtx = sqlj.runtime.ref.DefaultContext.getDefaultContext();
  if (__sJT_connCtx == null) sqlj.runtime.error.RuntimeRefErrors.raise_NULL_DEFAULT_CONN_CTX();
  sqlj.runtime.ExecutionContext __sJT_execCtx = __sJT_connCtx.getExecutionContext();
  if (__sJT_execCtx == null) sqlj.runtime.error.RuntimeRefErrors.raise_NULL_EXEC_CTX();
  synchronized (__sJT_execCtx) {
    sqlj.runtime.profile.RTStatement __sJT_stmt = __sJT_execCtx.registerStatement(__sJT_connCtx, Database_SJProfileKeys.getKey(0), 55);
    try 
    {
      cursor = new ODBooksReportCursor(__sJT_execCtx.executeQuery(), __sJT_execCtx.getFetchSize(), __sJT_execCtx.getMaxRows());
    }
    finally 
    {
      __sJT_execCtx.releaseStatement();
    }
  }
}


//  ************************************************************

/*@lineinfo:user-code*//*@lineinfo:1486^42*/

            String [] ODBOOKS_REPORT_HEADINGS =
                { "Last Name", "First Name", "Title", "Date Due" };
            int [] ODBOOKS_REPORT_COLUMN_WIDTHS =
                { 20, 20, 50, 10 };
            boolean [] ODBOOKS_REPORT_REPEAT_COLUMNS =
                { true, true, false, false };

            produceReport(stream,
                          "Fines Report",
                          ODBOOKS_REPORT_HEADINGS,
                          ODBOOKS_REPORT_COLUMN_WIDTHS,
                          ODBOOKS_REPORT_REPEAT_COLUMNS,
                          cursor.getResultSet());

            cursor.close();
        }
        catch(SQLException e) {
                throw new ErrorMessage("Unexpected SQL error " + e.getMessage());
        }
        finally
        {
            rollback();
        }
    }

    /* ************************************************************************
     * Utility methods
     * ***********************************************************************/

    /** Roll back a read-only transaction or a failed write transaction
     *
     */
    private void rollback()
    {
        try
        {
            /*@lineinfo:generated-code*//*@lineinfo:1524^13*/

//  ************************************************************
//  #sql { rollback  };
//  ************************************************************

{
  sqlj.runtime.ConnectionContext __sJT_connCtx = sqlj.runtime.ref.DefaultContext.getDefaultContext();
  if (__sJT_connCtx == null) sqlj.runtime.error.RuntimeRefErrors.raise_NULL_DEFAULT_CONN_CTX();
  sqlj.runtime.ExecutionContext __sJT_execCtx = __sJT_connCtx.getExecutionContext();
  if (__sJT_execCtx == null) sqlj.runtime.error.RuntimeRefErrors.raise_NULL_EXEC_CTX();
  synchronized (__sJT_execCtx) {
    sqlj.runtime.profile.RTStatement __sJT_stmt = __sJT_execCtx.registerStatement(__sJT_connCtx, Database_SJProfileKeys.getKey(0), 56);
    try 
    {
      __sJT_execCtx.executeUpdate();
    }
    finally 
    {
      __sJT_execCtx.releaseStatement();
    }
  }
}


//  ************************************************************

/*@lineinfo:user-code*//*@lineinfo:1524^29*/
        }
        catch(SQLException e)
        {
            System.out.println("Fatal error: SQL Exception " + e.getErrorCode() +
                " " + e.getMessage() + " during rollback. ");
            System.exit(1);
        }
    }

    /** Print a neatly formatted report
     *
     *  @param stream the stream to print the report to
     *  @param heading the overall heading for the report
     *  @param columnHeaadings the headings for the report's columns
     *  @param columnWidths the widths for the various columns
     *  @param repeatColumns for some report formats, certain values need to
     *         be suppressed for all but their first occurrence if the exact
     *         same values occur on successive lines of the report.  This
     *         array specifies which columns these are.  If _all_ of the
     *         columns specified contain the exact same values on
     *         successive lines, the values will be replaced by spaces on all
     *         but the first line.  This parameter can be null if this feature
     *         is not used for a certain report
     *  @param resultSet a SQL result set containing the report's data
     */
    private void produceReport(PrintStream stream,
                               String heading,
                               String [] columnHeadings,
                               int [] columnWidths,
                               boolean [] repeatColumns,
                               ResultSet resultSet) throws SQLException
    {
        stream.println(); stream.println(heading);
        for (int i = 0; i < heading.length(); i ++)
            stream.print('_');
        stream.println(); stream.println();

        for (int i = 0; i < columnHeadings.length; i ++)
            stream.print(pad(columnHeadings[i], columnWidths[i]));
        stream.println();
        for (int i = 0; i < columnHeadings.length; i ++)
        {
            for (int j = 0; j < columnHeadings[i].length(); j ++)
                stream.print('_');
            stream.print(pad("", columnWidths[i] - columnHeadings[i].length()));
        }
        stream.println(); stream.println();

        String [] previousLineValues = new String [columnHeadings.length];

        while(resultSet.next())
        {
            String [] currentLineValues = new String [columnHeadings.length];
            boolean repeatValuesSame = repeatColumns != null;

            for (int i = 0; i < currentLineValues.length; i ++)
            {
                currentLineValues[i] = resultSet.getString(i + 1);
                if (repeatValuesSame && repeatColumns[i] &&
                        ! currentLineValues[i].equals(previousLineValues[i]))
                    repeatValuesSame = false;
            }

            for (int i = 0; i < currentLineValues.length; i ++)
            {
                if (repeatValuesSame && repeatColumns[i])
                    stream.print(pad("", columnWidths[i]));
                else
                    stream.print(pad(currentLineValues[i], columnWidths[i]));
            }

            stream.println();
            previousLineValues = currentLineValues;
        }
}

    /** Pad a string with spaces
     *
     *  @param input the string to pad.  Can be null - in which case it will
     *         be converted to an empty string
     *  @param width the total width to pad it to
     *  @return the string padded with enough spaces to make the specified width.
     *          If the string was wider than width to begin with, it is returned
     *          unchanged
     */
    private static String pad(String input, int width)
    {
        if (input == null) input = "";
        StringBuffer result = new StringBuffer(input);
        while(result.length() < width)
            result.append(' ');
        return result.toString();
    }

    /* ************************************************************************
     * To facilitate testing, this object maintains a "deltaDate" value to be
     * added to the current date maintained by the database.  Since it can be
     * either negative or positive, it can be used to adjust the program's
     * notion of the current date forward or backward. The GUI provides a
     * menu option for "diddling" with the date for debugging.
     * ***********************************************************************/

    private int deltaDate;

    /** Get the current value of delta date - used only for testing purposes -
     *  should not be called from code that actually manipulates the
     *  database.
     *
     *  @return the current value of deltaDate
     */
    public int getDeltaDate()
    {
        return deltaDate;
    }

    /** Change the current value of delta date - used only for testing
     *  purposes - should not be called from code that actually manipulates
     *  the database.
     *
     *  @param deltaDate the new value for deltaDate
     */
    public void setDeltaDate(int deltaDate)
    {
        this.deltaDate = deltaDate;
    }

    /* ************************************************************************
     * Class constants and method
     * ***********************************************************************/

    // Symbolic names for various SQL Errors

    private static final int BAD_PASSWORD_SQL_ERROR = -1403;
    private static final String NO_ROW_SQL_STATE = "02000";
    private static final int DUPLICATE_KEY_SQL_ERROR = -803;

    private static final String BOOK_INFO_VALID_FORMAT_CONSTRAINT =
        "22001";
    private static final String BOOK_KEYWORD_VALID_KEYWORD_CONSTRAINT =
        "23513";
    private static final String BORROWER_CATEGORY_FOREIGN_CONSTRAINT =
        "23503";

    private static final String BOOK_AUTHOR_TABLE_NAME = "BOOK_AUTHOR";

    private static final String DRIVER_NAME = "com.ibm.db2.jcc.DB2Driver";
    private static final String DATABASE_URL =
        "jdbc:db2://joshua.cs.gordon.edu:50000/project";

    // For testing purposes only - display an array of values for fields that
    // are parameters to a method

    private String toString(Object [] information)
    {
        String result = "";
        for (int i = 0; i < information.length; i ++)
        {
            if (information[i] instanceof String)
                result += (String) information[i];
            else
            {
                Iterator iterator = ((List) information[i]).iterator();
                if (iterator.hasNext())
                {
                    String separator = "{ ";
                    while (iterator.hasNext())
                    {
                        result += separator += (String) iterator.next();
                        separator = ", ";
                    }
                    result += " }";
                }
                else
                    result += "{}";
            }
            if (i < information.length - 1)
                result += ", ";
        }
        return result;
    }
}/*@lineinfo:generated-code*/class Database_SJProfileKeys 
{
  private java.lang.Object[] keys;
  private final sqlj.runtime.profile.Loader loader = sqlj.runtime.RuntimeContext.getRuntime().getLoaderForClass(getClass());
  private static Database_SJProfileKeys inst = null;
  public static java.lang.Object getKey(int keyNum) 
    throws java.sql.SQLException 
  {
    synchronized(semesterproject.Database_SJProfileKeys.class) {
      if (inst == null)
      {
        inst = new Database_SJProfileKeys();
      }
    }
    return inst.keys[keyNum];
  }
  private Database_SJProfileKeys() 
    throws java.sql.SQLException 
  {
    keys = new java.lang.Object[1];
    keys[0] = sqlj.runtime.ref.DefaultContext.getProfileKey(loader, "semesterproject.Database_SJProfile0");
  }
}
