
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.time.*;
import java.time.format.DateTimeFormatter;

import java.util.*;
import java.util.stream.Collectors;

public class tz{

    private static final String TLV = "TLV";
    private static final String VVO = "VVO";

    public static void main(String[] args) throws ParseException {
        ObjectMapper objectMapper = new ObjectMapper();
        Tickets tickets = new Tickets();

        try {
            tickets = objectMapper.readValue(new File("src/main/resources/tickets.json"), Tickets.class);
        } catch (IOException e) {
            e.printStackTrace();
        }


        Map<String, Duration> result = tz.findMinTimeForEach(tickets);
        System.out.println(result);
        System.out.println("Average Price - Median Price = " + tz.differenceBetweenAveragePriceAndMedian(tickets));

    }

    public static Map<String, Duration> findMinTimeForEach(Tickets tickets) throws ParseException {

        Map<String, Duration> result = new HashMap<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");

        for (Ticket ticket : tickets.getTickets()){

            if ((!(ticket.getDestination().equals(tz.TLV))
                    && !(ticket.getDestination().equals(tz.VVO)))
                    || (!(ticket.getOrigin().equals(tz.TLV))
                    && !(ticket.getOrigin().equals(tz.VVO)))){
                continue;
            }

            if (ticket.getDepartureTime().length() == 4)
                ticket.setDepartureTime("0" + ticket.getDepartureTime());
            if (ticket.getArrivalTime().length() == 4)
                ticket.setArrivalTime("0" + ticket.getArrivalTime());

            LocalDateTime start = LocalDateTime
                    .parse(ticket.getDepartureDate() + " " + ticket.getDepartureTime(), formatter);
            LocalDateTime end = LocalDateTime
                    .parse(ticket.getArrivalDate() + " " + ticket.getArrivalTime(), formatter);

            Duration duration = Duration.between(start, end);

            if (result.get(ticket.getCarrier()) != null){
                if(result.get(ticket.getCarrier()).compareTo(duration) > 0) {
                    result.put(ticket.getCarrier(), duration);
                }
            }else {
                result.put(ticket.getCarrier(), duration);
            }

        }

        return result;
    }

    public static double differenceBetweenAveragePriceAndMedian(Tickets tickets){

        List<Integer> priceList = tickets.getTickets()
                .stream().map(Ticket::getPrice)
                .sorted(Integer::compareTo)
                .collect(Collectors.toList());

        int counter = priceList.size() / 2;

        double average = (double) priceList.stream()
                .reduce(0, Integer::sum) / priceList.size();

        double median = (double) (priceList.get(counter) + priceList.get(counter + 1)) / 2;


        return average - median;
    }
}


