

/* First created by JCasGen Fri Sep 25 17:09:18 EDT 2015 */
package type;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;



import org.apache.uima.jcas.cas.StringList;


/** Tokenized Question annoatation. Extends type.Question. Additional feature: tokenizedText
 * Updated by JCasGen Sat Sep 26 18:54:31 EDT 2015
 * XML source: /Users/zhuyund/Documents/workspace/pi4-zhuyund/src/main/resources/descriptors/typeSystem.xml
 * @generated */
public class TokennizedQuestion extends Question {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(TokennizedQuestion.class);
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int type = typeIndexID;
  /** @generated
   * @return index of the type  
   */
  @Override
  public              int getTypeIndexID() {return typeIndexID;}
 
  /** Never called.  Disable default constructor
   * @generated */
  protected TokennizedQuestion() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated
   * @param addr low level Feature Structure reference
   * @param type the type of this Feature Structure 
   */
  public TokennizedQuestion(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated
   * @param jcas JCas to which this Feature Structure belongs 
   */
  public TokennizedQuestion(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated
   * @param jcas JCas to which this Feature Structure belongs
   * @param begin offset to the begin spot in the SofA
   * @param end offset to the end spot in the SofA 
  */  
  public TokennizedQuestion(JCas jcas, int begin, int end) {
    super(jcas);
    setBegin(begin);
    setEnd(end);
    readObject();
  }   

  /** 
   * <!-- begin-user-doc -->
   * Write your own initialization here
   * <!-- end-user-doc -->
   *
   * @generated modifiable 
   */
  private void readObject() {/*default - does nothing empty block */}
     
 
    
  //*--------------*
  //* Feature: tokens

  /** getter for tokens - gets tonnized text. Tokens are joined by ' '
   * @generated
   * @return value of the feature 
   */
  public StringList getTokens() {
    if (TokennizedQuestion_Type.featOkTst && ((TokennizedQuestion_Type)jcasType).casFeat_tokens == null)
      jcasType.jcas.throwFeatMissing("tokens", "type.TokennizedQuestion");
    return (StringList)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((TokennizedQuestion_Type)jcasType).casFeatCode_tokens)));}
    
  /** setter for tokens - sets tonnized text. Tokens are joined by ' ' 
   * @generated
   * @param v value to set into the feature 
   */
  public void setTokens(StringList v) {
    if (TokennizedQuestion_Type.featOkTst && ((TokennizedQuestion_Type)jcasType).casFeat_tokens == null)
      jcasType.jcas.throwFeatMissing("tokens", "type.TokennizedQuestion");
    jcasType.ll_cas.ll_setRefValue(addr, ((TokennizedQuestion_Type)jcasType).casFeatCode_tokens, jcasType.ll_cas.ll_getFSRef(v));}    
  }

    