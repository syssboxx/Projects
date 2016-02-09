using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.IO;
using System.Net;
using System.Xml;
using System.Xml.Linq;
using Newtonsoft.Json;
using Newtonsoft.Json.Linq;

namespace JSON_Processing
{
    class Program
    {
        static void Main(string[] args)
        {
            // Download youtube videos
            string file = @"../../videos.xml";
            string HtmlName = @"../../index.html";
            string rrsVideosFile = "https://www.youtube.com/feeds/videos.xml?channel_id=UCLC-vbm7OWvpbqzXaoAMGGw";
            
            WebClient webClient = new WebClient();
            webClient.DownloadFile(rrsVideosFile, file);
            XmlDocument document = new XmlDocument();
            document.Load(file);

            //Parse teh XML from the feed to JSON
            String jsonFromXml = JsonConvert.SerializeXmlNode(document,Newtonsoft.Json.Formatting.Indented);
            Console.WriteLine(jsonFromXml);

            //Using LINQ-to-JSON select all the video titles and print the on the console
            var jsonObject = JObject.Parse(jsonFromXml);
            
            var videosJson = jsonObject["feed"]["entry"];
            var titles = videosJson.Select(v => v["title"]);

            foreach (var t in titles)
            {
                Console.WriteLine(t);
            }

            //Parse the videos' JSON to POCO
            var poco = ConvertJsonToPoco(jsonFromXml);

            //Using the POCOs create a HTML page that shows all videos from the RSS
            //Use  <iframe> 
            //Provide a links, that nagivate to their videos in YouTube 

            //var html = GetHtmlString(poco); 
            //SaveHtml(html, HtmlName);
            GenerateHtml(poco);

        }
        private static IEnumerable<Video> ConvertJsonToPoco(string json)
        {
            var jObject = JObject.Parse(json);
            var videos = jObject["feed"]["entry"];

            return videos.Select(video => JsonConvert.DeserializeObject<Video>(video.ToString()));
                   
        }

        private static void GenerateHtml(IEnumerable<Video> videos)
        {
            var html = "<!DOCTYPE html><html><body>";

            foreach (var video in videos)
            {
                html += string.Format("<div style=\"float:left; width: 400px; height: 430px; padding:10px;" +
                                  "margin:5px; background-color:yellowgreen; border-radius:10px\">" +
                                  "<iframe width=\"400\" height=\"325\" " +
                                  "src=\"http://www.youtube.com/embed/{2}?autoplay=0\" " +
                                  "frameborder=\"0\" allowfullscreen></iframe>" +
                                  "<h3>{1}</h3><a href=\"{0}\"><button>" +
                                  "<strong>Go to YouTube</strong></button></a></div>",
                                  video.Link.Href, video.Title, video.Id);
            }

            html += ("</body></html>");

            File.WriteAllText("../../index.html", html);

            
        }


        

        


    }
}
