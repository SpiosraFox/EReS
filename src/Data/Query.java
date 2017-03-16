/**
    A query of the database
    CSC-289 - Group 4
    @author Timothy Burns
*/

package Data;

import java.sql.SQLException;

public class Query
{
    // Fields
    private String sql;
    
    /**
        Constructor
    */
    
    public Query()
    {
        sql = "";
    }
    
    /**
        QueryAvailableLocationTimeframes - Query for & return a list of
        available timeframes at the location specified by the given name
    
        @param locationName Name of location to get available timeframes of
                            from the database
        @throws SQLException Error querying the database
        @return A list of available timeframes at the specified location
    */
    
    private TimeframeList queryAvailableLocationTimeframes(String locationName)
            throws SQLException
    {
        sql = "SELECT Timeframes.StartDate, Timeframes.StartTime, " +
              "Timeframes.EndDate, Timeframes.EndTime, Reservables.Cost " +
              "FROM Timeframes " +
              "INNER JOIN Reservables " +
              "ON Timeframes.TimeframeID = Reservables.TimeframeID " +
              "INNER JOIN Reservations " +
              "ON Reservables.LocationName = Reservations.LocationName " +
              "AND Reservables.TimeframeID = Reservations.TimeframeID " +
              "WHERE Reservables.LocationName = '" + locationName + "' " +
              "AND Reservables.LocationName <> Reservations.LocationName " +
              "ORDER BY StartDate, StartTime, EndDate, EndTime, Cost";
        
        return ResultSetParser.parseTimeframes
            (ReserveDB.getInstance().runQuery(this), false);
    }
    
    /**
        QueryIfLocationExists - Query if a record of a location exists in
        the database
    
        @param locationName The name of the location to query if exists in the
                            database
        @throws SQLException Error querying the database
        @return Whether the record of the location with the specified name
                exists in the database
    */
    
    public boolean queryIfLocationExists(String locationName) 
            throws SQLException
    {
        sql = "SELECT Locations.LocationName " +
              "FROM Locations " +
              "WHERE LocationName = '" + locationName + "'";
        
        return !ResultSetParser.isEmpty(ReserveDB.getInstance().runQuery(this));
    }
    
    /**
        QueryLocation - Query for and return the location with the specified
        name
    
        @param locationName Name of location to get from the database
        @throws SQLException Error querying the database
        @return The location with the specified name
    */
    
    public Location queryLocation(String locationName) throws SQLException
    {
        return null;
    }
    
    /**
        QueryLocationNames - Return the names of every location in the database
    
        @throws SQLException Error querying the database
        @return The names of every location in the database
    */
    
    public String[] queryLocationNames() throws SQLException
    {
        sql = "SELECT Locations.LocationName " +
              "FROM Locations " +
              "ORDER BY LocationName";
        
        return ResultSetParser.parseLocationNames
            (ReserveDB.getInstance().runQuery(this));
    }
    
    /**
        QueryLocationTimeframes - Query for & return the list of
        timeframes at the location specified by the given name
    
        @param name Name of location to get timeframes of from the database
        @throws SQLException Error querying the database
        @return timeframes A list of timeframes at the specified location
    */
    
    private TimeframeList queryLocationTimeframes(String name)
            throws SQLException
    {
        TimeframeList timeframes =
                queryReservedLocationTimeframes(name);
        
        TimeframeList availableTimeframes =
                queryAvailableLocationTimeframes(name);
        
        timeframes.addAll(availableTimeframes);
        
        return timeframes;
    }
    
    /**
        QueryIfReservableExists - Query if a record of a reservable exists in
        the database
    
        @param reservable The reservable to query if exists in the database
        @throws SQLException Error querying the database
        @return Whether the record of the reservable exists in the database
    */
    
    public boolean queryIfReservableExists(Reservable reservable)
            throws SQLException
    {
        sql = "SELECT Reservables.LocationName, Timeframes.StartDate, " +
              "Timeframes.StartTime, Timeframes.EndDate, Timeframes.EndTime " +
              "FROM Reservables " +
              "INNER JOIN Timeframes " +
              "ON Reservables.TimeframeID = Timeframes.TimeframeID " +
              "WHERE Reservables.LocationName = '" +
                reservable.getName() + "' " +
              "AND StartDate = '" +
                reservable.getStartDate() + "' " +
              "AND StartTime = '" +
                reservable.getStartTime() + "' " +
              "AND EndDate = '" +
                reservable.getEndDate() + "' " +
              "AND EndTime = '" +
                reservable.getEndTime() + "'";
        
        return !ResultSetParser.isEmpty(ReserveDB.getInstance().runQuery(this));
    }
    
    /**
        QueryIfReservableExists - Query if a record of a reservable with the
        given name exists in the database
    
        @param reservableName Name of reservable to query if exists in the
                              database
        @throws SQLException Error querying the database
        @return Whether a record of a reservable with the given name exists
                in the database
    */
    
    public boolean queryIfReservableExists(String reservableName)
            throws SQLException
    {
        sql = "SELECT Reservables.LocationName " +
              "FROM Reservables " +
              "WHERE Reservables.LocationName = '" + reservableName + "'";
        
        return !ResultSetParser.isEmpty(ReserveDB.getInstance().runQuery(this));
    }
    
    /**
        QueryIfReservableExists - Query if a record of a reservable with the
        given timeframe exists in the database
    
        @param timeframe Timeframe of reservable to query if exists in the
                         database
        @throws SQLException Error querying the database
        @return Whether a record of a reservable with the given timeframe exists
                in the database
    */
    
