

namespace LP_API.Controllers
{
    using Microsoft.AspNetCore.Mvc;
    using Microsoft.Extensions.Logging;
    using LP_API.Model;
    using LP_API.Services;
    using System.Threading.Tasks;
    using Mailjet.Client;
    using Mailjet.Client.Resources;
    using Newtonsoft.Json.Linq;
    using LP_API.Services.LP_API.Services;
    using Microsoft.Extensions.Configuration;
    public class ClientController : Controller
    {
        private readonly ApiAuthService _apiAuthService;
        private readonly ILogger<ClientController> _logger;
        private readonly IConfiguration _configuration;

        public ClientController(ApiAuthService apiAuthService, ILogger<ClientController> logger, IConfiguration configuration)
        {
            _apiAuthService = apiAuthService;
            _logger = logger;
            _configuration = configuration;
        }

        [HttpGet]
        public IActionResult Register()
        {
            _logger.LogInformation("GET Register called.");
            var client = new RegisterClient(); // Inicialize o modelo
            return View(client);
        }

        [HttpPost]
        public async Task<IActionResult> Register(RegisterClient registerClient)
        {
            _logger.LogInformation("POST Register called.");

            if (!ModelState.IsValid)
            {
                _logger.LogWarning("Model state is invalid.");
                foreach (var state in ModelState)
                {
                    foreach (var error in state.Value.Errors)
                    {
                        _logger.LogWarning("Field: {Field}, Error: {Error}", state.Key, error.ErrorMessage);
                    }
                }
                return View(registerClient);
            }

            _logger.LogInformation("Registering client with Name: {Name}, Email: {Email}, Password: {Password}", registerClient.Name, registerClient.Email, registerClient.Password);

            // Converta RegisterClient para Client
            var client = new Client
            {
                Name = registerClient.Name,
                Email = registerClient.Email,
                Password = registerClient.Password
            };

            var url = "https://services.inapa.com/opo/api/client";
            var response = await _apiAuthService.PostAsync(url, client);

            if (response.IsSuccessStatusCode)
            {
                _logger.LogInformation("Client registered successfully.");

                // Enviar email com a senha
                await SendEmailAsync(client.Email, client.Name, client.Password);

                return RedirectToAction("Success");
            }

            _logger.LogError("Error registering client. Status Code: {StatusCode}", response.StatusCode);
            ModelState.AddModelError(string.Empty, "Error registering user.");
            return View(registerClient);
        }

        public IActionResult Success()
        {
            _logger.LogInformation("GET Success called.");
            return View();
        }

        private async Task SendEmailAsync(string toEmail, string name, string password)
        {
            var apiKey = _configuration["Mailjet:ApiKey"];
            var apiSecret = _configuration["Mailjet:ApiSecret"];
            var client = new MailjetClient(apiKey, apiSecret);

            var request = new MailjetRequest
            {
                Resource = Send.Resource
            }
            .Property(Send.FromEmail, "lp3sendmail@gmail.com")
            .Property(Send.FromName, "Lp3 Oporto Olympics")
            .Property(Send.Subject, "Detalhes do registo")
            .Property(Send.HtmlPart,
                $"<div style='font-family: Arial, sans-serif; color: #333;'>"
                + $"<h2>Bem-vindo(a) à Oporto Olympics, {name}!</h2>"
                + $"<p>Estamos muito felizes em tê-lo(a) connosco.</p>"
                + $"<p>Aqui estão os detalhes do seu registo:</p>"
                + $"<ul>"
                + $"<li><strong>Nome:</strong> {name}</li>"
                + $"<li><strong>Password:</strong> {password}</li>"
                + $"</ul>"
                + $"<p>Por favor, guarde esta informação em um local seguro.</p>"
                + $"<p>Se precisar de ajuda ou tiver alguma dúvida, não hesite em nos contactar em <a href='mailto:lp3sendmail@gmail.com'>lp3sendmail@gmail.com</a>.</p>"
                + $"<br>"
                + $"<p>Atenciosamente,</p>"
                + $"<p><strong>Equipa Lp3 Oporto Olympics</strong></p>"
                + $"</div>")
            .Property(Send.Recipients, new JArray {
        new JObject {
            { "Email", toEmail }
        }
            });

            var response = await client.PostAsync(request);

            if (!response.IsSuccessStatusCode)
            {
                throw new Exception($"Erro ao enviar e-mail: {response.StatusCode}");
            }
        }
    }
}