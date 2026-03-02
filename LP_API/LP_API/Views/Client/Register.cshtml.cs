using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.RazorPages;
using LP_API.Model;
using LP_API.Services;
using System.Threading.Tasks;
using LP_API.Services.LP_API.Services;

namespace LP_API.Pages.Client
{
    public class RegisterModel : PageModel
    {
        private readonly ApiAuthService _apiAuthService;
        private readonly ILogger<RegisterModel> _logger;

        [BindProperty]
        public LP_API.Model.Client Client { get; set; }

        public RegisterModel(ApiAuthService apiAuthService, ILogger<RegisterModel> logger)
        {
            _apiAuthService = apiAuthService;
            _logger = logger;
        }

        public void OnGet()
        {
        }

        public async Task<IActionResult> OnPostAsync()
        {
            if (!ModelState.IsValid)
            {
                return Page();
            }

            _logger.LogInformation("Registering client with Name: {Name}, Email: {Email}, Password: {Password}", Client.Name, Client.Email, Client.Password);

            var url = "https://services.inapa.com/opo/api/client";
            var response = await _apiAuthService.PostAsync(url, Client);

            if (response.IsSuccessStatusCode)
            {
                _logger.LogInformation("Client registered successfully.");
                return RedirectToPage("Success");
            }

            _logger.LogError("Error registering client.");
            ModelState.AddModelError(string.Empty, "Error registering user.");
            return Page();
        }
    }
}