package vttp.batch5.csf.assessment.server.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import vttp.batch5.csf.assessment.server.models.MenuItem;
import vttp.batch5.csf.assessment.server.models.OrderRequest;
import vttp.batch5.csf.assessment.server.models.PaymentResponse;
import vttp.batch5.csf.assessment.server.services.RestaurantService;

@RestController
@RequestMapping(path="/api")
public class RestaurantController {

    @Autowired
    private RestaurantService restaurantSvc;

    @Autowired
    private ObjectMapper objectMapper;

    // TODO: Task 2.2
    @GetMapping(path="/menu", produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<MenuItem>> getMenus() {
        List<MenuItem> menus = restaurantSvc.getMenu();
        return ResponseEntity.ok(menus);
    }

    // TODO: Task 4
    @PostMapping(path="/order", consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> postFoodOrder(@RequestBody String payload) {
        try {
            OrderRequest orderRequest = objectMapper.readValue(payload, OrderRequest.class);

            if (!restaurantSvc.authenticateUser(orderRequest.getUsername(), orderRequest.getPassword())) {
                return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("{ \"message\": \"Invalid username and/or password\" }");
            }

            String orderId = restaurantSvc.generateOrderId();

            double total = orderRequest.getItems().stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();

            PaymentResponse paymentResponse = restaurantSvc.processPayment(
                orderId, orderRequest.getUsername(), total);

            return ResponseEntity.ok(objectMapper.writeValueAsString(paymentResponse));

        } catch (JsonProcessingException e) {
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("{ \"message\": \"Invalid request format\" }");
        } catch (Exception e) {
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("{ \"message\": \"An error occurred\" }");
        }
    }
}
