
import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.*;
import javax.swing.Timer;
import java.util.*;
import javax.sound.sampled.*;
import javax.sound.midi.*;
import java.lang.Character;
import javax.imageio.*;
/*
 *REQUIRED CLASSES:
 *Shape3DM
 *Point3DM
 *Rotation3D
 *ObserverM
 *Utils3DM
 *PointVector
 *
 *Counter
 *NumberQueue
 *GUI
 *FileHandler
 */
public class RobotControl{
    public static void main(String[] args){
        new ControlWindow();
    }
}

class ControlWindow extends JFrame implements ActionListener, ItemListener, MouseListener{
    int X = 926;
    int Y = 156;
    
    final Dimension ScreenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
    
    String CurrentDirectory = System.getProperty("user.dir");
    double[][] UsedData;
    double FramesPerSecond = 1000.0 / 20;
    double TotalTime;
    double PreviousTime;
    double EffectiveTime;
    Timer Stopwatch = new Timer((int)(GUI.Speed * 20), this);

   
    
    
    FileHandler StreamFeeder;
    boolean Streaming;
    boolean Editing; 
    
    JButton LoadButton = new JButton("Load File");
    JButton SaveButton = new JButton("Set File");
    JButton ToggleButton = new JButton("Start");
    JButton ResetButton = new JButton("Reset");
    
    JButton NextButton = new JButton("Next");
    JButton PreviousButton = new JButton("Prev");
    
    JButton ModifyButton = new JButton("Edit");
    JButton StoreButton = new JButton("Save");
    JTextField TimeField = new JTextField("0.0");
    
    JTextField BreakField = new JTextField("0.0");
    
    JTextField IncrementField = new JTextField("20");
    JTextField ClockField = new JTextField("0.0");
    
    JLabel LoadLabel = new JLabel("No File Loaded");
    JLabel SaveLabel = new JLabel("No File Selected");
    JTextField LoadTextField = new JTextField("Copy of RobotWheelSpeed.txt", 10);
    JTextField SaveTextField = new JTextField("RobotWheelStream.txt", 10);
    
