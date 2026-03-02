namespace LP_API.Model
{
    public class Game
    {
        public string Id { get; set; }
        public string GroupId { get; set; }
        public DateTime StartDate { get; set; }
        public DateTime EndDate { get; set; }
        public string Location { get; set; }
        public int Capacity { get; set; }
        public string Sport { get; set; }
        public bool Active { get; set; }
        public int EventId { get; set; }
    }

    public class GameResponse
    {
        public string Status { get; set; }
        public List<Game> Games { get; set; }
    }
}