package com.example.ItemsBase.controller;

import com.example.ItemsBase.domain.Item;
import com.example.ItemsBase.domain.ItemRepository;
import com.example.ItemsBase.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

@RestController
public class ItemController {

    @Autowired
    private ItemRepository itemRepository;

    @GetMapping("/items")
    public Page<Item> getItems(Pageable pageable) {
        return itemRepository.findAll(pageable);
    }

    @PostMapping("/items")
    public Item createItem(@Valid @RequestBody Item item) {
        return itemRepository.save(item);
    }

    @PutMapping("/items/{itemId}")
    public Item updateItem(@PathVariable Long itemId,
                           @Valid @RequestBody Item itemNew) {
        return itemRepository.findById(itemId)
                .map(item -> {
                    item.setName(itemNew.getName());
                    item.setDescription(itemNew.getDescription());
                    item.setId(itemNew.getId());
                    item.setTags(itemNew.getTags());
                    return itemRepository.save(item);
                }).orElseThrow(() -> new ResourceNotFoundException("Item not found with id " + itemId));
    }

    @DeleteMapping("/items/{itemId}")
    public ResponseEntity<?> deletItem(@PathVariable Long itemId) {
        return itemRepository.findById(itemId)
                .map(item -> {
                    itemRepository.delete(item);
                    return ResponseEntity.ok().build();
                }).orElseThrow(() -> new ResourceNotFoundException("Item not found with id " + itemId));
    }
}
