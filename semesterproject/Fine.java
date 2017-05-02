// TEMPORARY CLASS TO AVOID ERROR
package semesterproject;

import java.math.BigDecimal;
import java.util.Calendar;
import java.sql.Date;

public class Fine {
    String title;
    Date dateDue,
        dateReturned;
    BigDecimal amount;
    public static double DAILY_FINE_RATE = 1;

    public Fine(String title, Date dateDue, Date dateReturned, BigDecimal amount) {
        this.title = title;
        this.dateDue = dateDue;
        this.dateReturned = dateReturned;
        this.amount = amount;
    }
    public BigDecimal getAmountDue() {
        return amount;
    }
}
