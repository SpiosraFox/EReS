/**
 * 
 * @author Shane McCann
 */
package edu.faytechcc.student.burnst9091.data.search;

import edu.faytechcc.student.burnst9091.data.Reservable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.function.Predicate;

public class ReservableSearch
{
    // Fields
    private Predicate<Reservable> locationName,
                                  capacity,
                                  startDate,
                                  startTime,
                                  endDate,
                                  endTime,
                                  cost,
                                  finalPredicate;
    
    
    /**
     * Constructor
     */
    public ReservableSearch()
    {
        locationName = null;
        capacity = null;
        startDate = null;
        startTime = null;
        endDate = null;
        endTime = null;
        cost = null;
        finalPredicate = null;
    }
    
    /**
     * Search - 
     * 
     * @param criteria
     * @return 
     */
    public Predicate<Reservable> search(String criteria)
    {
        // Split search criteria
        String[] filters = criteria.split(";");

        for (String filter : filters)
        {
            // Split keys and values
            String[] constraint = filter.split("=");

            if (constraint.length == 2)
            {                
                String key = constraint[0].trim(), 
                       val = constraint[1].trim();

                switch(key.toLowerCase())
                {
                    case "locationname":
                    case "location":
                        locationName = filterByLocationName(val);
                        if (finalPredicate == null)
                            finalPredicate = locationName;
                        else
                            finalPredicate = finalPredicate.and(locationName);
                        break;
                    case "capacity":
                    case "cap":
                        capacity = filterByCapacity(val);
                        if (finalPredicate == null)
                            finalPredicate = capacity;
                        else
                            finalPredicate = finalPredicate.and(capacity);
                        break;
                    case "startdate":
                        startDate = filterByStartDate(val);
                        if (finalPredicate == null)
                            finalPredicate = startDate;
                        else
                            finalPredicate = finalPredicate.and(startDate);
                        break;
                    case "starttime":
                        //if (val.matches("\\d{4}-\\d{2}-\\d{2},\\d{2}:\\d{2}"))
                        startTime = filterByStartTime(val);
                        if (finalPredicate == null)
                            finalPredicate = startTime;
                        else
                            finalPredicate = finalPredicate.and(startTime);
                        break;
                    case "enddate":
                        endDate = filterByEndDate(val);
                        if (finalPredicate == null)
                            finalPredicate = endDate;
                        else
                            finalPredicate = finalPredicate.and(endDate);
                        break;
                    case "endtime":
                        endTime = filterByEndTime(val);
                        if (finalPredicate == null)
                            finalPredicate = endTime;
                        else
                            finalPredicate = finalPredicate.and(endTime);
                        break;
                        //start=2017-03-20,13:00; end=2017-03-20,14:00
                    case "cost":
                    case "price":
                        cost = filterByCost(val);
                        if (finalPredicate == null)
                            finalPredicate = cost;
                        else
                            finalPredicate = finalPredicate.and(cost);
                        break;
                }
            }
        }
        
        return finalPredicate;
    }
    
    private Predicate<Reservable> filterByLocationName(String value)
    {
        return r -> r.getLocation().getName().equalsIgnoreCase(value);
    }
    
    private Predicate<Reservable> filterByCapacity(String value)
    {
        return r -> r.getCapacity() == Integer.parseInt(value);
    }
    
    private Predicate<Reservable> filterByStartDate(String value)
    {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate start = LocalDate.parse(value, format);
        
        return r -> r.getStartDate().equals(start);
    }
    
    private Predicate<Reservable> filterByStartTime(String value)
    {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime start = LocalTime.parse(value, format);
        
        return r-> r.getStartTime().equals(start);
    }
    
    private Predicate<Reservable> filterByEndDate(String value)
    {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate end = LocalDate.parse(value, format);
        
        return r -> r.getEndDate().equals(end);
    }
    
    private Predicate<Reservable> filterByEndTime(String value)
    {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime end = LocalTime.parse(value, format);
        
        return r-> r.getEndTime().equals(end);
    }
    
    private Predicate<Reservable> filterByCost(String value)
    {
        return r -> r.getCost().equals(new BigDecimal(value));
    }
}