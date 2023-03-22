package com.dlb.chess.common;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.dlb.chess.common.exceptions.ProgrammingMistakeException;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class NonNullWrapperCommon {

  private NonNullWrapperCommon() {
  }

  @NonNull
  private static <E> E checkResult(@Nullable E result) {
    if (result == null) {
      throw new ProgrammingMistakeException("Assumed value is not null");
    }
    return result;
  }

  public static String getName(File file) {
    return checkResult(file.getName());
  }

  public static String getAbsolutePath(File file) {
    return checkResult(file.getAbsolutePath());
  }

  public static String nextLine(Scanner myReader) {
    return checkResult(myReader.nextLine());
  }

  public static String toString(StringBuilder stringBuilder) {
    return checkResult(stringBuilder.toString());
  }

  public static String toString(char c) {
    return checkResult(Character.toString(c));
  }

  public static String substring(String string, int beginIndex) {
    return checkResult(string.substring(beginIndex));
  }

  public static String substring(String string, int beginIndex, int endIndex) {
    return checkResult(string.substring(beginIndex, endIndex));
  }

  public static String replace(String string, String oldString, String newString) {
    return checkResult(string.replace(oldString, newString));
  }

  public static String replaceAll(String string, String regex, String replacement) {
    return checkResult(string.replaceAll(regex, replacement));
  }

  public static String toLowerCase(String string) {
    return checkResult(string.toLowerCase());
  }

  public static String toUpperCase(String string) {
    return checkResult(string.toUpperCase());
  }

  public static String valueOf(double doubleValue) {
    return checkResult(String.valueOf(doubleValue));
  }

  public static String valueOf(int integerValue) {
    return checkResult(String.valueOf(integerValue));
  }

  public static String format(String string, Object... args) {
    return checkResult(String.format(string, args));
  }

  public static String padEnd(String string, int minLength, char padChar) {
    return checkResult(Strings.padEnd(string, minLength, padChar));
  }

  public static String join(CharSequence delimiter, Iterable<? extends CharSequence> elements) {
    return checkResult(String.join(delimiter, elements));
  }

  public static String normalizeSpace(String str) {
    return checkResult(StringUtils.normalizeSpace(str));
  }

  public static String trim(String str) {
    return checkResult(str.trim());
  }

  public static String stripLeading(String str) {
    return checkResult(str.stripLeading());
  }

  public static String stripTrailing(String str) {
    return checkResult(str.stripTrailing());
  }

  public static String substringBefore(String str, String separator) {
    return checkResult(StringUtils.substringBefore(str, separator));
  }

  public static String substringAfter(String str, String separator) {
    return checkResult(StringUtils.substringAfter(str, separator));
  }

  public static String removeEnd(final String str, final String remove) {
    return checkResult(StringUtils.removeEnd(str, remove));
  }

  @SuppressWarnings("null")
  @NonNull
  public static String[] split(String str, String regex) {
    return checkResult(str.split(regex));
  }

  public static Logger getLogger(Class<?> theClass) {
    return checkResult(LogManager.getLogger(theClass));
  }

  public static <E extends Enum<E>> String name(E enumTyp) {
    return checkResult(enumTyp.name());
  }

  @NonNull
  public static <E, F> F get(Map<E, F> map, E key) {
    return checkResult(map.get(key));
  }

  @NonNull
  public static <E> E get(List<E> list, int index) {
    return checkResult(list.get(index));
  }

  @NonNull
  public static <E> E getFirst(List<E> list) {
    return checkResult(list.get(0));
  }

  @NonNull
  public static <E> E getLast(List<E> list) {
    return checkResult(list.get(list.size() - 1));
  }

  @NonNull
  public static <E> E getLast(E[] list) {
    return checkResult(list[list.length - 1]);
  }

  public static <T> List<T> unmodifiableList(List<? extends T> list) {
    return checkResult(Collections.unmodifiableList(list));
  }

  public static <E extends Enum<E>> EnumSet<E> newEnumSet(Iterable<E> iterable, Class<E> elementType) {
    return checkResult(Sets.newEnumSet(iterable, elementType));
  }

  public static <K extends Enum<K>, V> EnumMap<K, V> newEnumMap(Class<K> type) {
    return checkResult(Maps.newEnumMap(type));
  }

  public static <K extends Enum<K>, V> ImmutableMap<K, V> immutableEnumMap(Map<K, ? extends V> map) {
    return checkResult(Maps.immutableEnumMap(map));
  }

  public static <E> ImmutableList<E> copyOfList(Iterable<? extends E> elements) {
    return checkResult(ImmutableList.copyOf(elements));
  }

  public static <E> ImmutableSet<E> copyOfSet(Collection<? extends E> elements) {
    return checkResult(ImmutableSet.copyOf(elements));
  }

  public static <K, V> ImmutableMap<K, V> copyOfMap(Map<? extends K, ? extends V> map) {
    return checkResult(ImmutableMap.copyOf(map));
  }

  @SuppressWarnings({ "unchecked" })
  public static <T> List<T> asList(T... a) {
    return checkResult(Arrays.asList(a));
  }

  @SuppressWarnings("null")
  public static <E> List<E> subList(List<E> list, int fromIndex, int toIndex) {
    return list.subList(fromIndex, toIndex);
  }

}
