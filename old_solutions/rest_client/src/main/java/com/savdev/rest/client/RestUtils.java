package com.savdev.rest.client;

import java.math.BigInteger;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class RestUtils {
  private static final BigInteger LIMIT = BigInteger.valueOf(100);

  public interface PagedResponse<T> {

    List<T> items();

    BigInteger totalNumber();
  }

  public static <T> PagedResponse<T> instance(
    List<T> items,
    BigInteger totalNumber){
    return new PagedResponse<T>() {
      @Override
      public List<T> items() {
        return items;
      }

      @Override
      public BigInteger totalNumber() {
        return totalNumber;
      }
    };
  }

  public static <T> Stream<T> streamOf(final BiFunction<BigInteger, BigInteger, PagedResponse<T>> pageResourceFunction) {

    PagedResponse<T> firstPage = pageResourceFunction.apply(BigInteger.ZERO, LIMIT);

    //it might happen that server ignores too small limit and returns more elements per page:
    BigInteger actualLimit = LIMIT.max(BigInteger.valueOf(firstPage.items().size()));

    BigInteger pagesNumber = numberOfPages(actualLimit, firstPage.totalNumber());

    return Stream.concat(
      firstPage.items().stream(),
      IntStream.range(1, pagesNumber.intValueExact())
        .mapToObj(currentPageNumber ->
          pageResourceFunction.apply(
            LIMIT.multiply(BigInteger.valueOf(currentPageNumber)), LIMIT))
        .map(PagedResponse::items)
        .flatMap(List::stream));
  }

  private static BigInteger numberOfPages(final BigInteger actualLimit, final BigInteger total) {
    return (total.remainder(actualLimit).equals(BigInteger.ZERO))
      ? total.divide(actualLimit)
      : total.divide(actualLimit).add(BigInteger.ONE);
  }
}
