import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Test1 {
    public static void main(String[] args){
        LocalDate date = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        System.out.println(date.format(formatter));
    }
}
