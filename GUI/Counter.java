class Counter{
        double Max;
        double Min;
    
        double Current;
        double BaseIncrement;
        public String toString(){
            return Min + "-" + Max + ":" + Current;
        }
        public Counter(double mi, double ma, double Curr){
            Max = ma;
            Min = mi;
            Current = Curr;
        }
        public void setBaseIncrement(double BI){
            BaseIncrement = BI;
        }
        public void setTo(double V){
            Current = V;
            
        }
        public void increment(double I){
            Current = Current + I;
            
        }
        public void cap(){
            switch(checkBounds()){
                case 2: Current = Max; break;
                case -2: Current = Min; break;
            }
            
        }
        public int checkBounds(){
            if(Current > Max){
                return 2;
            }else if(Current < Min){
                return -2;
            }else if(Current == Max){
                return 1;
            }else if(Current == Min){
                return -1;
            }else{
                return 0;
            }
        }
    }
