using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Newtonsoft.Json;

namespace JSON_Processing
{
    public class Video
    {
        [JsonProperty("title")]
        public string Title { get; set; }
        [JsonProperty("link")]
        public string Link { get; set; }
        [JsonProperty("yt:videoId")]
        public string Id{ get; set; }
        public DateTime PublishedOn { get; set; }
        public string Description { get; set; }
        public int Views { get; set; }

    }
}
