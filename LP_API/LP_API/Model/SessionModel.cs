using System.ComponentModel.DataAnnotations;

namespace LP_API.Model
{
    public class SessionModel
    {
        public string Id { get; set; }
        public string GroupId { get; set; }
        public string Name { get; set; }
        public string Email { get; set; }
        public bool Active { get; set; }
    }

    public class ChangePasswordModel
    {
        [Required]
        public string Id { get; set; }

        [Required]
        [DataType(DataType.Password)]
        public string NewPassword { get; set; }
    }
}