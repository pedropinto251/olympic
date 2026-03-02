using LP_API.Services;
using LP_API.Services.LP_API.Services;

var builder = WebApplication.CreateBuilder(args);


builder.Services.AddControllersWithViews();
builder.Services.AddRazorPages(); 
builder.Services.AddHttpClient<ApiAuthService>(); 


builder.Services.AddDistributedMemoryCache();
builder.Services.AddSession(options =>
{
    options.IdleTimeout = TimeSpan.FromMinutes(30);
    options.Cookie.HttpOnly = true;
    options.Cookie.IsEssential = true;
});

var app = builder.Build();

// Configure the HTTP request pipeline.
if (!app.Environment.IsDevelopment())
{
    app.UseExceptionHandler("/Error");
    app.UseHsts();
}

app.UseHttpsRedirection();
app.UseStaticFiles();

app.UseRouting();

app.UseAuthorization();
app.UseSession(); 

// Redirecionar a URL raiz para a página de login
app.Use(async (context, next) =>
{
    if (context.Request.Path == "/")
    {
        context.Response.Redirect("/Login");
        return;
    }
    await next();
});

// Map controllers
app.MapControllerRoute(
    name: "default",
    pattern: "{controller=Login}/{action=Index}/{id?}");

app.MapControllerRoute(
    name: "register",
    pattern: "{controller=Client}/{action=Register}/{id?}");

app.MapControllerRoute(
    name: "changePassword",
    pattern: "{controller=ChangePassword}/{action=ChangePassword}/{id?}");

app.MapControllerRoute(
    name: "account",
    pattern: "{controller=Account}/{action=DeleteAccount}/{id?}");

app.MapRazorPages();

app.Run();