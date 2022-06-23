package com.example.backendschoolyandex;

import com.example.backendschoolyandex.Entities.Product;
import com.example.backendschoolyandex.Json.ErrorResponseJson;
import com.example.backendschoolyandex.Json.ItemsJson;
import com.example.backendschoolyandex.Repository.ItemsRepo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
public class Controller {

    private final ItemsRepo itemsRepo;
//    private final UserRepository userRepository;
//    private final TokenRepository tokenRepository;

//    private final ApplicationContext context = new AnnotationConfigApplicationContext(Config.class);
    @Autowired
    public Controller(ItemsRepo itemsRepo) {
        this.itemsRepo = itemsRepo;
//        this.userRepository = userRepository;
//        this.tokenRepository = tokenRepository;
    }

    @PostMapping(path = "/imports")
    public ResponseEntity getToken(@RequestBody String json) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ItemsJson itemsJson = mapper.readValue(json, ItemsJson.class);
        List<ItemsJson.Items> itemsList = itemsJson.getItems();
        for (ItemsJson.Items x : itemsList){
            if (x.getType().equals("OFFER") && x.getPrice() >= 0){
                Product product = new Product(x.getId(), x.getName(), x.getParentId(), x.getPrice(), x.getType(), itemsJson.getUpdateDate());
                itemsRepo.save(product);
            }else if (x.getType().equals("CATEGORY") && x.getPrice() == 0){
                Product product = new Product(x.getId(), x.getName(), x.getParentId(), null, x.getType(), itemsJson.getUpdateDate());
                itemsRepo.save(product);
            }else{
                ErrorResponseJson errorResponseJson = new ErrorResponseJson(400, "Validation Failed");
                String body = mapper.writeValueAsString(errorResponseJson);
                return ResponseEntity.status(400).contentType(MediaType.APPLICATION_JSON).body(body);
            }
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @DeleteMapping(path = "/delete/{id}")
    @Transactional
    public ResponseEntity delete(@PathVariable("id") String id) throws JsonProcessingException {
        if (itemsRepo.findByid(id) == null){
            ObjectMapper mapper = new ObjectMapper();
            String response = mapper.writeValueAsString(new ErrorResponseJson(404, "Item not found"));
            return ResponseEntity.status(404).contentType(MediaType.APPLICATION_JSON).body(response);
        }
        itemsRepo.deleteByid(id);
        List<Product> productList = itemsRepo.findByparentId(id);
        for (Product product : productList){
            if (itemsRepo.findByparentId(product.getId()) != null){
                delete(product.getId());
            }
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping(path = "/nodes/{id}")
    public ResponseEntity nodes(@PathVariable("id") String id) throws JsonProcessingException {
        if (itemsRepo.findByid(id) == null){
            ObjectMapper mapper = new ObjectMapper();
            String response = mapper.writeValueAsString(new ErrorResponseJson(404, "Item not found"));
            return ResponseEntity.status(404).contentType(MediaType.APPLICATION_JSON).body(response);
        }



    }
}