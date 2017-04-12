/**
    Controller for buttons on the opening view
    CSC-289 - Group 4
    @author Timothy Burns
*/

package edu.faytechcc.student.gayj5385.controller;

import edu.faytechcc.student.burnst9091.data.DatabaseSettings;
import edu.faytechcc.student.burnst9091.data.ReservableLocation;
import edu.faytechcc.student.burnst9091.data.Reservation;
import edu.faytechcc.student.burnst9091.data.SHA256SaltHasher;
import edu.faytechcc.student.burnst9091.data.SystemPreferences;
import edu.faytechcc.student.gayj5385.gui.MainPanel;
import edu.faytechcc.student.gayj5385.gui.OpenPanel;
import edu.faytechcc.student.gayj5385.gui.dialog.SendEmailDialog;
import edu.faytechcc.student.mccanns0131.database.LocationSQLDAO;
import edu.faytechcc.student.mccanns0131.database.ReservationSQLDAO;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;

public class OpeningController implements ActionListener
{
    // Fields
    private HashMap<ReservableLocation, List<Reservation>> reservations;
    private List<ReservableLocation> locations;
    private MainPanel mainPanel;
    private OpenPanel view;
    
    /**
        Constructs a new OpeningController initialized with the given panels,
        location & reservation listings
    
        @param main Main panel
        @param v The view
        @param locs The locations
        @param reserves The reservations
    */
    
    public OpeningController(MainPanel main, OpenPanel v, List<ReservableLocation> locs,
        HashMap<ReservableLocation, List<Reservation>> reserves)
    {
        mainPanel = main;
        view = v;
        locations = locs;
        reservations = reserves;
    }
    
    /**
        Responds to a button being clicked
    
        @param e The action event
    */
    
    @Override
    public void actionPerformed(ActionEvent e)
    {
        switch (e.getActionCommand())
        {
            case "Make Reservation":
                showGuestReservationPanel();
                break;
            case "Contact Administrator":
                contactAdmin();
                break;
            case "Login":
                login();
                break;
            case "Exit":
                System.exit(0);
                break;
        }
    }
    
    /**
        Show the dialog enabling a guest to contact the administrator
    */
    
    private void contactAdmin()
    {
        SendEmailDialog dialog = new SendEmailDialog(SendEmailDialog.GUEST,
            null);
        
        dialog.setVisible(true);
    }
    
    /**
        Show the dialog enabling the administrator to login
    */
    
    private void login()
    {
        JPasswordField passwordField = new JPasswordField(15);
        int action = JOptionPane.showConfirmDialog(view, passwordField,
            "Administrator Password", JOptionPane.OK_CANCEL_OPTION);
        
        if (action == JOptionPane.OK_OPTION)
        {
            SHA256SaltHasher saltHasher = new SHA256SaltHasher();
            
            // Validate password
            String password = new String(passwordField.getPassword());
            
            try
            {
                password = saltHasher.saltHash(password);
                
                String currentPassword = SystemPreferences.getAdminPassword();
                if (currentPassword.isEmpty())
                    currentPassword = saltHasher.saltHash(currentPassword);
                
                if (password.equals(currentPassword))
                    showAdminView();
                else
                    JOptionPane.showMessageDialog(view, "Invalid Password");
            }
            catch (NoSuchAlgorithmException ex)
            {
                JOptionPane.showMessageDialog(view,
                    "Salt & hash operation failed", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    /**
        Shows the administrator's view
    */
    
    private void showAdminView()
    {        
        try
        {
            // Update locations & reservations
            LocationSQLDAO locationDAO = new LocationSQLDAO();
            locations.clear();
            locations.addAll(locationDAO.getAll());

            ReservationSQLDAO reservationDAO = new ReservationSQLDAO();
            reservations.clear();
            for (ReservableLocation loc : locations)
            {
                List<Reservation> res = reservationDAO.getByLocation(loc);
                
                if (!res.isEmpty())
                    reservations.put(loc, res);
            }
        }
        catch (SQLException ex)
        {
            JOptionPane.showMessageDialog(view,
                "Failed updating location & reservation data", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
        
        mainPanel.showAdminPanel();
    }
    
    /**
        Shows the panel for the guest to make a reservation on
    */
    
    private void showGuestReservationPanel()
    {        
        try
        {
            // Update locations & reservations
            LocationSQLDAO locationDAO = new LocationSQLDAO();
            locations.clear();
            locations.addAll(locationDAO.getAll());
        }
        catch (SQLException ex)
        {
            JOptionPane.showMessageDialog(view,
                "Failed updating location data", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
        
        List<ReservableLocation> availLocs = new ArrayList<>();
        for (ReservableLocation loc : locations)
        {
            if (loc.getReservableTimeframes().size() > 0)
                availLocs.add(loc);
        }
        
        mainPanel.showGuestReservationPanel(availLocs);
    }
}