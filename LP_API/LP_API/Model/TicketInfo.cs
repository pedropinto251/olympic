namespace LP_API.Model
{
    public class TicketInfo
    {
        public string Id { get; set; }
        public DateTime StartDate { get; set; }
        public DateTime EndDate { get; set; }
        public string Location { get; set; }
        public string Seat { get; set; }
        public string TicketQR { get; set; }
    }

    public class TicketResponse
    {
        public string Status { get; set; }
        public List<TicketInfo> TicketInfo { get; set; }
    }
}
