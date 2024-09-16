package com.kit.grs.util;

import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class CalendarUtil {
    public static Date truncateDate(Date fromDate){
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            return sdf.parse(sdf.format(fromDate));
        }catch (ParseException pe){}
        return fromDate;
    }
}
