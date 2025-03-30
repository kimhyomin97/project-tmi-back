package com.tmi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tmi.entity.Word;
import com.tmi.repository.WordRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class WordControllerTest {
    @Autowired
    WordRepository wordRepository;

    @Test
    public void testWordInput() throws Exception {
        File file = new File("src/test/resources/wordfreq.json");
        System.out.println("file exists : " + file.exists());

        ObjectMapper objectMapper = new ObjectMapper();
        List<Map<String, Object>> words = objectMapper.readValue(file, objectMapper.getTypeFactory().constructCollectionType(List.class, Map.class));

        for (Map<String, Object> wordData : words) {
            Object rankObject = wordData.get("rank");
            Long rank = (rankObject instanceof Number) ? ((Number) rankObject).longValue() : null;
            String lemma = (String) wordData.get("lemma");

            Word newWord = new Word();
            newWord.setFreRank(rank);
            newWord.setWord(lemma);

            wordRepository.save(newWord);
        }
    }
}
