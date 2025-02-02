package com.japanApply.journal.dto.mapper;

import com.japanApply.journal.dto.VocabularyDto;
import com.japanApply.journal.dto.VocabularyResponseDto;
import com.japanApply.journal.model.Language;
import com.japanApply.journal.model.Vocabulary;

public class VocabularyMapper {

    public static Vocabulary toVocabulary(VocabularyDto vocabularyDto) {
        Vocabulary vocabulary = new Vocabulary();

        vocabulary.setExpression(vocabularyDto.expression());
        vocabulary.setTranslation(vocabularyDto.translation());
        vocabulary.setUsageExample(vocabularyDto.usageExample());

        try {
            vocabulary.setLanguageExpression(Language.valueOf(vocabularyDto.languageExpression().toUpperCase()));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid vocabulary Language: " + vocabularyDto.languageExpression());
        }
        try {
            vocabulary.setLanguageTranslation(Language.valueOf(vocabularyDto.languageTranslation().toUpperCase()));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid vocabulary Language: " + vocabularyDto.languageTranslation());
        }

        return vocabulary;
    }

    public static VocabularyResponseDto toVocabularyResponseDto(Vocabulary vocabulary) {
        return new VocabularyResponseDto(
                vocabulary.getId(),
                vocabulary.getExpression(),
                vocabulary.getTranslation(),
                vocabulary.getUsageExample(),
                vocabulary.getLanguageExpression().name(),
                vocabulary.getLanguageTranslation().name()
        );
    }
}