    public boolean queryIfReservableExists(Timeframe timeframe)
            throws SQLException
    {  
        sql = "SELECT Reservables.TimeframeID " +
              "FROM Reservables " +
              "INNER JOIN Timeframes " +
              "ON Reservables.TimeframeID = Timeframes.TimeframeID " +
              "WHERE Timeframes.StartDate = '" +
                timeframe.getStartDate() + "' " +
              "AND Timeframes.StartTime = '" +
                timeframe.getStartTime() + "' " +
              "AND Timeframes.EndDate = '" +
                timeframe.getEndDate() + "' " +
              "AND Timeframes.EndTime = '" +
                timeframe.getEndTime() + "'";
        
        return !ResultSetParser.isEmpty(ReserveDB.getInstance().runQuery(this));
    }
    
    /**
        QueryIfReservationExists - Query if a record of a reservation exists
        in the database
    
        @param reservation Reservation to query if exists in the database
        @throws SQLException Error querying the database
        @return Whether a record of a reservation exists in the database
    */
    
    public boolean queryIfReservationExists(Reservation reservation)
            throws SQLException
    {
        sql = "SELECT Locations.LocationName, Timeframes.StartDate, " +
              "Timeframes.StartTime, Timeframes.EndDate, Timeframes.EndTime, " +
              "FROM Reservations " +
              "INNER JOIN Reservables " +
              "ON Reservations.LocationName = Reservables.LocationName " +
              "AND Reservations.TimeframeID = Reservables.TimeframeID " +
              "INNER JOIN Locations " +
              "ON Reservables.LocationName = Locations.LocationName " +
              "INNER JOIN Timeframes " +
              "ON Reservables.TimeframeID = Timeframes.TimeframeID " +
              "WHERE LocationName = '" + reservation.getLocationName() + "' " +
              "AND StartDate = '" + reservation.getStartDate() + "' " +
              "AND StartTime = '" + reservation.getStartTime() + "' " +
              "AND EndDate = '" + reservation.getEndDate() + "' " +
              "AND EndTime = '" + reservation.getEndTime() + "'";
        
        return !ResultSetParser.isEmpty(ReserveDB.getInstance().runQuery(this));
    }
    
    /**
        QueryIfTimeframeExists - Query if a record of a timeframe exists
        in the database
    
        @param timeframe Timeframe to query if exists in the database
        @throws SQLException Error querying the database
        @return Whether the record of the timeframe exists in the database
    */
    
    public boolean queryIfTimeframeExists(Timeframe timeframe)
            throws SQLException
    {
        sql = "SELECT Timeframes.StartDate, Timeframes.StartTime, " +
              "Timeframes.EndDate, Timeframes.EndTime " +
              "FROM Timeframes " +
              "WHERE StartDate = '" + timeframe.getStartDate() + "' " +
              "AND StartTime = '" + timeframe.getStartTime() + "' " +
              "AND EndDate = '" + timeframe.getEndDate() + "' " +
              "AND EndTime = '" + timeframe.getEndTime() + "'";
        
        return !ResultSetParser.isEmpty(ReserveDB.getInstance().runQuery(this));
    }
    
    /**
        QueryIfReserverExists - Query if a record of a reserver exists in the
        database
        
        @param r Reserver to query if exists in the database
        @throws SQLException Error querying the database
        @return Whether the record of a reserver exists in the database
     */
    
    public boolean queryIfReserverExists(Reserver r) throws SQLException
    {
        // Build SQL statement
        sql = "SELECT Reservers.FirstName, Reservers.LastName, " +
              "Reservers.Email, Reservers.Phone " +
              "FROM Reservers " +
              "WHERE FirstName = '" + r.getFirstName() + "' " +
              "AND LastName = '" + r.getLastName() + "' " +
              "AND Email = '" + r.getEmailAddress() + "' " +
              "AND Phone = '" + r.getPhoneNumber() + "'";
        
        return !ResultSetParser.isEmpty(ReserveDB.getInstance().runQuery(this));
    }
    

    public boolean queryIfReserverExists(String email) throws SQLException
    {
        sql = "SELECT Reservers.email"
            + "FROM Reservers"
            + "WHERE email = '" + email + "'";
        
        return !ResultSetParser.isEmpty(ReserveDB.getInstance().runQuery(this));
    }

    /**
        QueryReservedLocationTimeframes - Query for & return a list of
        reserved timeframes at the location specified by the given name
    
        @param locationName Name of location to get reserved timeframes of
                            from the database
        @throws SQLException Error querying the database
        @return A list of reserved timeframes at the specified location
    */
    
    private TimeframeList queryReservedLocationTimeframes(String locationName)
            throws SQLException
    {          
        sql = "SELECT Timeframes.StartDate, Timeframes.StartTime, " +
              "Timeframes.EndDate, Timeframes.EndTime, Reservables.Cost " +
              "FROM Timeframes " +
              "INNER JOIN Reservables " +
              "ON Timeframes.TimeframeID = Reservables.TimeframeID " +
              "INNER JOIN Reservations " +
              "ON Reservables.LocationName = Reservations.LocationName " +
              "AND Reservables.TimeframeID = Reservations.TimeframeID " +
              "WHERE Reservations.LocationName = '" + locationName + "' " +
              "ORDER BY StartDate, StartTime, EndDate, EndTime, Cost";
        
        return ResultSetParser.parseTimeframes
            (ReserveDB.getInstance().runQuery(this), true);
    }
    
    /**
        ToString - Return a string representation of the object
    
        @return A string representation of the object
    */
    
    @Override
    public String toString()
    {
        return sql;
    }
}