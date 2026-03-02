

namespace LP_API.Controllers
{
    using Microsoft.AspNetCore.Mvc;
    using Microsoft.Extensions.Logging;
    using LP_API.Model;
    using LP_API.Services;
    using System.Threading.Tasks;
    using LP_API.Services.LP_API.Services;
    using System.Text.Json;
    public class ChangePasswordController : Controller
    {
        private readonly ApiAuthService _apiAuthService;
        private readonly ILogger<ChangePasswordController> _logger;

        public ChangePasswordController(ApiAuthService apiAuthService, ILogger<ChangePasswordController> logger)
        {
            _apiAuthService = apiAuthService;
            _logger = logger;
        }

        [HttpGet]
        public IActionResult ChangePassword()
        {
            var sessionData = HttpContext.Session.GetString("SessionModel");
            var sessionModel = JsonSerializer.Deserialize<SessionModel>(sessionData);

            if (sessionModel == null)
            {
                _logger.LogError("Session data is missing.");
                return RedirectToAction("Error"); // Redirecionar para uma página de erro apropriada
            }

            var model = new ChangePasswordModel { Id = sessionModel.Id };
            return View(model);
        }

        [HttpPost]
        public async Task<IActionResult> ChangePassword(ChangePasswordModel model)
        {
            if (!ModelState.IsValid)
            {
                _logger.LogWarning("Model state is invalid.");
                return View(model);
            }

            var url = $"https://services.inapa.com/opo/api/client/{model.Id}";
            var changePasswordModel = new { Password = model.NewPassword };
            var response = await _apiAuthService.PutAsync(url, changePasswordModel);

            if (response.IsSuccessStatusCode)
            {
                _logger.LogInformation("Password changed successfully.");
                return RedirectToAction("Success");
            }

            _logger.LogError("Error changing password. Status Code: {StatusCode}", response.StatusCode);
            ModelState.AddModelError(string.Empty, "Error changing password.");
            return View(model);
        }

        public IActionResult Success()
        {
            var loginModel = new LoginModel();
            return View("~/Views/Client/Index.cshtml", loginModel);
        }
    }
}