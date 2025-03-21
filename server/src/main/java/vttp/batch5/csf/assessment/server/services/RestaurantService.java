package vttp.batch5.csf.assessment.server.services;

import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import vttp.batch5.csf.assessment.server.models.MenuItem;
import vttp.batch5.csf.assessment.server.models.OrderItem;
import vttp.batch5.csf.assessment.server.models.PaymentRequest;
import vttp.batch5.csf.assessment.server.models.PaymentResponse;
import vttp.batch5.csf.assessment.server.repositories.OrdersRepository;
import vttp.batch5.csf.assessment.server.repositories.RestaurantRepository;

@Service
public class RestaurantService {

    @Autowired
    private RestaurantRepository restaurantRepo;

    @Autowired
    private OrdersRepository ordersRepo;

    @Value("${restaurant.payment.url}")
    private String paymentUrl;
    
    @Value("${restaurant.payment.payee}")
    private String payeeName;

    public List<MenuItem> getMenu() {
        return ordersRepo.getMenu();
    }

    public boolean authenticateUser(String username, String password) {
        return restaurantRepo.validateUser(username, password);
    }

    public String generateOrderId() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 8; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }

    public PaymentResponse processPayment(String orderId, String username, double total) {
        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setOrder_id(orderId);
        paymentRequest.setPayer(username);
        paymentRequest.setPayee(payeeName);
        paymentRequest.setPayment(total);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.set("X-Authenticate", username);

        HttpEntity<PaymentRequest> request = new HttpEntity<>(paymentRequest, headers);
        
        RestTemplate restTemplate = new RestTemplate();
        PaymentResponse response = restTemplate.postForObject(paymentUrl, request, PaymentResponse.class);

        if (response != null && response.getStatus().equals("paid")) {
            restaurantRepo.saveOrder(orderId, response.getPayment_id(), username, total);
            return response;
        }

        return response;
    }

    public void saveOrderToMongo(String orderId, String paymentId, String username, 
            double total, List<OrderItem> items) {
        ordersRepo.saveOrder(orderId, paymentId, username, total, items);
    }
}
