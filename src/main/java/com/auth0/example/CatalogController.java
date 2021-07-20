package com.auth0.example;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller for the home page.
 */
@Controller
public class CatalogController {

  @GetMapping("/catalog")
  public String catalog(Model model, @AuthenticationPrincipal OidcUser principal) throws UnirestException {
    if (principal != null) {
      model.addAttribute("profile", principal.getClaims());
    }

    HttpResponse<JsonNode> response1 = Unirest.post("https://abacpoc.us.auth0.com/oauth/token")
        .header("content-type", "application/json")
        .body("{\"client_id\":\"O9izEVcSwLfLm7OJHQR4bzN0EYW3ExvY\",\"client_secret\":\"AFK_-kHyTHF3cZ8A5oKkMpvSbYGT-xvM0HjwZ_QtGAxyv1rBBgRrDQPtejlAQ_Ec\",\"audience\":\"https://auth0-demo-cpc\",\"grant_type\":\"client_credentials\"}")
        .asJson();

    String accessToken = response1.getBody().getObject().getString("access_token");

    // call to cpc
    HttpResponse<JsonNode> response2 = Unirest.get("http://localhost:3001/api/data")
        .header("content-type", "application/json")
        .header("authorization", "Bearer " + accessToken)
        .asJson();

    // set data to show
    model.addAttribute("data", response2.toString());
    return "index";
  }
}