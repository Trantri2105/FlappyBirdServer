package com.example.flappybird.controller;

import com.example.flappybird.model.Score;
import com.example.flappybird.repository.ScoreRepository;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class ScoreController {
    private ScoreRepository scoreRepository;

    @PostMapping("/score")
    public ResponseEntity<String> saveScore(@RequestBody Score score) {
        score.setCreatedAt(new Date());
        try {
            scoreRepository.save(score);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error saving score", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>("Saved score", HttpStatus.OK);
    }

    @GetMapping("score/history")
    public ResponseEntity<List<Score>> getScoreHistory(@RequestParam Integer userId) {
        if (userId == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        List<Score> res;
        try {
            res = scoreRepository.findScoresByUserId(userId, Pageable.ofSize(6));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
        return new ResponseEntity<>(res, HttpStatus.OK);
    }
}
