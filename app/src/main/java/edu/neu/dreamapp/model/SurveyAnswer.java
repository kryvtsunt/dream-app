package edu.neu.dreamapp.model;

import java.util.List;

/**
 * @author agrawroh
 * @version v1.0
 */
public class SurveyAnswer {
    private String id;
    private List<SurveyQuestion> surveyQuestions;

    public SurveyAnswer() {

    }

    public SurveyAnswer(String id, List<SurveyQuestion> surveyQuestions) {
        this.id = id;
        this.surveyQuestions = surveyQuestions;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setSurveyQuestions(List<SurveyQuestion> surveyQuestions) {
        this.surveyQuestions = surveyQuestions;
    }

    public String getId() {
        return id;
    }

    public List<SurveyQuestion> getSurveyQuestions() {
        return surveyQuestions;
    }
}
