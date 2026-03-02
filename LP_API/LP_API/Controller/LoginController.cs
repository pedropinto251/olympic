namespace LP_API.Controllers
{
    using Microsoft.AspNetCore.Mvc;
    using Microsoft.Extensions.Logging;
    using LP_API.Model;
    using LP_API.Services;
    using System.Threading.Tasks;
    using LP_API.Services.LP_API.Services;
    using System.Text.Json;

    public class LoginController : Controller
    {
        private readonly ApiAuthService _apiAuthService;
        private readonly ILogger<LoginController> _logger;

        public LoginController(ApiAuthService apiAuthService, ILogger<LoginController> logger)
        {
            _apiAuthService = apiAuthService;
            _logger = logger;
        }

        [HttpGet]
        public IActionResult Index()
        {
            var loginModel = new LoginModel();
            return View("~/Views/Client/Index.cshtml", loginModel);
        }

        [HttpPost]
        public async Task<IActionResult> Index(LoginModel loginModel)
        {
            if (!ModelState.IsValid)
            {
                _logger.LogWarning("Model state is invalid.");
                return View(loginModel);
            }

            _logger.LogInformation("Logging in client with Email: {Email}", loginModel.Email);

            var client = new Client
            {
                Email = loginModel.Email,
                Password = loginModel.Password
            };

            var url = "https://services.inapa.com/opo/api/client/login";
            var response = await _apiAuthService.PostAsync(url, client);

            if (response.IsSuccessStatusCode)
            {
                var responseContent = await response.Content.ReadAsStringAsync();
                var apiResponse = JsonSerializer.Deserialize<ApiResponse>(responseContent);

                if (apiResponse != null && apiResponse.Client.Count > 0)
                {
                    var clientData = apiResponse.Client[0];
                    var sessionModel = new SessionModel
                    {
                        Id = clientData.Id,
                        GroupId = clientData.GroupId,
                        Name = clientData.Name,
                        Email = clientData.Email,
                        Active = clientData.Active
                    };

                    HttpContext.Session.SetString("SessionModel", JsonSerializer.Serialize(sessionModel));

                    _logger.LogInformation("Client logged in successfully. ClientId: {ClientId}", sessionModel.Id);
                    return RedirectToAction("Success");
                }
            }

            _logger.LogError("Error logging in client. Status Code: {StatusCode}", response.StatusCode);
            Response.StatusCode = 406;
            return Json(new { message = "Email ou palavra-passe incorretos." });
        }

        public IActionResult Success()
        {
            var sessionData = HttpContext.Session.GetString("SessionModel");
            var sessionModel = JsonSerializer.Deserialize<SessionModel>(sessionData);

            return View("~/Views/Client/SuccessLogin.cshtml", sessionModel);
        }
    }
}