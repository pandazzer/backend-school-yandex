package com.example.backendschoolyandex;

import com.example.backendschoolyandex.Entities.Product;
import com.example.backendschoolyandex.Json.AVGPriceJson;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping
public class Controller {

    private final ItemsRepo itemsRepo;
    ObjectMapper mapper = new ObjectMapper();

    @Autowired
    public Controller(ItemsRepo itemsRepo) {
        this.itemsRepo = itemsRepo;
    }

    @PostMapping(path = "/imports")
    public ResponseEntity imports(@RequestBody String json) throws JsonProcessingException, ParseException {

        ItemsJson itemsJson = mapper.readValue(json, ItemsJson.class);
        List<ItemsJson.Items> itemsList = itemsJson.getItems();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        Date date = sdf.parse(itemsJson.getUpdateDate());
        for (ItemsJson.Items items : itemsList){
            if (items.getType().equals("OFFER") && items.getPrice() >= 0){
                Product product = new Product(items.getId(), items.getName(), items.getParentId(), items.getPrice(), items.getType(), date);
                dateUpdate(items.getParentId(), date);
                itemsRepo.save(product);
            }else if (items.getType().equals("CATEGORY") && items.getPrice() == 0){
                Product product = new Product(items.getId(), items.getName(), items.getParentId(), null, items.getType(), date);
                itemsRepo.save(product);
            }else{
                ErrorResponseJson errorResponseJson = new ErrorResponseJson(400, "Validation Failed");
                String body = mapper.writeValueAsString(errorResponseJson);
                return ResponseEntity.status(400).contentType(MediaType.APPLICATION_JSON).body(body);
            }
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    private void dateUpdate(String id, Date date){
        if (id != null){
            Product product = itemsRepo.findByid(id);
            product.setUpdateDate(date);
            if (product.getParentId() != null){
                dateUpdate(product.getParentId(), date);
            }
        }

    }

    @DeleteMapping(path = "/delete/{id}")
    @Transactional
    public ResponseEntity delete(@PathVariable("id") String id) throws JsonProcessingException {
        if (!isValidUUID(id)){
            String body = mapper.writeValueAsString(new ErrorResponseJson(404, "Validation Failed"));
            return ResponseEntity.status(400).contentType(MediaType.APPLICATION_JSON).body(body);
        }
        if (itemsRepo.findByid(id) == null){
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
        if (!isValidUUID(id)){
            String body = mapper.writeValueAsString(new ErrorResponseJson(404, "Validation Failed"));
            return ResponseEntity.status(400).contentType(MediaType.APPLICATION_JSON).body(body);
        }
        if (itemsRepo.findByid(id) == null){
            String body = mapper.writeValueAsString(new ErrorResponseJson(404, "Item not found"));
            return ResponseEntity.status(404).contentType(MediaType.APPLICATION_JSON).body(body);
        }
        String response = mapper.writeValueAsString(avgPrice(id).stringJson);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(response);

    }

    private HelpClass avgPrice(String id) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        Product pr = itemsRepo.findByid(id);
        List<Product> productList = itemsRepo.findByparentId(id);
        int sum = 0;
        int countItems = 0;
        if (productList.size() != 0){
            AVGPriceJson[] avgPriceJsons = new AVGPriceJson[productList.size()];
            int i = 0;
            for (Product product : productList){
                if (product.getType().equals("CATEGORY")){
                    HelpClass helpClass = avgPrice(product.getId());
                    sum += helpClass.sumItems;
                    avgPriceJsons[i] = helpClass.stringJson;
                    i++;
                    countItems += helpClass.countItems;
                }else {
                    sum += product.getPrice();
                    String type = product.getType();
                    String name = product.getName();
                    String idProd = product.getId();
                    int price = product.getPrice();
                    String parentId = product.getParentId();
                    String date = sdf.format(product.getUpdateDate());
                    avgPriceJsons[i] = new AVGPriceJson(type, name, idProd, price, parentId, date, null);
                    i++;
                    countItems++;
                }
            }
            int avgPrice = sum/countItems;
            AVGPriceJson avgPriceJson = new AVGPriceJson(pr.getType(), pr.getName(), pr.getId(), avgPrice, pr.getParentId(), sdf.format(pr.getUpdateDate()), avgPriceJsons);
            return new HelpClass(avgPriceJson, sum, countItems);
        }else {
            AVGPriceJson avgPriceJson = new AVGPriceJson(pr.getType(), pr.getName(), pr.getId(), pr.getPrice(), pr.getParentId(), sdf.format(pr.getUpdateDate()), null);
            return new HelpClass(avgPriceJson, itemsRepo.findByid(id).getPrice(), 1);
        }
    }

    private static class HelpClass{
        AVGPriceJson stringJson;
        int sumItems;
        int countItems;

        public HelpClass(AVGPriceJson stringJson, int sumItems, int countItems) {
            this.stringJson = stringJson;
            this.sumItems = sumItems;
            this.countItems = countItems;
        }
    }

    public static boolean isValidUUID(String uuid) {
        // проверка UUID
        String regex = "^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$";
        if (uuid.matches(regex)) {
            return true;
        }
        return false;
    }
}