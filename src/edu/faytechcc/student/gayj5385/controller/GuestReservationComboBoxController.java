/**
    Controller for the locations combo box on the panel for the guest to make
    a reservation
    CSC-289 - Group 4
    @author Timothy Burns
*/

package edu.faytechcc.student.gayj5385.controller;

import edu.faytechcc.student.burnst9091.data.Location;
import edu.faytechcc.student.burnst9091.data.Timeframe;
import edu.faytechcc.student.gayj5385.gui.GuestReservationPanel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class GuestReservationComboBoxController implements ActionListener
{
    private GuestReservationPanel view;
    
    /**
        Constructs a new GuestReservationComboBoxController initialized with
        the given GuestReservationPanel to control the combo box of
    
        @param v The view
    */
    
    public GuestReservationComboBoxController(GuestReservationPanel v)
    {
        view = v;
    }
    
    /**
        Performs an action on a location being selected
    
        @param e The ActionEvent
    */
    
    @Override
    public void actionPerformed(ActionEvent e)
    {
        Location loc = view.getSelectedLocation();
        
        if (loc != null)
        {
            view.setCapacity(String.valueOf(loc.getCapacity()));
            List<Timeframe> timeframes = loc.deriveReservableTimeframes();
            view.setTimeframes(timeframes);
        }
    }
}