package com.tmi.controller;

import com.tmi.dto.ResponseDto;
import com.tmi.entity.Word;
import com.tmi.service.WordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/words")
@RequiredArgsConstructor
@Slf4j
public class WordController {

    private final WordService wordService;

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto<Word>> getWord(@PathVariable(required = true) Long id) {
        Word word = wordService.getWord(id);
        ResponseDto<Word> responseDto = new ResponseDto<>();
        responseDto.setMessage("단어 조회 성공");
        responseDto.setData(word);
        return ResponseEntity.ok().body(responseDto);
    }

    @PostMapping
    public ResponseEntity<ResponseDto<Void>> createWord(@RequestBody Word word) {
        log.info("create word {}", word.getWord());
        wordService.createWord(word);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