    JTextArea DataTextArea = new JTextArea("");
    JScrollPane DataScrollPane = new JScrollPane(DataTextArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    
    
    JTextArea SourceTextArea = new JTextArea("");
    JScrollPane SourceScrollPane = new JScrollPane(SourceTextArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    JScrollBar SourceScrollBar = SourceScrollPane.getVerticalScrollBar();
    
    Font StandardFont = new Font("COURIER", Font.PLAIN, 12);
    double BreakPoint = 0;
    int MouseX;
    int MouseY;
    boolean ClickingL;
    boolean ClickingR;
    int CurrentLine;
    long FrameCounter;
    Vector ShapesToPrint = new Vector();
    NumberQueue DelayList = new NumberQueue(10);
    
    double Temp;
    double AvTime;
    int Count;
    String[] PreviousOutput = new String[4];
    public void setCorners(Component Target, int TX, int TY, int BX, int BY){
        Target.setBounds(TX, TY, BX - TX, BY - TY);
    }
    public void updateTime(){
        if(Temp == 0)Temp = System.currentTimeMillis();
        AvTime = (AvTime * Count + System.currentTimeMillis() - Temp) / (++Count);
            
        System.out.println((int)(AvTime * 100)/100.0);
        DelayList.add(System.currentTimeMillis() - Temp);
        FramesPerSecond = 1000.0 / DelayList.average();
        PreviousTime = TotalTime;
        TotalTime = TotalTime + 1.0 / FramesPerSecond;
        Temp = System.currentTimeMillis();
    }
    public double[][] arrayExpand(String[] Input){
        String[][] ExpandedInput = new String[Input.length][];
        double[][] UsedArray = new double[Input.length][4];
        for(int i = 0; i < Input.length; i++){
            ExpandedInput[i] = Input[i].split("\t");
        }
        for(int i = 0; i < ExpandedInput.length; i++){
            for(int j = 0; j < ExpandedInput[i].length; j++){
                if(ExpandedInput[i][j].equals("^")){
                    UsedArray[i][j] = UsedArray[i-1][j];
                }else{
                    UsedArray[i][j] = Double.parseDouble(ExpandedInput[i][j]);
                }
            }
        }
        return UsedArray ;
    }
    public String[] arrayCompress(double[][] Array){
        for(int i = 0; i < Array.length; i++){
            for(int j = 0; j < Array[i].length; j++){
                Array[i][j] = GUI.round(Array[i][j], 100);
            }
        }
        Vector Output = new Vector();
        for(int i = 0; i < Array.length; i++){
            Output.add(Array[i][0] + "\t" + (i != 0 && Array[i][1] == Array[i-1][1] ? "^" : ""+Array[i][1]) + "\t" + (i != 0 && Array[i][2] == Array[i-1][2] ? "^" : ""+Array[i][2]) + "\t" + (i != 0 && Array[i][3] == Array[i-1][3] ? "^" : ""+Array[i][3]));
        }
        String[] FinalOutput = new String[Output.size()];
        Output.toArray(FinalOutput);
        return FinalOutput;
    }
    public void printData(JTextArea Target, JScrollBar TargetBar, double[][] Data){
        String[] RawData = new String[Data.length];
       
        for(int i = 0; i < RawData.length; i++){
            RawData[i] = "";
            if(i != 0){
                RawData[i] += " \n";
            }
            for(int j = 0; j < UsedData[i].length; j++){
            
                RawData[i] += GUI.processNumber((j == 0 ? 4 : 3), Data[i][j], 3);
            }
            
        }

        for(int i = 0; i < RawData.length; i++){
            Target.append(RawData[i]);
        }
   
        TargetBar.setMaximum(RawData.length * 17);
        
        TargetBar.setUnitIncrement(17);
    }
    public void loadData(){
        try{
            
            String Path = FilePicker.userGetFile("Load Input", LoadTextField.getText(), this);
            File Target = new File(Path);
            if(!Target.exists()){
                throw new IOException();
            }
            String[] RawData = FileHandler.readFile(LoadTextField.getText());
            UsedData = arrayExpand(RawData);
            printData(SourceTextArea, SourceScrollBar, UsedData);
            LoadTextField.setText(Path);
            LoadLabel.setText("Loading Successful");
        }catch(Exception e){
            LoadLabel.setText("Loading Failed");
        }
    }
    public void init(){
        DataTextArea.setEditable(false);
        SourceTextArea.setEditable(false);
        enable();
    }
    
    public void setLayout(Container ContentArea){
        ContentArea.setLayout(null);
        JPanel LeftArea = new JPanel();
        JPanel LMidArea = new JPanel();
        JPanel MidArea = new JPanel();
        JPanel RightArea = new JPanel();
        LeftArea.setLayout(null);
        LMidArea.setLayout(null);
        MidArea.setLayout(null);
        RightArea.setLayout(null);
        ContentArea.setBackground(Color.gray);
        LeftArea.setBackground(Color.gray);
        LMidArea.setBackground(Color.gray);
        MidArea.setBackground(Color.gray);
        RightArea.setBackground(Color.gray);
        //LeftArea.setBackground(Color.red);
        //LMidArea.setBackground(Color.yellow);
        //MidArea.setBackground(Color.blue);
        //RightArea.setBackground(Color.green);
        LeftArea.setBounds(0, 0, 284, 160);
        LMidArea.setBounds(284, 0, 228, 160);
        MidArea.setBounds(512, 0, 180, 160);
        RightArea.setBounds(692, 0, 228, 160);
        ContentArea.add(LeftArea);
        ContentArea.add(LMidArea);
        ContentArea.add(MidArea);
        ContentArea.add(RightArea);
        LeftArea.addMouseListener(this);
        RightArea.addMouseListener(this);
        
        
        
        
        LeftArea.add(LoadButton);
        LoadButton.addActionListener(this);
        setCorners(LoadButton, 4, 4, 88, 30);
        LoadButton.setToolTipText("Loads a target data-file into memory to be streamed from or edited");
        
        LeftArea.add(LoadTextField);
        setCorners(LoadTextField, 92, 4, 280, 30);
        LoadTextField.setToolTipText("The address of the data-file loaded");
        LoadTextField.setEditable(false);
        
        LeftArea.add(LoadLabel);
        setCorners(LoadLabel, 4, 34, 280, 60);
        
        
        LeftArea.add(SaveButton);
        SaveButton.addActionListener(this);
        setCorners(SaveButton, 4, 64, 88, 90);
        SaveButton.setToolTipText("Selects a target file for the data to be streamed into");
        
        LeftArea.add(SaveTextField);
        setCorners(SaveTextField, 92, 64, 280, 90);
        SaveTextField.setToolTipText("The address of the file to be streamed to");
        SaveTextField.setEditable(false);
        
        LeftArea.add(SaveLabel);
        setCorners(SaveLabel, 4, 94, 280, 120);
        
        LeftArea.add(ModifyButton);
        ModifyButton.addActionListener(this);
        setCorners(ModifyButton, 280 - 88, 34, 280, 60);
        ModifyButton.setToolTipText("Edit the loaded data");
        
        LeftArea.add(StoreButton);
        StoreButton.addActionListener(this);
        setCorners(StoreButton, 280 - 88, 94, 280, 120);
        StoreButton.setToolTipText("Save the edited version of the loaded data into the original file");
        
        LMidArea.add(SourceScrollPane);
        setCorners(SourceScrollPane, 4, 4, 224, 120);
        SourceTextArea.setFont(StandardFont);
        SourceTextArea.setToolTipText("The loaded data");
        
        
        
        MidArea.add(TimeField);
        TimeField.setHorizontalAlignment(JTextField.CENTER);
        setCorners(TimeField, 4, 4, 88, 30);
        TimeField.setToolTipText("The time to use when selecting data to stream");
        
        MidArea.add(NextButton);
        setCorners(NextButton, 4, 34, 88, 60);
        NextButton.addActionListener(this);
        NextButton.setToolTipText("Increments the time to the next data-point");
        
        MidArea.add(PreviousButton);
        setCorners(PreviousButton, 4, 64, 88, 90);
        PreviousButton.addActionListener(this);
        PreviousButton.setToolTipText("Decrements the time to the previous data-point");
        
        MidArea.add(BreakField);
        BreakField.setHorizontalAlignment(JTextField.CENTER);
        setCorners(BreakField, 4, 94, 88, 120);
        BreakField.setToolTipText("Set an arbitrary break-point");
        
  
        
        MidArea.add(ClockField);
        ClockField.setHorizontalAlignment(JTextField.CENTER);
        setCorners(ClockField, 92, 4, 176, 30);
        ClockField.setToolTipText("Time used in time-stamp of output");
        
        MidArea.add(ToggleButton);
        setCorners(ToggleButton, 92, 34, 176, 60);
        ToggleButton.addActionListener(this);
        ToggleButton.setToolTipText("Begin or Stop streaming");
        
        MidArea.add(ResetButton);
        setCorners(ResetButton, 92, 64, 176, 90);
        ResetButton.addActionListener(this);
        ResetButton.setToolTipText("Resets the simulator, including both timings");
        
        MidArea.add(IncrementField);
        IncrementField.setHorizontalAlignment(JTextField.CENTER);
        setCorners(IncrementField, 92, 94, 176, 120);
        IncrementField.setToolTipText("Sets the delay between streaming frames");
        
        
        
        RightArea.add(DataScrollPane);
        
        setCorners(DataScrollPane, 4, 4, 224, 120);
        DataTextArea.setFont(StandardFont);
        DataTextArea.setToolTipText("Data streamed so far");
        
        
    }
    public ControlWindow(){
        //BASIC INITIALIZATION
        super("Window"); 
        Stopwatch.stop();
        Stopwatch.setInitialDelay(0);
        this.setSize(X, Y);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        //SET CONTENT PANE
        Container ContentArea = getContentPane();
        

        this.setLocation(ScreenSize.width / 2 - X / 2, ScreenSize.height / 2 - Y / 2);
        
        
        
        
        
        //ADD CONTENT PANE AND PACK
        setLayout(ContentArea);
        init();
        this.setContentPane(ContentArea);
        this.show();
      //  Stopwatch.start();
        
        //this.pack();   
    }
    public void enable(){
        boolean Enable = !Streaming && !Editing;
        LoadButton.setEnabled(Enable);
        SaveButton.setEnabled(Enable);
        LoadTextField.setEnabled(Enable);
        SaveTextField.setEnabled(Enable);
        StoreButton.setEnabled(Enable && UsedData != null);
        ResetButton.setEnabled(Enable && UsedData != null && StreamFeeder != null);
        ToggleButton.setEnabled(UsedData != null && StreamFeeder != null);
       
        NextButton.setEnabled(Enable && UsedData != null);
        PreviousButton.setEnabled(Enable && UsedData != null);
        TimeField.setEnabled(Enable);
        ClockField.setEnabled(Enable);
        BreakField.setEnabled(Enable);
        
        
       
    }
    public Object getOutput(){
        int j = 0;
        
        for(j = 0; j < UsedData.length && UsedData[j][0] <= EffectiveTime; j++){}
        double[] Input;
        if(j - 1 < 0){
            Input = new double[]{TotalTime, 0, 0, 0};
        }else{
            Input = new double[]{TotalTime, UsedData[j - 1][1], UsedData[j - 1][2], UsedData[j - 1][3]};
        }
        CurrentLine = j - 1;
        String[] TempOutput = new String[4];
        String Output = "";
        
        for(int i = 0; i < Input.length; i++){
            TempOutput[i] = "" + GUI.round(Input[i], 100);
        }
        for(int i = 0; i < Input.length; i++){
            if(i != 0){
                Output += "\t";
            }
            if(TempOutput[i].equals(PreviousOutput[i])){
                TempOutput[i] = "^";
                Output += TempOutput[i];
            }else{
                Output += TempOutput[i];
                PreviousOutput[i] = TempOutput[i];
            }
            
        }
        if(TempOutput[1].equals("^") && TempOutput[2].equals("^") && TempOutput[3].equals("^")){
            Output = null;
        }
        
        return Output;
    }
    public void userGetFile(){
        
    }
    public void setStream(){
        String Path = FilePicker.userGetFile("Select Output", SaveTextField.getText(), this);
        if(Path != null){
            SaveTextField.setText(Path);
            StreamFeeder = new FileHandler(SaveTextField.getText(), false, true);   
            SaveLabel.setText("Ouput Selected");
        }else{
            SaveLabel.setText("Selection Failed");
        }
    }
    public void outputToScreen(Object Output){
        String[] Temp = ((String)Output).split("\t");
        Temp[0] = GUI.processNumber(4, Temp[0], 3);
        for(int i = 1; i < Temp.length; i++){
            
            if(Temp[i].equals("^")){
                Temp[i] = PreviousOutput[i];
            }
            Temp[0] += GUI.processNumber(3, Temp[i], 3);
        }
        DataTextArea.append((String)Temp[0] + "\n");   
    }
    public boolean parseData(String Data){
        String[] Chunks = Data.split("\n");
        for(int i = 0; i < Chunks.length; i++){
            if(Chunks[i].equals("")){
                return false;
            }
            String[] Bits = Chunks[i].split(" ");
            Chunks[i] = "";
            for(int j = 0; j < Bits.length; j++){
                Chunks[i] += Bits[j];
                if(!Bits[j].equals("")){
                    Chunks[i] += " ";
                }
            }
        }
        String[][] Grid = new String[Chunks.length][];
        for(int i = 0; i < Grid.length; i++){
            System.out.println(Chunks[i]);
            Grid[i] = Chunks[i].split(" ");
            if(Grid[i].length != 4){
                return false;
            }
            if(!(i == 0 || Double.parseDouble(Grid[i][0]) > Double.parseDouble(Grid[i - 1][0]))){
                return false;
            }
        }
        UsedData = new double[Grid.length][Grid[0].length];
        for(int i = 0; i < Grid.length; i++){
            for(int j = 0; j < Grid[i].length; j++){
                UsedData[i][j] = Double.parseDouble(Grid[i][j]);
            }
        }
        return true;
    }
    public void actionPerformed(ActionEvent Event){
        
        if(Event.getSource() == Stopwatch){
            //Increment Times
            updateTime();
            EffectiveTime += 1.0/FramesPerSecond;
            
            //Print output
            Object Output = getOutput();
            if(Output != null){
                StreamFeeder.writeFile(Output);
                outputToScreen(Output);
            }
            SourceScrollBar.setValue((CurrentLine) * 17);
            //Break if needed
            if(EffectiveTime > BreakPoint && BreakPoint > 0){
                actionPerformed(new ActionEvent(ToggleButton, 100, ""));
            }
            //Update Fields
            TimeField.setText(GUI.round(EffectiveTime, 100) + "");
            ClockField.setText(GUI.round(TotalTime, 100) + "");
        }else if(Event.getSource() == LoadButton){
            loadData();
            
            SourceScrollBar.setValue(6);
        }else if(Event.getSource() == SaveButton){
            setStream();
            
        }else if(Event.getSource() == ModifyButton){
            if(!Editing){
                System.out.println("A");
                Editing = true;
                SourceTextArea.setEditable(true);
                ModifyButton.setText("Finish");
            }else{
                boolean Valid = parseData(SourceTextArea.getText());
                if(Valid){
                    SourceTextArea.setText("");
                    printData(SourceTextArea, SourceScrollBar, UsedData);
                    System.out.println("B");
                    Editing = false;
                    SourceTextArea.setEditable(false);
                    ModifyButton.setText("Edit");
                }
                
            }
        }else if(Event.getSource() == StoreButton){
            String[] Text = arrayCompress(UsedData);
            FileHandler.writeFile(LoadTextField.getText(), Text);
           
            
        }else if(Event.getSource() == ToggleButton){
            Streaming = !Streaming;
          
            if(Streaming){
                try{
                Stopwatch.setDelay((int)Integer.parseInt(IncrementField.getText()));
                }catch(Exception e){}
                IncrementField.setText(Stopwatch.getDelay() + "");
                Temp = System.currentTimeMillis();
                Stopwatch.start();
                ToggleButton.setText("Stop");
                DelayList.initTo(20);
                //Work the Fields
                try{
                    TotalTime = Double.parseDouble(ClockField.getText());
                }catch(Exception e){
                    TotalTime = 0;
                }
                ClockField.setText(TotalTime + "");
                try{
                    EffectiveTime = Double.parseDouble(TimeField.getText());
                }catch(Exception e){
                    EffectiveTime = 0;
                }
                TimeField.setText(EffectiveTime + "");
                try{
                    BreakPoint = Double.parseDouble(BreakField.getText());
                }catch(Exception e){
                    BreakPoint = 0;
                }
                BreakField.setText(BreakPoint + "");
                
            }else{
                Stopwatch.stop();
                ToggleButton.setText("Start");
                
            }
            
        }else if(Event.getSource() == NextButton){
            EffectiveTime = Double.parseDouble(TimeField.getText());
            int j = 0;
            for(j = 0; j < UsedData.length && UsedData[j][0] <= EffectiveTime; j++){}
            
            EffectiveTime = UsedData[Math.min(UsedData.length - 1, j)][0];
            TimeField.setText("" + EffectiveTime);
            SourceScrollBar.setValue((j + 1) * SourceScrollBar.getUnitIncrement());
        }else if(Event.getSource() == PreviousButton){
            EffectiveTime = Double.parseDouble(TimeField.getText());
            int j = 0;
            for(j = 0; j < UsedData.length && UsedData[j][0] < EffectiveTime; j++){}
            EffectiveTime = UsedData[Math.max(0, j - 1)][0];
            TimeField.setText("" + EffectiveTime);
            SourceScrollBar.setValue((j - 1) * SourceScrollBar.getUnitIncrement());
        }else if(Event.getSource() == ResetButton){
            TotalTime = 0;
            DataTextArea.setText("");
            PreviousOutput = new String[]{"", "", "", ""};
            TotalTime = 0;
            TimeField.setText("0.0");
            ClockField.setText("0.0");
            EffectiveTime = 0;
            
            PreviousOutput = new String[4];
            
            StreamFeeder.closeWrite();
            StreamFeeder.openWrite();
         
        }
        enable();   
    }
    
    public void itemStateChanged(ItemEvent event){
        
    }
    
    public void mouseClicked(java.awt.event.MouseEvent mouseEvent) {
        System.out.println(mouseEvent.getPoint());
    }    
    
    public void mouseEntered(java.awt.event.MouseEvent mouseEvent) {
    }    
    
    public void mouseExited(java.awt.event.MouseEvent mouseEvent) {
    }
    
    public void mousePressed(java.awt.event.MouseEvent mouseEvent) {
    }
    
    public void mouseReleased(java.awt.event.MouseEvent mouseEvent) {
    }
    
}