package com.example.flappybird.controller;

import com.example.flappybird.dto.MaxScoreDTO;
import com.example.flappybird.response.ScoreResponse;
import com.example.flappybird.repository.ScoreRepository;
import com.example.flappybird.model.Score;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class RankingController {

    @Autowired
    private ScoreRepository scoreRepository;

    @GetMapping("/ranking")
    public ResponseEntity<Map<String, Object>> getRanking(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        List<MaxScoreDTO> topScores = scoreRepository.findTopScores(pageable);

        // Chuyển đổi kết quả thành danh sách `ScoreResponse`
        List<ScoreResponse> scoreResponses = topScores.stream().map(score -> new ScoreResponse(
                score.getUserId(),
                scoreRepository.findUsernameByUserId(score.getUserId()),
                score.getMaxScore(),
                score.getCreatedAt()
        )).collect(Collectors.toList());

        // Đóng gói dữ liệu vào Map để trả về thông tin phân trang
        Map<String, Object> response = new HashMap<>();
        response.put("scores", scoreResponses);
        response.put("currentPage", page + 1);
        response.put("totalItems", scoreResponses.size()); // tổng số điểm cao nhất của người dùng
        response.put("totalPages", (int) Math.ceil((double) scoreResponses.size() / size) + 1);

        return ResponseEntity.ok(response);
    }
}
