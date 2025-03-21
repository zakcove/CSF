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

    private static final String PAYMENT_URL = "https://payment-service-production-a75a.up.railway.app/api/payment";
    
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
        headers.set("X-Authenticate", username);

        HttpEntity<PaymentRequest> request = new HttpEntity<>(paymentRequest, headers);
        
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.postForObject(PAYMENT_URL, request, PaymentResponse.class);
    }
}
