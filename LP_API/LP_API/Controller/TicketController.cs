namespace LP_API.Controllers
{
    using LP_API.Model;
    using LP_API.Services;
    using LP_API.Services.LP_API.Services;
    using Microsoft.AspNetCore.Mvc;
    using Microsoft.Extensions.Logging;
    using System;
    using System.Collections.Generic;
    using System.Linq;
    using System.Net.Http;
    using System.Text.Json;
    using System.Threading.Tasks;

    public class TicketController : Controller
    {
        private readonly ApiAuthService _apiAuthService;
        private readonly ILogger<TicketController> _logger;

        public TicketController(ApiAuthService apiAuthService, ILogger<TicketController> logger)
        {
            _apiAuthService = apiAuthService;
            _logger = logger;
        }

        [HttpGet]
        public async Task<IActionResult> Index(string startDate)
        {
            // Obtém o ClientId da sessão
            var sessionData = HttpContext.Session.GetString("SessionModel");
            var sessionModel = JsonSerializer.Deserialize<SessionModel>(sessionData);

            if (sessionModel == null || string.IsNullOrEmpty(sessionModel.Id))
            {
                _logger.LogError("Client ID not found in session.");
                return RedirectToAction("Index");
            }

            var clientId = sessionModel.Id;

            var url = $"https://services.inapa.com/opo/api/ticket/client/{clientId}";
            var response = await _apiAuthService.GetAsync(url);

            if (response.IsSuccessStatusCode)
            {
                var responseContent = await response.Content.ReadAsStringAsync();
                var ticketResponse = JsonSerializer.Deserialize<TicketResponse>(responseContent);

                var tickets = ticketResponse.TicketInfo;

                if (!string.IsNullOrEmpty(startDate) && DateTime.TryParse(startDate, out var parsedDate))
                {
                    tickets = tickets.Where(t => t.StartDate.Date == parsedDate.Date).ToList();
                }

                ViewBag.StartDate = startDate;

                return View(tickets);
            }

            _logger.LogError("Error fetching tickets. Status Code: {StatusCode}", response.StatusCode);
            return View(new List<TicketInfo>());
        }
    }
}