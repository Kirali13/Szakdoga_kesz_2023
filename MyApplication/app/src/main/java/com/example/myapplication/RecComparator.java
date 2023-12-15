package com.example.myapplication;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

public class RecComparator implements Comparator<Tr_Receipt> {
    public int compare(Tr_Receipt o1, Tr_Receipt o2)
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy - mm -- dd");
        try {
            Date d1 = dateFormat.parse(o1.getTr_rec_date());
            Date d2 = dateFormat.parse(o2.getTr_rec_date());
            return d2.compareTo(d1); // Compare in descending order
        } catch (ParseException e) {
            e.printStackTrace(); // Handle parsing exceptions appropriately
            return 0; // Default comparison if parsing fails
        }
    }
}
