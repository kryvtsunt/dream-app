package edu.neu.dreamapp.model;

import java.util.List;

/**
 * @author agrawroh
 * @version v1.0
 */
public class SurveyQuestion {

    /**
     * question : question
     * option : {"one":"one","two":"two","three":"three","four":"four"}
     * selected : 3
     */

    private String question;
    private String subQuestion;
    private List<String> option;
    private String selected;

    public SurveyQuestion(String question, String subQuestion, List<String> option) {
        this.question = question;
        this.subQuestion = subQuestion;
        this.option = option;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setSubQuestion(String subQuestion) {
        this.subQuestion = subQuestion;
    }

    public String getSubQuestion() {
        return subQuestion;
    }

    public List<String> getOption() {
        return option;
    }

    public void setOption(List<String> option) {
        this.option = option;
    }

    public String getSelected() {
        return selected;
    }

    public void setSelected(String selected) {
        this.selected = selected;
    }

    public static class OptionBean {
        /**
         * one : one
         * two : two
         * three : three
         * four : four
         */

        private String one;
        private String two;
        private String three;
        private String four;
        private String five;

        public OptionBean(String one, String two, String three, String four, String five) {
            this.one = one;
            this.two = two;
            this.three = three;
            this.four = four;
            this.five = five;
        }

        public String getOne() {
            return one;
        }

        public void setOne(String one) {
            this.one = one;
        }

        public String getTwo() {
            return two;
        }

        public void setTwo(String two) {
            this.two = two;
        }

        public String getThree() {
            return three;
        }

        public void setThree(String three) {
            this.three = three;
        }

        public String getFour() {
            return four;
        }

        public void setFour(String four) {
            this.four = four;
        }

        public void setFive(String five) {
            this.five = five;
        }

        public String getFive() {
            return five;
        }
    }
}
