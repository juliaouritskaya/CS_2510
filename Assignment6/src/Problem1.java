
/*
import java.util.Comparator;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

import tester.Tester;

class Movie {
  String title;
  double rating;

  Movie(String title, double rating) {
    this.title = title;
    this.rating = rating;
  }
}

// list
interface IList<T> {
  <U> U fold(BiFunction<T, U, U> converter, U base);

  <U> IList<U> map(Function<T, U> converter);

  IList<T> filter(Predicate<T> pred);
}

class MtList<T> implements IList<T> {
  MtList() {
  }

  public <U> U fold(BiFunction<T, U, U> converter, U base) {
    return base;
  }

  public <U> IList<U> map(Function<T, U> converter) {
    return new MtList<U>();
  }

  public IList<T> filter(Predicate<T> pred) {
    return this;
  }
}

class ConsList<T> implements IList<T> {
  T first;
  IList<T> rest;

  ConsList(T first, IList<T> rest) {
    this.first = first;
    this.rest = rest;
  }

  // Bifunction takes in one of type T and another of type U and merges them
  public <U> U fold(BiFunction<T, U, U> converter, U base) {
    return converter.apply(this.first, this.rest.fold(converter, base));
  }

  public <U> IList<U> map(Function<T, U> converter) {
    return new ConsList<U>(converter.apply(this.first), this.rest.map(converter));
  }

  public IList<T> filter(Predicate<T> pred) {
    if (pred.test(this.first)) {
      // should be included
      return new ConsList<T>(this.first, this.rest.filter(pred));
    }
    else {
      // should not be included
      return this.rest.filter(pred);
    }
  }
}

class SumRatings implements BiFunction<Movie, Double, Double> {

  public Double apply(Movie m, Double acc) {
    return m.rating + acc;
  }
}

class ScaleRatings implements Function<Movie, Movie> {

  public Movie apply(Movie m) {
    return new Movie(m.title, m.rating * 10);
  }
}

class GoodMovies implements Predicate<Movie> {

  public boolean test(Movie m) {
    return m.rating >= 5.0;
  }
}

class MovieByAlpha implements Comparator<Movie> {

  public int compare(Movie m1, Movie m2) {
    return m1.title.compareTo(m2.title);
  }
  
}

class ExamplesList {
  ExamplesList() {
  }

  Movie theMask = new Movie("The Mask", 9.0);
  Movie moana = new Movie("Moana", 5.0);
  Movie emojiMovie = new Movie("Emoji Movie", -1.0);

  IList<Movie> movies = new ConsList<Movie>(theMask,
      new ConsList<Movie>(moana, new ConsList<Movie>(emojiMovie, new MtList<Movie>())));
  IList<Movie> goodMovies = new ConsList<Movie>(theMask,
      new ConsList<Movie>(moana, new MtList<Movie>()));

  boolean testSumRatings(Tester t) {
    return t.checkInexact(movies.fold(new SumRatings(), 0.0), 13.0, 0.001);
  }

  boolean testScaleRatings(Tester t) {
    return t.checkInexact(new ScaleRatings().apply(this.theMask).rating, 90.0, 0.001);
  }

  boolean testGoodMovies(Tester t) {
    return t.checkExpect(this.movies.filter(new GoodMovies()), this.goodMovies);
  }
  
  boolean testMoviesByAlpha(Tester t) {
    return 
        t.checkExpect(new MovieByAlpha().compare(this.moana, this.theMask) < 0, true);
  }
}
*/
