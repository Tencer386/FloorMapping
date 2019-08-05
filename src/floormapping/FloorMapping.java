/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package floormapping;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Arrays;


public class FloorMapping extends JFrame implements ActionListener, KeyListener
{
    private int totalX = 11;
    private int totalY = 19;
    int ysizearray = 0;
    int xPos = 0;
    int yPos = 0;
	int tempValue = 0;

    private JTextField[][] fields;
    private String[][] tempfields;
    private JTextField[] headerArray;
    private String[][] tempHeaderArray;
    //private JTextField[][] fields = new JTextField[totalX][totalY]; 
    String[] sortArray;
    //String[] sortArray = new String[(totalY-3)];
    
    private JButton btnClear, btnSave, btnExit, btnSaveTable, btnSort, btnRAF, btnFind;
    private String dataFileName = "ClientRoomData_20180307.csv";
    private String tableFileName = "ClientRoomData_20180307.csv";
    private String rafFileName = "ClientRoomData_20180307RAF.csv";

//    private String[] headingsAtTheTop = {};
    private JTextField txtFind;
    SpringLayout springLayout;
    
    Color PURPLE = new Color(128, 0, 128);
    
    public static void main(String[] args)
    {
        FloorMapping billsReportingSystem = new FloorMapping();
        billsReportingSystem.run();
    }
    
    private void run()
    {
        getScreenDimensions(dataFileName);
        fields = new JTextField[11][19];
        tempfields = new String[5][209];
        headerArray = new JTextField[8];
        tempHeaderArray = new String[4][2];
        sortArray = new String[(totalY-3)];
        
        setBounds(10, 10, xPos, yPos);
        setTitle("Floor Mapping");
        
        addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                System.exit(0);
            }
        });
        
        readDataFile(dataFileName);
        displayGUI();
        
        setResizable(true);
        setVisible(true);
    }

    
    //<editor-fold defaultstate="collapsed" desc="Display GUI">    

    private void getScreenDimensions(String fileName)
    {
        try
        {
//            int count = 0;
//            String line;
//            BufferedReader br = new BufferedReader(new FileReader(fileName));
//            while ((line = br.readLine()) != null)
//            {
//                String temp[] = line.split(",");                    
//                count++;
//                totalX = temp.length + 1;
//            }
//            totalY = count + 2;
            xPos = totalX * 65 + 400;
            if(xPos < 825) { xPos = 825; }
            yPos = totalY * 22 + 150;
            
//            br.close();
        }
        catch (Exception e)
        {
            System.err.println("Error: " + e.getMessage());            
        }
    }
    
    private void displayGUI()
    {
        springLayout = new SpringLayout();
        setLayout(springLayout);
        
        
        displayTextFields(springLayout);
        displayButtons(springLayout);
        setupTable(springLayout);
        displayTextInfo();
    }

	
