package vttp.batch5.csf.assessment.server.repositories;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import vttp.batch5.csf.assessment.server.models.MenuItem;

@Repository
public class RestaurantRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final String GET_MENU_ITEMS = 
        "SELECT * FROM menu_items ORDER BY name ASC";

    private static final String VALIDATE_USER = 
        "select count(*) from customers where username = ? and password = sha2(?, 224)";

    private static final String INSERT_ORDER = 
        "insert into place_orders (order_id, payment_id, order_date, total, username) values (?, ?, ?, ?, ?)";

    // TODO: Task 2.2
    public List<MenuItem> getMenus() {
        List<MenuItem> menuItems = new ArrayList<>();
      
        SqlRowSet rs = jdbcTemplate.queryForRowSet(GET_MENU_ITEMS);

        while(rs.next()) {
            MenuItem item = new MenuItem();
            item.setId(rs.getString("id"));
            item.setName(rs.getString("name"));
            item.setDescription(rs.getString("description"));
            item.setPrice(rs.getDouble("price"));
            menuItems.add(item);
        }
        
        return menuItems;
    }

    public boolean validateUser(String username, String password) {
        Integer count = jdbcTemplate.queryForObject(VALIDATE_USER, Integer.class, username, password);
        return count != null && count > 0;
    }

    public void saveOrder(String orderId, String paymentId, String username, double total) {
        jdbcTemplate.update(INSERT_ORDER, 
            orderId, paymentId, new Date(), total, username);
    }
}
