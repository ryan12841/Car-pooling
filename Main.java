import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args){
        List<RIDE>ridelist=new ArrayList<>();
        RIDE ride1=new RIDE(20,"Greater Noida","delhi",250, 20,);
        RIDE ride2=new RIDE(25,"Ajmer","mumbai",420);
        ridelist.add(ride1);
        ridelist.add(ride2);
        System.out.println(ridelist);
    }
}