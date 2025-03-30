package com.tmi.service;

import com.tmi.entity.Word;

public interface WordService {
    public Word getWord(Long id);

    public void createWord(Word word);
}
