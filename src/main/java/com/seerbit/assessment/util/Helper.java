package com.seerbit.assessment.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class Helper {

    public static boolean isDateFuture(final String date, final String dateFormat) {
        LocalDate localDate = LocalDate.now(ZoneId.systemDefault());

        LocalDateTime localDateTime = LocalDateTime.parse(date, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(dateFormat);
        LocalDate inputDate = LocalDate.parse(date, dtf);

        return inputDate.isAfter(localDate);
    }


    public static boolean isValidDate(String pDateString) throws ParseException {
        Date date = new SimpleDateFormat("yyyy-MM-ddThh:mm:ss.sssZ").parse(pDateString);
        return new Date().before(date);
    }

    public static BigDecimal roundUpAmount(BigDecimal amount){
        BigDecimal newAmount = BigDecimal.valueOf(amount.doubleValue());
        return newAmount.setScale(2, RoundingMode.HALF_UP);
    }


}
