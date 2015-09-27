import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.uima.cas.TypeSystem;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.cas.EmptyFSList;
import org.apache.uima.jcas.cas.EmptyStringList;
import org.apache.uima.jcas.cas.FSList;
import org.apache.uima.jcas.cas.NonEmptyFSList;
import org.apache.uima.jcas.cas.NonEmptyStringList;
import org.apache.uima.jcas.cas.StringList;
import org.apache.uima.cas.Type;

import type.ComponentAnnotation;

/**
 * Bridges between FSList and java Collection
 * 
 * @author zhuyund
 *
 */
public class FSListFactory<T extends ComponentAnnotation> {

  /***
   * create FSList from java Collection
   * 
   * @param aJCas
   * @param aCollection
   * @return FSList
   */
  public static FSList createFSList(JCas aJCas,
          Collection<? extends ComponentAnnotation> aCollection) {
    if (aCollection.size() == 0) {
      return new EmptyFSList(aJCas);
    }

    NonEmptyFSList head = new NonEmptyFSList(aJCas);
    NonEmptyFSList list = head;
    Iterator<? extends ComponentAnnotation> i = aCollection.iterator();
    while (i.hasNext()) {
      head.setHead(i.next());
      if (i.hasNext()) {
        head.setTail(new NonEmptyFSList(aJCas));
        head = (NonEmptyFSList) head.getTail();
      } else {
        head.setTail(new EmptyFSList(aJCas));
      }
    }

    return list;
  }

  /**
   * cast FSList into java collection
   * 
   * @param aList
   * @param type
   * @return List
   */
  @SuppressWarnings("unchecked")
  public static <T extends ComponentAnnotation> Collection<T> createCollection(FSList aList,
          Type type) {
    TypeSystem ts = aList.getCAS().getTypeSystem();
    List<T> data = new ArrayList<T>();
    FSList i = aList;
    while (i instanceof NonEmptyFSList) {
      NonEmptyFSList l = (NonEmptyFSList) i;
      T value = (T) l.getHead();
      if (value != null && (type == null || ts.subsumes(type, value.getType()))) {
        data.add((T) l.getHead());
      }
      i = l.getTail();
    }
    // return asList(data.toArray(new ComponentAnnotation[data.size()]));
    return data;
  }
  
  /**
   * cast StringList into java collection
   * 
   * @param aList
   * @param type
   * @return List
   */
  public static Collection<String> createCollection(StringList strList,
          Type type) {
    List<String> data = new ArrayList<String>();
    StringList i = strList;
    while (i instanceof NonEmptyStringList) {
      NonEmptyStringList l = (NonEmptyStringList) i;
      String value = (String) l.getHead();
      if (value != null && (type == null )) {
        data.add((String) l.getHead());
      }
      i = l.getTail();
    }
    //return asList(data.toArray(new ComponentAnnotation[data.size()]));
    return data;
  }

  /***
   * create a uima StringList from java Collection
   * @param aJCas
   * @param stringCollection
   * @return
   */
  public static StringList createStringList(JCas aJCas, Collection<String> stringCollection) {
    if (stringCollection.size() == 0) {
      return new EmptyStringList(aJCas);
    }

    NonEmptyStringList head = new NonEmptyStringList(aJCas);
    NonEmptyStringList list = head;
    Iterator<String> i = stringCollection.iterator();
    while (i.hasNext()) {
      head.setHead(i.next());
      if (i.hasNext()) {
        head.setTail(new NonEmptyStringList(aJCas));
        head = (NonEmptyStringList) head.getTail();
      } else {
        head.setTail(new EmptyStringList(aJCas));
      }
    }
    return list;
  }

  
}
