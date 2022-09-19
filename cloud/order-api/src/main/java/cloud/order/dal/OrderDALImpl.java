package cloud.order.dal;

import cloud.order.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OrderDALImpl implements OrderDAL{
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<Order> findAll() { return mongoTemplate.findAll(Order.class); }

    @Override
    public Order findById(Integer orderId){
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(orderId));
        return mongoTemplate.findOne(query, Order.class);
    }

    @Override
    public Order create(Order order){
        mongoTemplate.save(order);
        return order;
    }

    @Override
    public  List<Order> findByUsername(String username){
        Query query = new Query();
        query.addCriteria(Criteria.where("username").is(username));
        return mongoTemplate.find(query, Order.class);
    }
}
