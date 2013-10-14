class NumberQueue{
    double[] Numbers;
    int Counter = 0;
    public NumberQueue(int Length){
        Numbers = new double[Length];
    }
    public void initTo(double A){
        for(int i = 0; i < Numbers.length; i++){
            Numbers[i] = A;
        }
    }
    public void add(double A){
        
        Numbers[Counter] = A;
        Counter = (Counter + 1) % Numbers.length;
    }
    public double average(){
        double A = 0;
        for(int i = 0; i < Numbers.length; i++){
            A = A + Numbers[i];
        }
        A = A / Numbers.length;
        return A;
    }
    
}
