package org.springframework.samples.petclinic.web;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Scanner;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.samples.petclinic.model.Health;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HealthController {

	@GetMapping(value = { "/health" })
	public String showHealth(Map<String, Object> model) {

		Health health = new Health();

		try {
            URL url = new URL("http://psg2-2021-g6-64.herokuapp.com/manage/health");

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            int responsecode = conn.getResponseCode();

            if (responsecode != 200) {
                throw new RuntimeException("HttpResponseCode: " + responsecode);
            } else {

                String inline = "";
                Scanner scanner = new Scanner(url.openStream());

                while (scanner.hasNext()) {
                    inline += scanner.nextLine();
                }

                scanner.close();

                JSONParser parse = new JSONParser();
                JSONObject data_obj = (JSONObject) parse.parse(inline);
                health.setStatus(data_obj.get("status").toString());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
	
		model.put("health", health);

		return "health/showHealth";
	}

}
