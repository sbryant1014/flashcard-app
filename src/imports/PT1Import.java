package imports;

import core.Constants;
import java.util.Map;
import models.Answer;
import models.Database;
import models.DatabaseIO;
import models.Question;
import models.Source;

/**
 *
 * @author sambryant
 */
public class PT1Import {

  static String SRC_DIR = "imports/pt1";

  /**
   *
   * @param args
   */
  public static void main(String[] args) {
    Constants.setupProjectDirectories();

    Database d = DatabaseIO.getQuestionDatabaseIO().get();

    Map<Integer, Answer> answers = ImportUtilities.readAnswerFile(SRC_DIR + "/answers.txt");
    Source source = Source.SAMPLE_1;

    for (int i = 1; i <= 100; i++) {
      Question q = new Question(source, i, answers.get(i), SRC_DIR + "/PT1 " + i + ".png");
      d.addQuestionToSession(q);
    }

    DatabaseIO.getQuestionDatabaseIO().save();
  }

}
