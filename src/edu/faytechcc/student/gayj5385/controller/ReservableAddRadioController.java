/**
    Controller for radio buttons on the dialog to add reservables
    CSC-289 - Group 4
    @author Timothy Burns
*/

package edu.faytechcc.student.gayj5385.controller;

import edu.faytechcc.student.burnst9091.data.ReservableLocation;
import edu.faytechcc.student.gayj5385.gui.dialog.AddReservableDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ReservableAddRadioController implements ActionListener
{
    // Fields
    private AddReservableDialog view;
    
    /**
        Constructor - Accepts the view
    
        @param v The view
    */
    
    public ReservableAddRadioController(AddReservableDialog v)
    {
        view = v;
    }
    
    /**
        Respond to action event
    
        @param e The action event
    */
    
    @Override
    public void actionPerformed(ActionEvent e)
    {
        switch (e.getActionCommand())
        {
            case "New Location":
                doSwitch(false);
                break;
            case "Existing Location":
                doSwitch(true);
                break;
        }
    }
    
    /**
        Switch on a set of controls on the view according to whether
        existing locations is enabled
    
        @param enabled Whether existing locations is enabled
    */
    
    private void doSwitch(boolean enabled)
    {
        view.setExistingLocationsEnabled(enabled);
        view.setLocationEnabled(!enabled);
        view.setCapacityEnabled(!enabled);
        
        if (!enabled)
        {
            view.setLocation("");
            view.setCapacity("");
        }
        else
        {
            ReservableLocation loc = view.getSelectedLocation();
            
            if (loc != null)
            {
                view.setLocation(loc.getName());
                view.setCapacity(String.valueOf(loc.getCapacity()));
            }
        }
    }
}