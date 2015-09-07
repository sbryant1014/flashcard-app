package ui.questions;

import engine.ListFilter;
import engine.ListSorter;
import java.util.ArrayList;
import java.util.Observable;
import models.DatabaseIO;
import models.Question;

/**
 * Class which captures state of a list of questions.
 * This includes keeping track of current question index, total number of questions, etc.
 */
public class QuestionList extends Observable {
  
  public static class QuestionListException extends Exception {
    public QuestionListException(String str) {
      super(str);
    }
  }
  
  public static class OutOfQuestionsException extends QuestionListException {
    public OutOfQuestionsException(String str) {
      super(str);
    }
  }

  public static class NotStartedYetException extends RuntimeException {
    public NotStartedYetException(String str) {
      super(str);
    }
  }
  
  public static class NoQuestionsException extends QuestionListException {
    public NoQuestionsException(String str) {
      super(str);
    }
  }
  
  public static enum State {
    NOT_STARTED, STARTED;
  }
  
  
  private QuestionState _questionState;
  private ArrayList<Question> _questionList;
  private ListFilter _listFilter;
  private ListSorter _listSorter;
  private Integer _currentIndex;
  private Integer _totalNumber;
  private State _state;
    
  public QuestionList(ListFilter filter, ListSorter sorter) {
    this._listFilter = filter;
    this._listSorter = sorter;
    this._state = State.NOT_STARTED;
    this._questionState = new QuestionState(this);
    this.addObserver(this._questionState);
    _resetList();
  }
  
  private void _resetList() {
    this._questionList = DatabaseIO.getDatabase().getQuestions(_listFilter, _listSorter);
    this._currentIndex = null;
    this._totalNumber = this._questionList.size();
    this._state = State.NOT_STARTED;
    this.setChanged();
    this.notifyObservers();
  }
    
  public void setFilterSorter(ListFilter filter, ListSorter sorter) {
    this._listFilter = filter;
    this._listSorter = sorter;
    this._resetList();
  }
  
  public Boolean isStarted() {
    return this._state == State.STARTED;
  }
  
  public QuestionState getQuestionState() {
    return this._questionState;
  }
  
  public Integer getNumberOfQuestions() {
    return this._totalNumber;
  }
  
  public Integer getCurrentIndex() throws NotStartedYetException {
    if (this._state != State.STARTED) {
      throw new NotStartedYetException("Question list not started yet");
    } else {
      return this._currentIndex;
    }
  }
  
  public Question getCurrentQuestion() throws NotStartedYetException {
    if (this._state != State.STARTED) {
      throw new NotStartedYetException("Question list not started yet");
    } else {
      return this._questionList.get(this._currentIndex);
    }
  }
  
  public Boolean hasNextQuestion() {
    if (this._state != State.STARTED) {
      return this._totalNumber > 0;
    } else {
      return this._currentIndex + 1 < this._totalNumber;
    }
  }
  
  public Boolean hasLastQuestion() {
    if (this._state != State.STARTED) {
      return false;
    } else {
      return this._currentIndex > 0;
    }
  }
  
  public void nextQuestion() throws OutOfQuestionsException {
    if (this._state == State.NOT_STARTED) {
      this._currentIndex = -1;
      this._state = State.STARTED;
      this.setChanged();
    }
    if (this.hasNextQuestion()) {
      this._currentIndex++;
      this.setChanged();
      this.notifyObservers();
    } else {
      this.notifyObservers();
      throw new OutOfQuestionsException("No more questions (total: " + this._totalNumber + ")");
    }
  }
  
  public void lastQuestion() throws OutOfQuestionsException, NotStartedYetException {
    if (this._state == State.NOT_STARTED) {
      throw new NotStartedYetException("Cannot go back when quiz not started");
    }
    if (this.hasLastQuestion()) {
      this._currentIndex--;
      this.setChanged();
      this.notifyObservers();
    } else {
      throw new OutOfQuestionsException("No more questions (total: " + this._totalNumber + ")");
    }
  }
  
  void initialUpdate() {
    this.setChanged();
    this.notifyObservers();
  }
  
}