/*	//Version 1...
    private void displayTextFields(SpringLayout layout)
    {
        for (int y = 0; y < totalY; y++)
        {
            for (int x = 0; x < totalX; x++)     
            {
                xPos = x * 65 + 20;
                yPos = y * 22 + 20;           
				JTextField fields[x][y] = new JTextField(5);
				add(fields[x][y]);  
				fields[x][y].addKeyListener(fields[x][y]);
				layout.putConstraint(SpringLayout.WEST, fields[x][y], xPos, SpringLayout.WEST, this);
				layout.putConstraint(SpringLayout.NORTH, fields[x][y], yPos, SpringLayout.NORTH, this);
            }
        }
    }
*/
	
    private void displayTextFields(SpringLayout layout)
    {
        for (int y = 0; y < totalY; y++)
        {
            for (int x = 0; x < totalX; x++)     
            {
                xPos = x * 92 + 60;
                yPos = y * 20 + 45;           
                fields[x][y] = LibraryComponents.LocateAJTextField(this, this, layout, 8, xPos, yPos);
            }
        }
        
        for (int n = 0; n < 8; n++)
        {
            xPos = n * 92 + 60;
            yPos = 20;           
            headerArray[n] = LibraryComponents.LocateAJTextField(this, this, layout, 8, xPos, yPos);
        }
    }
    
    private void displayTextInfo()
    {
        for (int y = 0; y < ysizearray; y++)
        {
            int tempx = Integer.parseInt(tempfields[0][y]);
            int tempy = Integer.parseInt(tempfields[1][y]);
            fields[tempx][tempy].setText(tempfields[2][y]);
            
            String tempColour = tempfields[3][y];
            Color colourValue = null;
            switch(tempColour.toUpperCase())
            {
                case "GREEN":
                    colourValue = Color.GREEN;
                    break;
                case "BLUE":
                    colourValue = Color.BLUE;
                    break;
                case "YELLOW":
                    colourValue = Color.YELLOW;
                    break;
                case "PURPLE":
                    colourValue = PURPLE;
                    break;
            }
            setFieldColour(tempx, tempy, colourValue);
        }
        
        int j = 0;
        for (int n = 0; n < 4; n++)
        {
            headerArray[n + j].setText(tempHeaderArray[n][0]);
            j++;
        }
        j = 1;
        for (int n = 0; n < 4; n++)
        {
            headerArray[n + j].setText(tempHeaderArray[n][1]);
            j++;
        }
    }
    
    private void setFieldColour(int tempx, int tempy, Color colourValue)
    {
        fields[tempx][tempy].setBackground(colourValue);
    }
    
    private void displayButtons(SpringLayout layout)
    {
        int yPos = totalY * 22 + 40;
        int xTension = 0;
        if (totalX > 12) { xTension = ((totalX - 12) * 65); }
                
        btnClear = LibraryComponents.LocateAJButton(this, this, layout, "Clear", 20, yPos, 80, 25);
        btnSaveTable = LibraryComponents.LocateAJButton(this, this, layout, "Save Table", 100, yPos, 120, 25);
        btnSave = LibraryComponents.LocateAJButton(this, this, layout, "Save", 220, yPos, 80, 25);
        btnSort = LibraryComponents.LocateAJButton(this, this, layout, "Sort", 300, yPos, 80, 25);
        btnFind = LibraryComponents.LocateAJButton(this, this, layout, "Find", 555 + xTension, yPos, 80, 25);
        btnRAF = LibraryComponents.LocateAJButton(this, this, layout, "RAF", 635 + xTension, yPos, 80, 25);
        btnExit = LibraryComponents.LocateAJButton(this, this, layout, "Exit", 715 + xTension, yPos, 80, 25);
        txtFind = LibraryComponents.LocateAJTextField(this, this, layout, 13, 405 + xTension, yPos+4);
        txtFind.addActionListener(this);  
    }  
    
    private void setupTable(SpringLayout layout)
    {
//        for (int y = 0; y < totalY; y++)
//        {
//            for (int x = 0; x < totalX; x++)
//            {
//                setFieldProperties(x, y, true, 255, 255, 255);
//            }
//        }
//        for (int y = 0; y < totalY; y++)
//        {
//            setFieldProperties(0, y, false, 220, 220, 255);
//            setFieldProperties(totalX-1, y, false, 220, 255, 220);
//        }
//        for (int x = 0; x < totalX; x++)
//        {
//            fields[x][0].setText(headingsAtTheTop[x]);
//            setFieldProperties(x, 0, false, 220, 220, 255);
//            setFieldProperties(x, 1, true, 220, 255, 220);
//            setFieldProperties(x, totalY-1, false, 220, 255, 220);
//        }
//        fields[totalX-1][0].setText("Results");
//        fields[0][totalY - 1].setText("Mode");
    } 

    public void setFieldProperties(int x, int y, boolean editable, int r, int g, int b)
    {
        fields[x][y].setEditable(editable);
        fields[x][y].setBackground(new Color(r, g, b));
    }
    

    //</editor-fold>

                
    //<editor-fold defaultstate="collapsed" desc="Action and Key Listeners">    
    
    @Override
    public void actionPerformed(ActionEvent e)
    {
        setupTable(springLayout);

        if (e.getSource() == btnClear)
        {
            LibraryComponents.clearJTextFieldArray(fields,2,1,totalX,totalY);
        }
        if (e.getSource() == btnSaveTable)
        {
            saveTableToFile(tableFileName);
        }
        if (e.getSource() == btnSave)
        {
            writeDataFile(dataFileName);
            getFieldInfo(dataFileName);
        }
        if (e.getSource() == btnSort)
        {
            sortStudentRecords();
        }
        if (e.getSource() == btnFind  || e.getSource() == txtFind)
        {
            findStudentRecord();
        }
        if (e.getSource() == btnRAF)
        {
            writeRandomAccessFile(rafFileName);
            int requiredEntry = Integer.parseInt(checkInteger(txtFind.getText()));
            readRandomAccessFile(rafFileName,requiredEntry);
        }        
        if (e.getSource() == btnExit)
        {
            System.exit(0);
        }
    }

    @Override
    public void keyTyped(KeyEvent e)  {  }
    @Override
    public void keyPressed(KeyEvent e)  {  }
    @Override
    public void keyReleased(KeyEvent e)
    {
        
    }
    
    //</editor-fold>
    

    //<editor-fold defaultstate="collapsed" desc="Manage Screen Data">    
  
    
