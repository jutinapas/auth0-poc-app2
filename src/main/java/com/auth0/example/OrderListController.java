package com.auth0.example;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller for the home page.
 */
@Controller
public class OrderListController {

  @GetMapping("/order-list")
  public String OrderList(Model model, @AuthenticationPrincipal OidcUser principal) {
    if (principal != null) {
      model.addAttribute("profile", principal.getClaims());
    }

    // check roles
    String roles = principal.getClaims().get("https://roles").toString();
    String data = "Order List Page";
    if (roles.contains("Fuel")) {
      data = "Fuel Order List Page";
    } else if (roles.contains("Lubes")) {
      data = "Lube Order List Page";
    }

    model.addAttribute("data", data);

    return "index";
  }
}