package vttp.batch5.csf.assessment.server.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vttp.batch5.csf.assessment.server.models.MenuItem;
import vttp.batch5.csf.assessment.server.repositories.OrdersRepository;

@Service
public class RestaurantService {

    @Autowired
    private OrdersRepository ordersRepo;

    // TODO: Task 2.2
    public List<MenuItem> getMenu() {
        return ordersRepo.getMenu();
    }
}
