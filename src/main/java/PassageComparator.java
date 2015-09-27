import java.util.Comparator;
import type.Passage;
/**
 * A comparator for passages
 * Question Id in ascending order, passage score in descending order
 * Uses Template Pattern
 * @author zhuyund
 *
 */
public class PassageComparator implements Comparator<Passage> {

  @Override
  public int compare(Passage o1, Passage o2) {
    String qid1 = o1.getQuestion().getId();
    String qid2 = o2.getQuestion().getId();
    int tmp = qid1.compareTo(qid2);
    if(tmp != 0)
      return tmp; // question id in ascending order
    Double score1 = o1.getScore();
    Double score2 = o2.getScore();
    return score2.compareTo(score1); // score in descending order
  }

}
