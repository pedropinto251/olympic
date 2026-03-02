namespace LP_API.Services
{
    using System.Net.Http;
    using System.Net.Http.Headers;
    using System.Text;
    using System.Threading.Tasks;
    using Newtonsoft.Json;
    using global::LP_API.Model;

    namespace LP_API.Services
    {
        public class ApiAuthService
        {
            private readonly HttpClient _httpClient;

            public ApiAuthService(HttpClient httpClient)
            {
                _httpClient = httpClient;
                var username = "FG5";
                var password = "W0gyYJ!)Y6";
                var byteArray = Encoding.ASCII.GetBytes($"{username}:{password}");
                _httpClient.DefaultRequestHeaders.Authorization = new AuthenticationHeaderValue("Basic", Convert.ToBase64String(byteArray));
            }

            public async Task<HttpResponseMessage> PostAsync(string url, Client client)
            {
                var json = JsonConvert.SerializeObject(client);
                var content = new StringContent(json, Encoding.UTF8, "application/json");
                content.Headers.ContentType = new MediaTypeHeaderValue("application/json");

                return await _httpClient.PostAsync(url, content);
            }

            // Adicionando o método PutAsync com parâmetros opcionais
            public async Task<HttpResponseMessage> PutAsync(string url = null, object data = null)
            {
                if (url == null)
                {
                    throw new ArgumentNullException(nameof(url), "URL cannot be null");
                }

                var json = JsonConvert.SerializeObject(data ?? new { });
                var content = new StringContent(json, Encoding.UTF8, "application/json");
                content.Headers.ContentType = new MediaTypeHeaderValue("application/json");

                return await _httpClient.PutAsync(url, content);
            }
            // Adicionando o método GetAsync
            public async Task<HttpResponseMessage> GetAsync(string url)
            {
                return await _httpClient.GetAsync(url);
            }

            public async Task<HttpResponseMessage> DeleteAsync(string url)
            {
                return await _httpClient.DeleteAsync(url);
            }
        }
    }
}