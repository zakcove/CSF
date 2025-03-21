package vttp.batch5.csf.assessment.server.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vttp.batch5.csf.assessment.server.models.MenuItem;
import vttp.batch5.csf.assessment.server.services.RestaurantService;

@RestController
@RequestMapping(path="/api")
public class RestaurantController {

    @Autowired
    private RestaurantService restaurantSvc;

    // TODO: Task 2.2
    @GetMapping(path="/menu", produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<MenuItem>> getMenus() {
        List<MenuItem> menus = restaurantSvc.getMenu();
        return ResponseEntity.ok(menus);
    }

    // TODO: Task 4
    @PostMapping(path="/order", consumes=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> postFoodOrder(@RequestBody String payload) {
        return ResponseEntity.ok("{}");
    }
}
