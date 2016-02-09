using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Newtonsoft.Json;

namespace JSON_Processing
{
    public class Link
    {
        [JsonProperty("@href")]
        public string Href { get; set; }
    }
}
