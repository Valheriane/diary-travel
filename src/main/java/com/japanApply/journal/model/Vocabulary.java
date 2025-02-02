package com.japanApply.journal.model;

import jakarta.persistence.*;
import java.util.Set;

@Entity
@Table(name = "vocabulary")
public class Vocabulary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String expression;
    private String translation;
    private String usageExample;

    @Enumerated(EnumType.STRING)
    private Language languageExpression;

    @Enumerated(EnumType.STRING)
    private Language languageTranslation;

    @ManyToMany(mappedBy = "vocabularies")
    private Set<DiaryEntry> diaryEntries;

    public Vocabulary() {}

    public Vocabulary(Long id, String expression, String translation, String usageExample, Language languageExpression, Language languageTranslation, Set<DiaryEntry> diaryEntries) {
        this.id = id;
        this.expression = expression;
        this.translation = translation;
        this.usageExample = usageExample;
        this.languageExpression = languageExpression;
        this.languageTranslation = languageTranslation;
        this.diaryEntries = diaryEntries;
    }


    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getExpression() { return expression; }
    public void setExpression(String expression) { this.expression = expression; }

    public String getTranslation() { return translation; }
    public void setTranslation(String translation) { this.translation = translation; }

    public String getUsageExample() { return usageExample; }
    public void setUsageExample(String usageExample) { this.usageExample = usageExample; }

    public Language getLanguageExpression() { return languageExpression; }
    public void setLanguageExpression(Language languageExpression) { this.languageExpression = languageExpression; }

    public Language getLanguageTranslation() { return languageTranslation; }
    public void setLanguageTranslation(Language languageTranslation) { this.languageTranslation = languageTranslation; }

    public Set<DiaryEntry> getDiaryEntries() { return diaryEntries; }
    public void setDiaryEntries(Set<DiaryEntry> diaryEntries) { this.diaryEntries = diaryEntries; }
}
