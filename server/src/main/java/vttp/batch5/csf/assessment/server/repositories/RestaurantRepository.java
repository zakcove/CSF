package vttp.batch5.csf.assessment.server.repositories;

import java.util.ArrayList;
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
        "SELECT COUNT(*) as user_count FROM customers WHERE username = ? AND password = sha2(?, 224)";

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
        SqlRowSet rs = jdbcTemplate.queryForRowSet(VALIDATE_USER, username, password);
        if (rs.next()) {
            return rs.getInt("user_count") > 0;
        }
        return false;
    }
}
