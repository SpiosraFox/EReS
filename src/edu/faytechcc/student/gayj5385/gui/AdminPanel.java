/**
    The root administrator panel
    CSC-289 - Group 4
    @author Timothy Burns
*/

package edu.faytechcc.student.gayj5385.gui;

import edu.faytechcc.student.burnst9091.data.DatabaseSettings;
import edu.faytechcc.student.burnst9091.data.EmailSettings;
import edu.faytechcc.student.burnst9091.data.ReservableLocation;
import edu.faytechcc.student.burnst9091.data.Reservation;
import edu.faytechcc.student.burnst9091.data.SecurityOption;
import edu.faytechcc.student.gayj5385.controller.SettingsPanelController;
import edu.faytechcc.student.burnst9091.data.ReservableTimeframe;
import edu.faytechcc.student.burnst9091.data.search.Filter;
import edu.faytechcc.student.gayj5385.controller.AdminPanelController;
import edu.faytechcc.student.gayj5385.controller.reservable.ManageReservableButtonController;
import edu.faytechcc.student.gayj5385.controller.reservable.ManageReservableComboBoxController;
import edu.faytechcc.student.gayj5385.controller.reservable.ManageReservableListController;
import edu.faytechcc.student.gayj5385.controller.reservation.ManageReservationButtonController;
import edu.faytechcc.student.gayj5385.controller.reservation.ManageReservationComboBoxController;
import edu.faytechcc.student.gayj5385.controller.reservation.ManageReservationListController;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeListener;

public class AdminPanel extends JPanel
{
    // Fields
    private JButton logout;
    private JTabbedPane tabbedPane;
    private List<ReservableLocation> locations;
    private HashMap<ReservableLocation, List<Reservation>> reservations;
    private ManageReservablePanel mngReservablePanel;
    private ManageReservationPanel mngReservationPanel;
    private SettingsPanel settingsPanel;
    
    /**
        Constructs a new AdminPanel initialized with the given listing of
        locations & mapping of reservations
    
        @param locs The locations
        @param reserves The reservations
    */
    
    public AdminPanel(List<ReservableLocation> locs,
        HashMap<ReservableLocation, List<Reservation>> reserves)
    {
        super(new BorderLayout());
        
        locations = locs;
        reservations = reserves;
        
        tabbedPane = new JTabbedPane();
        tabbedPane.setBorder(BorderFactory.createEtchedBorder());
        
        tabbedPane.addChangeListener(new AdminPanelController(this));
        
        buildManageReservablePanel(locations);
        buildManageReservationPanel(reservations);
        buildSettingsPanel();
        buildBottomPanel();
        
        tabbedPane.add("Manage Reservables", mngReservablePanel);
        tabbedPane.add("Manage Reservations", mngReservationPanel);
        tabbedPane.add("Settings", settingsPanel);
        
        add(tabbedPane, BorderLayout.CENTER);
    }
    
    /**
        Builds the bottom panel of this panel
    */
    
    private void buildBottomPanel()
    {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        panel.add(logout = new JButton("Logout"));
        
        logout.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                MainPanel parent = ((MainPanel)getParent());
                parent.showOpenPanel();
            }
        });
        
        add(panel, BorderLayout.SOUTH);
    }
    
    /**
        Builds the panel to manage reservables on. initialized with the given
        list of locations
    
        @param locations The locations
    */
    
    private void buildManageReservablePanel(List<ReservableLocation> locations)
    {
        mngReservablePanel = new ManageReservablePanel();
        
        Filter<ReservableTimeframe> timeframeFilter = new Filter<>();
        Filter<ReservableLocation> locationFilter = new Filter<>();
        
        mngReservablePanel.registerButtonController(
            new ManageReservableButtonController(mngReservablePanel,
                locations, timeframeFilter, locationFilter));
        
        mngReservablePanel.registerComboBoxController(
            new ManageReservableComboBoxController(mngReservablePanel,
                timeframeFilter));
        
        mngReservablePanel.registerTimeframeListController(
            new ManageReservableListController(mngReservablePanel));        
    }
    
    /**
        Builds the panel to manage reservations on, initialized with
        the given mapping of reservations
    
        @param reservations ReservableLocation reservation mapping
        @return The built panel
    */
    
    private void buildManageReservationPanel(
            HashMap<ReservableLocation, List<Reservation>> reservations)
    {   
        mngReservationPanel = new ManageReservationPanel();
        
        Filter<ReservableLocation> locationFilter = new Filter<>();
        Filter<Reservation> reservationFilter = new Filter<>();
        
        mngReservationPanel.registerButtonController(
            new ManageReservationButtonController(mngReservationPanel,
                reservations, locationFilter, reservationFilter));
        
        mngReservationPanel.registerComboBoxController(
            new ManageReservationComboBoxController(mngReservationPanel,
                reservations, reservationFilter));
        
        mngReservationPanel.registerReservationListController(
            new ManageReservationListController(mngReservationPanel));
    }
    
    /**
        Builds the panel allowing updates to settings to be made
    
        @return The built panel
    */
    
    private void buildSettingsPanel()
    {
        settingsPanel = new SettingsPanel();
        settingsPanel.registerController(
            new SettingsPanelController(settingsPanel));
    }
    
    /**
        Return the name of the active tab
    
        @return The name of the active tab
    */
    
    public String getActiveTab()
    {
        return tabbedPane.getTitleAt(tabbedPane.getSelectedIndex());
    }
    
    /**
        Updates the settings panel's current settings input
        
        @param adminEmail Settings for the administrator's email
        @param guestEmail Settings for the guest's email
        @param db Settings for the database
        @param securityOptions Available security options to choose from
    */
    
    public void setSettingsSettings(EmailSettings adminEmail,
            EmailSettings guestEmail, DatabaseSettings db,
            SecurityOption[] securityOptions)
    {
        settingsPanel.setSecurityOptions(securityOptions);
        settingsPanel.setAdminEmailSettings(adminEmail);
        settingsPanel.setGuestEmailSettings(guestEmail);
        settingsPanel.setDBSettings(db);
    }
    
    /**
        Register change controller to the tabbed pane
    
        @param controller The controller to register
    */
    
    public void registerChangeController(ChangeListener controller)
    {
        tabbedPane.addChangeListener(controller);
    }
    
    /**
        Updates the panel's children with the latest model
    */
    
    public void updateModel()
    {
        if (locations.size() > 0)
        {
            mngReservablePanel.setLocations(locations);
            mngReservablePanel.setTimeframes(locations.get(0).getTimeframes());
        }
        
        if (reservations.size() > 0)
        {
            List<ReservableLocation> reservedLocs = new ArrayList<>(
                    reservations.keySet());
            
            mngReservationPanel.setLocations(reservedLocs);
            
            ReservableLocation loc = reservedLocs.get(0);
            mngReservationPanel.setReservations(reservations.get(loc));
        }
    }
}