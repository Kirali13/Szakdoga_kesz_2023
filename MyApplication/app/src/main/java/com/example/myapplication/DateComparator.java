package com.example.myapplication;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

//https://stackoverflow.com/questions/1206073/sorting-a-collection-of-objects

public class DateComparator implements Comparator<Tr_Object>{
        public int compare(Tr_Object o1, Tr_Object o2)
        {
            SimpleDateFormat dateFormat = new SimpleDateFormat("MMM d, yyyy");
            try {
                Date d1 = dateFormat.parse(o1.getTr_date());
                Date d2 = dateFormat.parse(o2.getTr_date());
                return d2.compareTo(d1); // Compare in descending order
            } catch (ParseException e) {
                e.printStackTrace();
                return 0; // Default comparison if parsing fails
            }
        }
}
