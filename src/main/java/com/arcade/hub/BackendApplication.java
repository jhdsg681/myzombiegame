package com.arcade.hub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@SpringBootApplication
public class BackendApplication {
    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }
}

class SharedDatabase {
    public static final List<Map<String, Object>> announcements = new CopyOnWriteArrayList<>();
    public static final List<Map<String, Object>> games = new CopyOnWriteArrayList<>();
}

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*") 
class ApiController {

    @GetMapping("/data")
    public Map<String, Object> getAllData() {
        Map<String, Object> response = new HashMap<>();
        response.put("announcements", SharedDatabase.announcements);
        response.put("customGames", SharedDatabase.games);
        return response;
    }

    @PostMapping("/announcements")
    public Map<String, Object> addAnnouncement(@RequestBody Map<String, String> payload) {
        Map<String, Object> newItem = new ConcurrentHashMap<>();
        newItem.put("id", System.currentTimeMillis());
        newItem.put("text", payload.get("text"));
        SharedDatabase.announcements.add(newItem);
        return Collections.singletonMap("success", true);
    }

    @DeleteMapping("/announcements/{id}")
    public Map<String, Object> deleteAnnouncement(@PathVariable long id) {
        SharedDatabase.announcements.removeIf(a -> (long) a.get("id") == id);
        return Collections.singletonMap("success", true);
    }

    @PostMapping("/games")
    public Map<String, Object> addGame(@RequestBody Map<String, String> payload) {
        Map<String, Object> newGame = new ConcurrentHashMap<>();
        newGame.put("id", System.currentTimeMillis());
        newGame.put("title", payload.get("title"));
        newGame.put("code", payload.get("code"));
        SharedDatabase.games.add(newGame);
        return Collections.singletonMap("success", true);
    }

    @DeleteMapping("/games/{id}")
    public Map<String, Object> deleteGame(@PathVariable long id) {
        SharedDatabase.games.removeIf(g -> (long) g.get("id") == id);
        return Collections.singletonMap("success", true);
    }
}
