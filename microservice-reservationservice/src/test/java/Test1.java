import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Date;

public class Test1 {
    public static void main(String[] args){
        LocalDate date = LocalDate.now();
        Date tomorrow = new Date(new Date().getTime() + (1000*60*60*24));

        long currentTime = System.currentTimeMillis();
        Date date1 = new Date(currentTime);

        ZonedDateTime zonedDateTime = ZonedDateTime.now();

//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat formatter3 = new SimpleDateFormat("yyyy-MM");
//        System.out.println(date.format(formatter));
        System.out.println(formatter2.format(date1));
        System.out.println(formatter1.format(tomorrow));

        Date now = new Date();
        System.out.println(formatter3.format(now));

        if(zonedDateTime.getMonthValue()<10){
            System.out.println("0" + zonedDateTime.getMonthValue());
        }
        else {
            System.out.println(zonedDateTime.getMonthValue());
        }

        System.out.println(zonedDateTime.getDayOfMonth() + 1);

    }

}
