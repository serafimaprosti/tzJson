import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Tickets {
    private List<Ticket> tickets;
    @JsonProperty("tickets")
    public List<Ticket> getTickets() {
        return tickets;
    }
    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }
}
