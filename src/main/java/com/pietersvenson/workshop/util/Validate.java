/*
 * MIT License
 *
 * Copyright (c) 2020 Pieter Svenson
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package com.pietersvenson.workshop.util;

import java.util.regex.Pattern;

/**
 * Utility class for validating input.
 */
public final class Validate {

  private Validate() {
  }

  public static boolean isKebabCase(String s) {
    return !Pattern.compile(".*[^a-z\\-].*").matcher(s).find();
  }

  /**
   * Ensures that the input is in the kebab case format.
   *
   * @param s            the input string
   * @param errorMessage the error message to throw if fails
   * @throws IllegalArgumentException the exception to throw if fails
   */
  public static void checkKebabCase(String s, String errorMessage) throws IllegalArgumentException {
    if (!isKebabCase(s)) {
      throw new IllegalArgumentException(errorMessage);
    }
  }

  public static boolean isName(String s) {
    return !Pattern.compile(".*[^a-zA-Z\\-].*").matcher(s).find();
  }

  public static boolean isAlphaNumeric(String s) {
    return !Pattern.compile(".*[^a-zA-Z0-9].*").matcher(s).find();
  }

  /**
   * Ensures that the input is in the kebab case format, but also allows periods.
   *
   * @param s            the input string
   * @param errorMessage the error message to throw if fails
   * @throws IllegalArgumentException the exception to throw if fails
   */
  public static void checkConfigFormat(String s, String errorMessage)
      throws IllegalArgumentException {
    if (Pattern.compile(".*[^a-z\\-\\.].*").matcher(s).find()) {
      throw new IllegalArgumentException(errorMessage);
    }
  }

}