namespace LP_API.Controllers
{
    using LP_API.Model;
    using LP_API.Services;
    using LP_API.Services.LP_API.Services;
    using Microsoft.AspNetCore.Mvc;
    using Microsoft.Extensions.Logging;
    using System;
    using System.Linq;
    using System.Net.Http;
    using System.Net.Http.Headers;
    using System.Text;
    using System.Text.Json;
    using System.Threading.Tasks;

    public class GameController : Controller
    {
        private readonly ApiAuthService _apiAuthService;
        private readonly ILogger<GameController> _logger;
        private readonly HttpClient _httpClient;

        public GameController(ApiAuthService apiAuthService, ILogger<GameController> logger, IHttpClientFactory httpClientFactory)
        {
            _apiAuthService = apiAuthService;
            _logger = logger;
            _httpClient = httpClientFactory.CreateClient();
            var username = "FG5";
            var password = "W0gyYJ!)Y6";
            var byteArray = Encoding.ASCII.GetBytes($"{username}:{password}");
            _httpClient.DefaultRequestHeaders.Authorization = new AuthenticationHeaderValue("Basic", Convert.ToBase64String(byteArray));
        }

        [HttpGet]
        public async Task<IActionResult> Index(string sport, string startDate)
        {
            var url = "https://services.inapa.com/opo/api/game/";
            var response = await _apiAuthService.GetAsync(url);

            if (response.IsSuccessStatusCode)
            {
                var responseContent = await response.Content.ReadAsStringAsync();
                var gameResponse = JsonSerializer.Deserialize<GameResponse>(responseContent);

                var games = gameResponse.Games;

                if (!string.IsNullOrEmpty(sport))
                {
                    games = games.Where(g => g.Sport.ToLower().Contains(sport.ToLower())).ToList();
                }

                if (!string.IsNullOrEmpty(startDate) && DateTime.TryParse(startDate, out var parsedDate))
                {
                    games = games.Where(g => g.StartDate.Date == parsedDate.Date).ToList();
                }

                ViewBag.Sport = sport;
                ViewBag.StartDate = startDate;

                return View(games);
            }

            _logger.LogError("Error fetching games. Status Code: {StatusCode}", response.StatusCode);
            return View(new List<Game>());
        }

        [HttpGet]
        public async Task<IActionResult> Details(string gameId)
        {
            var url = "https://services.inapa.com/opo/api/game/";
            var response = await _apiAuthService.GetAsync(url);

            if (response.IsSuccessStatusCode)
            {
                var responseContent = await response.Content.ReadAsStringAsync();
                var gameResponse = JsonSerializer.Deserialize<GameResponse>(responseContent);

                var game = gameResponse.Games.FirstOrDefault(g => g.Id == gameId);

                if (game != null)
                {
                    return View(game);
                }
            }

            _logger.LogError("Error fetching game details. Status Code: {StatusCode}", response.StatusCode);
            return RedirectToAction("Index");
        }

        [HttpPost]
        public async Task<IActionResult> CreateTicket(string gameId)
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

            // Obtém a capacidade do jogo
            var url = "https://services.inapa.com/opo/api/game/";
            var response = await _apiAuthService.GetAsync(url);

            if (!response.IsSuccessStatusCode)
            {
                _logger.LogError("Error fetching game details. Status Code: {StatusCode}", response.StatusCode);
                return RedirectToAction("Index");
            }

            var responseContent = await response.Content.ReadAsStringAsync();
            var gameResponse = JsonSerializer.Deserialize<GameResponse>(responseContent);
            var game = gameResponse.Games.FirstOrDefault(g => g.Id == gameId);

            if (game == null)
            {
                _logger.LogError("Game not found.");
                return RedirectToAction("Index");
            }

            // Gera um número de assento aleatório com base na capacidade do jogo
            var random = new Random();
            var seatNumber = random.Next(1, game.Capacity + 1); // Gera um número entre 1 e a capacidade do jogo
            var seat = $"A{seatNumber}";

            var ticket = new
            {
                ClientId = clientId,
                GameId = gameId,
                Seat = seat
            };

            var ticketUrl = "https://services.inapa.com/opo/api/Ticket";
            var json = JsonSerializer.Serialize(ticket);
            var content = new StringContent(json, Encoding.UTF8, "application/json");

            // Envia a requisição POST diretamente usando HttpClient configurado com autenticação
            var ticketResponse = await _httpClient.PostAsync(ticketUrl, content);

            if (ticketResponse.IsSuccessStatusCode)
            {
                _logger.LogInformation("Ticket created successfully.");
                return RedirectToAction("Index");
            }

            _logger.LogError("Error creating ticket. Status Code: {StatusCode}", ticketResponse.StatusCode);
            return RedirectToAction("Index");
        }
    }
}