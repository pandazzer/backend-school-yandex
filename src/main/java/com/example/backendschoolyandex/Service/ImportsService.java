package com.example.backendschoolyandex.Service;

import com.example.backendschoolyandex.Entities.Product;
import com.example.backendschoolyandex.Json.ItemsJson;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public class ImportsService {

    public ResponseEntity getResopnseForImports(String json) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ItemsJson itemsJson = mapper.readValue(json, ItemsJson.class);
        List<ItemsJson.Items> itemsList = itemsJson.getItems();
        for (ItemsJson.Items x : itemsList){
          //  Product product = new Product(x.getId(), x.getName(), x.getParentId(), x.getPrice(), x.getType(), itemsJson.getUpdateDate());
        }
        return ResponseEntity.ok().body(json);
    }
}
