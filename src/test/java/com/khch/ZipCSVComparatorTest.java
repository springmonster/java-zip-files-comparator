package com.khch;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;

/**
 * Test cases: 1. target 和 baseline 都是空
 * <p>
 * 2. target 和 baseline 都不为空，不存在重复数据，且相同 3. target 和 baseline 都不为空，不存在重复数据，且不相同
 * <p>
 * 4. target 和 baseline 都不为空，存在重复数据，且相同 5. target 和 baseline 都不为空，存在重复数据，且不相同
 * <p>
 * 6. target 不为空，baseline 为空 7. target 为空，baseline 不为空
 */
public class ZipCSVComparatorTest {

  private ZipCSVComparator zipCSVComparator;

  @Before
  public void setUp() {
    zipCSVComparator = new ZipCSVComparator();
  }

  @Test
  public void testCompareEmpty() {
    Map<String, Integer> map = new HashMap<>();
    List<String> list = new ArrayList<>();
    boolean compare = zipCSVComparator.compare(map, list);
    assertTrue("Empty files are same", compare);
  }

  @Test
  public void testCompareSame() {
    Map<String, Integer> map = new HashMap<>();
    map.put("a", 1);
    map.put("b", 1);
    map.put("c", 1);

    List<String> list = new ArrayList<>();
    list.add("a");
    list.add("b");
    list.add("c");

    boolean compare = zipCSVComparator.compare(map, list);
    assertTrue("Not empty files are same", compare);
  }

  @Test
  public void testCompareTargetIsMoreThanBaseline() {
    Map<String, Integer> map = new HashMap<>();
    map.put("a", 1);
    map.put("b", 1);

    List<String> list = new ArrayList<>();
    list.add("a");
    list.add("b");
    list.add("c");

    boolean compare = zipCSVComparator.compare(map, list);
    assertFalse("Files are not same", compare);
  }

  @Test
  public void testCompareBaselineIsMoreThanTarget() {
    Map<String, Integer> map = new HashMap<>();
    map.put("a", 1);
    map.put("b", 1);
    map.put("c", 1);

    List<String> list = new ArrayList<>();
    list.add("a");
    list.add("b");

    boolean compare = zipCSVComparator.compare(map, list);
    assertFalse("Files are not same", compare);
  }

  @Test
  public void testCompareSameWithDuplicatedContents() {
    Map<String, Integer> map = new HashMap<>();
    map.put("a", 2);
    map.put("b", 2);
    map.put("c", 2);

    List<String> list = new ArrayList<>();
    list.add("a");
    list.add("b");
    list.add("c");
    list.add("a");
    list.add("b");
    list.add("c");

    boolean compare = zipCSVComparator.compare(map, list);
    assertTrue("Not empty files are same", compare);
  }

  @Test
  public void testCompareTargetIsMoreThanBaselineWithDuplicatedContents() {
    Map<String, Integer> map = new HashMap<>();
    map.put("a", 1);
    map.put("b", 2);
    map.put("c", 2);

    List<String> list = new ArrayList<>();
    list.add("a");
    list.add("b");
    list.add("c");
    list.add("a");
    list.add("b");
    list.add("c");

    boolean compare = zipCSVComparator.compare(map, list);
    assertFalse("Files are not same", compare);
  }

  @Test
  public void testCompareBaselineIsMoreThanTargetWithDuplicatedContents() {
    Map<String, Integer> map = new HashMap<>();
    map.put("a", 2);
    map.put("b", 2);
    map.put("c", 2);

    List<String> list = new ArrayList<>();
    list.add("a");
    list.add("b");
    list.add("c");
    list.add("a");
    list.add("b");

    boolean compare = zipCSVComparator.compare(map, list);
    assertFalse("Files are not same", compare);
  }

  @Test
  public void testCompareTargetIsEmptyAndBaselineIsNotEmpty() {
    Map<String, Integer> map = new HashMap<>();
    map.put("a", 1);
    map.put("b", 1);
    map.put("c", 1);

    List<String> list = new ArrayList<>();

    boolean compare = zipCSVComparator.compare(map, list);
    assertFalse("Files are not same", compare);
  }

  @Test
  public void testCompareTargetIsNotEmptyAndBaselineIsEmpty() {
    Map<String, Integer> map = new HashMap<>();

    List<String> list = new ArrayList<>();
    list.add("a");
    list.add("b");
    list.add("c");

    boolean compare = zipCSVComparator.compare(map, list);
    assertFalse("Files are not same", compare);
  }
}