//    MOVED TO:  LIBRARY COMPONENTS...
//
//    public void ClearData(JTextField[][] JTxtFld, int maxX, int maxY)
//    {
//        for (int y = 2; y < maxY; y++)
//        {
//            for (int x = 1; x < maxX; x++)
//            {
//                JTxtFld[x][y].setText("");
//            }
//        }
//    }

//    MOVED TO:  LIBRARY COMPONENTS...
//
//    public int getLargestIndex(int arr[])
//    {
//        int largestIndex = -1;
//        int largestValue = -1;
//        for (int i = 0; i<arr.length; i++)
//        {
//            if(arr[i] > largestValue)
//            {
//                largestValue = arr[i];
//                largestIndex = i;
//            }
//        }        
//        return largestIndex;       
//    }
//
//    public int getLargestValue(int arr[])
//    {
//        int largestValue = -1;
//        for (int i = 0; i<arr.length; i++)
//        {
//            if(largestValue > arr[i])
//            {
//                largestValue = arr[i];
//            }
//        }        
//        return largestValue;       
//    }
    

    public String checkInteger(String strValue)
    {
        try 
        {
            Integer.parseInt(strValue);
            return strValue;
        }
        catch (Exception e) 
        {
            return "0";
        }
    }


	
    //</editor-fold>
    
    
    //<editor-fold defaultstate="collapsed" desc="Find and Sort Code">    
    
    public void findStudentRecord()
    {
        boolean found = false;
        int y = 2;
        String strFind = txtFind.getText();
        
        while(y < totalY-1 && found == false)
        {
           if(fields[0][y].getText().equalsIgnoreCase(strFind))
           {
               found = true;
           }
           y++;
        }
        if (found)
        {
            for (int x = 0; x < totalX; x++)
            {
                fields[x][y-1].setBackground(new Color(255,217,200));
            }
            txtFind.setText(txtFind.getText() + " ...Found.");
        }
        else
        {
            txtFind.setText(txtFind.getText() + " ...Not Found.");
        }
    }

    public void sortStudentRecords()
    {
        copyToSortTable();
        sortTheSortTable();
        displaySortedTable();
    }

    public void copyToSortTable()
    {
        for (int y = 2; y < totalY - 1; y++)
        {
            sortArray[y-2] = "";
            for (int x = 0; x < totalX-1; x++)
            {
                sortArray[y-2] = sortArray[y-2] + fields[x][y].getText() + ",";
            }
            sortArray[y-2] = sortArray[y-2] + fields[totalX - 1][y].getText();
        }      
    }
 
    public void sortTheSortTable()
    {
        Arrays.sort(sortArray);
    }

    public void displaySortedTable()
    {
        for (int y = 2; y < totalY - 1; y++)
        {
            String temp[] = sortArray[y-2].split(",");                    
            for (int x = 0; x < totalX; x++)
            {
                fields[x][y].setText(temp[x]);
            }
        }
    }
           
    //</editor-fold>    

    
    //<editor-fold defaultstate="collapsed" desc="File Management">    

    private void readDataFile(String fileName)
    {
        try
        {
            String line;
            int j = 0;
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            while (j <= 3)
            {
                line = br.readLine();
                String temp[] = line.split(","); 
                for (int n = 0; n <= 1; n++)
                {
                    tempHeaderArray[j][n] = temp[n];
                }
                j++;
            }
            int y = 0;
            while ((line = br.readLine()) != null)
            {
                String temp[] = line.split(",");                    
                for (int x = 0; x <= 4; x++)
                {
                    tempfields[x][y] = temp[x];
                }
                y++;
                System.out.println(y + "-");
                System.out.println(tempfields[2][y - 1]);
                ysizearray = y;
            }
//            for (int y = 4; y < totalY; y++)
//            {
//                String temp[] = br.readLine().split(",");                    
//                for (int x = 0; x < 4; x++)
//                {
//                    tempfields[x][y].setText(temp[x]);
//                }
//            }
            br.close();
        }
        catch (Exception e)
        {
            System.err.println("Error: " + e.getMessage());            
        }
    }
    
    private void getFieldInfo(String fileName)
    {
        try
        {
            BufferedWriter outFile = new BufferedWriter(new FileWriter("FloorMapping_NEW_TEST.csv"));
            for(int y = 0; y < totalY; y++)
            {
                for(int x = 0; x < totalX; x++)
                {
                    String tempInfo = fields[x][y].getText();
                
                if(!tempInfo.equals(""))
                    {
                        Color color = fields[x][y].getBackground();
                        String strColor;
                        if (color == Color.GREEN) {
                            strColor = "Green";
                        } else if (color == Color.BLUE) {
                            strColor = "Blue";
                        } else if (color == Color.YELLOW){
                            strColor = "Yellow";
                        } else if (color == PURPLE){
                            strColor = "Purple";
                        } else {
                            strColor = null;
                        }
                        
                        String strMove;
                        if(tempInfo == "Chair")
                        {
                            strMove = "No";
                        } else {
                            strMove = "Yes";
                        }
                        
                        outFile.write(x + "," + y + "," + tempInfo + "," + strColor + "," + strMove);
                        outFile.newLine();
                    }
                }
            }
            outFile.close();
            System.out.println("Floor Mapped Saved! - Test");
        }
        catch(Exception e)
        {
            System.err.println("Error: " + e.getMessage());
        }
    }
    
    public void writeDataFile(String fileName)
    {
        try
        {
            //BufferedWriter outFile = new BufferedWriter(new FileWriter(fileName));
            BufferedWriter outFile = new BufferedWriter(new FileWriter("FloorMapping_NEW.csv"));
            for (int y = 1; y < totalY - 1; y++)
            {
                for (int x = 0; x < totalX - 2; x++)
                {
                    outFile.write(fields[x][y].getText() + ",");
                }
                outFile.write(fields[totalX - 2][y].getText());
                outFile.newLine();
            }
            outFile.close();
            System.out.println("Floor Mapped Saved!");
        }
        catch (Exception e)
        {
            System.err.println("Error: " + e.getMessage());
        }
    }

    public void saveTableToFile(String fileName)
    {
        try
        {
            BufferedWriter outFile = new BufferedWriter(new FileWriter(fileName));
            for (int y = 0; y < totalY; y++)
            {
                for (int x = 0; x < totalX - 1; x++)
                {
                    outFile.write(fields[x][y].getText() + "," );               
                }
                outFile.write(fields[totalX - 1][y].getText());
                outFile.newLine();
            }
            outFile.close();
            System.out.println("Bills Reporting System TABLE has been saved.");
        }
        catch (Exception e)
        {
            System.err.println("Error: " + e.getMessage());
        }
    }

    //</editor-fold>    

    
    //<editor-fold defaultstate="collapsed" desc="Random Access File">    

    public void writeRandomAccessFile(String fileName)
    {
        try
        {
            String str;
            RandomAccessFile rafFile = new RandomAccessFile(fileName, "rw");
            for (int y = 1; y < totalY - 1; y++)
            {
                str = "";
                for (int x = 0; x < totalX - 1; x++)
                {
                    str = str + fields[x][y].getText();
                }
                rafFile.writeUTF(str);
            }
            rafFile.close();
            System.out.println("Bills Reporting System RAF data has been saved.");
        }
        catch (Exception e)
        {
            System.err.println("Error: " + e.getMessage());
        }
    }
    

    private void readRandomAccessFile(String fileName, int index)
    {
        try
        {
            String str = "";
            RandomAccessFile rafFile = new RandomAccessFile(fileName, "rw");
            for(int i = 0; i<index; i++)
            {
                str = rafFile.readUTF();
            }
            System.out.println(str);
            rafFile.close();
        }
        catch (Exception e)
        {
            System.err.println("Error: " + e.getMessage());            
        }
    }
    
    
    //</editor-fold>       
    
}


