package vttp.batch5.csf.assessment.server.repositories;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class RestaurantRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final String VALIDATE_USER = "select count(*) from customers where username = ? and password = sha2(?, 224)";

    private static final String INSERT_ORDER = "insert into place_orders (order_id, payment_id, order_date, total, username) values (?, ?, ?, ?, ?)";

    // TODO: Task 2.2
    public boolean validateUser(String username, String password) {
        Integer count = jdbcTemplate.queryForObject(VALIDATE_USER, Integer.class, username, password);
        return count != null && count > 0;
    }

    public void saveOrder(String orderId, String paymentId, String username, double total) {
        jdbcTemplate.update(INSERT_ORDER,
                orderId, paymentId, new Date(), total, username);
    }
}
