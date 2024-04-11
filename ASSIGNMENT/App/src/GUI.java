import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;

public class GUI extends JFrame
{
    //temp path for csv file
    public ArrayList<Project> fullList = App.parseCSV("C:/Users/skye/Documents/GitHub/CSCU9T4_Assignment_2/ASSIGNMENT/App/src/scotia_visual_productions_projects.csv");
    //filter options for filter search
    public String filters[] = {"","Type", "Location", "Venue", "Before Date", "After Date"};

    //Column headers for table
    public String col[] = {"Project ID", "Project Name", "Project Type", "Project Date",
                            "Project Location", "Project Cost", "Project Cost To Customer",
                            "Size of Venue", "Project Duration", "Duration Units", "Network", 
                            "Play Wright", "Genre", "Format"};

    //constructor used to load the first state of the program
    GUI(String title)
    {
        JButton b = new JButton("Load CSV");
        b.setPreferredSize(new Dimension(100,100));
        add(b);
        b.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                remove(b);
                initialState();
                revalidate();
                repaint();
            }
        });
        
        initWindow();
    }    

    //initialise the JFrame window
    public void initWindow()
    {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Scotia Visual Productions Projet Viewer");
        this.setPreferredSize(new Dimension(1920,1080));
        this.setLayout(new FlowLayout());
        this.pack();
        this.setVisible(true);
    }
    
    //initial state after csv is loaded / no filters used
    public void initialState()
    {
        JTable table = loadInitialTable();
                
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(1800,720));
        add(scrollPane);

        JComboBox<String> cb = filterBoxInit();
        add(cb);

        JTextField filterSearch = searchBoxInit();
        add(filterSearch);

        JTextPane tp = loadToolTip();
        add(tp);

        cb.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                if(((String)cb.getSelectedItem()).equals("Before Date") || (((String)cb.getSelectedItem()).equals("After Date")))
                {
                    tp.setText("Date format MUST be: 'dd/MM/yyyy' \n e.g. 11/08/2004 (11th August 2004)");
                } 
                else if((((String)cb.getSelectedItem()).equals("Type")))
                {
                    tp.setText("will return an empty table if the input ISN'T exactly equal to a type\n e.g. 'Music' or 'Film'");
                }
                else if(((String)cb.getSelectedItem()).equals("Location"))
                {
                    tp.setText("will return an empty table if input ISN'T exactly equal to a location\n e.g. 'Los Angeles' or 'Glasgow'");
                }
                else if((((String)cb.getSelectedItem()).equals("Venue")))
                {
                    tp.setText("will return an empty table if input ISN'T exactly equal to a venue size\n e.g. 'Small' or 'Medium' or 'Large'");
                } else 
                {
                    tp.setText("no filter will return the full list");
                }
            }
        });

        JButton filterSubmit = submitButtonInit();
        add(filterSubmit);
        filterSubmit.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e) 
            {
                remove(cb);
                remove(filterSearch);
                remove(filterSubmit);
                remove(scrollPane); 
                remove(tp);
                
                Press((String)cb.getSelectedItem(), filterSearch.getText());
            }
        });
    }
    
    //redraws the JFrame for when changes to the components are made
    public void reDraw()
    {
        repaint();
        revalidate();
    }

    //use this state when filters are being used
    public void newState(String[][] data)
    {
        JTable table = loadTable(data);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(1850,720));
        add(scrollPane);

        JComboBox<String> cb = filterBoxInit();
        JTextField sb = searchBoxInit();
        JButton b = submitButtonInit();
        JTextPane tp = loadToolTip();

        add(cb);
        add(sb);
        add(b);
        add(tp);

        cb.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                if(((String)cb.getSelectedItem()).equals("Before Date") || (((String)cb.getSelectedItem()).equals("After Date")))
                {
                    tp.setText("Date format MUST be: 'dd/MM/yyyy' \n e.g. 11/08/2004 (11th August 2004)");
                } 
                else if((((String)cb.getSelectedItem()).equals("Type")))
                {
                    tp.setText("will return an empty table if the input ISN'T exactly equal to a type\n e.g. 'Music' or 'Film'");
                }
                else if(((String)cb.getSelectedItem()).equals("Location"))
                {
                    tp.setText("will return an empty table if input ISN'T exactly equal to a location\n e.g. 'Los Angeles' or 'Glasgow'");
                }
                else if((((String)cb.getSelectedItem()).equals("Venue")))
                {
                    tp.setText("will return an empty table if input ISN'T exactly equal to a venue size\n e.g. 'Small' or 'Medium' or 'Large'");
                } else 
                {
                    tp.setText("no filter will return the full list");
                }
            }
        });

        b.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                remove(cb);
                remove(sb);
                remove(b);
                remove(scrollPane);
                remove(tp);
                
                Press((String)cb.getSelectedItem(), sb.getText());
            }
        });

    }

    //logic for when submit button is pressed
    public void Press(String filter, String input)
    {
        ArrayList<Project> filteredList = new ArrayList<>();
        if(filter.equals("Type"))
        {
            filteredList = App.typeSearch(fullList, input);
            newState(pullData(filteredList));
            reDraw();
        }
        else if(filter.equals("Location"))
        {
            filteredList = App.locationSearch(fullList, input);
            newState(pullData(filteredList));
            reDraw();
        }
        else if(filter.equals("Venue"))
        {
            filteredList = App.venueSizeSearch(fullList, input);
            newState(pullData(filteredList));
            reDraw();
        } else if(filter.equals("Before Date"))
        {
            filteredList = App.dateFilter(input, true, fullList);
            newState(pullData(filteredList));
            reDraw();
        } else if(filter.equals("After Date"))
        {
            filteredList = App.dateFilter(input, false, fullList);
            newState(pullData(filteredList));
            reDraw();
        }
        else //case where no filters / incomplete filters are used, resort to full table
        {
            initialState();
            reDraw();
        }    
    }
    
    //initalises the submit button
    public JButton submitButtonInit()
    {
        JButton b = new JButton("Search!");
        b.setPreferredSize(new Dimension(100,50));
        return b;
    }

    //inialises the filter text input
    public JTextField searchBoxInit()
    {
        JTextField sb = new JTextField();
        sb.isEditable();
        sb.setPreferredSize(new Dimension(300,50));
        return sb;
    }

    //initalises the dropdown box for filters
    public JComboBox<String> filterBoxInit()
    {
        JComboBox<String> cb = new JComboBox<>(filters);
        cb.setPreferredSize(new Dimension(100,50));
        return cb;
    }

    //initialise tooltip text
    public JTextPane loadToolTip()
    {
        JTextPane tp = new JTextPane();
        tp.setEditable(false);
        tp.setText("no filter will return the full list");

        return tp;
    }

    //initialises a table component
    public JTable loadTable(String[][] data)
    {
        JTable t = new JTable(data, col);
        return t;
    }

    //creates a table for the full list
    public JTable loadInitialTable()
    {
        JTable t = new JTable(loadFullList(), col);
        return t;
    }

    //loads full list
    public String[][] loadFullList()
    {
            return pullData(fullList);
    }

    //pulls data from arraylist into something the tables can use (an array of arrays)
    public String[][] pullData(ArrayList<Project> list)
    {
        String[][] data = new String[list.size()][14];
        for(int i = 1; i < list.size(); i++)
        {
            String row[] =
            {
                list.get(i).project_id,
                list.get(i).project_name,
                list.get(i).project_type,                  
                list.get(i).project_date,
                list.get(i).project_location,
                list.get(i).project_cost,
                list.get(i).project_customer_cost,
                list.get(i).size_of_venue,
                list.get(i).project_duration,
                list.get(i).duration_units,
                list.get(i).network,
                list.get(i).play_wright,
                list.get(i).genre,
                list.get(i).format   
            };

            for(int j = 0; j < 14; j++)
            {
                data[i-1][j] = row[j];
            }
        }
        return data;
    }
}