package vttp.batch5.csf.assessment.server.repositories;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import vttp.batch5.csf.assessment.server.models.MenuItem;
import vttp.batch5.csf.assessment.server.models.OrderItem;

@Repository
public class OrdersRepository {

    @Autowired
    private MongoTemplate template;

    private static final String MENU_COLLECTION = "menus";
    private static final String ORDERS_COLLECTION = "orders";

    // TODO: Task 2.2
    // You may change the method's signature
    // Write the native MongoDB query in the comment below
    //
    // db.menus.find().sort({ name: 1 })
    //
    public List<MenuItem> getMenu() {
        Query query = Query.query(Criteria.where("_id").exists(true))
            .with(Sort.by(Sort.Direction.ASC, "name"));
        return template.find(query, MenuItem.class, MENU_COLLECTION);
    }

    // TODO: Task 4
    // Write the native MongoDB query for your access methods in the comment below
    //
    // db.orders.insertOne({
    //   _id: "abcd1234",
    //   order_id: "abcd1234",
    //   payment_id: "xyz789",
    //   username: "fred",
    //   total: 23.10,
    //   timestamp: new Date(),
    //   items: [
    //     { id: "xxx", price: 7.70, quantity: 2 },
    //     { id: "yyy", price: 7.70, quantity: 1 }
    //   ]
    // })
    public void saveOrder(String orderId, String paymentId, String username, 
            double total, List<OrderItem> items) {
        Document orderDoc = new Document()
            .append("_id", orderId)
            .append("order_id", orderId)
            .append("payment_id", paymentId)
            .append("username", username)
            .append("total", total)
            .append("timestamp", new Date())
            .append("items", items.stream()
                .map(item -> new Document()
                    .append("id", item.getId())
                    .append("price", item.getPrice())
                    .append("quantity", item.getQuantity()))
                .collect(Collectors.toList()));

        template.insert(orderDoc, ORDERS_COLLECTION);
    }
}
