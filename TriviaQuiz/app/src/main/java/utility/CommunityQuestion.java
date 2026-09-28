package utility;

import java.util.List;

public class CommunityQuestion {
    public String question;
    public String correctAnswer;
    public List<String> incorrectAnswers;

    public CommunityQuestion(String question, String correctAnswer,
                             List<String> incorrectAnswers) {
        this.question         = question;
        this.correctAnswer    = correctAnswer;
        this.incorrectAnswers = incorrectAnswers;
    }
}
