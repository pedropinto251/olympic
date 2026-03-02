using System.Collections.Generic;

namespace LP_API.Model
{
    public class ApiResponse
    {
        public string Status { get; set; }
        public List<Client> Client { get; set; }
    }
}