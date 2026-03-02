namespace LP_API.Controller
{
    using LP_API.Model;
    using LP_API.Services;
    using LP_API.Services.LP_API.Services;
    using Microsoft.AspNetCore.Mvc;
    using Microsoft.Extensions.Logging;
    using System.Net.Http;
    using System.Text.Json;
    using System.Threading.Tasks;

    public class AccountController : Controller
    {
        private readonly ApiAuthService _apiAuthService;
        private readonly ILogger<AccountController> _logger;

        public AccountController(ApiAuthService apiAuthService, ILogger<AccountController> logger)
        {
            _apiAuthService = apiAuthService;
            _logger = logger;
        }

        [HttpGet]
        public IActionResult DeleteAccount()
        {
            return View();
        }

        [HttpDelete]
        public async Task<IActionResult> DeleteAccountConfirmed()
        {
            // Obtém o ClientId da sessão
            var sessionData = HttpContext.Session.GetString("SessionModel");
            var sessionModel = JsonSerializer.Deserialize<SessionModel>(sessionData);

            if (sessionModel == null || string.IsNullOrEmpty(sessionModel.Id))
            {
                _logger.LogError("Client ID not found in session.");
                return BadRequest("Client ID not found in session.");
            }

            var clientId = sessionModel.Id;

            var url = $"https://services.inapa.com/opo/api/client/{clientId}";
            var response = await _apiAuthService.DeleteAsync(url);

            if (response.StatusCode == System.Net.HttpStatusCode.NotAcceptable)
            {
                _logger.LogError("Client cannot be deleted. Status Code: {StatusCode}", response.StatusCode);
                return StatusCode(406, new { Message = "Client cannot be deleted" });
            }

            if (response.IsSuccessStatusCode)
            {
                return Ok();
            }

            var errorMessage = await response.Content.ReadAsStringAsync();
            _logger.LogError("Error deleting client. Status Code: {StatusCode}, Message: {Message}", response.StatusCode, errorMessage);
            return StatusCode((int)response.StatusCode, errorMessage);
        }
    }
}