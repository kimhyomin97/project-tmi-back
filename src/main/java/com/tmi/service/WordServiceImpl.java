package com.tmi.service;

import com.tmi.entity.Word;
import com.tmi.repository.WordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class WordServiceImpl implements WordService {

    private final WordRepository wordRepository;

    public Word getWord(Long id) {
        return wordRepository.findById(id).orElse(null);
    }

    public void createWord(Word word) {
        wordRepository.save(word);
    }
}
